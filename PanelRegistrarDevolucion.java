
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

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(600, 130));

        principal.add(scroll);
        principal.add(Box.createVerticalStrut(20));

        // Datos de devolución
        JLabel lblDatos = new JLabel("Datos de la devolución");
        lblDatos.setFont(new Font("Arial", Font.BOLD, 16));

        principal.add(lblDatos);
        principal.add(Box.createVerticalStrut(10));

        JPanel panelDatos = new JPanel(new GridLayout(3, 2, 15, 12));
        panelDatos.setBackground(Color.WHITE);

        txtIdPrestamo = new JTextField();
        txtLector = new JTextField();
        txtEjemplar = new JTextField();
        txtFechaLimite = new JTextField();
        txtFechaDevolucion = new JTextField(LocalDate.now().toString());
        txtDiasRetraso = new JTextField("0");

        txtIdPrestamo.setEditable(false);
        txtLector.setEditable(false);
        txtEjemplar.setEditable(false);
        txtFechaLimite.setEditable(false);
        txtDiasRetraso.setEditable(false);

        agregarCampo(panelDatos, "ID préstamo:", txtIdPrestamo);
        agregarCampo(panelDatos, "Lector:", txtLector);
        agregarCampo(panelDatos, "Ejemplar:", txtEjemplar);
        agregarCampo(panelDatos, "Fecha límite:", txtFechaLimite);
        agregarCampo(panelDatos, "Fecha devolución:", txtFechaDevolucion);
        agregarCampo(panelDatos, "Días de retraso:", txtDiasRetraso);

        principal.add(panelDatos);
        principal.add(Box.createVerticalStrut(15));

        // Observacion
        JLabel lblObservacion = new JLabel("Observaciones:");
        lblObservacion.setFont(new Font("Arial", Font.BOLD, 14));

        txtObservacion = new JTextArea(3, 20);
        txtObservacion.setLineWrap(true);
        txtObservacion.setWrapStyleWord(true);

        JScrollPane scrollObservacion = new JScrollPane(txtObservacion);

        principal.add(lblObservacion);
        principal.add(Box.createVerticalStrut(5));
        principal.add(scrollObservacion);
        principal.add(Box.createVerticalStrut(15));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCalcular = new JButton("Calcular retraso");
        JButton btnRegistrar = new JButton("Registrar devolución");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnCalcular);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRegistrar);

        principal.add(panelBotones);

        add(principal, BorderLayout.CENTER);

        // Eventos
        btnBuscar.addActionListener(e -> buscarPrestamo());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarEjemplar();
            }
        });

        btnCalcular.addActionListener(e -> calcularRetraso());

        btnRegistrar.addActionListener(e -> registrarDevolucion());

        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

