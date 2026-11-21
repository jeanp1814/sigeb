import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaUsuarios extends JFrame {
    private ArrayList<Lector> usuarios;
    private JTextField id = new JTextField(12);
    private JTextField documento = new JTextField(12);
    private JTextField nombres = new JTextField(12);
    private JTextField apellidos = new JTextField(12);
    private JTextField correo = new JTextField(12);
    private JTextField codigo = new JTextField(12);
    private JTextArea listado = new JTextArea();

    public VentanaUsuarios(ArrayList<Lector> usuarios) {
        this.usuarios = usuarios;
        setTitle("SIGEB - Usuarios");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 5, 5));
        formulario.add(new JLabel("ID:")); formulario.add(id);
        formulario.add(new JLabel("Documento:")); formulario.add(documento);
        formulario.add(new JLabel("Nombres:")); formulario.add(nombres);
        formulario.add(new JLabel("Apellidos:")); formulario.add(apellidos);
        formulario.add(new JLabel("Correo:")); formulario.add(correo);
        formulario.add(new JLabel("Código lector:")); formulario.add(codigo);
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

        registrar.addActionListener(e -> registrar());
        actualizar.addActionListener(e -> actualizar());
        desactivar.addActionListener(e -> desactivar());
        listar.addActionListener(e -> mostrar());
        mostrar();
    }

    private Lector buscar(int numero) {
        for (Lector u : usuarios) if (u.identificarse().startsWith("ID: " + numero + "\n")) return u;
        return null;
    }

    private void registrar() {
        try {
            int numero = Integer.parseInt(id.getText().trim());
            if (buscar(numero) != null) { aviso("ID repetido"); return; }
            if (documento.getText().trim().isEmpty() || nombres.getText().trim().isEmpty() ||
                apellidos.getText().trim().isEmpty() || correo.getText().trim().isEmpty() ||
                codigo.getText().trim().isEmpty()) { aviso("Complete todos los campos"); return; }
            usuarios.add(new Lector(numero, documento.getText(), nombres.getText(),
                apellidos.getText(), correo.getText(), codigo.getText()));
            mostrar(); aviso("Usuario registrado");
        } catch (NumberFormatException ex) { aviso("El ID debe ser un número"); }
    }

    private void actualizar() {
        try {
            Lector u = buscar(Integer.parseInt(id.getText().trim()));
            if (u == null) { aviso("Usuario no encontrado"); return; }
            if (nombres.getText().trim().isEmpty() || apellidos.getText().trim().isEmpty() ||
                correo.getText().trim().isEmpty()) { aviso("Complete nombres, apellidos y correo"); return; }
            u.actualizarDatos(nombres.getText(), apellidos.getText(), correo.getText());
            mostrar(); aviso("Usuario actualizado");
        } catch (NumberFormatException ex) { aviso("ID inválido"); }
    }

    private void desactivar() {
        try {
            Lector u = buscar(Integer.parseInt(id.getText().trim()));
            if (u == null) { aviso("Usuario no encontrado"); return; }
            u.desactivar(); mostrar(); aviso("Usuario desactivado");
        } catch (NumberFormatException ex) { aviso("ID inválido"); }
    }

    private void mostrar() {
        listado.setText("LISTA DE USUARIOS\n\n");
        for (Lector u : usuarios) {
            listado.append(u.identificarse() + "\n\n");
        }
    }

    private void aviso(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }

}
