package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Envio;

public interface EnvioBL {
    Envio crearEnvio(Envio envio) throws Exception;
    void actualizarEstado(Integer idEnvio, String nuevoEstado) throws Exception;
}