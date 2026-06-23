package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ValoracionDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.Valoracion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ValoracionDAOImpl implements ValoracionDAO {

    @Override
    public Valoracion insertar(Valoracion valoracion) throws Exception {
        String sql = """
                INSERT INTO valoracion(id_cliente, id_producto, calificacion, comentario)
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, valoracion.getCliente().getId_usuario());
            stmt.setString(2, valoracion.getProducto().getId_producto());
            stmt.setInt(3, valoracion.getCalificacion());
            stmt.setString(4, valoracion.getComentario());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    valoracion.setId_valoracion(rs.getInt(1));
                }
            }

            return valoracion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Valoracion valoracion) throws Exception {
        String sql = """
                DELETE FROM valoracion
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, valoracion.getId_valoracion());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Valoracion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_valoracion, id_cliente, id_producto, calificacion, comentario, creando_en, actualizado_en
                FROM valoracion
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearValoracion(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Valoracion actualizar(Valoracion valoracion) throws Exception {
        String sql = """
                UPDATE valoracion
                SET id_cliente = ?,
                    id_producto = ?,
                    calificacion = ?,
                    comentario = ?,
                    actualizado_en = ?
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, valoracion.getCliente().getId_usuario());
            stmt.setString(2, valoracion.getProducto().getId_producto());
            stmt.setInt(3, valoracion.getCalificacion());
            stmt.setString(4, valoracion.getComentario());

            if (valoracion.getFecha_actualizacion() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(valoracion.getFecha_actualizacion()));
            }

            stmt.setInt(6, valoracion.getId_valoracion());

            stmt.executeUpdate();

            return valoracion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Valoracion> listarTodos() throws Exception {
        ArrayList<Valoracion> valoraciones = new ArrayList<>();

        String sql = """
                SELECT id_valoracion, id_cliente, id_producto, calificacion, comentario, creado_en, actualizado_en
                FROM valoracion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                valoraciones.add(mapearValoracion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return valoraciones;
    }

    private Valoracion mapearValoracion(ResultSet rs) throws SQLException {
        Valoracion valoracion = new Valoracion();

        valoracion.setId_valoracion(rs.getInt("id_valoracion"));
        valoracion.setCalificacion(rs.getInt("calificacion"));
        valoracion.setComentario(rs.getString("comentario"));

        Timestamp fecha_creado = rs.getTimestamp("creando_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");
        if (fecha_creado != null) {
            valoracion.setFecha_creacion(fecha_creado.toLocalDateTime());
        }
        if (fecha_actualizado != null) {
            valoracion.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        valoracion.setCliente(cliente);

        Producto producto = new Producto();
        producto.setId_producto(rs.getString("id_producto"));
        valoracion.setProducto(producto);

        return valoracion;
    }
}