package proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

/**
 * Ventana principal de la aplicación gráfica del sistema One Piece TCG.
 *
 * Esta clase representa la interfaz gráfica principal construida con Swing.
 * Es el punto central de interacción del usuario en el modo gráfico.
 *
 * Su responsabilidad principal es:
 * - Construir y organizar toda la interfaz gráfica (botones, paneles y áreas de texto)
 * - Inicializar la conexión a la base de datos
 * - Crear e inyectar los DAOs necesarios
 * - Delegar las acciones del usuario al GestorAcciones
 * - Gestionar la comunicación entre UI y lógica del sistema
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class VentanaPrincipal extends JFrame {

    private Connection conn;
    private JugadorDAO jugadorDAO;
    private CartaDAO cartaDAO;
    private TiendaDAO tiendaDAO;
    private LogSistemaDAO logDAO;
    private GestorAcciones gestorAcciones;

    private JPanel cardPanel;
    private JTextArea outputArea;
    private JTextArea logsArea;
    private JLabel statusLabel;

    /**
     * Construye la ventana principal e inicializa la aplicación.
     * * @param conn La conexión activa a la base de datos.
     */
    public VentanaPrincipal(Connection conn) {
        this.conn = conn;
        setTitle("One Piece TCG - Sistema de Gestión");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponentes();
        initLogica();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try { if (conn != null) conn.close(); } catch (Exception ex) {}
            }
        });
    }

    /**
     * Inicializa y configura todos los componentes visuales de la ventana.
     * Organiza el layout principal y asigna los listeners a los botones.
     */
    private void initComponentes() {
        setLayout(new BorderLayout());

        // --- Panel Superior (Botonera) ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(new Color(45, 52, 54));

        JButton btnJugadores = new JButton("Ver Jugadores");
        JButton btnGestorJugadores = new JButton("Gestor de Jugadores");
        JButton btnCartas = new JButton("Catálogo Cartas");
        JButton btnTienda = new JButton("Ver Tienda");
        JButton btnInventario = new JButton("Ver Inventario");
        JButton btnComprar = new JButton("Comprar Carta");
        JButton btnVender = new JButton("Vender Carta");

        topPanel.add(btnJugadores);
        topPanel.add(btnGestorJugadores);
        topPanel.add(btnCartas);
        topPanel.add(btnTienda);
        topPanel.add(btnInventario);
        topPanel.add(btnComprar);
        topPanel.add(btnVender);
        add(topPanel, BorderLayout.NORTH);

        // --- Panel Central y Divisores ---
        cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        cardPanel.setBackground(new Color(240, 242, 245));
        JScrollPane cardScroll = new JScrollPane(cardPanel);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane outputScroll = new JScrollPane(outputArea);

        logsArea = new JTextArea();
        logsArea.setEditable(false);
        logsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logsArea.setForeground(Color.BLUE);
        JScrollPane logsScroll = new JScrollPane(logsArea);
        logsScroll.setBorder(BorderFactory.createTitledBorder("Logs del Sistema"));

        JSplitPane splitCentroVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, cardScroll, outputScroll);
        splitCentroVertical.setDividerLocation(450);

        JSplitPane splitPrincipalHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitCentroVertical, logsScroll);
        splitPrincipalHorizontal.setDividerLocation(850);
        add(splitPrincipalHorizontal, BorderLayout.CENTER);

        statusLabel = new JLabel("Iniciando aplicación...");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(statusLabel, BorderLayout.SOUTH);

        // --- Listeners de botones ---
        btnJugadores.addActionListener(e -> gestorAcciones.cargarJugadores());
        
        btnGestorJugadores.addActionListener(e -> gestorAcciones.mostrarGestorJugadores());
        
        btnCartas.addActionListener(e -> {
            CartaVisual.modoTienda = false;
            gestorAcciones.cargarCartas();
        });
        
        btnTienda.addActionListener(e -> {
            CartaVisual.modoTienda = true;
            gestorAcciones.cargarTienda();
        });
        
        btnInventario.addActionListener(e -> {
            CartaVisual.modoTienda = false;
            gestorAcciones.verInventario();
        });
        
        btnComprar.addActionListener(e -> gestorAcciones.comprarCarta());
        btnVender.addActionListener(e -> gestorAcciones.venderCarta());
    }

    /**
     * Inicializa los objetos DAO y el GestorAcciones.
     * Gestiona posibles errores de conexión inicial.
     */
    private void initLogica() {
        try {
            jugadorDAO = new JugadorDAO(conn);
            cartaDAO = new CartaDAO(conn);
            tiendaDAO = new TiendaDAO(conn);
            logDAO = new LogSistemaDAO();

            gestorAcciones = new GestorAcciones(
                    conn, jugadorDAO, cartaDAO, tiendaDAO, logDAO,
                    cardPanel, outputArea, logsArea, statusLabel, this
            );

            statusLabel.setText("Conectado a la base de datos");
            gestorAcciones.setOutput("Bienvenido al sistema gráfico de One Piece TCG.");
            gestorAcciones.refreshLogs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error de inicialización: " + e.getMessage(), 
                    "Error Crítico", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}