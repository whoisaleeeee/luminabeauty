package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.SeguimientoReclamoBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.SeguimientoReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.SeguimientoReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.Model.SeguimientoReclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SeguimientoReclamoBLImpl implements SeguimientoReclamoBL {

    private final SeguimientoReclamoDAO seguimientoReclamoDAO = new SeguimientoReclamoDAOImpl();
    private final ReclamoDAO reclamoDAO = new ReclamoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public SeguimientoReclamo registrarSeguimientoReclamo(SeguimientoReclamo seguimiento) throws Exception {
        try {
            validarDatosSeguimiento(seguimiento);
            validarRelacionesSeguimiento(seguimiento);

            seguimiento.setTipo(seguimiento.getTipo().trim().toUpperCase());

            if (seguimiento.getEstado_anterior() != null &&
                    !seguimiento.getEstado_anterior().trim().isEmpty()) {
                seguimiento.setEstado_anterior(seguimiento.getEstado_anterior().trim().toUpperCase());
            } else {
                seguimiento.setEstado_anterior(null);
            }

            if (seguimiento.getEstado_nuevo() != null &&
                    !seguimiento.getEstado_nuevo().trim().isEmpty()) {
                seguimiento.setEstado_nuevo(seguimiento.getEstado_nuevo().trim().toUpperCase());
            } else {
                seguimiento.setEstado_nuevo(null);
            }

            validarTipoSeguimiento(seguimiento.getTipo());
            validarEstadosSegunTipo(seguimiento);

            SeguimientoReclamo seguimientoRegistrado = seguimientoReclamoDAO.insertar(seguimiento);
            TransactionContext.commit();

            return seguimientoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar seguimiento de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public SeguimientoReclamo actualizarSeguimientoReclamo(SeguimientoReclamo seguimiento) throws Exception {
        try {
            if (seguimiento == null || seguimiento.getId_seguimiento_reclamo() <= 0) {
                throw new Exception("El ID del seguimiento de reclamo no es válido.");
            }

            SeguimientoReclamo seguimientoExistente = seguimientoReclamoDAO.buscarPorId(
                    seguimiento.getId_seguimiento_reclamo()
            );

            if (seguimientoExistente == null) {
                throw new Exception("El seguimiento de reclamo no existe.");
            }

            validarDatosSeguimiento(seguimiento);
            validarRelacionesSeguimiento(seguimiento);

            seguimiento.setTipo(seguimiento.getTipo().trim().toUpperCase());

            if (seguimiento.getEstado_anterior() != null &&
                    !seguimiento.getEstado_anterior().trim().isEmpty()) {
                seguimiento.setEstado_anterior(seguimiento.getEstado_anterior().trim().toUpperCase());
            } else {
                seguimiento.setEstado_anterior(null);
            }

            if (seguimiento.getEstado_nuevo() != null &&
                    !seguimiento.getEstado_nuevo().trim().isEmpty()) {
                seguimiento.setEstado_nuevo(seguimiento.getEstado_nuevo().trim().toUpperCase());
            } else {
                seguimiento.setEstado_nuevo(null);
            }

            validarTipoSeguimiento(seguimiento.getTipo());
            validarEstadosSegunTipo(seguimiento);

            SeguimientoReclamo seguimientoActualizado = seguimientoReclamoDAO.actualizar(seguimiento);
            TransactionContext.commit();

            return seguimientoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar seguimiento de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarSeguimientoReclamo(int idSeguimientoReclamo) throws Exception {
        try {
            if (idSeguimientoReclamo <= 0) {
                throw new Exception("El ID del seguimiento de reclamo no es válido.");
            }

            SeguimientoReclamo seguimiento = seguimientoReclamoDAO.buscarPorId(idSeguimientoReclamo);

            if (seguimiento == null) {
                throw new Exception("El seguimiento de reclamo no existe.");
            }

            seguimientoReclamoDAO.eliminar(seguimiento);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar seguimiento de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public SeguimientoReclamo buscarSeguimientoReclamo(int idSeguimientoReclamo) throws Exception {
        try {
            if (idSeguimientoReclamo <= 0) {
                throw new Exception("El ID del seguimiento de reclamo no es válido.");
            }

            return seguimientoReclamoDAO.buscarPorId(idSeguimientoReclamo);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarSeguimientosReclamo() throws Exception {
        try {
            return seguimientoReclamoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorReclamo(int idReclamo) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            ArrayList<SeguimientoReclamo> seguimientos = seguimientoReclamoDAO.listarTodos();
            ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

            for (SeguimientoReclamo seguimiento : seguimientos) {
                if (seguimiento.getReclamo() != null &&
                        seguimiento.getReclamo().getId_reclamo() == idReclamo) {
                    resultado.add(seguimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorTipo(String tipo) throws Exception {
        try {
            if (tipo == null || tipo.trim().isEmpty()) {
                throw new Exception("El tipo de seguimiento es obligatorio.");
            }

            tipo = tipo.trim().toUpperCase();
            validarTipoSeguimiento(tipo);

            ArrayList<SeguimientoReclamo> seguimientos = seguimientoReclamoDAO.listarTodos();
            ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

            for (SeguimientoReclamo seguimiento : seguimientos) {
                if (seguimiento.getTipo() != null &&
                        seguimiento.getTipo().equalsIgnoreCase(tipo)) {
                    resultado.add(seguimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<SeguimientoReclamo> seguimientos = seguimientoReclamoDAO.listarTodos();
            ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

            for (SeguimientoReclamo seguimiento : seguimientos) {
                if (seguimiento.getRegistrado_por_cliente() != null &&
                        seguimiento.getRegistrado_por_cliente().getId_usuario() == idCliente) {
                    resultado.add(seguimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarSeguimientosPorEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            ArrayList<SeguimientoReclamo> seguimientos = seguimientoReclamoDAO.listarTodos();
            ArrayList<SeguimientoReclamo> resultado = new ArrayList<>();

            for (SeguimientoReclamo seguimiento : seguimientos) {
                if (seguimiento.getRegistrado_por_empleado() != null &&
                        seguimiento.getRegistrado_por_empleado().getId_usuario() == idEmpleado) {
                    resultado.add(seguimiento);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public SeguimientoReclamo registrarCambioEstadoReclamo(int idReclamo,
                                                           String estadoNuevo,
                                                           String mensaje,
                                                           int idEmpleado) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            if (estadoNuevo == null || estadoNuevo.trim().isEmpty()) {
                throw new Exception("El estado nuevo es obligatorio.");
            }

            if (mensaje == null || mensaje.trim().isEmpty()) {
                throw new Exception("El mensaje del seguimiento es obligatorio.");
            }

            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            estadoNuevo = estadoNuevo.trim().toUpperCase();
            validarEstadoReclamo(estadoNuevo);

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado que registra el cambio no existe.");
            }

            String estadoAnterior = reclamo.getEstado();

            if (estadoAnterior != null && estadoAnterior.equalsIgnoreCase(estadoNuevo)) {
                throw new Exception("El estado nuevo debe ser diferente al estado actual.");
            }

            reclamo.setEstado(estadoNuevo);

            if (esEstadoFinal(estadoNuevo)) {
                reclamo.setResuelto_en(LocalDateTime.now());
            } else {
                reclamo.setResuelto_en(null);
            }

            reclamoDAO.actualizar(reclamo);

            SeguimientoReclamo seguimiento = new SeguimientoReclamo();
            seguimiento.setReclamo(reclamo);
            seguimiento.setTipo("CAMBIO_ESTADO");
            seguimiento.setMensaje(mensaje.trim());
            seguimiento.setEstado_anterior(estadoAnterior);
            seguimiento.setEstado_nuevo(estadoNuevo);
            seguimiento.setRegistrado_por_empleado(empleado);
            seguimiento.setRegistrado_por_cliente(null);

            SeguimientoReclamo seguimientoRegistrado = seguimientoReclamoDAO.insertar(seguimiento);
            TransactionContext.commit();

            return seguimientoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar cambio de estado del reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosSeguimiento(SeguimientoReclamo seguimiento) throws Exception {
        if (seguimiento == null) {
            throw new Exception("El seguimiento de reclamo no puede ser nulo.");
        }

        if (seguimiento.getReclamo() == null ||
                seguimiento.getReclamo().getId_reclamo() <= 0) {
            throw new Exception("Debe asignar un reclamo válido.");
        }

        if (seguimiento.getTipo() == null ||
                seguimiento.getTipo().trim().isEmpty()) {
            throw new Exception("El tipo de seguimiento es obligatorio.");
        }

        if (seguimiento.getMensaje() == null ||
                seguimiento.getMensaje().trim().isEmpty()) {
            throw new Exception("El mensaje del seguimiento es obligatorio.");
        }

        boolean tieneCliente = seguimiento.getRegistrado_por_cliente() != null &&
                seguimiento.getRegistrado_por_cliente().getId_usuario() > 0;

        boolean tieneEmpleado = seguimiento.getRegistrado_por_empleado() != null &&
                seguimiento.getRegistrado_por_empleado().getId_usuario() > 0;

        if (tieneCliente && tieneEmpleado) {
            throw new Exception("El seguimiento solo puede ser registrado por cliente o empleado, no ambos.");
        }

        if (!tieneCliente && !tieneEmpleado) {
            throw new Exception("Debe indicar quién registra el seguimiento.");
        }

        if (seguimiento.getRegistrado_por_cliente() != null &&
                seguimiento.getRegistrado_por_cliente().getId_usuario() <= 0) {
            throw new Exception("El cliente que registra el seguimiento no es válido.");
        }

        if (seguimiento.getRegistrado_por_empleado() != null &&
                seguimiento.getRegistrado_por_empleado().getId_usuario() <= 0) {
            throw new Exception("El empleado que registra el seguimiento no es válido.");
        }
    }

    private void validarRelacionesSeguimiento(SeguimientoReclamo seguimiento) throws Exception {
        Reclamo reclamo = reclamoDAO.buscarPorId(seguimiento.getReclamo().getId_reclamo());

        if (reclamo == null) {
            throw new Exception("El reclamo asociado al seguimiento no existe.");
        }

        if (seguimiento.getRegistrado_por_cliente() != null) {
            Cliente cliente = clienteDAO.buscarPorId(
                    seguimiento.getRegistrado_por_cliente().getId_usuario()
            );

            if (cliente == null) {
                throw new Exception("El cliente que registra el seguimiento no existe.");
            }
        }

        if (seguimiento.getRegistrado_por_empleado() != null) {
            Empleado empleado = empleadoDAO.buscarPorId(
                    seguimiento.getRegistrado_por_empleado().getId_usuario()
            );

            if (empleado == null) {
                throw new Exception("El empleado que registra el seguimiento no existe.");
            }
        }
    }

    private void validarEstadosSegunTipo(SeguimientoReclamo seguimiento) throws Exception {
        String tipo = seguimiento.getTipo();

        if ("CAMBIO_ESTADO".equals(tipo)) {
            if (seguimiento.getEstado_nuevo() == null ||
                    seguimiento.getEstado_nuevo().trim().isEmpty()) {
                throw new Exception("Para CAMBIO_ESTADO debe indicar estado nuevo.");
            }

            validarEstadoReclamo(seguimiento.getEstado_nuevo());

            if (seguimiento.getEstado_anterior() != null &&
                    !seguimiento.getEstado_anterior().trim().isEmpty()) {
                validarEstadoReclamo(seguimiento.getEstado_anterior());

                if (seguimiento.getEstado_anterior().equals(seguimiento.getEstado_nuevo())) {
                    throw new Exception("El estado anterior y el estado nuevo no pueden ser iguales.");
                }
            }

        } else {
            seguimiento.setEstado_anterior(null);
            seguimiento.setEstado_nuevo(null);
        }
    }

    private void validarTipoSeguimiento(String tipo) throws Exception {
        if (!tipo.equals("MENSAJE_CLIENTE") &&
                !tipo.equals("MENSAJE_SOPORTE") &&
                !tipo.equals("CAMBIO_ESTADO") &&
                !tipo.equals("NOTA_INTERNA")) {

            throw new Exception("Tipo de seguimiento de reclamo no válido.");
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

    private boolean esEstadoFinal(String estado) {
        return estado.equals("RESUELTO") ||
                estado.equals("CERRADO") ||
                estado.equals("RECHAZADO");
    }
}
