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
                INSERT INTO Valoracion(calificacion, comentario, fecha, idCliente, idProducto)
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, valoracion.getCalificacion());
            stmt.setString(2, valoracion.getComentario());

            if (valoracion.getFecha() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(valoracion.getFecha()));
            }

            stmt.setInt(4, valoracion.getCliente().getId());
            stmt.setInt(5, valoracion.getProducto().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    valoracion.setId(rs.getInt(1));
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
                DELETE FROM Valoracion
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, valoracion.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Valoracion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, calificacion, comentario, fecha, idCliente, idProducto
                FROM Valoracion
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearValoracion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Valoracion actualizar(Valoracion valoracion) throws Exception {
        String sql = """
                UPDATE Valoracion
                SET calificacion = ?,
                    comentario = ?,
                    fecha = ?,
                    idCliente = ?,
                    idProducto = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, valoracion.getCalificacion());
            stmt.setString(2, valoracion.getComentario());

            if (valoracion.getFecha() == null) {
                stmt.setNull(3, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(3, Timestamp.valueOf(valoracion.getFecha()));
            }

            stmt.setInt(4, valoracion.getCliente().getId());
            stmt.setInt(5, valoracion.getProducto().getId());
            stmt.setInt(6, valoracion.getId());

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
                SELECT id, calificacion, comentario, fecha, idCliente, idProducto
                FROM Valoracion
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

        valoracion.setId(rs.getInt("id"));
        valoracion.setCalificacion(rs.getInt("calificacion"));
        valoracion.setComentario(rs.getString("comentario"));

        Timestamp fecha = rs.getTimestamp("fecha");
        if (fecha != null) {
            valoracion.setFecha(fecha.toLocalDateTime());
        }

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        valoracion.setCliente(cliente);

        Producto producto = new Producto();
        producto.setId(rs.getInt("idProducto"));
        valoracion.setProducto(producto);

        return valoracion;
    }
}