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

            Cliente cliente = crearCliente(t);
            Empleado empleado = crearEmpleado(t);
            Direccion direccion = crearDireccion(cliente);
            CategoriaProducto categoria = crearCategoria(t);
            Marca marca = crearMarca(t);
            Producto producto = crearProducto(categoria, marca, t);
            CarroDeCompras carro = crearCarro(cliente);
            DetalleCarro detalleCarro = crearDetalleCarro(carro, producto);
            ListaDeDeseos lista = crearListaDeseos(cliente);
            DetalleLista detalleLista = crearDetalleLista(lista, producto);
            Valoracion valoracion = crearValoracion(cliente, producto);
            Cupon cupon = crearCupon(t);
            Pedido pedido = crearPedido(carro, cupon);
            DetallePedido detallePedido = crearDetallePedido(pedido, producto);
            MetodoDePago metodo = crearMetodoPago(t);
            Pago pago = crearPago(pedido, metodo);
            Envio envio = crearEnvio(pedido, direccion);
            ComprobanteDePago comprobante = crearComprobante(pedido, t);

            TransactionContext.commit();

            System.out.println("\n=================================");
            System.out.println("TODOS LOS CRUDS SE PROBARON BIEN");
            System.out.println("=================================");

        } catch (Exception e) {
            TransactionContext.rollback();
            System.out.println("\nERROR EN PRUEBAS CRUD:");
            e.printStackTrace();
        } finally {
            TransactionContext.close();
        }
    }

    static Cliente crearCliente(long t) throws Exception {
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
        System.out.println("Cliente insertado ID: " + c.getId());

        c.setNivelCliente("PLATA");
        c.setPuntosFidelidad(250);
        dao.actualizar(c);

        System.out.println("Clientes registrados: " + dao.listarTodos().size());
        return c;
    }

    static Empleado crearEmpleado(long t) throws Exception {
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
        System.out.println("Empleado insertado ID: " + e.getIdEmpleado());

        e.setRol("ADMIN");
        dao.actualizar(e);

        System.out.println("Empleados registrados: " + dao.listarTodos().size());
        return e;
    }

    static Direccion crearDireccion(Cliente cliente) throws Exception {
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
        System.out.println("Direccion insertada ID: " + d.getId());

        d.setReferencia("Referencia actualizada");
        dao.actualizar(d);

        System.out.println("Direcciones registradas: " + dao.listarTodos().size());
        return d;
    }

    static CategoriaProducto crearCategoria(long t) throws Exception {
        CategoriaProductoDAO dao = new CategoriaProductoDAOImpl();

        CategoriaProducto c = new CategoriaProducto();
        c.setNombre("Skincare " + t);
        c.setDescripcion("Productos para cuidado facial");
        c.setIdCategoriaPadre(0);

        dao.insertar(c);
        System.out.println("Categoria insertada ID: " + c.getId());

        c.setDescripcion("Categoria actualizada");
        dao.actualizar(c);

        System.out.println("Categorias registradas: " + dao.listarTodos().size());
        return c;
    }

    static Marca crearMarca(long t) throws Exception {
        MarcaDAO dao = new MarcaDAOImpl();

        Marca m = new Marca();
        m.setNombre("Lumina Brand " + t);
        m.setDescripcion("Marca ficticia de belleza");
        m.setLogo("logo.png");

        dao.insertar(m);
        System.out.println("Marca insertada ID: " + m.getId());

        m.setDescripcion("Descripcion actualizada");
        dao.actualizar(m);

        System.out.println("Marcas registradas: " + dao.listarTodos().size());
        return m;
    }

    static Producto crearProducto(CategoriaProducto categoria, Marca marca, long t) throws Exception {
        ProductoDAO dao = new ProductoDAOImpl();

        Producto p = new Producto();
        p.setNombre("Serum Facial");
        p.setSlug("serum-facial-" + t);
        p.setDescripcion("Serum hidratante de prueba");
        p.setPrecio(new BigDecimal("89.90"));
        p.setStock(50);
        p.setTipoPiel("TODOS");
        p.setImagen("serum.png");
        p.setEstado(1);
        p.setCategoria(categoria);
        p.setMarca(marca);

        dao.insertar(p);
        System.out.println("Producto insertado ID: " + p.getId());

        p.setStock(45);
        dao.actualizar(p);

        System.out.println("Productos registrados: " + dao.listarTodos().size());
        return p;
    }

    static CarroDeCompras crearCarro(Cliente cliente) throws Exception {
        CarroDeComprasDAO dao = new CarroDeComprasDAOImpl();

        CarroDeCompras c = new CarroDeCompras();
        c.setFechaCreacion(LocalDateTime.now());
        c.setCliente(cliente);

        dao.insertar(c);
        System.out.println("Carro insertado ID: " + c.getId());

        System.out.println("Carros registrados: " + dao.listarTodos().size());
        return c;
    }

    static DetalleCarro crearDetalleCarro(CarroDeCompras carro, Producto producto) throws Exception {
        DetalleCarroDAO dao = new DetalleCarroDAOImpl();

        DetalleCarro d = new DetalleCarro();
        d.setCantidad(2);
        d.setPrecioUnitario(producto.getPrecio());
        d.setCarro(carro);
        d.setIdProducto(producto);

        dao.insertar(d);
        System.out.println("DetalleCarro insertado ID: " + d.getId());

        d.setCantidad(3);
        dao.actualizar(d);

        System.out.println("Detalles carro registrados: " + dao.listarTodos().size());
        return d;
    }

    static ListaDeDeseos crearListaDeseos(Cliente cliente) throws Exception {
        ListaDeDeseosDAO dao = new ListaDeDeseosDAOImpl();

        ListaDeDeseos l = new ListaDeDeseos();
        l.setCliente(cliente);

        dao.insertar(l);
        System.out.println("Lista deseos insertada ID: " + l.getId());

        System.out.println("Listas registradas: " + dao.listarTodos().size());
        return l;
    }

    static DetalleLista crearDetalleLista(ListaDeDeseos lista, Producto producto) throws Exception {
        DetalleListaDAO dao = new DetalleListaDAOImpl();

        DetalleLista d = new DetalleLista();
        d.setLista(lista);
        d.setProducto(producto);

        dao.insertar(d);
        System.out.println("DetalleLista insertado ID: " + d.getId());

        System.out.println("Detalles lista registrados: " + dao.listarTodos().size());
        return d;
    }

    static Valoracion crearValoracion(Cliente cliente, Producto producto) throws Exception {
        ValoracionDAO dao = new ValoracionDAOImpl();

        Valoracion v = new Valoracion();
        v.setCalificacion(5);
        v.setComentario("Excelente producto");
        v.setFecha(LocalDateTime.now());
        v.setCliente(cliente);
        v.setProducto(producto);

        dao.insertar(v);
        System.out.println("Valoracion insertada ID: " + v.getId());

        v.setComentario("Comentario actualizado");
        dao.actualizar(v);

        System.out.println("Valoraciones registradas: " + dao.listarTodos().size());
        return v;
    }

    static Cupon crearCupon(long t) throws Exception {
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
        System.out.println("Cupon insertado ID: " + c.getId());

        c.setUsosActuales(1);
        dao.actualizar(c);

        System.out.println("Cupones registrados: " + dao.listarTodos().size());
        return c;
    }

    static Pedido crearPedido(CarroDeCompras carro, Cupon cupon) throws Exception {
        PedidoDAO dao = new PedidoDAOImpl();

        Pedido p = new Pedido();
        p.setFecha(LocalDateTime.now());
        p.setTotal(new BigDecimal("179.80"));
        p.setEstado("PENDIENTE");
        p.setCarroDeCompras(carro);
        p.setCupon(cupon);

        dao.insertar(p);
        System.out.println("Pedido insertado ID: " + p.getId());

        p.setEstado("CONFIRMADO");
        dao.actualizar(p);

        System.out.println("Pedidos registrados: " + dao.listarTodos().size());
        return p;
    }

    static DetallePedido crearDetallePedido(Pedido pedido, Producto producto) throws Exception {
        DetallePedidoDAO dao = new DetallePedidoDAOImpl();

        DetallePedido d = new DetallePedido();
        d.setCantidad(2);
        d.setPrecioUnitario(producto.getPrecio());
        d.setSubtotal(producto.getPrecio().multiply(new BigDecimal("2")));
        d.setPedido(pedido);
        d.setProducto(producto);

        dao.insertar(d);
        System.out.println("DetallePedido insertado ID: " + d.getId());

        d.setCantidad(3);
        d.setSubtotal(producto.getPrecio().multiply(new BigDecimal("3")));
        dao.actualizar(d);

        System.out.println("Detalles pedido registrados: " + dao.listarTodos().size());
        return d;
    }

    static MetodoDePago crearMetodoPago(long t) throws Exception {
        MetodoDePagoDAO dao = new MetodoDePagoDAOImpl();

        MetodoDePago m = new MetodoDePago();
        m.setNombre("Yape " + t);
        m.setDescripcion("Pago por aplicativo móvil");
        m.setIcono("yape.png");

        dao.insertar(m);
        System.out.println("MetodoPago insertado ID: " + m.getId());

        m.setDescripcion("Descripcion actualizada");
        dao.actualizar(m);

        System.out.println("Metodos registrados: " + dao.listarTodos().size());
        return m;
    }

    static Pago crearPago(Pedido pedido, MetodoDePago metodo) throws Exception {
        PagoDAO dao = new PagoDAOImpl();

        Pago p = new Pago();
        p.setMonto(new BigDecimal("179.80"));
        p.setEstado("COMPLETADO");
        p.setFechaPago(LocalDateTime.now());
        p.setPedido(pedido);
        p.setMetodoDePago(metodo);

        dao.insertar(p);
        System.out.println("Pago insertado ID: " + p.getId());

        System.out.println("Pagos registrados: " + dao.listarTodos().size());
        return p;
    }

    static Envio crearEnvio(Pedido pedido, Direccion direccion) throws Exception {
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
        System.out.println("Envio insertado ID: " + e.getId());

        e.setEstado("DESPACHADO");
        dao.actualizar(e);

        System.out.println("Envios registrados: " + dao.listarTodos().size());
        return e;
    }

    static ComprobanteDePago crearComprobante(Pedido pedido, long t) throws Exception {
        ComprobanteDePagoDAO dao = new ComprobanteDePagoDAOImpl();

        ComprobanteDePago c = new ComprobanteDePago();
        c.setTipo("BOLETA");
        c.setSerie("B001");
        c.setNumero((int) (t % 100000));
        c.setFechaEmision(LocalDateTime.now());
        c.setPedido(pedido);

        dao.insertar(c);
        System.out.println("Comprobante insertado ID: " + c.getId());

        System.out.println("Comprobantes registrados: " + dao.listarTodos().size());
        return c;
    }
}