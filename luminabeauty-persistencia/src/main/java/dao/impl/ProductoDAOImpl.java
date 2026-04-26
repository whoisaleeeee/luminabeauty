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
}