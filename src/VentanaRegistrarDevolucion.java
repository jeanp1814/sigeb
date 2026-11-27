import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class VentanaRegistrarDevolucion extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JComboBox<String> pendientes = new JComboBox<>();
    private JComboBox<String> administradores = new JComboBox<>();
    private ArrayList<DetallePrestamo> detalles = new ArrayList<>();
    private ArrayList<Administrador> admins = new ArrayList<>();
    private JTextField fecha = new JTextField(LocalDate.now().toString());

    public VentanaRegistrarDevolucion(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - RegistrarDevolucion");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel datos = new JPanel(new GridLayout(3, 2, 5, 5));
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            for (DetallePrestamo detalle : prestamo.obtenerPendientes()) {
                detalles.add(detalle);
                pendientes.addItem("Prestamo " + prestamo.getIdPrestamo() + " - "
                        + detalle.getEjemplar().getCodigoInventario() + " - limite: " + detalle.getFechaLimite());
            }
        }
        for (Usuario usuario : biblioteca.getUsuarios()) {
            if (usuario instanceof Administrador && usuario.getActivo()) {
                admins.add((Administrador) usuario);
                administradores.addItem(usuario.getIdUsuario() + " - " + usuario.getNombres());
            }
        }
        datos.add(new JLabel("Ejemplar pendiente:")); datos.add(pendientes);
        datos.add(new JLabel("Administrador:")); datos.add(administradores);
        datos.add(new JLabel("Fecha AAAA-MM-DD:")); datos.add(fecha);
        add(datos, BorderLayout.NORTH);
        JButton registrar = new JButton("Registrar devolucion");
        registrar.addActionListener(this); add(registrar, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (pendientes.getSelectedIndex() < 0 || administradores.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle y un administrador"); return;
        }
        LocalDate devolucion;
        try { devolucion = LocalDate.parse(fecha.getText().trim()); }
        catch (java.time.format.DateTimeParseException error) {
            JOptionPane.showMessageDialog(this, "Fecha incorrecta: use AAAA-MM-DD"); return;
        }
        DetallePrestamo detalle = detalles.get(pendientes.getSelectedIndex());
        biblioteca.registrarDevolucion(detalle, devolucion, admins.get(administradores.getSelectedIndex()));
        if (detalle.getFechaDevolucion() == null) {
            JOptionPane.showMessageDialog(this, "No se registro: revise la fecha y el administrador");
        } else {
            String mensaje = "Devolucion registrada";
            if (detalle.getPenalizacion() != null) mensaje += " - Penalizacion: S/ " + detalle.getPenalizacion().getMonto();
            JOptionPane.showMessageDialog(this, mensaje); dispose();
        }
    }
}
