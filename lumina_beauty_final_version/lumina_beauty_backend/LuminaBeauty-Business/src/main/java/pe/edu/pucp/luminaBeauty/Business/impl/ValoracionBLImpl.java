package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ValoracionBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ValoracionDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetallePedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ValoracionDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.Valoracion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ValoracionBLImpl implements ValoracionBL {

    private final ValoracionDAO valoracionDAO = new ValoracionDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public Valoracion registrarValoracion(Valoracion valoracion) throws Exception {
        try {
            validarDatosValoracion(valoracion);
            validarRelacionesValoracion(valoracion);

            if (valoracion.getEstado() == null || valoracion.getEstado().trim().isEmpty()) {
                valoracion.setEstado("PENDIENTE");
            }

            valoracion.setEstado(valoracion.getEstado().trim().toUpperCase());
            validarEstadoValoracion(valoracion.getEstado());

            validarRespuestaTienda(valoracion);

            Valoracion valoracionRegistrada = valoracionDAO.insertar(valoracion);
            TransactionContext.commit();

            return valoracionRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Valoracion actualizarValoracion(Valoracion valoracion) throws Exception {
        try {
            if (valoracion == null || valoracion.getId_valoracion() <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            Valoracion valoracionExistente = valoracionDAO.buscarPorId(valoracion.getId_valoracion());

            if (valoracionExistente == null) {
                throw new Exception("La valoración no existe.");
            }

            validarDatosValoracion(valoracion);
            validarRelacionesValoracion(valoracion);

            if (valoracion.getEstado() == null || valoracion.getEstado().trim().isEmpty()) {
                valoracion.setEstado(valoracionExistente.getEstado());
            }

            valoracion.setEstado(valoracion.getEstado().trim().toUpperCase());
            validarEstadoValoracion(valoracion.getEstado());

            validarRespuestaTienda(valoracion);

            Valoracion valoracionActualizada = valoracionDAO.actualizar(valoracion);
            TransactionContext.commit();

            return valoracionActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarValoracion(int idValoracion) throws Exception {
        try {
            if (idValoracion <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            Valoracion valoracion = valoracionDAO.buscarPorId(idValoracion);

            if (valoracion == null) {
                throw new Exception("La valoración no existe.");
            }

            valoracionDAO.eliminar(valoracion);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Valoracion buscarValoracion(int idValoracion) throws Exception {
        try {
            if (idValoracion <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            return valoracionDAO.buscarPorId(idValoracion);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Valoracion> listarValoraciones() throws Exception {
        try {
            return valoracionDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Valoracion> listarValoracionesPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<Valoracion> valoraciones = valoracionDAO.listarTodos();
            ArrayList<Valoracion> resultado = new ArrayList<>();

            for (Valoracion valoracion : valoraciones) {
                if (valoracion.getCliente() != null &&
                        valoracion.getCliente().getId_usuario() == idCliente) {
                    resultado.add(valoracion);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Valoracion> listarValoracionesPorProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            ArrayList<Valoracion> valoraciones = valoracionDAO.listarTodos();
            ArrayList<Valoracion> resultado = new ArrayList<>();

            for (Valoracion valoracion : valoraciones) {
                if (valoracion.getProducto() != null &&
                        valoracion.getProducto().getId_producto() == idProducto) {
                    resultado.add(valoracion);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Valoracion> listarValoracionesPorEstado(String estado) throws Exception {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                throw new Exception("El estado de la valoración es obligatorio.");
            }

            estado = estado.trim().toUpperCase();
            validarEstadoValoracion(estado);

            ArrayList<Valoracion> valoraciones = valoracionDAO.listarTodos();
            ArrayList<Valoracion> resultado = new ArrayList<>();

            for (Valoracion valoracion : valoraciones) {
                if (valoracion.getEstado() != null &&
                        valoracion.getEstado().equalsIgnoreCase(estado)) {
                    resultado.add(valoracion);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Valoracion publicarValoracion(int idValoracion) throws Exception {
        try {
            if (idValoracion <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            Valoracion valoracion = valoracionDAO.buscarPorId(idValoracion);

            if (valoracion == null) {
                throw new Exception("La valoración no existe.");
            }

            valoracion.setEstado("PUBLICADA");

            Valoracion valoracionActualizada = valoracionDAO.actualizar(valoracion);
            TransactionContext.commit();

            return valoracionActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al publicar valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Valoracion rechazarValoracion(int idValoracion) throws Exception {
        try {
            if (idValoracion <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            Valoracion valoracion = valoracionDAO.buscarPorId(idValoracion);

            if (valoracion == null) {
                throw new Exception("La valoración no existe.");
            }

            valoracion.setEstado("RECHAZADA");

            Valoracion valoracionActualizada = valoracionDAO.actualizar(valoracion);
            TransactionContext.commit();

            return valoracionActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al rechazar valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Valoracion responderValoracion(int idValoracion,
                                          String respuestaTienda,
                                          int idEmpleado) throws Exception {
        try {
            if (idValoracion <= 0) {
                throw new Exception("El ID de la valoración no es válido.");
            }

            if (respuestaTienda == null || respuestaTienda.trim().isEmpty()) {
                throw new Exception("La respuesta de la tienda es obligatoria.");
            }

            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Valoracion valoracion = valoracionDAO.buscarPorId(idValoracion);

            if (valoracion == null) {
                throw new Exception("La valoración no existe.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado que responde no existe.");
            }

            valoracion.setRespuesta_tienda(respuestaTienda.trim());
            valoracion.setRespondido_por(empleado);
            valoracion.setRespondido_en(LocalDateTime.now());

            Valoracion valoracionActualizada = valoracionDAO.actualizar(valoracion);
            TransactionContext.commit();

            return valoracionActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al responder valoración: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosValoracion(Valoracion valoracion) throws Exception {
        if (valoracion == null) {
            throw new Exception("La valoración no puede ser nula.");
        }

        if (valoracion.getCliente() == null ||
                valoracion.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido.");
        }

        if (valoracion.getProducto() == null ||
                valoracion.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido.");
        }

        if (valoracion.getCalificacion() < 1 || valoracion.getCalificacion() > 5) {
            throw new Exception("La calificación debe estar entre 1 y 5.");
        }

        if (valoracion.getDetallePedido() != null &&
                valoracion.getDetallePedido().getId_detalle_pedido() <= 0) {
            throw new Exception("El detalle de pedido asignado no es válido.");
        }

        if (valoracion.getRespondido_por() != null &&
                valoracion.getRespondido_por().getId_usuario() <= 0) {
            throw new Exception("El empleado que responde no es válido.");
        }
    }

    private void validarRelacionesValoracion(Valoracion valoracion) throws Exception {
        Cliente cliente = clienteDAO.buscarPorId(valoracion.getCliente().getId_usuario());

        if (cliente == null) {
            throw new Exception("El cliente asociado a la valoración no existe.");
        }

        Producto producto = productoDAO.buscarPorId(valoracion.getProducto().getId_producto());

        if (producto == null) {
            throw new Exception("El producto asociado a la valoración no existe.");
        }

        if (valoracion.getDetallePedido() != null) {
            DetallePedido detallePedido = detallePedidoDAO.buscarPorId(
                    valoracion.getDetallePedido().getId_detalle_pedido()
            );

            if (detallePedido == null) {
                throw new Exception("El detalle de pedido asociado a la valoración no existe.");
            }
        }

        if (valoracion.getRespondido_por() != null) {
            Empleado empleado = empleadoDAO.buscarPorId(
                    valoracion.getRespondido_por().getId_usuario()
            );

            if (empleado == null) {
                throw new Exception("El empleado que responde la valoración no existe.");
            }
        }
    }

    private void validarRespuestaTienda(Valoracion valoracion) throws Exception {
        boolean tieneRespuesta = valoracion.getRespuesta_tienda() != null &&
                !valoracion.getRespuesta_tienda().trim().isEmpty();

        boolean tieneEmpleado = valoracion.getRespondido_por() != null &&
                valoracion.getRespondido_por().getId_usuario() > 0;

        boolean tieneFechaRespuesta = valoracion.getRespondido_en() != null;

        if (!tieneRespuesta && !tieneEmpleado && !tieneFechaRespuesta) {
            valoracion.setRespuesta_tienda(null);
            valoracion.setRespondido_por(null);
            valoracion.setRespondido_en(null);
            return;
        }

        if (!tieneRespuesta || !tieneEmpleado) {
            throw new Exception("Si se responde una valoración, debe indicar respuesta y empleado.");
        }

        if (!tieneFechaRespuesta) {
            valoracion.setRespondido_en(LocalDateTime.now());
        }
    }

    private void validarEstadoValoracion(String estado) throws Exception {
        if (!estado.equals("PENDIENTE") &&
                !estado.equals("PUBLICADA") &&
                !estado.equals("RECHAZADA")) {
            throw new Exception("Estado de valoración no válido.");
        }
    }

    @Override
    public ArrayList<Valoracion> listarPublicadasPorProducto(
            int idProducto
    ) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            return valoracionDAO.listarPublicadasPorProducto(idProducto);

        } finally {
            TransactionContext.close();
        }
    }
}
