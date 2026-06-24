package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DireccionDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class DireccionDAOImpl implements DireccionDAO {

    @Override
    public Direccion insertar(Direccion direccion) throws Exception {

        String sql = """
                INSERT INTO direccion(
                    id_cliente,
                    direccion,
                    ciudad,
                    pais,
                    referencia,
                    codigo_postal
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, direccion.getCliente().getId_usuario());
            stmt.setString(2, direccion.getDireccion());
            stmt.setString(3, direccion.getCiudad());

            if (direccion.getPais() == null || direccion.getPais().isBlank()) {
                stmt.setString(4, "Peru");
            } else {
                stmt.setString(4, direccion.getPais());
            }

            stmt.setString(5, direccion.getReferencia());
            stmt.setString(6, direccion.getCodigo_postal());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    direccion.setId_direccion(rs.getInt(1));
                }
            }

            return direccion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Direccion direccion) throws Exception {

        String sql = """
                DELETE FROM direccion
                WHERE id_direccion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, direccion.getId_direccion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Direccion buscarPorId(Integer id) throws Exception {

        String sql = """
                SELECT id_direccion, id_cliente, direccion, ciudad, pais,
                       referencia, codigo_postal, creado_en, actualizado_en
                FROM direccion
                WHERE id_direccion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDireccion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Direccion actualizar(Direccion direccion) throws Exception {

        String sql = """
                UPDATE direccion
                SET direccion = ?,
                    ciudad = ?,
                    pais = ?,
                    referencia = ?,
                    codigo_postal = ?
                WHERE id_direccion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, direccion.getDireccion());
            stmt.setString(2, direccion.getCiudad());

            if (direccion.getPais() == null || direccion.getPais().isBlank()) {
                stmt.setString(3, "Peru");
            } else {
                stmt.setString(3, direccion.getPais());
            }

            stmt.setString(4, direccion.getReferencia());
            stmt.setString(5, direccion.getCodigo_postal());
            stmt.setInt(6, direccion.getId_direccion());

            stmt.executeUpdate();

            return direccion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Direccion> listarTodos() throws Exception {

        ArrayList<Direccion> direcciones = new ArrayList<>();

        String sql = """
                SELECT id_direccion, id_cliente, direccion, ciudad, pais,
                       referencia, codigo_postal, creado_en, actualizado_en
                FROM direccion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                direcciones.add(mapearDireccion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return direcciones;
    }

    private Direccion mapearDireccion(ResultSet rs) throws SQLException {

        Direccion direccion = new Direccion();

        direccion.setId_direccion(rs.getInt("id_direccion"));
        direccion.setDireccion(rs.getString("direccion"));
        direccion.setCiudad(rs.getString("ciudad"));
        direccion.setPais(rs.getString("pais"));
        direccion.setReferencia(rs.getString("referencia"));
        direccion.setCodigo_postal(rs.getString("codigo_postal"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        direccion.setCliente(cliente);

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            direccion.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            direccion.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return direccion;
    }
}