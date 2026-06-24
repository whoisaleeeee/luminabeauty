package pe.edu.pucp.luminaBeauty.Business.impl;

import pe.edu.pucp.luminaBeauty.Business.ReporteBL;
import pe.edu.pucp.luminaBeauty.DAO.ClienteDAO;
import pe.edu.pucp.luminaBeauty.DAO.EnvioDAO;
import pe.edu.pucp.luminaBeauty.DAO.PagoDAO;
import pe.edu.pucp.luminaBeauty.DAO.PedidoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ProductoDAO;
import pe.edu.pucp.luminaBeauty.DAO.ReclamoDAO;
import pe.edu.pucp.luminaBeauty.DAO.impl.ClienteDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.EnvioDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PagoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.PedidoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ProductoDAOImpl;
import pe.edu.pucp.luminaBeauty.DAO.impl.ReclamoDAOImpl;
import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ReporteBLImpl implements ReporteBL {

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final PagoDAO pagoDAO = new PagoDAOImpl();
    private final ReclamoDAO reclamoDAO = new ReclamoDAOImpl();
    private final EnvioDAO envioDAO = new EnvioDAOImpl();

    @Override
    public int contarClientesActivos() throws Exception {
        try {
            ArrayList<Cliente> clientes = clienteDAO.listarTodos();
            int contador = 0;

            for (Cliente cliente : clientes) {
                if (cliente.getEstado() == 1) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarProductosActivos() throws Exception {
        try {
            ArrayList<Producto> productos = productoDAO.listarTodos();
            int contador = 0;

            for (Producto producto : productos) {
                if (producto.getEstado() == 1) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarPedidosPorEstado(String estado) throws Exception {
        try {
            validarEstadoPedido(estado);

            ArrayList<Pedido> pedidos = pedidoDAO.listarTodos();
            int contador = 0;

            for (Pedido pedido : pedidos) {
                if (pedido.getEstado() != null &&
                        pedido.getEstado().equalsIgnoreCase(estado.trim())) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarPagosPorEstado(String estado) throws Exception {
        try {
            validarEstadoPago(estado);

            ArrayList<Pago> pagos = pagoDAO.listarTodos();
            int contador = 0;

            for (Pago pago : pagos) {
                if (pago.getEstado() != null &&
                        pago.getEstado().equalsIgnoreCase(estado.trim())) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarReclamosPorEstado(String estado) throws Exception {
        try {
            validarEstadoReclamo(estado);

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            int contador = 0;

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getEstado() != null &&
                        reclamo.getEstado().equalsIgnoreCase(estado.trim())) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public int contarEnviosPorEstado(String estado) throws Exception {
        try {
            validarEstadoEnvio(estado);

            ArrayList<Envio> envios = envioDAO.listarTodos();
            int contador = 0;

            for (Envio envio : envios) {
                if (envio.getEstado() != null &&
                        envio.getEstado().equalsIgnoreCase(estado.trim())) {
                    contador++;
                }
            }

            return contador;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public BigDecimal calcularTotalVentasCompletadas() throws Exception {
        try {
            ArrayList<Pago> pagos = pagoDAO.listarTodos();
            BigDecimal total = BigDecimal.ZERO;

            for (Pago pago : pagos) {
                if (pago.getEstado() != null &&
                        pago.getEstado().equalsIgnoreCase("COMPLETADO") &&
                        pago.getMonto() != null) {
                    total = total.add(pago.getMonto());
                }
            }

            return total;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public BigDecimal calcularTotalVentasPorEstadoPedido(String estado) throws Exception {
        try {
            validarEstadoPedido(estado);

            ArrayList<Pedido> pedidos = pedidoDAO.listarTodos();
            BigDecimal total = BigDecimal.ZERO;

            for (Pedido pedido : pedidos) {
                if (pedido.getEstado() != null &&
                        pedido.getEstado().equalsIgnoreCase(estado.trim()) &&
                        pedido.getTotal() != null) {
                    total = total.add(pedido.getTotal());
                }
            }

            return total;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception {
        try {
            if (umbralMinimo < 0) {
                throw new Exception("El umbral mínimo no puede ser negativo.");
            }

            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> resultado = new ArrayList<>();

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() > 0 &&
                        producto.getStock() <= umbralMinimo) {
                    resultado.add(producto);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Producto> listarProductosSinStock() throws Exception {
        try {
            ArrayList<Producto> productos = productoDAO.listarTodos();
            ArrayList<Producto> resultado = new ArrayList<>();

            for (Producto producto : productos) {
                if (producto.getEstado() == 1 &&
                        producto.getStock() <= 0) {
                    resultado.add(producto);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pedido> listarPedidosPorEstado(String estado) throws Exception {
        try {
            validarEstadoPedido(estado);

            ArrayList<Pedido> pedidos = pedidoDAO.listarTodos();
            ArrayList<Pedido> resultado = new ArrayList<>();

            for (Pedido pedido : pedidos) {
                if (pedido.getEstado() != null &&
                        pedido.getEstado().equalsIgnoreCase(estado.trim())) {
                    resultado.add(pedido);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Pago> listarPagosPorEstado(String estado) throws Exception {
        try {
            validarEstadoPago(estado);

            ArrayList<Pago> pagos = pagoDAO.listarTodos();
            ArrayList<Pago> resultado = new ArrayList<>();

            for (Pago pago : pagos) {
                if (pago.getEstado() != null &&
                        pago.getEstado().equalsIgnoreCase(estado.trim())) {
                    resultado.add(pago);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Reclamo> listarReclamosPorEstado(String estado) throws Exception {
        try {
            validarEstadoReclamo(estado);

            ArrayList<Reclamo> reclamos = reclamoDAO.listarTodos();
            ArrayList<Reclamo> resultado = new ArrayList<>();

            for (Reclamo reclamo : reclamos) {
                if (reclamo.getEstado() != null &&
                        reclamo.getEstado().equalsIgnoreCase(estado.trim())) {
                    resultado.add(reclamo);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Envio> listarEnviosPorEstado(String estado) throws Exception {
        try {
            validarEstadoEnvio(estado);

            ArrayList<Envio> envios = envioDAO.listarTodos();
            ArrayList<Envio> resultado = new ArrayList<>();

            for (Envio envio : envios) {
                if (envio.getEstado() != null &&
                        envio.getEstado().equalsIgnoreCase(estado.trim())) {
                    resultado.add(envio);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    @Override
    public ArrayList<Cliente> listarClientesActivos() throws Exception {
        try {
            ArrayList<Cliente> clientes = clienteDAO.listarTodos();
            ArrayList<Cliente> resultado = new ArrayList<>();

            for (Cliente cliente : clientes) {
                if (cliente.getEstado() == 1) {
                    resultado.add(cliente);
                }
            }

            return resultado;

        } finally {
            TransactionContext.close();
        }
    }

    private void validarEstadoPedido(String estado) throws Exception {
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado del pedido es obligatorio.");
        }

        estado = estado.trim().toUpperCase();

        if (!estado.equals("PENDIENTE") &&
                !estado.equals("CONFIRMADO") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("ENVIADO") &&
                !estado.equals("ENTREGADO") &&
                !estado.equals("CANCELADO")) {
            throw new Exception("Estado de pedido no válido.");
        }
    }

    private void validarEstadoPago(String estado) throws Exception {
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado del pago es obligatorio.");
        }

        estado = estado.trim().toUpperCase();

        if (!estado.equals("PENDIENTE") &&
                !estado.equals("COMPLETADO") &&
                !estado.equals("FALLIDO") &&
                !estado.equals("REEMBOLSADO")) {
            throw new Exception("Estado de pago no válido.");
        }
    }

    private void validarEstadoReclamo(String estado) throws Exception {
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado del reclamo es obligatorio.");
        }

        estado = estado.trim().toUpperCase();

        if (!estado.equals("ABIERTO") &&
                !estado.equals("EN_REVISION") &&
                !estado.equals("EN_PROCESO") &&
                !estado.equals("RESUELTO") &&
                !estado.equals("CERRADO") &&
                !estado.equals("RECHAZADO")) {
            throw new Exception("Estado de reclamo no válido.");
        }
    }

    private void validarEstadoEnvio(String estado) throws Exception {
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado del envío es obligatorio.");
        }

        estado = estado.trim().toUpperCase();

        if (!estado.equals("PREPARANDO") &&
                !estado.equals("DESPACHADO") &&
                !estado.equals("EN_TRANSITO") &&
                !estado.equals("ENTREGADO") &&
                !estado.equals("DEVUELTO")) {
            throw new Exception("Estado de envío no válido.");
        }
    }
}
