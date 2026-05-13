package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cliente;

public interface ClienteBL {
    Cliente registrarCliente(Cliente cliente) throws Exception;
    void sumarPuntos(Integer idCliente, int puntos) throws Exception;
}