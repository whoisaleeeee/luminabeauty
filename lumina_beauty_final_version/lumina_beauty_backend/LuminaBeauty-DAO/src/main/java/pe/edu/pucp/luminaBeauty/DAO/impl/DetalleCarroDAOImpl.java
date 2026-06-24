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
                INSERT INTO detalle_carrito(
                    id_carrito,
                    id_producto,
                    cantidad
                )
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detalle.getCarro().getId_carrito());
            stmt.setInt(2, detalle.getProducto().getId_producto());
            stmt.setInt(3, detalle.getCantidad());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId_detalle_carrito(rs.getInt(1));
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
                DELETE FROM detalle_carrito
                WHERE id_detalle_carrito = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getId_detalle_carrito());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle con ID: " + detalle.getId_detalle_carrito());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DetalleCarro buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_detalle_carrito,
                       id_carrito,
                       id_producto,
                       cantidad,
                       creado_en,
                       actualizado_en
                FROM detalle_carrito
                WHERE id_detalle_carrito = ?
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
                UPDATE detalle_carrito
                SET id_carrito = ?,
                    id_producto = ?,
                    cantidad = ?
                WHERE id_detalle_carrito = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, detalle.getCarro().getId_carrito());
            stmt.setInt(2, detalle.getProducto().getId_producto());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setInt(4, detalle.getId_detalle_carrito());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el detalle con ID: " + detalle.getId_detalle_carrito());
            }

            return detalle;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<DetalleCarro> listarTodos() throws Exception {
        ArrayList<DetalleCarro> detalles = new ArrayList<>();

        String sql = """
                SELECT id_detalle_carrito,
                       id_carrito,
                       id_producto,
                       cantidad,
                       creado_en,
                       actualizado_en
                FROM detalle_carrito
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

        detalle.setId_detalle_carrito(rs.getInt("id_detalle_carrito"));
        detalle.setCantidad(rs.getInt("cantidad"));

        CarroDeCompras carro = new CarroDeCompras();
        carro.setId_carrito(rs.getInt("id_carrito"));
        detalle.setCarro(carro);

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        detalle.setProducto(producto);

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            detalle.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            detalle.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return detalle;
    }
}