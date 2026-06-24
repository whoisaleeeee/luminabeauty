package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MovimientoPuntosFidelidadDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.MovimientoPuntosFidelidad;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class MovimientoPuntosFidelidadDAOImpl implements MovimientoPuntosFidelidadDAO {

    @Override
    public MovimientoPuntosFidelidad insertar(MovimientoPuntosFidelidad movimiento) throws Exception {
        String sql = """
                INSERT INTO movimiento_puntos_fidelidad(
                    id_cliente,
                    tipo_movimiento,
                    puntos,
                    saldo_anterior,
                    saldo_posterior,
                    id_pedido,
                    motivo,
                    registrado_por
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, movimiento.getCliente().getId_usuario());
            stmt.setString(2, movimiento.getTipo_movimiento());
            stmt.setInt(3, movimiento.getPuntos());
            stmt.setInt(4, movimiento.getSaldo_anterior());
            stmt.setInt(5, movimiento.getSaldo_posterior());

            if (movimiento.getPedido() != null) {
                stmt.setInt(6, movimiento.getPedido().getId_pedido());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setString(7, movimiento.getMotivo());

            if (movimiento.getRegistrado_por() != null) {
                stmt.setInt(8, movimiento.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movimiento.setId_movimiento_puntos(rs.getInt(1));
                }
            }

            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(MovimientoPuntosFidelidad movimiento) throws Exception {
        String sql = """
                DELETE FROM movimiento_puntos_fidelidad
                WHERE id_movimiento_puntos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, movimiento.getId_movimiento_puntos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el movimiento de puntos con ID: "
                        + movimiento.getId_movimiento_puntos());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovimientoPuntosFidelidad buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_movimiento_puntos,
                       id_cliente,
                       tipo_movimiento,
                       puntos,
                       saldo_anterior,
                       saldo_posterior,
                       id_pedido,
                       motivo,
                       registrado_por,
                       creado_en
                FROM movimiento_puntos_fidelidad
                WHERE id_movimiento_puntos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimiento(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MovimientoPuntosFidelidad actualizar(MovimientoPuntosFidelidad movimiento) throws Exception {
        String sql = """
                UPDATE movimiento_puntos_fidelidad
                SET id_cliente = ?,
                    tipo_movimiento = ?,
                    puntos = ?,
                    saldo_anterior = ?,
                    saldo_posterior = ?,
                    id_pedido = ?,
                    motivo = ?,
                    registrado_por = ?
                WHERE id_movimiento_puntos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, movimiento.getCliente().getId_usuario());
            stmt.setString(2, movimiento.getTipo_movimiento());
            stmt.setInt(3, movimiento.getPuntos());
            stmt.setInt(4, movimiento.getSaldo_anterior());
            stmt.setInt(5, movimiento.getSaldo_posterior());

            if (movimiento.getPedido() != null) {
                stmt.setInt(6, movimiento.getPedido().getId_pedido());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setString(7, movimiento.getMotivo());

            if (movimiento.getRegistrado_por() != null) {
                stmt.setInt(8, movimiento.getRegistrado_por().getId_usuario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            stmt.setInt(9, movimiento.getId_movimiento_puntos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el movimiento de puntos con ID: "
                        + movimiento.getId_movimiento_puntos());
            }

            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<MovimientoPuntosFidelidad> listarTodos() throws Exception {
        ArrayList<MovimientoPuntosFidelidad> movimientos = new ArrayList<>();

        String sql = """
                SELECT id_movimiento_puntos,
                       id_cliente,
                       tipo_movimiento,
                       puntos,
                       saldo_anterior,
                       saldo_posterior,
                       id_pedido,
                       motivo,
                       registrado_por,
                       creado_en
                FROM movimiento_puntos_fidelidad
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return movimientos;
    }

    private MovimientoPuntosFidelidad mapearMovimiento(ResultSet rs) throws SQLException {
        MovimientoPuntosFidelidad movimiento = new MovimientoPuntosFidelidad();

        movimiento.setId_movimiento_puntos(rs.getInt("id_movimiento_puntos"));
        movimiento.setTipo_movimiento(rs.getString("tipo_movimiento"));
        movimiento.setPuntos(rs.getInt("puntos"));
        movimiento.setSaldo_anterior(rs.getInt("saldo_anterior"));
        movimiento.setSaldo_posterior(rs.getInt("saldo_posterior"));
        movimiento.setMotivo(rs.getString("motivo"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        movimiento.setCliente(cliente);

        int idPedido = rs.getInt("id_pedido");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setId_pedido(idPedido);
            movimiento.setPedido(pedido);
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