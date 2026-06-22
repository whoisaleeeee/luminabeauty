package pe.edu.pucp.luminaBeauty.Model;
import java.time.LocalDateTime;
import java.util.Date;
public class ComprobanteDePago {
    private int id;
    private String tipo; // 'BOLETA', 'FACTURA', 'TICKET'
    private String serie;
    private int numero;
    private LocalDateTime fechaEmision;

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    //  (Relación 1 a 1 con Pedido)
    private Pedido pedido;

    public ComprobanteDePago() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }


}
