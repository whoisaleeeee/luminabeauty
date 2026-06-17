package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.CarroDeComprasDAO;
import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class CarroDeComprasDAOImpl implements CarroDeComprasDAO {

    @Override
    public CarroDeCompras insertar(CarroDeCompras carro) throws Exception {
        String sql = """
                INSERT INTO CarroDeCompras(fechaCreacion, idCliente)
                VALUES (?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(carro.getFechaCreacion()));
            stmt.setInt(2, carro.getCliente().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    carro.setId(rs.getInt(1));
                }
            }

            return carro;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(CarroDeCompras carro) throws Exception {
        String sql = """
                DELETE FROM CarroDeCompras
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, carro.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CarroDeCompras buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, fechaCreacion, idCliente
                FROM CarroDeCompras
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCarro(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public CarroDeCompras actualizar(CarroDeCompras carro) throws Exception {
        String sql = """
                UPDATE CarroDeCompras
                SET fechaCreacion = ?,
                    idCliente = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(carro.getFechaCreacion()));
            stmt.setInt(2, carro.getCliente().getId());
            stmt.setInt(3, carro.getId());

            stmt.executeUpdate();

            return carro;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<CarroDeCompras> listarTodos() throws Exception {
        ArrayList<CarroDeCompras> carros = new ArrayList<>();

        String sql = """
                SELECT id, fechaCreacion, idCliente
                FROM CarroDeCompras
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                carros.add(mapearCarro(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return carros;
    }

    private CarroDeCompras mapearCarro(ResultSet rs) throws SQLException {
        CarroDeCompras carro = new CarroDeCompras();

        carro.setId(rs.getInt("id"));
        carro.setFechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime());

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        carro.setCliente(cliente);

        return carro;
    }
}