package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.UsoCupon;

import java.util.ArrayList;

public interface UsoCuponBL {

    UsoCupon registrarUsoCupon(UsoCupon usoCupon) throws Exception;

    UsoCupon actualizarUsoCupon(UsoCupon usoCupon) throws Exception;

    void eliminarUsoCupon(int idUsoCupon) throws Exception;

    UsoCupon buscarUsoCupon(int idUsoCupon) throws Exception;

    ArrayList<UsoCupon> listarUsosCupon() throws Exception;

    ArrayList<UsoCupon> listarUsosPorCliente(int idCliente) throws Exception;

    ArrayList<UsoCupon> listarUsosPorCupon(int idCupon) throws Exception;

    UsoCupon buscarUsoPorPedido(int idPedido) throws Exception;

    boolean clienteYaUsoCupon(int idCliente, int idCupon) throws Exception;
}

