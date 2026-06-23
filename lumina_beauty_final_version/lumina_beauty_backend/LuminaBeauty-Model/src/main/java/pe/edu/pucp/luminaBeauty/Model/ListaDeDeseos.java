package pe.edu.pucp.luminaBeauty.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ListaDeDeseos {
    private int id_lista_deseos;
    private String nombre;
    private String descripcion;
    private LocalDateTime fecha_creacion;
    private LocalDateTime fecha_actualizacion;

    private Cliente cliente;

    private ArrayList<DetalleLista> detalles;

    public ListaDeDeseos() {
        this.detalles = new ArrayList<>();
    }

    public int getId_lista_deseos() {
        return id_lista_deseos;
    }

    public void setId_lista_deseos(int id_lista_deseos) {
        this.id_lista_deseos = id_lista_deseos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(LocalDateTime fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public LocalDateTime getFecha_actualizacion() {
        return fecha_actualizacion;
    }

    public void setFecha_actualizacion(LocalDateTime fecha_actualizacion) {
        this.fecha_actualizacion = fecha_actualizacion;
    }

    public ArrayList<DetalleLista> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetalleLista> detalles) {
        this.detalles = detalles;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
