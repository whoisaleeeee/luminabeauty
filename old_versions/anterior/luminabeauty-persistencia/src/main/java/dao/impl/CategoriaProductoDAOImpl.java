package dao.impl;

import dao.CategoriaProductoDAO;
import dao.DBManager;
import luminabeauty.model.CategoriaProducto;
import luminabeauty.model.CategoriaProducto;

import java.sql.*;
import java.util.ArrayList;

public class CategoriaProductoDAOImpl implements CategoriaProductoDAO {

    @Override
    public int insertar(CategoriaProducto c) {
        // SQL basado en tu tabla: nombre, descripcion, idCategoriaPadre
        String sql = "INSERT INTO CategoriaProducto(nombre, descripcion) VALUES(?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar categoría: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public ArrayList<CategoriaProducto> listarTodas() {
        ArrayList<CategoriaProducto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM CategoriaProducto";

        try (Connection con = DBManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                CategoriaProducto c = new CategoriaProducto();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public int actualizar(CategoriaProducto c) {

        String sql = "UPDATE CategoriaProducto SET nombre=?, descripcion=?, idCategoriaPadre=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombre());
            ps.setString(2, c.getDescripcion());
            if (c.getIdCategoriaPadre() == 0) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, c.getIdCategoriaPadre());
            }
            ps.setInt(4, c.getId());


            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
            return 0;
        }


    }

    @Override
    public int eliminar(int id) {

        String sql = "DELETE FROM CategoriaProducto WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
            return 0;
        }
    }
}