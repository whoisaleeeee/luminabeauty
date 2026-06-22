package dao.impl;

// Project imports
import dao.DBManager;
import dao.DireccionDAO;
import luminabeauty.model.Direccion;

// Java standard library imports (java.*)
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DireccionDAOImpl implements DireccionDAO {

    /* CREATE */
    @Override
    public int insertar(Direccion direccion){
        int result = 0;
        String sql = "INSERT INTO Direccion(direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, direccion.getDireccion());
            ps.setString(2, direccion.getCiudad());
            ps.setString(3, direccion.getPais());
            ps.setString(4, direccion.getReferencia());
            ps.setString(5, direccion.getCodigoPostal());
            ps.setBoolean(6, direccion.isEsPrincipal());
            ps.setInt(7, direccion.getIdCliente());

            result = ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar Direccion: " + e.getMessage());
        }
        return result;
    }

    /* READ - Listar */
    @Override
    public ArrayList<Direccion> listarTodos(){
        ArrayList<Direccion> list = new ArrayList<>();
        String sql = "SELECT id, direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente " +
                "FROM Direccion";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Direccion d = mapDireccion(rs);
                list.add(d);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar Direcciones: " + e.getMessage());
        }
        return list;
    }

    /* READ - ID Search */
    @Override
    public Direccion buscarPorId(int id){
        Direccion direccion = null;
        String sql = "SELECT id, direccion, ciudad, pais, referencia, codigoPostal, esPrincipal, idCliente " +
                "FROM Direccion WHERE id = ?";

        try(Connection con = DBManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    direccion = mapDireccion(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Direccion por ID: " + e.getMessage());
        }
        return direccion;
    }

    /* UPDATE */
    @Override
    public int actualizar(Direccion direccion){
        int resultado=0;
        String sql = "UPDATE Direccion SET direccion=?, ciudad=?, pais=?, referencia=?, " +
                "codigoPostal=?, esPrincipal=?, idCliente=? WHERE id=?";

        try(Connection con = DBManager.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, direccion.getDireccion());
            ps.setString(2, direccion.getCiudad());
            ps.setString(3, direccion.getPais());
            ps.setString(4, direccion.getReferencia());
            ps.setString(5, direccion.getCodigoPostal());
            ps.setBoolean(6, direccion.isEsPrincipal());
            ps.setInt(7, direccion.getIdCliente());
            ps.setInt(8, direccion.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Direccion: " + e.getMessage());
        }

        return resultado;
    }

    /* DELETE */
    @Override
    public int eliminar(int id){
        int resultado=0;
        String sql = "DELETE FROM Direccion WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Direccion: " + e.getMessage());
        }

        return resultado;
    }

    /* AUXILIAR METHOD for READ */
    private Direccion mapDireccion(ResultSet rs) throws SQLException {
        Direccion d = new Direccion();
        d.setId(rs.getInt("id"));
        d.setDireccion(rs.getString("direccion"));
        d.setCiudad(rs.getString("ciudad"));
        d.setPais(rs.getString("pais"));
        d.setReferencia(rs.getString("referencia"));
        d.setCodigoPostal(rs.getString("codigoPostal"));
        d.setEsPrincipal(rs.getBoolean("esPrincipal"));
        d.setIdCliente(rs.getInt("idCliente"));
        return d;
    }
}
