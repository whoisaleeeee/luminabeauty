package pe.edu.pucp.luminaBeauty.DAO.impl;

import pe.edu.pucp.luminaBeauty.DAO.ValoracionDAO;
import pe.edu.pucp.luminaBeauty.Model.*;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.sql.*;
import java.util.ArrayList;

public class ValoracionDAOImpl implements ValoracionDAO {

    @Override
    public Valoracion insertar(Valoracion valoracion) throws Exception {
        String sql = """
                INSERT INTO valoracion(
                    id_cliente,
                    id_producto,
                    id_detalle_pedido,
                    calificacion,
                    comentario,
                    estado,
                    respuesta_tienda,
                    respondido_por,
                    respondido_en
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, valoracion.getCliente().getId_usuario());
            stmt.setInt(2, valoracion.getProducto().getId_producto());
            stmt.setInt(3, valoracion.getDetallePedido().getId_detalle_pedido());
            stmt.setInt(4, valoracion.getCalificacion());
            stmt.setString(5, valoracion.getComentario());
            stmt.setString(6, valoracion.getEstado());

            if (valoracion.getRespuesta_tienda() == null) {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.INTEGER);
                stmt.setNull(9, Types.TIMESTAMP);
            } else {
                stmt.setString(7, valoracion.getRespuesta_tienda());
                stmt.setInt(8, valoracion.getRespondido_por().getId_usuario());
                stmt.setTimestamp(9, Timestamp.valueOf(valoracion.getRespondido_en()));
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    valoracion.setId_valoracion(rs.getInt(1));
                }
            }

            return valoracion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminar(Valoracion valoracion) throws Exception {
        String sql = """
                DELETE FROM valoracion
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, valoracion.getId_valoracion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la valoración con ID: " + valoracion.getId_valoracion());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Valoracion buscarPorId(Integer id) throws Exception {
        String sql = """
                SELECT id_valoracion,
                       id_cliente,
                       id_producto,
                       id_detalle_pedido,
                       calificacion,
                       comentario,
                       estado,
                       respuesta_tienda,
                       respondido_por,
                       respondido_en,
                       creado_en,
                       actualizado_en
                FROM valoracion
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearValoracion(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Valoracion actualizar(Valoracion valoracion) throws Exception {
        String sql = """
                UPDATE valoracion
                SET id_cliente = ?,
                    id_producto = ?,
                    id_detalle_pedido = ?,
                    calificacion = ?,
                    comentario = ?,
                    estado = ?,
                    respuesta_tienda = ?,
                    respondido_por = ?,
                    respondido_en = ?
                WHERE id_valoracion = ?
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, valoracion.getCliente().getId_usuario());
            stmt.setInt(2, valoracion.getProducto().getId_producto());
            stmt.setInt(3, valoracion.getDetallePedido().getId_detalle_pedido());
            stmt.setInt(4, valoracion.getCalificacion());
            stmt.setString(5, valoracion.getComentario());
            stmt.setString(6, valoracion.getEstado());

            if (valoracion.getRespuesta_tienda() == null) {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.INTEGER);
                stmt.setNull(9, Types.TIMESTAMP);
            } else {
                stmt.setString(7, valoracion.getRespuesta_tienda());
                stmt.setInt(8, valoracion.getRespondido_por().getId_usuario());
                stmt.setTimestamp(9, Timestamp.valueOf(valoracion.getRespondido_en()));
            }

            stmt.setInt(10, valoracion.getId_valoracion());

            int filas = stmt.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException("No se encontró la valoración con ID: " + valoracion.getId_valoracion());
            }

            return valoracion;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Valoracion> listarTodos() throws Exception {
        ArrayList<Valoracion> valoraciones = new ArrayList<>();

        String sql = """
                SELECT id_valoracion,
                       id_cliente,
                       id_producto,
                       id_detalle_pedido,
                       calificacion,
                       comentario,
                       estado,
                       respuesta_tienda,
                       respondido_por,
                       respondido_en,
                       creado_en,
                       actualizado_en
                FROM valoracion
                """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                valoraciones.add(mapearValoracion(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return valoraciones;
    }

    private Valoracion mapearValoracion(ResultSet rs) throws SQLException {
        Valoracion valoracion = new Valoracion();

        valoracion.setId_valoracion(rs.getInt("id_valoracion"));
        valoracion.setCalificacion(rs.getInt("calificacion"));
        valoracion.setComentario(rs.getString("comentario"));
        valoracion.setEstado(rs.getString("estado"));
        valoracion.setRespuesta_tienda(rs.getString("respuesta_tienda"));

        Cliente cliente = new Cliente();
        cliente.setId_usuario(rs.getInt("id_cliente"));
        valoracion.setCliente(cliente);

        Producto producto = new Producto();
        producto.setId_producto(rs.getInt("id_producto"));
        valoracion.setProducto(producto);

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setId_detalle_pedido(rs.getInt("id_detalle_pedido"));
        valoracion.setDetallePedido(detallePedido);

        int idEmpleado = rs.getInt("respondido_por");
        if (!rs.wasNull()) {
            Empleado empleado = new Empleado();
            empleado.setId_usuario(idEmpleado);
            valoracion.setRespondido_por(empleado);
        }

        Timestamp respondidoEn = rs.getTimestamp("respondido_en");
        Timestamp fechaCreacion = rs.getTimestamp("creado_en");
        Timestamp fechaActualizacion = rs.getTimestamp("actualizado_en");

        if (respondidoEn != null) {
            valoracion.setRespondido_en(respondidoEn.toLocalDateTime());
        }

        if (fechaCreacion != null) {
            valoracion.setFecha_creacion(fechaCreacion.toLocalDateTime());
        }

        if (fechaActualizacion != null) {
            valoracion.setFecha_actualizacion(fechaActualizacion.toLocalDateTime());
        }

        return valoracion;
    }

    @Override
    public ArrayList<Valoracion> listarPublicadasPorProducto(
            int idProducto
    ) throws Exception {
        ArrayList<Valoracion> valoraciones = new ArrayList<>();

        String sql = """
            SELECT v.id_valoracion,
                   v.id_cliente,
                   v.id_producto,
                   v.id_detalle_pedido,
                   v.calificacion,
                   v.comentario,
                   v.estado,
                   v.respuesta_tienda,
                   v.respondido_por,
                   v.respondido_en,
                   v.creado_en,
                   v.actualizado_en
            FROM valoracion v
            WHERE v.id_producto = ?
              AND v.estado = 'PUBLICADA'
            ORDER BY v.creado_en DESC, v.id_valoracion DESC
            """;

        Connection connection = TransactionContext.getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    valoraciones.add(mapearValoracion(rs));
                }
            }

            return valoraciones;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}