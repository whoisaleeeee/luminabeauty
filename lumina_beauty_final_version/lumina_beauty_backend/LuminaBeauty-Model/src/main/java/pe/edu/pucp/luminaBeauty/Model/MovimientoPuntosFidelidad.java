package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class MovimientoPuntosFidelidad {

    private int id_movimiento_puntos;
    private String tipo_movimiento;
    private int puntos;
    private int saldo_anterior;
    private int saldo_posterior;
    private String motivo;
    private LocalDateTime fecha_creacion;

    private Cliente cliente;
    private Pedido pedido;
    private Empleado registrado_por;

    public MovimientoPuntosFidelidad() {
    }

    public int getId_movimiento_puntos() {
        return id_movimiento_puntos;
    }

    public void setId_movimiento_puntos(int id_movimiento_puntos) {
        this.id_movimiento_puntos = id_movimiento_puntos;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getSaldo_anterior() {
        return saldo_anterior;
    }

    public void setSaldo_anterior(int saldo_anterior) {
        this.saldo_anterior = saldo_anterior;
    }

    public int getSaldo_posterior() {
        return saldo_posterior;
    }

    public void setSaldo_posterior(int saldo_posterior) {
        this.saldo_posterior = saldo_posterior;
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

    public Empleado getRegistrado_por() {
        return registrado_por;
    }

    public void setRegistrado_por(Empleado registrado_por) {
        this.registrado_por = registrado_por;
    }
}