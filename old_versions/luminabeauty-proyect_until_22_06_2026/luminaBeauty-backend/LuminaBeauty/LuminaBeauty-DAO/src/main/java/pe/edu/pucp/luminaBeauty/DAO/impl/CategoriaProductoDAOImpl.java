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
                INSERT INTO CategoriaProducto(nombre, descripcion, idCategoriaPadre)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS
        )) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());

            if (categoria.getIdCategoriaPadre() == 0) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, categoria.getIdCategoriaPadre());
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt(1));
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
                DELETE FROM CategoriaProducto
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, categoria.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoriaProducto buscarPorId(Integer id) throws Exception {

        String sql = """
                SELECT id, nombre, descripcion, idCategoriaPadre
                FROM CategoriaProducto
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    CategoriaProducto categoria = new CategoriaProducto();

                    categoria.setId(rs.getInt("id"));
                    categoria.setNombre(rs.getString("nombre"));
                    categoria.setDescripcion(rs.getString("descripcion"));

                    int idPadre = rs.getInt("idCategoriaPadre");

                    if (rs.wasNull()) {
                        categoria.setIdCategoriaPadre(0);
                    } else {
                        categoria.setIdCategoriaPadre(idPadre);
                    }

                    return categoria;
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
                UPDATE CategoriaProducto
                SET nombre = ?,
                    descripcion = ?,
                    idCategoriaPadre = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());

            if (categoria.getIdCategoriaPadre() == 0) {
                stmt.setNull(3, Types.INTEGER);
            } else {
                stmt.setInt(3, categoria.getIdCategoriaPadre());
            }

            stmt.setInt(4, categoria.getId());

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
                SELECT id, nombre, descripcion, idCategoriaPadre
                FROM CategoriaProducto
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                CategoriaProducto categoria = new CategoriaProducto();

                categoria.setId(rs.getInt("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));

                int idPadre = rs.getInt("idCategoriaPadre");

                if (rs.wasNull()) {
                    categoria.setIdCategoriaPadre(0);
                } else {
                    categoria.setIdCategoriaPadre(idPadre);
                }

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categorias;
    }
}