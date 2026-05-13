package pe.edu.pucp.luminaBeauty.Model;

public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String dni;
    private String telefono;
    private int estado;

    public Usuario() {
    }

    // ID
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Nombre
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Apellido
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    // Correo
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    // Contraseña
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    // DNI
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    // Teléfono
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Estado
    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}
