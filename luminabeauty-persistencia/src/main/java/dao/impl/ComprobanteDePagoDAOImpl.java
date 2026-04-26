package dao.impl;

// Project imports
import dao.ComprobanteDePagoDAO;
import luminabeauty.model.ComprobanteDePago;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ComprobanteDePagoDAOImpl implements ComprobanteDePagoDAO {

    @Override
    public int insertar(ComprobanteDePago comprobante) {
        int resultado = 0;
        String sql = "INSERT INTO ComprobanteDePago(tipo, serie, numero, fechaEmision, idPedido) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, comprobante.getTipo());
            ps.setString(2, comprobante.getSerie());
            ps.setInt(3, comprobante.getNumero());
            ps.setTimestamp(4, Timestamp.valueOf(comprobante.getFechaEmision()));
            ps.setInt(5, comprobante.getIdPedido());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar ComprobanteDePago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<ComprobanteDePago> listarTodos() {
        ArrayList<ComprobanteDePago> lista = new ArrayList<>();
        String sql = "SELECT id, tipo, serie, numero, fechaEmision, idPedido FROM ComprobanteDePago";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearComprobante(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Comprobantes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public ComprobanteDePago buscarPorId(int id) {
        ComprobanteDePago comprobante = null;
        String sql = "SELECT id, tipo, serie, numero, fechaEmision, idPedido FROM ComprobanteDePago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    comprobante = mapearComprobante(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar ComprobanteDePago: " + e.getMessage());
        }
        return comprobante;
    }

    @Override
    public int actualizar(ComprobanteDePago comprobante) {
        int resultado = 0;
        String sql = "UPDATE ComprobanteDePago SET tipo=?, serie=?, numero=?, fechaEmision=?, idPedido=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, comprobante.getTipo());
            ps.setString(2, comprobante.getSerie());
            ps.setInt(3, comprobante.getNumero());
            ps.setTimestamp(4, Timestamp.valueOf(comprobante.getFechaEmision()));
            ps.setInt(5, comprobante.getIdPedido());
            ps.setInt(6, comprobante.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar ComprobanteDePago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM ComprobanteDePago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar ComprobanteDePago: " + e.getMessage());
        }
        return resultado;
    }

    private ComprobanteDePago mapearComprobante(ResultSet rs) throws SQLException {
        ComprobanteDePago c = new ComprobanteDePago();
        c.setId(rs.getInt("id"));
        c.setTipo(rs.getString("tipo"));
        c.setSerie(rs.getString("serie"));
        c.setNumero(rs.getInt("numero"));
        c.setFechaEmision(rs.getTimestamp("fechaEmision").toLocalDateTime());
        c.setIdPedido(rs.getInt("idPedido"));
        return c;
    }
}
