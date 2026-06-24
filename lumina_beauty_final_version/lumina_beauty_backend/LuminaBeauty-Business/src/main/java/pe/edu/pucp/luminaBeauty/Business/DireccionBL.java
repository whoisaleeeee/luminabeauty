package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Direccion;

import java.util.ArrayList;

public interface DireccionBL {

    Direccion registrarDireccion(Direccion direccion) throws Exception;

    Direccion actualizarDireccion(Direccion direccion) throws Exception;

    void eliminarDireccion(int idDireccion) throws Exception;

    Direccion buscarDireccion(int idDireccion) throws Exception;

    ArrayList<Direccion> listarDirecciones() throws Exception;

    ArrayList<Direccion> listarDireccionesPorCliente(int idCliente) throws Exception;
}
