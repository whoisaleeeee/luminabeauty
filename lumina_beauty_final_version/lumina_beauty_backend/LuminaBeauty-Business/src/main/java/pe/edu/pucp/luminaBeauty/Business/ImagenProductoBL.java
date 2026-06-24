package pe.edu.pucp.luminaBeauty.Business;

import pe.edu.pucp.luminaBeauty.Model.ImagenProducto;

import java.util.ArrayList;

public interface ImagenProductoBL {

    ImagenProducto registrarImagenProducto(ImagenProducto imagen) throws Exception;

    ImagenProducto actualizarImagenProducto(ImagenProducto imagen) throws Exception;

    void eliminarImagenProducto(int idImagenProducto) throws Exception;

    ImagenProducto buscarImagenProducto(int idImagenProducto) throws Exception;

    ArrayList<ImagenProducto> listarImagenesProducto() throws Exception;

    ArrayList<ImagenProducto> listarImagenesPorProducto(int idProducto) throws Exception;

    void establecerImagenPrincipal(int idImagenProducto) throws Exception;
}
