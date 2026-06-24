package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ComprobanteDePagoDAO;
import pe.edu.pucp.luminaBeauty.Model.ComprobanteDePago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ComprobanteDePagoDAOImpl implements ComprobanteDePagoDAO {

    @Override
    public ComprobanteDePago insertar(ComprobanteDePago comprobante) throws Exception {
        String sql = """
                INSERT INTO comprobante_pago(
                    id_pedido,
                    tipo,
                    serie,
                    numero,
                    emitido_en
                )
                VALUES (?, ?, ?, ?, COALESCE(?, CURRENT_TIMESTAMP))
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, comprobante.getPedido().getId_pedido());
            stmt.setString(2, comprobante.getTipo());
            stmt.setString(3, comprobante.getSerie());
            stmt.setString(4, comprobante.getNumero());

            if (comprobante.getFecha_emision() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(comprobante.getFecha_emision()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    comprobante.setId_comprobante(rs.getInt(1));
                }
            }

            return comprobante;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(ComprobanteDePago comprobante) throws Exception {
        String sql = """
                DELETE FROM comprobante_pago
                WHERE id_comprobante = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, comprobante.getId_comprobante());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el comprobante con ID: "
                        + comprobante.getId_comprobante());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ComprobanteDePago buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_comprobante,
                       id_pedido,
                       tipo,
                       serie,
                       numero,
                       emitido_en
                FROM comprobante_pago
                WHERE id_comprobante = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearComprobante(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public ComprobanteDePago actualizar(ComprobanteDePago comprobante) throws Exception {
        String sql = """
                UPDATE comprobante_pago
                SET id_pedido = ?,
                    tipo = ?,
                    serie = ?,
                    numero = ?,
                    emitido_en = COALESCE(?, emitido_en)
                WHERE id_comprobante = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, comprobante.getPedido().getId_pedido());
            stmt.setString(2, comprobante.getTipo());
            stmt.setString(3, comprobante.getSerie());
            stmt.setString(4, comprobante.getNumero());

            if (comprobante.getFecha_emision() != null) {
                stmt.setTimestamp(5, Timestamp.valueOf(comprobante.getFecha_emision()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }

            stmt.setInt(6, comprobante.getId_comprobante());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el comprobante con ID: "
                        + comprobante.getId_comprobante());
            }

            return comprobante;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ComprobanteDePago> listarTodos() throws Exception {
        ArrayList<ComprobanteDePago> comprobantes = new ArrayList<>();

        String sql = """
                SELECT id_comprobante,
                       id_pedido,
                       tipo,
                       serie,
                       numero,
                       emitido_en
                FROM comprobante_pago
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                comprobantes.add(mapearComprobante(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return comprobantes;
    }

    private ComprobanteDePago mapearComprobante(ResultSet rs) throws SQLException {
        ComprobanteDePago comprobante = new ComprobanteDePago();

        comprobante.setId_comprobante(rs.getInt("id_comprobante"));
        comprobante.setTipo(rs.getString("tipo"));
        comprobante.setSerie(rs.getString("serie"));
        comprobante.setNumero(rs.getString("numero"));

        Pedido pedido = new Pedido();
        pedido.setId_pedido(rs.getInt("id_pedido"));
        comprobante.setPedido(pedido);

        Timestamp fechaEmision = rs.getTimestamp("emitido_en");

        if (fechaEmision != null) {
            comprobante.setFecha_emision(fechaEmision.toLocalDateTime());
        }

        return comprobante;
    }
}