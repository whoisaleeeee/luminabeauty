package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Reclamo;

import java.util.ArrayList;

public interface ReclamoBL {

    Reclamo registrarReclamo(Reclamo reclamo) throws Exception;

    Reclamo actualizarReclamo(Reclamo reclamo) throws Exception;

    void eliminarReclamo(int idReclamo) throws Exception;

    Reclamo buscarReclamo(int idReclamo) throws Exception;

    ArrayList<Reclamo> listarReclamos() throws Exception;

    ArrayList<Reclamo> listarReclamosPorCliente(int idCliente) throws Exception;

    ArrayList<Reclamo> listarReclamosPorPedido(int idPedido) throws Exception;

    ArrayList<Reclamo> listarReclamosPorEstado(String estado) throws Exception;

    ArrayList<Reclamo> listarReclamosPorPrioridad(String prioridad) throws Exception;

    Reclamo cambiarEstadoReclamo(int idReclamo, String estadoNuevo) throws Exception;

    Reclamo cambiarPrioridadReclamo(int idReclamo, String prioridadNueva) throws Exception;
}
