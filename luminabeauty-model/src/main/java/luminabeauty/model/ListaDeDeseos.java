package luminabeauty.model;

import java.util.ArrayList;

public class ListaDeDeseos {
	private int id;
	private int idCliente; // La FK obligatoria en el SQL
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
}