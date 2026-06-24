package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Reembolso {

    private int id_reembolso;
    private BigDecimal monto;
    private String estado;
    private String referencia_transaccion;
    private LocalDateTime procesado_en;
    private String motivo;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Pago pago;
    private Devolucion devolucion;
    private Empleado procesado_por;

    public Reembolso() {
        this.estado = "PENDIENTE";
    }

    public int getId_reembolso() {
        return id_reembolso;
    }

    public void setId_reembolso(int id_reembolso) {
        this.id_reembolso = id_reembolso;
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

    public String getReferencia_transaccion() {
        return referencia_transaccion;
    }

    public void setReferencia_transaccion(String referencia_transaccion) {
        this.referencia_transaccion = referencia_transaccion;
    }

    public LocalDateTime getProcesado_en() {
        return procesado_en;
    }

    public void setProcesado_en(LocalDateTime procesado_en) {
        this.procesado_en = procesado_en;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
    }

    public Empleado getProcesado_por() {
        return procesado_por;
    }

    public void setProcesado_por(Empleado procesado_por) {
        this.procesado_por = procesado_por;
    }
}

