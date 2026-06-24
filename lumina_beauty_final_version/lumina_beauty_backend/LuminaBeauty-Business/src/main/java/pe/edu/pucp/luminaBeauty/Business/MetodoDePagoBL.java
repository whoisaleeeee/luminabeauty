package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;

import java.util.ArrayList;

public interface MetodoDePagoBL {

    MetodoDePago registrarMetodoDePago(MetodoDePago metodoDePago) throws Exception;

    MetodoDePago actualizarMetodoDePago(MetodoDePago metodoDePago) throws Exception;

    void eliminarMetodoDePago(int idMetodoPago) throws Exception;

    MetodoDePago buscarMetodoDePago(int idMetodoPago) throws Exception;

    ArrayList<MetodoDePago> listarMetodosDePago() throws Exception;

    ArrayList<MetodoDePago> listarMetodosDePagoActivos() throws Exception;

    ArrayList<MetodoDePago> buscarMetodosDePagoPorNombre(String nombre) throws Exception;
}
