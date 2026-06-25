package pe.edu.pucp.luminaBeauty.DAO;

import java.util.ArrayList;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;

public interface MovimientoPuntosFidelidadDAO
        extends BaseDAO<MovimientoPuntosFidelidad, Integer> {

    ArrayList<MovimientoPuntosFidelidad> listarPorCliente(int idCliente)
            throws Exception;
}