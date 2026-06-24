package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.SeguimientoReclamoDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.Model.SeguimientoReclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class SeguimientoReclamoDAOImpl implements SeguimientoReclamoDAO {

    @Override
    public SeguimientoReclamo insertar(SeguimientoReclamo seguimiento) throws Exception {
        String sql = """
                INSERT INTO seguimiento_reclamo(
                    id_reclamo,
                    tipo,
                    mensaje,
                    estado_anterior,
                    estado_nuevo,
                    registrado_por_cliente,
                    registrado_por_empleado
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, seguimiento.getReclamo().getId_reclamo());
            stmt.setString(2, seguimiento.getTipo());
            stmt.setString(3, seguimiento.getMensaje());

            if (seguimiento.getEstado_anterior() != null) {
                stmt.setString(4, seguimiento.getEstado_anterior());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            if (seguimiento.getEstado_nuevo() != null) {
                stmt.setString(5, seguimiento.getEstado_nuevo());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (seguimiento.getRegistrado_por_cliente() != null) {
                stmt.setInt(6, seguimiento.getRegistrado_por_cliente().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (seguimiento.getRegistrado_por_empleado() != null) {
                stmt.setInt(7, seguimiento.getRegistrado_por_empleado().getId_usuario());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    seguimiento.setId_seguimiento_reclamo(rs.getInt(1));
                }
            }

            return seguimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(SeguimientoReclamo seguimiento) throws Exception {
        String sql = """
                DELETE FROM seguimiento_reclamo
                WHERE id_seguimiento_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, seguimiento.getId_seguimiento_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el seguimiento de reclamo con ID: "
                        + seguimiento.getId_seguimiento_reclamo());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SeguimientoReclamo buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_seguimiento_reclamo,
                       id_reclamo,
                       tipo,
                       mensaje,
                       estado_anterior,
                       estado_nuevo,
                       registrado_por_cliente,
                       registrado_por_empleado,
                       creado_en
                FROM seguimiento_reclamo
                WHERE id_seguimiento_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearSeguimiento(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public SeguimientoReclamo actualizar(SeguimientoReclamo seguimiento) throws Exception {
        String sql = """
                UPDATE seguimiento_reclamo
                SET id_reclamo = ?,
                    tipo = ?,
                    mensaje = ?,
                    estado_anterior = ?,
                    estado_nuevo = ?,
                    registrado_por_cliente = ?,
                    registrado_por_empleado = ?
                WHERE id_seguimiento_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, seguimiento.getReclamo().getId_reclamo());
            stmt.setString(2, seguimiento.getTipo());
            stmt.setString(3, seguimiento.getMensaje());

            if (seguimiento.getEstado_anterior() != null) {
                stmt.setString(4, seguimiento.getEstado_anterior());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }

            if (seguimiento.getEstado_nuevo() != null) {
                stmt.setString(5, seguimiento.getEstado_nuevo());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }

            if (seguimiento.getRegistrado_por_cliente() != null) {
                stmt.setInt(6, seguimiento.getRegistrado_por_cliente().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (seguimiento.getRegistrado_por_empleado() != null) {
                stmt.setInt(7, seguimiento.getRegistrado_por_empleado().getId_usuario());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            stmt.setInt(8, seguimiento.getId_seguimiento_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el seguimiento de reclamo con ID: "
                        + seguimiento.getId_seguimiento_reclamo());
            }

            return seguimiento;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<SeguimientoReclamo> listarTodos() throws Exception {
        ArrayList<SeguimientoReclamo> seguimientos = new ArrayList<>();

        String sql = """
                SELECT id_seguimiento_reclamo,
                       id_reclamo,
                       tipo,
                       mensaje,
                       estado_anterior,
                       estado_nuevo,
                       registrado_por_cliente,
                       registrado_por_empleado,
                       creado_en
                FROM seguimiento_reclamo
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                seguimientos.add(mapearSeguimiento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return seguimientos;
    }

    private SeguimientoReclamo mapearSeguimiento(ResultSet rs) throws SQLException {
        SeguimientoReclamo seguimiento = new SeguimientoReclamo();

        seguimiento.setId_seguimiento_reclamo(rs.getInt("id_seguimiento_reclamo"));
        seguimiento.setTipo(rs.getString("tipo"));
        seguimiento.setMensaje(rs.getString("mensaje"));
        seguimiento.setEstado_anterior(rs.getString("estado_anterior"));
        seguimiento.setEstado_nuevo(rs.getString("estado_nuevo"));

        Reclamo reclamo = new Reclamo();
        reclamo.setId_reclamo(rs.getInt("id_reclamo"));
        seguimiento.setReclamo(reclamo);

        int idCliente = rs.getInt("registrado_por_cliente");
        if (!rs.wasNull()) {
            Cliente cliente = new Cliente();
            cliente.setId_usuario(idCliente);
            seguimiento.setRegistrado_por_cliente(cliente);
        }

        int idEmpleado = rs.getInt("registrado_por_empleado");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            seguimiento.setRegistrado_por_empleado(empleado);
        }

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");

        if (fechaCreacion != null) {
            seguimiento.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        return seguimiento;
    }
}