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
                INSERT INTO detalle_lista_deseos(
                    id_lista_deseos,
                    id_producto
                )
                VALUES (?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getLista().getId_lista_deseos());
            stmt.setInt(2, detalle.getProducto().getId_producto());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId_detalle_lista_deseos(rs.getInt(1));
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
                DELETE FROM detalle_lista_deseos
                WHERE id_detalle_lista_deseos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getId_detalle_lista_deseos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de lista con ID: "
                        + detalle.getId_detalle_lista_deseos());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetalleLista buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_detalle_lista_deseos,
                       id_lista_deseos,
                       id_producto
                FROM detalle_lista_deseos
                WHERE id_detalle_lista_deseos = ?
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
                UPDATE detalle_lista_deseos
                SET id_lista_deseos = ?,
                    id_producto = ?
                WHERE id_detalle_lista_deseos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getLista().getId_lista_deseos());
            stmt.setInt(2, detalle.getProducto().getId_producto());
            stmt.setInt(3, detalle.getId_detalle_lista_deseos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle de lista con ID: "
                        + detalle.getId_detalle_lista_deseos());
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetalleLista> listarTodos() throws Exception {
        ArrayList<DetalleLista> detalles = new ArrayList<>();

        String sql = """
                SELECT id_detalle_lista_deseos,
                       id_lista_deseos,
                       id_producto
                FROM detalle_lista_deseos
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

        detalle.setId_detalle_lista_deseos(rs.getInt("id_detalle_lista_deseos"));

        ListaDeDeseos lista = new ListaDeDeseos();
        lista.setId_lista_deseos(rs.getInt("id_lista_deseos"));
        detalle.setLista(lista);

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        detalle.setProducto(producto);

        return detalle;
    }
}