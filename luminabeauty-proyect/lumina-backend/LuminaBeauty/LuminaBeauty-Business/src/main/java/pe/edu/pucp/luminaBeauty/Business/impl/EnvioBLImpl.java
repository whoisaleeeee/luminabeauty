package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.EnvioBL;
import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.EnvioDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class EnvioBLImpl implements EnvioBL {

    private EnvioDAO envioDAO = new EnvioDAOImpl();

    @Override
    public Envio crearEnvio(Envio envio) throws Exception {
        try {
            if (envio == null) {
                throw new Exception("El envío no puede ser nulo.");
            }

            if (envio.getPedido() == null) {
                throw new Exception("El envío debe tener un pedido.");
            }

            if (envio.getDireccion() == null) {
                throw new Exception("El envío debe tener una dirección.");
            }

            if (envio.getEstado() == null) {
                envio.setEstado("PREPARANDO");
            }

            envioDAO.insertar(envio);

            TransactionContext.commit();

            return envio;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al crear el envío.", ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void actualizarEstado(Integer idEnvio, String nuevoEstado) throws Exception {
        try {
            Envio envio = envioDAO.buscarPorId(idEnvio);

            if (envio == null) {
                throw new Exception("El envío no existe.");
            }

            envio.setEstado(nuevoEstado);
            envioDAO.actualizar(envio);

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar estado del envío.", ex);
        } finally {
            TransactionContext.close();
        }
    }
}