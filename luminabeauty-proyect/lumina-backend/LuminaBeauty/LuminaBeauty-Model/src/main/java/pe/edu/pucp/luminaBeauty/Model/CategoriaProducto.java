package pe.edu.pucp.luminaBeauty.Model;

public class CategoriaProducto {
    private int id;
    private String nombre;
    private String descripcion;
    private int idCategoriaPadre;

    public CategoriaProducto(){
    }

    public int getId(){
        return this.id;
    }

    public void setId(int idCategoria){
        this.id = idCategoria;
    }

    public String getNombre(){
        return this.nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }

    public int getIdCategoriaPadre() {
        return idCategoriaPadre;
    }

    public void setIdCategoriaPadre(int idCategoriaPadre) {
        this.idCategoriaPadre = idCategoriaPadre;
    }
}
