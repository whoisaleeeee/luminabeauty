package pe.edu.pucp.luminaBeauty.Model;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String nombre;
    private String slug;
    private String descripcion;
    private BigDecimal precio;
    private int stock;
    private String tipoPiel;
    private String imagen;
    private int estado;
    // IDs de las llaves foráneas
    private CategoriaProducto categoria;
    private Marca marca;
    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }



    public Producto() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getTipoPiel() { return tipoPiel; }
    public void setTipoPiel(String tipoPiel) { this.tipoPiel = tipoPiel; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

}
