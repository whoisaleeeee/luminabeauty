package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cliente;
import pe.edu.pucp.luminaBeauty.Model.Envio;
import pe.edu.pucp.luminaBeauty.Model.Pago;
import pe.edu.pucp.luminaBeauty.Model.Pedido;
import pe.edu.pucp.luminaBeauty.Model.Producto;
import pe.edu.pucp.luminaBeauty.Model.Reclamo;

import java.math.BigDecimal;
import java.util.ArrayList;

public interface ReporteBL {

    int contarClientesActivos() throws Exception;

    int contarProductosActivos() throws Exception;

    int contarPedidosPorEstado(String estado) throws Exception;

    int contarPagosPorEstado(String estado) throws Exception;

    int contarReclamosPorEstado(String estado) throws Exception;

    int contarEnviosPorEstado(String estado) throws Exception;

    BigDecimal calcularTotalVentasCompletadas() throws Exception;

    BigDecimal calcularTotalVentasPorEstadoPedido(String estado) throws Exception;

    ArrayList<Producto> listarProductosConStockBajo(int umbralMinimo) throws Exception;

    ArrayList<Producto> listarProductosSinStock() throws Exception;

    ArrayList<Pedido> listarPedidosPorEstado(String estado) throws Exception;

    ArrayList<Pago> listarPagosPorEstado(String estado) throws Exception;

    ArrayList<Reclamo> listarReclamosPorEstado(String estado) throws Exception;

    ArrayList<Envio> listarEnviosPorEstado(String estado) throws Exception;

    ArrayList<Cliente> listarClientesActivos() throws Exception;
}