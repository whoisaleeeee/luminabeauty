package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class Valoracion {

    private int id_valoracion;
    private int calificacion;
    private String comentario;
    private String estado;
    private String respuesta_tienda;
    private LocalDateTime respondido_en;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Cliente cliente;
    private Producto producto;
    private DetallePedido detallePedido;
    private Empleado respondido_por;

    public Valoracion() {
        this.estado = "PENDIENTE";
    }

    public int getId_valoracion() {
        return id_valoracion;
    }

    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuesta_tienda() {
        return respuesta_tienda;
    }

    public void setRespuesta_tienda(String respuesta_tienda) {
        this.respuesta_tienda = respuesta_tienda;
    }

    public LocalDateTime getRespondido_en() {
        return respondido_en;
    }

    public void setRespondido_en(LocalDateTime respondido_en) {
        this.respondido_en = respondido_en;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDateTime getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(LocalDateTime fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
    }

    public Empleado getRespondido_por() {
        return respondido_por;
    }

    public void setRespondido_por(Empleado respondido_por) {
        this.respondido_por = respondido_por;
    }
}