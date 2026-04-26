package dao.impl;

// Project imports
import dao.MetodoDePagoDAO;
import luminabeauty.model.MetodoDePago;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MetodoDePagoDAOImpl implements MetodoDePagoDAO {

    @Override
    public int insertar(MetodoDePago metodo) {
        int resultado = 0;
        String sql = "INSERT INTO MetodoDePago(nombre, descripcion, icono) VALUES(?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, metodo.getNombre());
            ps.setString(2, metodo.getDescripcion());
            ps.setString(3, metodo.getIcono());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar MetodoDePago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<MetodoDePago> listarTodos() {
        ArrayList<MetodoDePago> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, icono FROM MetodoDePago";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearMetodo(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar MetodosDePago: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public MetodoDePago buscarPorId(int id) {
        MetodoDePago metodo = null;
        String sql = "SELECT id, nombre, descripcion, icono FROM MetodoDePago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    metodo = mapearMetodo(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar MetodoDePago: " + e.getMessage());
        }
        return metodo;
    }

    @Override
    public int actualizar(MetodoDePago metodo) {
        int resultado = 0;
        String sql = "UPDATE MetodoDePago SET nombre=?, descripcion=?, icono=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, metodo.getNombre());
            ps.setString(2, metodo.getDescripcion());
            ps.setString(3, metodo.getIcono());
            ps.setInt(4, metodo.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar MetodoDePago: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM MetodoDePago WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar MetodoDePago: " + e.getMessage());
        }
        return resultado;
    }

    private MetodoDePago mapearMetodo(ResultSet rs) throws SQLException {
        MetodoDePago m = new MetodoDePago();
        m.setId(rs.getInt("id"));
        m.setNombre(rs.getString("nombre"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setIcono(rs.getString("icono"));
        return m;
    }
}
