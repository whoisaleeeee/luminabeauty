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
                INSERT INTO pago(id_pedido, id_metodo_pago, monto, estado, referencia_transaccion, fecha_pago, fecha_reembolso)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, pago.getPedido().getId_pedido());
            stmt.setInt(2, pago.getMetodoDePago().getId_metodo_pago());
            stmt.setBigDecimal(3, pago.getMonto());
            stmt.setString(4, pago.getEstado());
            stmt.setString(5, pago.getReferencia_transaccion());
            stmt.setTimestamp(6, Timestamp.valueOf(pago.getFechaPago()));
            stmt.setTimestamp(7, Timestamp.valueOf(pago.getFecha_reembolso()));

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

//        String sql = """
//                  UPDATE Pago SET estado = 'CANCELADO'
//                  WHERE id = ?
//                  """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pago.getId_pago());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Pago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_pago, id_pedido, id_metodo_pago, monto, estado, referencia_transaccion, fecha_pago, fecha_reembolso
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
                    fecha_reembolso = ?,
                    fecha_actualizacion = ?
                WHERE id_pago = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, pago.getPedido().getId_pedido());
            stmt.setInt(2, pago.getMetodoDePago().getId_metodo_pago());
            stmt.setBigDecimal(3, pago.getMonto());
            stmt.setString(4, pago.getEstado());
            stmt.setString(5, pago.getReferencia_transaccion());
            if (pago.getFechaPago() == null) {
                stmt.setNull(6, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(6, Timestamp.valueOf(pago.getFechaPago()));
            }
            if (pago.getFecha_reembolso() == null) {
                stmt.setNull(7, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(7, Timestamp.valueOf(pago.getFecha_reembolso()));
            }
            if (pago.getFecha_actualizacion() == null) {
                stmt.setNull(8, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(8, Timestamp.valueOf(pago.getFecha_actualizacion()));
            }
            stmt.setInt(9, pago.getId_pago());

            stmt.executeUpdate();

            return pago;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Pago> listarTodos() throws Exception {
        ArrayList<Pago> pagos = new ArrayList<>();

        String sql = """
                SELECT id_pago, id_pedido, id_metodo_pago, monto, estado, referencia_transaccion, fecha_pago, fecha_reembolso
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

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        pago.setPedido(pedido);

        MetodoDePago metodo = new MetodoDePago();
        metodo.setId_metodo_pago(rs.getInt("id_metodo_pago"));
        pago.setMetodoDePago(metodo);

        pago.setMonto(rs.getBigDecimal("monto"));
        pago.setEstado(rs.getString("estado"));
        pago.setReferencia_transaccion(rs.getString("referencia_transaccion"));

        Timestamp fechaPago = rs.getTimestamp("fecha_pago");
        if (fechaPago != null) {
            pago.setFechaPago(fechaPago.toLocalDateTime());
        }
        Timestamp fechaReembolso = rs.getTimestamp("fecha_reembolso");
        if (fechaReembolso != null) {
            pago.setFecha_reembolso(fechaReembolso.toLocalDateTime());
        }
        Timestamp fecha_creado = rs.getTimestamp("creando_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");
        if (fecha_creado != null) {
            pedido.setFecha_creacion(fecha_creado.toLocalDateTime());
        }
        if (fecha_actualizado != null) {
            pedido.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return pago;
    }
}