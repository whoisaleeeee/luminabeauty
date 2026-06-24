package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ImagenProductoBL;
import pe.edu.pucp.luminaBeauty.DAO.ImagenProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ImagenProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.ImagenProducto;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.util.ArrayList;

public class ImagenProductoBLImpl implements ImagenProductoBL {

    private final ImagenProductoDAO imagenProductoDAO = new ImagenProductoDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    @Override
    public ImagenProducto registrarImagenProducto(ImagenProducto imagen) throws Exception {
        try {
            validarDatosImagen(imagen);

            Producto producto = productoDAO.buscarPorId(imagen.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado a la imagen no existe.");
            }

            ImagenProducto imagenRegistrada = imagenProductoDAO.insertar(imagen);
            TransactionContext.commit();

            return imagenRegistrada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al registrar imagen del producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ImagenProducto actualizarImagenProducto(ImagenProducto imagen) throws Exception {
        try {
            if (imagen.getId_imagen_producto() <= 0) {
                throw new Exception("El ID de la imagen no es válido.");
            }

            ImagenProducto imagenExistente = imagenProductoDAO.buscarPorId(imagen.getId_imagen_producto());

            if (imagenExistente == null) {
                throw new Exception("La imagen del producto no existe.");
            }

            validarDatosImagen(imagen);

            Producto producto = productoDAO.buscarPorId(imagen.getProducto().getId_producto());

            if (producto == null) {
                throw new Exception("El producto asociado a la imagen no existe.");
            }

            ImagenProducto imagenActualizada = imagenProductoDAO.actualizar(imagen);
            TransactionContext.commit();

            return imagenActualizada;

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al actualizar imagen del producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void eliminarImagenProducto(int idImagenProducto) throws Exception {
        try {
            if (idImagenProducto <= 0) {
                throw new Exception("El ID de la imagen no es válido.");
            }

            ImagenProducto imagen = imagenProductoDAO.buscarPorId(idImagenProducto);

            if (imagen == null) {
                throw new Exception("La imagen del producto no existe.");
            }

            imagenProductoDAO.eliminar(imagen);
            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al eliminar imagen del producto: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ImagenProducto buscarImagenProducto(int idImagenProducto) throws Exception {
        try {
            if (idImagenProducto <= 0) {
                throw new Exception("El ID de la imagen no es válido.");
            }

            return imagenProductoDAO.buscarPorId(idImagenProducto);

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<ImagenProducto> listarImagenesProducto() throws Exception {
        try {
            return imagenProductoDAO.listarTodos();

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<ImagenProducto> listarImagenesPorProducto(int idProducto) throws Exception {
        try {
            if (idProducto <= 0) {
                throw new Exception("El ID del producto no es válido.");
            }

            Producto producto = productoDAO.buscarPorId(idProducto);

            if (producto == null) {
                throw new Exception("El producto no existe.");
            }

            ArrayList<ImagenProducto> imagenes = imagenProductoDAO.listarTodos();
            ArrayList<ImagenProducto> imagenesProducto = new ArrayList<>();

            for (ImagenProducto imagen : imagenes) {
                if (imagen.getProducto() != null &&
                        imagen.getProducto().getId_producto() == idProducto) {

                    imagenesProducto.add(imagen);
                }
            }

            return imagenesProducto;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public void establecerImagenPrincipal(int idImagenProducto) throws Exception {
        try {
            if (idImagenProducto <= 0) {
                throw new Exception("El ID de la imagen no es válido.");
            }

            ImagenProducto imagenPrincipal = imagenProductoDAO.buscarPorId(idImagenProducto);

            if (imagenPrincipal == null) {
                throw new Exception("La imagen del producto no existe.");
            }

            int idProducto = imagenPrincipal.getProducto().getId_producto();

            ArrayList<ImagenProducto> imagenes = imagenProductoDAO.listarTodos();

            for (ImagenProducto imagen : imagenes) {
                if (imagen.getProducto() != null &&
                        imagen.getProducto().getId_producto() == idProducto) {

                    if (imagen.getId_imagen_producto() == idImagenProducto) {
                        imagen.setEs_principal(1);
                    } else {
                        imagen.setEs_principal(0);
                    }

                    imagenProductoDAO.actualizar(imagen);
                }
            }

            TransactionContext.commit();

        } catch (Exception ex) {
            TransactionContext.rollback();
            throw new Exception("Error al establecer imagen principal: " + ex.getMessage(), ex);
        } finally {
            TransactionContext.close();
        }
    }

    private void validarDatosImagen(ImagenProducto imagen) throws Exception {
        if (imagen == null) {
            throw new Exception("La imagen no puede ser nula.");
        }

        if (imagen.getProducto() == null || imagen.getProducto().getId_producto() <= 0) {
            throw new Exception("Debe asignar un producto válido.");
        }

        if (imagen.getUrl_imagen() == null || imagen.getUrl_imagen().trim().isEmpty()) {
            throw new Exception("La URL de la imagen es obligatoria.");
        }

        if (imagen.getEs_principal() != 0 && imagen.getEs_principal() != 1) {
            throw new Exception("El campo es_principal solo puede ser 0 o 1.");
        }

        if (imagen.getOrden_visualizacion() <= 0) {
            throw new Exception("El orden de visualización debe ser mayor a cero.");
        }
    }
}

