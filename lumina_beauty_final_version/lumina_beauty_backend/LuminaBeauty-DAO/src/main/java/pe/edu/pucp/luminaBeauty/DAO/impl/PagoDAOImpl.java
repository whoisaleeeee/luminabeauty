package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.Model.MetodoDePago;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public Pago insertar(Pago pago) throws Exception {
        String sql = """
                INSERT INTO pago(
                    id_pedido,
                    id_metodo_pago,
                    monto,
                    estado,
                    referencia_transaccion,
                    fecha_pago,
                    fecha_reembolso
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pago.getPedido().getId_pedido());
            stmt.setInt(2, pago.getMetodoDePago().getId_metodo_pago());
            stmt.setBigDecimal(3, pago.getMonto());
            stmt.setString(4, pago.getEstado());
            stmt.setString(5, pago.getReferencia_transaccion());

            if (pago.getFecha_pago() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(pago.getFecha_pago()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (pago.getFecha_reembolso() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(pago.getFecha_reembolso()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pago.setId_pago(rs.getInt(1));
                }
            }

            return pago;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Pago pago) throws Exception {
        String sql = """
                DELETE FROM pago
                WHERE id_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pago.getId_pago());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el pago con ID: " + pago.getId_pago());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_pago,
                       id_pedido,
                       id_metodo_pago,
                       monto,
                       estado,
                       referencia_transaccion,
                       fecha_pago,
                       fecha_reembolso,
                       creado_en,
                       actualizado_en
                FROM pago
                WHERE id_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Pago actualizar(Pago pago) throws Exception {
        String sql = """
                UPDATE pago
                SET id_pedido = ?,
                    id_metodo_pago = ?,
                    monto = ?,
                    estado = ?,
                    referencia_transaccion = ?,
                    fecha_pago = ?,
                    fecha_reembolso = ?
                WHERE id_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pago.getPedido().getId_pedido());
            stmt.setInt(2, pago.getMetodoDePago().getId_metodo_pago());
            stmt.setBigDecimal(3, pago.getMonto());
            stmt.setString(4, pago.getEstado());
            stmt.setString(5, pago.getReferencia_transaccion());

            if (pago.getFecha_pago() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(pago.getFecha_pago()));
            } else {
                stmt.setNull(6, Types.TIMESTAMP);
            }

            if (pago.getFecha_reembolso() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(pago.getFecha_reembolso()));
            } else {
                stmt.setNull(7, Types.TIMESTAMP);
            }

            stmt.setInt(8, pago.getId_pago());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el pago con ID: " + pago.getId_pago());
            }

            return pago;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Pago> listarTodos() throws Exception {
        ArrayList<Pago> pagos = new ArrayList<>();

        String sql = """
                SELECT id_pago,
                       id_pedido,
                       id_metodo_pago,
                       monto,
                       estado,
                       referencia_transaccion,
                       fecha_pago,
                       fecha_reembolso,
                       creado_en,
                       actualizado_en
                FROM pago
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapearPago(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pagos;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago pago = new Pago();

        pago.setId_pago(rs.getInt("id_pago"));
        pago.setMonto(rs.getBigDecimal("monto"));
        pago.setEstado(rs.getString("estado"));
        pago.setReferencia_transaccion(rs.getString("referencia_transaccion"));

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        pago.setPedido(pedido);

        MetodoDePago metodo = new MetodoDePago();
        metodo.setId_metodo_pago(rs.getInt("id_metodo_pago"));
        pago.setMetodoDePago(metodo);

        Timestamp fechaPago = rs.getTimestamp("fecha_pago");
        Timestamp fechaReembolso = rs.getTimestamp("fecha_reembolso");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaPago != null) {
            pago.setFecha_pago(fechaPago.toLocalDateTime());
        }

        if (fechaReembolso != null) {
            pago.setFecha_reembolso(fechaReembolso.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            pago.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            pago.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return pago;
    }
}