package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.UsuarioDAO;
import pe.edu.pucp.luminaBeauty.Model.Usuario;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public Usuario insertar(Usuario usuario) throws Exception {
        String sql = """
                INSERT INTO Usuario(nombre, apellido, correo, contrasena, dni, telefono, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setString(5, usuario.getDni());
            stmt.setString(6, usuario.getTelefono());
            stmt.setInt(7, usuario.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Usuario usuario) throws Exception {
        String sql = """
                DELETE FROM Usuario
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, nombre, apellido, correo, contrasena, dni, telefono, estado
                FROM Usuario
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Usuario actualizar(Usuario usuario) throws Exception {
        String sql = """
                UPDATE Usuario
                SET nombre = ?,
                    apellido = ?,
                    correo = ?,
                    contrasena = ?,
                    dni = ?,
                    telefono = ?,
                    estado = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setString(5, usuario.getDni());
            stmt.setString(6, usuario.getTelefono());
            stmt.setInt(7, usuario.getEstado());
            stmt.setInt(8, usuario.getId());

            stmt.executeUpdate();

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Usuario> listarTodos() throws Exception {
        ArrayList<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id, nombre, apellido, correo, contrasena, dni, telefono, estado
                FROM Usuario
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setDni(rs.getString("dni"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setEstado(rs.getInt("estado"));

        return usuario;
    }
}