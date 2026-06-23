package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class EnvioDAOImpl implements EnvioDAO {

    @Override
    public Envio insertar(Envio envio) throws Exception {
        String sql = """
                INSERT INTO envio(id_pedido, zona_envio, numero_seguimiento, direccion_envio, ciudad_envio, pais_envio, 
                                  referencia_envio, codigo_postaL_envio, fecha_envio, fecha_entrega_estimada, 
                                  fecha_entrega_real)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (envio.getPedido() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setInt(1, envio.getPedido().getId_pedido());
            }
            stmt.setString(2, envio.getZona_envio());
            stmt.setString(3, envio.getNumero_seguimiento());
            stmt.setString(4, envio.getDireccion_envio());
            stmt.setString(5, envio.getCiudad_envio());
            stmt.setString(6, envio.getPais_envio());
            stmt.setString(7, envio.getReferencia_envio());
            stmt.setString(8, envio.getCodigo_postal_envio());

            if (envio.getFecha_envio() == null) {
                stmt.setNull(9, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(9, Timestamp.valueOf(envio.getFecha_envio()));
            }
            if (envio.getFecha_entrega_estimada() == null) {
                stmt.setNull(10, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(10, Timestamp.valueOf(envio.getFecha_entrega_estimada()));
            }
            if (envio.getFecha_entrega_real() == null) {
                stmt.setNull(11, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(11, Timestamp.valueOf(envio.getFecha_entrega_real()));
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

//        String sql = """
//                  UPDATE Envio SET estado = 'CANCELADO'
//                  WHERE id = ?
//                  """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, envio.getId_envio());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Envio buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_envio, id_pedido, zona_envio, numero_seguimiento, direccion_envio, ciudad_envio, pais_envio, 
                       referencia_envio, codigo_postaL_envio, fecha_envio, fecha_entrega_estimada, fecha_entrega_real, 
                       creado_en, actualizado_en
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
                    numero_seguimiento = ?, 
                    direccion_envio = ?, 
                    ciudad_envio = ?, 
                    pais_envio = ?, 
                    referencia_envio = ?, 
                    codigo_postaL_envio = ?, 
                    fecha_envio = ?, 
                    fecha_entrega_estimada = ?, 
                    fecha_entrega_real = ?, 
                    actualizado_en ? ?
                WHERE id_envio = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (envio.getPedido() == null) {
                stmt.setNull(1, Types.TIMESTAMP);
            } else {
                stmt.setInt(1, envio.getPedido().getId_pedido());
            }
            stmt.setString(2, envio.getZona_envio());
            stmt.setString(3, envio.getNumero_seguimiento());
            stmt.setString(4, envio.getDireccion_envio());
            stmt.setString(5, envio.getCiudad_envio());
            stmt.setString(6, envio.getPais_envio());
            stmt.setString(7, envio.getReferencia_envio());
            stmt.setString(8, envio.getCodigo_postal_envio());
            if (envio.getFecha_envio() == null) {
                stmt.setNull(9, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(9, Timestamp.valueOf(envio.getFecha_envio()));
            }

            if (envio.getFecha_entrega_estimada() == null) {
                stmt.setNull(10, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(10, Timestamp.valueOf(envio.getFecha_entrega_estimada()));
            }
            if (envio.getFecha_entrega_real() == null) {
                stmt.setNull(11, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(11, Timestamp.valueOf(envio.getFecha_entrega_real()));
            }
            if (envio.getFecha_actualizacion() == null) {
                stmt.setNull(12, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(12, Timestamp.valueOf(envio.getFecha_actualizacion()));
            }
            stmt.setInt(13, envio.getId_envio());

            stmt.executeUpdate();

            return envio;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Envio> listarTodos() throws Exception {
        ArrayList<Envio> envios = new ArrayList<>();

        String sql = """
                SELECT id_envio, id_pedido, zona_envio, numero_seguimiento, direccion_envio, ciudad_envio, pais_envio, 
                       referencia_envio, codigo_postaL_envio, fecha_envio, fecha_entrega_estimada, fecha_entrega_real, 
                       creado_en, actualizado_en
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

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        envio.setPedido(pedido);

        envio.setZona_envio(rs.getString("zona_envio"));
        envio.setEstado(rs.getString("estado"));
        envio.setNumero_seguimiento(rs.getString("numero_seguimiento"));
        envio.setCiudad_envio(rs.getString("ciudad_envio"));
        envio.setPais_envio(rs.getString("pais_envio"));
        envio.setReferencia_envio(rs.getString("referencia_envio"));
        envio.setCodigo_postal_envio(rs.getString("codigo_postal_envio"));

        Timestamp fechaEnvio = rs.getTimestamp("fecha_envio");
        if (fechaEnvio != null) {
            envio.setFecha_envio(fechaEnvio.toLocalDateTime());
        }

        Timestamp fechaEstimada = rs.getTimestamp("fecha_entrega_estimada");
        if (fechaEstimada != null) {
            envio.setFecha_entrega_estimada(fechaEstimada.toLocalDateTime());
        }

        Timestamp fechaReal = rs.getTimestamp("fecha_entrega_real");
        if (fechaReal != null) {
            envio.setFecha_entrega_real(fechaReal.toLocalDateTime());
        }

        Timestamp fecha_creado = rs.getTimestamp("creando_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");
        if (fecha_creado != null) {
            envio.setFecha_creacion(fecha_creado.toLocalDateTime());
        }
        if (fecha_actualizado != null) {
            envio.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return envio;
    }
}