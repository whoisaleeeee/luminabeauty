package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;

public class ImagenProducto {

    private int id_imagen_producto;
    private String url_imagen;
    private String texto_alternativo;
    private int es_principal;
    private int orden_visualizacion;
    private LocalDateTime fecha_creacion;

    private Producto producto;

    public ImagenProducto() {
        this.es_principal = 0;
        this.orden_visualizacion = 1;
    }

    public int getId_imagen_producto() {
        return id_imagen_producto;
    }

    public void setId_imagen_producto(int id_imagen_producto) {
        this.id_imagen_producto = id_imagen_producto;
    }

    public String getUrl_imagen() {
        return url_imagen;
    }

    public void setUrl_imagen(String url_imagen) {
        this.url_imagen = url_imagen;
    }

    public String getTexto_alternativo() {
        return texto_alternativo;
    }

    public void setTexto_alternativo(String texto_alternativo) {
        this.texto_alternativo = texto_alternativo;
    }

    public int getEs_principal() {
        return es_principal;
    }

    public void setEs_principal(int es_principal) {
        this.es_principal = es_principal;
    }

    public int getOrden_visualizacion() {
        return orden_visualizacion;
    }

    public void setOrden_visualizacion(int orden_visualizacion) {
        this.orden_visualizacion = orden_visualizacion;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}