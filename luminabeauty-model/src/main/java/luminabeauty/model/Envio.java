package luminabeauty.model;

import java.time.LocalDateTime;
import java.util.Date;

public class Envio {
	private int id;
	private LocalDateTime fechaEnvio;
	private LocalDateTime fechaEntregaEstimada;
	private LocalDateTime fechaEntregaReal;
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

	public LocalDateTime getFechaEnvio() { return fechaEnvio; }
	public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

	public LocalDateTime getFechaEntregaEstimada() { return fechaEntregaEstimada; }
	public void setFechaEntregaEstimada(LocalDateTime fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

	public LocalDateTime getFechaEntregaReal() { return fechaEntregaReal; }
	public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) { this.fechaEntregaReal = fechaEntregaReal; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public String getNumeroSeguimiento() { return numeroSeguimiento; }
	public void setNumeroSeguimiento(String numeroSeguimiento) { this.numeroSeguimiento = numeroSeguimiento; }

	public int getIdPedido() { return idPedido; }
	public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

	public int getIdDireccion() { return idDireccion; }
	public void setIdDireccion(int idDireccion) { this.idDireccion = idDireccion; }
}