
package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MovimientoInventarioDAO;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.MovimientoInventario;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
        import java.util.ArrayList;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {

    @Override
    public MovimientoInventario insertar(MovimientoInventario movimiento) throws Exception {
        String sql = """
                INSERT INTO movimiento_inventario(
                    id_producto,
                    tipo_movimiento,
                    cantidad,
                    stock_anterior,
                    stock_posterior,
                    id_pedido,
                    id_devolucion,
                    motivo,
                    registrado_por
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, movimiento.getProducto().getId_producto());
            stmt.setString(2, movimiento.getTipo_movimiento());
            stmt.setInt(3, movimiento.getCantidad());
            stmt.setInt(4, movimiento.getStock_anterior());
            stmt.setInt(5, movimiento.getStock_posterior());

            if (movimiento.getPedido() != null) {
                stmt.setInt(6, movimiento.getPedido().getId_pedido());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (movimiento.getDevolucion() != null) {
                stmt.setInt(7, movimiento.getDevolucion().getId_devolucion());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setString(8, movimiento.getMotivo());

            if (movimiento.getRegistrado_por() != null) {
                stmt.setInt(9, movimiento.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movimiento.setId_movimiento_inventario(rs.getInt(1));
                }
            }

            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(MovimientoInventario movimiento) throws Exception {
        String sql = """
                DELETE FROM movimiento_inventario
                WHERE id_movimiento_inventario = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, movimiento.getId_movimiento_inventario());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el movimiento de inventario con ID: "
                        + movimiento.getId_movimiento_inventario());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovimientoInventario buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_movimiento_inventario,
                       id_producto,
                       tipo_movimiento,
                       cantidad,
                       stock_anterior,
                       stock_posterior,
                       id_pedido,
                       id_devolucion,
                       motivo,
                       registrado_por,
                       creado_en
                FROM movimiento_inventario
                WHERE id_movimiento_inventario = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimientoInventario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MovimientoInventario actualizar(MovimientoInventario movimiento) throws Exception {
        String sql = """
                UPDATE movimiento_inventario
                SET id_producto = ?,
                    tipo_movimiento = ?,
                    cantidad = ?,
                    stock_anterior = ?,
                    stock_posterior = ?,
                    id_pedido = ?,
                    id_devolucion = ?,
                    motivo = ?,
                    registrado_por = ?
                WHERE id_movimiento_inventario = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, movimiento.getProducto().getId_producto());
            stmt.setString(2, movimiento.getTipo_movimiento());
            stmt.setInt(3, movimiento.getCantidad());
            stmt.setInt(4, movimiento.getStock_anterior());
            stmt.setInt(5, movimiento.getStock_posterior());

            if (movimiento.getPedido() != null) {
                stmt.setInt(6, movimiento.getPedido().getId_pedido());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (movimiento.getDevolucion() != null) {
                stmt.setInt(7, movimiento.getDevolucion().getId_devolucion());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setString(8, movimiento.getMotivo());

            if (movimiento.getRegistrado_por() != null) {
                stmt.setInt(9, movimiento.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }

            stmt.setInt(10, movimiento.getId_movimiento_inventario());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el movimiento de inventario con ID: "
                        + movimiento.getId_movimiento_inventario());
            }

            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<MovimientoInventario> listarTodos() throws Exception {
        ArrayList<MovimientoInventario> movimientos = new ArrayList<>();

        String sql = """
                SELECT id_movimiento_inventario,
                       id_producto,
                       tipo_movimiento,
                       cantidad,
                       stock_anterior,
                       stock_posterior,
                       id_pedido,
                       id_devolucion,
                       motivo,
                       registrado_por,
                       creado_en
                FROM movimiento_inventario
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movimientos.add(mapearMovimientoInventario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movimientos;
    }

    private MovimientoInventario mapearMovimientoInventario(ResultSet rs) throws SQLException {
        MovimientoInventario movimiento = new MovimientoInventario();

        movimiento.setId_movimiento_inventario(rs.getInt("id_movimiento_inventario"));
        movimiento.setTipo_movimiento(rs.getString("tipo_movimiento"));
        movimiento.setCantidad(rs.getInt("cantidad"));
        movimiento.setStock_anterior(rs.getInt("stock_anterior"));
        movimiento.setStock_posterior(rs.getInt("stock_posterior"));
        movimiento.setMotivo(rs.getString("motivo"));

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        movimiento.setProducto(producto);

        int idPedido = rs.getInt("id_pedido");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setId_pedido(idPedido);
            movimiento.setPedido(pedido);
        }

        int idDevolucion = rs.getInt("id_devolucion");
        if (!rs.wasNull()) {
            Devolucion devolucion = new Devolucion();
            devolucion.setId_devolucion(idDevolucion);
            movimiento.setDevolucion(devolucion);
        }

        int idEmpleado = rs.getInt("registrado_por");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            movimiento.setRegistrado_por(empleado);
        }

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");

        if (fechaCreacion != null) {
            movimiento.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        return movimiento;
    }
}

