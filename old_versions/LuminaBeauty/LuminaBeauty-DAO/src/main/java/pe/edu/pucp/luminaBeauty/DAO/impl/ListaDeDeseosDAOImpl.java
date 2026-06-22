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
                INSERT INTO ListaDeDeseos(idCliente)
                VALUES (?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, lista.getCliente().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lista.setId(rs.getInt(1));
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
                DELETE FROM ListaDeDeseos
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lista.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ListaDeDeseos buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, idCliente
                FROM ListaDeDeseos
                WHERE id = ?
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
                UPDATE ListaDeDeseos
                SET idCliente = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, lista.getCliente().getId());
            stmt.setInt(2, lista.getId());

            stmt.executeUpdate();

            return lista;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<ListaDeDeseos> listarTodos() throws Exception {
        ArrayList<ListaDeDeseos> listas = new ArrayList<>();

        String sql = """
                SELECT id, idCliente
                FROM ListaDeDeseos
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

        lista.setId(rs.getInt("id"));

        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        lista.setCliente(cliente);

        return lista;
    }
}