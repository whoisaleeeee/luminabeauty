package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class EnvioDAOImpl implements EnvioDAO {

    @Override
    public Envio insertar(Envio envio) throws Exception {
        String sql = """
                INSERT INTO envio(
                    id_pedido,
                    zona_envio,
                    estado,
                    numero_seguimiento,
                    direccion_envio,
                    ciudad_envio,
                    pais_envio,
                    referencia_envio,
                    codigo_postal_envio,
                    fecha_envio,
                    fecha_entrega_estimada,
                    fecha_entrega_real
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, envio.getPedido().getId_pedido());
            stmt.setString(2, envio.getZona_envio());
            stmt.setString(3, envio.getEstado());
            stmt.setString(4, envio.getNumero_seguimiento());
            stmt.setString(5, envio.getDireccion_envio());
            stmt.setString(6, envio.getCiudad_envio());
            stmt.setString(7, envio.getPais_envio());
            stmt.setString(8, envio.getReferencia_envio());
            stmt.setString(9, envio.getCodigo_postal_envio());

            if (envio.getFecha_envio() != null) {
                stmt.setTimestamp(10, Timestamp.valueOf(envio.getFecha_envio()));
            } else {
                stmt.setNull(10, Types.TIMESTAMP);
            }

            if (envio.getFecha_entrega_estimada() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(envio.getFecha_entrega_estimada()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            if (envio.getFecha_entrega_real() != null) {
                stmt.setTimestamp(12, Timestamp.valueOf(envio.getFecha_entrega_real()));
            } else {
                stmt.setNull(12, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    envio.setId_envio(rs.getInt(1));
                }
            }

            return envio;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Envio envio) throws Exception {
        String sql = """
                DELETE FROM envio
                WHERE id_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, envio.getId_envio());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el envío con ID: " + envio.getId_envio());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Envio buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_envio,
                       id_pedido,
                       zona_envio,
                       estado,
                       numero_seguimiento,
                       direccion_envio,
                       ciudad_envio,
                       pais_envio,
                       referencia_envio,
                       codigo_postal_envio,
                       fecha_envio,
                       fecha_entrega_estimada,
                       fecha_entrega_real,
                       creado_en,
                       actualizado_en
                FROM envio
                WHERE id_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Envio actualizar(Envio envio) throws Exception {
        String sql = """
                UPDATE envio
                SET id_pedido = ?,
                    zona_envio = ?,
                    estado = ?,
                    numero_seguimiento = ?,
                    direccion_envio = ?,
                    ciudad_envio = ?,
                    pais_envio = ?,
                    referencia_envio = ?,
                    codigo_postal_envio = ?,
                    fecha_envio = ?,
                    fecha_entrega_estimada = ?,
                    fecha_entrega_real = ?
                WHERE id_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, envio.getPedido().getId_pedido());
            stmt.setString(2, envio.getZona_envio());
            stmt.setString(3, envio.getEstado());
            stmt.setString(4, envio.getNumero_seguimiento());
            stmt.setString(5, envio.getDireccion_envio());
            stmt.setString(6, envio.getCiudad_envio());
            stmt.setString(7, envio.getPais_envio());
            stmt.setString(8, envio.getReferencia_envio());
            stmt.setString(9, envio.getCodigo_postal_envio());

            if (envio.getFecha_envio() != null) {
                stmt.setTimestamp(10, Timestamp.valueOf(envio.getFecha_envio()));
            } else {
                stmt.setNull(10, Types.TIMESTAMP);
            }

            if (envio.getFecha_entrega_estimada() != null) {
                stmt.setTimestamp(11, Timestamp.valueOf(envio.getFecha_entrega_estimada()));
            } else {
                stmt.setNull(11, Types.TIMESTAMP);
            }

            if (envio.getFecha_entrega_real() != null) {
                stmt.setTimestamp(12, Timestamp.valueOf(envio.getFecha_entrega_real()));
            } else {
                stmt.setNull(12, Types.TIMESTAMP);
            }

            stmt.setInt(13, envio.getId_envio());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el envío con ID: " + envio.getId_envio());
            }

            return envio;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Envio> listarTodos() throws Exception {
        ArrayList<Envio> envios = new ArrayList<>();

        String sql = """
                SELECT id_envio,
                       id_pedido,
                       zona_envio,
                       estado,
                       numero_seguimiento,
                       direccion_envio,
                       ciudad_envio,
                       pais_envio,
                       referencia_envio,
                       codigo_postal_envio,
                       fecha_envio,
                       fecha_entrega_estimada,
                       fecha_entrega_real,
                       creado_en,
                       actualizado_en
                FROM envio
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                envios.add(mapearEnvio(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return envios;
    }

    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Envio envio = new Envio();

        envio.setId_envio(rs.getInt("id_envio"));
        envio.setZona_envio(rs.getString("zona_envio"));
        envio.setEstado(rs.getString("estado"));
        envio.setNumero_seguimiento(rs.getString("numero_seguimiento"));
        envio.setDireccion_envio(rs.getString("direccion_envio"));
        envio.setCiudad_envio(rs.getString("ciudad_envio"));
        envio.setPais_envio(rs.getString("pais_envio"));
        envio.setReferencia_envio(rs.getString("referencia_envio"));
        envio.setCodigo_postal_envio(rs.getString("codigo_postal_envio"));

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        envio.setPedido(pedido);

        Timestamp fechaEnvio = rs.getTimestamp("fecha_envio");
        Timestamp fechaEstimada = rs.getTimestamp("fecha_entrega_estimada");
        Timestamp fechaReal = rs.getTimestamp("fecha_entrega_real");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaEnvio != null) {
            envio.setFecha_envio(fechaEnvio.toLocalDateTime());
        }

        if (fechaEstimada != null) {
            envio.setFecha_entrega_estimada(fechaEstimada.toLocalDateTime());
        }

        if (fechaReal != null) {
            envio.setFecha_entrega_real(fechaReal.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            envio.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            envio.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return envio;
    }
}