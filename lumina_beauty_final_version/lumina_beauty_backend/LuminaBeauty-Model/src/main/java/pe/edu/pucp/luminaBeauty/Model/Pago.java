package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
public class Pago {
    private int id_pago;
    private BigDecimal monto;
    private String estado;
    private LocalDateTime fechaPago;
    private String referencia_transaccion;
    private LocalDateTime fecha_pago;
    private LocalDateTime fecha_reembolso;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Pedido pedido;
    private MetodoDePago metodoDePago;

    public Pago() {
    }

    public int getId_pago() {
        return id_pago;
    }

    public void setId_pago(int id_pago) {
        this.id_pago = id_pago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getReferencia_transaccion() {
        return referencia_transaccion;
    }

    public void setReferencia_transaccion(String referencia_transaccion) {
        this.referencia_transaccion = referencia_transaccion;
    }

    public LocalDateTime getFecha_pago() {
        return fecha_pago;
    }

    public void setFecha_pago(LocalDateTime fecha_pago) {
        this.fecha_pago = fecha_pago;
    }

    public LocalDateTime getFecha_reembolso() {
        return fecha_reembolso;
    }

    public void setFecha_reembolso(LocalDateTime fecha_reembolso) {
        this.fecha_reembolso = fecha_reembolso;
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

    public MetodoDePago getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(MetodoDePago metodoDePago) {
        this.metodoDePago = metodoDePago;
    }
}
