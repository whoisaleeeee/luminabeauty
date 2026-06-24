package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.MetodoDePagoBL;
import pe.edu.pucp.luminaBeauty.DAO.MetodoDePagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.MetodoDePagoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class MetodoDePagoBLImpl implements MetodoDePagoBL {

    private final MetodoDePagoDAO metodoDePagoDAO = new MetodoDePagoDAOImpl();

    @Override
    public MetodoDePago registrarMetodoDePago(MetodoDePago metodoDePago) throws Exception {
        try {
            validarDatosMetodoDePago(metodoDePago);

            if (metodoDePago.getEstado() != 0 && metodoDePago.getEstado() != 1) {
                metodoDePago.setEstado(1);
            }

            MetodoDePago metodoExistente = buscarMetodoPorNombreInterno(metodoDePago.getNombre());

            if (metodoExistente != null) {
                throw new Exception("Ya existe un método de pago con ese nombre.");
            }

            MetodoDePago metodoRegistrado = metodoDePagoDAO.insertar(metodoDePago);
            TransactionContext.commit();

            return metodoRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar método de pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public MetodoDePago actualizarMetodoDePago(MetodoDePago metodoDePago) throws Exception {
        try {
            if (metodoDePago == null || metodoDePago.getId_metodo_pago() <= 0) {
                throw new Exception("El ID del método de pago no es válido.");
            }

            MetodoDePago metodoExistente = metodoDePagoDAO.buscarPorId(
                    metodoDePago.getId_metodo_pago()
            );

            if (metodoExistente == null) {
                throw new Exception("El método de pago no existe.");
            }

            validarDatosMetodoDePago(metodoDePago);

            if (metodoDePago.getEstado() != 0 && metodoDePago.getEstado() != 1) {
                metodoDePago.setEstado(1);
            }

            MetodoDePago metodoMismoNombre = buscarMetodoPorNombreInterno(
                    metodoDePago.getNombre()
            );

            if (metodoMismoNombre != null &&
                    metodoMismoNombre.getId_metodo_pago() != metodoDePago.getId_metodo_pago()) {
                throw new Exception("Ya existe otro método de pago con ese nombre.");
            }

            MetodoDePago metodoActualizado = metodoDePagoDAO.actualizar(metodoDePago);
            TransactionContext.commit();

            return metodoActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar método de pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarMetodoDePago(int idMetodoPago) throws Exception {
        try {
            if (idMetodoPago <= 0) {
                throw new Exception("El ID del método de pago no es válido.");
            }

            MetodoDePago metodoDePago = metodoDePagoDAO.buscarPorId(idMetodoPago);

            if (metodoDePago == null) {
                throw new Exception("El método de pago no existe.");
            }

            metodoDePagoDAO.eliminar(metodoDePago);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar método de pago: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public MetodoDePago buscarMetodoDePago(int idMetodoPago) throws Exception {
        try {
            if (idMetodoPago <= 0) {
                throw new Exception("El ID del método de pago no es válido.");
            }

            return metodoDePagoDAO.buscarPorId(idMetodoPago);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MetodoDePago> listarMetodosDePago() throws Exception {
        try {
            return metodoDePagoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MetodoDePago> listarMetodosDePagoActivos() throws Exception {
        try {
            ArrayList<MetodoDePago> metodos = metodoDePagoDAO.listarTodos();
            ArrayList<MetodoDePago> resultado = new ArrayList<>();

            for (MetodoDePago metodo : metodos) {
                if (metodo.getEstado() == 1) {
                    resultado.add(metodo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<MetodoDePago> buscarMetodosDePagoPorNombre(String nombre) throws Exception {
        try {
            ArrayList<MetodoDePago> metodos = metodoDePagoDAO.listarTodos();
            ArrayList<MetodoDePago> resultado = new ArrayList<>();

            if (nombre == null || nombre.trim().isEmpty()) {
                return metodos;
            }

            String nombreBuscado = nombre.trim().toLowerCase();

            for (MetodoDePago metodo : metodos) {
                if (metodo.getNombre() != null &&
                        metodo.getNombre().toLowerCase().contains(nombreBuscado)) {
                    resultado.add(metodo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosMetodoDePago(MetodoDePago metodoDePago) throws Exception {
        if (metodoDePago == null) {
            throw new Exception("El método de pago no puede ser nulo.");
        }

        if (metodoDePago.getNombre() == null ||
                metodoDePago.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del método de pago es obligatorio.");
        }

        if (metodoDePago.getEstado() != 0 && metodoDePago.getEstado() != 1) {
            metodoDePago.setEstado(1);
        }
    }

    private MetodoDePago buscarMetodoPorNombreInterno(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        ArrayList<MetodoDePago> metodos = metodoDePagoDAO.listarTodos();

        for (MetodoDePago metodo : metodos) {
            if (metodo.getNombre() != null &&
                    metodo.getNombre().equalsIgnoreCase(nombre.trim())) {
                return metodo;
            }
        }

        return null;
    }
}
