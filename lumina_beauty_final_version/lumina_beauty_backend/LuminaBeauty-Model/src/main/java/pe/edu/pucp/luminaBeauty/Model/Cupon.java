package pe.edu.pucp.luminaBeauty.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cupon {

    private int id_cupon;
    private String codigo;
    private String tipo_descuento;
    private BigDecimal valor_descuento;
    private LocalDateTime fecha_inicio;
    private LocalDateTime fecha_fin;
    private Integer limite_uso;
    private int estado;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    public Cupon() {
        this.estado = 1;
    }

    public int getId_cupon() {
        return id_cupon;
    }

    public void setId_cupon(int id_cupon) {
        this.id_cupon = id_cupon;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo_descuento() {
        return tipo_descuento;
    }

    public void setTipo_descuento(String tipo_descuento) {
        this.tipo_descuento = tipo_descuento;
    }

    public BigDecimal getValor_descuento() {
        return valor_descuento;
    }

    public void setValor_descuento(BigDecimal valor_descuento) {
        this.valor_descuento = valor_descuento;
    }

    public LocalDateTime getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(LocalDateTime fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public LocalDateTime getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(LocalDateTime fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Integer getLimite_uso() {
        return limite_uso;
    }

    public void setLimite_uso(Integer limite_uso) {
        this.limite_uso = limite_uso;
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

    public LocalDateTime getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(LocalDateTime fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }
}