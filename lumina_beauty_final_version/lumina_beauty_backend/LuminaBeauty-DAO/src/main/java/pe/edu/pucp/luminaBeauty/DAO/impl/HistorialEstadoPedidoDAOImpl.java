package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.HistorialEstadoPedidoDAO;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.HistorialEstadoPedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class HistorialEstadoPedidoDAOImpl implements HistorialEstadoPedidoDAO {

    @Override
    public HistorialEstadoPedido insertar(HistorialEstadoPedido historial) throws Exception {
        String sql = """
                INSERT INTO historial_estado_pedido(
                    id_pedido,
                    estado_anterior,
                    estado_nuevo,
                    comentario,
                    registrado_por
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, historial.getPedido().getId_pedido());

            if (historial.getEstado_anterior() != null) {
                stmt.setString(2, historial.getEstado_anterior());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setString(3, historial.getEstado_nuevo());
            stmt.setString(4, historial.getComentario());

            if (historial.getRegistrado_por() != null) {
                stmt.setInt(5, historial.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historial.setId_historial_estado_pedido(rs.getInt(1));
                }
            }

            return historial;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(HistorialEstadoPedido historial) throws Exception {
        String sql = """
                DELETE FROM historial_estado_pedido
                WHERE id_historial_estado_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, historial.getId_historial_estado_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el historial con ID: "
                        + historial.getId_historial_estado_pedido());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HistorialEstadoPedido buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_historial_estado_pedido,
                       id_pedido,
                       estado_anterior,
                       estado_nuevo,
                       comentario,
                       registrado_por,
                       creado_en
                FROM historial_estado_pedido
                WHERE id_historial_estado_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearHistorial(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public HistorialEstadoPedido actualizar(HistorialEstadoPedido historial) throws Exception {
        String sql = """
                UPDATE historial_estado_pedido
                SET id_pedido = ?,
                    estado_anterior = ?,
                    estado_nuevo = ?,
                    comentario = ?,
                    registrado_por = ?
                WHERE id_historial_estado_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, historial.getPedido().getId_pedido());

            if (historial.getEstado_anterior() != null) {
                stmt.setString(2, historial.getEstado_anterior());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setString(3, historial.getEstado_nuevo());
            stmt.setString(4, historial.getComentario());

            if (historial.getRegistrado_por() != null) {
                stmt.setInt(5, historial.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, historial.getId_historial_estado_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el historial con ID: "
                        + historial.getId_historial_estado_pedido());
            }

            return historial;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<HistorialEstadoPedido> listarTodos() throws Exception {
        ArrayList<HistorialEstadoPedido> historiales = new ArrayList<>();

        String sql = """
                SELECT id_historial_estado_pedido,
                       id_pedido,
                       estado_anterior,
                       estado_nuevo,
                       comentario,
                       registrado_por,
                       creado_en
                FROM historial_estado_pedido
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                historiales.add(mapearHistorial(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return historiales;
    }

    private HistorialEstadoPedido mapearHistorial(ResultSet rs) throws SQLException {
        HistorialEstadoPedido historial = new HistorialEstadoPedido();

        historial.setId_historial_estado_pedido(rs.getInt("id_historial_estado_pedido"));
        historial.setEstado_anterior(rs.getString("estado_anterior"));
        historial.setEstado_nuevo(rs.getString("estado_nuevo"));
        historial.setComentario(rs.getString("comentario"));

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        historial.setPedido(pedido);

        int idEmpleado = rs.getInt("registrado_por");

        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            historial.setRegistrado_por(empleado);
        }

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");

        if (fechaCreacion != null) {
            historial.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        return historial;
    }
}