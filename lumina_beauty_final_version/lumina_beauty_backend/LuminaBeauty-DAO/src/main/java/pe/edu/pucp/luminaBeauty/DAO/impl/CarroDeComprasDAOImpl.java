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
                INSERT INTO carrito(
                    id_cliente,
                    recordatorio_enviado_en
                )
                VALUES (?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, carro.getCliente().getId_usuario());

            if (carro.getRecordatorio_enviado_en() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(carro.getRecordatorio_enviado_en()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    carro.setId_carrito(rs.getInt(1));
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
                DELETE FROM carrito
                WHERE id_carrito = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, carro.getId_carrito());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el carrito con ID: " + carro.getId_carrito());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CarroDeCompras buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_carrito,
                       id_cliente,
                       creado_en,
                       actualizado_en,
                       recordatorio_enviado_en
                FROM carrito
                WHERE id_carrito = ?
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
                UPDATE carrito
                SET id_cliente = ?,
                    recordatorio_enviado_en = ?
                WHERE id_carrito = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, carro.getCliente().getId_usuario());

            if (carro.getRecordatorio_enviado_en() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(carro.getRecordatorio_enviado_en()));
            } else {
                stmt.setNull(2, Types.TIMESTAMP);
            }

            stmt.setInt(3, carro.getId_carrito());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el carrito con ID: " + carro.getId_carrito());
            }

            return carro;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<CarroDeCompras> listarTodos() throws Exception {
        ArrayList<CarroDeCompras> carros = new ArrayList<>();

        String sql = """
                SELECT id_carrito,
                       id_cliente,
                       creado_en,
                       actualizado_en,
                       recordatorio_enviado_en
                FROM carrito
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

        carro.setId_carrito(rs.getInt("id_carrito"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        carro.setCliente(cliente);

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");
        Timestamp recordatorio = rs.getTimestamp("recordatorio_enviado_en");

        if (fechaCreacion != null) {
            carro.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            carro.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        if (recordatorio != null) {
            carro.setRecordatorio_enviado_en(recordatorio.toLocalDateTime());
        }

        return carro;
    }
}