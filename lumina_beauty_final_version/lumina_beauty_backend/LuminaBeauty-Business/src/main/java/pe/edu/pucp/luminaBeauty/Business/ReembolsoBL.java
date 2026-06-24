package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Reembolso;

import java.util.ArrayList;

public interface ReembolsoBL {

    Reembolso registrarReembolso(Reembolso reembolso) throws Exception;

    Reembolso actualizarReembolso(Reembolso reembolso) throws Exception;

    void eliminarReembolso(int idReembolso) throws Exception;

    Reembolso buscarReembolso(int idReembolso) throws Exception;

    ArrayList<Reembolso> listarReembolsos() throws Exception;

    ArrayList<Reembolso> listarReembolsosPorEstado(String estado) throws Exception;

    ArrayList<Reembolso> listarReembolsosPorPago(int idPago) throws Exception;

    ArrayList<Reembolso> listarReembolsosPorDevolucion(int idDevolucion) throws Exception;

    ArrayList<Reembolso> listarReembolsosPorEmpleado(int idEmpleado) throws Exception;

    Reembolso procesarReembolso(int idReembolso,
                                int idEmpleado,
                                String referenciaTransaccion) throws Exception;

    Reembolso marcarReembolsoFallido(int idReembolso, String motivo) throws Exception;
}
