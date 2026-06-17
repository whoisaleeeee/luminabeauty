package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.PagoBL;
import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.PagoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class PagoBLImpl implements PagoBL {

    private PagoDAO pagoDAO = new PagoDAOImpl();
    private PedidoDAO pedidoDAO = new PedidoDAOImpl();

    @Override
    public Pago registrarPago(Pago pago) throws Exception {
        try {
            if (pago == null) {
                throw new Exception("El pago no puede ser nulo.");
            }

            if (pago.getPedido() == null) {
                throw new Exception("El pago debe tener un pedido.");
            }

            if (pago.getMetodoDePago() == null) {
                throw new Exception("Debe seleccionar un método de pago.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(pago.getPedido().getId());

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            if (pago.getMonto().compareTo(pedido.getTotal()) < 0) {
                throw new Exception("El monto pagado es menor al total del pedido.");
            }

            if (pago.getEstado() == null) {
                pago.setEstado("COMPLETADO");
            }

            pagoDAO.insertar(pago);

            pedido.setEstado("CONFIRMADO");
            pedidoDAO.actualizar(pedido);

            TransactionContext.commit();

            return pago;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar el pago.", ex);
        } finally {
            TransactionContext.close();
        }
    }
}