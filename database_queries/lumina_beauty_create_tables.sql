-- ============================================================================
-- LUMINA BEAUTY - ESQUEMA RELACIONAL PRINCIPAL
-- Motor objetivo: MySQL Server 8.4.8
-- Herramienta: MySQL Workbench 8.x
--
-- Script de creacion de tablas
-- ============================================================================

USE luminabeauty;

-- ============================================================================
-- 1. USUARIOS Y PERFILES
-- ============================================================================

CREATE TABLE usuario (
    id_usuario          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombres             VARCHAR(100) NOT NULL,
    apellidos           VARCHAR(100) NOT NULL,
    correo              VARCHAR(150) NOT NULL,
    contrasena_hash     VARCHAR(255) NOT NULL,
    telefono            VARCHAR(20) NULL,
    dni                 VARCHAR(20) NULL,
    tipo_usuario        ENUM('CLIENTE', 'EMPLEADO') NOT NULL,
    estado              TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    -- Necesaria para las FK compuestas de cliente y empleado.
    CONSTRAINT uq_usuario_id_tipo UNIQUE (id_usuario, tipo_usuario),
    CONSTRAINT uq_usuario_correo UNIQUE (correo),
    CONSTRAINT uq_usuario_dni UNIQUE (dni),
    CONSTRAINT chk_usuario_nombres_no_vacios
        CHECK (CHAR_LENGTH(TRIM(nombres)) > 0),
    CONSTRAINT chk_usuario_apellidos_no_vacios
        CHECK (CHAR_LENGTH(TRIM(apellidos)) > 0),
    CONSTRAINT chk_usuario_correo_no_vacio
        CHECK (CHAR_LENGTH(TRIM(correo)) > 0),
    CONSTRAINT chk_usuario_contrasena_hash_no_vacio
        CHECK (CHAR_LENGTH(TRIM(contrasena_hash)) > 0),
    CONSTRAINT chk_usuario_estado
        CHECK (estado IN (0, 1)),
    CONSTRAINT chk_usuario_correo_por_tipo
        CHECK (
            (tipo_usuario = 'EMPLEADO' AND LOWER(correo) LIKE '%@lumina.com')
            OR
            (tipo_usuario = 'CLIENTE' AND LOWER(correo) NOT LIKE '%@lumina.com')
        )
);

-- Los subtipos se validan sin triggers mediante una FK compuesta:
-- cliente solo puede referenciar a usuario de tipo CLIENTE; empleado, a EMPLEADO.
CREATE TABLE cliente (
    id_usuario                  INT UNSIGNED NOT NULL,
    tipo_usuario                ENUM('CLIENTE', 'EMPLEADO') NOT NULL DEFAULT 'CLIENTE',
    puntos_fidelidad            INT UNSIGNED NOT NULL DEFAULT 0,
    nivel_cliente               ENUM('BRONCE', 'PLATA', 'ORO', 'PLATINO')
                                NOT NULL DEFAULT 'BRONCE',
    id_direccion_principal      INT UNSIGNED NULL,

    CONSTRAINT pk_cliente PRIMARY KEY (id_usuario),
    CONSTRAINT uq_cliente_usuario_tipo UNIQUE (id_usuario, tipo_usuario),
    CONSTRAINT chk_cliente_tipo
        CHECK (tipo_usuario = 'CLIENTE'),
    CONSTRAINT fk_cliente_usuario_tipo
        FOREIGN KEY (id_usuario, tipo_usuario)
        REFERENCES usuario (id_usuario, tipo_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE empleado (
    id_usuario          INT UNSIGNED NOT NULL,
    tipo_usuario        ENUM('CLIENTE', 'EMPLEADO') NOT NULL DEFAULT 'EMPLEADO',
    rol                 ENUM('ADMIN', 'VENDEDOR', 'SOPORTE', 'BODEGUERO')
                        NOT NULL DEFAULT 'SOPORTE',

    CONSTRAINT pk_empleado PRIMARY KEY (id_usuario),
    CONSTRAINT uq_empleado_usuario_tipo UNIQUE (id_usuario, tipo_usuario),
    CONSTRAINT chk_empleado_tipo
        CHECK (tipo_usuario = 'EMPLEADO'),
    CONSTRAINT fk_empleado_usuario_tipo
        FOREIGN KEY (id_usuario, tipo_usuario)
        REFERENCES usuario (id_usuario, tipo_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- ============================================================================
-- 2. DIRECCIONES DEL CLIENTE
-- ============================================================================

CREATE TABLE direccion (
    id_direccion        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cliente          INT UNSIGNED NOT NULL,
    direccion           VARCHAR(255) NOT NULL,
    ciudad              VARCHAR(100) NOT NULL,
    pais                VARCHAR(100) NOT NULL DEFAULT 'Peru',
    referencia          VARCHAR(255) NULL,
    codigo_postal       VARCHAR(20) NULL,
    creado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_direccion PRIMARY KEY (id_direccion),
    CONSTRAINT uq_direccion_id_cliente UNIQUE (id_direccion, id_cliente),
    INDEX idx_direccion_cliente (id_cliente),
    CONSTRAINT chk_direccion_no_vacia
        CHECK (CHAR_LENGTH(TRIM(direccion)) > 0),
    CONSTRAINT chk_direccion_ciudad_no_vacia
        CHECK (CHAR_LENGTH(TRIM(ciudad)) > 0),
    CONSTRAINT chk_direccion_pais_no_vacio
        CHECK (CHAR_LENGTH(TRIM(pais)) > 0),
    CONSTRAINT fk_direccion_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- La FK compuesta garantiza que la direccion principal pertenezca al mismo cliente.
ALTER TABLE cliente
    ADD CONSTRAINT fk_cliente_direccion_principal
        FOREIGN KEY (id_direccion_principal, id_usuario)
        REFERENCES direccion (id_direccion, id_cliente)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT;

-- ============================================================================
-- 3. CATALOGO
-- ============================================================================

-- Categorias planas: evita jerarquias recursivas cuya ausencia de ciclos
-- no puede garantizarse declarativamente sin triggers.
CREATE TABLE categoria_producto (
    id_categoria            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre                  VARCHAR(100) NOT NULL,
    descripcion             TEXT NULL,
    estado                  TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_categoria_producto PRIMARY KEY (id_categoria),
    CONSTRAINT uq_categoria_producto_nombre UNIQUE (nombre),
    CONSTRAINT chk_categoria_producto_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT chk_categoria_producto_estado
        CHECK (estado IN (0, 1))
);

CREATE TABLE marca (
    id_marca            INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre              VARCHAR(100) NOT NULL,
    descripcion         TEXT NULL,
    logo_url            VARCHAR(500) NULL,
    estado              TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_marca PRIMARY KEY (id_marca),
    CONSTRAINT uq_marca_nombre UNIQUE (nombre),
    CONSTRAINT chk_marca_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT chk_marca_estado
        CHECK (estado IN (0, 1))
);

CREATE TABLE producto (
    id_producto         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_categoria        INT UNSIGNED NOT NULL,
    id_marca            INT UNSIGNED NOT NULL,
    nombre              VARCHAR(150) NOT NULL,
    sku                 VARCHAR(50) NOT NULL,
    slug                VARCHAR(200) NOT NULL,
    descripcion         TEXT NULL,
    precio              DECIMAL(12, 2) NOT NULL,
    stock               INT UNSIGNED NOT NULL DEFAULT 0,
    tipo_piel           ENUM('SECA', 'GRASA', 'MIXTA', 'SENSIBLE', 'NORMAL', 'TODOS') NULL,
    imagen_url          VARCHAR(500) NULL,
    estado              TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_producto PRIMARY KEY (id_producto),
    CONSTRAINT uq_producto_sku UNIQUE (sku),
    CONSTRAINT uq_producto_slug UNIQUE (slug),
    INDEX idx_producto_categoria_estado (id_categoria, estado),
    INDEX idx_producto_marca_estado (id_marca, estado),
    INDEX idx_producto_estado_precio (estado, precio),
    CONSTRAINT chk_producto_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT chk_producto_sku_no_vacio
        CHECK (CHAR_LENGTH(TRIM(sku)) > 0),
    CONSTRAINT chk_producto_slug_no_vacio
        CHECK (CHAR_LENGTH(TRIM(slug)) > 0),
    CONSTRAINT chk_producto_precio
        CHECK (precio >= 0),
    CONSTRAINT chk_producto_estado
        CHECK (estado IN (0, 1)),
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categoria_producto (id_categoria)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_producto_marca
        FOREIGN KEY (id_marca)
        REFERENCES marca (id_marca)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- ============================================================================
-- 4. CARRITO Y LISTAS DE DESEOS
-- ============================================================================

-- Cada cliente conserva un unico carrito vigente. Tras un checkout, se eliminan
-- sus detalles; el pedido conserva su propio detalle historico.
-- actualizado_en debe actualizarse en la misma transaccion que cualquier cambio
-- de detalle_carrito
CREATE TABLE carrito (
    id_carrito                  INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cliente                  INT UNSIGNED NOT NULL,
    creado_en                   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,
    recordatorio_enviado_en     DATETIME NULL,

    CONSTRAINT pk_carrito PRIMARY KEY (id_carrito),
    CONSTRAINT uq_carrito_cliente UNIQUE (id_cliente),
    INDEX idx_carrito_recordatorio_actualizado (recordatorio_enviado_en, actualizado_en),
    CONSTRAINT fk_carrito_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE detalle_carrito (
    id_detalle_carrito       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_carrito               INT UNSIGNED NOT NULL,
    id_producto              INT UNSIGNED NOT NULL,
    cantidad                 INT UNSIGNED NOT NULL DEFAULT 1,
    creado_en                DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_detalle_carrito PRIMARY KEY (id_detalle_carrito),
    CONSTRAINT uq_detalle_carrito_producto UNIQUE (id_carrito, id_producto),
    INDEX idx_detalle_carrito_producto (id_producto),
    CONSTRAINT chk_detalle_carrito_cantidad
        CHECK (cantidad > 0),
    CONSTRAINT fk_detalle_carrito_carrito
        FOREIGN KEY (id_carrito)
        REFERENCES carrito (id_carrito)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT fk_detalle_carrito_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- Un cliente puede tener varias listas de deseos, con nombres no repetidos.
CREATE TABLE lista_deseos (
    id_lista_deseos        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cliente             INT UNSIGNED NOT NULL,
    nombre                 VARCHAR(100) NOT NULL,
    descripcion            VARCHAR(255) NULL,
    creado_en              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_lista_deseos PRIMARY KEY (id_lista_deseos),
    CONSTRAINT uq_lista_deseos_cliente_nombre UNIQUE (id_cliente, nombre),
    CONSTRAINT chk_lista_deseos_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT fk_lista_deseos_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE detalle_lista_deseos (
    id_detalle_lista_deseos    INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_lista_deseos            INT UNSIGNED NOT NULL,
    id_producto                INT UNSIGNED NOT NULL,

    CONSTRAINT pk_detalle_lista_deseos PRIMARY KEY (id_detalle_lista_deseos),
    CONSTRAINT uq_detalle_lista_deseos_producto
        UNIQUE (id_lista_deseos, id_producto),
    INDEX idx_detalle_lista_deseos_producto (id_producto),
    CONSTRAINT fk_detalle_lista_deseos_lista
        FOREIGN KEY (id_lista_deseos)
        REFERENCES lista_deseos (id_lista_deseos)
        ON DELETE CASCADE
        ON UPDATE RESTRICT,
    CONSTRAINT fk_detalle_lista_deseos_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- ============================================================================
-- 5. VALORACIONES Y PROMOCIONES
-- ============================================================================

CREATE TABLE valoracion (
    id_valoracion       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cliente          INT UNSIGNED NOT NULL,
    id_producto         INT UNSIGNED NOT NULL,
    calificacion        TINYINT UNSIGNED NOT NULL,
    comentario          TEXT NULL,
    creado_en           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_valoracion PRIMARY KEY (id_valoracion),
    CONSTRAINT uq_valoracion_cliente_producto UNIQUE (id_cliente, id_producto),
    INDEX idx_valoracion_producto (id_producto),
    CONSTRAINT chk_valoracion_calificacion
        CHECK (calificacion BETWEEN 1 AND 5),
    CONSTRAINT fk_valoracion_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_valoracion_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE cupon (
    id_cupon                INT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo                  VARCHAR(50) NOT NULL,
    tipo_descuento          ENUM('PORCENTAJE', 'MONTO_FIJO') NOT NULL,
    valor_descuento         DECIMAL(12, 2) NOT NULL,
    fecha_inicio            DATETIME NOT NULL,
    fecha_fin               DATETIME NOT NULL,
    limite_uso              INT UNSIGNED NULL,
    estado                  TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_cupon PRIMARY KEY (id_cupon),
    CONSTRAINT uq_cupon_codigo UNIQUE (codigo),
    CONSTRAINT chk_cupon_codigo_no_vacio
        CHECK (CHAR_LENGTH(TRIM(codigo)) > 0),
    CONSTRAINT chk_cupon_valor
        CHECK (
            valor_descuento > 0
            AND (
                tipo_descuento = 'MONTO_FIJO'
                OR valor_descuento <= 100
            )
        ),
    CONSTRAINT chk_cupon_fechas
        CHECK (fecha_fin > fecha_inicio),
    CONSTRAINT chk_cupon_limite_uso
        CHECK (limite_uso IS NULL OR limite_uso > 0),
    CONSTRAINT chk_cupon_estado
        CHECK (estado IN (0, 1))
);

-- Configuracion vigente. El importe aplicado se conserva como historico en pedido.costo_envio.
CREATE TABLE tarifa_envio (
    zona_envio                  ENUM('LIMA', 'PROVINCIA') NOT NULL,
    costo_base                  DECIMAL(12, 2) NOT NULL,
    monto_minimo_envio_gratis   DECIMAL(12, 2) NULL,
    estado                      TINYINT UNSIGNED NOT NULL DEFAULT 1,
    actualizado_en              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_tarifa_envio PRIMARY KEY (zona_envio),
    CONSTRAINT chk_tarifa_envio_costo
        CHECK (costo_base >= 0),
    CONSTRAINT chk_tarifa_envio_minimo_gratis
        CHECK (monto_minimo_envio_gratis IS NULL OR monto_minimo_envio_gratis > 0),
    CONSTRAINT chk_tarifa_envio_estado
        CHECK (estado IN (0, 1))
);

CREATE TABLE metodo_pago (
    id_metodo_pago     INT UNSIGNED NOT NULL AUTO_INCREMENT,
    nombre             VARCHAR(100) NOT NULL,
    descripcion        VARCHAR(255) NULL,
    icono_url          VARCHAR(500) NULL,
    estado             TINYINT UNSIGNED NOT NULL DEFAULT 1,
    creado_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                       ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_metodo_pago PRIMARY KEY (id_metodo_pago),
    CONSTRAINT uq_metodo_pago_nombre UNIQUE (nombre),
    CONSTRAINT chk_metodo_pago_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre)) > 0),
    CONSTRAINT chk_metodo_pago_estado
        CHECK (estado IN (0, 1))
);

-- ============================================================================
-- 6. PEDIDOS, PAGOS, ENVIOS Y COMPROBANTES
-- ============================================================================

CREATE TABLE pedido (
    id_pedido               INT UNSIGNED NOT NULL AUTO_INCREMENT,
    codigo_pedido 			VARCHAR(14) NOT NULL,
    id_cliente              INT UNSIGNED NOT NULL,
    id_cupon                INT UNSIGNED NULL,
    codigo_cupon_aplicado   VARCHAR(50) NULL,
    subtotal_productos      DECIMAL(12, 2) NOT NULL,
    costo_envio             DECIMAL(12, 2) NOT NULL DEFAULT 0,
    descuento               DECIMAL(12, 2) NOT NULL DEFAULT 0,
    total                   DECIMAL(12, 2) NOT NULL,
    estado                  ENUM(
                                'PENDIENTE',
                                'CONFIRMADO',
                                'EN_PROCESO',
                                'ENVIADO',
                                'ENTREGADO',
                                'CANCELADO'
                            ) NOT NULL DEFAULT 'PENDIENTE',
    creado_en               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_pedido PRIMARY KEY (id_pedido),
    -- Clave candidata para validar la coherencia de uso_cupon.
    CONSTRAINT uq_pedido_cliente_cupon UNIQUE (id_pedido, id_cliente, id_cupon),
    INDEX idx_pedido_cliente_creado (id_cliente, creado_en),
    INDEX idx_pedido_estado_creado (estado, creado_en),
    CONSTRAINT chk_pedido_subtotal_productos
        CHECK (subtotal_productos >= 0),
    CONSTRAINT chk_pedido_costo_envio
        CHECK (costo_envio >= 0),
    CONSTRAINT chk_pedido_descuento
        CHECK (descuento >= 0 AND descuento <= subtotal_productos),
    CONSTRAINT chk_pedido_total
        CHECK (total = subtotal_productos - descuento + costo_envio),
    CONSTRAINT chk_pedido_cupon_snapshot
        CHECK (
            (id_cupon IS NULL AND codigo_cupon_aplicado IS NULL)
            OR
            (
                id_cupon IS NOT NULL
                AND codigo_cupon_aplicado IS NOT NULL
                AND CHAR_LENGTH(TRIM(codigo_cupon_aplicado)) > 0
            )
        ),
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_usuario)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_pedido_cupon
        FOREIGN KEY (id_cupon)
        REFERENCES cupon (id_cupon)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- precio_unitario, nombre_producto y sku_producto son una fotografia de venta.
-- No se guarda subtotal: se obtiene siempre como cantidad * precio_unitario.
CREATE TABLE detalle_pedido (
    id_detalle_pedido       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_pedido               INT UNSIGNED NOT NULL,
    id_producto             INT UNSIGNED NOT NULL,
    nombre_producto         VARCHAR(150) NOT NULL,
    sku_producto            VARCHAR(50) NOT NULL,
    cantidad                INT UNSIGNED NOT NULL,
    precio_unitario         DECIMAL(12, 2) NOT NULL,

    CONSTRAINT pk_detalle_pedido PRIMARY KEY (id_detalle_pedido),
    CONSTRAINT uq_detalle_pedido_producto UNIQUE (id_pedido, id_producto),
    INDEX idx_detalle_pedido_producto (id_producto),
    CONSTRAINT chk_detalle_pedido_nombre_no_vacio
        CHECK (CHAR_LENGTH(TRIM(nombre_producto)) > 0),
    CONSTRAINT chk_detalle_pedido_sku_no_vacio
        CHECK (CHAR_LENGTH(TRIM(sku_producto)) > 0),
    CONSTRAINT chk_detalle_pedido_cantidad
        CHECK (cantidad > 0),
    CONSTRAINT chk_detalle_pedido_precio_unitario
        CHECK (precio_unitario >= 0),
    CONSTRAINT fk_detalle_pedido_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_detalle_pedido_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto (id_producto)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- Registro de uso efectivo. Un mismo cliente no usa el mismo cupon mas de una vez.
CREATE TABLE uso_cupon (
    id_uso_cupon        INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_cupon            INT UNSIGNED NOT NULL,
    id_cliente          INT UNSIGNED NOT NULL,
    id_pedido           INT UNSIGNED NOT NULL,
    usado_en            DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_uso_cupon PRIMARY KEY (id_uso_cupon),
    CONSTRAINT uq_uso_cupon_pedido UNIQUE (id_pedido),
    CONSTRAINT uq_uso_cupon_cliente UNIQUE (id_cupon, id_cliente),
    INDEX idx_uso_cupon_pedido_cliente_cupon (id_pedido, id_cliente, id_cupon),
    CONSTRAINT fk_uso_cupon_pedido_cliente_cupon
        FOREIGN KEY (id_pedido, id_cliente, id_cupon)
        REFERENCES pedido (id_pedido, id_cliente, id_cupon)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE pago (
    id_pago                         INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_pedido                       INT UNSIGNED NOT NULL,
    id_metodo_pago                  INT UNSIGNED NOT NULL,
    monto                           DECIMAL(12, 2) NOT NULL,
    estado                          ENUM('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO')
                                    NOT NULL DEFAULT 'PENDIENTE',
    referencia_transaccion          VARCHAR(150) NULL,
    fecha_pago                      DATETIME NULL,
    fecha_reembolso                 DATETIME NULL,
    creado_en                       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en                  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                    ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_pago PRIMARY KEY (id_pago),
    -- Un pedido mantiene un unico registro de pago cuyo estado evoluciona.
    CONSTRAINT uq_pago_pedido UNIQUE (id_pedido),
    CONSTRAINT uq_pago_referencia_transaccion UNIQUE (referencia_transaccion),
    INDEX idx_pago_metodo_pago (id_metodo_pago),
    CONSTRAINT chk_pago_monto
        CHECK (monto > 0),
    CONSTRAINT chk_pago_fechas_por_estado
        CHECK (
            (
                estado IN ('PENDIENTE', 'FALLIDO')
                AND fecha_pago IS NULL
                AND fecha_reembolso IS NULL
            )
            OR
            (
                estado = 'COMPLETADO'
                AND fecha_pago IS NOT NULL
                AND fecha_pago >= creado_en
                AND fecha_reembolso IS NULL
            )
            OR
            (
                estado = 'REEMBOLSADO'
                AND fecha_pago IS NOT NULL
                AND fecha_pago >= creado_en
                AND fecha_reembolso IS NOT NULL
                AND fecha_reembolso >= fecha_pago
            )
        ),
    CONSTRAINT fk_pago_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT fk_pago_metodo_pago
        FOREIGN KEY (id_metodo_pago)
        REFERENCES metodo_pago (id_metodo_pago)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE envio (
    id_envio                INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_pedido               INT UNSIGNED NOT NULL,
    zona_envio              ENUM('LIMA', 'PROVINCIA') NOT NULL,
    estado                  ENUM('PREPARANDO', 'DESPACHADO', 'EN_TRANSITO', 'ENTREGADO', 'DEVUELTO')
                            NOT NULL DEFAULT 'PREPARANDO',
    numero_seguimiento      VARCHAR(100) NULL,
    direccion_envio         VARCHAR(255) NOT NULL,
    ciudad_envio            VARCHAR(100) NOT NULL,
    pais_envio              VARCHAR(100) NOT NULL,
    referencia_envio        VARCHAR(255) NULL,
    codigo_postal_envio     VARCHAR(20) NULL,
    fecha_envio             DATETIME NULL,
    fecha_entrega_estimada  DATETIME NULL,
    fecha_entrega_real      DATETIME NULL,
    creado_en               DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_envio PRIMARY KEY (id_envio),
    CONSTRAINT uq_envio_pedido UNIQUE (id_pedido),
    CONSTRAINT uq_envio_numero_seguimiento UNIQUE (numero_seguimiento),
    INDEX idx_envio_estado_creado (estado, creado_en),
    CONSTRAINT chk_envio_direccion_no_vacia
        CHECK (CHAR_LENGTH(TRIM(direccion_envio)) > 0),
    CONSTRAINT chk_envio_ciudad_no_vacia
        CHECK (CHAR_LENGTH(TRIM(ciudad_envio)) > 0),
    CONSTRAINT chk_envio_pais_no_vacio
        CHECK (CHAR_LENGTH(TRIM(pais_envio)) > 0),
    CONSTRAINT chk_envio_fecha_estimada
        CHECK (
            fecha_envio IS NULL
            OR fecha_entrega_estimada IS NULL
            OR fecha_entrega_estimada >= fecha_envio
        ),
    CONSTRAINT chk_envio_fecha_real
        CHECK (
            fecha_envio IS NULL
            OR fecha_entrega_real IS NULL
            OR fecha_entrega_real >= fecha_envio
        ),
    CONSTRAINT chk_envio_despachado_tiene_fecha
        CHECK (estado = 'PREPARANDO' OR fecha_envio IS NOT NULL),
    CONSTRAINT chk_envio_entregado_tiene_fecha
        CHECK (estado <> 'ENTREGADO' OR fecha_entrega_real IS NOT NULL),
    CONSTRAINT fk_envio_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

CREATE TABLE comprobante_pago (
    id_comprobante      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    id_pedido           INT UNSIGNED NOT NULL,
    tipo                ENUM('BOLETA', 'FACTURA', 'TICKET') NOT NULL,
    serie               VARCHAR(10) NOT NULL,
    numero              VARCHAR(20) NOT NULL,
    emitido_en          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_comprobante_pago PRIMARY KEY (id_comprobante),
    CONSTRAINT uq_comprobante_pago_pedido UNIQUE (id_pedido),
    CONSTRAINT uq_comprobante_pago_serie_numero UNIQUE (serie, numero),
    CONSTRAINT chk_comprobante_serie_no_vacia
        CHECK (CHAR_LENGTH(TRIM(serie)) > 0),
    CONSTRAINT chk_comprobante_numero_no_vacio
        CHECK (CHAR_LENGTH(TRIM(numero)) > 0),
    CONSTRAINT fk_comprobante_pago_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido (id_pedido)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
);

-- ============================================================================
-- 7. INDICES - actualizado al 22/06/2026
-- ============================================================================
-- Ahora los indices se declaran junto a cada tabla para que las FK reutilicen esos
-- indices y no se creen indices automaticos redundantes.