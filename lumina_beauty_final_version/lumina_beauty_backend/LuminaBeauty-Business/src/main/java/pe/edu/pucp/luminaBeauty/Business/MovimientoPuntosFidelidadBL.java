package pe.edu.pucp.luminaBeauty.Business;

import java.util.ArrayList;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;

public interface MovimientoPuntosFidelidadBL {

    ArrayList<MovimientoPuntosFidelidad> listarMovimientosPorCliente(
            int idCliente
    ) throws Exception;
}