# One Piece TCG Manager

Plataforma de gestión del juego de cartas de One Piece: catálogo de cartas
con imágenes, jugadores, tienda y registro de acciones. Práctica final de
Programación + Bases de Datos (1.º DAW).

Java (Swing) + MySQL, con patrón DAO.

## Requisitos

- Java 17 o superior.
- MySQL 8 en local (puerto 3306).

## Puesta en marcha

1. En MySQL, importa en este orden:
   - `sql/01_schema.sql` (crea la base de datos `one_piece_tcg` + datos).
   - `sql/02_procedimientos.sql` (procedimientos almacenados).
2. Revisa el usuario y la contraseña en `src/proyecto/Conexion.java`
   (por defecto `root` sin contraseña).
3. Ejecuta `dist/one-piece-tcg.jar` con MySQL en marcha, o importa la
   carpeta `src/` en Eclipse / VS Code (el conector JDBC va en `src/lib/`).

## Estructura

```text
src/proyecto/  codigo fuente Java
src/Imagenes/  imagenes de las cartas
src/lib/       conector JDBC de MySQL
sql/           esquema y procedimientos (importar en orden)
docs/          diagrama entidad-relacion y manual de usuario
dist/          ejecutable .jar + imagenes
```

## Autores

Pablo Manuel y Cristian.
