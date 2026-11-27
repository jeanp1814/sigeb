import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class AplicacionSIGEB {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(AplicacionSIGEB::iniciar);
    }

    private static void iniciar() {
        PoliticaPrestamo politica = new PoliticaPrestamo(7, 3, new BigDecimal("1.50"), true);
        Biblioteca biblioteca = new Biblioteca("SIGEB", politica);
        PersistenciaSIGEB persistencia = new PersistenciaSIGEB(Path.of("."));

        try {
            persistencia.cargar(biblioteca);
            new VentanaPrincipal(biblioteca, persistencia);
        } catch (IOException error) {
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos.\n" + error.getMessage(),
                    "Error de lectura", JOptionPane.ERROR_MESSAGE);
        }
    }
}
