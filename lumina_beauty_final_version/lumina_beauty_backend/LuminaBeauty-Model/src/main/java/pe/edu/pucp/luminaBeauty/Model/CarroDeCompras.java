package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CarroDeCompras {

    private int id_carrito;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;
    private LocalDateTime recordatorio_enviado_en;

    private Cliente cliente;
    private List<DetalleCarro> detalles;

    public CarroDeCompras() {
        this.detalles = new ArrayList<>();
    }

    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
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

    public LocalDateTime getRecordatorio_enviado_en() {
        return recordatorio_enviado_en;
    }

    public void setRecordatorio_enviado_en(LocalDateTime recordatorio_enviado_en) {
        this.recordatorio_enviado_en = recordatorio_enviado_en;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<DetalleCarro> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCarro> detalles) {
        this.detalles = detalles;
    }
}