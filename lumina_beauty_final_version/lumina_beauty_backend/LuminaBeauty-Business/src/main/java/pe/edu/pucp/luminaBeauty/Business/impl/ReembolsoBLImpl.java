package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ReembolsoBL;
import pe.edu.pucp.luminaBeauty.DAO.DevolucionDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ReembolsoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.DevolucionDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PagoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ReembolsoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Reembolso;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ReembolsoBLImpl implements ReembolsoBL {

    private final ReembolsoDAO reembolsoDAO = new ReembolsoDAOImpl();
    private final PagoDAO pagoDAO = new PagoDAOImpl();
    private final DevolucionDAO devolucionDAO = new DevolucionDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public Reembolso registrarReembolso(Reembolso reembolso) throws Exception {
        try {
            validarDatosReembolso(reembolso);
            validarRelacionesReembolso(reembolso);

            if (reembolso.getEstado() == null || reembolso.getEstado().trim().isEmpty()) {
                reembolso.setEstado("PENDIENTE");
            }

            reembolso.setEstado(reembolso.getEstado().trim().toUpperCase());
            validarEstadoReembolso(reembolso.getEstado());
            validarFechasSegunEstado(reembolso);

            validarReferenciaUnica(reembolso);

            Reembolso reembolsoRegistrado = reembolsoDAO.insertar(reembolso);
            TransactionContext.commit();

            return reembolsoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar reembolso: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reembolso actualizarReembolso(Reembolso reembolso) throws Exception {
        try {
            if (reembolso == null || reembolso.getId_reembolso() <= 0) {
                throw new Exception("El ID del reembolso no es válido.");
            }

            Reembolso reembolsoExistente = reembolsoDAO.buscarPorId(reembolso.getId_reembolso());

            if (reembolsoExistente == null) {
                throw new Exception("El reembolso no existe.");
            }

            validarDatosReembolso(reembolso);
            validarRelacionesReembolso(reembolso);

            if (reembolso.getEstado() == null || reembolso.getEstado().trim().isEmpty()) {
                reembolso.setEstado(reembolsoExistente.getEstado());
            }

            reembolso.setEstado(reembolso.getEstado().trim().toUpperCase());
            validarEstadoReembolso(reembolso.getEstado());
            validarFechasSegunEstado(reembolso);

            validarReferenciaUnica(reembolso);

            Reembolso reembolsoActualizado = reembolsoDAO.actualizar(reembolso);
            TransactionContext.commit();

            return reembolsoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar reembolso: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarReembolso(int idReembolso) throws Exception {
        try {
            if (idReembolso <= 0) {
                throw new Exception("El ID del reembolso no es válido.");
            }

            Reembolso reembolso = reembolsoDAO.buscarPorId(idReembolso);

            if (reembolso == null) {
                throw new Exception("El reembolso no existe.");
            }

            reembolsoDAO.eliminar(reembolso);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar reembolso: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reembolso buscarReembolso(int idReembolso) throws Exception {
        try {
            if (idReembolso <= 0) {
                throw new Exception("El ID del reembolso no es válido.");
            }

            return reembolsoDAO.buscarPorId(idReembolso);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reembolso> listarReembolsos() throws Exception {
        try {
            return reembolsoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reembolso> listarReembolsosPorEstado(String estado) throws Exception {
        try {
            if (estado == null || estado.trim().isEmpty()) {
                throw new Exception("El estado del reembolso es obligatorio.");
            }

            estado = estado.trim().toUpperCase();
            validarEstadoReembolso(estado);

            ArrayList<Reembolso> reembolsos = reembolsoDAO.listarTodos();
            ArrayList<Reembolso> resultado = new ArrayList<>();

            for (Reembolso reembolso : reembolsos) {
                if (reembolso.getEstado() != null &&
                        reembolso.getEstado().equalsIgnoreCase(estado)) {
                    resultado.add(reembolso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reembolso> listarReembolsosPorPago(int idPago) throws Exception {
        try {
            if (idPago <= 0) {
                throw new Exception("El ID del pago no es válido.");
            }

            Pago pago = pagoDAO.buscarPorId(idPago);

            if (pago == null) {
                throw new Exception("El pago no existe.");
            }

            ArrayList<Reembolso> reembolsos = reembolsoDAO.listarTodos();
            ArrayList<Reembolso> resultado = new ArrayList<>();

            for (Reembolso reembolso : reembolsos) {
                if (reembolso.getPago() != null &&
                        reembolso.getPago().getId_pago() == idPago) {
                    resultado.add(reembolso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reembolso> listarReembolsosPorDevolucion(int idDevolucion) throws Exception {
        try {
            if (idDevolucion <= 0) {
                throw new Exception("El ID de la devolución no es válido.");
            }

            Devolucion devolucion = devolucionDAO.buscarPorId(idDevolucion);

            if (devolucion == null) {
                throw new Exception("La devolución no existe.");
            }

            ArrayList<Reembolso> reembolsos = reembolsoDAO.listarTodos();
            ArrayList<Reembolso> resultado = new ArrayList<>();

            for (Reembolso reembolso : reembolsos) {
                if (reembolso.getDevolucion() != null &&
                        reembolso.getDevolucion().getId_devolucion() == idDevolucion) {
                    resultado.add(reembolso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reembolso> listarReembolsosPorEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            ArrayList<Reembolso> reembolsos = reembolsoDAO.listarTodos();
            ArrayList<Reembolso> resultado = new ArrayList<>();

            for (Reembolso reembolso : reembolsos) {
                if (reembolso.getProcesado_por() != null &&
                        reembolso.getProcesado_por().getId_usuario() == idEmpleado) {
                    resultado.add(reembolso);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reembolso procesarReembolso(int idReembolso,
                                       int idEmpleado,
                                       String referenciaTransaccion) throws Exception {
        try {
            if (idReembolso <= 0) {
                throw new Exception("El ID del reembolso no es válido.");
            }

            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            if (referenciaTransaccion == null || referenciaTransaccion.trim().isEmpty()) {
                throw new Exception("La referencia de transacción es obligatoria.");
            }

            Reembolso reembolso = reembolsoDAO.buscarPorId(idReembolso);

            if (reembolso == null) {
                throw new Exception("El reembolso no existe.");
            }

            if ("PROCESADO".equalsIgnoreCase(reembolso.getEstado())) {
                throw new Exception("El reembolso ya fue procesado.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado que procesa el reembolso no existe.");
            }

            Pago pago = pagoDAO.buscarPorId(reembolso.getPago().getId_pago());

            if (pago == null) {
                throw new Exception("El pago asociado al reembolso no existe.");
            }

            if (!"COMPLETADO".equalsIgnoreCase(pago.getEstado()) &&
                    !"REEMBOLSADO".equalsIgnoreCase(pago.getEstado())) {
                throw new Exception("Solo se puede procesar reembolso de un pago completado.");
            }

            reembolso.setEstado("PROCESADO");
            reembolso.setReferencia_transaccion(referenciaTransaccion.trim());
            reembolso.setProcesado_por(empleado);
            reembolso.setProcesado_en(LocalDateTime.now());

            validarReferenciaUnica(reembolso);

            pago.setEstado("REEMBOLSADO");
            pago.setFecha_reembolso(LocalDateTime.now());
            pagoDAO.actualizar(pago);

            Reembolso reembolsoActualizado = reembolsoDAO.actualizar(reembolso);
            TransactionContext.commit();

            return reembolsoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al procesar reembolso: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Reembolso marcarReembolsoFallido(int idReembolso, String motivo) throws Exception {
        try {
            if (idReembolso <= 0) {
                throw new Exception("El ID del reembolso no es válido.");
            }

            Reembolso reembolso = reembolsoDAO.buscarPorId(idReembolso);

            if (reembolso == null) {
                throw new Exception("El reembolso no existe.");
            }

            if ("PROCESADO".equalsIgnoreCase(reembolso.getEstado())) {
                throw new Exception("No se puede marcar como fallido un reembolso procesado.");
            }

            reembolso.setEstado("FALLIDO");
            reembolso.setProcesado_en(null);

            if (motivo != null && !motivo.trim().isEmpty()) {
                reembolso.setMotivo(motivo.trim());
            }

            Reembolso reembolsoActualizado = reembolsoDAO.actualizar(reembolso);
            TransactionContext.commit();

            return reembolsoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al marcar reembolso como fallido: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosReembolso(Reembolso reembolso) throws Exception {
        if (reembolso == null) {
            throw new Exception("El reembolso no puede ser nulo.");
        }

        if (reembolso.getPago() == null || reembolso.getPago().getId_pago() <= 0) {
            throw new Exception("Debe asignar un pago válido.");
        }

        if (reembolso.getMonto() == null ||
                reembolso.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("El monto del reembolso debe ser mayor a cero.");
        }

        if (reembolso.getDevolucion() != null &&
                reembolso.getDevolucion().getId_devolucion() <= 0) {
            throw new Exception("La devolución asociada no es válida.");
        }

        if (reembolso.getProcesado_por() != null &&
                reembolso.getProcesado_por().getId_usuario() <= 0) {
            throw new Exception("El empleado que procesa no es válido.");
        }
    }

    private void validarRelacionesReembolso(Reembolso reembolso) throws Exception {
        Pago pago = pagoDAO.buscarPorId(reembolso.getPago().getId_pago());

        if (pago == null) {
            throw new Exception("El pago asociado al reembolso no existe.");
        }

        if (!"COMPLETADO".equalsIgnoreCase(pago.getEstado()) &&
                !"REEMBOLSADO".equalsIgnoreCase(pago.getEstado())) {
            throw new Exception("Solo se puede registrar reembolso para un pago completado.");
        }

        if (reembolso.getMonto().compareTo(pago.getMonto()) > 0) {
            throw new Exception("El monto del reembolso no puede ser mayor al monto del pago.");
        }

        reembolso.setPago(pago);

        if (reembolso.getDevolucion() != null) {
            Devolucion devolucion = devolucionDAO.buscarPorId(
                    reembolso.getDevolucion().getId_devolucion()
            );

            if (devolucion == null) {
                throw new Exception("La devolución asociada al reembolso no existe.");
            }

            reembolso.setDevolucion(devolucion);
        }

        if (reembolso.getProcesado_por() != null) {
            Empleado empleado = empleadoDAO.buscarPorId(
                    reembolso.getProcesado_por().getId_usuario()
            );

            if (empleado == null) {
                throw new Exception("El empleado que procesa el reembolso no existe.");
            }

            reembolso.setProcesado_por(empleado);
        }
    }

    private void validarEstadoReembolso(String estado) throws Exception {
        if (!estado.equals("PENDIENTE") &&
                !estado.equals("PROCESADO") &&
                !estado.equals("FALLIDO")) {
            throw new Exception("Estado de reembolso no válido.");
        }
    }

    private void validarFechasSegunEstado(Reembolso reembolso) {
        String estado = reembolso.getEstado();

        if ("PENDIENTE".equals(estado) || "FALLIDO".equals(estado)) {
            reembolso.setProcesado_en(null);
        }

        if ("PROCESADO".equals(estado) && reembolso.getProcesado_en() == null) {
            reembolso.setProcesado_en(LocalDateTime.now());
        }
    }

    private void validarReferenciaUnica(Reembolso reembolso) throws Exception {
        if (reembolso.getReferencia_transaccion() == null ||
                reembolso.getReferencia_transaccion().trim().isEmpty()) {
            return;
        }

        ArrayList<Reembolso> reembolsos = reembolsoDAO.listarTodos();

        for (Reembolso item : reembolsos) {
            if (item.getReferencia_transaccion() != null &&
                    item.getReferencia_transaccion().equalsIgnoreCase(
                            reembolso.getReferencia_transaccion().trim()
                    ) &&
                    item.getId_reembolso() != reembolso.getId_reembolso()) {

                throw new Exception("Ya existe un reembolso con esa referencia de transacción.");
            }
        }
    }
}
