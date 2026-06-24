package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public Empleado insertar(Empleado empleado) throws Exception {

        empleado.setTipo_usuario("EMPLEADO");

        if (empleado.getRol() == null || empleado.getRol().isBlank()) {
            empleado.setRol("SOPORTE");
        }

        String sqlUsuario = """
                INSERT INTO usuario(
                    nombres,
                    apellidos,
                    correo,
                    contrasena_hash,
                    telefono,
                    dni,
                    tipo_usuario,
                    estado
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlEmpleado = """
                INSERT INTO empleado(
                    id_usuario,
                    tipo_usuario,
                    rol
                )
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

            stmtUsuario.setString(1, empleado.getNombres());
            stmtUsuario.setString(2, empleado.getApellidos());
            stmtUsuario.setString(3, empleado.getCorreo());
            stmtUsuario.setString(4, empleado.getContrasena_hash());
            stmtUsuario.setString(5, empleado.getTelefono());
            stmtUsuario.setString(6, empleado.getDni());
            stmtUsuario.setString(7, "EMPLEADO");
            stmtUsuario.setInt(8, empleado.getEstado());

            stmtUsuario.executeUpdate();

            try (ResultSet rs = stmtUsuario.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setId_usuario(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtEmpleado = connection.prepareStatement(sqlEmpleado)) {
                stmtEmpleado.setInt(1, empleado.getId_usuario());
                stmtEmpleado.setString(2, "EMPLEADO");
                stmtEmpleado.setString(3, empleado.getRol());

                stmtEmpleado.executeUpdate();
            }

            return empleado;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Empleado empleado) throws Exception {

        String sql = """
                UPDATE usuario
                SET estado = 0
                WHERE id_usuario = ?
                  AND tipo_usuario = 'EMPLEADO'
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, empleado.getId_usuario());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el empleado con ID: " + empleado.getId_usuario());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Empleado buscarPorId(Integer id) throws Exception {

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombres,
                    u.apellidos,
                    u.correo,
                    u.contrasena_hash,
                    u.telefono,
                    u.dni,
                    u.tipo_usuario,
                    u.estado,
                    u.creado_en,
                    u.actualizado_en,
                    e.rol
                FROM usuario u
                INNER JOIN empleado e 
                    ON u.id_usuario = e.id_usuario
                   AND u.tipo_usuario = e.tipo_usuario
                WHERE u.id_usuario = ?
                  AND u.tipo_usuario = 'EMPLEADO'
                  AND u.estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEmpleado(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Empleado actualizar(Empleado empleado) throws Exception {

        String sqlUsuario = """
                UPDATE usuario
                SET nombres = ?,
                    apellidos = ?,
                    correo = ?,
                    contrasena_hash = ?,
                    telefono = ?,
                    dni = ?,
                    estado = ?
                WHERE id_usuario = ?
                  AND tipo_usuario = 'EMPLEADO'
                """;

        String sqlEmpleado = """
                UPDATE empleado
                SET rol = ?
                WHERE id_usuario = ?
                  AND tipo_usuario = 'EMPLEADO'
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario);
             PreparedStatement stmtEmpleado = connection.prepareStatement(sqlEmpleado)) {

            stmtUsuario.setString(1, empleado.getNombres());
            stmtUsuario.setString(2, empleado.getApellidos());
            stmtUsuario.setString(3, empleado.getCorreo());
            stmtUsuario.setString(4, empleado.getContrasena_hash());
            stmtUsuario.setString(5, empleado.getTelefono());
            stmtUsuario.setString(6, empleado.getDni());
            stmtUsuario.setInt(7, empleado.getEstado());
            stmtUsuario.setInt(8, empleado.getId_usuario());

            stmtUsuario.executeUpdate();

            stmtEmpleado.setString(1, empleado.getRol());
            stmtEmpleado.setInt(2, empleado.getId_usuario());

            stmtEmpleado.executeUpdate();

            return empleado;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Empleado> listarTodos() throws Exception {

        ArrayList<Empleado> empleados = new ArrayList<>();

        String sql = """
                SELECT
                    u.id_usuario,
                    u.nombres,
                    u.apellidos,
                    u.correo,
                    u.contrasena_hash,
                    u.telefono,
                    u.dni,
                    u.tipo_usuario,
                    u.estado,
                    u.creado_en,
                    u.actualizado_en,
                    e.rol
                FROM usuario u
                INNER JOIN empleado e 
                    ON u.id_usuario = e.id_usuario
                   AND u.tipo_usuario = e.tipo_usuario
                WHERE u.tipo_usuario = 'EMPLEADO'
                  AND u.estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return empleados;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {

        Empleado empleado = new Empleado();

        empleado.setId_usuario(rs.getInt("id_usuario"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));
        empleado.setCorreo(rs.getString("correo"));
        empleado.setContrasena_hash(rs.getString("contrasena_hash"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setDni(rs.getString("dni"));
        empleado.setTipo_usuario(rs.getString("tipo_usuario"));
        empleado.setEstado(rs.getInt("estado"));
        empleado.setRol(rs.getString("rol"));

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            empleado.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            empleado.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return empleado;
    }
}