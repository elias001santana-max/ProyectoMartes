package Boton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class INTERFAZ {

    public JFrame frame;

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel contentPanel;

    private PanelDashboard panelDashboard;
    private PanelHabitaciones panelHabitaciones;
    private PanelHuespedes panelHuespedes;
    private PanelRecepcion panelRecepcion;
    private PanelReservas panelReservas;
   // private  PanelGestionIA panelGestionIA;
    private PanelMantenimiento panelMantenimiento;
    private PanelPagos panelPagos;

    private JButton btnDashboard, btnHabitaciones, btnHuespedes,
            btnRecepcion, btnReservas, btnGestionIA, btnMantenimiento, btnPagos;

    private JLabel lblReloj;
    private String activeCard = "Dashboard";
    private String nombreUsuario;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                INTERFAZ window = new INTERFAZ();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public INTERFAZ() {
        initialize();
        iniciarReloj();
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("🏨 MI HOTEL - Sistema de Gestión Hotelera Profesional");
        frame.setBounds(100, 100, 1400, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        frame.getContentPane().add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 246, 250));

        JPanel topbar = createTopbar();
        contentPanel.add(topbar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(245, 246, 250));

        panelDashboard = new PanelDashboard();
        panelHabitaciones = new PanelHabitaciones();
        panelHuespedes = new PanelHuespedes();
        panelRecepcion = new PanelRecepcion();
        panelReservas = new PanelReservas();
        //  panelGestionIA = new PanelGestionIA();
        panelMantenimiento = new PanelMantenimiento();
        panelPagos = new PanelPagos();

        mainPanel.add(panelDashboard, "Dashboard");
        mainPanel.add(panelHabitaciones, "Habitaciones");
        mainPanel.add(panelHuespedes, "Huespedes");
        mainPanel.add(panelRecepcion, "Recepcion");
        mainPanel.add(panelReservas, "Reservas");
        // mainPanel.add(panelGestionIA, "GestionIA");
        mainPanel.add(panelMantenimiento, "Mantenimiento");
        mainPanel.add(panelPagos, "Pagos");

        contentPanel.add(mainPanel, BorderLayout.CENTER);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Dashboard");
        setActiveButton(btnDashboard);
    }

    private JPanel createTopbar() {
        JPanel topbar = new JPanel(new BorderLayout());
        topbar.setBackground(Color.WHITE);
        topbar.setPreferredSize(new Dimension(1180, 70));
        topbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)),
                new EmptyBorder(15, 25, 15, 25)
        ));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        JTextField searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(new EmptyBorder(8, 12, 8, 12));
        searchField.setText("🔍 Buscar huéspedes, habitaciones, reservas...");

        searchField.addActionListener(e ->
                realizarBusquedaGlobal(searchField.getText())
        );

        searchPanel.add(searchField);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightPanel.setBackground(Color.WHITE);

        lblReloj = new JLabel();
        lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnUsuario = new JButton("👤 Admin ▼");
        btnUsuario.setFocusPainted(false);
        btnUsuario.setBorderPainted(false);

        rightPanel.add(lblReloj);
        rightPanel.add(btnUsuario);

        topbar.add(searchPanel, BorderLayout.WEST);
        topbar.add(rightPanel, BorderLayout.EAST);

        return topbar;
    }

    private void iniciarReloj() {
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy");
            lblReloj.setText("🕐 " + now.format(formatter));
        });
        timer.start();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(30, 58, 95));
        sidebar.setPreferredSize(new Dimension(250, 850));
        sidebar.setBorder(new EmptyBorder(25, 18, 25, 18));

        btnDashboard = createNavButton("📊  Dashboard", "Dashboard");
        btnHabitaciones = createNavButton("🛏️  Habitaciones", "Habitaciones");
        btnHuespedes = createNavButton("👥  Huéspedes", "Huespedes");
        btnRecepcion = createNavButton("🔔  Recepción", "Recepcion");
        btnReservas = createNavButton("📅  Reservas", "Reservas");
        btnGestionIA = createNavButton("🤖  Gestión con IA", "GestionIA");
        btnMantenimiento = createNavButton("🧹  Mantenimiento", "Mantenimiento");
        btnPagos = createNavButton("💰  Pagos", "Pagos");

        sidebar.add(btnDashboard);
        sidebar.add(btnHabitaciones);
        sidebar.add(btnHuespedes);
        sidebar.add(btnRecepcion);
        sidebar.add(btnReservas);
        sidebar.add(btnGestionIA);
        sidebar.add(btnMantenimiento);
        sidebar.add(btnPagos);

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton createNavButton(String text, String panelName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(30, 58, 95));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addActionListener(e -> {
            cardLayout.show(mainPanel, panelName);
            activeCard = panelName;
            setActiveButton(btn);
        });

        return btn;
    }

    private void setActiveButton(JButton activeBtn) {
        btnDashboard.setBackground(new Color(30, 58, 95));
        btnHabitaciones.setBackground(new Color(30, 58, 95));
        btnHuespedes.setBackground(new Color(30, 58, 95));
        btnRecepcion.setBackground(new Color(30, 58, 95));
        btnReservas.setBackground(new Color(30, 58, 95));
        btnGestionIA.setBackground(new Color(30, 58, 95));
        btnMantenimiento.setBackground(new Color(30, 58, 95));
        btnPagos.setBackground(new Color(30, 58, 95));

        activeBtn.setBackground(new Color(52, 152, 219));
    }

    private void realizarBusquedaGlobal(String query) {
        if (query == null || query.trim().isEmpty()) return;

        switch (activeCard) {
            case "Huespedes":
                panelHuespedes.filtrar(query);
                break;
            case "Reservas":
                panelReservas.filtrar(query);
                break;
            case "GestionIA":
                //panelGestionIA.realizarBusquedaIA(query);
                break;
            default:
                System.out.println("Búsqueda no implementada para: " + activeCard);
        }
    }
}
