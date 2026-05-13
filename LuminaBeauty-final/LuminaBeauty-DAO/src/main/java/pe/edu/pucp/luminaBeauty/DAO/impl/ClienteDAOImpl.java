package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente insertar(Cliente cliente) throws Exception {
        String sqlUsuario = """
                INSERT INTO Usuario(nombre, apellido, correo, contrasena, dni, telefono, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        String sqlCliente = """
                INSERT INTO Cliente(idUsuario, puntosFidelidad, nivelCliente)
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

            stmtUsuario.setString(1, cliente.getNombre());
            stmtUsuario.setString(2, cliente.getApellido());
            stmtUsuario.setString(3, cliente.getCorreo());
            stmtUsuario.setString(4, cliente.getContrasena());
            stmtUsuario.setString(5, cliente.getDni());
            stmtUsuario.setString(6, cliente.getTelefono());
            stmtUsuario.setInt(7, cliente.getEstado());

            stmtUsuario.executeUpdate();

            try (ResultSet rs = stmtUsuario.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, cliente.getId());
                stmtCliente.setInt(2, cliente.getPuntosFidelidad());
                stmtCliente.setString(3, cliente.getNivelCliente());
                stmtCliente.executeUpdate();
            }

            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Cliente cliente) throws Exception {
        String sql = """
                DELETE FROM Usuario
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT u.id, u.nombre, u.apellido, u.correo, u.contrasena,
                       u.dni, u.telefono, u.estado,
                       c.puntosFidelidad, c.nivelCliente
                FROM Usuario u
                INNER JOIN Cliente c ON u.id = c.idUsuario
                WHERE u.id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();

                    cliente.setId(rs.getInt("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setCorreo(rs.getString("correo"));
                    cliente.setContrasena(rs.getString("contrasena"));
                    cliente.setDni(rs.getString("dni"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEstado(rs.getInt("estado"));
                    cliente.setPuntosFidelidad(rs.getInt("puntosFidelidad"));
                    cliente.setNivelCliente(rs.getString("nivelCliente"));

                    return cliente;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Cliente actualizar(Cliente cliente) throws Exception {
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

        String sqlCliente = """
                UPDATE Cliente
                SET puntosFidelidad = ?,
                    nivelCliente = ?
                WHERE idUsuario = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario);
             PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {

            stmtUsuario.setString(1, cliente.getNombre());
            stmtUsuario.setString(2, cliente.getApellido());
            stmtUsuario.setString(3, cliente.getCorreo());
            stmtUsuario.setString(4, cliente.getContrasena());
            stmtUsuario.setString(5, cliente.getDni());
            stmtUsuario.setString(6, cliente.getTelefono());
            stmtUsuario.setInt(7, cliente.getEstado());
            stmtUsuario.setInt(8, cliente.getId());
            stmtUsuario.executeUpdate();

            stmtCliente.setInt(1, cliente.getPuntosFidelidad());
            stmtCliente.setString(2, cliente.getNivelCliente());
            stmtCliente.setInt(3, cliente.getId());
            stmtCliente.executeUpdate();

            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Cliente> listarTodos() throws Exception {
        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = """
                SELECT u.id, u.nombre, u.apellido, u.correo, u.contrasena,
                       u.dni, u.telefono, u.estado,
                       c.puntosFidelidad, c.nivelCliente
                FROM Usuario u
                INNER JOIN Cliente c ON u.id = c.idUsuario
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();

                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setCorreo(rs.getString("correo"));
                cliente.setContrasena(rs.getString("contrasena"));
                cliente.setDni(rs.getString("dni"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEstado(rs.getInt("estado"));
                cliente.setPuntosFidelidad(rs.getInt("puntosFidelidad"));
                cliente.setNivelCliente(rs.getString("nivelCliente"));

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }
}