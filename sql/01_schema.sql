-- =========================
-- CREAR BASE DE DATOS
-- =========================
CREATE DATABASE IF NOT EXISTS one_piece_tcg;
USE one_piece_tcg;

-- =========================
-- TABLA JUGADOR
-- =========================
CREATE TABLE jugador (
    id_jugador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
	puntos INT DEFAULT 100,
	berries INT DEFAULT 2000
);

-- =========================
-- TABLA CARTA
-- =========================
CREATE TABLE carta (
    id_carta INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    rareza ENUM('Comun', 'Rara', 'Epica', 'Legendaria') NOT NULL,
    poder_base INT NOT NULL,
    descripcion TEXT,
    imagen VARCHAR(255),
    id_carta_base INT,

    FOREIGN KEY (id_carta_base) REFERENCES carta(id_carta)
        ON DELETE SET NULL
);

-- =========================
-- TABLA TIENDA
-- =========================
CREATE TABLE tienda_cartas (
    id_oferta INT AUTO_INCREMENT PRIMARY KEY,
    id_carta INT NOT NULL,
    precio_berries INT NOT NULL,
    stock INT DEFAULT 1,

    FOREIGN KEY (id_carta) REFERENCES carta(id_carta)
        ON DELETE CASCADE
);

-- =========================
-- TABLA LOGS
-- =========================

CREATE TABLE logs_sistema (

    id_log INT AUTO_INCREMENT PRIMARY KEY,

    id_jugador INT NULL,

    accion ENUM(
        'REGISTRO',
        'COMPRA_CARTA',
        'VENTA_CARTA'
    ) NOT NULL,

    detalles TEXT NOT NULL,

    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_jugador)
        REFERENCES jugador(id_jugador)
        ON DELETE SET NULL
);

-- =========================
-- TABLA INVENTARIO
-- =========================

CREATE TABLE inventario (
id_inventario INT AUTO_INCREMENT PRIMARY KEY, 
id_jugador INT NOT NULL, id_carta INT NOT NULL, 
cantidad INT DEFAULT 1, FOREIGN KEY (id_jugador) 
REFERENCES jugador(id_jugador) ON DELETE CASCADE, 
FOREIGN KEY (id_carta) REFERENCES carta(id_carta) ON DELETE CASCADE);

-- =========================
-- INSERT JUGADORES
-- =========================
INSERT INTO jugador (nombre, puntos, berries) VALUES
('Lancer', 1500, 2000),
('Panther', 1200, 1500),
('Doran', 1800, 2500),
('Knight', 1700, 2200),
('Fisher', 2000, 3000);

-- =========================
-- INSERT CARTAS
-- =========================
INSERT INTO carta (nombre, tipo, rareza, poder_base, descripcion, imagen, id_carta_base) VALUES

-- LUFFY
('Monkey D. Luffy Base', 'Paramecia', 'Comun', 1200, 'Versión inicial del capitán', 'Luffy1.png', NULL),
('Luffy Gear 2', 'Paramecia', 'Rara', 1800, 'Aumenta velocidad y fuerza', 'Luffy2.png', 1),
('Luffy Gear 4', 'Haki', 'Epica', 2600, 'Forma avanzada con haki', 'Luffy4.png', 2),
('Monkey D. Luffy Gear 5', 'Haki', 'Legendaria', 3200, 'Despierta su fruta', 'Luffy5.png', 3),

-- ZORO
('Roronoa Zoro Base', 'Haki', 'Comun', 1300, 'Espadachín en formación', 'Zoro1.png', NULL),
('Zoro Enma', 'Haki', 'Rara', 1900, 'Domina espada Enma', 'Zoro2.png', 5),
('Roronoa Zoro Rey del Infierno', 'Haki', 'Epica', 2700, 'Haki del rey avanzado', 'Zoro3.png', 6),

-- SANJI
('Sanji Base', 'Haki', 'Comun', 1200, 'Cocinero y luchador', 'Sanji1.png', NULL),
('Sanji Diable Jambe', 'Haki', 'Rara', 1800, 'Piernas en llamas', 'Sanji2.png', 8),
('Sanji Ifrit Jambe', 'Haki', 'Epica', 2400, 'Fuego azul extremo', 'Sanji3.png', 9),

-- LAW
('Trafalgar Law Base', 'Paramecia', 'Rara', 1500, 'Cirujano de la muerte', 'Trafalgar1.png', NULL),
('Trafalgar Law Despertado', 'Paramecia', 'Epica', 2400, 'Control total del espacio', 'Trafalgar2.png', 11),

-- KID
('Eustass Kid Base', 'Paramecia', 'Rara', 1500, 'Controla metal', 'Kid1.png', NULL),
('Eustass Kid Despertado', 'Paramecia', 'Epica', 2400, 'Magnetismo extremo', 'Kid2.png', 13),

-- AKAINU
('Akainu Almirante', 'Logia', 'Epica', 2400, 'Antes de convertirse en almirante', 'Akainu1.png', NULL),
('Akainu Magma Absoluto', 'Logia', 'Legendaria', 2900, 'Magma devastador', 'Akainu2.png', 15),

-- KIZARU
('Kizaru Base', 'Logia', 'Rara', 2000, 'Almirante con poder de luz', 'Kizaru1.png', NULL),
('Kizaru Velocidad Luz', 'Logia', 'Epica', 2500, 'Ataques a velocidad lumínica', 'Kizaru2.png', 17),

-- TEACH
('Marshall D. Teach Base', 'Logia', 'Rara', 2100, 'Antes de dominar sus poderes', 'Barba1.png', NULL),
('Marshall D. Teach Oscuridad', 'Logia', 'Legendaria', 3000, 'Absorbe todo con la oscuridad', 'Barba2.png', 19),

-- MIHAWK
('Mihawk Espadachin', 'Haki', 'Epica', 2600, 'Gran maestro de la espada', 'Dracule1.png', NULL),
('Mihawk Espada Negra', 'Haki', 'Legendaria', 3100, 'El mejor espadachín del mundo', 'Dracule2.png', 21),

-- ENEL
('Enel Base', 'Logia', 'Rara', 2100, 'Usuario de la Goro Goro no Mi', 'Enel1.png', NULL),
('Enel Trueno Divino', 'Logia', 'Epica', 2500, 'Forma divina del trueno', 'Enel2.png', 23),

-- IMU
('Imu Sama', 'Haki', 'Legendaria', 3500, 'Entidad misteriosa con poder absoluto', 'IMU.png', NULL);

-- =========================
-- INSERT TIENDA
-- =========================

INSERT INTO tienda_cartas (id_carta, precio_berries, stock) VALUES
(2, 900, 2),
(3, 1600, 1),
(4, 2200, 1),
(5, 600, 3),
(7, 1700, 1),
(10, 1500, 2),
(12, 1500, 2),
(13, 800, 3),
(14, 1500, 2),
(15, 1500, 1),
(17, 1200, 2),
(18, 1500, 1),
(19, 1300, 2),
(20, 2000, 1),
(21, 1600, 1),
(22, 2100, 1),
(23, 1200, 2),
(24, 1500, 1),
(25, 2500, 1);

-- =========================
-- INSERT INVENTARIO (5 cartas por jugador)
-- =========================
INSERT INTO inventario (id_jugador, id_carta, cantidad) VALUES
-- Inventario de Lancer (id_jugador: 1)
(1, 1, 1),   -- Luffy Base
(1, 5, 1),   -- Zoro Base
(1, 11, 2),  -- Trafalgar Law Base (Tiene 2 repetidas)
(1, 20, 1),  -- Barbanegra Oscuridad
(1, 25, 1),  -- Imu Sama

-- Inventario de Panther (id_jugador: 2)
(2, 2, 1),   -- Luffy Gear 2
(2, 8, 1),   -- Sanji Base
(2, 12, 1),  -- Law Despertado
(2, 16, 1),  -- Akainu Magma
(2, 21, 1),  -- Mihawk Espadachin

-- Inventario de Doran (id_jugador: 3)
(3, 3, 1),   -- Luffy Gear 4
(3, 6, 1),   -- Zoro Enma
(3, 9, 1),   -- Sanji Diable Jambe
(3, 14, 1),  -- Kid Despertado
(3, 23, 1),  -- Enel Base

-- Inventario de Knight (id_jugador: 4)
(4, 4, 1),   -- Luffy Gear 5
(4, 7, 1),   -- Zoro Rey del Infierno
(4, 10, 2),  -- Sanji Ifrit Jambe (Tiene 2 repetidas)
(4, 18, 1),  -- Kizaru Velocidad Luz
(4, 22, 1),  -- Mihawk Espada Negra

-- Inventario de Fisher (id_jugador: 5)
(5, 1, 1),   -- Luffy Base
(5, 13, 1),  -- Kid Base
(5, 15, 1),  -- Akainu Almirante
(5, 17, 1),  -- Kizaru Base
(5, 19, 1);  -- Barbanegra Base