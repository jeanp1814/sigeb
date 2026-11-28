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

    public void registrarDevolucion(LocalDate fecha, Administrador admin, String observacion) {
        this.fechaDevolucion = fecha;
        this.devueltoPor = admin;
        this.observacion = observacion;
    }

    public int getIdDetalle() {
        return idDetalle;
    }

    public Administrador getDevueltoPor() {
        return devueltoPor;
    }

    public String getObservacion() {
        return observacion;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public int calcularDiasRetraso(LocalDate fecha) {
        if (fecha.isAfter(fechaLimite)) {
            return (int) ChronoUnit.DAYS.between(fechaLimite, fecha);
        }

        return 0;
    }

    public boolean estaVencido(LocalDate fecha) {
        return fechaDevolucion == null && fecha.isAfter(fechaLimite);
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public Penalizacion getPenalizacion() {
        return penalizacion;
    }

    public void setPenalizacion(Penalizacion penalizacion) {
        this.penalizacion = penalizacion;
    }
    public LocalDate getFechaLimite() {
        return fechaLimite;
    }
}
