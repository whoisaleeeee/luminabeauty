
package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ReembolsoDAO;
import pe.edu.pucp.luminaBeauty.Model.Devolucion;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Reembolso;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
        import java.util.ArrayList;

public class ReembolsoDAOImpl implements ReembolsoDAO {

    @Override
    public Reembolso insertar(Reembolso reembolso) throws Exception {
        String sql = """
                INSERT INTO reembolso(
                    id_pago,
                    id_devolucion,
                    monto,
                    estado,
                    referencia_transaccion,
                    procesado_por,
                    procesado_en,
                    motivo
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, reembolso.getPago().getId_pago());

            if (reembolso.getDevolucion() != null) {
                stmt.setInt(2, reembolso.getDevolucion().getId_devolucion());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setBigDecimal(3, reembolso.getMonto());
            stmt.setString(4, reembolso.getEstado());
            stmt.setString(5, reembolso.getReferencia_transaccion());

            if (reembolso.getProcesado_por() != null) {
                stmt.setInt(6, reembolso.getProcesado_por().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (reembolso.getProcesado_en() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(reembolso.getProcesado_en()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setString(8, reembolso.getMotivo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reembolso.setId_reembolso(rs.getInt(1));
                }
            }

            return reembolso;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Reembolso reembolso) throws Exception {
        String sql = """
                DELETE FROM reembolso
                WHERE id_reembolso = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reembolso.getId_reembolso());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el reembolso con ID: "
                        + reembolso.getId_reembolso());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reembolso buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_reembolso,
                       id_pago,
                       id_devolucion,
                       monto,
                       estado,
                       referencia_transaccion,
                       procesado_por,
                       procesado_en,
                       motivo,
                       creado_en,
                       actualizado_en
                FROM reembolso
                WHERE id_reembolso = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearReembolso(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Reembolso actualizar(Reembolso reembolso) throws Exception {
        String sql = """
                UPDATE reembolso
                SET id_pago = ?,
                    id_devolucion = ?,
                    monto = ?,
                    estado = ?,
                    referencia_transaccion = ?,
                    procesado_por = ?,
                    procesado_en = ?,
                    motivo = ?
                WHERE id_reembolso = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, reembolso.getPago().getId_pago());

            if (reembolso.getDevolucion() != null) {
                stmt.setInt(2, reembolso.getDevolucion().getId_devolucion());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setBigDecimal(3, reembolso.getMonto());
            stmt.setString(4, reembolso.getEstado());
            stmt.setString(5, reembolso.getReferencia_transaccion());

            if (reembolso.getProcesado_por() != null) {
                stmt.setInt(6, reembolso.getProcesado_por().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            if (reembolso.getProcesado_en() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(reembolso.getProcesado_en()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setString(8, reembolso.getMotivo());
            stmt.setInt(9, reembolso.getId_reembolso());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el reembolso con ID: "
                        + reembolso.getId_reembolso());
            }

            return reembolso;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Reembolso> listarTodos() throws Exception {
        ArrayList<Reembolso> reembolsos = new ArrayList<>();

        String sql = """
                SELECT id_reembolso,
                       id_pago,
                       id_devolucion,
                       monto,
                       estado,
                       referencia_transaccion,
                       procesado_por,
                       procesado_en,
                       motivo,
                       creado_en,
                       actualizado_en
                FROM reembolso
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reembolsos.add(mapearReembolso(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reembolsos;
    }

    private Reembolso mapearReembolso(ResultSet rs) throws SQLException {
        Reembolso reembolso = new Reembolso();

        reembolso.setId_reembolso(rs.getInt("id_reembolso"));
        reembolso.setMonto(rs.getBigDecimal("monto"));
        reembolso.setEstado(rs.getString("estado"));
        reembolso.setReferencia_transaccion(rs.getString("referencia_transaccion"));
        reembolso.setMotivo(rs.getString("motivo"));

        Pago pago = new Pago();
        pago.setId_pago(rs.getInt("id_pago"));
        reembolso.setPago(pago);

        int idDevolucion = rs.getInt("id_devolucion");
        if (!rs.wasNull()) {
            Devolucion devolucion = new Devolucion();
            devolucion.setId_devolucion(idDevolucion);
            reembolso.setDevolucion(devolucion);
        }

        int idEmpleado = rs.getInt("procesado_por");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            reembolso.setProcesado_por(empleado);
        }

        Timestamp procesadoEn = rs.getTimestamp("procesado_en");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (procesadoEn != null) {
            reembolso.setProcesado_en(procesadoEn.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            reembolso.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            reembolso.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return reembolso;
    }
}
