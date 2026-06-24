package pe.edu.pucp.luminaBeauty.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Pedido {

    private int id_pedido;
    private String codigo_pedido;
    private String codigo_cupon_aplicado;
    private BigDecimal subtotal_productos;
    private BigDecimal costo_envio;
    private BigDecimal descuento;
    private BigDecimal total;
    private String estado;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Cliente cliente;
    private Cupon cupon;

    private ArrayList<DetallePedido> detalles;

    public Pedido() {
        this.detalles = new ArrayList<>();
        this.estado = "PENDIENTE";
        this.costo_envio = BigDecimal.ZERO;
        this.descuento = BigDecimal.ZERO;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

    public String getCodigo_pedido() {
        return codigo_pedido;
    }

    public void setCodigo_pedido(String codigo_pedido) {
        this.codigo_pedido = codigo_pedido;
    }

    public String getCodigo_cupon_aplicado() {
        return codigo_cupon_aplicado;
    }

    public void setCodigo_cupon_aplicado(String codigo_cupon_aplicado) {
        this.codigo_cupon_aplicado = codigo_cupon_aplicado;
    }

    public BigDecimal getSubtotal_productos() {
        return subtotal_productos;
    }

    public void setSubtotal_productos(BigDecimal subtotal_productos) {
        this.subtotal_productos = subtotal_productos;
    }

    public BigDecimal getCosto_envio() {
        return costo_envio;
    }

    public void setCosto_envio(BigDecimal costo_envio) {
        this.costo_envio = costo_envio;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
    }

    public ArrayList<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public void calcularTotal() {
        this.total = this.subtotal_productos
                .subtract(this.descuento)
                .add(this.costo_envio);
    }
}