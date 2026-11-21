
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeParseException;

public class PanelRegistrarDevolucion extends JPanel {

    private JTextField txtBuscar;
    private JTextField txtIdPrestamo;
    private JTextField txtLector;
    private JTextField txtEjemplar;
    private JTextField txtFechaLimite;
    private JTextField txtFechaDevolucion;
    private JTextField txtDiasRetraso;

    private JTextArea txtObservacion;

    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelRegistrarDevolucion() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titulo
        JLabel titulo = new JLabel("Registrar devolución");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        add(titulo, BorderLayout.NORTH);

        // Panel principal
        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.WHITE);
        principal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Busqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBackground(Color.WHITE);

        txtBuscar = new JTextField();
        txtBuscar.setBorder(
            BorderFactory.createTitledBorder("Código del préstamo")
        );

        JButton btnBuscar = new JButton("Buscar préstamo");

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);
        panelBusqueda.add(btnBuscar, BorderLayout.EAST);

        principal.add(panelBusqueda);
        principal.add(Box.createVerticalStrut(20));

        // Tabla de ejemplares pendientes
        JLabel lblEjemplares = new JLabel("Ejemplares pendientes de devolución");
        lblEjemplares.setFont(new Font("Arial", Font.BOLD, 16));

        principal.add(lblEjemplares);
        principal.add(Box.createVerticalStrut(10));

        modelo = new DefaultTableModel(
            new String[]{
                "ID Detalle",
                "Ejemplar",
                "Fecha límite",
                "Estado"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

