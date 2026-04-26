package luminabeauty.model;

import java.util.Date;

public class Valoracion {
	private int id;
	private int calificacion; // 1 a 5
	private String comentario;
	private Date fecha;

	// Llaves foráneas obligatorias para el DAO
	private int idCliente;
	private int idProducto;

	// Objetos para lógica de negocio
	private Cliente cliente;
	private Producto producto;

	public Valoracion() {
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getCalificacion() { return calificacion; }
	public void setCalificacion(int calificacion) { this.calificacion = calificacion; }

	public String getComentario() { return comentario; }
	public void setComentario(String comentario) { this.comentario = comentario; }

	public Date getFecha() { return fecha; }
	public void setFecha(Date fecha) { this.fecha = fecha; }

	public int getIdCliente() { return idCliente; }
	public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

	public int getIdProducto() { return idProducto; }
	public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

	// Getters de Objetos
	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public Producto getProducto() { return producto; }
	public void setProducto(Producto producto) { this.producto = producto; }
}