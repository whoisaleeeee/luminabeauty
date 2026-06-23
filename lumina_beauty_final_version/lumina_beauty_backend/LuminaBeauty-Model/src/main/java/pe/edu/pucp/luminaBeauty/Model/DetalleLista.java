package pe.edu.pucp.luminaBeauty.Model;

public class DetalleLista {
    private int id_detalle_lista_deseos;

    private ListaDeDeseos lista;
    private Producto producto;

    public DetalleLista() {}
    public  ListaDeDeseos getLista() {return lista;}

    public void setLista(ListaDeDeseos lista) {this.lista = lista;}

    public int getId() {
        return id_detalle_lista_deseos;
    }

    public void setId(int id_detalle_lista_deseos) {
        this.id_detalle_lista_deseos = id_detalle_lista_deseos;
    }

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {this.producto = producto;}
}
