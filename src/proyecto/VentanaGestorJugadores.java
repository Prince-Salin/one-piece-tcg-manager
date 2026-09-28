package proyecto;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana secundaria para la gestión de jugadores del sistema One Piece TCG.
 *
 * Esta clase actúa como una Vista pura (patrón MVC). No contiene flujos lógicos,
 * recolección de entradas de texto ni confirmaciones; delega inmediatamente la
 * ejecución y el control interactivo al controlador unificado GestorAcciones.
 *
 * @author Pablo Manuel y Cristian
 * @version 2.0
 */
public class VentanaGestorJugadores extends JDialog {

    private GestorAcciones gestor;

    /**
     * Construye la ventana del gestor de jugadores de forma modal.
     * * @param parent Frame padre para bloquear las interacciones del fondo.
     * @param gestor Instancia del controlador unificado del sistema.
     */
    public VentanaGestorJugadores(JFrame parent, GestorAcciones gestor) {
        super(parent, "Gestor de Jugadores", true);
        this.gestor = gestor;
        
        initComponents();
    }

    /**
     * Inicializa y organiza los botones visuales de la interfaz en cuadrícula,
     * vinculando sus eventos directamente con las rutinas del gestor de acciones.
     */
    private void initComponents() {
        setSize(320, 220);
        setLayout(new GridLayout(3, 1, 12, 12));
        setLocationRelativeTo(getParent());
        
        // Añadir margen interno decorativo a la cuadrícula de botones
        if (getContentPane() instanceof JPanel) {
            ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        }

        JButton btnCrear = new JButton("Crear Jugador");
        JButton btnModificar = new JButton("Modificar Jugador");
        JButton btnEliminar = new JButton("Eliminar Jugador");

        btnCrear.addActionListener(e -> gestor.ejecutarCrearJugadorUI());
        btnModificar.addActionListener(e -> gestor.ejecutarModificarJugadorUI());
        btnEliminar.addActionListener(e -> gestor.ejecutarEliminarJugadorUI());

        add(btnCrear);
        add(btnModificar);
        add(btnEliminar);
    }
}