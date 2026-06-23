package pe.edu.pucp.luminaBeauty.Model;
import java.time.LocalDateTime;
import java.util.List;

public class CarroDeCompras {
    private int id_carrito;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;
    private LocalDateTime fecha_recordatorio;


    private Cliente cliente;
    private List<DetalleCarro> detalles;

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

    public LocalDateTime getFecha_recordatorio() {
        return fecha_recordatorio;
    }

    public void setFecha_recordatorio(LocalDateTime fecha_recordatorio) {
        this.fecha_recordatorio = fecha_recordatorio;
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
