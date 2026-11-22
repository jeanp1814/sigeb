import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;

public class VentanaPoliticaPrestamos extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JTextField dias = new JTextField();
    private JTextField maximo = new JTextField();
    private JTextField tarifa = new JTextField();
    private JCheckBox bloquear = new JCheckBox("Bloquear por penalizacion pendiente");

    public VentanaPoliticaPrestamos(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - PoliticaPrestamos");
        setSize(700, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        PoliticaPrestamo politica = biblioteca.getPolitica();
        dias.setText(String.valueOf(java.time.temporal.ChronoUnit.DAYS.between(
                LocalDate.now(), politica.calcularFechaLimite(LocalDate.now()))));
        maximo.setText(String.valueOf(politica.getMaximoEjemplaresActivos()));
        tarifa.setText(politica.getPenalizacionPorDia().toPlainString());
        bloquear.setSelected(politica.getBloquearPorPenalizacion());
        JPanel datos = new JPanel(new GridLayout(4, 2, 5, 5));
        datos.add(new JLabel("Dias de prestamo:")); datos.add(dias);
        datos.add(new JLabel("Maximo de ejemplares:")); datos.add(maximo);
        datos.add(new JLabel("Tarifa por dia:")); datos.add(tarifa);
        datos.add(new JLabel("Bloqueo:")); datos.add(bloquear);
        add(datos, BorderLayout.NORTH);
        JButton guardar = new JButton("Guardar"); guardar.addActionListener(this);
        add(guardar, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        try {
            int plazo = Integer.parseInt(dias.getText().trim());
            int limite = Integer.parseInt(maximo.getText().trim());
            BigDecimal monto = new BigDecimal(tarifa.getText().trim());
            if (plazo <= 0 || limite <= 0 || monto.signum() < 0) {
                JOptionPane.showMessageDialog(this, "Dias y maximo deben ser positivos; tarifa no negativa"); return;
            }
            biblioteca.getPolitica().actualizarReglas(plazo, limite, monto, bloquear.isSelected());
            JOptionPane.showMessageDialog(this, "Politica actualizada");
        } catch (NumberFormatException error) {
            JOptionPane.showMessageDialog(this, "Ingrese numeros validos; use punto en la tarifa");
        }
    }
}
