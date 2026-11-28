public class Lector extends Usuario {
    private String codigoLector;

    public Lector(int idUsuario, String documento, String nombres, String apellidos, String correo, String codigoLector) {
        super(idUsuario, documento, nombres, apellidos, correo);
        this.codigoLector = codigoLector;
    }
    
    public String getCodigoLector() {
        return codigoLector;
    }
}
