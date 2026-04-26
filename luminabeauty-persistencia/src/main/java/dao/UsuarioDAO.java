package dao;
import luminabeauty.model.Usuario;
import java.util.ArrayList;
public interface UsuarioDAO {
    int insertar(Usuario usuario);
    ArrayList<Usuario> listarTodos();
    int actualizar(Usuario usuario);

    int eliminar(int id);

    Usuario buscarPorId(int id);
}