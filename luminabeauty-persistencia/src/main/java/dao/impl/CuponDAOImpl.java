package dao.impl;

// Project imports
import dao.CuponDAO;
import luminabeauty.model.Cupon;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CuponDAOImpl implements CuponDAO {

    @Override
    public int insertar(Cupon cupon) {
        int resultado = 0;
        String sql = "INSERT INTO Cupon(codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cupon.getCodigo());
            ps.setString(2, cupon.getTipoDescuento());
            ps.setBigDecimal(3, cupon.getValorDescuento());
            ps.setTimestamp(4, Timestamp.valueOf(cupon.getFechaInicio()));
            ps.setTimestamp(5, Timestamp.valueOf(cupon.getFechaFin()));
            ps.setString(6, cupon.getEstado());
            ps.setObject(7, cupon.getLimiteUso());   // ✅ setObject permite NULL
            ps.setInt(8, cupon.getUsosActuales());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Cupon: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Cupon> listarTodos() {
        ArrayList<Cupon> lista = new ArrayList<>();
        String sql = "SELECT id, codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales FROM Cupon";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearCupon(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Cupones: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Cupon buscarPorId(int id) {
        Cupon cupon = null;
        String sql = "SELECT id, codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales FROM Cupon WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cupon = mapearCupon(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Cupon: " + e.getMessage());
        }
        return cupon;
    }

    @Override
    public int actualizar(Cupon cupon) {
        int resultado = 0;
        String sql = "UPDATE Cupon SET codigo=?, tipoDescuento=?, valorDescuento=?, fechaInicio=?, " +
                "fechaFin=?, estado=?, limiteUso=?, usosActuales=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cupon.getCodigo());
            ps.setString(2, cupon.getTipoDescuento());
            ps.setBigDecimal(3, cupon.getValorDescuento());
            ps.setTimestamp(4, Timestamp.valueOf(cupon.getFechaInicio()));
            ps.setTimestamp(5, Timestamp.valueOf(cupon.getFechaFin()));
            ps.setString(6, cupon.getEstado());
            ps.setObject(7, cupon.getLimiteUso());
            ps.setInt(8, cupon.getUsosActuales());
            ps.setInt(9, cupon.getId());
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Cupon: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Cupon WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Cupon: " + e.getMessage());
        }
        return resultado;
    }

    private Cupon mapearCupon(ResultSet rs) throws SQLException {
        Cupon c = new Cupon();
        c.setId(rs.getInt("id"));
        c.setCodigo(rs.getString("codigo"));
        c.setTipoDescuento(rs.getString("tipoDescuento"));
        c.setValorDescuento(rs.getBigDecimal("valorDescuento"));
        c.setFechaInicio(rs.getTimestamp("fechaInicio").toLocalDateTime());
        c.setFechaFin(rs.getTimestamp("fechaFin").toLocalDateTime());
        c.setEstado(rs.getString("estado"));
        // ✅ getObject para columnas nullable de tipo INT
        c.setLimiteUso((Integer) rs.getObject("limiteUso"));
        c.setUsosActuales(rs.getInt("usosActuales"));
        return c;
    }
}