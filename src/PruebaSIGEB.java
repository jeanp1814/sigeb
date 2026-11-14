//Est archivo no esta incluido en el proyecto final, es solo para probar la funcionalidad de las clases y metodos implementados.

import java.math.BigDecimal;
import java.time.LocalDate;

public class PruebaSIGEB {
    public static void main(String[] args) {
        Lector lector = new Lector(1, "12345678", "Jeanpiere", "Mamani",
                "jeanpiere@correo.com", "L001");
        Administrador admin = new Administrador(2, "87654321", "Ana", "Perez",
                "ana@correo.com", "A001");
        System.out.println("LECTOR");
        System.out.println(lector.identificarse());

        Categoria categoria = new Categoria(1, "Programacion", "Libros de programacion");
        Libro libro = new Libro(1, "9780000000001", "Java basico", "Editorial", 2026, categoria);
        libro.agregarAutor(new Autor(1, "Autor de ejemplo"));
        Ejemplar ejemplar = new Ejemplar(1, "INV001", "Estante A");
        libro.agregarEjemplar(ejemplar);
        System.out.println("\nCATALOGO");
        System.out.println("Ejemplares disponibles: " + libro.contarDisponibles());

        PoliticaPrestamo politica = new PoliticaPrestamo(7, 3, new BigDecimal("1.50"), true);
        LocalDate inicio = LocalDate.of(2026, 11, 13);
        LocalDate limite = politica.calcularFechaLimite(inicio);
        Prestamo prestamo = new Prestamo(1, inicio, lector, admin);
        DetallePrestamo detalle = new DetallePrestamo(1, ejemplar, limite);
        prestamo.agregarDetalle(detalle);
        ejemplar.cambiarEstado("Prestado");
        System.out.println("\nPRESTAMO");
        System.out.println("Fecha limite: " + limite);
        System.out.println("Pendientes: " + prestamo.obtenerPendientes().size());
        System.out.println("Ejemplares disponibles: " + libro.contarDisponibles());

        LocalDate devolucion = limite.plusDays(2);
        int dias = detalle.calcularDiasRetraso(devolucion);
        Penalizacion penalizacion = new Penalizacion(1, "Devolucion tardia", dias);
        penalizacion.calcularMonto(new BigDecimal("1.50"));
        System.out.println("\nPENALIZACION");
        System.out.println("Dias de retraso: " + dias);
        System.out.println("Calculo de penalizacion ejecutado.");
        penalizacion.resolver();
        System.out.println("Resolucion de penalizacion ejecutada.");

        detalle.registrarDevolucion(devolucion, admin, "Buen estado");
        ejemplar.cambiarEstado("Disponible");
        System.out.println("\nDEVOLUCION");
        System.out.println("Prestamo cerrado: " + prestamo.estaCerrado());
        System.out.println("Pendientes: " + prestamo.obtenerPendientes().size());
        System.out.println("Ejemplares disponibles: " + libro.contarDisponibles());
    }
}