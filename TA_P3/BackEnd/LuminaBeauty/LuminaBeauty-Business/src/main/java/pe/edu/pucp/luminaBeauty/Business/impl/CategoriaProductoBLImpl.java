package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CategoriaProductoBL;
import pe.edu.pucp.luminaBeauty.DAO.impl.CategoriaProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;

public class CategoriaProductoBLImpl implements CategoriaProductoBL {
    private CategoriaProductoDAOImpl dao = new CategoriaProductoDAOImpl();

    @Override
    public java.util.ArrayList<CategoriaProducto> listarTodos() throws Exception {
        return dao.listarTodos();
    }
}
