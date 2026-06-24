
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.DetalleLista;

import java.util.ArrayList;

public interface DetalleListaBL {

    DetalleLista registrarDetalleLista(DetalleLista detalleLista) throws Exception;

    DetalleLista actualizarDetalleLista(DetalleLista detalleLista) throws Exception;

    void eliminarDetalleLista(int idDetalleLista) throws Exception;

    DetalleLista buscarDetalleLista(int idDetalleLista) throws Exception;

    ArrayList<DetalleLista> listarDetallesLista() throws Exception;

    ArrayList<DetalleLista> listarDetallesPorLista(int idListaDeDeseos) throws Exception;

    ArrayList<DetalleLista> listarDetallesPorProducto(int idProducto) throws Exception;
}

