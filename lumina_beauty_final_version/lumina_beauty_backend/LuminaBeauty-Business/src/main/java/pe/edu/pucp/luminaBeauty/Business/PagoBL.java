package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Pago;

import java.util.ArrayList;

public interface PagoBL {

    Pago registrarPago(Pago pago) throws Exception;

    Pago actualizarPago(Pago pago) throws Exception;

    void eliminarPago(int idPago) throws Exception;

    Pago buscarPago(int idPago) throws Exception;

    ArrayList<Pago> listarPagos() throws Exception;

    ArrayList<Pago> listarPagosPorEstado(String estado) throws Exception;

    ArrayList<Pago> listarPagosPorMetodoPago(int idMetodoPago) throws Exception;

    Pago buscarPagoPorPedido(int idPedido) throws Exception;

    Pago completarPago(int idPago, String referenciaTransaccion) throws Exception;

    Pago marcarPagoFallido(int idPago) throws Exception;

    Pago reembolsarPago(int idPago) throws Exception;
}
