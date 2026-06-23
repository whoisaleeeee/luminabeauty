package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.MetodoDePagoDAO;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class MetodoDePagoDAOImpl implements MetodoDePagoDAO {

    @Override
    public MetodoDePago insertar(MetodoDePago metodo) throws Exception {
        String sql = """
                INSERT INTO metodo_pago(nombre, descripcion, icono_url)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, metodo.getNombre());
            stmt.setString(2, metodo.getDescripcion());
            stmt.setString(3, metodo.getIcono_url());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    metodo.setId_metodo_pago(rs.getInt(1));
                }
            }

            return metodo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(MetodoDePago metodo) throws Exception {
        String sql = """
                DELETE FROM metodo_pago
                WHERE id_metodo_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, metodo.getId_metodo_pago());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MetodoDePago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_metodo_pago, nombre, descripcion, icono_url, estado, creado_en, actualizado_en
                FROM metodo_pago
                WHERE id_metodo_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearMetodoDePago(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public MetodoDePago actualizar(MetodoDePago metodo) throws Exception {
        String sql = """
                UPDATE metodo_pago
                SET nombre = ?,
                    descripcion = ?,
                    icono_url = ?,
                    estado = ?,
                    actualizado_en = ?
                WHERE id_metodo_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, metodo.getNombre());
            stmt.setString(2, metodo.getDescripcion());
            stmt.setString(3, metodo.getIcono_url());
            stmt.setInt(4, metodo.getId_metodo_pago());

            stmt.executeUpdate();

            return metodo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<MetodoDePago> listarTodos() throws Exception {
        ArrayList<MetodoDePago> metodos = new ArrayList<>();

        String sql = """
                SELECT id_metodo_pago, nombre, descripcion, icono_url, estado, creado_en, actualizado_en
                FROM metodo_pago
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                metodos.add(mapearMetodoDePago(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return metodos;
    }

    private MetodoDePago mapearMetodoDePago(ResultSet rs) throws SQLException {
        MetodoDePago metodo = new MetodoDePago();

        metodo.setId_metodo_pago(rs.getInt("id_metodo_pago"));
        metodo.setNombre(rs.getString("nombre"));
        metodo.setDescripcion(rs.getString("descripcion"));
        metodo.setIcono_url(rs.getString("icono_url"));
        metodo.setEstado(rs.getInt("estado"));
        Timestamp fecha_creado = rs.getTimestamp("creando_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");
        if (fecha_creado != null) {
            metodo.setFecha_creacion(fecha_creado.toLocalDateTime());
        }
        if (fecha_actualizado != null) {
            metodo.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return metodo;
    }
}