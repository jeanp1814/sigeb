import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PersistenciaSIGEB {
    private final Path directorio;
    private final Path archivoUsuarios;
    private final Path archivoPolitica;

    public PersistenciaSIGEB(Path directorio) {
        this.directorio = directorio;
        archivoUsuarios = directorio.resolve("usuarios.txt");
        archivoPolitica = directorio.resolve("politica.txt");
    }

    public void cargar(Biblioteca biblioteca) throws IOException {
        ArrayList<Usuario> usuarios = leerUsuarios(biblioteca.getUsuarios());
        PoliticaPrestamo politica = leerPolitica();

        for (Usuario usuario : usuarios) {
            biblioteca.registrarUsuario(usuario);
        }
        if (politica != null) {
            biblioteca.getPolitica().actualizarReglas(politica.getDiasPrestamo(),
                    politica.getMaximoEjemplaresActivos(), politica.getPenalizacionPorDia(),
                    politica.getBloquearPorPenalizacion());
        }
    }

    public void guardar(Biblioteca biblioteca) throws IOException {
        if (Files.isDirectory(archivoUsuarios) || Files.isDirectory(archivoPolitica)) {
            throw new IOException("usuarios.txt y politica.txt deben ser archivos.");
        }

        Path usuariosTemporal = Files.createTempFile(directorio, "usuarios-", ".tmp");
        try {
            Path politicaTemporal = Files.createTempFile(directorio, "politica-", ".tmp");
            try {
                escribirUsuarios(usuariosTemporal, biblioteca.getUsuarios());
                escribirPolitica(politicaTemporal, biblioteca.getPolitica());
                reemplazarArchivo(usuariosTemporal, archivoUsuarios);
                reemplazarArchivo(politicaTemporal, archivoPolitica);
            } finally {
                Files.deleteIfExists(politicaTemporal);
            }
        } finally {
            Files.deleteIfExists(usuariosTemporal);
        }
    }

    private void reemplazarArchivo(Path temporal, Path destino) throws IOException {
        try {
            Files.move(temporal, destino, ATOMIC_MOVE, REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException error) {
            Files.move(temporal, destino, REPLACE_EXISTING);
        }
    }

    private ArrayList<Usuario> leerUsuarios(List<Usuario> existentes) throws IOException {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        if (Files.notExists(archivoUsuarios)) {
            return usuarios;
        }
        Set<Integer> ids = new HashSet<>();
        Set<String> documentos = new HashSet<>();
        for (Usuario usuario : existentes) {
            ids.add(usuario.getIdUsuario());
            documentos.add(usuario.getDocumento());
        }

        try (Scanner entrada = new Scanner(new FileReader(archivoUsuarios.toFile(), StandardCharsets.UTF_8))) {
            int numeroLinea = 0;
            while (entrada.hasNextLine()) {
                String linea = entrada.nextLine();
                numeroLinea++;
                try {
                    Usuario usuario = leerUsuario(linea);
                    if (!ids.add(usuario.getIdUsuario()) || !documentos.add(usuario.getDocumento())) {
                        throw new IllegalArgumentException("ID o documento repetido.");
                    }
                    usuarios.add(usuario);
                } catch (IllegalArgumentException error) {
                    throw new IOException("usuarios.txt, línea " + numeroLinea + ": " + error.getMessage(), error);
                }
            }
            if (entrada.ioException() != null) {
                throw entrada.ioException();
            }
        }
        return usuarios;
    }

    private Usuario leerUsuario(String linea) {
        String[] campos = linea.split("\\|", -1);
        if (campos.length != 8) {
            throw new IllegalArgumentException("Se esperaban ocho campos.");
        }
        int id = Integer.parseInt(campos[1]);
        String documento = desescapar(campos[2]);
        String nombres = desescapar(campos[3]);
        String apellidos = desescapar(campos[4]);
        String correo = desescapar(campos[5]);
        boolean activo = leerBooleano(campos[6]);
        String codigo = desescapar(campos[7]);

        Usuario usuario = switch (campos[0]) {
            case "LECTOR" -> new Lector(id, documento, nombres, apellidos, correo, codigo);
            case "ADMINISTRADOR" -> new Administrador(id, documento, nombres, apellidos, correo, codigo);
            default -> throw new IllegalArgumentException("Tipo de usuario inválido.");
        };
        if (!activo) {
            usuario.desactivar();
        }
        return usuario;
    }

    private PoliticaPrestamo leerPolitica() throws IOException {
        if (Files.notExists(archivoPolitica)) {
            return null;
        }
        try (Scanner entrada = new Scanner(new FileReader(archivoPolitica.toFile(), StandardCharsets.UTF_8))) {
            String linea = entrada.hasNextLine() ? entrada.nextLine() : "";
            boolean hayOtraLinea = entrada.hasNextLine();
            if (entrada.ioException() != null) {
                throw entrada.ioException();
            }
            String[] campos = linea.split("\\|", -1);
            if (campos.length != 4 || hayOtraLinea) {
                throw new IllegalArgumentException("Se esperaba una línea con cuatro campos.");
            }
            int dias = Integer.parseInt(campos[0]);
            int maximo = Integer.parseInt(campos[1]);
            BigDecimal tarifa = new BigDecimal(campos[2]);
            boolean bloquear = leerBooleano(campos[3]);
            if (dias <= 0 || maximo <= 0 || tarifa.signum() < 0) {
                throw new IllegalArgumentException("Días y máximo deben ser positivos; la tarifa debe ser mayor o igual a cero.");
            }
            return new PoliticaPrestamo(dias, maximo, tarifa, bloquear);
        } catch (IllegalArgumentException error) {
            throw new IOException("politica.txt: " + error.getMessage(), error);
        }
    }

    private void escribirUsuarios(Path archivo, List<Usuario> usuarios) throws IOException {
        try (PrintWriter salida = new PrintWriter(new FileWriter(archivo.toFile(), StandardCharsets.UTF_8))) {
            for (Usuario usuario : usuarios) {
                String tipo;
                String codigo;
                if (usuario instanceof Lector lector) {
                    tipo = "LECTOR";
                    codigo = lector.getCodigoLector();
                } else if (usuario instanceof Administrador administrador) {
                    tipo = "ADMINISTRADOR";
                    codigo = administrador.getCodigoAdministrador();
                } else {
                    throw new IOException("usuarios.txt: tipo de usuario sin formato de guardado.");
                }
                salida.println(String.join("|", tipo, String.valueOf(usuario.getIdUsuario()),
                        escapar(usuario.getDocumento()), escapar(usuario.getNombres()),
                        escapar(usuario.getApellidos()), escapar(usuario.getCorreo()),
                        String.valueOf(usuario.getActivo()), escapar(codigo)));
            }
            salida.close();
            if (salida.checkError()) {
                throw new IOException("Error al escribir usuarios.txt.");
            }
        }
    }

    private void escribirPolitica(Path archivo, PoliticaPrestamo politica) throws IOException {
        try (PrintWriter salida = new PrintWriter(new FileWriter(archivo.toFile(), StandardCharsets.UTF_8))) {
            salida.println(String.join("|", String.valueOf(politica.getDiasPrestamo()),
                    String.valueOf(politica.getMaximoEjemplaresActivos()),
                    politica.getPenalizacionPorDia().toString(),
                    String.valueOf(politica.getBloquearPorPenalizacion())));
            salida.close();
            if (salida.checkError()) {
                throw new IOException("Error al escribir politica.txt.");
            }
        }
    }

    private static boolean leerBooleano(String valor) {
        if (!valor.equals("true") && !valor.equals("false")) {
            throw new IllegalArgumentException("Valor booleano inválido.");
        }
        return Boolean.parseBoolean(valor);
    }

    private static String escapar(String texto) {
        return texto.replace("%", "%25").replace("|", "%7C")
                .replace("\r", "%0D").replace("\n", "%0A")
                .replace("\u0085", "%85").replace("\u2028", "%2028").replace("\u2029", "%2029");
    }

    private static String desescapar(String texto) {
        return texto.replace("%7C", "|").replace("%0D", "\r").replace("%0A", "\n")
                .replace("%85", "\u0085").replace("%2028", "\u2028").replace("%2029", "\u2029")
                .replace("%25", "%");
    }
}
