package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class SeguimientoReclamo {

    private int id_seguimiento_reclamo;
    private String tipo;
    private String mensaje;
    private String estado_anterior;
    private String estado_nuevo;
    private LocalDateTime fecha_creacion;

    private Reclamo reclamo;
    private Cliente registrado_por_cliente;
    private Empleado registrado_por_empleado;

    public SeguimientoReclamo() {
    }

    public int getId_seguimiento_reclamo() {
        return id_seguimiento_reclamo;
    }

    public void setId_seguimiento_reclamo(int id_seguimiento_reclamo) {
        this.id_seguimiento_reclamo = id_seguimiento_reclamo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
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

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Reclamo getReclamo() {
        return reclamo;
    }

    public void setReclamo(Reclamo reclamo) {
        this.reclamo = reclamo;
    }

    public Cliente getRegistrado_por_cliente() {
        return registrado_por_cliente;
    }

    public void setRegistrado_por_cliente(Cliente registrado_por_cliente) {
        this.registrado_por_cliente = registrado_por_cliente;
    }

    public Empleado getRegistrado_por_empleado() {
        return registrado_por_empleado;
    }

    public void setRegistrado_por_empleado(Empleado registrado_por_empleado) {
        this.registrado_por_empleado = registrado_por_empleado;
    }
}