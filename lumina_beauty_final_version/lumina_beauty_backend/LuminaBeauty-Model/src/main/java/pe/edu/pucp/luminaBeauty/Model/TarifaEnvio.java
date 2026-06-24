package pe.edu.pucp.luminaBeauty.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TarifaEnvio {

    private String zona_envio;
    private BigDecimal costo_base;
    private BigDecimal monto_minimo_envio_gratis;
    private int estado;
    private LocalDateTime fecha_actualizacion;

    public TarifaEnvio() {
        this.estado = 1;
    }

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

    public BigDecimal getMonto_minimo_envio_gratis() {
        return monto_minimo_envio_gratis;
    }

    public void setMonto_minimo_envio_gratis(BigDecimal monto_minimo_envio_gratis) {
        this.monto_minimo_envio_gratis = monto_minimo_envio_gratis;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(LocalDateTime fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
}