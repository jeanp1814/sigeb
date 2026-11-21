
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PanelConsultarPrestamos extends JPanel {

    private JTabbedPane pestañas;

    public PanelConsultarPrestamos() {

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Titulo
        JLabel titulo = new JLabel("Consultar préstamos");
        titulo.setFont(new Font("Arial", Font.BOLD, 25));

        add(titulo, BorderLayout.NORTH);

        // Pestañas
        pestañas = new JTabbedPane();

        pestañas.addTab("Pendientes", crearPanelTabla("PENDIENTE"));
        pestañas.addTab("Historial", crearPanelTabla("HISTORIAL"));
        pestañas.addTab("Vencidos", crearPanelTabla("VENCIDO"));

        add(pestañas, BorderLayout.CENTER);
    }

    private JPanel crearPanelTabla(String tipo) {

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Buscador
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 0));
        panelBusqueda.setBackground(Color.WHITE);

        JTextField txtBuscar = new JTextField();
        txtBuscar.setBorder(
            BorderFactory.createTitledBorder("Buscar por código de préstamo, lector o ejemplar")
        );

        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBusqueda.add(txtBuscar, BorderLayout.CENTER);

        JPanel botonesBusqueda = new JPanel(new FlowLayout());
        botonesBusqueda.setBackground(Color.WHITE);
        botonesBusqueda.add(btnBuscar);
        botonesBusqueda.add(btnLimpiar);

        panelBusqueda.add(botonesBusqueda, BorderLayout.EAST);

        panel.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {
            "ID Préstamo",
            "Lector",
            "Ejemplar",
            "Fecha préstamo",
            "Fecha límite",
            "Fecha devolución",
            "Estado"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        TableRowSorter<DefaultTableModel> ordenador =
            new TableRowSorter<>(modelo);

        tabla.setRowSorter(ordenador);

        JScrollPane scroll = new JScrollPane(tabla);

        panel.add(scroll, BorderLayout.CENTER);

        // Información inferior
        JLabel lblCantidad = new JLabel("Registros: 0");
        lblCantidad.setFont(new Font("Arial", Font.PLAIN, 13));

        panel.add(lblCantidad, BorderLayout.SOUTH);

        // Búsqueda
        btnBuscar.addActionListener(e -> {

            String texto = txtBuscar.getText().trim();

            if (texto.isEmpty()) {
                ordenador.setRowFilter(null);
            } else {
                ordenador.setRowFilter(
                    RowFilter.regexFilter(
                        "(?i)" + java.util.regex.Pattern.quote(texto)
                    )
                );
            }
        });

        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
        });

        return panel;
    }
}
