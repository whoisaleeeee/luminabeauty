package dao.impl;

// Project imports
import dao.ValoracionDAO;
import luminabeauty.model.Valoracion;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ValoracionDAOImpl implements ValoracionDAO {

    @Override
    public int insertar(Valoracion valoracion) {
        int resultado = 0;
        String sql = "INSERT INTO Valoracion(calificacion, comentario, fecha, idCliente, idProducto) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, valoracion.getCalificacion());
            ps.setString(2, valoracion.getComentario());
            ps.setTimestamp(3, Timestamp.valueOf(valoracion.getFecha()));
            ps.setInt(4, valoracion.getIdCliente());
            ps.setInt(5, valoracion.getIdProducto());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Valoracion: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Valoracion> listarTodos() {
        ArrayList<Valoracion> lista = new ArrayList<>();
        String sql = "SELECT id, calificacion, comentario, fecha, idCliente, idProducto FROM Valoracion";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearValoracion(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Valoraciones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Valoracion buscarPorId(int id) {
        Valoracion valoracion = null;
        String sql = "SELECT id, calificacion, comentario, fecha, idCliente, idProducto FROM Valoracion WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    valoracion = mapearValoracion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Valoracion: " + e.getMessage());
        }
        return valoracion;
    }

    @Override
    public int actualizar(Valoracion valoracion) {
        int resultado = 0;
        String sql = "UPDATE Valoracion SET calificacion=?, comentario=?, fecha=?, idCliente=?, idProducto=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, valoracion.getCalificacion());
            ps.setString(2, valoracion.getComentario());
            ps.setTimestamp(3, Timestamp.valueOf(valoracion.getFecha()));
            ps.setInt(4, valoracion.getIdCliente());
            ps.setInt(5, valoracion.getIdProducto());
            ps.setInt(6, valoracion.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Valoracion: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Valoracion WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Valoracion: " + e.getMessage());
        }
        return resultado;
    }

    private Valoracion mapearValoracion(ResultSet rs) throws SQLException {
        Valoracion v = new Valoracion();
        v.setId(rs.getInt("id"));
        v.setCalificacion(rs.getInt("calificacion"));
        v.setComentario(rs.getString("comentario"));
        v.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
        v.setIdCliente(rs.getInt("idCliente"));
        v.setIdProducto(rs.getInt("idProducto"));
        return v;
    }
}
