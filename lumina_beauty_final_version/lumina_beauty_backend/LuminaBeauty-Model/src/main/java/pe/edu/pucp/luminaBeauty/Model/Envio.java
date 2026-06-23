package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;
import java.util.Date;

public class Envio {
    private int id_envio;
    private String zona_envio;
    private String estado;
    private String numero_seguimiento;
    private String direccion_envio;
    private String ciudad_envio;
    private String pais_envio;
    private String referencia_envio;
    private String codigo_postal_envio;
    private LocalDateTime fecha_envio;
    private LocalDateTime fecha_entrega_estimada;
    private LocalDateTime fecha_entrega_real;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Pedido pedido;

    public Envio() {
    }

    public int getId_envio() {
        return id_envio;
    }

    public void setId_envio(int id_envio) {
        this.id_envio = id_envio;
    }

    public String getZona_envio() {
        return zona_envio;
    }

    public void setZona_envio(String zona_envio) {
        this.zona_envio = zona_envio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero_seguimiento() {
        return numero_seguimiento;
    }

    public void setNumero_seguimiento(String numero_seguimiento) {
        this.numero_seguimiento = numero_seguimiento;
    }

    public String getDireccion_envio() {
        return direccion_envio;
    }

    public void setDireccion_envio(String direccion_envio) {
        this.direccion_envio = direccion_envio;
    }

    public String getCiudad_envio() {
        return ciudad_envio;
    }

    public void setCiudad_envio(String ciudad_envio) {
        this.ciudad_envio = ciudad_envio;
    }

    public String getPais_envio() {
        return pais_envio;
    }

    public void setPais_envio(String pais_envio) {
        this.pais_envio = pais_envio;
    }

    public String getReferencia_envio() {
        return referencia_envio;
    }

    public void setReferencia_envio(String referencia_envio) {
        this.referencia_envio = referencia_envio;
    }

    public String getCodigo_postal_envio() {
        return codigo_postal_envio;
    }

    public void setCodigo_postal_envio(String codigo_postal_envio) {
        this.codigo_postal_envio = codigo_postal_envio;
    }

    public LocalDateTime getFecha_envio() {
        return fecha_envio;
    }

    public void setFecha_envio(LocalDateTime fecha_envio) {
        this.fecha_envio = fecha_envio;
    }

    public LocalDateTime getFecha_entrega_estimada() {
        return fecha_entrega_estimada;
    }

    public void setFecha_entrega_estimada(LocalDateTime fecha_entrega_estimada) {
        this.fecha_entrega_estimada = fecha_entrega_estimada;
    }

    public LocalDateTime getFecha_entrega_real() {
        return fecha_entrega_real;
    }

    public void setFecha_entrega_real(LocalDateTime fecha_entrega_real) {
        this.fecha_entrega_real = fecha_entrega_real;
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

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}

