import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;

public class VentanaRegistrarPrestamo extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JComboBox<String> lectores = new JComboBox<>();
    private JComboBox<String> administradores = new JComboBox<>();
    private JComboBox<String> ejemplares = new JComboBox<>();
    private ArrayList<Lector> listaLectores = new ArrayList<>();
    private ArrayList<Administrador> listaAdministradores = new ArrayList<>();
    private ArrayList<Ejemplar> listaEjemplares = new ArrayList<>();
    private ArrayList<Ejemplar> seleccionados = new ArrayList<>();
    private JTextArea listado = new JTextArea();

    public VentanaRegistrarPrestamo(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - RegistrarPrestamo");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel formulario = new JPanel(new GridLayout(3, 2, 5, 5));
        for (Usuario usuario : biblioteca.getUsuarios()) {
            if (!usuario.getActivo()) continue;
            if (usuario instanceof Lector) {
                listaLectores.add((Lector) usuario);
                lectores.addItem(usuario.getIdUsuario() + " - " + usuario.getNombres());
            } else if (usuario instanceof Administrador) {
                listaAdministradores.add((Administrador) usuario);
                administradores.addItem(usuario.getIdUsuario() + " - " + usuario.getNombres());
            }
        }
        for (Libro libro : biblioteca.getLibros()) {
            for (Ejemplar ejemplar : libro.getEjemplares()) {
                if (ejemplar.estaDisponible()) {
                    listaEjemplares.add(ejemplar);
                    ejemplares.addItem(ejemplar.getCodigoInventario() + " - " + libro.getTitulo());
                }
            }
        }
        formulario.add(new JLabel("Lector:")); formulario.add(lectores);
        formulario.add(new JLabel("Administrador:")); formulario.add(administradores);
        formulario.add(new JLabel("Ejemplar:")); formulario.add(ejemplares);
        add(formulario, BorderLayout.NORTH);
        listado.setEditable(false); add(listado, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        for (String texto : new String[]{"Agregar ejemplar", "Limpiar", "Registrar"}) {
            JButton boton = new JButton(texto); boton.addActionListener(this); botones.add(boton);
        }
        add(botones, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        if (accion.equals("Limpiar")) { seleccionados.clear(); listado.setText(""); return; }
        if (accion.equals("Agregar ejemplar")) {
            if (ejemplares.getSelectedIndex() < 0) return;
            Ejemplar ejemplar = listaEjemplares.get(ejemplares.getSelectedIndex());
            if (seleccionados.contains(ejemplar)) {
                JOptionPane.showMessageDialog(this, "Ejemplar ya seleccionado"); return;
            }
            seleccionados.add(ejemplar);
            listado.append(ejemplar.getCodigoInventario() + "\n");
            return;
        }
        if (lectores.getSelectedIndex() < 0 || administradores.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Registre un lector y un administrador activos"); return;
        }
        Prestamo prestamo = biblioteca.registrarPrestamo(
                listaLectores.get(lectores.getSelectedIndex()), seleccionados,
                listaAdministradores.get(administradores.getSelectedIndex()));
        if (prestamo == null) {
            JOptionPane.showMessageDialog(this, "No se registro: revise disponibilidad, limite y penalizaciones");
        } else {
            JOptionPane.showMessageDialog(this, "Prestamo registrado: " + prestamo.getIdPrestamo());
            dispose();
        }
    }
}
