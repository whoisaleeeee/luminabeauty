package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.EmpleadoDAO;
import pe.edu.pucp.luminaBeauty.Model.Empleado;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public Empleado insertar(Empleado empleado) throws Exception {
        String sqlUsuario = """
                INSERT INTO Usuario(nombre, apellido, correo, contrasena, dni, telefono, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlEmpleado = """
                INSERT INTO Empleado(idUsuario, rol)
                VALUES (?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

            stmtUsuario.setString(1, empleado.getNombre());
            stmtUsuario.setString(2, empleado.getApellido());
            stmtUsuario.setString(3, empleado.getCorreo());
            stmtUsuario.setString(4, empleado.getContrasena());
            stmtUsuario.setString(5, empleado.getDni());
            stmtUsuario.setString(6, empleado.getTelefono());
            stmtUsuario.setInt(7, empleado.getEstado());

            stmtUsuario.executeUpdate();

            try (ResultSet rs = stmtUsuario.getGeneratedKeys()) {
                if (rs.next()) {
                    empleado.setIdEmpleado(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtEmpleado = connection.prepareStatement(sqlEmpleado)) {
                stmtEmpleado.setInt(1, empleado.getIdEmpleado());
                stmtEmpleado.setString(2, empleado.getRol());
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
                DELETE FROM Usuario
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, empleado.getIdEmpleado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Empleado buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT u.id, u.nombre, u.apellido, u.correo, u.contrasena,
                       u.dni, u.telefono, u.estado,
                       e.rol
                FROM Usuario u
                INNER JOIN Empleado e ON u.id = e.idUsuario
                WHERE u.id = ?
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

        String sqlEmpleado = """
                UPDATE Empleado
                SET rol = ?
                WHERE idUsuario = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario);
             PreparedStatement stmtEmpleado = connection.prepareStatement(sqlEmpleado)) {

            stmtUsuario.setString(1, empleado.getNombre());
            stmtUsuario.setString(2, empleado.getApellido());
            stmtUsuario.setString(3, empleado.getCorreo());
            stmtUsuario.setString(4, empleado.getContrasena());
            stmtUsuario.setString(5, empleado.getDni());
            stmtUsuario.setString(6, empleado.getTelefono());
            stmtUsuario.setInt(7, empleado.getEstado());
            stmtUsuario.setInt(8, empleado.getIdEmpleado());
            stmtUsuario.executeUpdate();

            stmtEmpleado.setString(1, empleado.getRol());
            stmtEmpleado.setInt(2, empleado.getIdEmpleado());
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
                SELECT u.id, u.nombre, u.apellido, u.correo, u.contrasena,
                       u.dni, u.telefono, u.estado,
                       e.rol
                FROM Usuario u
                INNER JOIN Empleado e ON u.id = e.idUsuario
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

        empleado.setIdEmpleado(rs.getInt("id"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setApellido(rs.getString("apellido"));
        empleado.setCorreo(rs.getString("correo"));
        empleado.setContrasena(rs.getString("contrasena"));
        empleado.setDni(rs.getString("dni"));
        empleado.setTelefono(rs.getString("telefono"));
        empleado.setEstado(rs.getInt("estado"));
        empleado.setRol(rs.getString("rol"));

        return empleado;
    }
}