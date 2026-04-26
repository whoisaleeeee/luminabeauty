package luminabeauty.model;

import java.util.ArrayList;
import java.util.Date;

public class CarroDeCompras {
	private int id;
	private int idCliente; // Obligatorio para la FK en SQL
	private Date fechaCreacion;

	private ArrayList<DetalleCarro> detalles;

	public CarroDeCompras() {
		this.detalles = new ArrayList<>();
	}

	// Getters y Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public ArrayList<DetalleCarro> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleCarro> detalles) {
		this.detalles = detalles;
	}
}