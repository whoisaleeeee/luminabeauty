package pe.edu.pucp.luminaBeauty.Model;

public class Empleado extends Usuario {

    private String rol;

    public Empleado() {
        super();
        this.setTipo_usuario("EMPLEADO");
        this.setEstado(1);
        this.rol = "SOPORTE";
    }

    public int getIdEmpleado() {
        return super.getId_usuario();
    }

    public void setIdEmpleado(int idEmpleado) {
        super.setId_usuario(idEmpleado);
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}