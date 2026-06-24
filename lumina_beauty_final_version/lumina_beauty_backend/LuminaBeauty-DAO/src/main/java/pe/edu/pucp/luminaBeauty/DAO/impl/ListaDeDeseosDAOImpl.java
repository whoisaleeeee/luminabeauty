package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ListaDeDeseosDAO;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ListaDeDeseosDAOImpl implements ListaDeDeseosDAO {

    @Override
    public ListaDeDeseos insertar(ListaDeDeseos lista) throws Exception {
        String sql = """
                INSERT INTO lista_deseos(
                    id_cliente,
                    nombre,
                    descripcion
                )
                VALUES (?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lista.getCliente().getId_usuario());
            stmt.setString(2, lista.getNombre());
            stmt.setString(3, lista.getDescripcion());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lista.setId_lista_deseos(rs.getInt(1));
                }
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(ListaDeDeseos lista) throws Exception {
        String sql = """
                DELETE FROM lista_deseos
                WHERE id_lista_deseos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, lista.getId_lista_deseos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la lista de deseos con ID: " + lista.getId_lista_deseos());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListaDeDeseos buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_lista_deseos,
                       id_cliente,
                       nombre,
                       descripcion,
                       creado_en,
                       actualizado_en
                FROM lista_deseos
                WHERE id_lista_deseos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearLista(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public ListaDeDeseos actualizar(ListaDeDeseos lista) throws Exception {
        String sql = """
                UPDATE lista_deseos
                SET id_cliente = ?,
                    nombre = ?,
                    descripcion = ?
                WHERE id_lista_deseos = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, lista.getCliente().getId_usuario());
            stmt.setString(2, lista.getNombre());
            stmt.setString(3, lista.getDescripcion());
            stmt.setInt(4, lista.getId_lista_deseos());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la lista de deseos con ID: " + lista.getId_lista_deseos());
            }

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ListaDeDeseos> listarTodos() throws Exception {
        ArrayList<ListaDeDeseos> listas = new ArrayList<>();

        String sql = """
                SELECT id_lista_deseos,
                       id_cliente,
                       nombre,
                       descripcion,
                       creado_en,
                       actualizado_en
                FROM lista_deseos
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listas.add(mapearLista(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listas;
    }

    private ListaDeDeseos mapearLista(ResultSet rs) throws SQLException {
        ListaDeDeseos lista = new ListaDeDeseos();

        lista.setId_lista_deseos(rs.getInt("id_lista_deseos"));
        lista.setNombre(rs.getString("nombre"));
        lista.setDescripcion(rs.getString("descripcion"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        lista.setCliente(cliente);

        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (fechaCreacion != null) {
            lista.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            lista.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return lista;
    }
}