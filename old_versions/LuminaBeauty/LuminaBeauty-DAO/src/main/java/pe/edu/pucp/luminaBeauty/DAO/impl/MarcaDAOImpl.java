package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MarcaDAO;
import pe.edu.pucp.luminaBeauty.Model.Marca;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class MarcaDAOImpl implements MarcaDAO {

    @Override
    public Marca insertar(Marca marca) throws Exception {
        String sql = """
                INSERT INTO Marca(nombre, descripcion, logo)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, marca.getNombre());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getLogo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    marca.setId(rs.getInt(1));
                }
            }

            return marca;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Marca marca) throws Exception {
        String sql = """
                DELETE FROM Marca
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, marca.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Marca buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, nombre, descripcion, logo
                FROM Marca
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMarca(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Marca actualizar(Marca marca) throws Exception {
        String sql = """
                UPDATE Marca
                SET nombre = ?,
                    descripcion = ?,
                    logo = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, marca.getNombre());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getLogo());
            stmt.setInt(4, marca.getId());

            stmt.executeUpdate();

            return marca;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Marca> listarTodos() throws Exception {
        ArrayList<Marca> marcas = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, logo
                FROM Marca
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                marcas.add(mapearMarca(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return marcas;
    }

    private Marca mapearMarca(ResultSet rs) throws SQLException {
        Marca marca = new Marca();

        marca.setId(rs.getInt("id"));
        marca.setNombre(rs.getString("nombre"));
        marca.setDescripcion(rs.getString("descripcion"));
        marca.setLogo(rs.getString("logo"));

        return marca;
    }
}