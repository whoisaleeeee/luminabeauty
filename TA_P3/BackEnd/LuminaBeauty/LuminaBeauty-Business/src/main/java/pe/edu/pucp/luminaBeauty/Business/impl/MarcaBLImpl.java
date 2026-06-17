package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.DAO.impl.MarcaDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;

public class MarcaBLImpl implements MarcaBL {
    private MarcaDAOImpl dao = new MarcaDAOImpl();

    @Override
    public java.util.ArrayList<Marca> listarTodos() throws Exception {
        return dao.listarTodos();
    }
}
