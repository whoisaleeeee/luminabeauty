package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class DetalleCarro {

    private int id_detalle_carrito;
    private int cantidad;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private CarroDeCompras carro;
    private Producto producto;

    public DetalleCarro() {
        this.cantidad = 1;
    }

    public int getId_detalle_carrito() {
        return id_detalle_carrito;
    }

    public void setId_detalle_carrito(int id_detalle_carrito) {
        this.id_detalle_carrito = id_detalle_carrito;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
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

    public CarroDeCompras getCarro() {
        return carro;
    }

    public void setCarro(CarroDeCompras carro) {
        this.carro = carro;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}