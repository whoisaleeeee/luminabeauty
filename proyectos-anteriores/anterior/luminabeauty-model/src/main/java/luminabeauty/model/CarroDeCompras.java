package luminabeauty.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class CarroDeCompras {
	private int id;
	private int idCliente; // Obligatorio para la FK en SQL
	private LocalDateTime fechaCreacion;

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

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public ArrayList<DetalleCarro> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleCarro> detalles) {
		this.detalles = detalles;
	}
}