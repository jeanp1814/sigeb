
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PanelPenalizaciones extends JPanel {

    private JTextField txtBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> ordenador;

    public PanelPenalizaciones() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titulo
        JLabel titulo = new JLabel("Gestión de penalizaciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        add(titulo, BorderLayout.NORTH);

        // Panel principal
        JPanel principal = new JPanel(new BorderLayout(10, 15));
        principal.setBackground(Color.WHITE);
        principal.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        // Busqueda
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBackground(Color.WHITE);

        txtBuscar = new JTextField();
        txtBuscar.setBorder(
            BorderFactory.createTitledBorder(
                "Buscar por código, lector o estado"
            )
        );

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

        JPanel botonesBusqueda = new JPanel(new FlowLayout());
        botonesBusqueda.setBackground(Color.WHITE);
        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnLimpiar);

        panelBusqueda.add(botonesBusqueda, BorderLayout.EAST);

        principal.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de penalizaciones
        String[] columnas = {
            "ID Penalización",
            "ID Préstamo",
            "Lector",
            "Motivo",
            "Días retraso",
            "Monto (S/)",
            "Estado"
        };

