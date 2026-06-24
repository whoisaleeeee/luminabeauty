package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.DetallePedido;

import java.util.ArrayList;

public interface DetallePedidoBL {

    DetallePedido registrarDetallePedido(DetallePedido detallePedido) throws Exception;

    DetallePedido actualizarDetallePedido(DetallePedido detallePedido) throws Exception;

    void eliminarDetallePedido(int idDetallePedido) throws Exception;

    DetallePedido buscarDetallePedido(int idDetallePedido) throws Exception;

    ArrayList<DetallePedido> listarDetallesPedido() throws Exception;

    ArrayList<DetallePedido> listarDetallesPorPedido(int idPedido) throws Exception;

    ArrayList<DetallePedido> listarDetallesPorProducto(int idProducto) throws Exception;
}

