import java.math.BigDecimal;
import java.time.LocalDate;
public class politicaPrestamo {

    private int diasPrestamo;
    private int maximoEjemplaresActivos;
    private BigDecimal penalizacionPorDia;
    private boolean bloquearPorPenalizacion;

    public politicaPrestamo(int diasPrestamo, int maximoEjemplaresActivos, BigDecimal penalizacionPorDia, boolean bloquearPorPenalizacion) {
        this.diasPrestamo = diasPrestamo;
        this.maximoEjemplaresActivos = maximoEjemplaresActivos;
        this.penalizacionPorDia = penalizacionPorDia;
        this.bloquearPorPenalizacion = bloquearPorPenalizacion;
    }

    public LocalDate calcularFechaLimite(LocalDate inicio){
        return inicio.plusDays(diasPrestamo);
    }

    public void actualizarReglas(int dias, int maximo, BigDecimal tarifa, boolean bloquear) {
        this.diasPrestamo = dias;
        this.maximoEjemplaresActivos = maximo;
        this.penalizacionPorDia = tarifa;
        this.bloquearPorPenalizacion = bloquear;
    }
    
}
