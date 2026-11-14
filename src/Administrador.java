public class Administrador extends Usuario {
    private String codigoAdministrador;

    public Administrador(int idUsuario, String documento, String nombres, String apellidos, String correo, String codigoAdministrador) {
        super(idUsuario, documento, nombres, apellidos, correo);
        this.codigoAdministrador = codigoAdministrador;
    }
    
}
