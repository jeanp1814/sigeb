import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrestamosDevoluciones extends JFrame implements ActionListener {
    private Biblioteca biblioteca;

    public VentanaPrestamosDevoluciones(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - PrestamosDevoluciones");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        JPanel botones = new JPanel(new GridLayout(5, 1, 5, 5));
        for (String texto : new String[]{"Registrar prestamo", "Registrar devolucion", "Consultar prestamos", "Penalizaciones", "Politica de prestamos"}) {
            JButton boton = new JButton(texto); boton.addActionListener(this); botones.add(boton);
        }
        add(botones, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        if (accion.equals("Registrar prestamo")) new VentanaRegistrarPrestamo(biblioteca);
        else if (accion.equals("Registrar devolucion")) new VentanaRegistrarDevolucion(biblioteca);
        else if (accion.equals("Consultar prestamos")) new VentanaConsultarPrestamos(biblioteca);
        else if (accion.equals("Penalizaciones")) new VentanaPenalizaciones(biblioteca);
        else new VentanaPoliticaPrestamos(biblioteca);
    }
}
