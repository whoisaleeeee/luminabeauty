package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Direccion;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public Cliente insertar(Cliente cliente) throws Exception {

        cliente.setTipo_usuario("CLIENTE");

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

        String sqlCliente = """
                INSERT INTO cliente(
                    id_usuario,
                    tipo_usuario,
                    puntos_fidelidad,
                    nivel_cliente,
                    id_direccion_principal
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

            stmtUsuario.setString(1, cliente.getNombres());
            stmtUsuario.setString(2, cliente.getApellidos());
            stmtUsuario.setString(3, cliente.getCorreo());
            stmtUsuario.setString(4, cliente.getContrasena_hash());
            stmtUsuario.setString(5, cliente.getTelefono());
            stmtUsuario.setString(6, cliente.getDni());
            stmtUsuario.setString(7, "CLIENTE");
            stmtUsuario.setInt(8, cliente.getEstado());

            stmtUsuario.executeUpdate();

            try (ResultSet rs = stmtUsuario.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId_usuario(rs.getInt(1));
                }
            }

            try (PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {

                stmtCliente.setInt(1, cliente.getId_usuario());
                stmtCliente.setString(2, "CLIENTE");
                stmtCliente.setInt(3, cliente.getPuntos_fidelidad());
                stmtCliente.setString(4, cliente.getNivel_cliente());

                if (cliente.getDireccion_principal() != null) {
                    stmtCliente.setInt(5, cliente.getDireccion_principal().getId_direccion());
                } else {
                    stmtCliente.setNull(5, Types.INTEGER);
                }

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
                UPDATE usuario
                SET estado = 0
                WHERE id_usuario = ?
                  AND tipo_usuario = 'CLIENTE'
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, cliente.getId_usuario());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró el cliente con ID: " + cliente.getId_usuario());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cliente buscarPorId(Integer id) throws Exception {

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
                    c.puntos_fidelidad,
                    c.nivel_cliente,
                    c.id_direccion_principal
                FROM usuario u
                INNER JOIN cliente c ON u.id_usuario = c.id_usuario
                WHERE u.id_usuario = ?
                  AND u.tipo_usuario = 'CLIENTE'
                  AND u.estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
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
                UPDATE usuario
                SET nombres = ?,
                    apellidos = ?,
                    correo = ?,
                    contrasena_hash = ?,
                    telefono = ?,
                    dni = ?,
                    estado = ?
                WHERE id_usuario = ?
                  AND tipo_usuario = 'CLIENTE'
                """;

        String sqlCliente = """
                UPDATE cliente
                SET puntos_fidelidad = ?,
                    nivel_cliente = ?,
                    id_direccion_principal = ?
                WHERE id_usuario = ?
                  AND tipo_usuario = 'CLIENTE'
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario);
             PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {

            stmtUsuario.setString(1, cliente.getNombres());
            stmtUsuario.setString(2, cliente.getApellidos());
            stmtUsuario.setString(3, cliente.getCorreo());
            stmtUsuario.setString(4, cliente.getContrasena_hash());
            stmtUsuario.setString(5, cliente.getTelefono());
            stmtUsuario.setString(6, cliente.getDni());
            stmtUsuario.setInt(7, cliente.getEstado());
            stmtUsuario.setInt(8, cliente.getId_usuario());

            stmtUsuario.executeUpdate();

            stmtCliente.setInt(1, cliente.getPuntos_fidelidad());
            stmtCliente.setString(2, cliente.getNivel_cliente());

            if (cliente.getDireccion_principal() != null) {
                stmtCliente.setInt(3, cliente.getDireccion_principal().getId_direccion());
            } else {
                stmtCliente.setNull(3, Types.INTEGER);
            }

            stmtCliente.setInt(4, cliente.getId_usuario());

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
                    c.puntos_fidelidad,
                    c.nivel_cliente,
                    c.id_direccion_principal
                FROM usuario u
                INNER JOIN cliente c ON u.id_usuario = c.id_usuario
                WHERE u.tipo_usuario = 'CLIENTE'
                  AND u.estado = 1
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {

        Cliente cliente = new Cliente();

        cliente.setId_usuario(rs.getInt("id_usuario"));
        cliente.setNombres(rs.getString("nombres"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setCorreo(rs.getString("correo"));
        cliente.setContrasena_hash(rs.getString("contrasena_hash"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setDni(rs.getString("dni"));
        cliente.setTipo_usuario(rs.getString("tipo_usuario"));
        cliente.setEstado(rs.getInt("estado"));

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            cliente.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            cliente.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        cliente.setPuntos_fidelidad(rs.getInt("puntos_fidelidad"));
        cliente.setNivel_cliente(rs.getString("nivel_cliente"));

        int idDireccionPrincipal = rs.getInt("id_direccion_principal");

        if (!rs.wasNull()) {
            Direccion direccionPrincipal = new Direccion();
            direccionPrincipal.setId_direccion(idDireccionPrincipal);
            cliente.setDireccion_principal(direccionPrincipal);
        }
        
        return cliente;
    }
}