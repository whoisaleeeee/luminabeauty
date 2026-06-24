
package pe.edu.pucp.luminaBeauty.Model;
public class DetalleDevolucion {

    private int id_detalle_devolucion;
    private int cantidad_solicitada;
    private Integer cantidad_recibida;
    private String condicion_producto;
    private String observacion;

    private Devolucion devolucion;
    private DetallePedido detallePedido;

    public DetalleDevolucion() {
        this.condicion_producto = "SIN_EVALUAR";
    }

    public int getId_detalle_devolucion() {
        return id_detalle_devolucion;
    }

    public void setId_detalle_devolucion(int id_detalle_devolucion) {
        this.id_detalle_devolucion = id_detalle_devolucion;
    }

    public int getCantidad_solicitada() {
        return cantidad_solicitada;
    }

    public void setCantidad_solicitada(int cantidad_solicitada) {
        this.cantidad_solicitada = cantidad_solicitada;
    }

    public Integer getCantidad_recibida() {
        return cantidad_recibida;
    }

    public void setCantidad_recibida(Integer cantidad_recibida) {
        this.cantidad_recibida = cantidad_recibida;
    }

    public String getCondicion_producto() {
        return condicion_producto;
    }

    public void setCondicion_producto(String condicion_producto) {
        this.condicion_producto = condicion_producto;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Devolucion getDevolucion() {
        return devolucion;
    }

    public void setDevolucion(Devolucion devolucion) {
        this.devolucion = devolucion;
    }

    public DetallePedido getDetallePedido() {
        return detallePedido;
    }

    public void setDetallePedido(DetallePedido detallePedido) {
        this.detallePedido = detallePedido;
    }
}

