public class Categoria {
    private int idCategoria;
    private String nombre;
    private String descripcion;

    public Categoria(int id, String nombre, String descripcion) {
        idCategoria = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void actualizarDatos(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
