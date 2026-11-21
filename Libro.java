import java.util.ArrayList;

public class Libro {
    private int idLibro;
    private String isbn;
    private String titulo;
    private String editorial;
    private int añoPublicacion;
    private ArrayList<Autor> autores;
    private Categoria categoria;
    private ArrayList<Ejemplar> ejemplares;

    public Libro(int id, String isbn, String titulo, String editorial, int año, Categoria categoria) {
        idLibro = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.editorial = editorial;
        añoPublicacion = año;
        this.categoria = categoria;

        autores = new ArrayList<>();
        ejemplares = new ArrayList<>();
    }

    public void agregarAutor(Autor autor) {
        autores.add(autor);
    }

    public void agregarEjemplar(Ejemplar ejemplar) {
        ejemplares.add(ejemplar);
    }

    public void actualizarDatos(String titulo, String editorial, int año) {
        this.titulo = titulo;
        this.editorial = editorial;
        añoPublicacion = año;
    }

    public int contarDisponibles() {
        int cantidad = 0;

        for (Ejemplar ejemplar : ejemplares) {
            if (ejemplar.estaDisponible()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    // Métodos de consulta para mostrar los datos
    public int getIdLibro() {
        return idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public ArrayList<Autor> getAutores() {
        return autores;
    }

    public ArrayList<Ejemplar> getEjemplares() {
        return ejemplares;
    }
}