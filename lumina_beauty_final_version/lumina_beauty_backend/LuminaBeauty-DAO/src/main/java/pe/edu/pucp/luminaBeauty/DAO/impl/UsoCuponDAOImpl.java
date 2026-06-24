package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.UsoCuponDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.UsoCupon;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class UsoCuponDAOImpl implements UsoCuponDAO {

    @Override
    public UsoCupon insertar(UsoCupon usoCupon) throws Exception {
        String sql = """
                INSERT INTO uso_cupon(
                    id_cupon,
                    id_cliente,
                    id_pedido,
                    usado_en
                )
                VALUES (?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP))
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, usoCupon.getCupon().getId_cupon());
            stmt.setInt(2, usoCupon.getCliente().getId_usuario());
            stmt.setInt(3, usoCupon.getPedido().getId_pedido());

            if (usoCupon.getFecha_uso() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(usoCupon.getFecha_uso()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usoCupon.setId_uso_cupon(rs.getInt(1));
                }
            }

            return usoCupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(UsoCupon usoCupon) throws Exception {
        String sql = """
                DELETE FROM uso_cupon
                WHERE id_uso_cupon = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, usoCupon.getId_uso_cupon());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el uso de cupón con ID: "
                        + usoCupon.getId_uso_cupon());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UsoCupon buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_uso_cupon,
                       id_cupon,
                       id_cliente,
                       id_pedido,
                       usado_en
                FROM uso_cupon
                WHERE id_uso_cupon = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsoCupon(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public UsoCupon actualizar(UsoCupon usoCupon) throws Exception {
        String sql = """
                UPDATE uso_cupon
                SET id_cupon = ?,
                    id_cliente = ?,
                    id_pedido = ?,
                    usado_en = COALESCE(?, usado_en)
                WHERE id_uso_cupon = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, usoCupon.getCupon().getId_cupon());
            stmt.setInt(2, usoCupon.getCliente().getId_usuario());
            stmt.setInt(3, usoCupon.getPedido().getId_pedido());

            if (usoCupon.getFecha_uso() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(usoCupon.getFecha_uso()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP);
            }

            stmt.setInt(5, usoCupon.getId_uso_cupon());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el uso de cupón con ID: "
                        + usoCupon.getId_uso_cupon());
            }

            return usoCupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<UsoCupon> listarTodos() throws Exception {
        ArrayList<UsoCupon> usosCupon = new ArrayList<>();

        String sql = """
                SELECT id_uso_cupon,
                       id_cupon,
                       id_cliente,
                       id_pedido,
                       usado_en
                FROM uso_cupon
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usosCupon.add(mapearUsoCupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usosCupon;
    }

    private UsoCupon mapearUsoCupon(ResultSet rs) throws SQLException {
        UsoCupon usoCupon = new UsoCupon();

        usoCupon.setId_uso_cupon(rs.getInt("id_uso_cupon"));

        Cupon cupon = new Cupon();
        cupon.setId_cupon(rs.getInt("id_cupon"));
        usoCupon.setCupon(cupon);

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        usoCupon.setCliente(cliente);

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        usoCupon.setPedido(pedido);

        Timestamp fechaUso = rs.getTimestamp("usado_en");

        if (fechaUso != null) {
            usoCupon.setFecha_uso(fechaUso.toLocalDateTime());
        }

        return usoCupon;
    }
}