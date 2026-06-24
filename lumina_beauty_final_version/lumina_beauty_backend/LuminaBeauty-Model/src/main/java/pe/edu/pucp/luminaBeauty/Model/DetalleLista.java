package pe.edu.pucp.luminaBeauty.Model;

public class DetalleLista {

    private int id_detalle_lista_deseos;

    private ListaDeDeseos lista;
    private Producto producto;

    public DetalleLista() {
    }

    public int getId_detalle_lista_deseos() {
        return id_detalle_lista_deseos;
    }

    public void setId_detalle_lista_deseos(int id_detalle_lista_deseos) {
        this.id_detalle_lista_deseos = id_detalle_lista_deseos;
    }

    public ListaDeDeseos getLista() {
        return lista;
    }

    public void setLista(ListaDeDeseos lista) {
        this.lista = lista;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}