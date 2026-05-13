package dao.impl;

// Project imports
import dao.CarroDeComprasDAO;
import luminabeauty.model.CarroDeCompras;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CarroDeComprasDAOImpl implements CarroDeComprasDAO {

    @Override
    public int insertar(CarroDeCompras carro) {
        int resultado = 0;
        String sql = "INSERT INTO CarroDeCompras(fechaCreacion, idCliente) VALUES(?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(carro.getFechaCreacion()));
            ps.setInt(2, carro.getIdCliente());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar CarroDeCompras: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<CarroDeCompras> listarTodos() {
        ArrayList<CarroDeCompras> lista = new ArrayList<>();
        String sql = "SELECT id, fechaCreacion, idCliente FROM CarroDeCompras";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCarro(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar CarroDeCompras: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public CarroDeCompras buscarPorId(int id) {
        CarroDeCompras carro = null;
        String sql = "SELECT id, fechaCreacion, idCliente FROM CarroDeCompras WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    carro = mapearCarro(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar CarroDeCompras: " + e.getMessage());
        }
        return carro;
    }

    @Override
    public int actualizar(CarroDeCompras carro) {
        int resultado = 0;
        String sql = "UPDATE CarroDeCompras SET fechaCreacion=?, idCliente=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(carro.getFechaCreacion()));
            ps.setInt(2, carro.getIdCliente());
            ps.setInt(3, carro.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar CarroDeCompras: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM CarroDeCompras WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar CarroDeCompras: " + e.getMessage());
        }
        return resultado;
    }

    private CarroDeCompras mapearCarro(ResultSet rs) throws SQLException {
        CarroDeCompras c = new CarroDeCompras();
        c.setId(rs.getInt("id"));
        c.setFechaCreacion(rs.getTimestamp("fechaCreacion").toLocalDateTime());
        c.setIdCliente(rs.getInt("idCliente"));
        return c;
    }
}
