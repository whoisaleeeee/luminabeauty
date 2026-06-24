package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class Devolucion {

    private int id_devolucion;
    private String motivo;
    private String detalle_motivo;
    private String estado;
    private LocalDateTime solicitado_en;
    private LocalDateTime aprobado_en;
    private LocalDateTime recibido_en;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Cliente cliente;
    private Pedido pedido;
    private Reclamo reclamo;
    private Empleado aprobado_por;
    private Empleado recibido_por;

    public Devolucion() {
        this.estado = "SOLICITADA";
    }

    public int getId_devolucion() {
        return id_devolucion;
    }

    public void setId_devolucion(int id_devolucion) {
        this.id_devolucion = id_devolucion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDetalle_motivo() {
        return detalle_motivo;
    }

    public void setDetalle_motivo(String detalle_motivo) {
        this.detalle_motivo = detalle_motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getSolicitado_en() {
        return solicitado_en;
    }

    public void setSolicitado_en(LocalDateTime solicitado_en) {
        this.solicitado_en = solicitado_en;
    }

    public LocalDateTime getAprobado_en() {
        return aprobado_en;
    }

    public void setAprobado_en(LocalDateTime aprobado_en) {
        this.aprobado_en = aprobado_en;
    }

    public LocalDateTime getRecibido_en() {
        return recibido_en;
    }

    public void setRecibido_en(LocalDateTime recibido_en) {
        this.recibido_en = recibido_en;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

    public Empleado getAprobado_por() {
        return aprobado_por;
    }

    public void setAprobado_por(Empleado aprobado_por) {
        this.aprobado_por = aprobado_por;
    }

    public Empleado getRecibido_por() {
        return recibido_por;
    }

    public void setRecibido_por(Empleado recibido_por) {
        this.recibido_por = recibido_por;
    }
}
