public abstract class Usuario{
    private int idUsuario;
    private String documento;
    private String nombres;
    private String apellidos;
    private String correo;
    private boolean activo;

    public Usuario(int idUsuario, String documento, String nombres, String apellidos, String correo) {
        this.idUsuario = idUsuario;
        this.documento = documento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.activo = true;
    }

    public void actualizarDatos(String nombres, String apellidos, String correo){
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String identificarse(){
        return "ID: " + idUsuario + "\nDocumento: " + documento + "\nNombres: " + nombres + "\nApellidos: " + apellidos + "\nCorreo: " + correo + "\nActivo: " + activo;
    }

    public void desactivar(){
        activo = false;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean getActivo() {
        return activo;
    }
}