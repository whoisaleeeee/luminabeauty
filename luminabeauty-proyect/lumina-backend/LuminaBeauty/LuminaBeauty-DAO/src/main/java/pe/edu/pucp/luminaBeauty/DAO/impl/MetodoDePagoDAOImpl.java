package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MetodoDePagoDAO;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class MetodoDePagoDAOImpl implements MetodoDePagoDAO {

    @Override
    public MetodoDePago insertar(MetodoDePago metodo) throws Exception {
        String sql = """
                INSERT INTO MetodoDePago(nombre, descripcion, icono)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, metodo.getNombre());
            stmt.setString(2, metodo.getDescripcion());
            stmt.setString(3, metodo.getIcono());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    metodo.setId(rs.getInt(1));
                }
            }

            return metodo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(MetodoDePago metodo) throws Exception {
        String sql = """
                DELETE FROM MetodoDePago
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, metodo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetodoDePago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, nombre, descripcion, icono
                FROM MetodoDePago
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMetodoDePago(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MetodoDePago actualizar(MetodoDePago metodo) throws Exception {
        String sql = """
                UPDATE MetodoDePago
                SET nombre = ?,
                    descripcion = ?,
                    icono = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, metodo.getNombre());
            stmt.setString(2, metodo.getDescripcion());
            stmt.setString(3, metodo.getIcono());
            stmt.setInt(4, metodo.getId());

            stmt.executeUpdate();

            return metodo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<MetodoDePago> listarTodos() throws Exception {
        ArrayList<MetodoDePago> metodos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, icono
                FROM MetodoDePago
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                metodos.add(mapearMetodoDePago(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return metodos;
    }

    private MetodoDePago mapearMetodoDePago(ResultSet rs) throws SQLException {
        MetodoDePago metodo = new MetodoDePago();

        metodo.setId(rs.getInt("id"));
        metodo.setNombre(rs.getString("nombre"));
        metodo.setDescripcion(rs.getString("descripcion"));
        metodo.setIcono(rs.getString("icono"));

        return metodo;
    }
}