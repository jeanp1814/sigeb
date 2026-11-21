
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

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(600, 180));

        panelPrincipal.add(scroll);

        panelPrincipal.add(Box.createVerticalStrut(20));

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(Color.WHITE);

        JButton btnEliminar = new JButton("Eliminar ejemplar");
        JButton btnRegistrar = new JButton("Registrar préstamo");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnRegistrar);

        panelPrincipal.add(panelBotones);

        add(panelPrincipal, BorderLayout.CENTER);

        // Eventos
        btnAgregar.addActionListener(e -> agregarEjemplar());

        btnEliminar.addActionListener(e -> eliminarEjemplar());

        btnLimpiar.addActionListener(e -> limpiarFormulario());

        btnRegistrar.addActionListener(e -> registrarPrestamo());
    }

    private void agregarCampo(
        JPanel panel, String texto, JTextField campo
    ) {

        JPanel contenedor = new JPanel(new BorderLayout(5, 5));
        contenedor.setBackground(Color.WHITE);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 13));

        contenedor.add(etiqueta, BorderLayout.NORTH);
        contenedor.add(campo, BorderLayout.CENTER);

        panel.add(contenedor);
    }

    private void agregarEjemplar() {

        String codigo = txtEjemplar.getText().trim();

        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Ingrese el código del ejemplar."
            );
            return;
        }

        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 1).toString().equals(codigo)) {
                JOptionPane.showMessageDialog(
                    this, "El ejemplar ya fue agregado."
                );
                return;
            }
        }

        modelo.addRow(new Object[]{
            modelo.getRowCount() + 1,
            codigo
        });

        txtEjemplar.setText("");
    }

    private void eliminarEjemplar() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                this, "Seleccione un ejemplar."
            );
            return;
        }

        modelo.removeRow(fila);

        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.setValueAt(i + 1, i, 0);
        }
    }

    private void registrarPrestamo() {

        if (txtIdPrestamo.getText().trim().isEmpty()
            || txtLector.getText().trim().isEmpty()
            || txtAdministrador.getText().trim().isEmpty()
            || txtFechaLimite.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this, "Complete todos los campos."
            );
            return;
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "Agregue al menos un ejemplar."
            );
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(
                txtFechaPrestamo.getText().trim()
            );

            LocalDate limite = LocalDate.parse(
                txtFechaLimite.getText().trim()
            );

            if (limite.isBefore(fecha)) {
                JOptionPane.showMessageDialog(
                    this, "La fecha límite no puede ser anterior al préstamo."
                );
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Datos del préstamo validados correctamente.\n"
                + "Ejemplares: " + modelo.getRowCount()
            );

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Ingrese las fechas en formato AAAA-MM-DD."
            );
        }
    }

    private void limpiarFormulario() {

        txtIdPrestamo.setText("");
        txtLector.setText("");
        txtAdministrador.setText("");
        txtFechaPrestamo.setText(LocalDate.now().toString());
        txtFechaLimite.setText("");
        txtEjemplar.setText("");

        modelo.setRowCount(0);
    }
}
