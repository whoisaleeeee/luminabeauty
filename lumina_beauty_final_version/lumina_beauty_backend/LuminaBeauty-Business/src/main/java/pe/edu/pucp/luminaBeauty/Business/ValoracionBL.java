package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Valoracion;

import java.util.ArrayList;

public interface ValoracionBL {

    Valoracion registrarValoracion(Valoracion valoracion) throws Exception;

    Valoracion actualizarValoracion(Valoracion valoracion) throws Exception;

    void eliminarValoracion(int idValoracion) throws Exception;

    Valoracion buscarValoracion(int idValoracion) throws Exception;

    ArrayList<Valoracion> listarValoraciones() throws Exception;

    ArrayList<Valoracion> listarValoracionesPorCliente(int idCliente) throws Exception;

    ArrayList<Valoracion> listarValoracionesPorProducto(int idProducto) throws Exception;

    ArrayList<Valoracion> listarValoracionesPorEstado(String estado) throws Exception;

    Valoracion publicarValoracion(int idValoracion) throws Exception;

    Valoracion rechazarValoracion(int idValoracion) throws Exception;

    Valoracion responderValoracion(int idValoracion,
                                   String respuestaTienda,
                                   int idEmpleado) throws Exception;
}

