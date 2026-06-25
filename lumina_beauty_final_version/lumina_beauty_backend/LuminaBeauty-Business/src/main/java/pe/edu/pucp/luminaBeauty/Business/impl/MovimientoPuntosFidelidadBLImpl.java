package pe.edu.pucp.luminaBeauty.Business.impl;

import java.util.ArrayList;

import pe.edu.pucp.luminaBeauty.Business.MovimientoPuntosFidelidadBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.MovimientoPuntosFidelidadDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.MovimientoPuntosFidelidadDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

public class MovimientoPuntosFidelidadBLImpl
        implements MovimientoPuntosFidelidadBL {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();

    private final MovimientoPuntosFidelidadDAO movimientoDAO =
            new MovimientoPuntosFidelidadDAOImpl();

    @Override
    public ArrayList<MovimientoPuntosFidelidad> listarMovimientosPorCliente(
            int idCliente
    ) throws Exception {
        try {
            if (idCliente <= 0) {
                throw new Exception("El ID del cliente no es válido.");
            }

            Cliente cliente = clienteDAO.buscarPorId(idCliente);

            if (cliente == null) {
                throw new Exception("El cliente no existe.");
            }

            return movimientoDAO.listarPorCliente(idCliente);

        } finally {
            TransactionContext.close();
        }
    }
}