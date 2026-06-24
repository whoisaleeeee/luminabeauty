package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Envio;

import java.util.ArrayList;

public interface EnvioBL {

    Envio registrarEnvio(Envio envio) throws Exception;

    Envio actualizarEnvio(Envio envio) throws Exception;

    void eliminarEnvio(int idEnvio) throws Exception;

    Envio buscarEnvio(int idEnvio) throws Exception;

    ArrayList<Envio> listarEnvios() throws Exception;

    ArrayList<Envio> listarEnviosPorEstado(String estado) throws Exception;

    ArrayList<Envio> listarEnviosPorZona(String zonaEnvio) throws Exception;

    Envio buscarEnvioPorPedido(int idPedido) throws Exception;

    Envio despacharEnvio(int idEnvio, String numeroSeguimiento) throws Exception;

    Envio marcarEnvioEnTransito(int idEnvio) throws Exception;

    Envio marcarEnvioEntregado(int idEnvio) throws Exception;

    Envio marcarEnvioDevuelto(int idEnvio) throws Exception;
}
