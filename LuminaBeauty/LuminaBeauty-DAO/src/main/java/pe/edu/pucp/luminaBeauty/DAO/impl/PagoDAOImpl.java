package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public Pago insertar(Pago pago) throws Exception {
        String sql = """
                INSERT INTO Pago(monto, estado, fechaPago, idPedido, idMetodo)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setBigDecimal(1, pago.getMonto());
            stmt.setString(2, pago.getEstado());

            if (pago.getFechaPago() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(pago.getFechaPago()));
            }

            stmt.setInt(4, pago.getPedido().getId());
            stmt.setInt(5, pago.getMetodoDePago().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pago.setId(rs.getInt(1));
                }
            }

            return pago;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Pago pago) throws Exception {
        String sql = """
                DELETE FROM Pago
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pago.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, monto, estado, fechaPago, idPedido, idMetodo
                FROM Pago
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Pago actualizar(Pago pago) throws Exception {
        String sql = """
                UPDATE Pago
                SET monto = ?,
                    estado = ?,
                    fechaPago = ?,
                    idPedido = ?,
                    idMetodo = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBigDecimal(1, pago.getMonto());
            stmt.setString(2, pago.getEstado());

            if (pago.getFechaPago() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(pago.getFechaPago()));
            }

            stmt.setInt(4, pago.getPedido().getId());
            stmt.setInt(5, pago.getMetodoDePago().getId());
            stmt.setInt(6, pago.getId());

            stmt.executeUpdate();

            return pago;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Pago> listarTodos() throws Exception {
        ArrayList<Pago> pagos = new ArrayList<>();

        String sql = """
                SELECT id, monto, estado, fechaPago, idPedido, idMetodo
                FROM Pago
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pagos;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();

        pago.setId(rs.getInt("id"));
        pago.setMonto(rs.getBigDecimal("monto"));
        pago.setEstado(rs.getString("estado"));

        Timestamp fechaPago = rs.getTimestamp("fechaPago");
        if (fechaPago != null) {
            pago.setFechaPago(fechaPago.toLocalDateTime());
        }

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("idPedido"));
        pago.setPedido(pedido);

        MetodoDePago metodo = new MetodoDePago();
        metodo.setId(rs.getInt("idMetodo"));
        pago.setMetodoDePago(metodo);

        return pago;
    }
}