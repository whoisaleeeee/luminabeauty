package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class HistorialEstadoPedido {

    private int id_historial_estado_pedido;
    private String estado_anterior;
    private String estado_nuevo;
    private String comentario;
    private LocalDateTime fecha_creacion;

    private Pedido pedido;
    private Empleado registrado_por;

    public HistorialEstadoPedido() {
    }

    public int getId_historial_estado_pedido() {
        return id_historial_estado_pedido;
    }

    public void setId_historial_estado_pedido(int id_historial_estado_pedido) {
        this.id_historial_estado_pedido = id_historial_estado_pedido;
    }

    public String getEstado_anterior() {
        return estado_anterior;
    }

    public void setEstado_anterior(String estado_anterior) {
        this.estado_anterior = estado_anterior;
    }

    public String getEstado_nuevo() {
        return estado_nuevo;
    }

    public void setEstado_nuevo(String estado_nuevo) {
        this.estado_nuevo = estado_nuevo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Empleado getRegistrado_por() {
        return registrado_por;
    }

    public void setRegistrado_por(Empleado registrado_por) {
        this.registrado_por = registrado_por;
    }
}