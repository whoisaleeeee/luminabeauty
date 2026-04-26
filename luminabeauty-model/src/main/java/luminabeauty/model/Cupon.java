package luminabeauty.model;

import java.util.Date;

public class Cupon {
	private int id;
	private String codigo;
	private String tipoDescuento; // 'PORCENTAJE' o 'MONTO_FIJO'
	private double valorDescuento;
	private Date fechaInicio;
	private Date fechaFin;
	private String estado; // 'ACTIVO', 'INACTIVO', 'EXPIRADO'
	private int limiteUso;
	private int usosActuales;

	public Cupon() {
	}

	// Getters y Setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getCodigo() { return codigo; }
	public void setCodigo(String codigo) { this.codigo = codigo; }

	public String getTipoDescuento() { return tipoDescuento; }
	public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }

	public double getValorDescuento() { return valorDescuento; }
	public void setValorDescuento(double valorDescuento) { this.valorDescuento = valorDescuento; }

	public Date getFechaInicio() { return fechaInicio; }
	public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

	public Date getFechaFin() { return fechaFin; }
	public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public int getLimiteUso() { return limiteUso; }
	public void setLimiteUso(int limiteUso) { this.limiteUso = limiteUso; }

	public int getUsosActuales() { return usosActuales; }
	public void setUsosActuales(int usosActuales) { this.usosActuales = usosActuales; }
}