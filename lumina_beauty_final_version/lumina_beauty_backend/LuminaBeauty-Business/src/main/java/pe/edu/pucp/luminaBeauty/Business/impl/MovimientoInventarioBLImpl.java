package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.MovimientoInventarioBL;
import pe.edu.pucp.luminaBeauty.DAO.DevolucionDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.MovimientoInventarioDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.DevolucionDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.MovimientoInventarioDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.MovimientoInventario;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class MovimientoInventarioBLImpl implements MovimientoInventarioBL {

    private final MovimientoInventarioDAO movimientoInventarioDAO = new MovimientoInventarioDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DevolucionDAO devolucionDAO = new DevolucionDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public MovimientoInventario registrarMovimientoInventario(MovimientoInventario movimiento) throws Exception {
        try {
            validarDatosMovimiento(movimiento);

            Producto producto = productoDAO.buscarPorId(movimiento.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado al movimiento no existe.");
            }

            validarRelacionesOpcionales(movimiento);

            String tipoMovimiento = movimiento.getTipo_movimiento().trim().toUpperCase();
            movimiento.setTipo_movimiento(tipoMovimiento);

            int stockAnterior = producto.getStock();
            int stockPosterior = calcularStockPosterior(stockAnterior, movimiento.getCantidad(), tipoMovimiento);

            if (stockPosterior < 0) {
                throw new Exception("El movimiento dejaría el stock en negativo.");
            }

            movimiento.setStock_anterior(stockAnterior);
            movimiento.setStock_posterior(stockPosterior);

            producto.setStock(stockPosterior);
            productoDAO.actualizar(producto);

            MovimientoInventario movimientoRegistrado = movimientoInventarioDAO.insertar(movimiento);
            TransactionContext.commit();

            return movimientoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar movimiento de inventario: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public MovimientoInventario actualizarMovimientoInventario(MovimientoInventario movimiento) throws Exception {
        try {
            if (movimiento == null || movimiento.getId_movimiento_inventario() <= 0) {
                throw new Exception("El ID del movimiento de inventario no es válido.");
            }

            MovimientoInventario movimientoExistente = movimientoInventarioDAO.buscarPorId(
                    movimiento.getId_movimiento_inventario()
            );

            if (movimientoExistente == null) {
                throw new Exception("El movimiento de inventario no existe.");
            }

            validarDatosMovimiento(movimiento);

            Producto producto = productoDAO.buscarPorId(movimiento.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado al movimiento no existe.");
            }

            validarRelacionesOpcionales(movimiento);

            String tipoMovimiento = movimiento.getTipo_movimiento().trim().toUpperCase();
            movimiento.setTipo_movimiento(tipoMovimiento);

            validarConsistenciaStock(
                    movimiento.getStock_anterior(),
                    movimiento.getCantidad(),
                    movimiento.getStock_posterior(),
                    tipoMovimiento
            );

            MovimientoInventario movimientoActualizado = movimientoInventarioDAO.actualizar(movimiento);
            TransactionContext.commit();

            return movimientoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar movimiento de inventario: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarMovimientoInventario(int idMovimientoInventario) throws Exception {
        try {
            if (idMovimientoInventario <= 0) {
                throw new Exception("El ID del movimiento de inventario no es válido.");
            }

            MovimientoInventario movimiento = movimientoInventarioDAO.buscarPorId(idMovimientoInventario);

            if (movimiento == null) {
                throw new Exception("El movimiento de inventario no existe.");
            }

            movimientoInventarioDAO.eliminar(movimiento);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar movimiento de inventario: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public MovimientoInventario buscarMovimientoInventario(int idMovimientoInventario) throws Exception {
        try {
            if (idMovimientoInventario <= 0) {
                throw new Exception("El ID del movimiento de inventario no es válido.");
            }

            return movimientoInventarioDAO.buscarPorId(idMovimientoInventario);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosInventario() throws Exception {
        try {
            return movimientoInventarioDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosPorProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            ArrayList<MovimientoInventario> movimientos = movimientoInventarioDAO.listarTodos();
            ArrayList<MovimientoInventario> resultado = new ArrayList<>();

            for (MovimientoInventario movimiento : movimientos) {
                if (movimiento.getProducto() != null &&
                        movimiento.getProducto().getId_producto() == idProducto) {
                    resultado.add(movimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosPorTipo(String tipoMovimiento) throws Exception {
        try {
            if (tipoMovimiento == null || tipoMovimiento.trim().isEmpty()) {
                throw new Exception("El tipo de movimiento es obligatorio.");
            }

            tipoMovimiento = tipoMovimiento.trim().toUpperCase();
            validarTipoMovimiento(tipoMovimiento);

            ArrayList<MovimientoInventario> movimientos = movimientoInventarioDAO.listarTodos();
            ArrayList<MovimientoInventario> resultado = new ArrayList<>();

            for (MovimientoInventario movimiento : movimientos) {
                if (movimiento.getTipo_movimiento() != null &&
                        movimiento.getTipo_movimiento().equalsIgnoreCase(tipoMovimiento)) {
                    resultado.add(movimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosPorPedido(int idPedido) throws Exception {
        try {
            if (idPedido <= 0) {
                throw new Exception("El ID del pedido no es válido.");
            }

            Pedido pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                throw new Exception("El pedido no existe.");
            }

            ArrayList<MovimientoInventario> movimientos = movimientoInventarioDAO.listarTodos();
            ArrayList<MovimientoInventario> resultado = new ArrayList<>();

            for (MovimientoInventario movimiento : movimientos) {
                if (movimiento.getPedido() != null &&
                        movimiento.getPedido().getId_pedido() == idPedido) {
                    resultado.add(movimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosPorDevolucion(int idDevolucion) throws Exception {
        try {
            if (idDevolucion <= 0) {
                throw new Exception("El ID de la devolución no es válido.");
            }

            Devolucion devolucion = devolucionDAO.buscarPorId(idDevolucion);

            if (devolucion == null) {
                throw new Exception("La devolución no existe.");
            }

            ArrayList<MovimientoInventario> movimientos = movimientoInventarioDAO.listarTodos();
            ArrayList<MovimientoInventario> resultado = new ArrayList<>();

            for (MovimientoInventario movimiento : movimientos) {
                if (movimiento.getDevolucion() != null &&
                        movimiento.getDevolucion().getId_devolucion() == idDevolucion) {
                    resultado.add(movimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarMovimientosPorEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            ArrayList<MovimientoInventario> movimientos = movimientoInventarioDAO.listarTodos();
            ArrayList<MovimientoInventario> resultado = new ArrayList<>();

            for (MovimientoInventario movimiento : movimientos) {
                if (movimiento.getRegistrado_por() != null &&
                        movimiento.getRegistrado_por().getId_usuario() == idEmpleado) {
                    resultado.add(movimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosMovimiento(MovimientoInventario movimiento) throws Exception {
        if (movimiento == null) {
            throw new Exception("El movimiento de inventario no puede ser nulo.");
        }

        if (movimiento.getProducto() == null ||
                movimiento.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido.");
        }

        if (movimiento.getTipo_movimiento() == null ||
                movimiento.getTipo_movimiento().trim().isEmpty()) {
            throw new Exception("El tipo de movimiento es obligatorio.");
        }

        String tipoMovimiento = movimiento.getTipo_movimiento().trim().toUpperCase();
        validarTipoMovimiento(tipoMovimiento);

        if (movimiento.getCantidad() <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }

        if (movimiento.getPedido() != null &&
                movimiento.getPedido().getId_pedido() <= 0) {
            throw new Exception("El pedido asociado no es válido.");
        }

        if (movimiento.getDevolucion() != null &&
                movimiento.getDevolucion().getId_devolucion() <= 0) {
            throw new Exception("La devolución asociada no es válida.");
        }

        if (movimiento.getRegistrado_por() != null &&
                movimiento.getRegistrado_por().getId_usuario() <= 0) {
            throw new Exception("El empleado registrado no es válido.");
        }
    }

    private void validarRelacionesOpcionales(MovimientoInventario movimiento) throws Exception {
        if (movimiento.getPedido() != null) {
            Pedido pedido = pedidoDAO.buscarPorId(movimiento.getPedido().getId_pedido());

            if (pedido == null) {
                throw new Exception("El pedido asociado al movimiento no existe.");
            }
        }

        if (movimiento.getDevolucion() != null) {
            Devolucion devolucion = devolucionDAO.buscarPorId(
                    movimiento.getDevolucion().getId_devolucion()
            );

            if (devolucion == null) {
                throw new Exception("La devolución asociada al movimiento no existe.");
            }
        }

        if (movimiento.getRegistrado_por() != null) {
            Empleado empleado = empleadoDAO.buscarPorId(
                    movimiento.getRegistrado_por().getId_usuario()
            );

            if (empleado == null) {
                throw new Exception("El empleado que registra el movimiento no existe.");
            }
        }
    }

    private void validarTipoMovimiento(String tipoMovimiento) throws Exception {
        if (!tipoMovimiento.equals("ENTRADA") &&
                !tipoMovimiento.equals("SALIDA_VENTA") &&
                !tipoMovimiento.equals("AJUSTE_POSITIVO") &&
                !tipoMovimiento.equals("AJUSTE_NEGATIVO") &&
                !tipoMovimiento.equals("DEVOLUCION_CLIENTE") &&
                !tipoMovimiento.equals("MERMA")) {

            throw new Exception("Tipo de movimiento de inventario no válido.");
        }
    }

    private int calcularStockPosterior(int stockAnterior, int cantidad, String tipoMovimiento) {
        if (esMovimientoEntrada(tipoMovimiento)) {
            return stockAnterior + cantidad;
        }

        return stockAnterior - cantidad;
    }

    private void validarConsistenciaStock(int stockAnterior,
                                          int cantidad,
                                          int stockPosterior,
                                          String tipoMovimiento) throws Exception {

        int stockCalculado = calcularStockPosterior(stockAnterior, cantidad, tipoMovimiento);

        if (stockPosterior != stockCalculado) {
            throw new Exception("El stock posterior no coincide con el tipo de movimiento y la cantidad.");
        }

        if (stockPosterior < 0) {
            throw new Exception("El stock posterior no puede ser negativo.");
        }
    }

    private boolean esMovimientoEntrada(String tipoMovimiento) {
        return tipoMovimiento.equals("ENTRADA") ||
                tipoMovimiento.equals("AJUSTE_POSITIVO") ||
                tipoMovimiento.equals("DEVOLUCION_CLIENTE");
    }
}

