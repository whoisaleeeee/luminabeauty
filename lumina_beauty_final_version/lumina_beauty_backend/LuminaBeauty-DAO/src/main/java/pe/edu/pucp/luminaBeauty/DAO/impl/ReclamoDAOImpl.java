package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ReclamoDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.DetallePedido;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ReclamoDAOImpl implements ReclamoDAO {

    @Override
    public Reclamo insertar(Reclamo reclamo) throws Exception {
        String sql = """
                INSERT INTO reclamo(
                    id_cliente,
                    id_pedido,
                    id_detalle_pedido,
                    tipo,
                    asunto,
                    descripcion,
                    estado,
                    prioridad,
                    resuelto_en
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reclamo.getCliente().getId_usuario());

            if (reclamo.getPedido() != null) {
                stmt.setInt(2, reclamo.getPedido().getId_pedido());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            if (reclamo.getDetallePedido() != null) {
                stmt.setInt(3, reclamo.getDetallePedido().getId_detalle_pedido());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, reclamo.getTipo());
            stmt.setString(5, reclamo.getAsunto());
            stmt.setString(6, reclamo.getDescripcion());
            stmt.setString(7, reclamo.getEstado());
            stmt.setString(8, reclamo.getPrioridad());

            if (reclamo.getResuelto_en() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(reclamo.getResuelto_en()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reclamo.setId_reclamo(rs.getInt(1));
                }
            }

            return reclamo;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Reclamo reclamo) throws Exception {
        String sql = """
                DELETE FROM reclamo
                WHERE id_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reclamo.getId_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el reclamo con ID: " + reclamo.getId_reclamo());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reclamo buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_reclamo,
                       id_cliente,
                       id_pedido,
                       id_detalle_pedido,
                       tipo,
                       asunto,
                       descripcion,
                       estado,
                       prioridad,
                       resuelto_en,
                       creado_en,
                       actualizado_en
                FROM reclamo
                WHERE id_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReclamo(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Reclamo actualizar(Reclamo reclamo) throws Exception {
        String sql = """
                UPDATE reclamo
                SET id_cliente = ?,
                    id_pedido = ?,
                    id_detalle_pedido = ?,
                    tipo = ?,
                    asunto = ?,
                    descripcion = ?,
                    estado = ?,
                    prioridad = ?,
                    resuelto_en = ?
                WHERE id_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reclamo.getCliente().getId_usuario());

            if (reclamo.getPedido() != null) {
                stmt.setInt(2, reclamo.getPedido().getId_pedido());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            if (reclamo.getDetallePedido() != null) {
                stmt.setInt(3, reclamo.getDetallePedido().getId_detalle_pedido());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, reclamo.getTipo());
            stmt.setString(5, reclamo.getAsunto());
            stmt.setString(6, reclamo.getDescripcion());
            stmt.setString(7, reclamo.getEstado());
            stmt.setString(8, reclamo.getPrioridad());

            if (reclamo.getResuelto_en() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(reclamo.getResuelto_en()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            stmt.setInt(10, reclamo.getId_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el reclamo con ID: " + reclamo.getId_reclamo());
            }

            return reclamo;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Reclamo> listarTodos() throws Exception {
        ArrayList<Reclamo> reclamos = new ArrayList<>();

        String sql = """
                SELECT id_reclamo,
                       id_cliente,
                       id_pedido,
                       id_detalle_pedido,
                       tipo,
                       asunto,
                       descripcion,
                       estado,
                       prioridad,
                       resuelto_en,
                       creado_en,
                       actualizado_en
                FROM reclamo
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reclamos.add(mapearReclamo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reclamos;
    }

    private Reclamo mapearReclamo(ResultSet rs) throws SQLException {
        Reclamo reclamo = new Reclamo();

        reclamo.setId_reclamo(rs.getInt("id_reclamo"));
        reclamo.setTipo(rs.getString("tipo"));
        reclamo.setAsunto(rs.getString("asunto"));
        reclamo.setDescripcion(rs.getString("descripcion"));
        reclamo.setEstado(rs.getString("estado"));
        reclamo.setPrioridad(rs.getString("prioridad"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        reclamo.setCliente(cliente);

        int idPedido = rs.getInt("id_pedido");
        if (!rs.wasNull()) {
            Pedido pedido = new Pedido();
            pedido.setId_pedido(idPedido);
            reclamo.setPedido(pedido);
        }

        int idDetallePedido = rs.getInt("id_detalle_pedido");
        if (!rs.wasNull()) {
            DetallePedido detallePedido = new DetallePedido();
            detallePedido.setId_detalle_pedido(idDetallePedido);
            reclamo.setDetallePedido(detallePedido);
        }

        Timestamp resueltoEn = rs.getTimestamp("resuelto_en");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (resueltoEn != null) {
            reclamo.setResuelto_en(resueltoEn.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            reclamo.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            reclamo.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return reclamo;
    }
}