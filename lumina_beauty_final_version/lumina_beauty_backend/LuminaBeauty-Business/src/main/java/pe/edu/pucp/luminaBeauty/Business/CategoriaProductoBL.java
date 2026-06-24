
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;

import java.util.ArrayList;

public interface CategoriaProductoBL {

    CategoriaProducto registrarCategoria(CategoriaProducto categoria) throws Exception;

    CategoriaProducto actualizarCategoria(CategoriaProducto categoria) throws Exception;

    void eliminarCategoria(int idCategoria) throws Exception;

    CategoriaProducto buscarCategoria(int idCategoria) throws Exception;

    ArrayList<CategoriaProducto> listarCategorias() throws Exception;

    ArrayList<CategoriaProducto> buscarCategoriasPorNombre(String nombre) throws Exception;
}
