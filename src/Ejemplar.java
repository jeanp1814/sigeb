public class Ejemplar {
    private int idEjemplar;
    private String codigoInventario;
    private String ubicacion;
    private String estado;

    public Ejemplar(int id, String codigo, String ubicacion) {
        idEjemplar = id;
        codigoInventario = codigo;
        this.ubicacion = ubicacion;
        estado = "Disponible";
    }

    public boolean estaDisponible() {
        return estado.equals("Disponible");
    }

    public void cambiarEstado(String estado) {
        this.estado = estado;
    }

    public int getIdEjemplar() {
        return idEjemplar;
    }

    public String getCodigoInventario() {
        return codigoInventario;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getEstado() {
        return estado;
    }
}