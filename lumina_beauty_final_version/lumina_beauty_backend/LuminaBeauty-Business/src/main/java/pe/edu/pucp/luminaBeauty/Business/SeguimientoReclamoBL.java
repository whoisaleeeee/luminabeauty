package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.SeguimientoReclamo;

import java.util.ArrayList;

public interface SeguimientoReclamoBL {

    SeguimientoReclamo registrarSeguimientoReclamo(SeguimientoReclamo seguimiento) throws Exception;

    SeguimientoReclamo actualizarSeguimientoReclamo(SeguimientoReclamo seguimiento) throws Exception;

    void eliminarSeguimientoReclamo(int idSeguimientoReclamo) throws Exception;

    SeguimientoReclamo buscarSeguimientoReclamo(int idSeguimientoReclamo) throws Exception;

    ArrayList<SeguimientoReclamo> listarSeguimientosReclamo() throws Exception;

    ArrayList<SeguimientoReclamo> listarSeguimientosPorReclamo(int idReclamo) throws Exception;

    ArrayList<SeguimientoReclamo> listarSeguimientosPorTipo(String tipo) throws Exception;

    ArrayList<SeguimientoReclamo> listarSeguimientosPorCliente(int idCliente) throws Exception;

    ArrayList<SeguimientoReclamo> listarSeguimientosPorEmpleado(int idEmpleado) throws Exception;

    SeguimientoReclamo registrarCambioEstadoReclamo(int idReclamo,
                                                    String estadoNuevo,
                                                    String mensaje,
                                                    int idEmpleado) throws Exception;
}

