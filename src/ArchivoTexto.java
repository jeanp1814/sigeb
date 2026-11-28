import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

final class ArchivoTexto {
    private ArchivoTexto() {
    }

    static void leer(Path archivo, int cantidadCampos, Consumer<String[]> procesar) throws IOException {
        if (Files.notExists(archivo)) {
            return;
        }
        try (Scanner entrada = new Scanner(new FileReader(archivo.toFile(), StandardCharsets.UTF_8))) {
            int numeroLinea = 0;
            while (entrada.hasNextLine()) {
                numeroLinea++;
                String[] campos = entrada.nextLine().split("\\|", -1);
                try {
                    if (campos.length != cantidadCampos) {
                        throw new IllegalArgumentException("Se esperaban " + cantidadCampos + " campos.");
                    }
                    for (int i = 0; i < campos.length; i++) {
                        campos[i] = desescapar(campos[i]);
                    }
                    procesar.accept(campos);
                } catch (IllegalArgumentException error) {
                    throw new IOException(archivo.getFileName() + ", línea " + numeroLinea + ": "
                            + error.getMessage(), error);
                }
            }
            if (entrada.ioException() != null) {
                throw entrada.ioException();
            }
        }
    }

    static void escribir(Path archivo, Consumer<PrintWriter> imprimir) throws IOException {
        try (PrintWriter salida = new PrintWriter(new FileWriter(archivo.toFile(), StandardCharsets.UTF_8))) {
            imprimir.accept(salida);
            salida.close();
            if (salida.checkError()) {
                throw new IOException("Error al escribir " + archivo.getFileName() + ".");
            }
        } catch (IllegalArgumentException error) {
            throw new IOException(archivo.getFileName() + ": " + error.getMessage(), error);
        }
    }

    static void fila(PrintWriter salida, Object... datos) {
        String[] campos = new String[datos.length];
        for (int i = 0; i < datos.length; i++) {
            campos[i] = escapar(String.valueOf(datos[i]));
        }
        salida.println(String.join("|", campos));
    }

    static boolean booleano(String valor) {
        if (!valor.equals("true") && !valor.equals("false")) {
            throw new IllegalArgumentException("Valor booleano inválido.");
        }
        return Boolean.parseBoolean(valor);
    }

    static <T> void indexar(Map<Integer, T> registros, int id, T registro, String tipo) {
        if (registros.putIfAbsent(id, registro) != null) {
            throw new IllegalArgumentException("ID de " + tipo + " repetido: " + id + ".");
        }
    }

    static <T> T buscar(Map<Integer, T> registros, String campo, String tipo) {
        T registro = registros.get(Integer.parseInt(campo));
        if (registro == null) {
            throw new IllegalArgumentException("No existe " + tipo + " con ID " + campo + ".");
        }
        return registro;
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
