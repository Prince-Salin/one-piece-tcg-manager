-- =========================
-- PROCEDIMIENTO COMPRAR CARTA
-- =========================

DELIMITER //

CREATE PROCEDURE sp_comprar_carta(
    IN p_nombre_jugador VARCHAR(100),
    IN p_nombre_carta VARCHAR(100)
)
BEGIN
    DECLARE v_id_jugador INT;
    DECLARE v_id_carta INT;
    DECLARE v_precio INT;
    DECLARE v_berries INT;
    DECLARE v_existe INT;
    DECLARE v_stock INT;

    START TRANSACTION;

    -- Obtener jugador
    SELECT id_jugador, berries INTO v_id_jugador, v_berries
    FROM jugador
    WHERE nombre = p_nombre_jugador;

    IF v_id_jugador IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El jugador no existe';
    END IF;

    -- Obtener carta
    SELECT id_carta INTO v_id_carta
    FROM carta
    WHERE nombre = p_nombre_carta;

    IF v_id_carta IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carta no existe';
    END IF;

    -- Obtener precio y stock
    SELECT precio_berries, stock INTO v_precio, v_stock
    FROM tienda_cartas
    WHERE id_carta = v_id_carta;

    IF v_precio IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carta no está en la tienda';
    END IF;

    IF v_stock <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No hay stock disponible';
    END IF;

    -- Comprobar dinero
    IF v_berries < v_precio THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Berries insuficientes';
    END IF;

    -- Restar dinero
    UPDATE jugador
    SET berries = berries - v_precio
    WHERE id_jugador = v_id_jugador;

    -- Añadir a inventario
    SELECT COUNT(*) INTO v_existe
    FROM inventario
    WHERE id_jugador = v_id_jugador 
    AND id_carta = v_id_carta;

    IF v_existe > 0 THEN
        UPDATE inventario
        SET cantidad = cantidad + 1
        WHERE id_jugador = v_id_jugador 
        AND id_carta = v_id_carta;
    ELSE
        INSERT INTO inventario (id_jugador, id_carta, cantidad)
        VALUES (v_id_jugador, v_id_carta, 1);
    END IF;

    -- Reducir stock
    UPDATE tienda_cartas
    SET stock = stock - 1
    WHERE id_carta = v_id_carta;

    COMMIT;

END //

DELIMITER ;

-- =========================
-- PROCEDIMIENTO VENDER CARTA
-- =========================

DELIMITER //

CREATE PROCEDURE sp_vender_carta(
    IN p_nombre_jugador VARCHAR(50),
    IN p_nombre_carta VARCHAR(50)
)
BEGIN
    DECLARE v_id_jugador INT;
    DECLARE v_id_carta INT;
    DECLARE v_precio INT;
    DECLARE v_precio_venta INT;
    DECLARE v_cantidad INT;

    START TRANSACTION;

    -- Obtener ID jugador
    SELECT id_jugador INTO v_id_jugador
    FROM jugador
    WHERE nombre = p_nombre_jugador;

    IF v_id_jugador IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El jugador no existe';
    END IF;

    -- Obtener ID carta
    SELECT id_carta INTO v_id_carta
    FROM carta
    WHERE nombre = p_nombre_carta;

    IF v_id_carta IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carta no existe';
    END IF;

    -- Obtener precio
    SELECT precio_berries INTO v_precio
    FROM tienda_cartas
    WHERE id_carta = v_id_carta;

    IF v_precio IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La carta no está en la tienda';
    END IF;

    -- Calcular precio venta (50%)
    SET v_precio_venta = ROUND(v_precio * 0.5);

    -- Ver cantidad en inventario
    SELECT cantidad INTO v_cantidad
    FROM inventario
    WHERE id_jugador = v_id_jugador
    AND id_carta = v_id_carta;

    IF v_cantidad IS NULL OR v_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El jugador no tiene esta carta';
    END IF;

    -- Restar carta del inventario
    IF v_cantidad > 1 THEN
        UPDATE inventario
        SET cantidad = cantidad - 1
        WHERE id_jugador = v_id_jugador
        AND id_carta = v_id_carta;
    ELSE
        DELETE FROM inventario
        WHERE id_jugador = v_id_jugador
        AND id_carta = v_id_carta;
    END IF;

    -- Dar dinero
    UPDATE jugador
    SET berries = berries + v_precio_venta
    WHERE id_jugador = v_id_jugador;

    -- Aumentar stock
    UPDATE tienda_cartas
    SET stock = stock + 1
    WHERE id_carta = v_id_carta;

    COMMIT;

END //

DELIMITER ;