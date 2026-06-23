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
                INSERT INTO producto(idCategoria, idMarca, nombre, sku, slug, descripcion, precio, stock, tipo_piel, imagen_url, estado)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, producto.getCategoria().getId_categoria());
            stmt.setInt(2, producto.getMarca().getId_marca());
            stmt.setString(3, producto.getNombre());
            stmt.setString(4, producto.getSku());
            stmt.setString(5, producto.getSlug());
            stmt.setString(6, producto.getDescripcion());
            stmt.setBigDecimal(7, producto.getPrecio());
            stmt.setInt(8, producto.getStock());
            stmt.setString(9, producto.getTipoPiel());
            stmt.setString(10, producto.getImagen());
            stmt.setInt(11, producto.getEstado());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId_producto(rs.getString(1));
                }
            }

            return producto;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Producto producto) throws Exception {
//        String sql = """
//                DELETE FROM Producto
//                WHERE id = ?
//                """;

        String sql = """
                  UPDATE producto SET estado = 0
                  WHERE id_producto = ?
                  """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getId_producto());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto buscarPorId(String id) throws Exception {
        String sql = """
                SELECT id_producto, idCategoria, idMarca, nombre, sku, slug, descripcion, precio, stock, tipoPiel, imagen_url, estado, creado_en, actualizado_en
                FROM producto
                WHERE id_producto = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearProducto(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Producto actualizar(Producto producto) throws Exception {
        String sql = """
                UPDATE producto
                SET idCategoria = ?,
                    idMarca = ?,
                    nombre = ?,
                    sku = ?,
                    slug = ?,
                    descripcion = ?,
                    precio = ?,
                    stock = ?,
                    tipo_piel = ?,
                    imagen_url = ?,
                    estado = ?,
                    actualizado_en = ?
                WHERE id_producto = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, producto.getCategoria().getId_categoria());
            stmt.setInt(2, producto.getMarca().getId_marca());
            stmt.setString(3, producto.getNombre());
            stmt.setString(4, producto.getSku());
            stmt.setString(5, producto.getSlug());
            stmt.setString(6, producto.getDescripcion());
            stmt.setBigDecimal(7, producto.getPrecio());
            stmt.setInt(8, producto.getStock());
            stmt.setString(9, producto.getTipoPiel());
            stmt.setString(10, producto.getImagen());
            stmt.setInt(11, producto.getEstado());
            if (producto.getFecha_actualizacion() == null) {
                stmt.setNull(5, Types.TIMESTAMP);
            } else {
                stmt.setTimestamp(5, Timestamp.valueOf(producto.getFecha_actualizacion()));
            }

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
                SELECT id_producto, idCategoria, idMarca, nombre, sku, slug, descripcion, precio, stock, tipo_piel, imagen_url, estado, creado_en, actualizado_en
                FROM producto
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

        producto.setId_producto(rs.getString("id_producto"));

        CategoriaProducto categoria = new CategoriaProducto();
        categoria.setId_categoria(rs.getInt("idCategoria"));
        producto.setCategoria(categoria);

        Marca marca = new Marca();
        marca.setId_marca(rs.getInt("id_marca"));
        producto.setMarca(marca);

        producto.setNombre(rs.getString("nombre"));
        producto.setSku(rs.getString("sku"));
        producto.setSlug(rs.getString("slug"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getBigDecimal("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setTipoPiel(rs.getString("tipo_piel"));
        producto.setImagen(rs.getString("imagen_url"));
        producto.setEstado(rs.getInt("estado"));

        Timestamp fecha_creado = rs.getTimestamp("creando_en");
        Timestamp fecha_actualizado = rs.getTimestamp("actualizado_en");
        if (fecha_creado != null) {
            producto.setFecha_creacion(fecha_creado.toLocalDateTime());
        }
        if (fecha_actualizado != null) {
            producto.setFecha_actualizacion(fecha_actualizado.toLocalDateTime());
        }

        return producto;
    }
}