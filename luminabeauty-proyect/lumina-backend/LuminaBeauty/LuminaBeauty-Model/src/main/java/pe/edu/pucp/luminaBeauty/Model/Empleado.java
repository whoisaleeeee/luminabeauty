package pe.edu.pucp.luminaBeauty.Model;

public class Empleado extends Usuario{
    private String rol; // único de Empleado

    public Empleado() {
        super(); // Llama al constructor de la clase padre
    }

    // ID que viene de la clase Usuario
    public int getIdEmpleado() {
        return super.getId();
    }

    public void setIdEmpleado(int idEmpleado) {
        super.setId(idEmpleado);
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
