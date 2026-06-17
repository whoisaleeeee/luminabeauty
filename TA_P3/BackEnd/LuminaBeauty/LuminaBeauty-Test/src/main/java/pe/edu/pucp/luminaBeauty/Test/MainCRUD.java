package pe.edu.pucp.luminaBeauty.Test;

import pe.edu.pucp.luminaBeauty.DAO.*;
import pe.edu.pucp.luminaBeauty.DAO.impl.*;
import pe.edu.pucp.luminaBeauty.Model.*;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MainCRUD {

    public static void main(String[] args) {
        try {
            long t = System.currentTimeMillis();

            Cliente cliente = probarCliente(t);
            Empleado empleado = probarEmpleado(t);
            Direccion direccion = probarDireccion(cliente);
            CategoriaProducto categoria = probarCategoria(t);
            Marca marca = probarMarca(t);
            Producto producto = probarProducto(categoria, marca, t);
            CarroDeCompras carro = probarCarro(cliente);
            DetalleCarro detalleCarro = probarDetalleCarro(carro, producto);
            ListaDeDeseos lista = probarListaDeseos(cliente);
            DetalleLista detalleLista = probarDetalleLista(lista, producto);
            Valoracion valoracion = probarValoracion(cliente, producto);
            Cupon cupon = probarCupon(t);
            Pedido pedido = probarPedido(carro, cupon);
            DetallePedido detallePedido = probarDetallePedido(pedido, producto);
            MetodoDePago metodo = probarMetodoPago(t);
            Pago pago = probarPago(pedido, metodo);
            Envio envio = probarEnvio(pedido, direccion);
            ComprobanteDePago comprobante = probarComprobante(pedido, t);

            eliminarComprobante(comprobante);
            eliminarEnvio(envio);
            eliminarPago(pago);
            eliminarMetodoPago(metodo);
            eliminarDetallePedido(detallePedido);
            eliminarPedido(pedido);
            eliminarCupon(cupon);
            eliminarValoracion(valoracion);
            eliminarDetalleLista(detalleLista);
            eliminarListaDeseos(lista);
            eliminarDetalleCarro(detalleCarro);
            eliminarCarro(carro);
            eliminarProducto(producto);
            eliminarMarca(marca);
            eliminarCategoria(categoria);
            eliminarDireccion(direccion);
            eliminarEmpleado(empleado);
            eliminarCliente(cliente);

            TransactionContext.commit();

            System.out.println("\n=================================");
            System.out.println("CRUD COMPLETO PROBADO CORRECTAMENTE");
            System.out.println("=================================");

        } catch (Exception e) {
            TransactionContext.rollback();
            System.out.println("\nERROR EN PRUEBAS CRUD:");
            e.printStackTrace();
        } finally {
            TransactionContext.close();
        }
    }

    static Cliente probarCliente(long t) throws Exception {
        System.out.println("\n========== CLIENTE ==========");
        ClienteDAO dao = new ClienteDAOImpl();

        Cliente c = new Cliente();
        c.setNombre("Valentina");
        c.setApellido("Luna");
        c.setCorreo("valentina" + t + "@gmail.com");
        c.setContrasena("123456");
        c.setDni("77889900");
        c.setTelefono("987654321");
        c.setEstado(1);
        c.setPuntosFidelidad(100);
        c.setNivelCliente("BRONCE");

        dao.insertar(c);
        System.out.println("Insertado ID: " + c.getId());

        Cliente buscado = dao.buscarPorId(c.getId());
        System.out.println("Buscado: " + buscado.getNombre());

        buscado.setNivelCliente("PLATA");
        buscado.setPuntosFidelidad(250);
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Empleado probarEmpleado(long t) throws Exception {
        System.out.println("\n========== EMPLEADO ==========");
        EmpleadoDAO dao = new EmpleadoDAOImpl();

        Empleado e = new Empleado();
        e.setNombre("Camila");
        e.setApellido("Torres");
        e.setCorreo("empleado" + t + "@gmail.com");
        e.setContrasena("admin123");
        e.setDni("11223344");
        e.setTelefono("955444333");
        e.setEstado(1);
        e.setRol("VENDEDOR");

        dao.insertar(e);
        System.out.println("Insertado ID: " + e.getIdEmpleado());

        Empleado buscado = dao.buscarPorId(e.getIdEmpleado());
        System.out.println("Buscado: " + buscado.getNombre());

        buscado.setRol("ADMIN");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Direccion probarDireccion(Cliente cliente) throws Exception {
        System.out.println("\n========== DIRECCION ==========");
        DireccionDAO dao = new DireccionDAOImpl();

        Direccion d = new Direccion();
        d.setDireccion("Av. Universitaria 1801");
        d.setCiudad("Lima");
        d.setPais("Peru");
        d.setReferencia("Frente al parque");
        d.setCodigoPostal("15074");
        d.setEsPrincipal(true);
        d.setCliente(cliente);

        dao.insertar(d);
        System.out.println("Insertada ID: " + d.getId());

        Direccion buscada = dao.buscarPorId(d.getId());
        System.out.println("Buscada: " + buscada.getDireccion());

        buscada.setReferencia("Referencia actualizada");
        dao.actualizar(buscada);
        System.out.println("Actualizada");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscada;
    }

    static CategoriaProducto probarCategoria(long t) throws Exception {
        System.out.println("\n========== CATEGORIA ==========");
        CategoriaProductoDAO dao = new CategoriaProductoDAOImpl();

        CategoriaProducto c = new CategoriaProducto();
        c.setNombre("Skincare " + t);
        c.setDescripcion("Productos para cuidado facial");
        c.setIdCategoriaPadre(0);

        dao.insertar(c);
        System.out.println("Insertada ID: " + c.getId());

        CategoriaProducto buscada = dao.buscarPorId(c.getId());
        System.out.println("Buscada: " + buscada.getNombre());

        buscada.setDescripcion("Categoria actualizada");
        dao.actualizar(buscada);
        System.out.println("Actualizada");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscada;
    }

    static Marca probarMarca(long t) throws Exception {
        System.out.println("\n========== MARCA ==========");
        MarcaDAO dao = new MarcaDAOImpl();

        Marca m = new Marca();
        m.setNombre("Lumina Brand " + t);
        m.setDescripcion("Marca ficticia");
        m.setLogo("logo.png");

        dao.insertar(m);
        System.out.println("Insertada ID: " + m.getId());

        Marca buscada = dao.buscarPorId(m.getId());
        System.out.println("Buscada: " + buscada.getNombre());

        buscada.setDescripcion("Descripcion actualizada");
        dao.actualizar(buscada);
        System.out.println("Actualizada");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscada;
    }

    static Producto probarProducto(CategoriaProducto categoria, Marca marca, long t) throws Exception {
        System.out.println("\n========== PRODUCTO ==========");
        ProductoDAO dao = new ProductoDAOImpl();

        Producto p = new Producto();
        p.setNombre("Serum Facial");
        p.setSlug("serum-facial-" + t);
        p.setDescripcion("Serum hidratante");
        p.setPrecio(new BigDecimal("89.90"));
        p.setStock(50);
        p.setTipoPiel("TODOS");
        p.setImagen("serum.png");
        p.setEstado(1);
        p.setCategoria(categoria);
        p.setMarca(marca);

        dao.insertar(p);
        System.out.println("Insertado ID: " + p.getId());

        Producto buscado = dao.buscarPorId(p.getId());
        System.out.println("Buscado: " + buscado.getNombre());

        buscado.setStock(45);
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static CarroDeCompras probarCarro(Cliente cliente) throws Exception {
        System.out.println("\n========== CARRO ==========");
        CarroDeComprasDAO dao = new CarroDeComprasDAOImpl();

        CarroDeCompras c = new CarroDeCompras();
        c.setFechaCreacion(LocalDateTime.now());
        c.setCliente(cliente);

        dao.insertar(c);
        System.out.println("Insertado ID: " + c.getId());

        CarroDeCompras buscado = dao.buscarPorId(c.getId());
        System.out.println("Buscado ID: " + buscado.getId());

        buscado.setFechaCreacion(LocalDateTime.now());
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static DetalleCarro probarDetalleCarro(CarroDeCompras carro, Producto producto) throws Exception {
        System.out.println("\n========== DETALLE CARRO ==========");
        DetalleCarroDAO dao = new DetalleCarroDAOImpl();

        DetalleCarro d = new DetalleCarro();
        d.setCantidad(2);
        d.setPrecioUnitario(producto.getPrecio());
        d.setCarro(carro);
        d.setIdProducto(producto);

        dao.insertar(d);
        System.out.println("Insertado ID: " + d.getId());

        DetalleCarro buscado = dao.buscarPorId(d.getId());
        System.out.println("Buscado cantidad: " + buscado.getCantidad());

        buscado.setCantidad(3);
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static ListaDeDeseos probarListaDeseos(Cliente cliente) throws Exception {
        System.out.println("\n========== LISTA DE DESEOS ==========");
        ListaDeDeseosDAO dao = new ListaDeDeseosDAOImpl();

        ListaDeDeseos l = new ListaDeDeseos();
        l.setCliente(cliente);

        dao.insertar(l);
        System.out.println("Insertada ID: " + l.getId());

        ListaDeDeseos buscada = dao.buscarPorId(l.getId());
        System.out.println("Buscada ID: " + buscada.getId());

        dao.actualizar(buscada);
        System.out.println("Actualizada");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscada;
    }

    static DetalleLista probarDetalleLista(ListaDeDeseos lista, Producto producto) throws Exception {
        System.out.println("\n========== DETALLE LISTA ==========");
        DetalleListaDAO dao = new DetalleListaDAOImpl();

        DetalleLista d = new DetalleLista();
        d.setLista(lista);
        d.setProducto(producto);

        dao.insertar(d);
        System.out.println("Insertado ID: " + d.getId());

        DetalleLista buscado = dao.buscarPorId(d.getId());
        System.out.println("Buscado ID: " + buscado.getId());

        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Valoracion probarValoracion(Cliente cliente, Producto producto) throws Exception {
        System.out.println("\n========== VALORACION ==========");
        ValoracionDAO dao = new ValoracionDAOImpl();

        Valoracion v = new Valoracion();
        v.setCalificacion(5);
        v.setComentario("Excelente producto");
        v.setFecha(LocalDateTime.now());
        v.setCliente(cliente);
        v.setProducto(producto);

        dao.insertar(v);
        System.out.println("Insertada ID: " + v.getId());

        Valoracion buscada = dao.buscarPorId(v.getId());
        System.out.println("Buscada comentario: " + buscada.getComentario());

        buscada.setComentario("Comentario actualizado");
        dao.actualizar(buscada);
        System.out.println("Actualizada");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscada;
    }

    static Cupon probarCupon(long t) throws Exception {
        System.out.println("\n========== CUPON ==========");
        CuponDAO dao = new CuponDAOImpl();

        Cupon c = new Cupon();
        c.setCodigo("LUMINA" + t);
        c.setTipoDescuento("PORCENTAJE");
        c.setValorDescuento(new BigDecimal("10.00"));
        c.setFechaInicio(LocalDateTime.now());
        c.setFechaFin(LocalDateTime.now().plusDays(30));
        c.setEstado("ACTIVO");
        c.setLimiteUso(100);
        c.setUsosActuales(0);

        dao.insertar(c);
        System.out.println("Insertado ID: " + c.getId());

        Cupon buscado = dao.buscarPorId(c.getId());
        System.out.println("Buscado codigo: " + buscado.getCodigo());

        buscado.setUsosActuales(1);
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Pedido probarPedido(CarroDeCompras carro, Cupon cupon) throws Exception {
        System.out.println("\n========== PEDIDO ==========");
        PedidoDAO dao = new PedidoDAOImpl();

        Pedido p = new Pedido();
        p.setFecha(LocalDateTime.now());
        p.setTotal(new BigDecimal("179.80"));
        p.setEstado("PENDIENTE");
        p.setCarroDeCompras(carro);
        p.setCupon(cupon);

        dao.insertar(p);
        System.out.println("Insertado ID: " + p.getId());

        Pedido buscado = dao.buscarPorId(p.getId());
        System.out.println("Buscado estado: " + buscado.getEstado());

        buscado.setEstado("CONFIRMADO");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static DetallePedido probarDetallePedido(Pedido pedido, Producto producto) throws Exception {
        System.out.println("\n========== DETALLE PEDIDO ==========");
        DetallePedidoDAO dao = new DetallePedidoDAOImpl();

        DetallePedido d = new DetallePedido();
        d.setCantidad(2);
        d.setPrecioUnitario(producto.getPrecio());
        d.setSubtotal(producto.getPrecio().multiply(new BigDecimal("2")));
        d.setPedido(pedido);
        d.setProducto(producto);

        dao.insertar(d);
        System.out.println("Insertado ID: " + d.getId());

        DetallePedido buscado = dao.buscarPorId(d.getId());
        System.out.println("Buscado cantidad: " + buscado.getCantidad());

        buscado.setCantidad(3);
        buscado.setSubtotal(producto.getPrecio().multiply(new BigDecimal("3")));
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static MetodoDePago probarMetodoPago(long t) throws Exception {
        System.out.println("\n========== METODO DE PAGO ==========");
        MetodoDePagoDAO dao = new MetodoDePagoDAOImpl();

        MetodoDePago m = new MetodoDePago();
        m.setNombre("Yape " + t);
        m.setDescripcion("Pago por aplicativo móvil");
        m.setIcono("yape.png");

        dao.insertar(m);
        System.out.println("Insertado ID: " + m.getId());

        MetodoDePago buscado = dao.buscarPorId(m.getId());
        System.out.println("Buscado nombre: " + buscado.getNombre());

        buscado.setDescripcion("Descripcion actualizada");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Pago probarPago(Pedido pedido, MetodoDePago metodo) throws Exception {
        System.out.println("\n========== PAGO ==========");
        PagoDAO dao = new PagoDAOImpl();

        Pago p = new Pago();
        p.setMonto(new BigDecimal("179.80"));
        p.setEstado("COMPLETADO");
        p.setFechaPago(LocalDateTime.now());
        p.setPedido(pedido);
        p.setMetodoDePago(metodo);

        dao.insertar(p);
        System.out.println("Insertado ID: " + p.getId());

        Pago buscado = dao.buscarPorId(p.getId());
        System.out.println("Buscado estado: " + buscado.getEstado());

        buscado.setEstado("REEMBOLSADO");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static Envio probarEnvio(Pedido pedido, Direccion direccion) throws Exception {
        System.out.println("\n========== ENVIO ==========");
        EnvioDAO dao = new EnvioDAOImpl();

        Envio e = new Envio();
        e.setFechaEnvio(LocalDateTime.now());
        e.setFechaEntregaEstimada(LocalDateTime.now().plusDays(5));
        e.setFechaEntregaReal(null);
        e.setEstado("PREPARANDO");
        e.setNumeroSeguimiento("TRK" + System.currentTimeMillis());
        e.setPedido(pedido);
        e.setDireccion(direccion);

        dao.insertar(e);
        System.out.println("Insertado ID: " + e.getId());

        Envio buscado = dao.buscarPorId(e.getId());
        System.out.println("Buscado estado: " + buscado.getEstado());

        buscado.setEstado("DESPACHADO");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static ComprobanteDePago probarComprobante(Pedido pedido, long t) throws Exception {
        System.out.println("\n========== COMPROBANTE ==========");
        ComprobanteDePagoDAO dao = new ComprobanteDePagoDAOImpl();

        ComprobanteDePago c = new ComprobanteDePago();
        c.setTipo("BOLETA");
        c.setSerie("B001");
        c.setNumero((int) (t % 100000));
        c.setFechaEmision(LocalDateTime.now());
        c.setPedido(pedido);

        dao.insertar(c);
        System.out.println("Insertado ID: " + c.getId());

        ComprobanteDePago buscado = dao.buscarPorId(c.getId());
        System.out.println("Buscado serie: " + buscado.getSerie());

        buscado.setTipo("TICKET");
        dao.actualizar(buscado);
        System.out.println("Actualizado");

        System.out.println("Total: " + dao.listarTodos().size());
        return buscado;
    }

    static void eliminarComprobante(ComprobanteDePago c) throws Exception {
        new ComprobanteDePagoDAOImpl().eliminar(c);
        System.out.println("Comprobante eliminado");
    }

    static void eliminarEnvio(Envio e) throws Exception {
        new EnvioDAOImpl().eliminar(e);
        System.out.println("Envio eliminado");
    }

    static void eliminarPago(Pago p) throws Exception {
        new PagoDAOImpl().eliminar(p);
        System.out.println("Pago eliminado");
    }

    static void eliminarMetodoPago(MetodoDePago m) throws Exception {
        new MetodoDePagoDAOImpl().eliminar(m);
        System.out.println("MetodoDePago eliminado");
    }

    static void eliminarDetallePedido(DetallePedido d) throws Exception {
        new DetallePedidoDAOImpl().eliminar(d);
        System.out.println("DetallePedido eliminado");
    }

    static void eliminarPedido(Pedido p) throws Exception {
        new PedidoDAOImpl().eliminar(p);
        System.out.println("Pedido eliminado");
    }

    static void eliminarCupon(Cupon c) throws Exception {
        new CuponDAOImpl().eliminar(c);
        System.out.println("Cupon eliminado");
    }

    static void eliminarValoracion(Valoracion v) throws Exception {
        new ValoracionDAOImpl().eliminar(v);
        System.out.println("Valoracion eliminada");
    }

    static void eliminarDetalleLista(DetalleLista d) throws Exception {
        new DetalleListaDAOImpl().eliminar(d);
        System.out.println("DetalleLista eliminado");
    }

    static void eliminarListaDeseos(ListaDeDeseos l) throws Exception {
        new ListaDeDeseosDAOImpl().eliminar(l);
        System.out.println("ListaDeDeseos eliminada");
    }

    static void eliminarDetalleCarro(DetalleCarro d) throws Exception {
        new DetalleCarroDAOImpl().eliminar(d);
        System.out.println("DetalleCarro eliminado");
    }

    static void eliminarCarro(CarroDeCompras c) throws Exception {
        new CarroDeComprasDAOImpl().eliminar(c);
        System.out.println("Carro eliminado");
    }

    static void eliminarProducto(Producto p) throws Exception {
        new ProductoDAOImpl().eliminar(p);
        System.out.println("Producto eliminado");
    }

    static void eliminarMarca(Marca m) throws Exception {
        new MarcaDAOImpl().eliminar(m);
        System.out.println("Marca eliminada");
    }

    static void eliminarCategoria(CategoriaProducto c) throws Exception {
        new CategoriaProductoDAOImpl().eliminar(c);
        System.out.println("Categoria eliminada");
    }

    static void eliminarDireccion(Direccion d) throws Exception {
        new DireccionDAOImpl().eliminar(d);
        System.out.println("Direccion eliminada");
    }

    static void eliminarEmpleado(Empleado e) throws Exception {
        new EmpleadoDAOImpl().eliminar(e);
        System.out.println("Empleado eliminado");
    }

    static void eliminarCliente(Cliente c) throws Exception {
        new ClienteDAOImpl().eliminar(c);
        System.out.println("Cliente eliminado");
    }
}