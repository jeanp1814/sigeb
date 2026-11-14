public class administrador extends usuario {
    private String codigoAdministrador;

    public administrador(int idUsuario, String documento, String nombres, String apellidos, String correo, String codigoAdministrador) {
        super(idUsuario, documento, nombres, apellidos, correo);
        this.codigoAdministrador = codigoAdministrador;
    }
    
}
