package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.TarifaEnvioDAO;
import pe.edu.pucp.luminaBeauty.Model.TarifaEnvio;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class TarifaEnvioDAOImpl implements TarifaEnvioDAO {

    @Override
    public TarifaEnvio insertar(TarifaEnvio tarifa) throws Exception {
        String sql = """
                INSERT INTO tarifa_envio(
                    zona_envio,
                    costo_base,
                    monto_minimo_envio_gratis,
                    estado
                )
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getZona_envio());
            stmt.setBigDecimal(2, tarifa.getCosto_base());

            if (tarifa.getMonto_minimo_envio_gratis() != null) {
                stmt.setBigDecimal(3, tarifa.getMonto_minimo_envio_gratis());
            } else {
                stmt.setNull(3, Types.DECIMAL);
            }

            stmt.setInt(4, tarifa.getEstado());

            stmt.executeUpdate();

            return tarifa;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(TarifaEnvio tarifa) throws Exception {
        String sql = """
                UPDATE tarifa_envio
                SET estado = 0
                WHERE zona_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, tarifa.getZona_envio());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la tarifa de envío para la zona: "
                        + tarifa.getZona_envio());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TarifaEnvio buscarPorId(String zonaEnvio) throws Exception {
        String sql = """
                SELECT zona_envio,
                       costo_base,
                       monto_minimo_envio_gratis,
                       estado,
                       actualizado_en
                FROM tarifa_envio
                WHERE zona_envio = ?
                  AND estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, zonaEnvio);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTarifaEnvio(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public TarifaEnvio actualizar(TarifaEnvio tarifa) throws Exception {
        String sql = """
                UPDATE tarifa_envio
                SET costo_base = ?,
                    monto_minimo_envio_gratis = ?,
                    estado = ?
                WHERE zona_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setBigDecimal(1, tarifa.getCosto_base());

            if (tarifa.getMonto_minimo_envio_gratis() != null) {
                stmt.setBigDecimal(2, tarifa.getMonto_minimo_envio_gratis());
            } else {
                stmt.setNull(2, Types.DECIMAL);
            }

            stmt.setInt(3, tarifa.getEstado());
            stmt.setString(4, tarifa.getZona_envio());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la tarifa de envío para la zona: "
                        + tarifa.getZona_envio());
            }

            return tarifa;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<TarifaEnvio> listarTodos() throws Exception {
        ArrayList<TarifaEnvio> tarifas = new ArrayList<>();

        String sql = """
                SELECT zona_envio,
                       costo_base,
                       monto_minimo_envio_gratis,
                       estado,
                       actualizado_en
                FROM tarifa_envio
                WHERE estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tarifas.add(mapearTarifaEnvio(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tarifas;
    }

    private TarifaEnvio mapearTarifaEnvio(ResultSet rs) throws SQLException {
        TarifaEnvio tarifa = new TarifaEnvio();

        tarifa.setZona_envio(rs.getString("zona_envio"));
        tarifa.setCosto_base(rs.getBigDecimal("costo_base"));
        tarifa.setMonto_minimo_envio_gratis(rs.getBigDecimal("monto_minimo_envio_gratis"));
        tarifa.setEstado(rs.getInt("estado"));

        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaActualizacion != null) {
            tarifa.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return tarifa;
    }
}