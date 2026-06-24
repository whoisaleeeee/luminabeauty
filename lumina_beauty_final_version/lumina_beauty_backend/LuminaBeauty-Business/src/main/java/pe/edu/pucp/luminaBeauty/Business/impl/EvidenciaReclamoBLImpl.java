package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.EvidenciaReclamoBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.DAO.EvidenciaReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EmpleadoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EvidenciaReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.EvidenciaReclamo;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class EvidenciaReclamoBLImpl implements EvidenciaReclamoBL {

    private final EvidenciaReclamoDAO evidenciaReclamoDAO = new EvidenciaReclamoDAOImpl();
    private final ReclamoDAO reclamoDAO = new ReclamoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();

    @Override
    public EvidenciaReclamo registrarEvidenciaReclamo(EvidenciaReclamo evidencia) throws Exception {
        try {
            validarDatosEvidencia(evidencia);
            validarRelacionesEvidencia(evidencia);

            if (evidencia.getTipo_archivo() == null || evidencia.getTipo_archivo().trim().isEmpty()) {
                evidencia.setTipo_archivo("IMAGEN");
            }

            evidencia.setTipo_archivo(evidencia.getTipo_archivo().trim().toUpperCase());
            validarTipoArchivo(evidencia.getTipo_archivo());

            EvidenciaReclamo evidenciaRegistrada = evidenciaReclamoDAO.insertar(evidencia);
            TransactionContext.commit();

            return evidenciaRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar evidencia de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public EvidenciaReclamo actualizarEvidenciaReclamo(EvidenciaReclamo evidencia) throws Exception {
        try {
            if (evidencia == null || evidencia.getId_evidencia_reclamo() <= 0) {
                throw new Exception("El ID de la evidencia no es válido.");
            }

            EvidenciaReclamo evidenciaExistente = evidenciaReclamoDAO.buscarPorId(
                    evidencia.getId_evidencia_reclamo()
            );

            if (evidenciaExistente == null) {
                throw new Exception("La evidencia de reclamo no existe.");
            }

            validarDatosEvidencia(evidencia);
            validarRelacionesEvidencia(evidencia);

            if (evidencia.getTipo_archivo() == null || evidencia.getTipo_archivo().trim().isEmpty()) {
                evidencia.setTipo_archivo(evidenciaExistente.getTipo_archivo());
            }

            evidencia.setTipo_archivo(evidencia.getTipo_archivo().trim().toUpperCase());
            validarTipoArchivo(evidencia.getTipo_archivo());

            EvidenciaReclamo evidenciaActualizada = evidenciaReclamoDAO.actualizar(evidencia);
            TransactionContext.commit();

            return evidenciaActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar evidencia de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarEvidenciaReclamo(int idEvidenciaReclamo) throws Exception {
        try {
            if (idEvidenciaReclamo <= 0) {
                throw new Exception("El ID de la evidencia no es válido.");
            }

            EvidenciaReclamo evidencia = evidenciaReclamoDAO.buscarPorId(idEvidenciaReclamo);

            if (evidencia == null) {
                throw new Exception("La evidencia de reclamo no existe.");
            }

            evidenciaReclamoDAO.eliminar(evidencia);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar evidencia de reclamo: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public EvidenciaReclamo buscarEvidenciaReclamo(int idEvidenciaReclamo) throws Exception {
        try {
            if (idEvidenciaReclamo <= 0) {
                throw new Exception("El ID de la evidencia no es válido.");
            }

            return evidenciaReclamoDAO.buscarPorId(idEvidenciaReclamo);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarEvidenciasReclamo() throws Exception {
        try {
            return evidenciaReclamoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorReclamo(int idReclamo) throws Exception {
        try {
            if (idReclamo <= 0) {
                throw new Exception("El ID del reclamo no es válido.");
            }

            Reclamo reclamo = reclamoDAO.buscarPorId(idReclamo);

            if (reclamo == null) {
                throw new Exception("El reclamo no existe.");
            }

            ArrayList<EvidenciaReclamo> evidencias = evidenciaReclamoDAO.listarTodos();
            ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

            for (EvidenciaReclamo evidencia : evidencias) {
                if (evidencia.getReclamo() != null &&
                        evidencia.getReclamo().getId_reclamo() == idReclamo) {
                    resultado.add(evidencia);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorTipo(String tipoArchivo) throws Exception {
        try {
            if (tipoArchivo == null || tipoArchivo.trim().isEmpty()) {
                throw new Exception("El tipo de archivo es obligatorio.");
            }

            tipoArchivo = tipoArchivo.trim().toUpperCase();
            validarTipoArchivo(tipoArchivo);

            ArrayList<EvidenciaReclamo> evidencias = evidenciaReclamoDAO.listarTodos();
            ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

            for (EvidenciaReclamo evidencia : evidencias) {
                if (evidencia.getTipo_archivo() != null &&
                        evidencia.getTipo_archivo().equalsIgnoreCase(tipoArchivo)) {
                    resultado.add(evidencia);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<EvidenciaReclamo> evidencias = evidenciaReclamoDAO.listarTodos();
            ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

            for (EvidenciaReclamo evidencia : evidencias) {
                if (evidencia.getSubido_por_cliente() != null &&
                        evidencia.getSubido_por_cliente().getId_usuario() == idCliente) {
                    resultado.add(evidencia);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarEvidenciasPorEmpleado(int idEmpleado) throws Exception {
        try {
            if (idEmpleado <= 0) {
                throw new Exception("El ID del empleado no es válido.");
            }

            Empleado empleado = empleadoDAO.buscarPorId(idEmpleado);

            if (empleado == null) {
                throw new Exception("El empleado no existe.");
            }

            ArrayList<EvidenciaReclamo> evidencias = evidenciaReclamoDAO.listarTodos();
            ArrayList<EvidenciaReclamo> resultado = new ArrayList<>();

            for (EvidenciaReclamo evidencia : evidencias) {
                if (evidencia.getSubido_por_empleado() != null &&
                        evidencia.getSubido_por_empleado().getId_usuario() == idEmpleado) {
                    resultado.add(evidencia);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosEvidencia(EvidenciaReclamo evidencia) throws Exception {
        if (evidencia == null) {
            throw new Exception("La evidencia de reclamo no puede ser nula.");
        }

        if (evidencia.getReclamo() == null ||
                evidencia.getReclamo().getId_reclamo() <= 0) {
            throw new Exception("Debe asignar un reclamo válido.");
        }

        if (evidencia.getUrl_archivo() == null ||
                evidencia.getUrl_archivo().trim().isEmpty()) {
            throw new Exception("La URL del archivo es obligatoria.");
        }

        boolean tieneCliente = evidencia.getSubido_por_cliente() != null &&
                evidencia.getSubido_por_cliente().getId_usuario() > 0;

        boolean tieneEmpleado = evidencia.getSubido_por_empleado() != null &&
                evidencia.getSubido_por_empleado().getId_usuario() > 0;

        if (tieneCliente && tieneEmpleado) {
            throw new Exception("La evidencia solo puede ser subida por cliente o empleado, no ambos.");
        }

        if (!tieneCliente && !tieneEmpleado) {
            throw new Exception("Debe indicar quién subió la evidencia.");
        }

        if (evidencia.getSubido_por_cliente() != null &&
                evidencia.getSubido_por_cliente().getId_usuario() <= 0) {
            throw new Exception("El cliente que sube la evidencia no es válido.");
        }

        if (evidencia.getSubido_por_empleado() != null &&
                evidencia.getSubido_por_empleado().getId_usuario() <= 0) {
            throw new Exception("El empleado que sube la evidencia no es válido.");
        }
    }

    private void validarRelacionesEvidencia(EvidenciaReclamo evidencia) throws Exception {
        Reclamo reclamo = reclamoDAO.buscarPorId(evidencia.getReclamo().getId_reclamo());

        if (reclamo == null) {
            throw new Exception("El reclamo asociado a la evidencia no existe.");
        }

        if (evidencia.getSubido_por_cliente() != null) {
            Cliente cliente = clienteDAO.buscarPorId(
                    evidencia.getSubido_por_cliente().getId_usuario()
            );

            if (cliente == null) {
                throw new Exception("El cliente que sube la evidencia no existe.");
            }
        }

        if (evidencia.getSubido_por_empleado() != null) {
            Empleado empleado = empleadoDAO.buscarPorId(
                    evidencia.getSubido_por_empleado().getId_usuario()
            );

            if (empleado == null) {
                throw new Exception("El empleado que sube la evidencia no existe.");
            }
        }
    }

    private void validarTipoArchivo(String tipoArchivo) throws Exception {
        if (!tipoArchivo.equals("IMAGEN") &&
                !tipoArchivo.equals("VIDEO") &&
                !tipoArchivo.equals("DOCUMENTO")) {

            throw new Exception("Tipo de archivo no válido.");
        }
    }
}
