package dao.impl;

import dao.DBManager;
import dao.UsuarioDAO;
import luminabeauty.model.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public int insertar(Usuario u) {

        String sql = "INSERT INTO Usuario(nombre, apellido, correo, contrasena, dni, telefono) VALUES(?,?,?,?,?,?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getDni());
            ps.setString(6, u.getTelefono());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public ArrayList<Usuario> listarTodos() {

        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try (Connection con = DBManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setDni(rs.getString("dni"));
                u.setTelefono(rs.getString("telefono"));

                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public int actualizar(Usuario u) {

        String sql = "UPDATE Usuario SET nombre=?, apellido=?, correo=?, contrasena=?, dni=?, telefono=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.setString(5, u.getDni());
            ps.setString(6, u.getTelefono());
            ps.setInt(7, u.getId());

            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int eliminar(int id) {

        String sql = "DELETE FROM Usuario WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public Usuario buscarPorId(int id) {

        String sql = "SELECT * FROM Usuario WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setCorreo(rs.getString("correo"));
                u.setDni(rs.getString("dni"));
                u.setTelefono(rs.getString("telefono"));
                return u;
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }

        return null;
    }
}