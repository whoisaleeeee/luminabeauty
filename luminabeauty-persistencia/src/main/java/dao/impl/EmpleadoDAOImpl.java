package dao.impl;

// Project imports
import dao.EmpleadoDAO;
import luminabeauty.model.Empleado;
import dao.DBManager;

// Java standard library imports
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    @Override
    public int insertar(Empleado empleado) {
        int resultado = 0;
        String sql = "INSERT INTO Empleado(idUsuario, rol) VALUES(?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empleado.getId());
            ps.setString(2, empleado.getRol());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar Empleado: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public ArrayList<Empleado> listarTodos() {
        ArrayList<Empleado> lista = new ArrayList<>();
        String sql = "SELECT idUsuario, rol FROM Empleado";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEmpleado(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Empleados: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Empleado buscarPorId(int idUsuario) {
        Empleado empleado = null;
        String sql = "SELECT idUsuario, rol FROM Empleado WHERE idUsuario = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = mapearEmpleado(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Empleado: " + e.getMessage());
        }
        return empleado;
    }

    @Override
    public int actualizar(Empleado empleado) {
        int resultado = 0;
        String sql = "UPDATE Empleado SET rol=? WHERE idUsuario=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, empleado.getRol());
            ps.setInt(2, empleado.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Empleado: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int idUsuario, boolean logico) {
        int resultado = 0;
        String sqlFisico = "DELETE FROM Empleado WHERE idUsuario = ?";//script elminacion fisica
        String sqlLogico = "UPDATE Usuario SET estado=0 WHERE id=?";//script elminacion logica

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = logico ? con.prepareStatement(sqlLogico) : con.prepareStatement(sqlFisico)) {

            ps.setInt(1, idUsuario);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Empleado: " + e.getMessage());
        }
        return resultado;
    }

    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setId(rs.getInt("idUsuario"));
        e.setRol(rs.getString("rol"));
        return e;
    }
}
