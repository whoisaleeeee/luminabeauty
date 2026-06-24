package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public Pedido insertar(Pedido pedido) throws Exception {
        String sql = """
                INSERT INTO pedido(
                    codigo_pedido,
                    id_cliente,
                    id_cupon,
                    codigo_cupon_aplicado,
                    subtotal_productos,
                    costo_envio,
                    descuento,
                    total,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, pedido.getCodigo_pedido());
            stmt.setInt(2, pedido.getCliente().getId_usuario());

            if (pedido.getCupon() != null) {
                stmt.setInt(3, pedido.getCupon().getId_cupon());
                stmt.setString(4, pedido.getCodigo_cupon_aplicado());
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setBigDecimal(5, pedido.getSubtotal_productos());
            stmt.setBigDecimal(6, pedido.getCosto_envio());
            stmt.setBigDecimal(7, pedido.getDescuento());
            stmt.setBigDecimal(8, pedido.getTotal());
            stmt.setString(9, pedido.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId_pedido(rs.getInt(1));
                }
            }

            return pedido;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Pedido pedido) throws Exception {
        String sql = """
                UPDATE pedido
                SET estado = 'CANCELADO'
                WHERE id_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pedido.getId_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el pedido con ID: " + pedido.getId_pedido());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pedido buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_pedido,
                       codigo_pedido,
                       id_cliente,
                       id_cupon,
                       codigo_cupon_aplicado,
                       subtotal_productos,
                       costo_envio,
                       descuento,
                       total,
                       estado,
                       creado_en,
                       actualizado_en
                FROM pedido
                WHERE id_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Pedido actualizar(Pedido pedido) throws Exception {
        String sql = """
                UPDATE pedido
                SET codigo_pedido = ?,
                    id_cliente = ?,
                    id_cupon = ?,
                    codigo_cupon_aplicado = ?,
                    subtotal_productos = ?,
                    costo_envio = ?,
                    descuento = ?,
                    total = ?,
                    estado = ?
                WHERE id_pedido = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, pedido.getCodigo_pedido());
            stmt.setInt(2, pedido.getCliente().getId_usuario());

            if (pedido.getCupon() != null) {
                stmt.setInt(3, pedido.getCupon().getId_cupon());
                stmt.setString(4, pedido.getCodigo_cupon_aplicado());
            } else {
                stmt.setNull(3, Types.INTEGER);
                stmt.setNull(4, Types.VARCHAR);
            }

            stmt.setBigDecimal(5, pedido.getSubtotal_productos());
            stmt.setBigDecimal(6, pedido.getCosto_envio());
            stmt.setBigDecimal(7, pedido.getDescuento());
            stmt.setBigDecimal(8, pedido.getTotal());
            stmt.setString(9, pedido.getEstado());
            stmt.setInt(10, pedido.getId_pedido());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el pedido con ID: " + pedido.getId_pedido());
            }

            return pedido;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Pedido> listarTodos() throws Exception {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        String sql = """
                SELECT id_pedido,
                       codigo_pedido,
                       id_cliente,
                       id_cupon,
                       codigo_cupon_aplicado,
                       subtotal_productos,
                       costo_envio,
                       descuento,
                       total,
                       estado,
                       creado_en,
                       actualizado_en
                FROM pedido
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pedidos;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido pedido = new Pedido();

        pedido.setId_pedido(rs.getInt("id_pedido"));
        pedido.setCodigo_pedido(rs.getString("codigo_pedido"));
        pedido.setCodigo_cupon_aplicado(rs.getString("codigo_cupon_aplicado"));
        pedido.setSubtotal_productos(rs.getBigDecimal("subtotal_productos"));
        pedido.setCosto_envio(rs.getBigDecimal("costo_envio"));
        pedido.setDescuento(rs.getBigDecimal("descuento"));
        pedido.setTotal(rs.getBigDecimal("total"));
        pedido.setEstado(rs.getString("estado"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        pedido.setCliente(cliente);

        int idCupon = rs.getInt("id_cupon");
        if (!rs.wasNull()) {
            Cupon cupon = new Cupon();
            cupon.setId_cupon(idCupon);
            pedido.setCupon(cupon);
        }

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            pedido.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            pedido.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return pedido;
    }
}