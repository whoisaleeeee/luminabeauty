package pe.edu.pucp.luminaBeauty.DAO;

import java.util.ArrayList;
import pe.edu.pucp.luminaBeauty.Model.Pedido;

public interface PedidoDAO extends BaseDAO<Pedido, Integer> {

    ArrayList<Pedido> listarPorCliente(int idCliente) throws Exception;
}