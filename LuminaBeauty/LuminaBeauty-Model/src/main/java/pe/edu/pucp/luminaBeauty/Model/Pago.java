package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
public class Pago {
    private int id;
    private BigDecimal monto;
    private String estado; // ENUM en SQL: 'PENDIENTE', 'COMPLETADO', etc.
    private LocalDateTime fechaPago;



    // Llaves foráneas para el DAO e INSERT
    private Pedido pedido;
    private MetodoDePago metodoDePago;

    public Pago() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Pedido getPedido() {return pedido;}

    public void setPedido(Pedido pedido) {this.pedido = pedido;}

    public MetodoDePago getMetodoDePago() {return metodoDePago;}

    public void setMetodoDePago(MetodoDePago metodoDePago) {this.metodoDePago = metodoDePago;}
}
