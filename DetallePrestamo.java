import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DetallePrestamo {

    private int idDetalle;
    private Ejemplar ejemplar;
    private LocalDate fechaLimite;
    private LocalDate fechaDevolucion;
    private Administrador devueltoPor;
    private String observacion;
    private Penalizacion penalizacion;

    public DetallePrestamo(int id, Ejemplar ejemplar, LocalDate limite) {
        this.idDetalle = id;
        this.ejemplar = ejemplar;
        this.fechaLimite = limite;
        this.fechaDevolucion = null;
        this.devueltoPor = null;
        this.observacion = "";
        this.penalizacion = null;
    }

