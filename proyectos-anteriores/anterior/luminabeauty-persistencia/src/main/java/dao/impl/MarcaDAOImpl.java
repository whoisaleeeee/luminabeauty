package dao.impl;

import dao.DBManager;
import dao.MarcaDAO;
import luminabeauty.model.Marca;
import java.sql.*;
import java.util.ArrayList;

public class MarcaDAOImpl implements MarcaDAO {

    @Override
    public int insertar(Marca marca) {
        String sql = "INSERT INTO Marca(nombre, descripcion, logo) VALUES(?, ?, ?)";
        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, marca.getNombre());
            ps.setString(2, marca.getDescripcion());
            ps.setString(3, marca.getLogo());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public ArrayList<Marca> listarTodas() {
        ArrayList<Marca> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, logo FROM Marca";
        try (Connection con = DBManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Marca m = new Marca();
                m.setId(rs.getInt("id"));
                m.setNombre(rs.getString("nombre"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setLogo(rs.getString("logo"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }
}