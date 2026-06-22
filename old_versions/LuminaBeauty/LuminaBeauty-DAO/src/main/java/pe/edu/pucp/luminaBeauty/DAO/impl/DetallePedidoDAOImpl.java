package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DetallePedidoDAO;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    @Override
    public DetallePedido insertar(DetallePedido detalle) throws Exception {
        String sql = """
                INSERT INTO DetallePedido(cantidad, precioUnitario, subtotal, idPedido, idProducto)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setBigDecimal(3, detalle.getSubtotal());
            stmt.setInt(4, detalle.getPedido().getId());
            stmt.setInt(5, detalle.getProducto().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId(rs.getInt(1));
                }
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(DetallePedido detalle) throws Exception {
        String sql = """
                DELETE FROM DetallePedido
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetallePedido buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, cantidad, precioUnitario, subtotal, idPedido, idProducto
                FROM DetallePedido
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDetallePedido(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public DetallePedido actualizar(DetallePedido detalle) throws Exception {
        String sql = """
                UPDATE DetallePedido
                SET cantidad = ?,
                    precioUnitario = ?,
                    subtotal = ?,
                    idPedido = ?,
                    idProducto = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setBigDecimal(3, detalle.getSubtotal());
            stmt.setInt(4, detalle.getPedido().getId());
            stmt.setInt(5, detalle.getProducto().getId());
            stmt.setInt(6, detalle.getId());

            stmt.executeUpdate();

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetallePedido> listarTodos() throws Exception {
        ArrayList<DetallePedido> detalles = new ArrayList<>();

        String sql = """
                SELECT id, cantidad, precioUnitario, subtotal, idPedido, idProducto
                FROM DetallePedido
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetallePedido(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return detalles;
    }

    private DetallePedido mapearDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detalle = new DetallePedido();

        detalle.setId(rs.getInt("id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("idPedido"));
        detalle.setPedido(pedido);

        Producto producto = new Producto();
        producto.setId(rs.getInt("idProducto"));
        detalle.setProducto(producto);

        return detalle;
    }
}