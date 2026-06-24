package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.CategoriaProductoDAO;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class CategoriaProductoDAOImpl implements CategoriaProductoDAO {

    @Override
    public CategoriaProducto insertar(CategoriaProducto categoria) throws Exception {

        String sql = """
                INSERT INTO categoria_producto(nombre, descripcion, estado)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId_categoria(rs.getInt(1));
                }
            }

            return categoria;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(CategoriaProducto categoria) throws Exception {

        String sql = """
                UPDATE categoria_producto
                SET estado = 0
                WHERE id_categoria = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, categoria.getId_categoria());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la categoría con ID: " + categoria.getId_categoria());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoriaProducto buscarPorId(Integer id) throws Exception {

        String sql = """
                SELECT id_categoria, nombre, descripcion, estado, creado_en, actualizado_en
                FROM categoria_producto
                WHERE id_categoria = ?
                  AND estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCategoria(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public CategoriaProducto actualizar(CategoriaProducto categoria) throws Exception {

        String sql = """
                UPDATE categoria_producto
                SET nombre = ?,
                    descripcion = ?,
                    estado = ?
                WHERE id_categoria = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getEstado());
            stmt.setInt(4, categoria.getId_categoria());

            stmt.executeUpdate();

            return categoria;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<CategoriaProducto> listarTodos() throws Exception {

        ArrayList<CategoriaProducto> categorias = new ArrayList<>();

        String sql = """
                SELECT id_categoria, nombre, descripcion, estado, creado_en, actualizado_en
                FROM categoria_producto
                WHERE estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapearCategoria(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categorias;
    }

    private CategoriaProducto mapearCategoria(ResultSet rs) throws SQLException {

        CategoriaProducto categoria = new CategoriaProducto();

        categoria.setId_categoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("nombre"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setEstado(rs.getInt("estado"));

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            categoria.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            categoria.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return categoria;
    }
}