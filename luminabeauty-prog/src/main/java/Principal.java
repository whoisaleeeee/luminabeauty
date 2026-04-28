import dao.*;
import dao.impl.*;
import luminabeauty.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Principal {
    public static void main(String[] args) {
        System.out.println("INICIANDO PRUEBAS CRUD - SISTEMA LUMINA BEAUTY 2026...\n");

        //PRUEBA JESÚS
        {
//        //==================== 1. PRUEBA USUARIO ==================================
//        // Insertar
//        UsuarioDAO usuarioDao = new UsuarioDAOImpl();
//        Usuario nuevoUsuario = new Usuario();
//        nuevoUsuario.setNombre("Valentina");
//        nuevoUsuario.setApellido("Luna");
//        nuevoUsuario.setCorreo("valentina.luna@beautymail.com"); // Correo único
//        nuevoUsuario.setContrasena("Lumina2026!");
//        nuevoUsuario.setDni("77889900");
//        nuevoUsuario.setTelefono("987654321");
//
//        if (usuarioDao.insertar(nuevoUsuario) > 0) {
//            System.out.println(" 1. Usuario 'Valentina Luna' registrado con éxito.");
//        }
//
//        //leer
//        ArrayList<Usuario> usuarios = usuarioDao.listarTodos();
//
//        System.out.println("\n--- USUARIOS ---");
//        for (Usuario u : usuarios) {
//            System.out.println(u.getId() + " - " + u.getNombre() + " " + u.getApellido());
//        }
//
//        //Update
//
//        Usuario u = usuarioDao.buscarPorId(2);
//
//        // asegurar que no sea null
//        if (u.getContrasena() == null) {
//            u.setContrasena("valor_por_defecto");
//        }
//        u.setTelefono("999888777");
//        usuarioDao.actualizar(u);
//
//
//        //Eliminar
//        usuarioDao.eliminar(1);
//        System.out.println("Usuario eliminado");
//
//        //==================== 2. PRUEBA CATEGORÍA ==================================
//
//        //Insertar
//        CategoriaProductoDAO catDao = new CategoriaProductoDAOImpl();
//        CategoriaProducto nuevaCat = new CategoriaProducto();
//        nuevaCat.setNombre("Cuidado Facial");
//        nuevaCat.setDescripcion("Serums, cremas hidratantes y protectores solares");
//        if (catDao.insertar(nuevaCat) > 0) {
//            System.out.println(" 2. Categoría 'Cuidado Facial' registrada.");
//        }
//
//        //Leer
//        ArrayList<CategoriaProducto> categorias = catDao.listarTodas();
//
//        System.out.println("\n--- CATEGORÍAS ---");
//        for (CategoriaProducto c : categorias) {
//            System.out.println(c.getId() + " - " + c.getNombre());
//        }
//
//        //Actualizar
//        int idBuscado = 1;
//
//        CategoriaProducto cat = null;
//
//        for (CategoriaProducto c : categorias) {
//            if (c.getId() == idBuscado) {
//                cat = c;
//                break;
//            }
//        }
//        if (cat != null) {
//            cat.setDescripcion("Nueva descripción actualizada");
//            catDao.actualizar(cat);
//            System.out.println("Categoría actualizada");
//        } else {
//            System.out.println("Categoría no encontrada");
//        }
//
//
//        //eliminar
//        int idCat = 3;
//        catDao.eliminar(idCat);
//        System.out.println("Categoría eliminada");
//
//        // 3. REGISTRO DE MARCA
//        MarcaDAO marcaDao = new MarcaDAOImpl();
//        Marca nuevaMarca = new Marca();
//        nuevaMarca.setNombre("L'Oréal Paris");
//        nuevaMarca.setDescripcion("Líder mundial en cosmética y belleza");
//
//        if (marcaDao.insertar(nuevaMarca) > 0) {
//            System.out.println(" 3. Marca 'L'Oréal Paris' registrada.");
//        }
//
//
//        // 4. REGISTRO DE PRODUCTO
//        ProductoDAO productoDao = new ProductoDAOImpl();
//        Producto nuevoProd = new Producto();
//        nuevoProd.setNombre("Serum Ácido Hialurónico Revitalift");
//        nuevoProd.setSlug("serum-revitalift-hialuronico");
//        nuevoProd.setPrecio(89.90);
//        nuevoProd.setStock(50);
//        nuevoProd.setTipoPiel("TODOS");
//        nuevoProd.setEstado(1);
//
//        // Suponiendo que el DAO recibe los IDs de las tablas anteriores
//        nuevoProd.setIdCategoria(1);
//        nuevoProd.setIdMarca(1);

//        if (productoDao.insertar(nuevoProd) > 0) {
//            System.out.println(" 4. Producto 'Serum Revitalift' registrado con éxito.");
//        }
        }

        //PRUEBA ALEXANDRA
        {
//            probarCliente(); /* Cliente */
//            crearDireccion(); /* Direccion */
//            probarEmpleado(); /* Empleado */
//            probarCarroDeCompras(); /* CarroDeCompras */
//            probarDetalleCarro(); /* DetalleCarro */
//            probarListaDeDeseos(); /* ListaDeDeseos */
//            probarDetalleLista(); /* DetalleLista */
//            probarValoracion(); /* Valoracion */
//            probarCupon(); /* Cupon */
//            probarPedido(); /* Pedido */
//            probarDetallePedido(); /* DetallePedido */
//            probarMetodoDePago();/* MetodoDePago */
//            probarPago();/* Pago */
//            probarEnvio();/* Envio */
//            probarComprobanteDePago();/* ComprobanteDePago */
        }
    }

    static void probarCliente() {
        ClienteDAOImpl dao = new ClienteDAOImpl();

        // INSERT - FIRST INSERT NUEVO USUARIO O IT DOESNT WORK
        Cliente c = new Cliente();
        c.setId(15);
        c.setPuntosFidelidad(100);
        c.setNivelCliente("BRONCE");
        int res = dao.insertar(c);
        System.out.println("Insertar Cliente:   " + (res > 0 ? "LOGRADO" : "FALLO"));

        // ID SEARCH
        Cliente encontrado = dao.buscarPorId(15); //CAMBIAR ID HERE
        System.out.println("Buscar Cliente:     " + (encontrado != null ? "LOGRADO → nivel: " + encontrado.getNivelCliente() : "FALLO"));

        // UPDATE
        if (encontrado != null) {
            encontrado.setPuntosFidelidad(250);
            encontrado.setNivelCliente("PLATA");
            int upd = dao.actualizar(encontrado);
            System.out.println("Actualizar Cliente: " + (upd > 0 ? "LOGRADO" : "FALLO"));
        }

        // LIST
        int total = dao.listarTodos().size();
        System.out.println("Listar Clientes:" + total + " registro(s)\n");
    }

    static void crearDireccion() {
        DireccionDAOImpl dao = new DireccionDAOImpl();

        Direccion d = new Direccion();
        d.setDireccion("Av. Universitaria 1801");
        d.setCiudad("Lima");
        d.setPais("Peru");
        d.setReferencia("Frente al Parque de las Leyendas");
        d.setCodigoPostal("15074");
        d.setEsPrincipal(true);
        d.setIdCliente(15);

        int res = dao.insertar(d);
        System.out.println("Insertar Direccion: " + (res > 0 ? "YEY!" : "SOS: SUPER MAL") + "\n");
    }

    static void probarEmpleado() {
        EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
        //INSERT - NEED PREVIOUS USER ID
        Empleado e = new Empleado();
        e.setId(1);
        e.setRol("VENDEDOR");
        int res = dao.insertar(e);
        System.out.println("Insertar Empleado:   " + (res > 0 ? "OK" : "FAIL"));
        //ID SEARCH
        Empleado encontrado = dao.buscarPorId(1);
        System.out.println("Buscar Empleado:     " + (encontrado != null ? "OK → rol: " + encontrado.getRol() : "FAIL"));
        //UPDATE
        if (encontrado != null) {
            encontrado.setRol("ADMIN");
            int upd = dao.actualizar(encontrado);
            System.out.println("Actualizar Empleado: " + (upd > 0 ? "OK" : "FAIL"));
        }
        //LIST
        int total = dao.listarTodos().size();
        System.out.println("Listar Empleados: " + total + " registro(s)");
        //DELETE
        int del = dao.eliminar(1, false);
        System.out.println("Eliminar Empleado:   " + (del > 0 ? "OK" : "FAIL") + "\n");
    }

    static void probarCarroDeCompras() { /* depends on Cliente */
        CarroDeComprasDAOImpl dao = new CarroDeComprasDAOImpl();
        //INSERT - NEEDS ID CLIENTE
        CarroDeCompras carro = new CarroDeCompras();
        carro.setFechaCreacion(LocalDateTime.now());
        carro.setIdCliente(15);
        int res = dao.insertar(carro);
        System.out.println("Insertar Carro:   " + (res > 0 ? "OK" : "FAIL"));
        //ID SEARCH
        CarroDeCompras encontrado = dao.buscarPorId(1);
        System.out.println("Buscar Carro:     " + (encontrado != null ? "OK → idCliente: " + encontrado.getIdCliente() : "FAIL"));
        //UPDATE
        if (encontrado != null) {
            encontrado.setFechaCreacion(LocalDateTime.now());
            int upd = dao.actualizar(encontrado);
            System.out.println("Actualizar Carro: " + (upd > 0 ? "OK" : "FAIL"));
        }
        //LIST
        int total = dao.listarTodos().size();
        System.out.println("Listar Carros:  " + total + " registro(s)\n");

        // NOT DELETE, because Pedido depends on it.
    }

    static void probarDetalleCarro() {
        DetalleCarroDAOImpl dao = new DetalleCarroDAOImpl();

        ProductoDAOImpl productoDAO = new ProductoDAOImpl();
        int idProduct = 1;
        Producto producto = productoDAO.buscarPorId(idProduct);

        if (producto == null) {
            System.out.println("No se encuentra el Producto id=" + idProduct+". Exit prueba.\n");
            return;
        }

        // INSERT
        DetalleCarro det = new DetalleCarro();
        det.setCantidad(2);
        det.setPrecioUnitario(producto.getPrecio());
        det.setIdCarro(1);
        det.setIdProducto(producto.getId());

        int res = dao.insertar(det);
        System.out.println("Insertar DetalleCarro:   " + (res > 0 ? "OK" : "FAIL"));

        // SEARCH
        DetalleCarro encontrado = dao.buscarPorId(1);
        System.out.println("Buscar DetalleCarro:     " + (encontrado != null
                ? "OK → cantidad: " + encontrado.getCantidad() + " | precio: " + encontrado.getPrecioUnitario()
                : "FAIL"));

        // UPDATE
        if (encontrado != null) {
            encontrado.setCantidad(3);
            encontrado.setPrecioUnitario(producto.getPrecio());
            int upd = dao.actualizar(encontrado);
            System.out.println("Actualizar DetalleCarro: " + (upd > 0 ? "OK" : "FAIL"));
        }

        // LIST
        int total = dao.listarTodos().size();
        System.out.println("Listar DetalleCarro:     " + total + " registro(s)");

        // DELETE
        int del = dao.eliminar(1);
        System.out.println("Eliminar DetalleCarro:   " + (del > 0 ? "OK" : "FAIL") + "\n");
    }

    static void probarListaDeDeseos() { /* depends on Cliente*/
        ListaDeDeseosDAOImpl dao = new ListaDeDeseosDAOImpl();
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        int idCliente = 15;
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        if (cliente == null) {
            System.out.println("No se encuentra el Cliente id="+idCliente+". Exit prueba.\n");
            return;
        }
        //INSERT
        ListaDeDeseos lista = new ListaDeDeseos();
        lista.setIdCliente(cliente.getId());
        lista.setFechaCreacion(LocalDateTime.now());
        lista.setFechaActualizacion(null);
        int res = dao.insertar(lista);
        System.out.println("Insertar Lista:   " + (res > 0 ? "OK" : "FAIL"));
        //ID SEARCH
        ListaDeDeseos encontrada = dao.buscarPorId(1);
        System.out.println("Buscar Lista:     " + (encontrada != null
                ? "OK -> idCliente: " + encontrada.getIdCliente() : "FAIL"));
        //UPDATE
        if (encontrada != null) {
            encontrada.setFechaActualizacion(LocalDateTime.now());
            int upd = dao.actualizar(encontrada);
            System.out.println("Actualizar Lista: " + (upd > 0 ? "OK" : "FAIL"));
        }
        //LIST
        int total = dao.listarTodos().size();
        System.out.println("Listar Listas:    " + total + " registro(s)\n");
        // NOT DELETE, because DetalleLista depends on it.
    }
}

