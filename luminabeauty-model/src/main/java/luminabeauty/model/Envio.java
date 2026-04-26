package luminabeauty.model;

import java.util.Date;

public class Envio {
	private int id;
	private Date fechaEnvio;
	private Date fechaEntregaEstimada;
	private Date fechaEntregaReal;
	private String estado; // ENUM: 'PREPARANDO', 'DESPACHADO', etc.
	private String numeroSeguimiento;

	// Llaves foráneas fundamentales para el INSERT/SELECT
	private int idPedido;
	private int idDireccion;

	public Envio() {
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public Date getFechaEnvio() { return fechaEnvio; }
	public void setFechaEnvio(Date fechaEnvio) { this.fechaEnvio = fechaEnvio; }

	public Date getFechaEntregaEstimada() { return fechaEntregaEstimada; }
	public void setFechaEntregaEstimada(Date fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

	public Date getFechaEntregaReal() { return fechaEntregaReal; }
	public void setFechaEntregaReal(Date fechaEntregaReal) { this.fechaEntregaReal = fechaEntregaReal; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public String getNumeroSeguimiento() { return numeroSeguimiento; }
	public void setNumeroSeguimiento(String numeroSeguimiento) { this.numeroSeguimiento = numeroSeguimiento; }

	public int getIdPedido() { return idPedido; }
	public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

	public int getIdDireccion() { return idDireccion; }
	public void setIdDireccion(int idDireccion) { this.idDireccion = idDireccion; }
}