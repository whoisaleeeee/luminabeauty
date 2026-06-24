
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.ListaDeDeseos;

import java.util.ArrayList;

public interface ListaDeDeseosBL {

    ListaDeDeseos registrarListaDeDeseos(ListaDeDeseos listaDeDeseos) throws Exception;

    ListaDeDeseos actualizarListaDeDeseos(ListaDeDeseos listaDeDeseos) throws Exception;

    void eliminarListaDeDeseos(int idListaDeDeseos) throws Exception;

    ListaDeDeseos buscarListaDeDeseos(int idListaDeDeseos) throws Exception;

    ArrayList<ListaDeDeseos> listarListasDeDeseos() throws Exception;

    ArrayList<ListaDeDeseos> listarListasPorCliente(int idCliente) throws Exception;

    ArrayList<ListaDeDeseos> buscarListasPorNombre(String nombre) throws Exception;
}

