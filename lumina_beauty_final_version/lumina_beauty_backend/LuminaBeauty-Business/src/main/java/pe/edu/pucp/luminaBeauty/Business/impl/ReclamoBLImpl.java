package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ReclamoBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReclamoBLImpl implements ReclamoBL {

    private final ReclamoDAO reclamoDAO = new ReclamoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();

    @Override
    public Reclamo registrarReclamo(Reclamo reclamo) throws Exception {
        try {
            validarDatosReclamo(reclamo);
            validarRelacionesReclamo(reclamo);

            if (reclamo.getEstado() == null || reclamo.getEstado().trim().isEmpty()) {
                reclamo.setEstado("ABIERTO");
            }

            if (reclamo.getPrioridad() == null || reclamo.getPrioridad().trim().isEmpty()) {
                reclamo.setPrioridad("MEDIA");
            }

            reclamo.setTipo(reclamo.getTipo().trim().toUpperCase());
            reclamo.setEstado(reclamo.getEstado().trim().toUpperCase());
            reclamo.setPrioridad(reclamo.getPrioridad().trim().toUpperCase());

            validarTipoReclamo(reclamo.getTipo());
            validarEstadoReclamo(reclamo.getEstado());
            validarPrioridadReclamo(reclamo.getPrioridad());
            validarFechaResolucion(reclamo);

            Reclamo reclamoRegistrado = reclamoDAO.insertar(reclamo);
            TransactionContext.commit();

            return reclamoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reclamo actualizarReclamo(Reclamo reclamo) throws Exception {
        try {
            if (reclamo == null || reclamo.getId_reclamo() <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            Reclamo reclamoExistente = reclamoDAO.buscarPorId(reclamo.getId_reclamo());

            if (reclamoExistente == null) {
                throw new Exception("El reclamo no existe.");
            }

            validarDatosReclamo(reclamo);
            validarRelacionesReclamo(reclamo);

            if (reclamo.getEstado() == null || reclamo.getEstado().trim().isEmpty()) {
                reclamo.setEstado(reclamoExistente.getEstado());
            }

            if (reclamo.getPrioridad() == null || reclamo.getPrioridad().trim().isEmpty()) {
                reclamo.setPrioridad(reclamoExistente.getPrioridad());
            }

            reclamo.setTipo(reclamo.getTipo().trim().toUpperCase());
            reclamo.setEstado(reclamo.getEstado().trim().toUpperCase());
            reclamo.setPrioridad(reclamo.getPrioridad().trim().toUpperCase());

            validarTipoReclamo(reclamo.getTipo());
            validarEstadoReclamo(reclamo.getEstado());
            validarPrioridadReclamo(reclamo.getPrioridad());
            validarFechaResolucion(reclamo);

            Reclamo reclamoActualizado = reclamoDAO.actualizar(reclamo);
            TransactionContext.commit();

            return reclamoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarReclamo(int idReclamo) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            reclamoDAO.eliminar(reclamo);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reclamo buscarReclamo(int idReclamo) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            return reclamoDAO.buscarPorId(idReclamo);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamos() throws Exception {
        try {
            return reclamoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamosPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            ArrayList<Reclamo> resultado = new ArrayList<>();

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getCliente() != null &&
                        reclamo.getCliente().getId_usuario() == idCliente) {
                    resultado.add(reclamo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamosPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            ArrayList<Reclamo> resultado = new ArrayList<>();

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getPedido() != null &&
                        reclamo.getPedido().getId_pedido() == idPedido) {
                    resultado.add(reclamo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamosPorEstado(String estado) throws Exception {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                throw new Exception("El estado del reclamo es obligatorio.");
            }

            estado = estado.trim().toUpperCase();
            validarEstadoReclamo(estado);

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            ArrayList<Reclamo> resultado = new ArrayList<>();

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getEstado() != null &&
                        reclamo.getEstado().equalsIgnoreCase(estado)) {
                    resultado.add(reclamo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamosPorPrioridad(String prioridad) throws Exception {
        try {
            if (prioridad == null || prioridad.trim().isEmpty()) {
                throw new Exception("La prioridad del reclamo es obligatoria.");
            }

            prioridad = prioridad.trim().toUpperCase();
            validarPrioridadReclamo(prioridad);

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            ArrayList<Reclamo> resultado = new ArrayList<>();

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getPrioridad() != null &&
                        reclamo.getPrioridad().equalsIgnoreCase(prioridad)) {
                    resultado.add(reclamo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reclamo cambiarEstadoReclamo(int idReclamo, String estadoNuevo) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            if (estadoNuevo == null || estadoNuevo.trim().isEmpty()) {
                throw new Exception("El nuevo estado del reclamo es obligatorio.");
            }

            estadoNuevo = estadoNuevo.trim().toUpperCase();
            validarEstadoReclamo(estadoNuevo);

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            reclamo.setEstado(estadoNuevo);

            if (esEstadoFinal(estadoNuevo)) {
                reclamo.setResuelto_en(LocalDateTime.now());
            } else {
                reclamo.setResuelto_en(null);
            }

            Reclamo reclamoActualizado = reclamoDAO.actualizar(reclamo);
            TransactionContext.commit();

            return reclamoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al cambiar estado del reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reclamo asignarArea(int idReclamo, String areaNueva) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            if (areaNueva == null || areaNueva.trim().isEmpty()) {
                throw new Exception("El área asignada es obligatoria.");
            }

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            if (reclamo.getTipo() == null || reclamo.getTipo().trim().isEmpty()) {
                reclamo.setTipo("OTRO");
            } else {
                reclamo.setTipo(reclamo.getTipo().trim().toUpperCase());
            }

            reclamo.setArea_asignada(areaNueva.trim());
            reclamo.setEstado("EN_PROCESO");
            reclamo.setResuelto_en(null);

            Reclamo reclamoActualizado = reclamoDAO.actualizar(reclamo);
            TransactionContext.commit();

            return reclamoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al asignar área del reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reclamo cambiarPrioridadReclamo(int idReclamo, String prioridadNueva) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            if (prioridadNueva == null || prioridadNueva.trim().isEmpty()) {
                throw new Exception("La nueva prioridad del reclamo es obligatoria.");
            }

            prioridadNueva = prioridadNueva.trim().toUpperCase();
            validarPrioridadReclamo(prioridadNueva);

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            reclamo.setPrioridad(prioridadNueva);

            Reclamo reclamoActualizado = reclamoDAO.actualizar(reclamo);
            TransactionContext.commit();

            return reclamoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al cambiar prioridad del reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosReclamo(Reclamo reclamo) throws Exception {
        if (reclamo == null) {
            throw new Exception("El reclamo no puede ser nulo.");
        }

        if (reclamo.getCliente() == null ||
                reclamo.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido.");
        }

        if (reclamo.getTipo() == null || reclamo.getTipo().trim().isEmpty()) {
            throw new Exception("El tipo de reclamo es obligatorio.");
        }

        if (reclamo.getAsunto() == null || reclamo.getAsunto().trim().isEmpty()) {
            throw new Exception("El asunto del reclamo es obligatorio.");
        }

        if (reclamo.getDescripcion() == null || reclamo.getDescripcion().trim().isEmpty()) {
            throw new Exception("La descripción del reclamo es obligatoria.");
        }

        if (reclamo.getPedido() != null &&
                reclamo.getPedido().getId_pedido() <= 0) {
            throw new Exception("El pedido asociado no es válido.");
        }

        if (reclamo.getDetallePedido() != null &&
                reclamo.getDetallePedido().getId_detalle_pedido() <= 0) {
            throw new Exception("El detalle de pedido asociado no es válido.");
        }
    }

    private void validarRelacionesReclamo(Reclamo reclamo) throws Exception {
        Cliente cliente = clienteDAO.buscarPorId(reclamo.getCliente().getId_usuario());

        if (cliente == null) {
            throw new Exception("El cliente asociado al reclamo no existe.");
        }

        if (reclamo.getPedido() != null) {
            Pedido pedido = pedidoDAO.buscarPorId(reclamo.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al reclamo no existe.");
            }

            if (pedido.getCliente() != null &&
                    pedido.getCliente().getId_usuario() != cliente.getId_usuario()) {
                throw new Exception("El pedido no pertenece al cliente indicado.");
            }
        }

        if (reclamo.getDetallePedido() != null) {
            DetallePedido detallePedido = detallePedidoDAO.buscarPorId(
                    reclamo.getDetallePedido().getId_detalle_pedido()
            );

            if (detallePedido == null) {
                throw new Exception("El detalle de pedido asociado al reclamo no existe.");
            }
        }
    }

    private void validarTipoReclamo(String tipo) throws Exception {
        if (!tipo.equals("PRODUCTO_DANADO") &&
                !tipo.equals("PRODUCTO_INCORRECTO") &&
                !tipo.equals("FALTANTE") &&
                !tipo.equals("DEMORA_ENVIO") &&
                !tipo.equals("COBRO_INCORRECTO") &&
                !tipo.equals("OTRO")) {

            throw new Exception("Tipo de reclamo no válido.");
        }
    }

    private void validarEstadoReclamo(String estado) throws Exception {
        if (!estado.equals("ABIERTO") &&
                !estado.equals("EN_REVISION") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("RESUELTO") &&
                !estado.equals("CERRADO") &&
                !estado.equals("RECHAZADO")) {

            throw new Exception("Estado de reclamo no válido.");
        }
    }

    private void validarPrioridadReclamo(String prioridad) throws Exception {
        if (!prioridad.equals("BAJA") &&
                !prioridad.equals("MEDIA") &&
                !prioridad.equals("ALTA")) {

            throw new Exception("Prioridad de reclamo no válida.");
        }
    }

    private void validarFechaResolucion(Reclamo reclamo) {
        if (esEstadoFinal(reclamo.getEstado()) && reclamo.getResuelto_en() == null) {
            reclamo.setResuelto_en(LocalDateTime.now());
        }

        if (!esEstadoFinal(reclamo.getEstado())) {
            reclamo.setResuelto_en(null);
        }
    }

    private boolean esEstadoFinal(String estado) {
        return estado.equals("RESUELTO") ||
                estado.equals("CERRADO") ||
                estado.equals("RECHAZADO");
    }
}
