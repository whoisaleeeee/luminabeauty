USE luminabeauty;

START TRANSACTION;

-- ============================================================================
-- 1. USUARIOS
-- 3 clientes + 3 empleados. Los empleados usan correo @lumina.com.
-- Las contrasenas son hashes BCrypt de prueba.
-- ============================================================================

INSERT INTO usuario
(id_usuario, nombres, apellidos, correo, contrasena_hash, telefono, dni, tipo_usuario, estado)
VALUES
(1, 'Valeria', 'Rojas', 'valeria.rojas@gmail.com', '$2a$10$abcdefghijklmnopqrstuuabcdefghijklmnopqrstuuabcdefghij', '999111222', '70000001', 'CLIENTE', 1),
(2, 'Camila', 'Torres', 'camila.torres@hotmail.com', '$2a$10$bcdefghijklmnopqrstuvubcdefghijklmnopqrstuvabcdefghij', '999222333', '70000002', 'CLIENTE', 1),
(3, 'Lucia', 'Mendoza', 'lucia.mendoza@yahoo.com', '$2a$10$cdefghijklmnopqrstuvvucdefghijklmnopqrstuvvabcdefghij', '999333444', '70000003', 'CLIENTE', 1),
(4, 'Andrea', 'Salazar', 'andrea.salazar@lumina.com', '$2a$10$defghijklmnopqrstuvwudefghijklmnopqrstuvwabcdefghij', '988111222', '70000004', 'EMPLEADO', 1),
(5, 'Diego', 'Fernandez', 'diego.fernandez@lumina.com', '$2a$10$efghijklmnopqrstuvwxuefghijklmnopqrstuvwxabcdefghij', '988222333', '70000005', 'EMPLEADO', 1),
(6, 'Mariana', 'Castillo', 'mariana.castillo@lumina.com', '$2a$10$fghijklmnopqrstuvwxyufghijklmnopqrstuvwxyabcdefghij', '988333444', '70000006', 'EMPLEADO', 1);

INSERT INTO cliente
(id_usuario, tipo_usuario, puntos_fidelidad, nivel_cliente, id_direccion_principal)
VALUES
(1, 'CLIENTE', 120, 'BRONCE', NULL),
(2, 'CLIENTE', 450, 'PLATA', NULL),
(3, 'CLIENTE', 900, 'ORO', NULL);

INSERT INTO empleado
(id_usuario, tipo_usuario, rol)
VALUES
(4, 'EMPLEADO', 'ADMIN'),
(5, 'EMPLEADO', 'VENDEDOR'),
(6, 'EMPLEADO', 'SOPORTE');

-- ============================================================================
-- 2. DIRECCIONES
-- ============================================================================

INSERT INTO direccion
(id_direccion, id_cliente, direccion, ciudad, pais, referencia, codigo_postal)
VALUES
(1, 1, 'Av. Primavera 123, Santiago de Surco', 'Lima', 'Peru', 'Frente a una farmacia', '15039'),
(2, 2, 'Calle Los Jazmines 456, Miraflores', 'Lima', 'Peru', 'Edificio color blanco', '15074'),
(3, 3, 'Av. Ejercito 789, Cayma', 'Arequipa', 'Peru', 'Cerca al centro comercial', '04017');

UPDATE cliente SET id_direccion_principal = 1 WHERE id_usuario = 1;
UPDATE cliente SET id_direccion_principal = 2 WHERE id_usuario = 2;
UPDATE cliente SET id_direccion_principal = 3 WHERE id_usuario = 3;

-- ============================================================================
-- 3. CATALOGO
-- ============================================================================

INSERT INTO categoria_producto
(id_categoria, nombre, descripcion, estado)
VALUES
(1, 'Skincare', 'Productos para cuidado facial y rutina diaria.', 1),
(2, 'Maquillaje', 'Productos de maquillaje para rostro, ojos y labios.', 1),
(3, 'Cuidado capilar', 'Productos para limpieza, tratamiento y cuidado del cabello.', 1);

INSERT INTO marca
(id_marca, nombre, descripcion, logo_url, estado)
VALUES
(1, 'Lumina Skin', 'Marca principal de cuidado de la piel.', 'https://cdn.lumina.com/marcas/lumina-skin.png', 1),
(2, 'Bella Glow', 'Marca especializada en maquillaje.', 'https://cdn.lumina.com/marcas/bella-glow.png', 1),
(3, 'Hair Bloom', 'Marca de cuidado capilar.', 'https://cdn.lumina.com/marcas/hair-bloom.png', 1);

INSERT INTO producto
(id_producto, id_categoria, id_marca, nombre, sku, slug, descripcion, precio, stock, tipo_piel, imagen_url, estado)
VALUES
(1, 1, 1, 'Serum Facial Vitamina C 30ml', 'SKU-SKIN-0001', 'serum-facial-vitamina-c-30ml',
 'Serum antioxidante para iluminar la piel y mejorar la textura.', 79.90, 25, 'TODOS',
 'https://cdn.lumina.com/productos/serum-vitamina-c.png', 1),
(2, 2, 2, 'Labial Mate Rosado Nude', 'SKU-MAKE-0001', 'labial-mate-rosado-nude',
 'Labial de acabado mate con larga duracion.', 39.90, 40, NULL,
 'https://cdn.lumina.com/productos/labial-rosado-nude.png', 1),
(3, 3, 3, 'Mascarilla Capilar Reparadora 250ml', 'SKU-HAIR-0001', 'mascarilla-capilar-reparadora-250ml',
 'Tratamiento nutritivo para cabello seco o maltratado.', 59.90, 18, NULL,
 'https://cdn.lumina.com/productos/mascarilla-reparadora.png', 1);

-- ============================================================================
-- 4. CARRITO Y LISTAS DE DESEOS
-- ============================================================================

INSERT INTO carrito
(id_carrito, id_cliente, recordatorio_enviado_en)
VALUES
(1, 1, NULL),
(2, 2, NULL),
(3, 3, NULL);

INSERT INTO detalle_carrito
(id_detalle_carrito, id_carrito, id_producto, cantidad)
VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 1);

INSERT INTO lista_deseos
(id_lista_deseos, id_cliente, nombre, descripcion)
VALUES
(1, 1, 'Favoritos', 'Productos que Valeria desea comprar pronto.'),
(2, 2, 'Rutina de maquillaje', 'Productos para renovar maquillaje diario.'),
(3, 3, 'Cuidado capilar', 'Productos para reparar cabello seco.');

INSERT INTO detalle_lista_deseos
(id_detalle_lista_deseos, id_lista_deseos, id_producto)
VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3);

-- ============================================================================
-- 5. VALORACIONES Y PROMOCIONES
-- ============================================================================

INSERT INTO valoracion
(id_valoracion, id_cliente, id_producto, calificacion, comentario)
VALUES
(1, 1, 1, 5, 'Textura ligera y buen acabado en la piel.'),
(2, 2, 2, 4, 'Color bonito, aunque requiere retoque despues de varias horas.'),
(3, 3, 3, 5, 'Deja el cabello suave desde el primer uso.');

INSERT INTO cupon
(id_cupon, codigo, tipo_descuento, valor_descuento, fecha_inicio, fecha_fin, limite_uso, estado)
VALUES
(1, 'BIENVENIDA10', 'PORCENTAJE', 10.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 500, 1),
(2, 'LIMA15', 'MONTO_FIJO', 15.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 300, 1),
(3, 'GLOW20', 'PORCENTAJE', 20.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 200, 1);

-- Excepcion: esta tabla solo puede tener 2 filas validas porque zona_envio es ENUM('LIMA', 'PROVINCIA')
-- y ademas es la clave primaria.
INSERT INTO tarifa_envio
(zona_envio, costo_base, monto_minimo_envio_gratis, estado)
VALUES
('LIMA', 10.00, 120.00, 1),
('PROVINCIA', 18.00, 180.00, 1);

INSERT INTO metodo_pago
(id_metodo_pago, nombre, descripcion, icono_url, estado)
VALUES
(1, 'Tarjeta de credito', 'Pago simulado con tarjeta de credito.', 'https://cdn.lumina.com/pagos/tarjeta-credito.png', 1),
(2, 'Tarjeta de debito', 'Pago simulado con tarjeta de debito.', 'https://cdn.lumina.com/pagos/tarjeta-debito.png', 1),
(3, 'Yape', 'Pago simulado mediante billetera digital Yape.', 'https://cdn.lumina.com/pagos/yape.png', 1);

-- ============================================================================
-- 6. PEDIDOS, DETALLES, CUPONES, PAGOS, ENVIOS Y COMPROBANTES
-- ============================================================================

INSERT INTO pedido
(id_pedido, id_cliente, id_cupon, codigo_cupon_aplicado, subtotal_productos, costo_envio, descuento, total, estado, creado_en)
VALUES
(1, 1, 1, 'BIENVENIDA10', 79.90, 10.00, 7.99, 81.91, 'CONFIRMADO', '2026-06-22 10:00:00'),
(2, 2, 2, 'LIMA15', 79.80, 10.00, 15.00, 74.80, 'EN_PROCESO', '2026-06-22 10:30:00'),
(3, 3, 3, 'GLOW20', 59.90, 18.00, 11.98, 65.92, 'ENVIADO', '2026-06-22 11:00:00');

INSERT INTO detalle_pedido
(id_detalle_pedido, id_pedido, id_producto, nombre_producto, sku_producto, cantidad, precio_unitario)
VALUES
(1, 1, 1, 'Serum Facial Vitamina C 30ml', 'SKU-SKIN-0001', 1, 79.90),
(2, 2, 2, 'Labial Mate Rosado Nude', 'SKU-MAKE-0001', 2, 39.90),
(3, 3, 3, 'Mascarilla Capilar Reparadora 250ml', 'SKU-HAIR-0001', 1, 59.90);

INSERT INTO uso_cupon
(id_uso_cupon, id_cupon, id_cliente, id_pedido, usado_en)
VALUES
(1, 1, 1, 1, '2026-06-22 10:00:30'),
(2, 2, 2, 2, '2026-06-22 10:30:30'),
(3, 3, 3, 3, '2026-06-22 11:00:30');

INSERT INTO pago
(id_pago, id_pedido, id_metodo_pago, monto, estado, referencia_transaccion, fecha_pago, fecha_reembolso, creado_en)
VALUES
(1, 1, 1, 81.91, 'COMPLETADO', 'TXN-LUM-000001', '2026-06-22 10:01:00', NULL, '2026-06-22 10:00:10'),
(2, 2, 2, 74.80, 'COMPLETADO', 'TXN-LUM-000002', '2026-06-22 10:31:00', NULL, '2026-06-22 10:30:10'),
(3, 3, 3, 65.92, 'COMPLETADO', 'TXN-LUM-000003', '2026-06-22 11:01:00', NULL, '2026-06-22 11:00:10');

INSERT INTO envio
(id_envio, id_pedido, zona_envio, estado, numero_seguimiento, direccion_envio, ciudad_envio, pais_envio,
 referencia_envio, codigo_postal_envio, fecha_envio, fecha_entrega_estimada, fecha_entrega_real, creado_en)
VALUES
(1, 1, 'LIMA', 'PREPARANDO', NULL, 'Av. Primavera 123, Santiago de Surco', 'Lima', 'Peru',
 'Frente a una farmacia', '15039', NULL, NULL, NULL, '2026-06-22 10:02:00'),
(2, 2, 'LIMA', 'DESPACHADO', 'LUM-ENV-000002', 'Calle Los Jazmines 456, Miraflores', 'Lima', 'Peru',
 'Edificio color blanco', '15074', '2026-06-22 13:00:00', '2026-06-23 18:00:00', NULL, '2026-06-22 10:32:00'),
(3, 3, 'PROVINCIA', 'EN_TRANSITO', 'LUM-ENV-000003', 'Av. Ejercito 789, Cayma', 'Arequipa', 'Peru',
 'Cerca al centro comercial', '04017', '2026-06-22 14:00:00', '2026-06-25 18:00:00', NULL, '2026-06-22 11:02:00');

INSERT INTO comprobante_pago
(id_comprobante, id_pedido, tipo, serie, numero, emitido_en)
VALUES
(1, 1, 'BOLETA', 'B001', '00000001', '2026-06-22 10:03:00'),
(2, 2, 'BOLETA', 'B001', '00000002', '2026-06-22 10:33:00'),
(3, 3, 'FACTURA', 'F001', '00000001', '2026-06-22 11:03:00');

COMMIT;

-- ============================================================================
-- VERIFICACION para cantidad de registros en tablas
-- ============================================================================

SELECT 'usuario' AS tabla, COUNT(*) AS cantidad FROM usuario
UNION ALL SELECT 'cliente', COUNT(*) FROM cliente
UNION ALL SELECT 'empleado', COUNT(*) FROM empleado
UNION ALL SELECT 'direccion', COUNT(*) FROM direccion
UNION ALL SELECT 'categoria_producto', COUNT(*) FROM categoria_producto
UNION ALL SELECT 'marca', COUNT(*) FROM marca
UNION ALL SELECT 'producto', COUNT(*) FROM producto
UNION ALL SELECT 'carrito', COUNT(*) FROM carrito
UNION ALL SELECT 'detalle_carrito', COUNT(*) FROM detalle_carrito
UNION ALL SELECT 'lista_deseos', COUNT(*) FROM lista_deseos
UNION ALL SELECT 'detalle_lista_deseos', COUNT(*) FROM detalle_lista_deseos
UNION ALL SELECT 'valoracion', COUNT(*) FROM valoracion
UNION ALL SELECT 'cupon', COUNT(*) FROM cupon
UNION ALL SELECT 'tarifa_envio', COUNT(*) FROM tarifa_envio
UNION ALL SELECT 'metodo_pago', COUNT(*) FROM metodo_pago
UNION ALL SELECT 'pedido', COUNT(*) FROM pedido
UNION ALL SELECT 'detalle_pedido', COUNT(*) FROM detalle_pedido
UNION ALL SELECT 'uso_cupon', COUNT(*) FROM uso_cupon
UNION ALL SELECT 'pago', COUNT(*) FROM pago
UNION ALL SELECT 'envio', COUNT(*) FROM envio
UNION ALL SELECT 'comprobante_pago', COUNT(*) FROM comprobante_pago;
