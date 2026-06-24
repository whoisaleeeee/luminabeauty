
package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DevolucionDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
        import java.util.ArrayList;

public class DevolucionDAOImpl implements DevolucionDAO {

    @Override
    public Devolucion insertar(Devolucion devolucion) throws Exception {
        String sql = """
                INSERT INTO devolucion(
                    id_cliente,
                    id_pedido,
                    id_reclamo,
                    motivo,
                    detalle_motivo,
                    estado,
                    solicitado_en,
                    aprobado_por,
                    aprobado_en,
                    recibido_por,
                    recibido_en
                )
                VALUES (?, ?, ?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP), ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, devolucion.getCliente().getId_usuario());
            stmt.setInt(2, devolucion.getPedido().getId_pedido());

            if (devolucion.getReclamo() != null) {
                stmt.setInt(3, devolucion.getReclamo().getId_reclamo());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, devolucion.getMotivo());
            stmt.setString(5, devolucion.getDetalle_motivo());
            stmt.setString(6, devolucion.getEstado());

            if (devolucion.getSolicitado_en() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(devolucion.getSolicitado_en()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            if (devolucion.getAprobado_por() != null) {
                stmt.setInt(8, devolucion.getAprobado_por().getId_usuario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (devolucion.getAprobado_en() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(devolucion.getAprobado_en()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            if (devolucion.getRecibido_por() != null) {
                stmt.setInt(10, devolucion.getRecibido_por().getId_usuario());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            if (devolucion.getRecibido_en() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(devolucion.getRecibido_en()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    devolucion.setId_devolucion(rs.getInt(1));
                }
            }

            return devolucion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Devolucion devolucion) throws Exception {
        String sql = """
                DELETE FROM devolucion
                WHERE id_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, devolucion.getId_devolucion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la devolución con ID: "
                        + devolucion.getId_devolucion());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Devolucion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_devolucion,
                       id_cliente,
                       id_pedido,
                       id_reclamo,
                       motivo,
                       detalle_motivo,
                       estado,
                       solicitado_en,
                       aprobado_por,
                       aprobado_en,
                       recibido_por,
                       recibido_en,
                       creado_en,
                       actualizado_en
                FROM devolucion
                WHERE id_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDevolucion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Devolucion actualizar(Devolucion devolucion) throws Exception {
        String sql = """
                UPDATE devolucion
                SET id_cliente = ?,
                    id_pedido = ?,
                    id_reclamo = ?,
                    motivo = ?,
                    detalle_motivo = ?,
                    estado = ?,
                    solicitado_en = ?,
                    aprobado_por = ?,
                    aprobado_en = ?,
                    recibido_por = ?,
                    recibido_en = ?
                WHERE id_devolucion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, devolucion.getCliente().getId_usuario());
            stmt.setInt(2, devolucion.getPedido().getId_pedido());

            if (devolucion.getReclamo() != null) {
                stmt.setInt(3, devolucion.getReclamo().getId_reclamo());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, devolucion.getMotivo());
            stmt.setString(5, devolucion.getDetalle_motivo());
            stmt.setString(6, devolucion.getEstado());

            if (devolucion.getSolicitado_en() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(devolucion.getSolicitado_en()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            if (devolucion.getAprobado_por() != null) {
                stmt.setInt(8, devolucion.getAprobado_por().getId_usuario());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (devolucion.getAprobado_en() != null) {
                stmt.setTimestamp(9, Timestamp.valueOf(devolucion.getAprobado_en()));
            } else {
                stmt.setNull(9, Types.TIMESTAMP);
            }

            if (devolucion.getRecibido_por() != null) {
                stmt.setInt(10, devolucion.getRecibido_por().getId_usuario());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            if (devolucion.getRecibido_en() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(devolucion.getRecibido_en()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            stmt.setInt(12, devolucion.getId_devolucion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la devolución con ID: "
                        + devolucion.getId_devolucion());
            }

            return devolucion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Devolucion> listarTodos() throws Exception {
        ArrayList<Devolucion> devoluciones = new ArrayList<>();

        String sql = """
                SELECT id_devolucion,
                       id_cliente,
                       id_pedido,
                       id_reclamo,
                       motivo,
                       detalle_motivo,
                       estado,
                       solicitado_en,
                       aprobado_por,
                       aprobado_en,
                       recibido_por,
                       recibido_en,
                       creado_en,
                       actualizado_en
                FROM devolucion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                devoluciones.add(mapearDevolucion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return devoluciones;
    }

    private Devolucion mapearDevolucion(ResultSet rs) throws SQLException {
        Devolucion devolucion = new Devolucion();

        devolucion.setId_devolucion(rs.getInt("id_devolucion"));
        devolucion.setMotivo(rs.getString("motivo"));
        devolucion.setDetalle_motivo(rs.getString("detalle_motivo"));
        devolucion.setEstado(rs.getString("estado"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        devolucion.setCliente(cliente);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        devolucion.setPedido(pedido);

        int idReclamo = rs.getInt("id_reclamo");
        if (!rs.wasNull()) {
            Reclamo reclamo = new Reclamo();
            reclamo.setId_reclamo(idReclamo);
            devolucion.setReclamo(reclamo);
        }

        int idAprobadoPor = rs.getInt("aprobado_por");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idAprobadoPor);
            devolucion.setAprobado_por(empleado);
        }

        int idRecibidoPor = rs.getInt("recibido_por");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idRecibidoPor);
            devolucion.setRecibido_por(empleado);
        }

        Timestamp solicitadoEn = rs.getTimestamp("solicitado_en");
        Timestamp aprobadoEn = rs.getTimestamp("aprobado_en");
        Timestamp recibidoEn = rs.getTimestamp("recibido_en");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (solicitadoEn != null) {
            devolucion.setSolicitado_en(solicitadoEn.toLocalDateTime());
        }

        if (aprobadoEn != null) {
            devolucion.setAprobado_en(aprobadoEn.toLocalDateTime());
        }

        if (recibidoEn != null) {
            devolucion.setRecibido_en(recibidoEn.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            devolucion.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            devolucion.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return devolucion;
    }
}

