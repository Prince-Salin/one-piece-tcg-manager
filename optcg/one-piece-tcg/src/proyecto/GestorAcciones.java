package proyecto;

import javax.swing.*;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Gestor centralizado de acciones del usuario dentro de la aplicación.
 *
 * Esta clase actúa como controlador principal de la interfaz gráfica,
 * coordinando las operaciones entre la base de datos (DAOs) y la UI Swing.
 *
 * Se encarga de gestionar funcionalidades como:
 * - Visualización y control de flujos de ventanas auxiliares
 * - Carga de jugadores, cartas y tienda
 * - Búsqueda de cartas e inventarios de usuario
 * - Ejecución de transacciones de compra y venta
 * - Control de flujos interactivos de entrada/confirmación para el CRUD de jugadores
 * - Sincronización y actualización de logs del sistema
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class GestorAcciones {

    private Connection conn;
    private JugadorDAO jugadorDAO;
    private CartaDAO cartaDAO;
    private TiendaDAO tiendaDAO;
    private LogSistemaDAO logDAO;

    private JPanel cardPanel;
    private JTextArea outputArea;
    private JTextArea logsArea;
    private JLabel statusLabel;
    private JFrame ventanaPadre;

    /**
     * Constructor del gestor de acciones.
     * @param conn Conexión a la base de datos
     * @param jugadorDAO DAO de jugadores
     * @param cartaDAO DAO de cartas
     * @param tiendaDAO DAO de la tienda
     * @param logDAO DAO de logs del sistema
     * @param cardPanel Panel donde se renderizan las cartas
     * @param outputArea Área de texto para mensajes informativos
     * @param logsArea Área de texto para el historial de logs
     * @param statusLabel Etiqueta de estado de la interfaz
     * @param ventanaPadre Referencia al JFrame principal
     */
    public GestorAcciones(Connection conn, JugadorDAO jugadorDAO, CartaDAO cartaDAO, 
                          TiendaDAO tiendaDAO, LogSistemaDAO logDAO, JPanel cardPanel, 
                          JTextArea outputArea, JTextArea logsArea, JLabel statusLabel, 
                          JFrame ventanaPadre) {
        this.conn = conn;
        this.jugadorDAO = jugadorDAO;
        this.cartaDAO = cartaDAO;
        this.tiendaDAO = tiendaDAO;
        this.logDAO = logDAO;
        this.cardPanel = cardPanel;
        this.outputArea = outputArea;
        this.logsArea = logsArea;
        this.statusLabel = statusLabel;
        this.ventanaPadre = ventanaPadre;
    }

    /**
     * Abre de forma modal la ventana secundaria del gestor de jugadores.
     */
    public void mostrarGestorJugadores() {
        VentanaGestorJugadores ventanaGestor = new VentanaGestorJugadores(ventanaPadre, this);
        ventanaGestor.setVisible(true);
    }

    /**
     * Carga el ranking TOP 10 de jugadores y lo muestra en el área de salida.
     */
    public void cargarJugadores() {
        try {
            List<Jugador> lista = jugadorDAO.top10();
            limpiarPanel();
            if (lista.isEmpty()) {
                setOutput("No hay jugadores registrados.");
            } else {
                setOutput(buildTopJugadoresTexto(lista));
            }
            setStatus("TOP 10 cargado");
        } catch (Exception e) {
            mostrarError("Error al cargar jugadores: " + e.getMessage());
        }
    }

    /**
     * Carga el catálogo completo de cartas disponibles en el sistema.
     */
    public void cargarCartas() {
        try {
            List<Carta> cartas = cartaDAO.listar();
            if (cartas.isEmpty()) {
                setOutput("No hay cartas disponibles.");
                limpiarPanel();
            } else {
                mostrarCartas(cartas);
                setOutput("Cartas disponibles:");
            }
            setStatus("Cartas cargadas");
        } catch (Exception e) {
            mostrarError("Error al cargar cartas: " + e.getMessage());
        }
    }

    /**
     * Solicita un nombre al usuario y busca la carta correspondiente.
     */
    public void buscarCarta() {
        String nombre = JOptionPane.showInputDialog(ventanaPadre, "Introduce el nombre de la carta:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            Carta carta = cartaDAO.buscarCartaPorNombre(nombre.trim());
            if (carta == null) {
                setOutput("No se encontró la carta.");
                limpiarPanel();
            } else {
                Carta[] array = {carta};
                mostrarCartas(java.util.Arrays.asList(array));
                setOutput("Carta encontrada:");
            }
            setStatus("Búsqueda completada");
        } catch (Exception e) {
            mostrarError("Error al buscar carta: " + e.getMessage());
        }
    }

    /**
     * Carga las ofertas actuales de la tienda en la interfaz.
     */
    public void cargarTienda() {
        try {
            List<Tienda> ofertas = tiendaDAO.listar();
            if (ofertas.isEmpty()) {
                setOutput("No hay cartas en stock.");
                limpiarPanel();
            } else {
                mostrarOfertas(ofertas);
                setOutput("Ofertas de la tienda:");
            }
            setStatus("Tienda cargada");
        } catch (Exception e) {
            mostrarError("Error al cargar tienda: " + e.getMessage());
        }
    }

    /**
     * Renderiza una lista de objetos Carta en el panel visual.
     * @param cartas Lista de cartas a mostrar
     */
    private void mostrarCartas(List<Carta> cartas) {
        limpiarPanel();
        for (int i = 0; i < cartas.size(); i++) {
            cardPanel.add(new CartaVisual(cartas.get(i)));
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Renderiza las ofertas de la tienda en el panel visual.
     * @param ofertas Lista de ofertas a mostrar
     * @throws SQLException Si hay error en la consulta de datos
     */
    private void mostrarOfertas(List<Tienda> ofertas) throws SQLException {
        limpiarPanel();
        for (int i = 0; i < ofertas.size(); i++) {
            Carta carta = cartaDAO.buscarCartaPorId(ofertas.get(i).getIdCarta());
            if (carta != null) {
                cardPanel.add(new CartaVisual(carta));
            }
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Limpia el panel de visualización de cartas y refresca la UI.
     */
    private void limpiarPanel() {
        cardPanel.removeAll();
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    /**
     * Formatea la lista de jugadores para su visualización en texto, 
     * alineando las columnas con bucles while e incluyendo el rango de One Piece.
     */
    private String buildTopJugadoresTexto(List<Jugador> lista) {
        String sb = "No.  Nombre               Puntos   Berries   Rango\n";
        sb += "------------------------------------------------------------------------\n";
        
        for (int i = 0; i < lista.size(); i++) {
            Jugador j = lista.get(i);
            String rango = obtenerRangoOnePiece(j.getPuntos());
            
            // 1. Alineamos el nombre (20 espacios)
            String nombre = j.getNombre();
            while (nombre.length() < 20) {
                nombre += " ";
            }
            
            // 2. Alineamos los puntos (9 espacios)
            String puntos = String.valueOf(j.getPuntos());
            while (puntos.length() < 9) {
                puntos += " ";
            }
            
            // 3. Alineamos los berries (10 espacios)
            String berries = String.valueOf(j.getBerries());
            while (berries.length() < 10) {
                berries += " ";
            }
            
            // Montamos la línea final usando los textos ya rellenados con espacios
            sb += (i + 1) + "    " + nombre + puntos + berries + rango + "\n";
        }
        return sb;
    }

    /**
     * Calcula el título o rango del jugador en el universo de One Piece
     * basándose en su cantidad actual de puntos.
     */
    private String obtenerRangoOnePiece(int puntos) {
        if (puntos < 100) {
            return "Grumete";                 // Nivel más bajo
        } else if (puntos < 500) {
            return "Pirata Novato";           // Ya tienen cartel de recompensa bajo
        } else if (puntos < 1500) {
            return "Supernova";               // Promesas de la peor generación
        } else if (puntos < 3000) {
            return "Shichibukai";             // Piratas reconocidos por el Gobierno
        } else if (puntos < 6000) {
            return "Yonkou";                  // Emperadores del mar
        } else {
            return "Rey de los Piratas";      // El nivel máximo
        }
    }

    /**
     * Muestra el inventario de cartas de un jugador específico.
     */
    public void verInventario() {
        String nombre = JOptionPane.showInputDialog(ventanaPadre, "Introduce el nombre del jugador:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            List<Carta> cartas = jugadorDAO.obtenerCartasDeJugador(nombre.trim());
            if (cartas.isEmpty()) {
                setOutput("El jugador no tiene cartas o no existe.");
                limpiarPanel();
            } else {
                mostrarCartas(cartas);
                setOutput("Inventario de " + nombre.trim() + ":");
            }
            setStatus("Inventario cargado");
        } catch (Exception e) {
            mostrarError("Error al cargar inventario: " + e.getMessage());
        }
    }

    /**
     * Gestiona el proceso de compra de una carta por parte de un jugador.
     */
    public void comprarCarta() {
        String jugador = pedirTextoUI("Nombre del jugador:");
        if (jugador == null) return;
        
        String carta = pedirTextoUI("Nombre de la carta:");
        if (carta == null) return;

        try {
            jugadorDAO.comprarCarta(jugador, carta);
            Logger.log(conn, logDAO, null, Accion.COMPRA_CARTA.name(), jugador + " compró " + carta);
            setOutput("Compra realizada correctamente.");
            refreshLogs();
            setStatus("Compra completada");
        } catch (Exception e) {
            mostrarError("Error al comprar carta: " + e.getMessage());
        }
    }

    /**
     * Gestiona el proceso de venta de una carta por parte de un jugador.
     */
    public void venderCarta() {
        String jugador = pedirTextoUI("Nombre del jugador:");
        if (jugador == null) return;

        String carta = pedirTextoUI("Nombre de la carta:");
        if (carta == null) return;

        try {
            jugadorDAO.venderCarta(jugador, carta);
            Logger.log(conn, logDAO, null, Accion.VENTA_CARTA.name(), jugador + " vendió " + carta);
            setOutput("Venta realizada correctamente.");
            refreshLogs();
            setStatus("Venta completada");
        } catch (Exception e) {
            mostrarError("Error al vender carta: " + e.getMessage());
        }
    }

    /**
     * Despliega la UI interactiva para solicitar el nombre de un nuevo jugador y lo registra en el sistema.
     */
    public void ejecutarCrearJugadorUI() {
        String nombre = JOptionPane.showInputDialog(ventanaPadre, "Nombre del nuevo jugador:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        try {
            jugadorDAO.insertar(new Jugador(nombre.trim()));
            Logger.log(conn, logDAO, null, "REGISTRO", "Jugador creado: " + nombre.trim());
            setOutput("Jugador '" + nombre.trim() + "' creado correctamente.");
            refreshLogs();
            setStatus("Jugador creado");
        } catch (Exception e) {
            mostrarError("Error al crear jugador: " + e.getMessage());
        }
    }

    /**
     * Despliega la UI interactiva para solicitar el nombre del jugador a modificar,
     * valida su existencia y actualiza su nombre al nuevo valor proporcionado.
     */
    public void ejecutarModificarJugadorUI() {
        String nombreActual = JOptionPane.showInputDialog(ventanaPadre, "Nombre del jugador que deseas modificar:");
        if (nombreActual == null || nombreActual.trim().isEmpty()) return;

        String nuevoNombre = JOptionPane.showInputDialog(ventanaPadre, "Introduce el nuevo nombre para '" + nombreActual.trim() + "':");
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

        try {
            Jugador j = jugadorDAO.buscarPorNombre(nombreActual.trim());
            if (j != null) {
                j.setNombre(nuevoNombre.trim());
                jugadorDAO.actualizar(j);
                Logger.log(conn, logDAO, null, "MODIFICACION", "Jugador modificado: " + nombreActual.trim() + " a " + nuevoNombre.trim());
                setOutput("Jugador modificado correctamente de '" + nombreActual.trim() + "' a '" + nuevoNombre.trim() + "'.");
                refreshLogs();
                setStatus("Jugador modificado");
            } else {
                mostrarError("No se encontró un jugador llamado: " + nombreActual.trim());
            }
        } catch (Exception e) {
            mostrarError("Error al modificar jugador: " + e.getMessage());
        }
    }

    /**
     * Despliega la UI interactiva para solicitar el nombre del jugador a eliminar,
     * pide confirmación explícita de seguridad y procesa su baja en el sistema.
     */
    public void ejecutarEliminarJugadorUI() {
        String nombre = JOptionPane.showInputDialog(ventanaPadre, "Nombre del jugador que deseas eliminar:");
        if (nombre == null || nombre.trim().isEmpty()) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                ventanaPadre, 
                "¿Estás seguro de que deseas eliminar permanentemente a '" + nombre.trim() + "'?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            Jugador j = jugadorDAO.buscarPorNombre(nombre.trim());
            if (j != null) {
                jugadorDAO.eliminar(j.getId());
                Logger.log(conn, logDAO, null, "ELIMINACION", "Jugador eliminado: " + nombre.trim());
                setOutput("Jugador '" + nombre.trim() + "' eliminado correctamente.");
                refreshLogs();
                setStatus("Jugador eliminado");
            } else {
                mostrarError("No se encontró un jugador llamado: " + nombre.trim());
            }
        } catch (Exception e) {
            mostrarError("Error al eliminar jugador: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar para solicitar entrada de texto vía diálogo.
     * @param mensaje Mensaje a mostrar al usuario
     * @return El texto introducido o null si se cancela
     */
    private String pedirTextoUI(String mensaje) {
        String valor = JOptionPane.showInputDialog(ventanaPadre, mensaje);
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }

    /**
     * Actualiza el área de logs con los registros más recientes de la base de datos.
     */
    public void refreshLogs() {
        try {
            List<LogSistema> logs = logDAO.obtenerUltimosLogs(conn);
            String texto = "=== ÚLTIMOS LOGS ===\n";
            for (int i = 0; i < logs.size(); i++) {
                texto += logs.get(i).getAccion() + " | " + logs.get(i).getDetalles() + "\n";
            }
            logsArea.setText(texto);
        } catch (Exception e) {
            logsArea.setText("No se pudieron cargar los logs.");
        }
    }

    /**
     * Establece el texto en el área de salida principal.
     * @param mensaje Texto a mostrar
     */
    public void setOutput(String mensaje) {
        outputArea.setText(mensaje);
        outputArea.setCaretPosition(0);
    }

    /**
     * Actualiza el mensaje en la etiqueta de estado de la interfaz.
     * @param texto Texto de estado
     */
    public void setStatus(String texto) {
        statusLabel.setText(texto);
        statusLabel.setForeground(new Color(0, 120, 0));
    }

    /**
     * Muestra un diálogo de error al usuario.
     * @param mensaje Mensaje descriptivo del error
     */
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(ventanaPadre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
        statusLabel.setText("Error");
        statusLabel.setForeground(Color.RED);
    }
}