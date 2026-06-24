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
                INSERT INTO usuario(nombres, apellidos, correo, contrasena_hash, telefono, dni, tipo_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena_hash());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getDni());
            stmt.setString(7, usuario.getTipo_usuario());
            stmt.setInt(8, usuario.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId_usuario(rs.getInt(1));
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
            UPDATE usuario
            SET estado = 0
            WHERE id_usuario = ?
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId_usuario());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("No se encontró el usuario con ID: " + usuario.getId_usuario());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Usuario buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_usuario, nombres, apellidos, correo, contrasena_hash, telefono, dni, tipo_usuario, estado, creado_en, actualizado_en
                FROM usuario
                WHERE id_usuario = ?
                AND estado = 1
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
            UPDATE usuario
            SET nombres = ?,
                apellidos = ?,
                correo = ?,
                contrasena_hash = ?,
                telefono = ?,
                dni = ?,
                tipo_usuario = ?,
                estado = ?
            WHERE id_usuario = ?
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNombres());
            stmt.setString(2, usuario.getApellidos());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena_hash());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getDni());
            stmt.setString(7, usuario.getTipo_usuario());
            stmt.setInt(8, usuario.getEstado());
            stmt.setInt(9, usuario.getId_usuario());

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
                SELECT id_usuario,nombres, apellidos, correo, contrasena_hash, telefono, dni, tipo_usuario, estado, creado_en, actualizado_en
                FROM usuario
                WHERE estado = 1
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

        usuario.setId_usuario(rs.getInt("id_usuario"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setContrasena_hash(rs.getString("contrasena_hash"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setDni(rs.getString("dni"));
        usuario.setTipo_usuario(rs.getString("tipo_usuario"));
        usuario.setEstado(rs.getInt("estado"));

        Timestamp fecha_creado = rs.getTimestamp("creado_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");

        if (fecha_creado != null) {
            usuario.setFecha_creacion(fecha_creado.toLocalDateTime());
        }

        if (fecha_actualizado != null) {
            usuario.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return usuario;
    }
}