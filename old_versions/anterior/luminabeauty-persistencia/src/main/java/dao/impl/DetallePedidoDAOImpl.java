package dao.impl;

// Project imports
import dao.DetallePedidoDAO;
import luminabeauty.model.DetallePedido;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetallePedidoDAOImpl implements DetallePedidoDAO {

    @Override
    public int insertar(DetallePedido detalle) {
        int resultado = 0;
        String sql = "INSERT INTO DetallePedido(cantidad, precioUnitario, subtotal, idPedido, idProducto) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setBigDecimal(3, detalle.getSubtotal());
            ps.setInt(4, detalle.getIdPedido());
            ps.setInt(5, detalle.getIdProducto());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar DetallePedido: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<DetallePedido> listarTodos() {
        ArrayList<DetallePedido> lista = new ArrayList<>();
        String sql = "SELECT id, cantidad, precioUnitario, subtotal, idPedido, idProducto FROM DetallePedido";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDetalle(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar DetallePedido: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public DetallePedido buscarPorId(int id) {
        DetallePedido detalle = null;
        String sql = "SELECT id, cantidad, precioUnitario, subtotal, idPedido, idProducto FROM DetallePedido WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapearDetalle(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar DetallePedido: " + e.getMessage());
        }
        return detalle;
    }

    @Override
    public int actualizar(DetallePedido detalle) {
        int resultado = 0;
        String sql = "UPDATE DetallePedido SET cantidad=?, precioUnitario=?, subtotal=?, idPedido=?, idProducto=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setBigDecimal(3, detalle.getSubtotal());
            ps.setInt(4, detalle.getIdPedido());
            ps.setInt(5, detalle.getIdProducto());
            ps.setInt(6, detalle.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar DetallePedido: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM DetallePedido WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar DetallePedido: " + e.getMessage());
        }
        return resultado;
    }

    private DetallePedido mapearDetalle(ResultSet rs) throws SQLException {
        DetallePedido d = new DetallePedido();
        d.setId(rs.getInt("id"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));
        d.setSubtotal(rs.getBigDecimal("subtotal"));
        d.setIdPedido(rs.getInt("idPedido"));
        d.setIdProducto(rs.getInt("idProducto"));
        return d;
    }
}
