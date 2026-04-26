package dao.impl;

// Project imports
import dao.PagoDAO;
import luminabeauty.model.Pago;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PagoDAOImpl implements PagoDAO {

    @Override
    public int insertar(Pago pago) {
        int resultado = 0;
        String sql = "INSERT INTO Pago(monto, estado, fechaPago, idPedido, idMetodo) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, pago.getMonto());
            ps.setString(2, pago.getEstado());
            ps.setTimestamp(3, Timestamp.valueOf(pago.getFechaPago()));
            ps.setInt(4, pago.getIdPedido());
            ps.setInt(5, pago.getIdMetodo());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Pago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Pago> listarTodos() {
        ArrayList<Pago> lista = new ArrayList<>();
        String sql = "SELECT id, monto, estado, fechaPago, idPedido, idMetodo FROM Pago";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPago(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Pagos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Pago buscarPorId(int id) {
        Pago pago = null;
        String sql = "SELECT id, monto, estado, fechaPago, idPedido, idMetodo FROM Pago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pago = mapearPago(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Pago: " + e.getMessage());
        }
        return pago;
    }

    @Override
    public int actualizar(Pago pago) {
        int resultado = 0;
        String sql = "UPDATE Pago SET monto=?, estado=?, fechaPago=?, idPedido=?, idMetodo=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, pago.getMonto());
            ps.setString(2, pago.getEstado());
            ps.setTimestamp(3, Timestamp.valueOf(pago.getFechaPago()));
            ps.setInt(4, pago.getIdPedido());
            ps.setInt(5, pago.getIdMetodo());
            ps.setInt(6, pago.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Pago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Pago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Pago: " + e.getMessage());
        }
        return resultado;
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {
        Pago p = new Pago();
        p.setId(rs.getInt("id"));
        p.setMonto(rs.getBigDecimal("monto"));
        p.setEstado(rs.getString("estado"));
        p.setFechaPago(rs.getTimestamp("fechaPago").toLocalDateTime());
        p.setIdPedido(rs.getInt("idPedido"));
        p.setIdMetodo(rs.getInt("idMetodo"));
        return p;
    }
}