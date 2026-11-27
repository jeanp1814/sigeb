import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame implements ActionListener {
    private final Biblioteca biblioteca;
    private final PersistenciaSIGEB persistencia;
    private final ArrayList<Autor> autores = new ArrayList<>();
    private final ArrayList<Categoria> categorias = new ArrayList<>();

    private final JButton usuarios = new JButton("Usuarios");
    private final JButton catalogo = new JButton("Catálogo e inventario");
    private final JButton prestamos = new JButton("Préstamos y devoluciones");
    private final JButton guardar = new JButton("Guardar datos");

    public VentanaPrincipal(Biblioteca biblioteca, PersistenciaSIGEB persistencia) {
        this.biblioteca = biblioteca;
        this.persistencia = persistencia;

        setTitle(biblioteca.getNombre());
        setSize(550, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel titulo = new JLabel("Sistema de Gestión de Biblioteca", SwingConstants.CENTER);
        add(titulo, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (JButton boton : new JButton[]{usuarios, catalogo, prestamos, guardar}) {
            boton.addActionListener(this);
            panel.add(boton);
        }
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == usuarios) {
            new VentanaUsuarios(biblioteca).setVisible(true);
        } else if (evento.getSource() == catalogo) {
            new VentanaCatalogo(biblioteca, autores, categorias).setVisible(true);
        } else if (evento.getSource() == prestamos) {
            new VentanaPrestamosDevoluciones(biblioteca);
        } else if (evento.getSource() == guardar) {
            guardarDatos();
        }
    }

    private void guardarDatos() {
        try {
            persistencia.guardar(biblioteca);
            JOptionPane.showMessageDialog(this, "Se guardaron los usuarios y la política de préstamos.");
        } catch (IOException error) {
            JOptionPane.showMessageDialog(this, "No se completó el guardado.\n" + error.getMessage(),
                    "Error de escritura", JOptionPane.ERROR_MESSAGE);
        }
    }
}
