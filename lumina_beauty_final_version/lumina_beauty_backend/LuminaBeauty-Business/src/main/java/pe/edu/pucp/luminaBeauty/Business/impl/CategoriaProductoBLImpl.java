
package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CategoriaProductoBL;
import pe.edu.pucp.luminaBeauty.DAO.CategoriaProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.CategoriaProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class CategoriaProductoBLImpl implements CategoriaProductoBL {

    private final CategoriaProductoDAO categoriaProductoDAO = new CategoriaProductoDAOImpl();

    @Override
    public CategoriaProducto registrarCategoria(CategoriaProducto categoria) throws Exception {
        try {
            validarDatosCategoria(categoria);

            if (categoria.getEstado() != 0 && categoria.getEstado() != 1) {
                categoria.setEstado(1);
            }

            CategoriaProducto categoriaRegistrada = categoriaProductoDAO.insertar(categoria);
            TransactionContext.commit();

            return categoriaRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar categoría: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public CategoriaProducto actualizarCategoria(CategoriaProducto categoria) throws Exception {
        try {
            if (categoria.getId_categoria() <= 0) {
                throw new Exception("El ID de la categoría no es válido.");
            }

            CategoriaProducto categoriaExistente = categoriaProductoDAO.buscarPorId(categoria.getId_categoria());

            if (categoriaExistente == null) {
                throw new Exception("La categoría no existe.");
            }

            validarDatosCategoria(categoria);

            CategoriaProducto categoriaActualizada = categoriaProductoDAO.actualizar(categoria);
            TransactionContext.commit();

            return categoriaActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar categoría: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarCategoria(int idCategoria) throws Exception {
        try {
            if (idCategoria <= 0) {
                throw new Exception("El ID de la categoría no es válido.");
            }

            CategoriaProducto categoria = categoriaProductoDAO.buscarPorId(idCategoria);

            if (categoria == null) {
                throw new Exception("La categoría no existe.");
            }

            categoriaProductoDAO.eliminar(categoria);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar categoría: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public CategoriaProducto buscarCategoria(int idCategoria) throws Exception {
        try {
            if (idCategoria <= 0) {
                throw new Exception("El ID de la categoría no es válido.");
            }

            return categoriaProductoDAO.buscarPorId(idCategoria);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<CategoriaProducto> listarCategorias() throws Exception {
        try {
            return categoriaProductoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<CategoriaProducto> buscarCategoriasPorNombre(String nombre) throws Exception {
        try {
            ArrayList<CategoriaProducto> categorias = categoriaProductoDAO.listarTodos();
            ArrayList<CategoriaProducto> resultado = new ArrayList<>();

            if (nombre == null || nombre.trim().isEmpty()) {
                return categorias;
            }

            String nombreBuscado = nombre.trim().toLowerCase();

            for (CategoriaProducto categoria : categorias) {
                if (categoria.getNombre() != null &&
                        categoria.getNombre().toLowerCase().contains(nombreBuscado)) {
                    resultado.add(categoria);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosCategoria(CategoriaProducto categoria) throws Exception {
        if (categoria == null) {
            throw new Exception("La categoría no puede ser nula.");
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la categoría es obligatorio.");
        }

        if (categoria.getEstado() != 0 && categoria.getEstado() != 1) {
            categoria.setEstado(1);
        }
    }
}
