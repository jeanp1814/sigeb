import java.math.BigDecimal;

public class Penalizacion {

    private int idPenalizacion;
    private String motivo;
    private int diasRetraso;
    private BigDecimal monto;
    private boolean resuelta;

    public Penalizacion(int id, String motivo, int dias) {
        this(id, motivo, dias, BigDecimal.ZERO, false);
    }

    Penalizacion(int id, String motivo, int dias, BigDecimal monto, boolean resuelta) {
        this.idPenalizacion = id;
        this.motivo = motivo;
        this.diasRetraso = dias;
        this.monto = monto;
        this.resuelta = resuelta;
    }

    public void calcularMonto(BigDecimal tarifa) {
        monto = tarifa.multiply(BigDecimal.valueOf(diasRetraso));
    }

    public void resolver() {
        resuelta = true;
    }

    public int getIdPenalizacion() {
        return idPenalizacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public int getDiasRetraso() {
        return diasRetraso;
    }

    public boolean getResuelta() {
        return resuelta;
    }

    public BigDecimal getMonto() {
        return monto;
    }
}
