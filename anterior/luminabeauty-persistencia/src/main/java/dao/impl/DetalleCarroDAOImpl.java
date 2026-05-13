package dao.impl;

// Project imports
import dao.DetalleCarroDAO;
import luminabeauty.model.DetalleCarro;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetalleCarroDAOImpl implements DetalleCarroDAO {

    @Override
    public int insertar(DetalleCarro detalle) {
        int resultado = 0;
        String sql = "INSERT INTO DetalleCarro(cantidad, precioUnitario, idCarro, idProducto) VALUES(?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setInt(3, detalle.getIdCarro());
            ps.setInt(4, detalle.getIdProducto());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar DetalleCarro: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<DetalleCarro> listarTodos() {
        ArrayList<DetalleCarro> lista = new ArrayList<>();
        String sql = "SELECT id, cantidad, precioUnitario, idCarro, idProducto FROM DetalleCarro";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDetalle(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar DetalleCarro: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public DetalleCarro buscarPorId(int id) {
        DetalleCarro detalle = null;
        String sql = "SELECT id, cantidad, precioUnitario, idCarro, idProducto FROM DetalleCarro WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapearDetalle(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar DetalleCarro: " + e.getMessage());
        }
        return detalle;
    }

    @Override
    public int actualizar(DetalleCarro detalle) {
        int resultado = 0;
        String sql = "UPDATE DetalleCarro SET cantidad=?, precioUnitario=?, idCarro=?, idProducto=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setBigDecimal(2, detalle.getPrecioUnitario());
            ps.setInt(3, detalle.getIdCarro());
            ps.setInt(4, detalle.getIdProducto());
            ps.setInt(5, detalle.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar DetalleCarro: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM DetalleCarro WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar DetalleCarro: " + e.getMessage());
        }
        return resultado;
    }

    private DetalleCarro mapearDetalle(ResultSet rs) throws SQLException {
        DetalleCarro d = new DetalleCarro();
        d.setId(rs.getInt("id"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getBigDecimal("precioUnitario"));
        d.setIdCarro(rs.getInt("idCarro"));
        d.setIdProducto(rs.getInt("idProducto"));
        return d;
    }
}