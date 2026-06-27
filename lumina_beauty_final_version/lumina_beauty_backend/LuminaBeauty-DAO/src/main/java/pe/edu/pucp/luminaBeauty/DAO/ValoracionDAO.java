package pe.edu.pucp.luminaBeauty.DAO;

import java.util.ArrayList;
import pe.edu.pucp.luminaBeauty.Model.Valoracion;

public interface ValoracionDAO extends BaseDAO<Valoracion, Integer> {

    ArrayList<Valoracion> listarPublicadasPorProducto(int idProducto)
            throws Exception;
}