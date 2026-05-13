package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public Pedido insertar(Pedido pedido) throws Exception {
        String sql = """
                INSERT INTO Pedido(fecha, total, estado, idCarrito, idCupon)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (pedido.getFecha() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            }

            stmt.setBigDecimal(2, pedido.getTotal());
            stmt.setString(3, pedido.getEstado());
            stmt.setInt(4, pedido.getCarroDeCompras().getId());

            if (pedido.getCupon() == null || pedido.getCupon().getId() == 0) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, pedido.getCupon().getId());
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getInt(1));
                }
            }

            return pedido;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Pedido pedido) throws Exception {
        String sql = """
                DELETE FROM Pedido
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pedido.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pedido buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, fecha, total, estado, idCarrito, idCupon
                FROM Pedido
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Pedido actualizar(Pedido pedido) throws Exception {
        String sql = """
                UPDATE Pedido
                SET fecha = ?,
                    total = ?,
                    estado = ?,
                    idCarrito = ?,
                    idCupon = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            if (pedido.getFecha() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            }

            stmt.setBigDecimal(2, pedido.getTotal());
            stmt.setString(3, pedido.getEstado());
            stmt.setInt(4, pedido.getCarroDeCompras().getId());

            if (pedido.getCupon() == null || pedido.getCupon().getId() == 0) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, pedido.getCupon().getId());
            }

            stmt.setInt(6, pedido.getId());

            stmt.executeUpdate();

            return pedido;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Pedido> listarTodos() throws Exception {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        String sql = """
                SELECT id, fecha, total, estado, idCarrito, idCupon
                FROM Pedido
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        pedido.setId(rs.getInt("id"));

        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            pedido.setFecha(fecha.toLocalDateTime());
        }

        pedido.setTotal(rs.getBigDecimal("total"));
        pedido.setEstado(rs.getString("estado"));

        CarroDeCompras carro = new CarroDeCompras();
        carro.setId(rs.getInt("idCarrito"));
        pedido.setCarroDeCompras(carro);

        int idCupon = rs.getInt("idCupon");
        if (!rs.wasNull()) {
            Cupon cupon = new Cupon();
            cupon.setId(idCupon);
            pedido.setCupon(cupon);
        } else {
            pedido.setCupon(null);
        }

        return pedido;
    }
}