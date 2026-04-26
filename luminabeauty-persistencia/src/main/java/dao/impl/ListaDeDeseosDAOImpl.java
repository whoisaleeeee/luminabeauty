package dao.impl;

// Project imports
import dao.ListaDeDeseosDAO;
import luminabeauty.model.ListaDeDeseos;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ListaDeDeseosDAOImpl implements ListaDeDeseosDAO {

    @Override
    public int insertar(ListaDeDeseos lista) {
        int resultado = 0;
        String sql = "INSERT INTO ListaDeDeseos(idCliente) VALUES(?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lista.getIdCliente());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar ListaDeDeseos: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<ListaDeDeseos> listarTodos() {
        ArrayList<ListaDeDeseos> listaTotal = new ArrayList<>();
        String sql = "SELECT id, idCliente FROM ListaDeDeseos";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                listaTotal.add(mapearLista(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar ListaDeDeseos: " + e.getMessage());
        }
        return listaTotal;
    }

    @Override
    public ListaDeDeseos buscarPorId(int id) {
        ListaDeDeseos lista = null;
        String sql = "SELECT id, idCliente FROM ListaDeDeseos WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lista = mapearLista(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar ListaDeDeseos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public int actualizar(ListaDeDeseos lista) {
        int resultado = 0;
        String sql = "UPDATE ListaDeDeseos SET idCliente=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lista.getIdCliente());
            ps.setInt(2, lista.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar ListaDeDeseos: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM ListaDeDeseos WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar ListaDeDeseos: " + e.getMessage());
        }
        return resultado;
    }

    private ListaDeDeseos mapearLista(ResultSet rs) throws SQLException {
        ListaDeDeseos l = new ListaDeDeseos();
        l.setId(rs.getInt("id"));
        l.setIdCliente(rs.getInt("idCliente"));
        return l;
    }
}