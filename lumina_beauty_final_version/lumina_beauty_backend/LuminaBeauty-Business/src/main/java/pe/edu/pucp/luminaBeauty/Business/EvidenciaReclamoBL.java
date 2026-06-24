package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.EvidenciaReclamo;

import java.util.ArrayList;

public interface EvidenciaReclamoBL {

    EvidenciaReclamo registrarEvidenciaReclamo(EvidenciaReclamo evidencia) throws Exception;

    EvidenciaReclamo actualizarEvidenciaReclamo(EvidenciaReclamo evidencia) throws Exception;

    void eliminarEvidenciaReclamo(int idEvidenciaReclamo) throws Exception;

    EvidenciaReclamo buscarEvidenciaReclamo(int idEvidenciaReclamo) throws Exception;

    ArrayList<EvidenciaReclamo> listarEvidenciasReclamo() throws Exception;

    ArrayList<EvidenciaReclamo> listarEvidenciasPorReclamo(int idReclamo) throws Exception;

    ArrayList<EvidenciaReclamo> listarEvidenciasPorTipo(String tipoArchivo) throws Exception;

    ArrayList<EvidenciaReclamo> listarEvidenciasPorCliente(int idCliente) throws Exception;

    ArrayList<EvidenciaReclamo> listarEvidenciasPorEmpleado(int idEmpleado) throws Exception;
}
