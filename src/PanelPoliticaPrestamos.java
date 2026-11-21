
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class PanelPoliticaPrestamos extends JPanel {

    private JSpinner spDiasPrestamo;
    private JSpinner spMaxEjemplares;
    private JSpinner spPenalizacion;
    private JCheckBox chkRenovaciones;
    private JSpinner spDiasRenovacion;

    public PanelPoliticaPrestamos() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titulo
        JLabel titulo = new JLabel("Política de préstamos");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        add(titulo, BorderLayout.NORTH);

        // Panel principal
        JPanel principal = new JPanel();
        principal.setLayout(new BoxLayout(principal, BoxLayout.Y_AXIS));
        principal.setBackground(Color.WHITE);
        principal.setBorder(
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        );

        JLabel subtitulo = new JLabel("Configuración de reglas");
        subtitulo.setFont(new Font("Arial", Font.BOLD, 18));

        principal.add(subtitulo);
        principal.add(Box.createVerticalStrut(25));

        // Configuracion
        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 20, 20));
        panelDatos.setBackground(Color.WHITE);

        spDiasPrestamo = new JSpinner(
            new SpinnerNumberModel(7, 1, 90, 1)
        );

        spMaxEjemplares = new JSpinner(
            new SpinnerNumberModel(3, 1, 20, 1)
        );

        spPenalizacion = new JSpinner(
            new SpinnerNumberModel(2.0, 0.0, 100.0, 0.5)
        );

        chkRenovaciones = new JCheckBox("Permitir renovaciones");
        chkRenovaciones.setBackground(Color.WHITE);

        spDiasRenovacion = new JSpinner(
            new SpinnerNumberModel(7, 1, 30, 1)
        );

        agregarCampo(
            panelDatos,
            "Días máximos de préstamo:",
            spDiasPrestamo
        );

        agregarCampo(
            panelDatos,
            "Máximo de ejemplares:",
            spMaxEjemplares
        );

        agregarCampo(
            panelDatos,
            "Penalización por día (S/):",
            spPenalizacion
        );

        agregarCampo(
            panelDatos,
            "Renovaciones:",
            chkRenovaciones
        );

        agregarCampo(
            panelDatos,
            "Días adicionales por renovación:",
            spDiasRenovacion
        );

        principal.add(panelDatos);
        principal.add(Box.createVerticalStrut(30));

        // Botones
        JPanel panelBotones = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        panelBotones.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar configuración");
        JButton btnRestablecer = new JButton("Restablecer");

        panelBotones.add(btnRestablecer);
        panelBotones.add(btnGuardar);

        principal.add(panelBotones);

        add(principal, BorderLayout.CENTER);

        // Eventos
        chkRenovaciones.addActionListener(e -> {
            spDiasRenovacion.setEnabled(
                chkRenovaciones.isSelected()
            );
        });

        btnGuardar.addActionListener(e -> guardarConfiguracion());

        btnRestablecer.addActionListener(e -> restablecerConfiguracion());

        spDiasRenovacion.setEnabled(false);
    }

    private void agregarCampo(
        JPanel panel, String texto, JComponent componente
    ) {

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(etiqueta);
        panel.add(componente);
    }

    private void guardarConfiguracion() {

        int diasPrestamo = (Integer) spDiasPrestamo.getValue();
        int maxEjemplares = (Integer) spMaxEjemplares.getValue();

        double penalizacion = (Double) spPenalizacion.getValue();

        boolean renovaciones = chkRenovaciones.isSelected();

        int diasRenovacion = (Integer) spDiasRenovacion.getValue();

        String mensaje =
            "Configuración guardada correctamente.\n\n"
            + "Días de préstamo: " + diasPrestamo + "\n"
            + "Máximo de ejemplares: " + maxEjemplares + "\n"
            + "Penalización diaria: S/ " + penalizacion + "\n"
            + "Renovaciones: " + (renovaciones ? "Sí" : "No");

        if (renovaciones) {
            mensaje += "\nDías adicionales: " + diasRenovacion;
        }

        JOptionPane.showMessageDialog(
            this,
            mensaje,
            "Política de préstamos",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void restablecerConfiguracion() {

        spDiasPrestamo.setValue(7);
        spMaxEjemplares.setValue(3);
        spPenalizacion.setValue(2.0);
        chkRenovaciones.setSelected(false);
        spDiasRenovacion.setValue(7);

        spDiasRenovacion.setEnabled(false);
    }

    // Métodos para consultar la configuración

    public int getDiasPrestamo() {
        return (Integer) spDiasPrestamo.getValue();
    }

    public int getMaxEjemplares() {
        return (Integer) spMaxEjemplares.getValue();
    }

    public BigDecimal getPenalizacionDiaria() {
        return BigDecimal.valueOf(
            (Double) spPenalizacion.getValue()
        );
    }

    public boolean isRenovacionesPermitidas() {
        return chkRenovaciones.isSelected();
    }

    public int getDiasRenovacion() {
        return (Integer) spDiasRenovacion.getValue();
    }
}
