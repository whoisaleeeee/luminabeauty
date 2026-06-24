package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Pedido;

import java.util.ArrayList;

public interface PedidoBL {

    Pedido crearPedido(Pedido pedido) throws Exception;

    Pedido buscarPedido(int idPedido) throws Exception;

    ArrayList<Pedido> listarPedidos() throws Exception;

    ArrayList<Pedido> listarPedidosPorCliente(int idCliente) throws Exception;

    void cancelarPedido(int idPedido) throws Exception;

    Pedido actualizarEstadoPedido(int idPedido, String estadoNuevo) throws Exception;
}

