package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.MovimientoInventario;

import java.util.ArrayList;

public interface MovimientoInventarioBL {

    MovimientoInventario registrarMovimientoInventario(MovimientoInventario movimiento) throws Exception;

    MovimientoInventario actualizarMovimientoInventario(MovimientoInventario movimiento) throws Exception;

    void eliminarMovimientoInventario(int idMovimientoInventario) throws Exception;

    MovimientoInventario buscarMovimientoInventario(int idMovimientoInventario) throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosInventario() throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosPorProducto(int idProducto) throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosPorTipo(String tipoMovimiento) throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosPorPedido(int idPedido) throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosPorDevolucion(int idDevolucion) throws Exception;

    ArrayList<MovimientoInventario> listarMovimientosPorEmpleado(int idEmpleado) throws Exception;
}

