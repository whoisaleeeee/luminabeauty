package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.CuponDAO;
import pe.edu.pucp.luminaBeauty.Model.Cupon;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class CuponDAOImpl implements CuponDAO {

    @Override
    public Cupon insertar(Cupon cupon) throws Exception {
        String sql = """
                INSERT INTO Cupon(codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, cupon.getCodigo());
            stmt.setString(2, cupon.getTipoDescuento());
            stmt.setBigDecimal(3, cupon.getValorDescuento());
            stmt.setTimestamp(4, Timestamp.valueOf(cupon.getFechaInicio()));
            stmt.setTimestamp(5, Timestamp.valueOf(cupon.getFechaFin()));
            stmt.setString(6, cupon.getEstado());

            if (cupon.getLimiteUso() == 0) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, cupon.getLimiteUso());
            }

            stmt.setInt(8, cupon.getUsosActuales());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cupon.setId(rs.getInt(1));
                }
            }

            return cupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Cupon cupon) throws Exception {
        String sql = """
                DELETE FROM Cupon
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cupon.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Cupon buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales
                FROM Cupon
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCupon(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Cupon actualizar(Cupon cupon) throws Exception {
        String sql = """
                UPDATE Cupon
                SET codigo = ?,
                    tipoDescuento = ?,
                    valorDescuento = ?,
                    fechaInicio = ?,
                    fechaFin = ?,
                    estado = ?,
                    limiteUso = ?,
                    usosActuales = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cupon.getCodigo());
            stmt.setString(2, cupon.getTipoDescuento());
            stmt.setBigDecimal(3, cupon.getValorDescuento());
            stmt.setTimestamp(4, Timestamp.valueOf(cupon.getFechaInicio()));
            stmt.setTimestamp(5, Timestamp.valueOf(cupon.getFechaFin()));
            stmt.setString(6, cupon.getEstado());

            if (cupon.getLimiteUso() == 0) {
                stmt.setNull(7, Types.INTEGER);
            } else {
                stmt.setInt(7, cupon.getLimiteUso());
            }

            stmt.setInt(8, cupon.getUsosActuales());
            stmt.setInt(9, cupon.getId());

            stmt.executeUpdate();

            return cupon;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Cupon> listarTodos() throws Exception {
        ArrayList<Cupon> cupones = new ArrayList<>();

        String sql = """
                SELECT id, codigo, tipoDescuento, valorDescuento, fechaInicio, fechaFin, estado, limiteUso, usosActuales
                FROM Cupon
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cupones.add(mapearCupon(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cupones;
    }

    private Cupon mapearCupon(ResultSet rs) throws SQLException {
        Cupon cupon = new Cupon();

        cupon.setId(rs.getInt("id"));
        cupon.setCodigo(rs.getString("codigo"));
        cupon.setTipoDescuento(rs.getString("tipoDescuento"));
        cupon.setValorDescuento(rs.getBigDecimal("valorDescuento"));
        cupon.setFechaInicio(rs.getTimestamp("fechaInicio").toLocalDateTime());
        cupon.setFechaFin(rs.getTimestamp("fechaFin").toLocalDateTime());
        cupon.setEstado(rs.getString("estado"));

        int limiteUso = rs.getInt("limiteUso");
        if (rs.wasNull()) {
            cupon.setLimiteUso(0);
        } else {
            cupon.setLimiteUso(limiteUso);
        }

        cupon.setUsosActuales(rs.getInt("usosActuales"));

        return cupon;
    }
}