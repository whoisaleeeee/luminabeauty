package dao.impl;

// Project imports
import dao.ClienteDAO;
import luminabeauty.model.Cliente;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAOImpl implements ClienteDAO {

    @Override
    public int insertar(Cliente cliente) {
        int resultado = 0;
        String sql = "INSERT INTO Cliente(idUsuario, puntosFidelidad, nivelCliente) VALUES(?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getId());
            ps.setInt(2, cliente.getPuntosFidelidad());
            ps.setString(3, cliente.getNivelCliente());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Cliente: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Cliente> listarTodos() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, puntosFidelidad, nivelCliente FROM Cliente";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapCliente(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Clientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Cliente buscarPorId(int idUsuario) {
        Cliente cliente = null;
        String sql = "SELECT idUsuario, puntosFidelidad, nivelCliente FROM Cliente WHERE idUsuario = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = mapCliente(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Cliente: " + e.getMessage());
        }
        return cliente;
    }

    @Override
    public int actualizar(Cliente cliente) {
        int resultado = 0;
        String sql = "UPDATE Cliente SET puntosFidelidad=?, nivelCliente=? WHERE idUsuario=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cliente.getPuntosFidelidad());
            ps.setString(2, cliente.getNivelCliente());
            ps.setInt(3, cliente.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Cliente: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int idUsuario) {
        int resultado = 0;
        String sql = "DELETE FROM Cliente WHERE idUsuario = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Cliente: " + e.getMessage());
        }
        return resultado;
    }

    private Cliente mapCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("idUsuario"));
        c.setPuntosFidelidad(rs.getInt("puntosFidelidad"));
        c.setNivelCliente(rs.getString("nivelCliente"));
        return c;
    }
}