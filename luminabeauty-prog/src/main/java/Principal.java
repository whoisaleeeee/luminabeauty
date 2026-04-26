import dao.CategoriaProductoDAO;
import dao.MarcaDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import dao.impl.CategoriaProductoDAOImpl;
import dao.impl.MarcaDAOImpl;
import dao.impl.ProductoDAOImpl;
import dao.impl.UsuarioDAOImpl;
import luminabeauty.model.*;

import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS CRUD SISTEMA LUMINA BEAUTY 2026 ===");

        //==================== 1. PRUEBA USUARIO ==================================
        // Insertar
        UsuarioDAO usuarioDao = new UsuarioDAOImpl();
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Valentina");
        nuevoUsuario.setApellido("Luna");
        nuevoUsuario.setCorreo("valentina.luna@beautymail.com"); // Correo único
        nuevoUsuario.setContrasena("Lumina2026!");
        nuevoUsuario.setDni("77889900");
        nuevoUsuario.setTelefono("987654321");

        if (usuarioDao.insertar(nuevoUsuario) > 0) {
            System.out.println(" 1. Usuario 'Valentina Luna' registrado con éxito.");
        }

        //leer
        ArrayList<Usuario> usuarios = usuarioDao.listarTodos();

        System.out.println("\n--- USUARIOS ---");
        for (Usuario u : usuarios) {
            System.out.println(u.getId() + " - " + u.getNombre() + " " + u.getApellido());
        }

        //Update

        Usuario u = usuarioDao.buscarPorId(2);

        // asegurar que no sea null
        if (u.getContrasena() == null) {
            u.setContrasena("valor_por_defecto");
        }
        u.setTelefono("999888777");
        usuarioDao.actualizar(u);


        //Eliminar
        usuarioDao.eliminar(1);
        System.out.println("Usuario eliminado");

        //==================== 2. PRUEBA CATEGORÍA ==================================

        //Insertar
        CategoriaProductoDAO catDao = new CategoriaProductoDAOImpl();
        CategoriaProducto nuevaCat = new CategoriaProducto();
        nuevaCat.setNombre("Cuidado Facial");
        nuevaCat.setDescripcion("Serums, cremas hidratantes y protectores solares");
        if (catDao.insertar(nuevaCat) > 0) {
            System.out.println(" 2. Categoría 'Cuidado Facial' registrada.");
        }

        //Leer
        ArrayList<CategoriaProducto> categorias = catDao.listarTodas();

        System.out.println("\n--- CATEGORÍAS ---");
        for (CategoriaProducto c : categorias) {
            System.out.println(c.getId() + " - " + c.getNombre());
        }

        //Actualizar
        int idBuscado = 1;

        CategoriaProducto cat = null;

        for (CategoriaProducto c : categorias) {
            if (c.getId() == idBuscado) {
                cat = c;
                break;
            }
        }
        if (cat != null) {
            cat.setDescripcion("Nueva descripción actualizada");
            catDao.actualizar(cat);
            System.out.println("Categoría actualizada");
        } else {
            System.out.println("Categoría no encontrada");
        }


        //eliminar
        int idCat = 3;
        catDao.eliminar(idCat);
        System.out.println("Categoría eliminada");

        // 3. REGISTRO DE MARCA
        MarcaDAO marcaDao = new MarcaDAOImpl();
        Marca nuevaMarca = new Marca();
        nuevaMarca.setNombre("L'Oréal Paris");
        nuevaMarca.setDescripcion("Líder mundial en cosmética y belleza");

        if (marcaDao.insertar(nuevaMarca) > 0) {
            System.out.println(" 3. Marca 'L'Oréal Paris' registrada.");
        }


        // 4. REGISTRO DE PRODUCTO
        ProductoDAO productoDao = new ProductoDAOImpl();
        Producto nuevoProd = new Producto();
        nuevoProd.setNombre("Serum Ácido Hialurónico Revitalift");
        nuevoProd.setSlug("serum-revitalift-hialuronico");
        nuevoProd.setPrecio(89.90);
        nuevoProd.setStock(50);
        nuevoProd.setTipoPiel("TODOS");
        nuevoProd.setEstado(1);

        // Suponiendo que el DAO recibe los IDs de las tablas anteriores
        nuevoProd.setIdCategoria(1);
        nuevoProd.setIdMarca(1);

//        if (productoDao.insertar(nuevoProd) > 0) {
//            System.out.println(" 4. Producto 'Serum Revitalift' registrado con éxito.");
//        }





    }
}