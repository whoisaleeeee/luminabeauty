package luminabeauty.model;

import java.util.Date;
import java.util.ArrayList;

public class Pedido {
	private int id;
	private Date fecha;
	private double total;
	private String estado; // ENUM ('PENDIENTE', 'ENVIADO', etc.)

	// Llaves foráneas necesarias para el DAO
	private int idCarrito;
	private int idCupon; // Puede ser null en SQL

	// Relaciones lógicas
	private ArrayList<DetallePedido> detalles;

	public Pedido() {
		this.detalles = new ArrayList<>();
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public Date getFecha() { return fecha; }
	public void setFecha(Date fecha) { this.fecha = fecha; }

	public double getTotal() { return total; }
	public void setTotal(double total) { this.total = total; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public int getIdCarrito() { return idCarrito; }
	public void setIdCarrito(int idCarrito) { this.idCarrito = idCarrito; }

	public int getIdCupon() { return idCupon; }
	public void setIdCupon(int idCupon) { this.idCupon = idCupon; }
}