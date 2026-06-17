package pe.edu.pucp.luminaBeauty.Model;

public class DetalleLista {
    private int id;
    private ListaDeDeseos lista;
    private Producto producto;

    public DetalleLista() {}
    public  ListaDeDeseos getLista() {return lista;}

    public void setLista(ListaDeDeseos lista) {this.lista = lista;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {this.producto = producto;}
}
