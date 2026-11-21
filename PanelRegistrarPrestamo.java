
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PanelRegistrarPrestamo extends JPanel {

    private JTextField txtIdPrestamo;
    private JTextField txtLector;
    private JTextField txtAdministrador;
    private JTextField txtFechaPrestamo;
    private JTextField txtFechaLimite;
    private JTextField txtEjemplar;

    private DefaultTableModel modelo;
    private JTable tabla;

    public PanelRegistrarPrestamo() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titulo
        JLabel titulo = new JLabel("Registrar nuevo préstamo");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        add(titulo, BorderLayout.NORTH);

        // Panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // Datos del préstamo
        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 15, 15));
        panelDatos.setBackground(Color.WHITE);

        txtIdPrestamo = new JTextField();
        txtLector = new JTextField();
        txtAdministrador = new JTextField();
        txtFechaPrestamo = new JTextField(LocalDate.now().toString());
        txtFechaLimite = new JTextField();

        agregarCampo(panelDatos, "Código del préstamo:", txtIdPrestamo);
        agregarCampo(panelDatos, "Código del lector:", txtLector);
        agregarCampo(panelDatos, "Código del administrador:", txtAdministrador);
        agregarCampo(panelDatos, "Fecha del préstamo:", txtFechaPrestamo);
        agregarCampo(panelDatos, "Fecha límite:", txtFechaLimite);

        panelPrincipal.add(panelDatos);
        panelPrincipal.add(Box.createVerticalStrut(25));

        // Agregar ejemplares
        JLabel lblEjemplares = new JLabel("Ejemplares del préstamo");
        lblEjemplares.setFont(new Font("Arial", Font.BOLD, 16));

        panelPrincipal.add(lblEjemplares);
        panelPrincipal.add(Box.createVerticalStrut(10));

        JPanel panelAgregar = new JPanel(new BorderLayout(10, 0));
        panelAgregar.setBackground(Color.WHITE);

        txtEjemplar = new JTextField();
        txtEjemplar.setBorder(
            BorderFactory.createTitledBorder("Código del ejemplar")
        );

        JButton btnAgregar = new JButton("Agregar ejemplar");

        panelAgregar.add(txtEjemplar, BorderLayout.CENTER);
        panelAgregar.add(btnAgregar, BorderLayout.EAST);

        panelPrincipal.add(panelAgregar);
        panelPrincipal.add(Box.createVerticalStrut(15));

        // Tabla de ejemplares
        modelo = new DefaultTableModel(
            new String[]{"N°", "Código del ejemplar"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

