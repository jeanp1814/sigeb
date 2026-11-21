import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaCatalogo extends JFrame {
    private ArrayList<Libro> libros;
    private ArrayList<Autor> autores;
    private ArrayList<Categoria> categorias;
    private JTextArea listado = new JTextArea();
    private JTextField buscar = new JTextField(18);
    private JComboBox<String> secciones = new JComboBox<>(new String[]{"Categorías", "Autores", "Libros", "Ejemplares"});
    private JPanel acciones = new JPanel(new FlowLayout());

    public VentanaCatalogo(ArrayList<Libro> libros, ArrayList<Autor> autores, ArrayList<Categoria> categorias) {
        this.libros = libros;
        this.autores = autores;
        this.categorias = categorias;
        setTitle("SIGEB - Catálogo");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        JPanel arriba = new JPanel(new FlowLayout());
        arriba.add(new JLabel("Paso 1: Categorías → 2: Autores → 3: Libros → 4: Ejemplares     Sección:")); arriba.add(secciones);
        add(arriba, BorderLayout.NORTH);
        listado.setEditable(false);
        add(new JScrollPane(listado), BorderLayout.CENTER);
        add(acciones, BorderLayout.SOUTH);
        secciones.addActionListener(e -> cambiarSeccion());
        cambiarSeccion();
    }

    private void boton(String nombre, Runnable accion) {
        JButton b = new JButton(nombre);
        b.addActionListener(e -> accion.run());
        acciones.add(b);
    }

    private void cambiarSeccion() {
        acciones.removeAll();
        int seccion = secciones.getSelectedIndex();
        if (seccion == 2) {
            boton("Registrar libro", () -> registrarLibro());
            boton("Actualizar libro", () -> actualizarLibro());
            boton("Asignar autor", () -> asignarAutor());
            acciones.add(new JLabel("Título/ISBN:")); acciones.add(buscar);
            boton("Buscar", () -> mostrarLibros(buscar.getText()));
            boton("Mostrar todos", () -> { buscar.setText(""); mostrarLibros(""); });
        } else if (seccion == 1) {
            boton("Registrar autor", () -> registrarAutor());
            boton("Actualizar autor", () -> actualizarAutor());
            boton("Listar", () -> mostrar());
        } else if (seccion == 0) {
            boton("Registrar categoría", () -> registrarCategoria());
            boton("Actualizar categoría", () -> actualizarCategoria());
            boton("Listar", () -> mostrar());
        } else {
            boton("Agregar ejemplar", () -> agregarEjemplar());
            boton("Cambiar estado", () -> cambiarEstado());
            boton("Listar", () -> mostrar());
        }
        acciones.revalidate(); acciones.repaint(); mostrar();
    }

    private void registrarLibro() {
        try {
            int id = numero("ID libro:");
            if (libro(id) != null) { aviso("ID repetido"); return; }
            String isbn = pedir("ISBN:"); if (isbn == null) return;
            for (Libro l : libros) if (l.getIsbn().equals(isbn)) { aviso("ISBN repetido"); return; }
            String titulo = pedir("Título:"); if (titulo == null) return;
            String editorial = pedir("Editorial:"); if (editorial == null) return;
            int anio = numero("Año publicación:");
            Categoria c = categoria(numero("ID categoría (consulte la sección Categorías):"));
            if (c == null) { aviso("Categoría no encontrada"); return; }
            libros.add(new Libro(id, isbn, titulo, editorial, anio, c));
            mostrar(); aviso("Libro registrado");
        } catch (Exception ex) { error(); }
    }

    private void actualizarLibro() {
        try {
            Libro l = libro(numero("ID libro:"));
            if (l == null) { aviso("Libro no encontrado"); return; }
            String titulo = pedir("Nuevo título:"); if (titulo == null) return;
            String editorial = pedir("Nueva editorial:"); if (editorial == null) return;
            int anio = numero("Nuevo año:");
            l.actualizarDatos(titulo, editorial, anio);
            mostrar(); aviso("Libro actualizado");
        } catch (Exception ex) { error(); }
    }

    private void asignarAutor() {
        try {
            Libro l = libro(numero("ID libro (consulte la sección Libros):"));
            Autor a = autor(numero("ID autor (consulte la sección Autores):"));
            if (l == null || a == null) { aviso("Libro o autor no encontrado"); return; }
            if (!l.getAutores().contains(a)) l.agregarAutor(a);
            mostrar(); aviso("Autor asignado");
        } catch (Exception ex) { error(); }
    }

    private void registrarAutor() {
        try {
            int id = numero("ID autor:");
            if (autor(id) != null) { aviso("ID repetido"); return; }
            String nombre = pedir("Nombre completo:"); if (nombre == null) return;
            autores.add(new Autor(id, nombre)); mostrar(); aviso("Autor registrado");
        } catch (Exception ex) { error(); }
    }

    private void actualizarAutor() {
        try {
            Autor a = autor(numero("ID autor:"));
            if (a == null) { aviso("Autor no encontrado"); return; }
            String nombre = pedir("Nuevo nombre:"); if (nombre == null) return;
            a.actualizarDatos(nombre); mostrar(); aviso("Autor actualizado");
        } catch (Exception ex) { error(); }
    }

    private void registrarCategoria() {
        try {
            int id = numero("ID categoría:");
            if (categoria(id) != null) { aviso("ID repetido"); return; }
            String nombre = pedir("Nombre:"); if (nombre == null) return;
            String desc = pedir("Descripción:"); if (desc == null) return;
            categorias.add(new Categoria(id, nombre, desc)); mostrar(); aviso("Categoría registrada");
        } catch (Exception ex) { error(); }
    }

    private void actualizarCategoria() {
        try {
            Categoria c = categoria(numero("ID categoría:"));
            if (c == null) { aviso("Categoría no encontrada"); return; }
            String nombre = pedir("Nuevo nombre:"); if (nombre == null) return;
            String desc = pedir("Nueva descripción:"); if (desc == null) return;
            c.actualizarDatos(nombre, desc); mostrar(); aviso("Categoría actualizada");
        } catch (Exception ex) { error(); }
    }

    private void agregarEjemplar() {
        try {
            Libro l = libro(numero("ID libro:"));
            if (l == null) { aviso("Libro no encontrado"); return; }
            int id = numero("ID ejemplar:");
            if (ejemplar(id) != null) { aviso("ID repetido"); return; }
            String codigo = pedir("Código inventario:"); if (codigo == null) return;
            for (Libro x : libros) for (Ejemplar e : x.getEjemplares())
                if (e.getCodigoInventario().equals(codigo)) { aviso("Código repetido"); return; }
            String ubicacion = pedir("Ubicación:"); if (ubicacion == null) return;
            l.agregarEjemplar(new Ejemplar(id, codigo, ubicacion));
            mostrar(); aviso("Ejemplar agregado");
        } catch (Exception ex) { error(); }
    }

    private void cambiarEstado() {
        try {
            Ejemplar e = ejemplar(numero("ID ejemplar:"));
            if (e == null) { aviso("Ejemplar no encontrado"); return; }
            String[] opciones = {"Disponible", "Prestado", "Mantenimiento", "Perdido"};
            String estado = (String) JOptionPane.showInputDialog(this, "Nuevo estado:",
                "Estado", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (estado == null) return;
            e.cambiarEstado(estado); mostrar(); aviso("Estado actualizado");
        } catch (Exception ex) { error(); }
    }

    private void mostrar() {
        int seccion = secciones.getSelectedIndex();
        if (seccion == 2) mostrarLibros("");
        if (seccion == 1) {
            listado.setText("AUTORES\n\n");
            for (Autor a : autores) listado.append("ID: " + a.getIdAutor() + " | " + a.getNombreCompleto() + "\n");
        }
        if (seccion == 0) {
            listado.setText("CATEGORÍAS\n\n");
            for (Categoria c : categorias) listado.append("ID: " + c.getIdCategoria() + " | " + c.getNombre() + " | " + c.getDescripcion() + "\n");
        }
        if (seccion == 3) {
            listado.setText("EJEMPLARES\n\n");
            for (Libro l : libros) for (Ejemplar e : l.getEjemplares())
                listado.append("Libro: " + l.getTitulo() + " | ID: " + e.getIdEjemplar() +
                    " | Código: " + e.getCodigoInventario() + " | Ubicación: " +
                    e.getUbicacion() + " | Estado: " + e.getEstado() + "\n");
        }
    }

    private void mostrarLibros(String filtro) {
        listado.setText("LIBROS\n\n");
        for (Libro l : libros) {
            if (l.getTitulo().toLowerCase().contains(filtro.toLowerCase()) || l.getIsbn().contains(filtro)) {
                listado.append("ID: " + l.getIdLibro() + " | ISBN: " + l.getIsbn() +
                    " | " + l.getTitulo() + " | Editorial: " + l.getEditorial() +
                    " | Año: " + l.getAñoPublicacion() + " | Categoría: " +
                    l.getCategoria().getNombre() + " | Disponibles: " + l.contarDisponibles() + "\n");
            }
        }
    }

    private Libro libro(int id) { for (Libro l : libros) if (l.getIdLibro() == id) return l; return null; }
    private Autor autor(int id) { for (Autor a : autores) if (a.getIdAutor() == id) return a; return null; }
    private Categoria categoria(int id) { for (Categoria c : categorias) if (c.getIdCategoria() == id) return c; return null; }
    private Ejemplar ejemplar(int id) {
        for (Libro l : libros) for (Ejemplar e : l.getEjemplares()) if (e.getIdEjemplar() == id) return e;
        return null;
    }
    private int numero(String mensaje) { return Integer.parseInt(JOptionPane.showInputDialog(this, mensaje)); }
    private String pedir(String mensaje) {
        String s = JOptionPane.showInputDialog(this, mensaje);
        if (s == null) return null;
        if (s.trim().isEmpty()) { aviso("No puede estar vacío"); return null; }
        return s.trim();
    }
    private void aviso(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    private void error() { aviso("Operación cancelada o dato numérico incorrecto"); }
}
