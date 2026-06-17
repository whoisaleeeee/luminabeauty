package pe.edu.pucp.luminaBeauty.Model;

public class Marca {
    private int id;
    private String nombre;
    private String descripcion;
    private String logo;

    public Marca() {}

    // Getters y Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {this.logo = logo;}
}
