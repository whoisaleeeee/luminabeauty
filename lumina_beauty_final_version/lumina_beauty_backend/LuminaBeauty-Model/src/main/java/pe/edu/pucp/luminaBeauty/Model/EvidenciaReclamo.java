package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class EvidenciaReclamo {

    private int id_evidencia_reclamo;
    private String url_archivo;
    private String tipo_archivo;
    private String descripcion;
    private LocalDateTime fecha_creacion;

    private Reclamo reclamo;
    private Cliente subido_por_cliente;
    private Empleado subido_por_empleado;

    public EvidenciaReclamo() {
        this.tipo_archivo = "IMAGEN";
    }

    public int getId_evidencia_reclamo() {
        return id_evidencia_reclamo;
    }

    public void setId_evidencia_reclamo(int id_evidencia_reclamo) {
        this.id_evidencia_reclamo = id_evidencia_reclamo;
    }

    public String getUrl_archivo() {
        return url_archivo;
    }

    public void setUrl_archivo(String url_archivo) {
        this.url_archivo = url_archivo;
    }

    public String getTipo_archivo() {
        return tipo_archivo;
    }

    public void setTipo_archivo(String tipo_archivo) {
        this.tipo_archivo = tipo_archivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Cliente getSubido_por_cliente() {
        return subido_por_cliente;
    }

    public void setSubido_por_cliente(Cliente subido_por_cliente) {
        this.subido_por_cliente = subido_por_cliente;
    }

    public Empleado getSubido_por_empleado() {
        return subido_por_empleado;
    }

    public void setSubido_por_empleado(Empleado subido_por_empleado) {
        this.subido_por_empleado = subido_por_empleado;
    }
}

