package pe.edu.pucp.luminaBeauty.Model;

public class Empleado extends Usuario{
    private String tipo_usuario;
    private String rol; // único de Empleado

    public Empleado() {
        super(); // Llama al constructor de la clase padre
    }


    public String getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(String tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public int getIdEmpleado() {
        return super.getId_usuario();
    }

    public void setIdEmpleado(int idEmpleado) {
        super.setId_usuario(idEmpleado);
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
