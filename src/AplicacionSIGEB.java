import java.math.BigDecimal;

public class AplicacionSIGEB {
    public static void main(String[] args) {
        PoliticaPrestamo politica = new PoliticaPrestamo(7, 3, new BigDecimal("1.50"), true);

        Biblioteca biblioteca = new Biblioteca("SIGEB", politica);

        new VentanaPrincipal(biblioteca);
    }
}