
package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.MarcaBL;
import pe.edu.pucp.luminaBeauty.DAO.MarcaDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.MarcaDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Marca;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class MarcaBLImpl implements MarcaBL {

    private final MarcaDAO marcaDAO = new MarcaDAOImpl();

    @Override
    public Marca registrarMarca(Marca marca) throws Exception {
        try {
            validarDatosMarca(marca);

            if (marca.getEstado() != 0 && marca.getEstado() != 1) {
                marca.setEstado(1);
            }

            Marca marcaRegistrada = marcaDAO.insertar(marca);
            TransactionContext.commit();

            return marcaRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar marca: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Marca actualizarMarca(Marca marca) throws Exception {
        try {
            if (marca.getId_marca() <= 0) {
                throw new Exception("El ID de la marca no es válido.");
            }

            Marca marcaExistente = marcaDAO.buscarPorId(marca.getId_marca());

            if (marcaExistente == null) {
                throw new Exception("La marca no existe.");
            }

            validarDatosMarca(marca);

            Marca marcaActualizada = marcaDAO.actualizar(marca);
            TransactionContext.commit();

            return marcaActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar marca: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarMarca(int idMarca) throws Exception {
        try {
            if (idMarca <= 0) {
                throw new Exception("El ID de la marca no es válido.");
            }

            Marca marca = marcaDAO.buscarPorId(idMarca);

            if (marca == null) {
                throw new Exception("La marca no existe.");
            }

            marcaDAO.eliminar(marca);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar marca: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public Marca buscarMarca(int idMarca) throws Exception {
        try {
            if (idMarca <= 0) {
                throw new Exception("El ID de la marca no es válido.");
            }

            return marcaDAO.buscarPorId(idMarca);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Marca> listarMarcas() throws Exception {
        try {
            return marcaDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Marca> buscarMarcasPorNombre(String nombre) throws Exception {
        try {
            ArrayList<Marca> marcas = marcaDAO.listarTodos();
            ArrayList<Marca> resultado = new ArrayList<>();

            if (nombre == null || nombre.trim().isEmpty()) {
                return marcas;
            }

            String nombreBuscado = nombre.trim().toLowerCase();

            for (Marca marca : marcas) {
                if (marca.getNombre() != null &&
                        marca.getNombre().toLowerCase().contains(nombreBuscado)) {
                    resultado.add(marca);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosMarca(Marca marca) throws Exception {
        if (marca == null) {
            throw new Exception("La marca no puede ser nula.");
        }

        if (marca.getNombre() == null || marca.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la marca es obligatorio.");
        }

        if (marca.getEstado() != 0 && marca.getEstado() != 1) {
            marca.setEstado(1);
        }
    }
}
