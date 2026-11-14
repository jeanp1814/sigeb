
import java.time.LocalDate;
import java.util.ArrayList;

public class Prestamo {

    private int idPrestamo;
    private LocalDate fechaPrestamo;
    private Lector lector;
    private Administrador registradoPor;
    private ArrayList<DetallePrestamo> detalles;

    public Prestamo(int id, LocalDate fecha, Lector lector, Administrador admin) {
        this.idPrestamo = id;
        this.fechaPrestamo = fecha;
        this.lector = lector;
        this.registradoPor = admin;
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetallePrestamo detalle) {
        detalles.add(detalle);
    }

    public boolean estaCerrado() {
        for (DetallePrestamo detalle : detalles) {
            if (detalle.getFechaDevolucion() == null) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<DetallePrestamo> obtenerPendientes() {
        ArrayList<DetallePrestamo> pendientes = new ArrayList<>();

        for (DetallePrestamo detalle : detalles) {
            if (detalle.getFechaDevolucion() == null) {
                pendientes.add(detalle);
            }
        }

        return pendientes;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public Lector getLector() {
        return lector;
    }

    public Administrador getRegistradoPor() {
        return registradoPor;
    }

    public ArrayList<DetallePrestamo> getDetalles() {
        return detalles;
    }
}
