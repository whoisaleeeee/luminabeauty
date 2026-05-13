package dao.impl;

// Project imports
import dao.DetalleListaDAO;
import luminabeauty.model.DetalleLista;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DetalleListaDAOImpl implements DetalleListaDAO {

    @Override
    public int insertar(DetalleLista detalle) {
        int resultado = 0;
        String sql = "INSERT INTO DetalleLista(idLista, idProducto) VALUES(?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdLista());
            ps.setInt(2, detalle.getIdProducto());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar DetalleLista: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<DetalleLista> listarTodos() {
        ArrayList<DetalleLista> lista = new ArrayList<>();
        String sql = "SELECT id, idLista, idProducto FROM DetalleLista";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearDetalle(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar DetalleLista: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public DetalleLista buscarPorId(int id) {
        DetalleLista detalle = null;
        String sql = "SELECT id, idLista, idProducto FROM DetalleLista WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    detalle = mapearDetalle(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar DetalleLista: " + e.getMessage());
        }
        return detalle;
    }

    @Override
    public int actualizar(DetalleLista detalle) {
        int resultado = 0;
        String sql = "UPDATE DetalleLista SET idLista=?, idProducto=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdLista());
            ps.setInt(2, detalle.getIdProducto());
            ps.setInt(3, detalle.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar DetalleLista: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM DetalleLista WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar DetalleLista: " + e.getMessage());
        }
        return resultado;
    }

    private DetalleLista mapearDetalle(ResultSet rs) throws SQLException {
        DetalleLista d = new DetalleLista();
        d.setId(rs.getInt("id"));
        d.setIdLista(rs.getInt("idLista"));
        d.setIdProducto(rs.getInt("idProducto"));
        return d;
    }
}
