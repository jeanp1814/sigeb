import javax.swing.*;
import java.awt.*;

public class FrmPrestamosDevoluciones extends JFrame {

    private JPanel panelContenido;
    private CardLayout cardLayout;

    private final Color COLOR_MENU = new Color(35, 47, 62);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_BOTON = new Color(52, 73, 94);

    public FrmPrestamosDevoluciones() {

        setTitle("Sistema de Biblioteca - Préstamos y Devoluciones");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(25, 118, 160));
        panelSuperior.setPreferredSize(new Dimension(1100, 65));

        JLabel titulo = new JLabel("  GESTIÓN DE PRÉSTAMOS Y DEVOLUCIONES");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        panelSuperior.add(titulo, BorderLayout.WEST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel lateral
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new GridLayout(7, 1, 5, 10));
        panelMenu.setBackground(COLOR_MENU);
        panelMenu.setPreferredSize(new Dimension(230, 600));
        panelMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnInicio = crearBoton("Inicio");
        JButton btnRegistrarPrestamo = crearBoton("Registrar préstamo");
        JButton btnRegistrarDevolucion = crearBoton("Registrar devolución");
        JButton btnConsultas = crearBoton("Consultar préstamos");
        JButton btnPenalizaciones = crearBoton("Penalizaciones");
        JButton btnPolitica = crearBoton("Política de préstamos");
        JButton btnSalir = crearBoton("Salir");

        panelMenu.add(btnInicio);
        panelMenu.add(btnRegistrarPrestamo);
        panelMenu.add(btnRegistrarDevolucion);
        panelMenu.add(btnConsultas);
        panelMenu.add(btnPenalizaciones);
        panelMenu.add(btnPolitica);
        panelMenu.add(btnSalir);

        add(panelMenu, BorderLayout.WEST);

        // Panel central
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        panelContenido.setBackground(COLOR_FONDO);

        panelContenido.add(crearPanelInicio(), "inicio");
        panelContenido.add(new PanelRegistrarPrestamo(), "prestamos");
        panelContenido.add(crearPanelVacio("Registro de devoluciones"), "devoluciones");
        panelContenido.add(crearPanelVacio("Consultas de préstamos"), "consultas");
        panelContenido.add(crearPanelVacio("Gestión de penalizaciones"), "penalizaciones");
        panelContenido.add(crearPanelVacio("Política de préstamos"), "politica");

        add(panelContenido, BorderLayout.CENTER);

        // Eventos de los botones
        btnInicio.addActionListener(e -> mostrarPanel("inicio"));

        btnRegistrarPrestamo.addActionListener(
            e -> mostrarPanel("prestamos")
        );

        btnRegistrarDevolucion.addActionListener(
            e -> mostrarPanel("devoluciones")
        );

        btnConsultas.addActionListener(
            e -> mostrarPanel("consultas")
        );

        btnPenalizaciones.addActionListener(
            e -> mostrarPanel("penalizaciones")
        );

        btnPolitica.addActionListener(
            e -> mostrarPanel("politica")
        );

        btnSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir del módulo de préstamos?",
                "Confirmar salida",
                JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);

        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setForeground(Color.WHITE);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    private JPanel crearPanelInicio() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        JLabel titulo = new JLabel("Bienvenido al módulo");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Préstamos y Devoluciones");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitulo.setForeground(Color.GRAY);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descripcion = new JLabel(
            "Seleccione una opción del menú para comenzar."
        );
        descripcion.setFont(new Font("Arial", Font.PLAIN, 15));
        descripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        contenido.add(titulo);
        contenido.add(Box.createVerticalStrut(15));
        contenido.add(subtitulo);
        contenido.add(Box.createVerticalStrut(25));
        contenido.add(descripcion);

        panel.add(contenido);

        return panel;
    }

    private JPanel crearPanelVacio(String titulo) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO);

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(etiqueta);

        return panel;
    }

    private void mostrarPanel(String nombre) {
        cardLayout.show(panelContenido, nombre);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            FrmPrestamosDevoluciones ventana =
                new FrmPrestamosDevoluciones();

            ventana.setVisible(true);
        });
    }
}