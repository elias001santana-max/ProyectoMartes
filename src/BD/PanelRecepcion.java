package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRecepcion extends JPanel {

    private DatabaseManager db;

    public PanelRecepcion() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));

        db = DatabaseManager.getInstance();

        // Panel superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel central con estadísticas
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerPanel.setBackground(new Color(245, 246, 250));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        int disponibles = db.contarHabitacionesPorEstado("DISPONIBLE");
        int ocupadas = db.contarHabitacionesPorEstado("OCUPADA");
        int huespedes = db.contarTotalHuespedes();
        int reservas = db.obtenerTodasReservas().size();

        centerPanel.add(createStatCard("Habitaciones Disponibles", String.valueOf(disponibles), "🛏️", new Color(46, 204, 113)));
        centerPanel.add(createStatCard("Habitaciones Ocupadas", String.valueOf(ocupadas), "🔑", new Color(231, 76, 60)));
        centerPanel.add(createStatCard("Huéspedes Actuales", String.valueOf(huespedes), "👥", new Color(52, 152, 219)));
        centerPanel.add(createStatCard("Reservas Activas", String.valueOf(reservas), "📋", new Color(155, 89, 182)));

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = createQuickActionsPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        
        // REGISTRAR ESTE PANEL PARA ACTUALIZACIONES AUTOMÁTICAS
        db.addRefreshListener(this::actualizarEstadisticas);
    }
    
    private void actualizarEstadisticas() {
        // Este método actualizará las estadísticas cuando se llame
        Component[] components = getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                if (panel.getLayout() instanceof GridLayout) {
                    panel.removeAll();
                    
                    int disponibles = db.contarHabitacionesPorEstado("DISPONIBLE");
                    int ocupadas = db.contarHabitacionesPorEstado("OCUPADA");
                    int huespedes = db.contarTotalHuespedes();
                    int reservas = db.obtenerTodasReservas().size();

                    panel.add(createStatCard("Habitaciones Disponibles", String.valueOf(disponibles), "🛏️", new Color(46, 204, 113)));
                    panel.add(createStatCard("Habitaciones Ocupadas", String.valueOf(ocupadas), "🔑", new Color(231, 76, 60)));
                    panel.add(createStatCard("Huéspedes Actuales", String.valueOf(huespedes), "👥", new Color(52, 152, 219)));
                    panel.add(createStatCard("Reservas Activas", String.valueOf(reservas), "📋", new Color(155, 89, 182)));
                    
                    panel.revalidate();
                    panel.repaint();
                    break;
                }
            }
        }
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("🔔 Panel de Recepción");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 58, 95));

        JLabel fecha = new JLabel("📅 " + java.time.LocalDate.now());
        fecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        fecha.setForeground(new Color(127, 140, 141));

        panel.add(titulo, BorderLayout.WEST);
        panel.add(fecha, BorderLayout.EAST);

        return panel;
    }

    private JPanel createStatCard(String titulo, String valor, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                new EmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitulo.setForeground(new Color(127, 140, 141));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblValor.setForeground(color);

        textPanel.add(lblTitulo);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(lblValor);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panel.setBackground(new Color(245, 246, 250));

        JButton btnCheckIn = createActionButton("Check-in Rápido", new Color(46, 204, 113));
        JButton btnReserva = createActionButton("Nueva Reserva", new Color(52, 152, 219));
        JButton btnCambioHab = createActionButton("Cambio de Habitación", new Color(230, 126, 34));

        btnCheckIn.addActionListener(e -> mostrarCheckIn());
        btnReserva.addActionListener(e -> mostrarNuevaReserva());
        btnCambioHab.addActionListener(e -> mostrarCambioHabitacion());

        panel.add(btnCheckIn);
        panel.add(btnReserva);
        panel.add(btnCambioHab);

        return panel;
    }

    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ----- MÉTODOS DE ACCIÓN -----
    private void mostrarCheckIn() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de Check-in en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarNuevaReserva() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de Nueva Reserva en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarCambioHabitacion() {
        JOptionPane.showMessageDialog(this, "Funcionalidad de Cambio de Habitación en desarrollo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}