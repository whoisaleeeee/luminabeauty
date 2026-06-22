package pe.edu.pucp.luminaBeauty.Test;

import pe.edu.pucp.luminaBeauty.Business.*;
import pe.edu.pucp.luminaBeauty.Business.impl.*;
import pe.edu.pucp.luminaBeauty.DAO.*;
import pe.edu.pucp.luminaBeauty.DAO.impl.*;
import pe.edu.pucp.luminaBeauty.Model.*;
import pe.edu.pucp.luminaBeauty.dbManager.TransactionContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MainBusinessTest {

    public static void main(String[] args) {

        try {
            System.out.println("INICIANDO PRUEBAS BUSINESS\n");

            long t = System.currentTimeMillis();

            Cliente cliente = crearClienteBase(t);
            CategoriaProducto categoria = crearCategoriaBase(t);
            Marca marca = crearMarcaBase(t);
            Producto producto = crearProductoBase(categoria, marca, t);
            CarroDeCompras carro = crearCarroBase(cliente);
            MetodoDePago metodo = crearMetodoPagoBase(t);
            Direccion direccion = crearDireccionBase(cliente);
            Cupon cupon = crearCuponBase(t);

            probarClienteBL(cliente);
            probarProductoBL(producto);
            probarCuponBL(cupon);
            probarCarroBL(carro, producto);

            Pedido pedido = probarPedidoBL(carro, producto, cupon);

            probarPagoBL(pedido, metodo);
            probarEnvioBL(pedido, direccion);


            System.out.println("TODOS LOS MÉTODOS BUSINESS FUNCIONARON");


        } catch (Exception e) {
            System.out.println("\nERROR EN MAIN BUSINESS:");
            e.printStackTrace();
        }
    }

    static void probarClienteBL(Cliente cliente) throws Exception {
        ClienteBL clienteBL = new ClienteBLImpl();

        clienteBL.sumarPuntos(cliente.getId(), 300);

        System.out.println("ClienteBL.sumarPuntos OK");
    }

    static void probarProductoBL(Producto producto) throws Exception {
        ProductoBL productoBL = new ProductoBLImpl();

        productoBL.validarStock(producto.getId(), 2);
        System.out.println("ProductoBL.validarStock OK");

        Producto productoEncontrado = productoBL.buscarProducto(producto.getId());
        System.out.println("ProductoBL.buscarProducto OK: " + productoEncontrado.getNombre());

        productoBL.descontarStock(producto.getId(), 1);
        System.out.println("ProductoBL.descontarStock OK");
    }

    static void probarCuponBL(Cupon cupon) throws Exception {
        CuponBL cuponBL = new CuponBLImpl();

        cuponBL.validarCupon(cupon);
        System.out.println("CuponBL.validarCupon OK");

        BigDecimal totalConDescuento =
                cuponBL.aplicarDescuento(cupon, new BigDecimal("100.00"));

        System.out.println("CuponBL.aplicarDescuento OK: " + totalConDescuento);
    }

    static void probarCarroBL(CarroDeCompras carro, Producto producto) throws Exception {
        CarroBL carroBL = new CarroBLImpl();

        carroBL.agregarProducto(carro, producto, 2);

        System.out.println("CarroBL.agregarProducto OK");
    }

    static Pedido probarPedidoBL(CarroDeCompras carro, Producto producto, Cupon cupon) throws Exception {
        PedidoBL pedidoBL = new PedidoBLImpl();

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setCarroDeCompras(carro);
        pedido.setCupon(cupon);

        DetallePedido detalle = new DetallePedido();
        detalle.setProducto(producto);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio().multiply(new BigDecimal("2")));
        detalle.setPedido(pedido);

        pedido.getDetalles().add(detalle);
        pedido.setTotal(detalle.getSubtotal());

        Pedido pedidoCreado = pedidoBL.crearPedido(pedido);

        System.out.println("PedidoBL.crearPedido OK: " + pedidoCreado.getId());

        return pedidoCreado;
    }

    static void probarPagoBL(Pedido pedido, MetodoDePago metodo) throws Exception {
        PagoBL pagoBL = new PagoBLImpl();

        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoDePago(metodo);
        pago.setMonto(pedido.getTotal());
        pago.setEstado("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());

        Pago pagoRegistrado = pagoBL.registrarPago(pago);

        System.out.println("PagoBL.registrarPago OK: " + pagoRegistrado.getId());
    }

    static void probarEnvioBL(Pedido pedido, Direccion direccion) throws Exception {
        EnvioBL envioBL = new EnvioBLImpl();

        Envio envio = new Envio();
        envio.setPedido(pedido);
        envio.setDireccion(direccion);
        envio.setEstado("PREPARANDO");
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setFechaEntregaEstimada(LocalDateTime.now().plusDays(5));
        envio.setNumeroSeguimiento("TRK" + System.currentTimeMillis());

        Envio envioCreado = envioBL.crearEnvio(envio);

        envioBL.actualizarEstado(envioCreado.getId(), "DESPACHADO");

        System.out.println("EnvioBL.crearEnvio OK: " + envioCreado.getId());
        System.out.println("EnvioBL.actualizarEstado OK");
    }

    static Cliente crearClienteBase(long t) throws Exception {
        ClienteDAO dao = new ClienteDAOImpl();

        Cliente c = new Cliente();
        c.setNombre("Valentina");
        c.setApellido("Luna");
        c.setCorreo("cliente" + t + "@gmail.com");
        c.setContrasena("123456");
        c.setDni("77889900");
        c.setTelefono("987654321");
        c.setEstado(1);
        c.setPuntosFidelidad(100);
        c.setNivelCliente("BRONCE");

        dao.insertar(c);
        TransactionContext.commit();
        TransactionContext.close();

        return c;
    }

    static CategoriaProducto crearCategoriaBase(long t) throws Exception {
        CategoriaProductoDAO dao = new CategoriaProductoDAOImpl();

        CategoriaProducto c = new CategoriaProducto();
        c.setNombre("Skincare " + t);
        c.setDescripcion("Productos de cuidado facial");
        c.setIdCategoriaPadre(0);

        dao.insertar(c);
        TransactionContext.commit();
        TransactionContext.close();

        return c;
    }

    static Marca crearMarcaBase(long t) throws Exception {
        MarcaDAO dao = new MarcaDAOImpl();

        Marca m = new Marca();
        m.setNombre("Lumina Brand " + t);
        m.setDescripcion("Marca ficticia de belleza");
        m.setLogo("logo.png");

        dao.insertar(m);
        TransactionContext.commit();
        TransactionContext.close();

        return m;
    }

    static Producto crearProductoBase(CategoriaProducto categoria, Marca marca, long t) throws Exception {
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
        TransactionContext.commit();
        TransactionContext.close();

        return p;
    }

    static CarroDeCompras crearCarroBase(Cliente cliente) throws Exception {
        CarroDeComprasDAO dao = new CarroDeComprasDAOImpl();

        CarroDeCompras c = new CarroDeCompras();
        c.setCliente(cliente);
        c.setFechaCreacion(LocalDateTime.now());

        dao.insertar(c);
        TransactionContext.commit();
        TransactionContext.close();

        return c;
    }

    static MetodoDePago crearMetodoPagoBase(long t) throws Exception {
        MetodoDePagoDAO dao = new MetodoDePagoDAOImpl();

        MetodoDePago m = new MetodoDePago();
        m.setNombre("Yape " + t);
        m.setDescripcion("Pago por aplicativo móvil");
        m.setIcono("yape.png");

        dao.insertar(m);
        TransactionContext.commit();
        TransactionContext.close();

        return m;
    }

    static Direccion crearDireccionBase(Cliente cliente) throws Exception {
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
        TransactionContext.commit();
        TransactionContext.close();

        return d;
    }

    static Cupon crearCuponBase(long t) throws Exception {
        CuponDAO dao = new CuponDAOImpl();

        Cupon c = new Cupon();
        c.setCodigo("LUMINA" + t);
        c.setTipoDescuento("PORCENTAJE");
        c.setValorDescuento(new BigDecimal("10.00"));
        c.setFechaInicio(LocalDateTime.now().minusDays(1));
        c.setFechaFin(LocalDateTime.now().plusDays(30));
        c.setEstado("ACTIVO");
        c.setLimiteUso(100);
        c.setUsosActuales(0);

        dao.insertar(c);
        TransactionContext.commit();
        TransactionContext.close();

        return c;
    }
}