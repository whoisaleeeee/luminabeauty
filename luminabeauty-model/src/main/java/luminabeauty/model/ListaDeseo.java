package luminabeauty.model;

import java.util.ArrayList;

public class ListaDeseo {
	private int id;
	private int idCliente; // La FK obligatoria en el SQL
	private ArrayList<DetalleLista> detalles;

	public ListaDeseo() {
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