package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.PagoBL;
import pe.edu.pucp.luminaBeauty.DAO.MetodoDePagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.MetodoDePagoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PagoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PagoBLImpl implements PagoBL {

    private final PagoDAO pagoDAO = new PagoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final MetodoDePagoDAO metodoDePagoDAO = new MetodoDePagoDAOImpl();

    @Override
    public Pago registrarPago(Pago pago) throws Exception {
        try {
            validarDatosPago(pago);
            validarRelacionesPago(pago);

            if (pago.getEstado() == null || pago.getEstado().trim().isEmpty()) {
                pago.setEstado("PENDIENTE");
            }

            pago.setEstado(pago.getEstado().trim().toUpperCase());
            validarEstadoPago(pago.getEstado());
            validarFechasSegunEstado(pago);

            Pago pagoPedido = buscarPagoPorPedidoInterno(pago.getPedido().getId_pedido());

            if (pagoPedido != null) {
                throw new Exception("Este pedido ya tiene un pago registrado.");
            }

            Pago pagoRegistrado = pagoDAO.insertar(pago);
            TransactionContext.commit();

            return pagoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago actualizarPago(Pago pago) throws Exception {
        try {
            if (pago == null || pago.getId_pago() <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pagoExistente = pagoDAO.buscarPorId(pago.getId_pago());

            if (pagoExistente == null) {
                throw new Exception("El pago no existe.");
            }

            validarDatosPago(pago);
            validarRelacionesPago(pago);

            if (pago.getEstado() == null || pago.getEstado().trim().isEmpty()) {
                pago.setEstado(pagoExistente.getEstado());
            }

            pago.setEstado(pago.getEstado().trim().toUpperCase());
            validarEstadoPago(pago.getEstado());
            validarFechasSegunEstado(pago);

            Pago pagoPedido = buscarPagoPorPedidoInterno(pago.getPedido().getId_pedido());

            if (pagoPedido != null && pagoPedido.getId_pago() != pago.getId_pago()) {
                throw new Exception("Este pedido ya tiene otro pago registrado.");
            }

            Pago pagoActualizado = pagoDAO.actualizar(pago);
            TransactionContext.commit();

            return pagoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarPago(int idPago) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pago = pagoDAO.buscarPorId(idPago);

            if (pago == null) {
                throw new Exception("El pago no existe.");
            }

            pagoDAO.eliminar(pago);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago buscarPago(int idPago) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            return pagoDAO.buscarPorId(idPago);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pago> listarPagos() throws Exception {
        try {
            return pagoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pago> listarPagosPorEstado(String estado) throws Exception {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                throw new Exception("El estado del pago es obligatorio.");
            }

            estado = estado.trim().toUpperCase();
            validarEstadoPago(estado);

            ArrayList<Pago> pagos = pagoDAO.listarTodos();
            ArrayList<Pago> resultado = new ArrayList<>();

            for (Pago pago : pagos) {
                if (pago.getEstado() != null &&
                        pago.getEstado().equalsIgnoreCase(estado)) {
                    resultado.add(pago);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pago> listarPagosPorMetodoPago(int idMetodoPago) throws Exception {
        try {
            if (idMetodoPago <= 0) {
                throw new Exception("El ID del método de pago no es válido.");
            }

            MetodoDePago metodoDePago = metodoDePagoDAO.buscarPorId(idMetodoPago);

            if (metodoDePago == null) {
                throw new Exception("El método de pago no existe.");
            }

            ArrayList<Pago> pagos = pagoDAO.listarTodos();
            ArrayList<Pago> resultado = new ArrayList<>();

            for (Pago pago : pagos) {
                if (pago.getMetodoDePago() != null &&
                        pago.getMetodoDePago().getId_metodo_pago() == idMetodoPago) {
                    resultado.add(pago);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago buscarPagoPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            return buscarPagoPorPedidoInterno(idPedido);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago completarPago(int idPago, String referenciaTransaccion) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pago = pagoDAO.buscarPorId(idPago);

            if (pago == null) {
                throw new Exception("El pago no existe.");
            }

            if ("REEMBOLSADO".equalsIgnoreCase(pago.getEstado())) {
                throw new Exception("No se puede completar un pago reembolsado.");
            }

            if (referenciaTransaccion == null || referenciaTransaccion.trim().isEmpty()) {
                throw new Exception("La referencia de transacción es obligatoria.");
            }

            pago.setEstado("COMPLETADO");
            pago.setReferencia_transaccion(referenciaTransaccion.trim());
            pago.setFecha_pago(LocalDateTime.now());
            pago.setFecha_reembolso(null);

            Pago pagoActualizado = pagoDAO.actualizar(pago);
            TransactionContext.commit();

            return pagoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al completar pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago marcarPagoFallido(int idPago) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pago = pagoDAO.buscarPorId(idPago);

            if (pago == null) {
                throw new Exception("El pago no existe.");
            }

            if ("COMPLETADO".equalsIgnoreCase(pago.getEstado()) ||
                    "REEMBOLSADO".equalsIgnoreCase(pago.getEstado())) {
                throw new Exception("No se puede marcar como fallido un pago completado o reembolsado.");
            }

            pago.setEstado("FALLIDO");
            pago.setFecha_pago(null);
            pago.setFecha_reembolso(null);

            Pago pagoActualizado = pagoDAO.actualizar(pago);
            TransactionContext.commit();

            return pagoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al marcar pago como fallido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Pago reembolsarPago(int idPago) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pago = pagoDAO.buscarPorId(idPago);

            if (pago == null) {
                throw new Exception("El pago no existe.");
            }

            if (!"COMPLETADO".equalsIgnoreCase(pago.getEstado())) {
                throw new Exception("Solo se puede reembolsar un pago completado.");
            }

            if (pago.getFecha_pago() == null) {
                pago.setFecha_pago(LocalDateTime.now());
            }

            pago.setEstado("REEMBOLSADO");
            pago.setFecha_reembolso(LocalDateTime.now());

            Pago pagoActualizado = pagoDAO.actualizar(pago);
            TransactionContext.commit();

            return pagoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al reembolsar pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosPago(Pago pago) throws Exception {
        if (pago == null) {
            throw new Exception("El pago no puede ser nulo.");
        }

        if (pago.getPedido() == null || pago.getPedido().getId_pedido() <= 0) {
            throw new Exception("Debe asignar un pedido válido.");
        }

        if (pago.getMetodoDePago() == null ||
                pago.getMetodoDePago().getId_metodo_pago() <= 0) {
            throw new Exception("Debe asignar un método de pago válido.");
        }

        if (pago.getMonto() == null ||
                pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto del pago debe ser mayor a cero.");
        }
    }

    private void validarRelacionesPago(Pago pago) throws Exception {
        Pedido pedido = pedidoDAO.buscarPorId(pago.getPedido().getId_pedido());

        if (pedido == null) {
            throw new Exception("El pedido asociado al pago no existe.");
        }

        MetodoDePago metodoDePago = metodoDePagoDAO.buscarPorId(
                pago.getMetodoDePago().getId_metodo_pago()
        );

        if (metodoDePago == null) {
            throw new Exception("El método de pago asociado no existe.");
        }

        pago.setPedido(pedido);
        pago.setMetodoDePago(metodoDePago);
    }

    private void validarEstadoPago(String estado) throws Exception {
        if (!estado.equals("PENDIENTE") &&
                !estado.equals("COMPLETADO") &&
                !estado.equals("FALLIDO") &&
                !estado.equals("REEMBOLSADO")) {

            throw new Exception("Estado de pago no válido.");
        }
    }

    private void validarFechasSegunEstado(Pago pago) {
        String estado = pago.getEstado();

        if ("PENDIENTE".equals(estado) || "FALLIDO".equals(estado)) {
            pago.setFecha_pago(null);
            pago.setFecha_reembolso(null);
        }

        if ("COMPLETADO".equals(estado)) {
            if (pago.getFecha_pago() == null) {
                pago.setFecha_pago(LocalDateTime.now());
            }

            pago.setFecha_reembolso(null);
        }

        if ("REEMBOLSADO".equals(estado)) {
            if (pago.getFecha_pago() == null) {
                pago.setFecha_pago(LocalDateTime.now());
            }

            if (pago.getFecha_reembolso() == null) {
                pago.setFecha_reembolso(LocalDateTime.now());
            }
        }
    }

    private Pago buscarPagoPorPedidoInterno(int idPedido) throws Exception {
        ArrayList<Pago> pagos = pagoDAO.listarTodos();

        for (Pago pago : pagos) {
            if (pago.getPedido() != null &&
                    pago.getPedido().getId_pedido() == idPedido) {
                return pago;
            }
        }

        return null;
    }
}
