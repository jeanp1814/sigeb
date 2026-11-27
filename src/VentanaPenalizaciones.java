import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;

public class VentanaPenalizaciones extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JComboBox<String> opciones = new JComboBox<>();
    private ArrayList<Penalizacion> penalizaciones = new ArrayList<>();

    public VentanaPenalizaciones(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - Penalizaciones");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        add(opciones, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        JButton actualizar = new JButton("Actualizar"); actualizar.addActionListener(this); botones.add(actualizar);
        JButton resolver = new JButton("Resolver"); resolver.addActionListener(this); botones.add(resolver);
        add(botones, BorderLayout.SOUTH); mostrar();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getActionCommand().equals("Resolver") && opciones.getSelectedIndex() >= 0) {
            biblioteca.resolverPenalizacion(penalizaciones.get(opciones.getSelectedIndex()));
        }
        mostrar();
    }
    private void mostrar() {
        opciones.removeAllItems(); penalizaciones.clear();
        for (Prestamo prestamo : biblioteca.getPrestamos()) {
            for (DetallePrestamo detalle : prestamo.getDetalles()) {
                Penalizacion penalizacion = detalle.getPenalizacion();
                if (penalizacion != null) {
                    penalizaciones.add(penalizacion);
                    opciones.addItem("Prestamo " + prestamo.getIdPrestamo() + " - "
                            + prestamo.getLector().getNombres() + " - S/ " + penalizacion.getMonto()
                            + " - " + (penalizacion.getResuelta() ? "Resuelta" : "Pendiente"));
                }
            }
        }
    }
}
