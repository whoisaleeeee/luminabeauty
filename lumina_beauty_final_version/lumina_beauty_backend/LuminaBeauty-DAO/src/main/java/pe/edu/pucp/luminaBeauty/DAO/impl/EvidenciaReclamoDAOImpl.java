

package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EvidenciaReclamoDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.Model.EvidenciaReclamo;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
        import java.util.ArrayList;

public class EvidenciaReclamoDAOImpl implements EvidenciaReclamoDAO {

    @Override
    public EvidenciaReclamo insertar(EvidenciaReclamo evidencia) throws Exception {
        String sql = """
                INSERT INTO evidencia_reclamo(
                    id_reclamo,
                    url_archivo,
                    tipo_archivo,
                    descripcion,
                    subido_por_cliente,
                    subido_por_empleado
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, evidencia.getReclamo().getId_reclamo());
            stmt.setString(2, evidencia.getUrl_archivo());
            stmt.setString(3, evidencia.getTipo_archivo());
            stmt.setString(4, evidencia.getDescripcion());

            if (evidencia.getSubido_por_cliente() != null) {
                stmt.setInt(5, evidencia.getSubido_por_cliente().getId_usuario());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (evidencia.getSubido_por_empleado() != null) {
                stmt.setInt(6, evidencia.getSubido_por_empleado().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    evidencia.setId_evidencia_reclamo(rs.getInt(1));
                }
            }

            return evidencia;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(EvidenciaReclamo evidencia) throws Exception {
        String sql = """
                DELETE FROM evidencia_reclamo
                WHERE id_evidencia_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, evidencia.getId_evidencia_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la evidencia con ID: "
                        + evidencia.getId_evidencia_reclamo());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EvidenciaReclamo buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_evidencia_reclamo,
                       id_reclamo,
                       url_archivo,
                       tipo_archivo,
                       descripcion,
                       subido_por_cliente,
                       subido_por_empleado,
                       creado_en
                FROM evidencia_reclamo
                WHERE id_evidencia_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEvidenciaReclamo(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public EvidenciaReclamo actualizar(EvidenciaReclamo evidencia) throws Exception {
        String sql = """
                UPDATE evidencia_reclamo
                SET id_reclamo = ?,
                    url_archivo = ?,
                    tipo_archivo = ?,
                    descripcion = ?,
                    subido_por_cliente = ?,
                    subido_por_empleado = ?
                WHERE id_evidencia_reclamo = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, evidencia.getReclamo().getId_reclamo());
            stmt.setString(2, evidencia.getUrl_archivo());
            stmt.setString(3, evidencia.getTipo_archivo());
            stmt.setString(4, evidencia.getDescripcion());

            if (evidencia.getSubido_por_cliente() != null) {
                stmt.setInt(5, evidencia.getSubido_por_cliente().getId_usuario());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (evidencia.getSubido_por_empleado() != null) {
                stmt.setInt(6, evidencia.getSubido_por_empleado().getId_usuario());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, evidencia.getId_evidencia_reclamo());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la evidencia con ID: "
                        + evidencia.getId_evidencia_reclamo());
            }

            return evidencia;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<EvidenciaReclamo> listarTodos() throws Exception {
        ArrayList<EvidenciaReclamo> evidencias = new ArrayList<>();

        String sql = """
                SELECT id_evidencia_reclamo,
                       id_reclamo,
                       url_archivo,
                       tipo_archivo,
                       descripcion,
                       subido_por_cliente,
                       subido_por_empleado,
                       creado_en
                FROM evidencia_reclamo
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                evidencias.add(mapearEvidenciaReclamo(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return evidencias;
    }

    private EvidenciaReclamo mapearEvidenciaReclamo(ResultSet rs) throws SQLException {
        EvidenciaReclamo evidencia = new EvidenciaReclamo();

        evidencia.setId_evidencia_reclamo(rs.getInt("id_evidencia_reclamo"));
        evidencia.setUrl_archivo(rs.getString("url_archivo"));
        evidencia.setTipo_archivo(rs.getString("tipo_archivo"));
        evidencia.setDescripcion(rs.getString("descripcion"));

        Reclamo reclamo = new Reclamo();
        reclamo.setId_reclamo(rs.getInt("id_reclamo"));
        evidencia.setReclamo(reclamo);

        int idCliente = rs.getInt("subido_por_cliente");
        if (!rs.wasNull()) {
            Cliente cliente = new Cliente();
            cliente.setId_usuario(idCliente);
            evidencia.setSubido_por_cliente(cliente);
        }

        int idEmpleado = rs.getInt("subido_por_empleado");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            evidencia.setSubido_por_empleado(empleado);
        }

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");

        if (fechaCreacion != null) {
            evidencia.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        return evidencia;
    }
}
