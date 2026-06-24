package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MarcaDAO;
import pe.edu.pucp.luminaBeauty.Model.Marca;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class MarcaDAOImpl implements MarcaDAO {

    @Override
    public Marca insertar(Marca marca) throws Exception {
        String sql = """
                INSERT INTO marca(nombre, descripcion, logo_url, estado)
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, marca.getNombre());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getLogo_url());
            stmt.setInt(4, marca.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    marca.setId_marca(rs.getInt(1));
                }
            }

            return marca;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Marca marca) throws Exception {
        String sql = """
                UPDATE marca
                SET estado = 0
                WHERE id_marca = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, marca.getId_marca());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la marca con ID: " + marca.getId_marca());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Marca buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_marca, nombre, descripcion, logo_url, estado, creado_en, actualizado_en
                FROM marca
                WHERE id_marca = ?
                  AND estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearMarca(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Marca actualizar(Marca marca) throws Exception {
        String sql = """
                UPDATE marca
                SET nombre = ?,
                    descripcion = ?,
                    logo_url = ?,
                    estado = ?
                WHERE id_marca = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, marca.getNombre());
            stmt.setString(2, marca.getDescripcion());
            stmt.setString(3, marca.getLogo_url());
            stmt.setInt(4, marca.getEstado());
            stmt.setInt(5, marca.getId_marca());

            stmt.executeUpdate();

            return marca;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Marca> listarTodos() throws Exception {
        ArrayList<Marca> marcas = new ArrayList<>();

        String sql = """
                SELECT id_marca, nombre, descripcion, logo_url, estado, creado_en, actualizado_en
                FROM marca
                WHERE estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                marcas.add(mapearMarca(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return marcas;
    }

    private Marca mapearMarca(ResultSet rs) throws SQLException {
        Marca marca = new Marca();

        marca.setId_marca(rs.getInt("id_marca"));
        marca.setNombre(rs.getString("nombre"));
        marca.setDescripcion(rs.getString("descripcion"));
        marca.setLogo_url(rs.getString("logo_url"));
        marca.setEstado(rs.getInt("estado"));

        Timestamp fecha_creado = rs.getTimestamp("creado_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");

        if (fecha_creado != null) {
            marca.setFecha_creacion(fecha_creado.toLocalDateTime());
        }

        if (fecha_actualizado != null) {
            marca.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return marca;
    }
}