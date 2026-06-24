package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class MovimientoInventario {

    private int id_movimiento_inventario;
    private String tipo_movimiento;
    private int cantidad;
    private int stock_anterior;
    private int stock_posterior;
    private String motivo;
    private LocalDateTime fecha_creacion;

    private Producto producto;
    private Pedido pedido;
    private Devolucion devolucion;
    private Empleado registrado_por;

    public MovimientoInventario() {
    }

    public int getId_movimiento_inventario() {
        return id_movimiento_inventario;
    }

    public void setId_movimiento_inventario(int id_movimiento_inventario) {
        this.id_movimiento_inventario = id_movimiento_inventario;
    }

    public String getTipo_movimiento() {
        return tipo_movimiento;
    }

    public void setTipo_movimiento(String tipo_movimiento) {
        this.tipo_movimiento = tipo_movimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStock_anterior() {
        return stock_anterior;
    }

    public void setStock_anterior(int stock_anterior) {
        this.stock_anterior = stock_anterior;
    }

    public int getStock_posterior() {
        return stock_posterior;
    }

    public void setStock_posterior(int stock_posterior) {
        this.stock_posterior = stock_posterior;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
    }

    public Empleado getRegistrado_por() {
        return registrado_por;
    }

    public void setRegistrado_por(Empleado registrado_por) {
        this.registrado_por = registrado_por;
    }
}
