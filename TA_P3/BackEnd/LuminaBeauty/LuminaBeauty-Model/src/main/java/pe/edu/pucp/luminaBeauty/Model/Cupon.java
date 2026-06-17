package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
public class Cupon {
    private int id;
    private String codigo;
    private String tipoDescuento; // 'PORCENTAJE' o 'MONTO_FIJO'
    private BigDecimal valorDescuento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
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

    public BigDecimal getValorDescuento() { return valorDescuento; }
    public void setValorDescuento(BigDecimal valorDescuento) { this.valorDescuento = valorDescuento; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getLimiteUso() { return limiteUso; }
    public void setLimiteUso(int limiteUso) { this.limiteUso = limiteUso; }

    public int getUsosActuales() { return usosActuales; }
    public void setUsosActuales(int usosActuales) { this.usosActuales = usosActuales; }
}
