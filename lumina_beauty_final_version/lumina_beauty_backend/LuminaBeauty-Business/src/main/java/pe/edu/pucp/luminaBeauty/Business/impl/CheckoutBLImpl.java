package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.CheckoutBL;
import pe.edu.pucp.luminaBeauty.DAO.*;
import pe.edu.pucp.luminaBeauty.DAO.impl.*;
import pe.edu.pucp.luminaBeauty.Model.*;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CheckoutBLImpl implements CheckoutBL {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAOImpl();
    private final EnvioDAO envioDAO = new EnvioDAOImpl();
    private final PagoDAO pagoDAO = new PagoDAOImpl();
    private final MetodoDePagoDAO metodoDePagoDAO = new MetodoDePagoDAOImpl();
    private final UsoCuponDAO usoCuponDAO = new UsoCuponDAOImpl();

    @Override
    public CheckoutResponse procesarCheckout(CheckoutRequest request) throws Exception {
        try {
            validarRequest(request);

            Pedido pedido = request.getPedido();
            Envio envio = request.getEnvio();
            Pago pago = request.getPago();

            Cliente cliente = clienteDAO.buscarPorId(
                    pedido.getCliente().getId_usuario()
            );

            if (cliente == null) {
                throw new Exception("El cliente asociado no existe.");
            }

            MetodoDePago metodo = metodoDePagoDAO.buscarPorId(
                    pago.getMetodoDePago().getId_metodo_pago()
            );

            if (metodo == null) {
                throw new Exception("El método de pago seleccionado no existe.");
            }

            BigDecimal subtotal = BigDecimal.ZERO;

            for (DetallePedido detalle : pedido.getDetalles()) {
                validarDetalle(detalle);

                Producto productoBD = productoDAO.buscarPorId(
                        detalle.getProducto().getId_producto()
                );

                if (productoBD == null) {
                    throw new Exception("El producto no existe.");
                }

                if (productoBD.getStock() < detalle.getCantidad()) {
                    throw new Exception(
                            "No hay stock suficiente para: "
                                    + productoBD.getNombre()
                    );
                }

                detalle.setNombre_producto(productoBD.getNombre());
                detalle.setSku_producto(productoBD.getSku());
                detalle.setPrecioUnitario(productoBD.getPrecio());

                BigDecimal subtotalDetalle = productoBD.getPrecio()
                        .multiply(BigDecimal.valueOf(detalle.getCantidad()));

                subtotal = subtotal.add(subtotalDetalle);
            }

            if (pedido.getCosto_envio() == null) {
                pedido.setCosto_envio(BigDecimal.ZERO);
            }

            if (pedido.getDescuento() == null) {
                pedido.setDescuento(BigDecimal.ZERO);
            }

            pedido.setCliente(cliente);
            pedido.setSubtotal_productos(subtotal);
            pedido.calcularTotal();

            // El pedido queda pendiente de preparación o despacho.
            pedido.setEstado("PENDIENTE");

            if (pedido.getCodigo_pedido() == null
                    || pedido.getCodigo_pedido().trim().isEmpty()) {
                pedido.setCodigo_pedido(generarCodigoPedido());
            }

            Pedido pedidoRegistrado = pedidoDAO.insertar(pedido);

            for (DetallePedido detalle : pedido.getDetalles()) {
                detalle.setPedido(pedidoRegistrado);

                detallePedidoDAO.insertar(detalle);

                Producto productoBD = productoDAO.buscarPorId(
                        detalle.getProducto().getId_producto()
                );

                productoBD.setStock(
                        productoBD.getStock() - detalle.getCantidad()
                );

                productoDAO.actualizar(productoBD);
            }

            envio.setPedido(pedidoRegistrado);
            prepararEnvio(envio);

            Envio envioRegistrado = envioDAO.insertar(envio);

            /*
             * Se inserta primero como PENDIENTE.
             * Así MySQL asigna creado_en correctamente.
             */
            prepararPagoPendiente(
                    pago,
                    pedidoRegistrado,
                    metodo
            );

            Pago pagoRegistrado = pagoDAO.insertar(pago);

            /*
             * Contra entrega (ID 7) queda PENDIENTE sin fecha de pago.
             * Los demás métodos se completan usando la fecha del servidor MySQL.
             */
            boolean esContraEntrega = metodo.getId_metodo_pago() == 7;

            if (!esContraEntrega) {
                completarPagoConFechaBaseDatos(pagoRegistrado);

                pagoRegistrado.setEstado("COMPLETADO");
                pagoRegistrado.setFecha_pago(LocalDateTime.now());
                pagoRegistrado.setFecha_reembolso(null);
            }

            if (pedido.getCupon() != null
                    && pedido.getCupon().getId_cupon() > 0) {

                UsoCupon usoCupon = new UsoCupon();

                usoCupon.setCupon(pedido.getCupon());
                usoCupon.setCliente(cliente);
                usoCupon.setPedido(pedidoRegistrado);

                usoCuponDAO.insertar(usoCupon);
            }

            TransactionContext.commit();

            return CheckoutResponse.exito(
                    pedidoRegistrado,
                    envioRegistrado,
                    pagoRegistrado
            );

        } catch (Exception ex) {
            TransactionContext.rollback();

            throw new Exception(
                    "No se pudo completar el checkout: "
                            + ex.getMessage(),
                    ex
            );

        } finally {
            TransactionContext.close();
        }
    }

    private void prepararPagoPendiente(
            Pago pago,
            Pedido pedido,
            MetodoDePago metodo
    ) {
        pago.setPedido(pedido);
        pago.setMetodoDePago(metodo);
        pago.setMonto(pedido.getTotal());

        // Todo pago inicia pendiente para cumplir el CHECK de la base.
        pago.setEstado("PENDIENTE");
        pago.setFecha_pago(null);
        pago.setFecha_reembolso(null);

        if (pago.getReferencia_transaccion() == null
                || pago.getReferencia_transaccion().trim().isEmpty()) {

            boolean esContraEntrega = metodo.getId_metodo_pago() == 7;

            String referencia = esContraEntrega
                    ? "CONTRA-ENTREGA-" + System.currentTimeMillis()
                    : "SIM-"
                      + normalizarNombreMetodo(metodo.getNombre())
                      + "-"
                      + System.currentTimeMillis();

            pago.setReferencia_transaccion(referencia);
        }
    }

    private String normalizarNombreMetodo(String nombreMetodo) {
        if (nombreMetodo == null || nombreMetodo.trim().isEmpty()) {
            return "PAGO";
        }

        return nombreMetodo
                .trim()
                .toUpperCase()
                .replace(" ", "-");
    }

    private void completarPagoConFechaBaseDatos(Pago pago) throws Exception {
        String sql = """
                UPDATE pago
                SET estado = 'COMPLETADO',
                    fecha_pago = CURRENT_TIMESTAMP,
                    fecha_reembolso = NULL
                WHERE id_pago = ?
                """;

        try (PreparedStatement stmt = TransactionContext
                .getConnection()
                .prepareStatement(sql)) {

            stmt.setInt(1, pago.getId_pago());

            int filas = stmt.executeUpdate();

            if (filas != 1) {
                throw new Exception(
                        "No se pudo completar el pago registrado."
                );
            }

        } catch (SQLException ex) {
            throw new Exception(
                    "No se pudo actualizar el estado del pago.",
                    ex
            );
        }
    }

    private void validarRequest(CheckoutRequest request) throws Exception {
        if (request == null) {
            throw new Exception("La solicitud de checkout es obligatoria.");
        }

        if (request.getPedido() == null
                || request.getPedido().getCliente() == null
                || request.getPedido().getCliente().getId_usuario() <= 0) {
            throw new Exception("El pedido no tiene un cliente válido.");
        }

        if (request.getPedido().getDetalles() == null
                || request.getPedido().getDetalles().isEmpty()) {
            throw new Exception(
                    "El pedido debe tener al menos un producto."
            );
        }

        if (request.getEnvio() == null) {
            throw new Exception("Los datos de envío son obligatorios.");
        }

        if (request.getPago() == null
                || request.getPago().getMetodoDePago() == null
                || request.getPago().getMetodoDePago().getId_metodo_pago() <= 0) {
            throw new Exception("El método de pago es obligatorio.");
        }
    }

    private void validarDetalle(DetallePedido detalle) throws Exception {
        if (detalle == null
                || detalle.getProducto() == null
                || detalle.getProducto().getId_producto() <= 0
                || detalle.getCantidad() <= 0) {
            throw new Exception(
                    "Existe un producto inválido en el pedido."
            );
        }
    }

    private void prepararEnvio(Envio envio) {
        if (envio.getZona_envio() == null
                || envio.getZona_envio().trim().isEmpty()) {
            envio.setZona_envio("LIMA");
        }

        if (envio.getEstado() == null
                || envio.getEstado().trim().isEmpty()) {
            envio.setEstado("PREPARANDO");
        }

        if (envio.getPais_envio() == null
                || envio.getPais_envio().trim().isEmpty()) {
            envio.setPais_envio("Peru");
        }

        if (envio.getNumero_seguimiento() == null
                || envio.getNumero_seguimiento().trim().isEmpty()) {
            envio.setNumero_seguimiento(
                    "LUM-" + System.currentTimeMillis()
            );
        }
    }

    private String generarCodigoPedido() {
        return "P" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
    }
}