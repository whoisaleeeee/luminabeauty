package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ClienteBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class ClienteBLImpl implements ClienteBL {

    private ClienteDAO clienteDAO = new ClienteDAOImpl();

    @Override
    public Cliente registrarCliente(Cliente cliente) throws Exception {
        try {
            if (cliente == null) {
                throw new Exception("El cliente no puede ser nulo.");
            }

            if (cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
                throw new Exception("El correo es obligatorio.");
            }

            if (cliente.getContrasena() == null || cliente.getContrasena().isBlank()) {
                throw new Exception("La contraseña es obligatoria.");
            }

            if (cliente.getNivelCliente() == null) {
                cliente.setNivelCliente("BRONCE");
            }

            clienteDAO.insertar(cliente);

            TransactionContext.commit();

            return cliente;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar cliente.", ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void sumarPuntos(Integer idCliente, int puntos) throws Exception {
        try {
            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            cliente.setPuntosFidelidad(cliente.getPuntosFidelidad() + puntos);

            if (cliente.getPuntosFidelidad() >= 1000) {
                cliente.setNivelCliente("PLATINO");
            } else if (cliente.getPuntosFidelidad() >= 500) {
                cliente.setNivelCliente("ORO");
            } else if (cliente.getPuntosFidelidad() >= 200) {
                cliente.setNivelCliente("PLATA");
            } else {
                cliente.setNivelCliente("BRONCE");
            }

            clienteDAO.actualizar(cliente);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al sumar puntos.", ex);
        } finally {
            TransactionContext.close();
        }
    }
}