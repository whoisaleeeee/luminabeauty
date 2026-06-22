-- ============================================================================
-- LUMINA BEAUTY - ELIMINACION COMPLETA DE TABLAS Y TRIGGERS
-- Destino: MySQL Server 8.4.x
-- ADVERTENCIA: Este script elimina de forma permanente todas las tablas,
--             datos, claves foraneas, indices y triggers del esquema.
--             No incluye DROP DATABASE.
-- ============================================================================

USE luminabeauty;

-- Se desactivan temporalmente porque existe una referencia circular entre
-- cliente e direccion (cliente.id_direccion_principal -> direccion y
-- direccion.id_cliente -> cliente). Se restauran al finalizar.
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- 1. TRIGGERS
-- ============================================================================
DROP TRIGGER IF EXISTS trg_cliente_validar_tipo_bi;
DROP TRIGGER IF EXISTS trg_empleado_validar_tipo_bi;
DROP TRIGGER IF EXISTS trg_usuario_proteger_tipo_bu;
DROP TRIGGER IF EXISTS trg_detalle_carrito_actualizar_fecha_ai;
DROP TRIGGER IF EXISTS trg_detalle_carrito_actualizar_fecha_au;
DROP TRIGGER IF EXISTS trg_detalle_carrito_actualizar_fecha_ad;
DROP TRIGGER IF EXISTS trg_categoria_producto_validar_padre_bi;
DROP TRIGGER IF EXISTS trg_categoria_producto_validar_padre_bu;
DROP TRIGGER IF EXISTS trg_pago_asignar_completado_bi;
DROP TRIGGER IF EXISTS trg_pago_asignar_completado_bu;

-- ============================================================================
-- 2. TABLAS TRANSACCIONALES Y TABLAS HIJAS
-- ============================================================================
DROP TABLE IF EXISTS uso_cupon;
DROP TABLE IF EXISTS detalle_pedido;
DROP TABLE IF EXISTS comprobante_pago;
DROP TABLE IF EXISTS envio;
DROP TABLE IF EXISTS pago;
DROP TABLE IF EXISTS pedido;

DROP TABLE IF EXISTS valoracion;
DROP TABLE IF EXISTS detalle_lista_deseos;
DROP TABLE IF EXISTS lista_deseos;
DROP TABLE IF EXISTS detalle_carrito;
DROP TABLE IF EXISTS carrito;

-- ============================================================================
-- 3. CATALOGO, CONFIGURACION Y PERFILES
-- ============================================================================
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS tarifa_envio;
DROP TABLE IF EXISTS metodo_pago;
DROP TABLE IF EXISTS cupon;
DROP TABLE IF EXISTS marca;
DROP TABLE IF EXISTS categoria_producto;

DROP TABLE IF EXISTS direccion;
DROP TABLE IF EXISTS empleado;
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS usuario;

SET FOREIGN_KEY_CHECKS = 1;

-- Verificacion opcional:
-- SHOW TABLES;
