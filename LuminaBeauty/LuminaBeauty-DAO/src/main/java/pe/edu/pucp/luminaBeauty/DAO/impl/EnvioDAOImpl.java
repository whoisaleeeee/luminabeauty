package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class EnvioDAOImpl implements EnvioDAO {

    @Override
    public Envio insertar(Envio envio) throws Exception {
        String sql = """
                INSERT INTO Envio(fechaEnvio, fechaEntregaEstimada, fechaEntregaReal, estado, numeroSeguimiento, idPedido, idDireccion)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (envio.getFechaEnvio() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(1, Timestamp.valueOf(envio.getFechaEnvio()));
            }

            if (envio.getFechaEntregaEstimada() == null) {
                stmt.setNull(2, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(envio.getFechaEntregaEstimada()));
            }

            if (envio.getFechaEntregaReal() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(envio.getFechaEntregaReal()));
            }

            stmt.setString(4, envio.getEstado());
            stmt.setString(5, envio.getNumeroSeguimiento());
            stmt.setInt(6, envio.getPedido().getId());
            stmt.setInt(7, envio.getDireccion().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    envio.setId(rs.getInt(1));
                }
            }

            return envio;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Envio envio) throws Exception {
        String sql = """
                DELETE FROM Envio
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, envio.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Envio buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, fechaEnvio, fechaEntregaEstimada, fechaEntregaReal,
                       estado, numeroSeguimiento, idPedido, idDireccion
                FROM Envio
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Envio actualizar(Envio envio) throws Exception {
        String sql = """
                UPDATE Envio
                SET fechaEnvio = ?,
                    fechaEntregaEstimada = ?,
                    fechaEntregaReal = ?,
                    estado = ?,
                    numeroSeguimiento = ?,
                    idPedido = ?,
                    idDireccion = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (envio.getFechaEnvio() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(1, Timestamp.valueOf(envio.getFechaEnvio()));
            }

            if (envio.getFechaEntregaEstimada() == null) {
                stmt.setNull(2, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(envio.getFechaEntregaEstimada()));
            }

            if (envio.getFechaEntregaReal() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(envio.getFechaEntregaReal()));
            }

            stmt.setString(4, envio.getEstado());
            stmt.setString(5, envio.getNumeroSeguimiento());
            stmt.setInt(6, envio.getPedido().getId());
            stmt.setInt(7, envio.getDireccion().getId());
            stmt.setInt(8, envio.getId());

            stmt.executeUpdate();

            return envio;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Envio> listarTodos() throws Exception {
        ArrayList<Envio> envios = new ArrayList<>();

        String sql = """
                SELECT id, fechaEnvio, fechaEntregaEstimada, fechaEntregaReal,
                       estado, numeroSeguimiento, idPedido, idDireccion
                FROM Envio
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                envios.add(mapearEnvio(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return envios;
    }

    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Envio envio = new Envio();

        envio.setId(rs.getInt("id"));

        Timestamp fechaEnvio = rs.getTimestamp("fechaEnvio");
        if (fechaEnvio != null) {
            envio.setFechaEnvio(fechaEnvio.toLocalDateTime());
        }

        Timestamp fechaEstimada = rs.getTimestamp("fechaEntregaEstimada");
        if (fechaEstimada != null) {
            envio.setFechaEntregaEstimada(fechaEstimada.toLocalDateTime());
        }

        Timestamp fechaReal = rs.getTimestamp("fechaEntregaReal");
        if (fechaReal != null) {
            envio.setFechaEntregaReal(fechaReal.toLocalDateTime());
        }

        envio.setEstado(rs.getString("estado"));
        envio.setNumeroSeguimiento(rs.getString("numeroSeguimiento"));

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("idPedido"));
        envio.setPedido(pedido);

        Direccion direccion = new Direccion();
        direccion.setId(rs.getInt("idDireccion"));
        envio.setDireccion(direccion);

        return envio;
    }
}