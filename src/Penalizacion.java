import java.math.BigDecimal;

public class Penalizacion {

    private int idPenalizacion;
    private String motivo;
    private int diasRetraso;
    private BigDecimal monto;
    private boolean resuelta;

    public Penalizacion(int id, String motivo, int dias) {
        this.idPenalizacion = id;
        this.motivo = motivo;
        this.diasRetraso = dias;
        this.monto = BigDecimal.ZERO;
        this.resuelta = false;
    }

    public void calcularMonto(BigDecimal tarifa) {
        monto = tarifa.multiply(BigDecimal.valueOf(diasRetraso));
    }

    public void resolver() {
        resuelta = true;
    }
}
