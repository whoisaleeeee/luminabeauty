package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.DireccionBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.DireccionDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.DireccionDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class DireccionBLImpl implements DireccionBL {

    private final DireccionDAO direccionDAO = new DireccionDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    @Override
    public Direccion registrarDireccion(Direccion direccion) throws Exception {
        try {
            validarDatosDireccion(direccion);

            Cliente cliente = clienteDAO.buscarPorId(direccion.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado a la dirección no existe.");
            }

            if (direccion.getPais() == null || direccion.getPais().trim().isEmpty()) {
                direccion.setPais("Peru");
            }

            Direccion direccionRegistrada = direccionDAO.insertar(direccion);
            TransactionContext.commit();

            return direccionRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar dirección: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Direccion actualizarDireccion(Direccion direccion) throws Exception {
        try {
            if (direccion == null || direccion.getId_direccion() <= 0) {
                throw new Exception("El ID de la dirección no es válido.");
            }

            Direccion direccionExistente = direccionDAO.buscarPorId(direccion.getId_direccion());

            if (direccionExistente == null) {
                throw new Exception("La dirección no existe.");
            }

            validarDatosDireccion(direccion);

            Cliente cliente = clienteDAO.buscarPorId(direccion.getCliente().getId_usuario());

            if (cliente == null) {
                throw new Exception("El cliente asociado a la dirección no existe.");
            }

            if (direccion.getPais() == null || direccion.getPais().trim().isEmpty()) {
                direccion.setPais("Peru");
            }

            Direccion direccionActualizada = direccionDAO.actualizar(direccion);
            TransactionContext.commit();

            return direccionActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar dirección: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarDireccion(int idDireccion) throws Exception {
        try {
            if (idDireccion <= 0) {
                throw new Exception("El ID de la dirección no es válido.");
            }

            Direccion direccion = direccionDAO.buscarPorId(idDireccion);

            if (direccion == null) {
                throw new Exception("La dirección no existe.");
            }

            direccionDAO.eliminar(direccion);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar dirección: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Direccion buscarDireccion(int idDireccion) throws Exception {
        try {
            if (idDireccion <= 0) {
                throw new Exception("El ID de la dirección no es válido.");
            }

            return direccionDAO.buscarPorId(idDireccion);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Direccion> listarDirecciones() throws Exception {
        try {
            return direccionDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Direccion> listarDireccionesPorCliente(int idCliente) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            ArrayList<Direccion> direcciones = direccionDAO.listarTodos();
            ArrayList<Direccion> resultado = new ArrayList<>();

            for (Direccion direccion : direcciones) {
                if (direccion.getCliente() != null &&
                        direccion.getCliente().getId_usuario() == idCliente) {
                    resultado.add(direccion);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosDireccion(Direccion direccion) throws Exception {
        if (direccion == null) {
            throw new Exception("La dirección no puede ser nula.");
        }

        if (direccion.getCliente() == null || direccion.getCliente().getId_usuario() <= 0) {
            throw new Exception("Debe asignar un cliente válido.");
        }

        if (direccion.getDireccion() == null || direccion.getDireccion().trim().isEmpty()) {
            throw new Exception("La dirección es obligatoria.");
        }

        if (direccion.getCiudad() == null || direccion.getCiudad().trim().isEmpty()) {
            throw new Exception("La ciudad es obligatoria.");
        }
    }
}

