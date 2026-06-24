package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.DAO.MarcaDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.MarcaDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;

import java.util.List;

public class MarcaBLImpl implements MarcaBL {
    MarcaDAO marcaDAO = new MarcaDAOImpl();
    @Override
    public List<Marca> listaMarcas() throws Exception {
        return marcaDAO.listarTodos();
    }
}
