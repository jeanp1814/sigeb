import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class PersistenciaCatalogo {
    static final String AUTORES = "autores.txt";
    static final String CATEGORIAS = "categorias.txt";
    static final String LIBROS = "libros.txt";
    static final String EJEMPLARES = "ejemplares.txt";

    private PersistenciaCatalogo() {
    }

    static void cargar(Path directorio, Biblioteca biblioteca) throws IOException {
        Map<Integer, Autor> autores = new HashMap<>();
        Map<Integer, Categoria> categorias = new HashMap<>();
        for (Autor autor : biblioteca.getAutores()) {
            autores.put(autor.getIdAutor(), autor);
        }
        for (Categoria categoria : biblioteca.getCategorias()) {
            categorias.put(categoria.getIdCategoria(), categoria);
        }
        ArchivoTexto.leer(directorio.resolve(AUTORES), 2, campos -> {
            int id = Integer.parseInt(campos[0]);
            Autor autor = new Autor(id, campos[1]);
            ArchivoTexto.indexar(autores, id, autor, "autor");
            biblioteca.registrarAutor(autor);
        });
        ArchivoTexto.leer(directorio.resolve(CATEGORIAS), 3, campos -> {
            int id = Integer.parseInt(campos[0]);
            Categoria categoria = new Categoria(id, campos[1], campos[2]);
            ArchivoTexto.indexar(categorias, id, categoria, "categoría");
            biblioteca.registrarCategoria(categoria);
        });
        Map<Integer, Libro> librosNuevos = cargarLibros(directorio, biblioteca, autores, categorias);
        cargarEjemplares(directorio, biblioteca, librosNuevos);
    }

    private static Map<Integer, Libro> cargarLibros(Path directorio, Biblioteca biblioteca,
            Map<Integer, Autor> autores, Map<Integer, Categoria> categorias) throws IOException {
        Map<Integer, Libro> librosNuevos = new HashMap<>();
        Set<Integer> ids = new HashSet<>();
        Set<String> isbns = new HashSet<>();
        for (Libro libro : biblioteca.getLibros()) {
            ids.add(libro.getIdLibro());
            isbns.add(libro.getIsbn());
        }
        ArchivoTexto.leer(directorio.resolve(LIBROS), 7, campos -> {
            int id = Integer.parseInt(campos[0]);
            if (!ids.add(id) || !isbns.add(campos[1])) {
                throw new IllegalArgumentException("ID o ISBN de libro repetido.");
            }
            Categoria categoria = ArchivoTexto.buscar(categorias, campos[5], "categoría");
            Libro libro = new Libro(id, campos[1], campos[2], campos[3], Integer.parseInt(campos[4]), categoria);
            if (!campos[6].isEmpty()) {
                Set<Integer> idsAutores = new HashSet<>();
                for (String campo : campos[6].split(",", -1)) {
                    Autor autor = ArchivoTexto.buscar(autores, campo, "autor");
                    if (!idsAutores.add(autor.getIdAutor())) {
                        throw new IllegalArgumentException("Autor repetido en el libro.");
                    }
                    libro.agregarAutor(autor);
                }
            }
            librosNuevos.put(id, libro);
            biblioteca.registrarLibro(libro);
        });
        return librosNuevos;
    }

    private static void cargarEjemplares(Path directorio, Biblioteca biblioteca,
            Map<Integer, Libro> librosNuevos) throws IOException {
        Set<Integer> ids = new HashSet<>();
        Set<String> codigos = new HashSet<>();
        for (Libro libro : biblioteca.getLibros()) {
            for (Ejemplar ejemplar : libro.getEjemplares()) {
                ids.add(ejemplar.getIdEjemplar());
                codigos.add(ejemplar.getCodigoInventario());
            }
        }
        ArchivoTexto.leer(directorio.resolve(EJEMPLARES), 5, campos -> {
            int id = Integer.parseInt(campos[0]);
            if (!ids.add(id) || !codigos.add(campos[2])) {
                throw new IllegalArgumentException("ID o código de ejemplar repetido.");
            }
            Libro libro = ArchivoTexto.buscar(librosNuevos, campos[1], "libro de la carga");
            Ejemplar ejemplar = new Ejemplar(id, campos[2], campos[3]);
            ejemplar.cambiarEstado(campos[4]);
            libro.agregarEjemplar(ejemplar);
        });
    }

    static void guardar(Path directorio, Biblioteca biblioteca) throws IOException {
        ArchivoTexto.escribir(directorio.resolve(AUTORES), salida -> {
            for (Autor autor : biblioteca.getAutores()) {
                ArchivoTexto.fila(salida, autor.getIdAutor(), autor.getNombreCompleto());
            }
        });
        ArchivoTexto.escribir(directorio.resolve(CATEGORIAS), salida -> {
            for (Categoria categoria : biblioteca.getCategorias()) {
                ArchivoTexto.fila(salida, categoria.getIdCategoria(), categoria.getNombre(), categoria.getDescripcion());
            }
        });
        ArchivoTexto.escribir(directorio.resolve(LIBROS), salida -> {
            for (Libro libro : biblioteca.getLibros()) {
                String autores = libro.getAutores().stream()
                        .map(autor -> Integer.toString(autor.getIdAutor()))
                        .collect(Collectors.joining(","));
                ArchivoTexto.fila(salida, libro.getIdLibro(), libro.getIsbn(), libro.getTitulo(),
                        libro.getEditorial(), libro.getAñoPublicacion(), libro.getCategoria().getIdCategoria(), autores);
            }
        });
        ArchivoTexto.escribir(directorio.resolve(EJEMPLARES), salida -> {
            for (Libro libro : biblioteca.getLibros()) {
                for (Ejemplar ejemplar : libro.getEjemplares()) {
                    ArchivoTexto.fila(salida, ejemplar.getIdEjemplar(), libro.getIdLibro(),
                            ejemplar.getCodigoInventario(), ejemplar.getUbicacion(), ejemplar.getEstado());
                }
            }
        });
    }
}
