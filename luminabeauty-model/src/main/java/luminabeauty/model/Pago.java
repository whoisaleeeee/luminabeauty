package luminabeauty.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Pago {
	private int id;
	private BigDecimal monto;
	private String estado; // ENUM en SQL: 'PENDIENTE', 'COMPLETADO', etc.
	private LocalDateTime fechaPago;

	// Llaves foráneas para el DAO e INSERT
	private int idPedido;
	private int idMetodo;

	public Pago() {
	}

	// Getters y Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(LocalDateTime fechaPago) {
		this.fechaPago = fechaPago;
	}

	public int getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}

	public int getIdMetodo() {
		return idMetodo;
	}

	public void setIdMetodo(int idMetodo) {
		this.idMetodo = idMetodo;
	}
}