package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.HistorialEstadoPedido;

import java.util.ArrayList;

public interface HistorialEstadoPedidoBL {

    HistorialEstadoPedido registrarHistorialEstadoPedido(HistorialEstadoPedido historial) throws Exception;

    HistorialEstadoPedido actualizarHistorialEstadoPedido(HistorialEstadoPedido historial) throws Exception;

    void eliminarHistorialEstadoPedido(int idHistorialEstadoPedido) throws Exception;

    HistorialEstadoPedido buscarHistorialEstadoPedido(int idHistorialEstadoPedido) throws Exception;

    ArrayList<HistorialEstadoPedido> listarHistorialesEstadoPedido() throws Exception;

    ArrayList<HistorialEstadoPedido> listarHistorialesPorPedido(int idPedido) throws Exception;

    ArrayList<HistorialEstadoPedido> listarHistorialesPorEmpleado(int idEmpleado) throws Exception;

    ArrayList<HistorialEstadoPedido> listarHistorialesPorEstadoNuevo(String estadoNuevo) throws Exception;

    HistorialEstadoPedido registrarCambioEstadoPedido(int idPedido,
                                                      String estadoNuevo,
                                                      String comentario,
                                                      Integer idEmpleado) throws Exception;
}

