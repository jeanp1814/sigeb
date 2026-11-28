import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PersistenciaSIGEB {
    private static final String USUARIOS = "usuarios.txt";
    private static final String POLITICA = "politica.txt";
    private static final List<String> ARCHIVOS = List.of(USUARIOS, POLITICA,
            PersistenciaCatalogo.AUTORES, PersistenciaCatalogo.CATEGORIAS, PersistenciaCatalogo.LIBROS,
            PersistenciaCatalogo.EJEMPLARES, PersistenciaPrestamos.PRESTAMOS,
            PersistenciaPrestamos.DETALLES, PersistenciaPrestamos.PENALIZACIONES);

    private final Path directorio;

    public PersistenciaSIGEB(Path directorio) {
        this.directorio = directorio;
    }

    public void cargar(Biblioteca biblioteca) throws IOException {
        Biblioteca copia = biblioteca.copiaParaCarga();
        cargarDesde(directorio, copia);
        biblioteca.aplicarCarga(copia);
    }

    private static void cargarDesde(Path origen, Biblioteca biblioteca) throws IOException {
        cargarUsuarios(origen.resolve(USUARIOS), biblioteca);
        cargarPolitica(origen.resolve(POLITICA), biblioteca.getPolitica());
        PersistenciaCatalogo.cargar(origen, biblioteca);
        PersistenciaPrestamos.cargar(origen, biblioteca);
    }

    public void guardar(Biblioteca biblioteca) throws IOException {
        for (String nombre : ARCHIVOS) {
            if (Files.isDirectory(directorio.resolve(nombre))) {
                throw new IOException("Los destinos deben ser archivos: " + nombre + ".");
            }
        }

        Path temporal = Files.createTempDirectory(directorio, ".sigeb-");
        Path nuevos = temporal.resolve("nuevos");
        Path anteriores = temporal.resolve("anteriores");
        boolean conservarRespaldo = false;
        try {
            Files.createDirectory(nuevos);
            Files.createDirectory(anteriores);
            escribirUsuarios(nuevos.resolve(USUARIOS), biblioteca);
            escribirPolitica(nuevos.resolve(POLITICA), biblioteca.getPolitica());
            PersistenciaCatalogo.guardar(nuevos, biblioteca);
            PersistenciaPrestamos.guardar(nuevos, biblioteca);

            cargarDesde(nuevos, new Biblioteca(biblioteca.getNombre(),
                    new PoliticaPrestamo(7, 3, new BigDecimal("1.50"), true)));
            publicar(nuevos, anteriores);
        } catch (ErrorRecuperacion error) {
            conservarRespaldo = true;
            throw error;
        } finally {
            if (!conservarRespaldo) {
                for (String nombre : ARCHIVOS) {
                    Files.deleteIfExists(nuevos.resolve(nombre));
                    Files.deleteIfExists(anteriores.resolve(nombre));
                }
                Files.deleteIfExists(nuevos);
                Files.deleteIfExists(anteriores);
                Files.deleteIfExists(temporal);
            }
        }
    }

    private void publicar(Path nuevos, Path anteriores) throws IOException {
        Set<String> respaldados = new HashSet<>();
        for (String nombre : ARCHIVOS) {
            Path destino = directorio.resolve(nombre);
            if (!Files.notExists(destino)) {
                Files.copy(destino, anteriores.resolve(nombre));
                respaldados.add(nombre);
            }
        }

        List<String> actualizados = new ArrayList<>();
        try {
            for (String nombre : ARCHIVOS) {
                actualizados.add(nombre);
                mover(nuevos.resolve(nombre), directorio.resolve(nombre));
            }
        } catch (IOException original) {
            ErrorRecuperacion errorRecuperacion = null;
            for (int i = actualizados.size() - 1; i >= 0; i--) {
                String nombre = actualizados.get(i);
                Path destino = directorio.resolve(nombre);
                try {
                    if (respaldados.contains(nombre)) {
                        Files.copy(anteriores.resolve(nombre), destino, REPLACE_EXISTING);
                    } else {
                        Files.deleteIfExists(destino);
                    }
                } catch (IOException error) {
                    if (errorRecuperacion == null) {
                        errorRecuperacion = new ErrorRecuperacion(anteriores.toAbsolutePath(), original);
                    }
                    errorRecuperacion.addSuppressed(error);
                }
            }
            if (errorRecuperacion != null) {
                throw errorRecuperacion;
            }
            throw original;
        }
    }

    private static void mover(Path origen, Path destino) throws IOException {
        try {
            Files.move(origen, destino, ATOMIC_MOVE, REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException error) {
            Files.move(origen, destino, REPLACE_EXISTING);
        }
    }

    private static void cargarUsuarios(Path archivo, Biblioteca biblioteca) throws IOException {
        Set<Integer> ids = new HashSet<>();
        Set<String> documentos = new HashSet<>();
        for (Usuario usuario : biblioteca.getUsuarios()) {
            ids.add(usuario.getIdUsuario());
            documentos.add(usuario.getDocumento());
        }
        ArchivoTexto.leer(archivo, 8, campos -> {
            int id = Integer.parseInt(campos[1]);
            if (!ids.add(id) || !documentos.add(campos[2])) {
                throw new IllegalArgumentException("ID o documento repetido.");
            }
            Usuario usuario = switch (campos[0]) {
                case "LECTOR" -> new Lector(id, campos[2], campos[3], campos[4], campos[5], campos[7]);
                case "ADMINISTRADOR" -> new Administrador(id, campos[2], campos[3], campos[4], campos[5], campos[7]);
                default -> throw new IllegalArgumentException("Tipo de usuario inválido.");
            };
            if (!ArchivoTexto.booleano(campos[6])) {
                usuario.desactivar();
            }
            biblioteca.registrarUsuario(usuario);
        });
    }

    private static void cargarPolitica(Path archivo, PoliticaPrestamo politica) throws IOException {
        if (Files.notExists(archivo)) {
            return;
        }
        int[] lineas = {0};
        ArchivoTexto.leer(archivo, 4, campos -> {
            if (++lineas[0] > 1) {
                throw new IllegalArgumentException("Se esperaba una sola línea.");
            }
            int dias = Integer.parseInt(campos[0]);
            int maximo = Integer.parseInt(campos[1]);
            BigDecimal tarifa = new BigDecimal(campos[2]);
            if (dias <= 0 || maximo <= 0 || tarifa.signum() < 0) {
                throw new IllegalArgumentException("Días y máximo deben ser positivos; la tarifa debe ser mayor o igual a cero.");
            }
            politica.actualizarReglas(dias, maximo, tarifa, ArchivoTexto.booleano(campos[3]));
        });
        if (lineas[0] == 0) {
            throw new IOException("politica.txt: se esperaba una línea con cuatro campos.");
        }
    }

    private static void escribirUsuarios(Path archivo, Biblioteca biblioteca) throws IOException {
        ArchivoTexto.escribir(archivo, salida -> {
            for (Usuario usuario : biblioteca.getUsuarios()) {
                String tipo;
                String codigo;
                if (usuario instanceof Lector lector) {
                    tipo = "LECTOR";
                    codigo = lector.getCodigoLector();
                } else if (usuario instanceof Administrador administrador) {
                    tipo = "ADMINISTRADOR";
                    codigo = administrador.getCodigoAdministrador();
                } else {
                    throw new IllegalArgumentException("Tipo de usuario sin formato de guardado.");
                }
                ArchivoTexto.fila(salida, tipo, usuario.getIdUsuario(), usuario.getDocumento(), usuario.getNombres(),
                        usuario.getApellidos(), usuario.getCorreo(), usuario.getActivo(), codigo);
            }
        });
    }

    private static void escribirPolitica(Path archivo, PoliticaPrestamo politica) throws IOException {
        ArchivoTexto.escribir(archivo, salida -> ArchivoTexto.fila(salida, politica.getDiasPrestamo(),
                politica.getMaximoEjemplaresActivos(), politica.getPenalizacionPorDia(),
                politica.getBloquearPorPenalizacion()));
    }

    private static class ErrorRecuperacion extends IOException {
        ErrorRecuperacion(Path respaldo, IOException causa) {
            super("El guardado se interrumpió y no se pudieron restaurar todos los archivos. "
                    + "Copia anterior: " + respaldo, causa);
        }
    }
}
