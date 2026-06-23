package pe.edu.pucp.luminaBeauty.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TarifaEnvio {
    private String zona_envio;
    private BigDecimal costo_base;
    private BigDecimal costo_minimo_envio_gratis;
    private int estado;
    private LocalDateTime fecha_creacion;

    public String getZona_envio() {
        return zona_envio;
    }

    public void setZona_envio(String zona_envio) {
        this.zona_envio = zona_envio;
    }

    public BigDecimal getCosto_base() {
        return costo_base;
    }

    public void setCosto_base(BigDecimal costo_base) {
        this.costo_base = costo_base;
    }

    public BigDecimal getCosto_minimo_envio_gratis() {
        return costo_minimo_envio_gratis;
    }

    public void setCosto_minimo_envio_gratis(BigDecimal costo_minimo_envio_gratis) {
        this.costo_minimo_envio_gratis = costo_minimo_envio_gratis;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }
}
