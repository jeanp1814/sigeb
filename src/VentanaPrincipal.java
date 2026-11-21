import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JButton usuarios;
    private JButton catalogo;
    private JButton prestamos;

    public VentanaPrincipal(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;

        setTitle(biblioteca.getNombre());
        setSize(550, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Sistema de Gestion de Biblioteca",SwingConstants.CENTER);

        add(titulo, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        usuarios = new JButton("Usuarios");
        catalogo = new JButton("Catalogo e inventario");
        prestamos = new JButton("Prestamos y devoluciones");

        usuarios.addActionListener(this);
        catalogo.addActionListener(this);
        prestamos.addActionListener(this);

        panel.add(usuarios);
        panel.add(catalogo);
        panel.add(prestamos);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == usuarios) {
            JOptionPane.showMessageDialog(this, "Falta conectar la ventana de usuarios.");
        } else if (evento.getSource() == catalogo) {
            JOptionPane.showMessageDialog(this,"Falta conectar la ventana de catalogo.");
        } else if (evento.getSource() == prestamos) {
            JOptionPane.showMessageDialog(this,"Falta conectar la ventana de prestamos.");
        }
    }
}