package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DireccionDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class DireccionDAOImpl implements DireccionDAO {

    @Override
    public Direccion insertar(Direccion direccion) throws Exception {
        String sql = """
                INSERT INTO Direccion(direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, direccion.getDireccion());
            stmt.setString(2, direccion.getCiudad());
            stmt.setString(3, direccion.getPais());
            stmt.setString(4, direccion.getReferencia());
            stmt.setString(5, direccion.getCodigoPostal());
            stmt.setBoolean(6, direccion.isEsPrincipal());
            stmt.setInt(7, direccion.getCliente().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    direccion.setId(rs.getInt(1));
                }
            }

            return direccion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Direccion direccion) throws Exception {
        String sql = """
                DELETE FROM Direccion
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, direccion.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Direccion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente
                FROM Direccion
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDireccion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Direccion actualizar(Direccion direccion) throws Exception {
        String sql = """
                UPDATE Direccion
                SET direccion = ?,
                    ciudad = ?,
                    pais = ?,
                    referencia = ?,
                    codigoPostal = ?,
                    esPrincipal = ?,
                    idCliente = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, direccion.getDireccion());
            stmt.setString(2, direccion.getCiudad());
            stmt.setString(3, direccion.getPais());
            stmt.setString(4, direccion.getReferencia());
            stmt.setString(5, direccion.getCodigoPostal());
            stmt.setBoolean(6, direccion.isEsPrincipal());
            stmt.setInt(7, direccion.getCliente().getId());
            stmt.setInt(8, direccion.getId());

            stmt.executeUpdate();

            return direccion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Direccion> listarTodos() throws Exception {
        ArrayList<Direccion> direcciones = new ArrayList<>();

        String sql = """
                SELECT id, direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente
                FROM Direccion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                direcciones.add(mapearDireccion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return direcciones;
    }

    private Direccion mapearDireccion(ResultSet rs) throws SQLException {
        Direccion direccion = new Direccion();

        direccion.setId(rs.getInt("id"));
        direccion.setDireccion(rs.getString("direccion"));
        direccion.setCiudad(rs.getString("ciudad"));
        direccion.setPais(rs.getString("pais"));
        direccion.setReferencia(rs.getString("referencia"));
        direccion.setCodigoPostal(rs.getString("codigoPostal"));
        direccion.setEsPrincipal(rs.getBoolean("esPrincipal"));

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        direccion.setCliente(cliente);

        return direccion;
    }
}