package luminabeauty.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ListaDeDeseos {
	private int id;
	private int idCliente; // La FK obligatoria en el SQL
	private LocalDateTime fechaCreacion;
	private LocalDateTime fechaActualizacion;
	private ArrayList<DetalleLista> detalles;

	public ListaDeDeseos() {
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

	public ArrayList<DetalleLista> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleLista> detalles) {
		this.detalles = detalles;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
}