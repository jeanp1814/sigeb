
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

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Arial", Font.PLAIN, 12));

        tabla.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        ordenador = new TableRowSorter<>(modelo);
        tabla.setRowSorter(ordenador);

        JScrollPane scroll = new JScrollPane(tabla);

        principal.add(scroll, BorderLayout.CENTER);

        // Panel inferior
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(Color.WHITE);

        JLabel lblCantidad = new JLabel("Total de penalizaciones: 0");

        JButton btnResolver = new JButton("Resolver penalización");
        JButton btnActualizar = new JButton("Actualizar");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.setBackground(Color.WHITE);

        botones.add(btnActualizar);
        botones.add(btnResolver);

        panelInferior.add(lblCantidad, BorderLayout.WEST);
        panelInferior.add(botones, BorderLayout.EAST);

        principal.add(panelInferior, BorderLayout.SOUTH);

        add(principal, BorderLayout.CENTER);

        // Eventos
        btnBuscar.addActionListener(e -> buscar());

        btnLimpiar.addActionListener(e -> {
            txtBuscar.setText("");
            ordenador.setRowFilter(null);
        });

        btnResolver.addActionListener(e -> resolverPenalizacion());

        btnActualizar.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                this,
                "La actualización se conectará con los datos del sistema."
            );
        });
    }

    private void buscar() {

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
    }

    private void resolverPenalizacion() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Seleccione una penalización."
            );
            return;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);

        String estado = modelo.getValueAt(filaModelo, 6).toString();

        if (estado.equals("Resuelta")) {
            JOptionPane.showMessageDialog(
                this,
                "Esta penalización ya fue resuelta."
            );
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Desea marcar esta penalización como resuelta?",
            "Confirmar resolución",
            JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {

            modelo.setValueAt("Resuelta", filaModelo, 6);

            JOptionPane.showMessageDialog(
                this,
                "Penalización marcada como resuelta."
            );
        }
    }
}
