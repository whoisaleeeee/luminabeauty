package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.DetalleCarroDAO;
import pe.edu.pucp.luminaBeauty.Model.CarroDeCompras;
import pe.edu.pucp.luminaBeauty.Model.DetalleCarro;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class DetalleCarroDAOImpl implements DetalleCarroDAO {

    @Override
    public DetalleCarro insertar(DetalleCarro detalle) throws Exception {
        String sql = """
                INSERT INTO DetalleCarro(cantidad, precioUnitario, idCarro, idProducto)
                VALUES (?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setInt(3, detalle.getCarro().getId());
            stmt.setInt(4, detalle.getIdProducto().getId());

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
    public void eliminar(DetalleCarro detalle) throws Exception {
        String sql = """
                DELETE FROM DetalleCarro
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
    public DetalleCarro buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, cantidad, precioUnitario, idCarro, idProducto
                FROM DetalleCarro
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDetalleCarro(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public DetalleCarro actualizar(DetalleCarro detalle) throws Exception {
        String sql = """
                UPDATE DetalleCarro
                SET cantidad = ?,
                    precioUnitario = ?,
                    idCarro = ?,
                    idProducto = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getCantidad());
            stmt.setBigDecimal(2, detalle.getPrecioUnitario());
            stmt.setInt(3, detalle.getCarro().getId());
            stmt.setInt(4, detalle.getIdProducto().getId());
            stmt.setInt(5, detalle.getId());

            stmt.executeUpdate();

            return detalle;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetalleCarro> listarTodos() throws Exception {
        ArrayList<DetalleCarro> detalles = new ArrayList<>();

        String sql = """
                SELECT id, cantidad, precioUnitario, idCarro, idProducto
                FROM DetalleCarro
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetalleCarro(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return detalles;
    }

    private DetalleCarro mapearDetalleCarro(ResultSet rs) throws SQLException {
        DetalleCarro detalle = new DetalleCarro();

        detalle.setId(rs.getInt("id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));

        CarroDeCompras carro = new CarroDeCompras();
        carro.setId(rs.getInt("idCarro"));
        detalle.setCarro(carro);

        Producto producto = new Producto();
        producto.setId(rs.getInt("idProducto"));
        detalle.setIdProducto(producto);

        return detalle;
    }
}