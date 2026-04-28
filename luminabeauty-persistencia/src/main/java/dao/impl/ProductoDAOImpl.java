package dao.impl;

import dao.DBManager;
import dao.ProductoDAO;
import luminabeauty.model.Producto;
import java.sql.*;
import java.util.ArrayList;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public int insertar(Producto p) {
        //SQL: nombre, slug, precio, stock, idCategoria, idMarca
        String sql = "INSERT INTO Producto(nombre, slug, precio, stock, idCategoria, idMarca) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            // (ejemplo: "Labial Rojo" -> "labial-rojo")
            ps.setString(2, p.getNombre().toLowerCase().trim().replace(" ", "-"));
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getStock());
            ps.setInt(5, 1); // ID de Categoria
            ps.setInt(6, 1); // ID de Marca

            return ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public ArrayList<Producto> listarTodos() {
        ArrayList<Producto> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, stock FROM Producto";

        try (Connection con = DBManager.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto();
                // Ojo: En tu SQL la columna es 'id', no 'id_producto'
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getBigDecimal("precio"));
                p.setStock(rs.getInt("stock"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Producto buscarPorId(int id) {
        Producto producto = null;
        String sql = "SELECT id, nombre, slug, descripcion, precio, stock, tipoPiel, " +
                "imagen, estado, fechaCreacion, idCategoria, idMarca " +
                "FROM Producto WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar Producto: " + e.getMessage());
        }
        return producto;
    }

    @Override
    public int actualizar(Producto producto) {
        int resultado = 0;
        String sql = "UPDATE Producto SET nombre=?, slug=?, descripcion=?, precio=?, stock=?, " +
                "tipoPiel=?, imagen=?, estado=?, idCategoria=?, idMarca=? WHERE id=?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getSlug());
            ps.setString(3, producto.getDescripcion());
            ps.setBigDecimal(4, producto.getPrecio());
            ps.setInt(5, producto.getStock());
            ps.setString(6, producto.getTipoPiel());
            ps.setString(7, producto.getImagen());
            ps.setInt(8, producto.getEstado());
            ps.setInt(9, producto.getIdCategoria());
            ps.setInt(10, producto.getIdMarca());
            ps.setInt(11, producto.getId());

            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar Producto: " + e.getMessage());
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        String sql = "DELETE FROM Producto WHERE id = ?";

        try (Connection con = DBManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar Producto: " + e.getMessage());
        }
        return resultado;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setSlug(rs.getString("slug"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setTipoPiel(rs.getString("tipoPiel"));
        p.setImagen(rs.getString("imagen"));
        p.setEstado(rs.getInt("estado"));
        p.setIdCategoria(rs.getInt("idCategoria"));
        p.setIdMarca(rs.getInt("idMarca"));
        return p;
    }
}