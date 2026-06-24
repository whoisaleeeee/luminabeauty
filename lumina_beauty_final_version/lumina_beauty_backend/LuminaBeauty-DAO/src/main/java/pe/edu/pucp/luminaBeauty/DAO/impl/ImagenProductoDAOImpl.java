package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ImagenProductoDAO;
import pe.edu.pucp.luminaBeauty.Model.ImagenProducto;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ImagenProductoDAOImpl implements ImagenProductoDAO {

    @Override
    public ImagenProducto insertar(ImagenProducto imagen) throws Exception {
        String sql = """
                INSERT INTO imagen_producto(
                    id_producto,
                    url_imagen,
                    texto_alternativo,
                    es_principal,
                    orden_visualizacion
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, imagen.getProducto().getId_producto());
            stmt.setString(2, imagen.getUrl_imagen());
            stmt.setString(3, imagen.getTexto_alternativo());
            stmt.setInt(4, imagen.getEs_principal());
            stmt.setInt(5, imagen.getOrden_visualizacion());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    imagen.setId_imagen_producto(rs.getInt(1));
                }
            }

            return imagen;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(ImagenProducto imagen) throws Exception {
        String sql = """
                DELETE FROM imagen_producto
                WHERE id_imagen_producto = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, imagen.getId_imagen_producto());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la imagen del producto con ID: "
                        + imagen.getId_imagen_producto());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ImagenProducto buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_imagen_producto,
                       id_producto,
                       url_imagen,
                       texto_alternativo,
                       es_principal,
                       orden_visualizacion,
                       creado_en
                FROM imagen_producto
                WHERE id_imagen_producto = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearImagenProducto(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public ImagenProducto actualizar(ImagenProducto imagen) throws Exception {
        String sql = """
                UPDATE imagen_producto
                SET id_producto = ?,
                    url_imagen = ?,
                    texto_alternativo = ?,
                    es_principal = ?,
                    orden_visualizacion = ?
                WHERE id_imagen_producto = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, imagen.getProducto().getId_producto());
            stmt.setString(2, imagen.getUrl_imagen());
            stmt.setString(3, imagen.getTexto_alternativo());
            stmt.setInt(4, imagen.getEs_principal());
            stmt.setInt(5, imagen.getOrden_visualizacion());
            stmt.setInt(6, imagen.getId_imagen_producto());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la imagen del producto con ID: "
                        + imagen.getId_imagen_producto());
            }

            return imagen;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ImagenProducto> listarTodos() throws Exception {
        ArrayList<ImagenProducto> imagenes = new ArrayList<>();

        String sql = """
                SELECT id_imagen_producto,
                       id_producto,
                       url_imagen,
                       texto_alternativo,
                       es_principal,
                       orden_visualizacion,
                       creado_en
                FROM imagen_producto
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                imagenes.add(mapearImagenProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return imagenes;
    }

    private ImagenProducto mapearImagenProducto(ResultSet rs) throws SQLException {
        ImagenProducto imagen = new ImagenProducto();

        imagen.setId_imagen_producto(rs.getInt("id_imagen_producto"));
        imagen.setUrl_imagen(rs.getString("url_imagen"));
        imagen.setTexto_alternativo(rs.getString("texto_alternativo"));
        imagen.setEs_principal(rs.getInt("es_principal"));
        imagen.setOrden_visualizacion(rs.getInt("orden_visualizacion"));

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        imagen.setProducto(producto);

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");

        if (fechaCreacion != null) {
            imagen.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        return imagen;
    }
}