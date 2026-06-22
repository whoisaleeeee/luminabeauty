package dao.impl;

// Project imports
import dao.EnvioDAO;
import luminabeauty.model.Envio;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class EnvioDAOImpl implements EnvioDAO {

    @Override
    public int insertar(Envio envio) {
        int resultado = 0;
        String sql = "INSERT INTO Envio(fechaEnvio, fechaEntregaEstimada, fechaEntregaReal, estado, numeroSeguimiento, idPedido, idDireccion) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // setObject permite NULL en campos opcionales de fecha
            ps.setObject(1, envio.getFechaEnvio() != null ? Timestamp.valueOf(envio.getFechaEnvio()) : null);
            ps.setObject(2, envio.getFechaEntregaEstimada() != null ? Timestamp.valueOf(envio.getFechaEntregaEstimada()) : null);
            ps.setObject(3, envio.getFechaEntregaReal() != null ? Timestamp.valueOf(envio.getFechaEntregaReal()) : null);
            ps.setString(4, envio.getEstado());
            ps.setString(5, envio.getNumeroSeguimiento());
            ps.setInt(6, envio.getIdPedido());
            ps.setInt(7, envio.getIdDireccion());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Envio: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Envio> listarTodos() {
        ArrayList<Envio> lista = new ArrayList<>();
        String sql = "SELECT id, fechaEnvio, fechaEntregaEstimada, fechaEntregaReal, estado, numeroSeguimiento, idPedido, idDireccion FROM Envio";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEnvio(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Envios: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Envio buscarPorId(int id) {
        Envio envio = null;
        String sql = "SELECT id, fechaEnvio, fechaEntregaEstimada, fechaEntregaReal, estado, numeroSeguimiento, idPedido, idDireccion FROM Envio WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    envio = mapearEnvio(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Envio: " + e.getMessage());
        }
        return envio;
    }

    @Override
    public int actualizar(Envio envio) {
        int resultado = 0;
        String sql = "UPDATE Envio SET fechaEnvio=?, fechaEntregaEstimada=?, fechaEntregaReal=?, " +
                "estado=?, numeroSeguimiento=?, idPedido=?, idDireccion=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, envio.getFechaEnvio() != null ? Timestamp.valueOf(envio.getFechaEnvio()) : null);
            ps.setObject(2, envio.getFechaEntregaEstimada() != null ? Timestamp.valueOf(envio.getFechaEntregaEstimada()) : null);
            ps.setObject(3, envio.getFechaEntregaReal() != null ? Timestamp.valueOf(envio.getFechaEntregaReal()) : null);
            ps.setString(4, envio.getEstado());
            ps.setString(5, envio.getNumeroSeguimiento());
            ps.setInt(6, envio.getIdPedido());
            ps.setInt(7, envio.getIdDireccion());
            ps.setInt(8, envio.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Envio: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Envio WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Envio: " + e.getMessage());
        }
        return resultado;
    }

    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Envio e = new Envio();
        e.setId(rs.getInt("id"));
        // getTimestamp puede retornar null en columnas opcionales
        Timestamp fechaEnvio = rs.getTimestamp("fechaEnvio");
        e.setFechaEnvio(fechaEnvio.toLocalDateTime());
        Timestamp fechaEstimada = rs.getTimestamp("fechaEntregaEstimada");
        e.setFechaEntregaEstimada(fechaEstimada.toLocalDateTime());
        Timestamp fechaReal = rs.getTimestamp("fechaEntregaReal");
        e.setFechaEntregaReal(fechaReal.toLocalDateTime());
        e.setEstado(rs.getString("estado"));
        e.setNumeroSeguimiento(rs.getString("numeroSeguimiento"));
        e.setIdPedido(rs.getInt("idPedido"));
        e.setIdDireccion(rs.getInt("idDireccion"));
        return e;
    }
}
