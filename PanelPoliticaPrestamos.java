
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

