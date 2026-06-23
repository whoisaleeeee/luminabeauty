package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class UsoCupon {
    private int id_uso_cupon;
    private LocalDateTime fecha_uso;

    private Cupon cupon;
    private Cliente cliente;
    private Pedido pedido;

    public int getId_uso_cupon() {
        return id_uso_cupon;
    }

    public void setId_uso_cupon(int id_uso_cupon) {
        this.id_uso_cupon = id_uso_cupon;
    }

    public LocalDateTime getFecha_uso() {
        return fecha_uso;
    }

    public void setFecha_uso(LocalDateTime fecha_uso) {
        this.fecha_uso = fecha_uso;
    }

    public Cupon getCupon() {
        return cupon;
    }

    public void setCupon(Cupon cupon) {
        this.cupon = cupon;
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
}
