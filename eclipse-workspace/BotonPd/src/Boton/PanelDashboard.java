package Boton;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.*;
//import BD.PanelGestionIA;

public class PanelDashboard extends JPanel {
    private DatabaseManager db;
    private JLabel lblTotalHabitaciones, lblOcupacion, lblHuespedes;
    
    public PanelDashboard() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));

        db = DatabaseManager.getInstance();

        // Panel superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Panel central con resumen
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        centerPanel.setBackground(new Color(245, 246, 250));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cargar estadísticas reales
        int totalHabitaciones = db.obtenerTodasHabitaciones().size();
        double ocupacion = db.calcularOcupacion();
        int totalHuespedes = db.contarTotalHuespedes();

        JPanel cardHabitaciones = createSummaryCard("Total Habitaciones", String.valueOf(totalHabitaciones), new Color(52, 152, 219), "🛏️");
        JPanel cardOcupacion = createSummaryCard("Ocupación Actual", String.format("%.1f%%", ocupacion), new Color(46, 204, 113), "📊");
        JPanel cardHuespedes = createSummaryCard("Huéspedes Totales", String.valueOf(totalHuespedes), new Color(155, 89, 182), "👥");

        centerPanel.add(cardHabitaciones);
        centerPanel.add(cardOcupacion);
        centerPanel.add(cardHuespedes);

        add(centerPanel, BorderLayout.CENTER);

        // Panel de actividad reciente
        JPanel activityPanel = createActivityPanel();
        add(activityPanel, BorderLayout.SOUTH);
        
        // REGISTRAR ESTE PANEL PARA ACTUALIZACIONES AUTOMÁTICAS
        db.addRefreshListener(this::actualizarDatos);
    }
    
    private void mostrarPanelGestionIA() {
        removeAll();
        setLayout(new BorderLayout());
        //add(new PanelGestionIA(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("📊 Dashboard");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 58, 95));
        
        JLabel subtitulo = new JLabel("Bienvenido al Sistema de Gestión Hotelera");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(127, 140, 141));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);
        titlePanel.add(titulo);
        titlePanel.add(subtitulo);
        
        // Botón de actualizar
        JButton btnActualizar = new JButton("🔄 Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorderPainted(false);
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnActualizar.setPreferredSize(new Dimension(130, 35));
        
        btnActualizar.addActionListener(e -> actualizarDatos());
        
        panel.add(titlePanel, BorderLayout.WEST);
        panel.add(btnActualizar, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createSummaryCard(String titulo, String valor, Color color, String icono) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 15));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        // Icono grande
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        
        // Panel de texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblTitulo.setForeground(new Color(127, 140, 141));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(lblTitulo);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(lblValor);
        
        card.add(lblIcono, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        // Efecto hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBackground(new Color(248, 249, 250));
                textPanel.setBackground(new Color(248, 249, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBackground(Color.WHITE);
                textPanel.setBackground(Color.WHITE);
            }
        });
        
        return card;
    }
    
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titulo = new JLabel("📋 Resumen del Sistema");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(30, 58, 95));
        
        // Panel de estadísticas detalladas
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        int disponibles = db.contarHabitacionesPorEstado("DISPONIBLE");
        int ocupadas = db.contarHabitacionesPorEstado("OCUPADA");
        int mantenimiento = db.contarHabitacionesPorEstado("MANTENIMIENTO");
        int reservasActivas = db.obtenerTodasReservas().size();
        
        statsPanel.add(createMiniStat("Habitaciones Disponibles", String.valueOf(disponibles), new Color(46, 204, 113)));
        statsPanel.add(createMiniStat("Habitaciones Ocupadas", String.valueOf(ocupadas), new Color(231, 76, 60)));
        statsPanel.add(createMiniStat("En Mantenimiento", String.valueOf(mantenimiento), new Color(149, 165, 166)));
        statsPanel.add(createMiniStat("Reservas Totales", String.valueOf(reservasActivas), new Color(52, 152, 219)));
        
        panel.add(titulo, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createMiniStat(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblLabel.setForeground(new Color(100, 100, 100));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(color);
        
        panel.add(lblLabel, BorderLayout.NORTH);
        panel.add(lblValue, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void actualizarDatos() {
        // Recargar el panel
        removeAll();
        setLayout(new BorderLayout());
        
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        centerPanel.setBackground(new Color(245, 246, 250));
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        int totalHabitaciones = db.obtenerTodasHabitaciones().size();
        double ocupacion = db.calcularOcupacion();
        int totalHuespedes = db.contarTotalHuespedes();
        
        centerPanel.add(createSummaryCard("Total Habitaciones", String.valueOf(totalHabitaciones), new Color(52, 152, 219), "🛏️"));
        centerPanel.add(createSummaryCard("Ocupación Actual", String.format("%.1f%%", ocupacion), new Color(46, 204, 113), "📊"));
        centerPanel.add(createSummaryCard("Huéspedes Totales", String.valueOf(totalHuespedes), new Color(155, 89, 182), "👥"));
        
        add(centerPanel, BorderLayout.CENTER);
        add(createActivityPanel(), BorderLayout.SOUTH);
        
        revalidate();
        repaint();
    }
}