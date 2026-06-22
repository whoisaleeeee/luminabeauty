package pe.edu.pucp.luminaBeauty.Model;
import java.math.BigDecimal;

public class DetalleCarro {
    private int id; // El ID auto-incremental de la tabla
    private int cantidad;
    private BigDecimal precioUnitario; // DECIMAL(10,2) en SQL
    private CarroDeCompras carro; // FK hacia CarroDeCompras
    private Producto producto; // FK hacia Producto

    public Producto getIdProducto() {return producto;}

    public void setIdProducto(Producto idProducto) {this.producto = idProducto;}

    public CarroDeCompras getCarro() {return carro;}

    public void setCarro(CarroDeCompras carro) {this.carro = carro;}

    public DetalleCarro() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }




}
