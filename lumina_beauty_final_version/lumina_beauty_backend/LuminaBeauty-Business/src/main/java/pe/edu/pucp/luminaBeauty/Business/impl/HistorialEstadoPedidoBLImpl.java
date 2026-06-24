package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.HistorialEstadoPedidoBL;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.HistorialEstadoPedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.HistorialEstadoPedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.HistorialEstadoPedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class HistorialEstadoPedidoBLImpl implements HistorialEstadoPedidoBL {

    private final HistorialEstadoPedidoDAO historialEstadoPedidoDAO = new HistorialEstadoPedidoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public HistorialEstadoPedido registrarHistorialEstadoPedido(HistorialEstadoPedido historial) throws Exception {
        try {
            validarDatosHistorial(historial);

            Pedido pedido = pedidoDAO.buscarPorId(historial.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al historial no existe.");
            }

            if (historial.getRegistrado_por() != null) {
                Empleado empleado = empleadoDAO.buscarPorId(historial.getRegistrado_por().getId_usuario());

                if (empleado == null) {
                    throw new Exception("El empleado que registra el historial no existe.");
                }
            }

            HistorialEstadoPedido historialRegistrado = historialEstadoPedidoDAO.insertar(historial);
            TransactionContext.commit();

            return historialRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar historial de estado del pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public HistorialEstadoPedido actualizarHistorialEstadoPedido(HistorialEstadoPedido historial) throws Exception {
        try {
            if (historial == null || historial.getId_historial_estado_pedido() <= 0) {
                throw new Exception("El ID del historial de estado no es válido.");
            }

            HistorialEstadoPedido historialExistente = historialEstadoPedidoDAO.buscarPorId(
                    historial.getId_historial_estado_pedido()
            );

            if (historialExistente == null) {
                throw new Exception("El historial de estado del pedido no existe.");
            }

            validarDatosHistorial(historial);

            Pedido pedido = pedidoDAO.buscarPorId(historial.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al historial no existe.");
            }

            if (historial.getRegistrado_por() != null) {
                Empleado empleado = empleadoDAO.buscarPorId(historial.getRegistrado_por().getId_usuario());

                if (empleado == null) {
                    throw new Exception("El empleado que registra el historial no existe.");
                }
            }

            HistorialEstadoPedido historialActualizado = historialEstadoPedidoDAO.actualizar(historial);
            TransactionContext.commit();

            return historialActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar historial de estado del pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarHistorialEstadoPedido(int idHistorialEstadoPedido) throws Exception {
        try {
            if (idHistorialEstadoPedido <= 0) {
                throw new Exception("El ID del historial de estado no es válido.");
            }

            HistorialEstadoPedido historial = historialEstadoPedidoDAO.buscarPorId(idHistorialEstadoPedido);

            if (historial == null) {
                throw new Exception("El historial de estado del pedido no existe.");
            }

            historialEstadoPedidoDAO.eliminar(historial);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar historial de estado del pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public HistorialEstadoPedido buscarHistorialEstadoPedido(int idHistorialEstadoPedido) throws Exception {
        try {
            if (idHistorialEstadoPedido <= 0) {
                throw new Exception("El ID del historial de estado no es válido.");
            }

            return historialEstadoPedidoDAO.buscarPorId(idHistorialEstadoPedido);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<HistorialEstadoPedido> listarHistorialesEstadoPedido() throws Exception {
        try {
            return historialEstadoPedidoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            ArrayList<HistorialEstadoPedido> historiales = historialEstadoPedidoDAO.listarTodos();
            ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

            for (HistorialEstadoPedido historial : historiales) {
                if (historial.getPedido() != null &&
                        historial.getPedido().getId_pedido() == idPedido) {
                    resultado.add(historial);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            ArrayList<HistorialEstadoPedido> historiales = historialEstadoPedidoDAO.listarTodos();
            ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

            for (HistorialEstadoPedido historial : historiales) {
                if (historial.getRegistrado_por() != null &&
                        historial.getRegistrado_por().getId_usuario() == idEmpleado) {
                    resultado.add(historial);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<HistorialEstadoPedido> listarHistorialesPorEstadoNuevo(String estadoNuevo) throws Exception {
        try {
            if (estadoNuevo == null || estadoNuevo.trim().isEmpty()) {
                throw new Exception("El estado nuevo es obligatorio.");
            }

            estadoNuevo = estadoNuevo.trim().toUpperCase();
            validarEstadoPedido(estadoNuevo);

            ArrayList<HistorialEstadoPedido> historiales = historialEstadoPedidoDAO.listarTodos();
            ArrayList<HistorialEstadoPedido> resultado = new ArrayList<>();

            for (HistorialEstadoPedido historial : historiales) {
                if (historial.getEstado_nuevo() != null &&
                        historial.getEstado_nuevo().equalsIgnoreCase(estadoNuevo)) {
                    resultado.add(historial);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public HistorialEstadoPedido registrarCambioEstadoPedido(int idPedido,
                                                             String estadoNuevo,
                                                             String comentario,
                                                             Integer idEmpleado) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            if (estadoNuevo == null || estadoNuevo.trim().isEmpty()) {
                throw new Exception("El estado nuevo es obligatorio.");
            }

            estadoNuevo = estadoNuevo.trim().toUpperCase();
            validarEstadoPedido(estadoNuevo);

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            String estadoAnterior = pedido.getEstado();

            if (estadoAnterior != null && estadoAnterior.equalsIgnoreCase(estadoNuevo)) {
                throw new Exception("El estado nuevo debe ser diferente al estado actual.");
            }

            Empleado empleado = null;

            if (idEmpleado != null && idEmpleado > 0) {
                empleado = empleadoDAO.buscarPorId(idEmpleado);

                if (empleado == null) {
                    throw new Exception("El empleado que registra el cambio no existe.");
                }
            }

            pedido.setEstado(estadoNuevo);
            pedidoDAO.actualizar(pedido);

            HistorialEstadoPedido historial = new HistorialEstadoPedido();
            historial.setPedido(pedido);
            historial.setEstado_anterior(estadoAnterior);
            historial.setEstado_nuevo(estadoNuevo);
            historial.setComentario(comentario);
            historial.setRegistrado_por(empleado);

            HistorialEstadoPedido historialRegistrado = historialEstadoPedidoDAO.insertar(historial);
            TransactionContext.commit();

            return historialRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar cambio de estado del pedido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosHistorial(HistorialEstadoPedido historial) throws Exception {
        if (historial == null) {
            throw new Exception("El historial no puede ser nulo.");
        }

        if (historial.getPedido() == null || historial.getPedido().getId_pedido() <= 0) {
            throw new Exception("Debe asignar un pedido válido.");
        }

        if (historial.getEstado_nuevo() == null || historial.getEstado_nuevo().trim().isEmpty()) {
            throw new Exception("El estado nuevo es obligatorio.");
        }

        String estadoNuevo = historial.getEstado_nuevo().trim().toUpperCase();
        historial.setEstado_nuevo(estadoNuevo);
        validarEstadoPedido(estadoNuevo);

        if (historial.getEstado_anterior() != null &&
                !historial.getEstado_anterior().trim().isEmpty()) {

            String estadoAnterior = historial.getEstado_anterior().trim().toUpperCase();
            historial.setEstado_anterior(estadoAnterior);
            validarEstadoPedido(estadoAnterior);

            if (estadoAnterior.equals(estadoNuevo)) {
                throw new Exception("El estado anterior y el estado nuevo no pueden ser iguales.");
            }
        } else {
            historial.setEstado_anterior(null);
        }

        if (historial.getRegistrado_por() != null &&
                historial.getRegistrado_por().getId_usuario() <= 0) {
            throw new Exception("El empleado registrado no es válido.");
        }
    }

    private void validarEstadoPedido(String estado) throws Exception {
        if (!estado.equals("PENDIENTE") &&
                !estado.equals("CONFIRMADO") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("ENVIADO") &&
                !estado.equals("ENTREGADO") &&
                !estado.equals("CANCELADO")) {

            throw new Exception("Estado de pedido no válido.");
        }
    }
}
