
package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.Marca;

import java.util.ArrayList;

public interface MarcaBL {

    Marca registrarMarca(Marca marca) throws Exception;

    Marca actualizarMarca(Marca marca) throws Exception;

    void eliminarMarca(int idMarca) throws Exception;

    Marca buscarMarca(int idMarca) throws Exception;

    ArrayList<Marca> listarMarcas() throws Exception;

    ArrayList<Marca> buscarMarcasPorNombre(String nombre) throws Exception;
}
