package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;
import java.util.Date;

public class Valoracion {
    private int id;
    private int calificacion; // 1 a 5
    private String comentario;
    private LocalDateTime fecha;
    private Cliente cliente;
    private Producto producto;



    public Valoracion() {
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCalificacion() { return calificacion; }
    public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }


    // Getters de Objetos
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
