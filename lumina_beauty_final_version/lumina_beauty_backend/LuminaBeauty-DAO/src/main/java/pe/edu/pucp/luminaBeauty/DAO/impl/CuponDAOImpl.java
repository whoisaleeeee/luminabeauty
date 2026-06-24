package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.CuponDAO;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class CuponDAOImpl implements CuponDAO {

    @Override
    public Cupon insertar(Cupon cupon) throws Exception {
        String sql = """
                INSERT INTO cupon(
                    codigo,
                    tipo_descuento,
                    valor_descuento,
                    fecha_inicio,
                    fecha_fin,
                    limite_uso,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cupon.getCodigo());
            stmt.setString(2, cupon.getTipo_descuento());
            stmt.setBigDecimal(3, cupon.getValor_descuento());
            stmt.setTimestamp(4, Timestamp.valueOf(cupon.getFecha_inicio()));
            stmt.setTimestamp(5, Timestamp.valueOf(cupon.getFecha_fin()));

            if (cupon.getLimite_uso() != null) {
                stmt.setInt(6, cupon.getLimite_uso());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, cupon.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cupon.setId_cupon(rs.getInt(1));
                }
            }

            return cupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Cupon cupon) throws Exception {
        String sql = """
                UPDATE cupon
                SET estado = 0
                WHERE id_cupon = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, cupon.getId_cupon());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el cupón con ID: " + cupon.getId_cupon());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cupon buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_cupon,
                       codigo,
                       tipo_descuento,
                       valor_descuento,
                       fecha_inicio,
                       fecha_fin,
                       limite_uso,
                       estado,
                       creado_en,
                       actualizado_en
                FROM cupon
                WHERE id_cupon = ?
                  AND estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCupon(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Cupon actualizar(Cupon cupon) throws Exception {
        String sql = """
                UPDATE cupon
                SET codigo = ?,
                    tipo_descuento = ?,
                    valor_descuento = ?,
                    fecha_inicio = ?,
                    fecha_fin = ?,
                    limite_uso = ?,
                    estado = ?
                WHERE id_cupon = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cupon.getCodigo());
            stmt.setString(2, cupon.getTipo_descuento());
            stmt.setBigDecimal(3, cupon.getValor_descuento());
            stmt.setTimestamp(4, Timestamp.valueOf(cupon.getFecha_inicio()));
            stmt.setTimestamp(5, Timestamp.valueOf(cupon.getFecha_fin()));

            if (cupon.getLimite_uso() != null) {
                stmt.setInt(6, cupon.getLimite_uso());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.setInt(7, cupon.getEstado());
            stmt.setInt(8, cupon.getId_cupon());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el cupón con ID: " + cupon.getId_cupon());
            }

            return cupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Cupon> listarTodos() throws Exception {
        ArrayList<Cupon> cupones = new ArrayList<>();

        String sql = """
                SELECT id_cupon,
                       codigo,
                       tipo_descuento,
                       valor_descuento,
                       fecha_inicio,
                       fecha_fin,
                       limite_uso,
                       estado,
                       creado_en,
                       actualizado_en
                FROM cupon
                WHERE estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cupones.add(mapearCupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cupones;
    }

    private Cupon mapearCupon(ResultSet rs) throws SQLException {
        Cupon cupon = new Cupon();

        cupon.setId_cupon(rs.getInt("id_cupon"));
        cupon.setCodigo(rs.getString("codigo"));
        cupon.setTipo_descuento(rs.getString("tipo_descuento"));
        cupon.setValor_descuento(rs.getBigDecimal("valor_descuento"));
        cupon.setEstado(rs.getInt("estado"));

        int limiteUso = rs.getInt("limite_uso");
        if (rs.wasNull()) {
            cupon.setLimite_uso(null);
        } else {
            cupon.setLimite_uso(limiteUso);
        }

        Timestamp fechaInicio = rs.getTimestamp("fecha_inicio");
        Timestamp fechaFin = rs.getTimestamp("fecha_fin");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaInicio != null) {
            cupon.setFecha_inicio(fechaInicio.toLocalDateTime());
        }

        if (fechaFin != null) {
            cupon.setFecha_fin(fechaFin.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            cupon.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            cupon.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return cupon;
    }
}