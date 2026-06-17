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
            INSERT INTO ComprobanteDePago(tipo, serie, numero, fechaEmision, idPedido)
            VALUES (?, ?, ?, ?, ?)
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, comprobante.getTipo());
            stmt.setString(2, comprobante.getSerie());
            stmt.setInt(3, comprobante.getNumero());
            stmt.setTimestamp(4, Timestamp.valueOf(comprobante.getFechaEmision()));
            stmt.setInt(5, comprobante.getPedido().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    comprobante.setId(rs.getInt(1));
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
            DELETE FROM ComprobanteDePago
            WHERE id = ?
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, comprobante.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ComprobanteDePago buscarPorId(Integer id) throws Exception {
        String sql = """
            SELECT id, tipo, serie, numero, fechaEmision, idPedido
            FROM ComprobanteDePago
            WHERE id = ?
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
            UPDATE ComprobanteDePago
            SET tipo = ?,
                serie = ?,
                numero = ?,
                fechaEmision = ?,
                idPedido = ?
            WHERE id = ?
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, comprobante.getTipo());
            stmt.setString(2, comprobante.getSerie());
            stmt.setInt(3, comprobante.getNumero());
            stmt.setTimestamp(4, Timestamp.valueOf(comprobante.getFechaEmision()));
            stmt.setInt(5, comprobante.getPedido().getId());
            stmt.setInt(6, comprobante.getId());

            stmt.executeUpdate();

            return comprobante;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ComprobanteDePago> listarTodos() throws Exception {
        ArrayList<ComprobanteDePago> comprobantes = new ArrayList<>();

        String sql = """
            SELECT id, tipo, serie, numero, fechaEmision, idPedido
            FROM ComprobanteDePago
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

        comprobante.setId(rs.getInt("id"));
        comprobante.setTipo(rs.getString("tipo"));
        comprobante.setSerie(rs.getString("serie"));
        comprobante.setNumero(rs.getInt("numero"));
        comprobante.setFechaEmision(rs.getTimestamp("fechaEmision").toLocalDateTime());

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("idPedido"));
        comprobante.setPedido(pedido);

        return comprobante;
    }
}