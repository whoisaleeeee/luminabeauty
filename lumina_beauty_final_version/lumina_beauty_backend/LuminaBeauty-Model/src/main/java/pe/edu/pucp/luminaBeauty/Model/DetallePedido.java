package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;

public class DetallePedido {
    private int id_detalle_pedido;
    private String nombre_producto;
    private String sku_producto;
    private int cantidad;
    private BigDecimal precio_unitario;

    private Pedido pedido;
    private Producto producto;

    public DetallePedido() {
    }

    public int getId_detalle_pedido() {
        return id_detalle_pedido;
    }

    public void setId_detalle_pedido(int id_detalle_pedido) {
        this.id_detalle_pedido = id_detalle_pedido;
    }

    public String getNombre_producto() {
        return nombre_producto;
    }

    public void setNombre_producto(String nombre_producto) {
        this.nombre_producto = nombre_producto;
    }

    public String getSku_producto() {
        return sku_producto;
    }

    public void setSku_producto(String sku_producto) {
        this.sku_producto = sku_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precio_unitario;
    }

    public void setPrecioUnitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
