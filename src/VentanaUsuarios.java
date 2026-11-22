import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class VentanaUsuarios extends JFrame implements ActionListener {
    private Biblioteca biblioteca;
    private JComboBox<String> tipo = new JComboBox<>(new String[]{"Lector", "Administrador"});
    private JTextField id = new JTextField(12);
    private JTextField documento = new JTextField(12);
    private JTextField nombres = new JTextField(12);
    private JTextField apellidos = new JTextField(12);
    private JTextField correo = new JTextField(12);
    private JTextField codigo = new JTextField(12);
    private JTextArea listado = new JTextArea();

    public VentanaUsuarios(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        setTitle("SIGEB - Usuarios");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel formulario = new JPanel(new GridLayout(7, 2, 5, 5));
        formulario.add(new JLabel("Tipo:")); formulario.add(tipo);
        formulario.add(new JLabel("ID:")); formulario.add(id);
        formulario.add(new JLabel("Documento:")); formulario.add(documento);
        formulario.add(new JLabel("Nombres:")); formulario.add(nombres);
        formulario.add(new JLabel("Apellidos:")); formulario.add(apellidos);
        formulario.add(new JLabel("Correo:")); formulario.add(correo);
        formulario.add(new JLabel("Código:")); formulario.add(codigo);
        add(formulario, BorderLayout.NORTH);

        listado.setEditable(false);
        add(new JScrollPane(listado), BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        JButton registrar = new JButton("Registrar");
        JButton actualizar = new JButton("Actualizar");
        JButton desactivar = new JButton("Desactivar");
        JButton listar = new JButton("Listar");
        botones.add(registrar); botones.add(actualizar);
        botones.add(desactivar); botones.add(listar);
        add(botones, BorderLayout.SOUTH);

        registrar.addActionListener(this);
        actualizar.addActionListener(this);
        desactivar.addActionListener(this);
        listar.addActionListener(this);
        mostrar();
    }

    private Usuario buscar(int numero) {
        for (Usuario u : biblioteca.getUsuarios()) if (u.getIdUsuario() == numero) return u;
        return null;
    }

    private void registrar() {
        try {
            int numero = Integer.parseInt(id.getText().trim());
            if (buscar(numero) != null) { aviso("ID repetido"); return; }
            if (documento.getText().trim().isEmpty() || nombres.getText().trim().isEmpty() ||
                apellidos.getText().trim().isEmpty() || correo.getText().trim().isEmpty() ||
                codigo.getText().trim().isEmpty()) { aviso("Complete todos los campos"); return; }
            for (Usuario actual : biblioteca.getUsuarios()) {
                if (actual.getDocumento().equals(documento.getText().trim())) {
                    aviso("Documento repetido"); return;
                }
            }
            Usuario nuevo;
            if (tipo.getSelectedIndex() == 0) {
                nuevo = new Lector(numero, documento.getText().trim(), nombres.getText(),
                        apellidos.getText(), correo.getText(), codigo.getText());
            } else {
                nuevo = new Administrador(numero, documento.getText().trim(), nombres.getText(),
                        apellidos.getText(), correo.getText(), codigo.getText());
            }
            biblioteca.registrarUsuario(nuevo);
            mostrar(); aviso("Usuario registrado");
        } catch (NumberFormatException ex) { aviso("El ID debe ser un número"); }
    }

    private void actualizar() {
        try {
            Usuario u = buscar(Integer.parseInt(id.getText().trim()));
            if (u == null) { aviso("Usuario no encontrado"); return; }
            if (nombres.getText().trim().isEmpty() || apellidos.getText().trim().isEmpty() ||
                correo.getText().trim().isEmpty()) { aviso("Complete nombres, apellidos y correo"); return; }
            u.actualizarDatos(nombres.getText(), apellidos.getText(), correo.getText());
            mostrar(); aviso("Usuario actualizado");
        } catch (NumberFormatException ex) { aviso("ID inválido"); }
    }

    private void desactivar() {
        try {
            Usuario u = buscar(Integer.parseInt(id.getText().trim()));
            if (u == null) { aviso("Usuario no encontrado"); return; }
            biblioteca.desactivarUsuario(u.getIdUsuario()); mostrar(); aviso("Usuario desactivado");
        } catch (NumberFormatException ex) { aviso("ID inválido"); }
    }

    private void mostrar() {
        listado.setText("LISTA DE USUARIOS\n\n");
        for (Usuario u : biblioteca.getUsuarios()) {
            listado.append(u.identificarse() + "\n\n");
        }
    }

    private void aviso(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }

    @Override
    public void actionPerformed(ActionEvent evento) {
        String accion = evento.getActionCommand();
        if (accion.equals("Registrar")) registrar();
        else if (accion.equals("Actualizar")) actualizar();
        else if (accion.equals("Desactivar")) desactivar();
        else mostrar();
    }
}
