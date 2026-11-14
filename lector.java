public class lector extends usuario {
    private String codigoLector;

    public lector(int idUsuario, String documento, String nombres, String apellidos, String correo, String codigoLector) {
        super(idUsuario, documento, nombres, apellidos, correo);
        this.codigoLector = codigoLector;
    }
    
}
