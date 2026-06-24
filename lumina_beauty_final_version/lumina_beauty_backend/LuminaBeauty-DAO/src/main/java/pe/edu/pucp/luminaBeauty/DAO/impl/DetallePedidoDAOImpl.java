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
                INSERT INTO detalle_pedido(
                    id_pedido,
                    id_producto,
                    nombre_producto,
                    sku_producto,
                    cantidad,
                    precio_unitario
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getPedido().getId_pedido());
            stmt.setInt(2, detalle.getProducto().getId_producto());
            stmt.setString(3, detalle.getNombre_producto());
            stmt.setString(4, detalle.getSku_producto());
            stmt.setInt(5, detalle.getCantidad());
            stmt.setBigDecimal(6, detalle.getPrecioUnitario());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId_detalle_pedido(rs.getInt(1));
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
                DELETE FROM detalle_pedido
                WHERE id_detalle_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getId_detalle_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de pedido con ID: "
                        + detalle.getId_detalle_pedido());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetallePedido buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_detalle_pedido,
                       id_pedido,
                       id_producto,
                       nombre_producto,
                       sku_producto,
                       cantidad,
                       precio_unitario
                FROM detalle_pedido
                WHERE id_detalle_pedido = ?
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
                UPDATE detalle_pedido
                SET id_pedido = ?,
                    id_producto = ?,
                    nombre_producto = ?,
                    sku_producto = ?,
                    cantidad = ?,
                    precio_unitario = ?
                WHERE id_detalle_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getPedido().getId_pedido());
            stmt.setInt(2, detalle.getProducto().getId_producto());
            stmt.setString(3, detalle.getNombre_producto());
            stmt.setString(4, detalle.getSku_producto());
            stmt.setInt(5, detalle.getCantidad());
            stmt.setBigDecimal(6, detalle.getPrecioUnitario());
            stmt.setInt(7, detalle.getId_detalle_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de pedido con ID: "
                        + detalle.getId_detalle_pedido());
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetallePedido> listarTodos() throws Exception {
        ArrayList<DetallePedido> detalles = new ArrayList<>();

        String sql = """
                SELECT id_detalle_pedido,
                       id_pedido,
                       id_producto,
                       nombre_producto,
                       sku_producto,
                       cantidad,
                       precio_unitario
                FROM detalle_pedido
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

        detalle.setId_detalle_pedido(rs.getInt("id_detalle_pedido"));
        detalle.setNombre_producto(rs.getString("nombre_producto"));
        detalle.setSku_producto(rs.getString("sku_producto"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        detalle.setPedido(pedido);

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        detalle.setProducto(producto);

        return detalle;
    }
}