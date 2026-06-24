
package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DetalleDevolucionDAO;
import pe.edu.pucp.luminaBeauty.Model.DetalleDevolucion;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
        import java.util.ArrayList;

public class DetalleDevolucionDAOImpl implements DetalleDevolucionDAO {

    @Override
    public DetalleDevolucion insertar(DetalleDevolucion detalle) throws Exception {
        String sql = """
                INSERT INTO detalle_devolucion(
                    id_devolucion,
                    id_detalle_pedido,
                    cantidad_solicitada,
                    cantidad_recibida,
                    condicion_producto,
                    observacion
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getDevolucion().getId_devolucion());
            stmt.setInt(2, detalle.getDetallePedido().getId_detalle_pedido());
            stmt.setInt(3, detalle.getCantidad_solicitada());

            if (detalle.getCantidad_recibida() != null) {
                stmt.setInt(4, detalle.getCantidad_recibida());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, detalle.getCondicion_producto());
            stmt.setString(6, detalle.getObservacion());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId_detalle_devolucion(rs.getInt(1));
                }
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(DetalleDevolucion detalle) throws Exception {
        String sql = """
                DELETE FROM detalle_devolucion
                WHERE id_detalle_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getId_detalle_devolucion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de devolución con ID: "
                        + detalle.getId_detalle_devolucion());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetalleDevolucion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_detalle_devolucion,
                       id_devolucion,
                       id_detalle_pedido,
                       cantidad_solicitada,
                       cantidad_recibida,
                       condicion_producto,
                       observacion
                FROM detalle_devolucion
                WHERE id_detalle_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalleDevolucion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public DetalleDevolucion actualizar(DetalleDevolucion detalle) throws Exception {
        String sql = """
                UPDATE detalle_devolucion
                SET id_devolucion = ?,
                    id_detalle_pedido = ?,
                    cantidad_solicitada = ?,
                    cantidad_recibida = ?,
                    condicion_producto = ?,
                    observacion = ?
                WHERE id_detalle_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getDevolucion().getId_devolucion());
            stmt.setInt(2, detalle.getDetallePedido().getId_detalle_pedido());
            stmt.setInt(3, detalle.getCantidad_solicitada());

            if (detalle.getCantidad_recibida() != null) {
                stmt.setInt(4, detalle.getCantidad_recibida());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }

            stmt.setString(5, detalle.getCondicion_producto());
            stmt.setString(6, detalle.getObservacion());
            stmt.setInt(7, detalle.getId_detalle_devolucion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de devolución con ID: "
                        + detalle.getId_detalle_devolucion());
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetalleDevolucion> listarTodos() throws Exception {
        ArrayList<DetalleDevolucion> detalles = new ArrayList<>();

        String sql = """
                SELECT id_detalle_devolucion,
                       id_devolucion,
                       id_detalle_pedido,
                       cantidad_solicitada,
                       cantidad_recibida,
                       condicion_producto,
                       observacion
                FROM detalle_devolucion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetalleDevolucion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return detalles;
    }

    private DetalleDevolucion mapearDetalleDevolucion(ResultSet rs) throws SQLException {
        DetalleDevolucion detalle = new DetalleDevolucion();

        detalle.setId_detalle_devolucion(rs.getInt("id_detalle_devolucion"));
        detalle.setCantidad_solicitada(rs.getInt("cantidad_solicitada"));

        int cantidadRecibida = rs.getInt("cantidad_recibida");
        if (!rs.wasNull()) {
            detalle.setCantidad_recibida(cantidadRecibida);
        }

        detalle.setCondicion_producto(rs.getString("condicion_producto"));
        detalle.setObservacion(rs.getString("observacion"));

        Devolucion devolucion = new Devolucion();
        devolucion.setId_devolucion(rs.getInt("id_devolucion"));
        detalle.setDevolucion(devolucion);

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setId_detalle_pedido(rs.getInt("id_detalle_pedido"));
        detalle.setDetallePedido(detallePedido);

        return detalle;
    }
}

