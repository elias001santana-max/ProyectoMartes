package BD;


public class Usuario {
    private String usuario;
    private String password;
    private String nombre;
	public Object frame;

    public Usuario(String usuario, String password, String nombre) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
    }

    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }

	public void setNombreUsuario(String nombre2) {
		// TODO Auto-generated method stub
		
	}
}
