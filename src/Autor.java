public class Autor {
    private int idAutor;
    private String nombreCompleto;

    public Autor(int id, String nombre) {
        idAutor = id;
        nombreCompleto = nombre;
    }

    public void actualizarDatos(String nombre) {
        nombreCompleto = nombre;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
}