package dao.impl;

// Project imports
import dao.PedidoDAO;
import luminabeauty.model.Pedido;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class PedidoDAOImpl implements PedidoDAO {

    @Override
    public int insertar(Pedido pedido) {
        int resultado = 0;
        String sql = "INSERT INTO Pedido(fecha, total, estado, idCarrito, idCupon) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            ps.setBigDecimal(2, pedido.getTotal());
            ps.setString(3, pedido.getEstado());
            ps.setInt(4, pedido.getIdCarrito());
            ps.setObject(5, pedido.getIdCupon());    // ✅ NULL permitido

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Pedido: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Pedido> listarTodos() {
        ArrayList<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, fecha, total, estado, idCarrito, idCupon FROM Pedido";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPedido(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Pedidos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Pedido buscarPorId(int id) {
        Pedido pedido = null;
        String sql = "SELECT id, fecha, total, estado, idCarrito, idCupon FROM Pedido WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = mapearPedido(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Pedido: " + e.getMessage());
        }
        return pedido;
    }

    @Override
    public int actualizar(Pedido pedido) {
        int resultado = 0;
        String sql = "UPDATE Pedido SET fecha=?, total=?, estado=?, idCarrito=?, idCupon=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(pedido.getFecha()));
            ps.setBigDecimal(2, pedido.getTotal());
            ps.setString(3, pedido.getEstado());
            ps.setInt(4, pedido.getIdCarrito());
            ps.setObject(5, pedido.getIdCupon());
            ps.setInt(6, pedido.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Pedido: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Pedido WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Pedido: " + e.getMessage());
        }
        return resultado;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        p.setTotal(rs.getBigDecimal("total"));
        p.setEstado(rs.getString("estado"));
        p.setIdCarrito(rs.getInt("idCarrito"));
        p.setIdCupon((Integer) rs.getObject("idCupon")); // ✅ nullable
        return p;
    }
}
