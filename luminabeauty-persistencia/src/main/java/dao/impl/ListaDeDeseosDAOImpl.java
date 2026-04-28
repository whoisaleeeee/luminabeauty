package dao.impl;

// Project imports
import dao.ListaDeDeseosDAO;
import luminabeauty.model.ListaDeDeseos;
import dao.DBManager;

// Java standard library imports
import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class ListaDeDeseosDAOImpl implements ListaDeDeseosDAO {

    @Override
    public int insertar(ListaDeDeseos lista) {
        int resultado = 0;
        String sql = "INSERT INTO ListaDeDeseos(idCliente, fechaCreacion, fechaActualizacion) VALUES(?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lista.getIdCliente());
            ps.setTimestamp(2, lista.getFechaCreacion() != null
                    ? Timestamp.valueOf(lista.getFechaCreacion()) : null);
            ps.setObject(3, lista.getFechaActualizacion() != null  // ✅ null-safe
                    ? Timestamp.valueOf(lista.getFechaActualizacion()) : null);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar ListaDeDeseos: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<ListaDeDeseos> listarTodos() {
        ArrayList<ListaDeDeseos> listaTotal = new ArrayList<>();
        String sql = "SELECT id, idCliente, fechaCreacion, fechaActualizacion FROM ListaDeDeseos"; // ✅

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
        String sql = "SELECT id, idCliente, fechaCreacion, fechaActualizacion FROM ListaDeDeseos WHERE id = ?"; // ✅

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
        String sql = "UPDATE ListaDeDeseos SET idCliente=?, fechaActualizacion=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lista.getIdCliente());
            ps.setTimestamp(2, Timestamp.valueOf(lista.getFechaActualizacion()));
            ps.setInt(3, lista.getId());
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
        l.setFechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime());

        // fechaActualizacion puede ser null si aun no se ha actualizado
        Timestamp fechaAct = rs.getTimestamp("fechaActualizacion");
        l.setFechaActualizacion(fechaAct != null ? fechaAct.toLocalDateTime() : null);
        return l;
    }
}