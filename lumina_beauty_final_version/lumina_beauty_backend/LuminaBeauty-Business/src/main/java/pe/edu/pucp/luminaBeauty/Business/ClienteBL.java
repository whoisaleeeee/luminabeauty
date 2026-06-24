
        package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Cliente;

import java.util.ArrayList;

public interface ClienteBL {

    Cliente registrarCliente(Cliente cliente) throws Exception;

    Cliente actualizarCliente(Cliente cliente) throws Exception;

    void eliminarCliente(int idCliente) throws Exception;

    Cliente buscarCliente(int idCliente) throws Exception;

    ArrayList<Cliente> listarClientes() throws Exception;

    void sumarPuntos(int idCliente, int puntos) throws Exception;

    void restarPuntos(int idCliente, int puntos) throws Exception;
}

