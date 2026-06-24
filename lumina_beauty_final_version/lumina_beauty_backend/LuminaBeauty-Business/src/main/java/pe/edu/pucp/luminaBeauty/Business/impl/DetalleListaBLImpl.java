package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.DetalleListaBL;
import pe.edu.pucp.luminaBeauty.DAO.DetalleListaDAO;
import pe.edu.pucp.luminaBeauty.DAO.ListaDeDeseosDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.DetalleListaDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ListaDeDeseosDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.DetalleLista;
import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class DetalleListaBLImpl implements DetalleListaBL {

    private final DetalleListaDAO detalleListaDAO = new DetalleListaDAOImpl();
    private final ListaDeDeseosDAO listaDeDeseosDAO = new ListaDeDeseosDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public DetalleLista registrarDetalleLista(DetalleLista detalleLista) throws Exception {
        try {
            validarDatosDetalleLista(detalleLista);

            ListaDeDeseos lista = listaDeDeseosDAO.buscarPorId(
                    detalleLista.getLista().getId_lista_deseos()
            );

            if (lista == null) {
                throw new Exception("La lista de deseos asociada no existe.");
            }

            Producto producto = productoDAO.buscarPorId(
                    detalleLista.getProducto().getId_producto()
            );

            if (producto == null) {
                throw new Exception("El producto asociado no existe.");
            }

            DetalleLista detalleRegistrado = detalleListaDAO.insertar(detalleLista);
            TransactionContext.commit();

            return detalleRegistrado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar detalle de lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetalleLista actualizarDetalleLista(DetalleLista detalleLista) throws Exception {
        try {
            if (detalleLista == null || detalleLista.getId_detalle_lista_deseos() <= 0) {
                throw new Exception("El ID del detalle de lista no es válido.");
            }

            DetalleLista detalleExistente = detalleListaDAO.buscarPorId(
                    detalleLista.getId_detalle_lista_deseos()
            );

            if (detalleExistente == null) {
                throw new Exception("El detalle de lista de deseos no existe.");
            }

            validarDatosDetalleLista(detalleLista);

            ListaDeDeseos lista = listaDeDeseosDAO.buscarPorId(
                    detalleLista.getLista().getId_lista_deseos()
            );

            if (lista == null) {
                throw new Exception("La lista de deseos asociada no existe.");
            }

            Producto producto = productoDAO.buscarPorId(
                    detalleLista.getProducto().getId_producto()
            );

            if (producto == null) {
                throw new Exception("El producto asociado no existe.");
            }

            DetalleLista detalleActualizado = detalleListaDAO.actualizar(detalleLista);
            TransactionContext.commit();

            return detalleActualizado;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar detalle de lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarDetalleLista(int idDetalleLista) throws Exception {
        try {
            if (idDetalleLista <= 0) {
                throw new Exception("El ID del detalle de lista no es válido.");
            }

            DetalleLista detalleLista = detalleListaDAO.buscarPorId(idDetalleLista);

            if (detalleLista == null) {
                throw new Exception("El detalle de lista de deseos no existe.");
            }

            detalleListaDAO.eliminar(detalleLista);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar detalle de lista de deseos: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public DetalleLista buscarDetalleLista(int idDetalleLista) throws Exception {
        try {
            if (idDetalleLista <= 0) {
                throw new Exception("El ID del detalle de lista no es válido.");
            }

            return detalleListaDAO.buscarPorId(idDetalleLista);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetalleLista> listarDetallesLista() throws Exception {
        try {
            return detalleListaDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetalleLista> listarDetallesPorLista(int idListaDeDeseos) throws Exception {
        try {
            if (idListaDeDeseos <= 0) {
                throw new Exception("El ID de la lista de deseos no es válido.");
            }

            ListaDeDeseos lista = listaDeDeseosDAO.buscarPorId(idListaDeDeseos);

            if (lista == null) {
                throw new Exception("La lista de deseos no existe.");
            }

            ArrayList<DetalleLista> detalles = detalleListaDAO.listarTodos();
            ArrayList<DetalleLista> resultado = new ArrayList<>();

            for (DetalleLista detalle : detalles) {
                if (detalle.getLista() != null &&
                        detalle.getLista().getId_lista_deseos() == idListaDeDeseos) {
                    resultado.add(detalle);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<DetalleLista> listarDetallesPorProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            ArrayList<DetalleLista> detalles = detalleListaDAO.listarTodos();
            ArrayList<DetalleLista> resultado = new ArrayList<>();

            for (DetalleLista detalle : detalles) {
                if (detalle.getProducto() != null &&
                        detalle.getProducto().getId_producto() == idProducto) {
                    resultado.add(detalle);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosDetalleLista(DetalleLista detalleLista) throws Exception {
        if (detalleLista == null) {
            throw new Exception("El detalle de lista no puede ser nulo.");
        }

        if (detalleLista.getLista() == null ||
                detalleLista.getLista().getId_lista_deseos() <= 0) {
            throw new Exception("Debe asignar una lista de deseos válida.");
        }

        if (detalleLista.getProducto() == null ||
                detalleLista.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido.");
        }
    }
}

