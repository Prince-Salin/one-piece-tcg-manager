package proyecto;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Componente visual personalizado para representar una carta de One Piece TCG.
 *
 * Esta clase extiende JPanel y se encarga de renderizar la información visual 
 * de una carta (nombre, tipo, rareza, imagen y poder).
 *
 * Además, gestiona dinámicamente la visualización de datos de la tienda 
 * (precio y stock) mediante un interruptor estático de modo. Incluye animaciones
 * interactivas escalonadas dependiendo de la rareza de la carta.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class CartaVisual extends JPanel {

    private static final int CARD_WIDTH = 190;
    private static final int CARD_HEIGHT = 320;
    
    /** Interruptor global para habilitar o deshabilitar la visualización de datos de tienda. */
    public static boolean modoTienda = false; 

    private JLabel lblPrecio;
    private JLabel lblStock;

    /**
     * Construye el panel visual de la carta con sus datos correspondientes.
     * @param carta El objeto Carta a representar visualmente.
     */
    public CartaVisual(Carta carta) {
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        // --- 1. Saber qué rareza es y su color ---
        Color colorBorde = Color.GRAY; // Por defecto
        String rarezaStr = "COMUN";
        
        if (carta.getRareza() != null) {
            rarezaStr = carta.getRareza().name().toUpperCase();
        }

        if (rarezaStr.equals("LEGENDARIA")) {
            colorBorde = new Color(255, 215, 0); // Dorado
        } else if (rarezaStr.equals("EPICA")) {
            colorBorde = new Color(128, 0, 128); // Morado
        } else if (rarezaStr.equals("RARA")) {
            colorBorde = Color.BLUE; // Azul
        } else if (rarezaStr.equals("COMUN")) {
            colorBorde = Color.GREEN; // Verde
        }

        // Creamos el borde normal (grosor 3) y se lo ponemos a la carta
        javax.swing.border.Border bordeNormal = BorderFactory.createLineBorder(colorBorde, 3, true);
        setBorder(bordeNormal);

        // Guardamos el color de fondo original para poder restaurarlo luego
        Color fondoNormal = getBackground();
        
        // Guardamos la rareza en una variable final para usarla en los eventos
        final String rarezaFinal = rarezaStr;

        // --- 2. Efectos visuales ESCALONADOS (Para Raras, Épicas y Legendarias) ---
        if (rarezaFinal.equals("RARA") || rarezaFinal.equals("EPICA") || rarezaFinal.equals("LEGENDARIA")) {
            
            // Borde para las Raras (grosor 5 - intermedio)
            javax.swing.border.Border bordeRaro = BorderFactory.createLineBorder(colorBorde, 5, true);
            // Borde para Épicas y Legendarias (grosor 7 - muy destacado)
            javax.swing.border.Border bordeGordo = BorderFactory.createLineBorder(colorBorde, 7, true);

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    
                    // Elegimos qué borde poner según si es Rara o superior
                    if (rarezaFinal.equals("RARA")) {
                        setBorder(bordeRaro);
                    } else {
                        setBorder(bordeGordo);
                    }
                    
                    // Si además es Legendaria, cambiamos el fondo
                    if (rarezaFinal.equals("LEGENDARIA")) {
                        setBackground(new Color(255, 255, 200)); 
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    // Restauramos todo a la normalidad
                    setBorder(bordeNormal);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    
                    if (rarezaFinal.equals("LEGENDARIA")) {
                        setBackground(fondoNormal);
                    }
                }
            });
        }

        // --- 3. Panel Superior ---
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(new JLabel(carta.getNombre(), SwingConstants.CENTER));
        panelSuperior.add(new JLabel(carta.getRareza().name() + " - " + carta.getTipo(), SwingConstants.CENTER));
        panelSuperior.setOpaque(false); 
        add(panelSuperior, BorderLayout.NORTH);

     // --- 4. Centro: Imagen ---
        JLabel lblImagen = new JLabel("", SwingConstants.CENTER);
        String nombreImg = (carta.getImagen() != null && !carta.getImagen().trim().isEmpty()) ? carta.getImagen() : "default.png";
        
        // Buscamos la ruta de la imagen directamente
        java.net.URL urlImagen = getClass().getResource("/imagenes/" + nombreImg);
        
        // Comprobamos que la URL no sea null antes de crear el ImageIcon
        if (urlImagen != null) {
            ImageIcon icono = new ImageIcon(urlImagen);
            lblImagen.setIcon(new ImageIcon(icono.getImage().getScaledInstance(150, 180, Image.SCALE_SMOOTH)));
        } else {
            lblImagen.setText("[ Sin Imagen ]");
        }
        add(lblImagen, BorderLayout.CENTER);

        // --- 5. Panel Inferior: Poder, Descripción, Evolución, Precio y Stock ---
        JPanel panelInferior = new JPanel(new GridLayout(5, 1));
        panelInferior.setBackground(Color.WHITE);
        panelInferior.setOpaque(false);

        // Poder
        panelInferior.add(new JLabel("Poder Base: " + carta.getPoderBase(), SwingConstants.CENTER));

        // Descripción
        String desc = carta.getDescripcion();
        if (desc != null && desc.length() > 30) desc = desc.substring(0, 27) + "...";
        JLabel lblDesc = new JLabel(desc != null ? desc : "Sin descripción", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 10));
        panelInferior.add(lblDesc);

        // Evolución
        String nombreEvolucion = "Carta Base";
        if (carta.getIdCartaBase() != null) {
            try (Connection tempConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/one_piece_tcg", "root", "")) {
                CartaDAO cDAO = new CartaDAO(tempConn);
                Carta cartaBase = cDAO.buscarCartaPorId(carta.getIdCartaBase());
                if (cartaBase != null) {
                    nombreEvolucion = "Evoluciona de: " + cartaBase.getNombre();
                }
            } catch (Exception e) {
                nombreEvolucion = "Evoluciona de ID: " + carta.getIdCartaBase();
            }
        }
        JLabel lblEvo = new JLabel(nombreEvolucion, SwingConstants.CENTER);
        lblEvo.setFont(new Font("Arial", Font.ITALIC, 10));
        lblEvo.setForeground(Color.GRAY);
        panelInferior.add(lblEvo);

        // Precio y Stock
        lblPrecio = new JLabel("", SwingConstants.CENTER);
        lblPrecio.setForeground(new Color(0, 100, 0));
        lblPrecio.setVisible(false);

        lblStock = new JLabel("", SwingConstants.CENTER);
        lblStock.setVisible(false);

        panelInferior.add(lblPrecio);
        panelInferior.add(lblStock);
        add(panelInferior, BorderLayout.SOUTH);

        // --- Carga de datos condicional ---
        if (modoTienda) {
            buscarPrecioYStockEnBaseDatos(carta.getIdCarta());
        }
    }

    /**
     * Consulta la base de datos para obtener el precio y stock de la carta actual.
     * Solo se ejecuta si el modo tienda está activo.
     * @param idCarta El identificador de la carta para buscar su oferta.
     */
    private void buscarPrecioYStockEnBaseDatos(int idCarta) {
        try (Connection temporalConn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/one_piece_tcg", "root", "")) {
            TiendaDAO tiendaDAO = new TiendaDAO(temporalConn);
            for (Tienda oferta : tiendaDAO.listar()) {
                if (oferta.getIdCarta() == idCarta) {
                    lblPrecio.setText("Precio: " + oferta.getPrecioBerries() + " Berries");
                    lblStock.setText("Stock: " + oferta.getStock());
                    lblPrecio.setVisible(true);
                    lblStock.setVisible(true);
                    break; 
                }
            }
        } catch (Exception e) {
            // Se silencia la excepción para no interrumpir la visualización de la carta
        }
    }
}