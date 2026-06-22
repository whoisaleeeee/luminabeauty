package luminabeauty.model;

import java.math.BigDecimal;

public class DetallePedido {
	private int id;
	private int cantidad;
	private BigDecimal precioUnitario;
	private BigDecimal subtotal;
	private int idPedido; // FK
	private int idProducto; // FK

	// Objeto Producto para facilitar mostrar nombres en reportes o boletas
	private Producto producto;

	public DetallePedido() {
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getCantidad() { return cantidad; }
	public void setCantidad(int cantidad) { this.cantidad = cantidad; }

	public BigDecimal getPrecioUnitario() { return precioUnitario; }
	public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

	public BigDecimal getSubtotal() { return subtotal; }
	public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

	public int getIdPedido() { return idPedido; }
	public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

	public int getIdProducto() { return idProducto; }
	public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

	public Producto getProducto() { return producto; }
	public void setProducto(Producto producto) { this.producto = producto; }
}