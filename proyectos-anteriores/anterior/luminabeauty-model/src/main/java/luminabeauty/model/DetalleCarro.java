package luminabeauty.model;

import java.math.BigDecimal;

public class DetalleCarro {
	private int id; // El ID auto-incremental de la tabla
	private int cantidad;
	private BigDecimal precioUnitario; // DECIMAL(10,2) en SQL
	private int idCarro; // FK hacia CarroDeCompras
	private int idProducto; // FK hacia Producto

	// Opcional
	private Producto producto;

	public DetalleCarro() {
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getCantidad() { return cantidad; }
	public void setCantidad(int cantidad) { this.cantidad = cantidad; }

	public BigDecimal getPrecioUnitario() { return precioUnitario; }
	public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

	public int getIdCarro() { return idCarro; }
	public void setIdCarro(int idCarro) { this.idCarro = idCarro; }

	public int getIdProducto() { return idProducto; }
	public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

	public Producto getProducto() { return producto; }
	public void setProducto(Producto producto) { this.producto = producto; }
}