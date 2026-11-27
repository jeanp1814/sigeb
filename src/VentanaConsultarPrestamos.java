import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class VentanaConsultarPrestamos extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JComboBox<String> filtro = new JComboBox<>(new String[]{"Historial", "Pendientes", "Vencidos"});
    private JTextArea listado = new JTextArea();

    public VentanaConsultarPrestamos(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - ConsultarPrestamos");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(filtro, BorderLayout.NORTH); filtro.addActionListener(this);
        listado.setEditable(false); add(new JScrollPane(listado), BorderLayout.CENTER);
        JButton actualizar = new JButton("Actualizar"); actualizar.addActionListener(this);
        add(actualizar, BorderLayout.SOUTH); mostrar();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        mostrar();
    }
    private void mostrar() {
        listado.setText("");
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                boolean incluir = filtro.getSelectedIndex() == 0
                        || (filtro.getSelectedIndex() == 1 && detalle.getFechaDevolucion() == null)
                        || (filtro.getSelectedIndex() == 2 && detalle.estaVencido(LocalDate.now()));
                if (incluir) listado.append("Prestamo: " + prestamo.getIdPrestamo()
                        + " | Lector: " + prestamo.getLector().getNombres()
                        + " | Ejemplar: " + detalle.getEjemplar().getCodigoInventario()
                        + " | Limite: " + detalle.getFechaLimite()
                        + " | Devolucion: " + detalle.getFechaDevolucion() + "\n");
            }
        }
    }
}
