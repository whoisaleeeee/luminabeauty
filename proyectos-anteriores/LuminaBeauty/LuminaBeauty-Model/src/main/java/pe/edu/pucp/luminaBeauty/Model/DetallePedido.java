package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;

public class DetallePedido {
    private int id;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private Pedido pedido; // FK
    private Producto producto; // FK


    public DetallePedido() {
    }

    // Getters y Setters
    public Pedido getPedido() {return pedido;}

    public void setPedido(Pedido pedido) {this.pedido = pedido;}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
