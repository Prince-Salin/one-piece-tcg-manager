# One Piece TCG Manager

Aplicación de escritorio para gestionar un juego de cartas coleccionables
de One Piece: catálogo de cartas con imagen, jugadores con puntos y
berries, tienda para comprar cartas y registro de acciones del sistema.

Práctica final de **Programación + Bases de Datos (1.º DAW)**.

## Funcionalidades

- Catálogo visual de cartas (nombre, tipo, rareza, poder) con su imagen.
- Gestión de jugadores: alta, consulta, puntos y saldo en berries.
- Tienda: compra de cartas con berries (vía procedimiento almacenado).
- Registro de acciones y log del sistema consultables desde la app.
- Interfaz gráfica en Java Swing, datos persistidos en MySQL.

## Tecnologías

- Java 17+ (Swing) · MySQL 8 · JDBC (`src/lib/`).
- Patrón DAO (`CartaDAO`, `JugadorDAO`, `TiendaDAO`, `LogSistemaDAO`).
- Procedimientos almacenados (p. ej. `sp_comprar_carta`).

## Requisitos

- Java 17 o superior (`java -version` para comprobarlo).
- MySQL 8 en local, puerto 3306 (vale el de XAMPP).

## Puesta en marcha

1. Arranca MySQL (p. ej. desde el panel de XAMPP).
2. Importa los scripts **en orden** (crean la BD `one_piece_tcg`):
   ```powershell
   & "C:\xampp\mysql\bin\mysql.exe" -u root < "sql\01_schema.sql"
   & "C:\xampp\mysql\bin\mysql.exe" -u root < "sql\02_procedimientos.sql"
   ```
   También puedes importarlos desde phpMyAdmin (*Importar*).
3. Comprueba la conexión en `src/proyecto/Conexion.java`: por defecto
   `127.0.0.1:3306`, usuario `root`, contraseña vacía (como XAMPP).
4. Con MySQL en marcha, ejecuta:
   ```powershell
   java -jar "dist\one-piece-tcg.jar"
   ```
   El `.jar` ya incluye las imágenes y el conector MySQL: no necesita
   nada más.

> Para modificar el código, importa la carpeta `src/` en Eclipse o
> VS Code con el `.jar` de `src/lib/` en el classpath.

## Estructura

```text
src/proyecto/  código fuente Java (modelo, DAO, ventanas)
src/Imagenes/  imágenes de las cartas
src/lib/       conector JDBC de MySQL
sql/           01 esquema + datos · 02 procedimientos (importar en orden)
docs/          diagrama entidad-relación y manual de usuario
dist/          ejecutable .jar listo para usar
```

## Documentación

- `docs/entidad-relacion.pdf`: diseño de la base de datos.
- `docs/manual-usuario.pdf`: guía de uso de la aplicación.

## Autores

Pablo Manuel y Cristian — 1.º DAW.
