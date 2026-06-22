package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DetalleListaDAO;
import pe.edu.pucp.luminaBeauty.Model.DetalleLista;
import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class DetalleListaDAOImpl implements DetalleListaDAO {

    @Override
    public DetalleLista insertar(DetalleLista detalle) throws Exception {
        String sql = """
                INSERT INTO DetalleLista(idLista, idProducto)
                VALUES (?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getLista().getId());
            stmt.setInt(2, detalle.getProducto().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId(rs.getInt(1));
                }
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(DetalleLista detalle) throws Exception {
        String sql = """
                DELETE FROM DetalleLista
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetalleLista buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, idLista, idProducto
                FROM DetalleLista
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalleLista(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public DetalleLista actualizar(DetalleLista detalle) throws Exception {
        String sql = """
                UPDATE DetalleLista
                SET idLista = ?,
                    idProducto = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getLista().getId());
            stmt.setInt(2, detalle.getProducto().getId());
            stmt.setInt(3, detalle.getId());

            stmt.executeUpdate();

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetalleLista> listarTodos() throws Exception {
        ArrayList<DetalleLista> detalles = new ArrayList<>();

        String sql = """
                SELECT id, idLista, idProducto
                FROM DetalleLista
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetalleLista(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return detalles;
    }

    private DetalleLista mapearDetalleLista(ResultSet rs) throws SQLException {
        DetalleLista detalle = new DetalleLista();

        detalle.setId(rs.getInt("id"));

        ListaDeDeseos lista = new ListaDeDeseos();
        lista.setId(rs.getInt("idLista"));
        detalle.setLista(lista);

        Producto producto = new Producto();
        producto.setId(rs.getInt("idProducto"));
        detalle.setProducto(producto);

        return detalle;
    }
}