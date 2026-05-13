package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.Model.CategoriaProducto;
import pe.edu.pucp.luminaBeauty.Model.Marca;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ProductoDAOImpl implements ProductoDAO {

    @Override
    public Producto insertar(Producto producto) throws Exception {
        String sql = """
                INSERT INTO Producto(nombre, slug, descripcion, precio, stock, tipoPiel, imagen, estado, idCategoria, idMarca)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getSlug());
            stmt.setString(3, producto.getDescripcion());
            stmt.setBigDecimal(4, producto.getPrecio());
            stmt.setInt(5, producto.getStock());
            stmt.setString(6, producto.getTipoPiel());
            stmt.setString(7, producto.getImagen());
            stmt.setInt(8, producto.getEstado());
            stmt.setInt(9, producto.getCategoria().getId());
            stmt.setInt(10, producto.getMarca().getId());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId(rs.getInt(1));
                }
            }

            return producto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Producto producto) throws Exception {
        String sql = """
                DELETE FROM Producto
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, producto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id, nombre, slug, descripcion, precio, stock, tipoPiel, imagen, estado, idCategoria, idMarca
                FROM Producto
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Producto actualizar(Producto producto) throws Exception {
        String sql = """
                UPDATE Producto
                SET nombre = ?,
                    slug = ?,
                    descripcion = ?,
                    precio = ?,
                    stock = ?,
                    tipoPiel = ?,
                    imagen = ?,
                    estado = ?,
                    idCategoria = ?,
                    idMarca = ?
                WHERE id = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getSlug());
            stmt.setString(3, producto.getDescripcion());
            stmt.setBigDecimal(4, producto.getPrecio());
            stmt.setInt(5, producto.getStock());
            stmt.setString(6, producto.getTipoPiel());
            stmt.setString(7, producto.getImagen());
            stmt.setInt(8, producto.getEstado());
            stmt.setInt(9, producto.getCategoria().getId());
            stmt.setInt(10, producto.getMarca().getId());
            stmt.setInt(11, producto.getId());

            stmt.executeUpdate();

            return producto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Producto> listarTodos() throws Exception {
        ArrayList<Producto> productos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, slug, descripcion, precio, stock, tipoPiel, imagen, estado, idCategoria, idMarca
                FROM Producto
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return productos;
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();

        producto.setId(rs.getInt("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setSlug(rs.getString("slug"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setTipoPiel(rs.getString("tipoPiel"));
        producto.setImagen(rs.getString("imagen"));
        producto.setEstado(rs.getInt("estado"));

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setId(rs.getInt("idCategoria"));
        producto.setCategoria(categoria);

        Marca marca = new Marca();
        marca.setId(rs.getInt("idMarca"));
        producto.setMarca(marca);

        return producto;
    }
}