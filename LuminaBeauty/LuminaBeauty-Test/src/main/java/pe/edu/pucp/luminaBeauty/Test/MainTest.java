package pe.edu.pucp.luminaBeauty.Test;

import pe.edu.pucp.luminaBeauty.Business.PedidoBL;
import pe.edu.pucp.luminaBeauty.Business.impl.PedidoBLImpl;
import pe.edu.pucp.luminaBeauty.Model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MainTest {
    public static void main(String[] args) {

        try {
            CarroDeCompras carro = new CarroDeCompras();
            carro.setId(1);

            Producto producto = new Producto();
            producto.setId(1);

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(2);
            detalle.setPrecioUnitario(new BigDecimal("50.00"));
            detalle.setSubtotal(new BigDecimal("100.00"));

            ArrayList<DetallePedido> detalles = new ArrayList<>();
            detalles.add(detalle);

            Pedido pedido = new Pedido();
            pedido.setFecha(LocalDateTime.now());
            pedido.setEstado("PENDIENTE");
            pedido.setTotal(new BigDecimal("100.00"));
            pedido.setCarroDeCompras(carro);
            pedido.setCupon(null);
            pedido.setDetalles(detalles);

            PedidoBL pedidoBL = new PedidoBLImpl();
            Pedido pedidoRegistrado = pedidoBL.crearPedido(pedido);

            System.out.println("Pedido registrado correctamente.");
            System.out.println("ID generado: " + pedidoRegistrado.getId());

        } catch (Exception e) {
            System.out.println("Error al registrar pedido:");
            e.printStackTrace();
        }
    }
}