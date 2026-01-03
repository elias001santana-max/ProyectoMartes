package BD;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class INTERFAZ {

	private JFrame frame;
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private JPanel contentPanel;
	private PanelDashboard panelDashboard;
	private PanelHabitaciones panelHabitaciones;
	private PanelHuespedes panelHuespedes;
	private PanelRecepcion panelRecepcion;
	private PanelReservas panelReservas;
	
	private JButton btnDashboard, btnHabitaciones, btnHuespedes, btnRecepcion, btnReservas;
	private JLabel lblReloj;
	private String activeCard = "Dashboard";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					INTERFAZ window = new INTERFAZ();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public INTERFAZ() {
		initialize();
		iniciarReloj();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("🏨 MI HOTEL - Sistema de Gestión Hotelera Profesional");
		frame.setBounds(100, 100, 1400, 850);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());
		
		// Sidebar izquierdo
		JPanel sidebar = createSidebar();
		frame.getContentPane().add(sidebar, BorderLayout.WEST);
		
		// Panel derecho con topbar y contenido
		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(new Color(245, 246, 250));
		
		// Topbar superior
		JPanel topbar = createTopbar();
		contentPanel.add(topbar, BorderLayout.NORTH);
		
		// Panel principal con CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainPanel.setBackground(new Color(245, 246, 250));
		
		// Agregar paneles
		// Agregar paneles
		panelDashboard = new PanelDashboard();
		panelHabitaciones = new PanelHabitaciones();
		panelHuespedes = new PanelHuespedes();
		panelRecepcion = new PanelRecepcion();
		panelReservas = new PanelReservas();
		
		mainPanel.add(panelDashboard, "Dashboard");
		mainPanel.add(panelHabitaciones, "Habitaciones");
		mainPanel.add(panelHuespedes, "Huespedes");
		mainPanel.add(panelRecepcion, "Recepcion");
		mainPanel.add(panelReservas, "Reservas");
		
		contentPanel.add(mainPanel, BorderLayout.CENTER);
		frame.getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// Mostrar dashboard por defecto
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
		
		// Panel izquierdo con búsqueda
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		searchPanel.setBackground(Color.WHITE);
		
		JTextField searchField = new JTextField(25);
		searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		searchField.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
			new EmptyBorder(8, 12, 8, 12)
		));
		searchField.setForeground(new Color(100, 100, 100));
		searchField.setText("🔍 Buscar huéspedes, habitaciones, reservas...");
		
		searchField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				if (searchField.getText().contains("Buscar")) {
					searchField.setText("");
					searchField.setForeground(Color.BLACK);
				}
			}
			public void focusLost(java.awt.event.FocusEvent evt) {
				if (searchField.getText().isEmpty()) {
					searchField.setText("🔍 Buscar huéspedes, habitaciones, reservas...");
					searchField.setForeground(new Color(100, 100, 100));
				}
			}
		});
		

		
		searchField.addActionListener(e -> realizarBusquedaGlobal(searchField.getText()));
		
		searchPanel.add(searchField);
		
		// Panel derecho con notificaciones, reloj y usuario
		JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
		rightPanel.setBackground(Color.WHITE);
		
		// Reloj
		lblReloj = new JLabel();
		lblReloj.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblReloj.setForeground(new Color(100, 100, 100));
		
		// Botón de notificaciones
		JButton btnNotificaciones = new JButton("🔔");
		btnNotificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		btnNotificaciones.setForeground(new Color(52, 152, 219));
		btnNotificaciones.setBackground(new Color(240, 248, 255));
		btnNotificaciones.setFocusPainted(false);
		btnNotificaciones.setBorderPainted(false);
		btnNotificaciones.setPreferredSize(new Dimension(45, 40));
		btnNotificaciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnNotificaciones.setToolTipText("3 notificaciones nuevas");
		
		// Menú de usuario
		JButton btnUsuario = new JButton("👤 Admin ▼");
		btnUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnUsuario.setForeground(new Color(44, 62, 80));
		btnUsuario.setBackground(new Color(245, 246, 250));
		btnUsuario.setFocusPainted(false);
		btnUsuario.setBorderPainted(false);
		btnUsuario.setPreferredSize(new Dimension(120, 40));
		btnUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		rightPanel.add(lblReloj);
		rightPanel.add(btnNotificaciones);
		rightPanel.add(btnUsuario);
		
		topbar.add(searchPanel, BorderLayout.WEST);
		topbar.add(rightPanel, BorderLayout.EAST);
		
		return topbar;
	}
	
	private void iniciarReloj() {
		Timer timer = new Timer(1000, e -> {
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss | dd/MM/yyyy");
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
		
		// Logo/Título
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(new Color(30, 58, 95));
		logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
		logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblLogo = new JLabel("🏨");
		lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 52));
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblTitulo = new JLabel("MI HOTEL");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblSubtitulo = new JLabel("Sistema de Gestión");
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblSubtitulo.setForeground(new Color(189, 195, 199));
		lblSubtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		logoPanel.add(lblLogo);
		logoPanel.add(Box.createVerticalStrut(8));
		logoPanel.add(lblTitulo);
		logoPanel.add(Box.createVerticalStrut(3));
		logoPanel.add(lblSubtitulo);
		
		sidebar.add(logoPanel);
		sidebar.add(Box.createVerticalStrut(35));
		
		// Separador
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(52, 73, 94));
		separator.setBackground(new Color(52, 73, 94));
		separator.setMaximumSize(new Dimension(214, 2));
		sidebar.add(separator);
		sidebar.add(Box.createVerticalStrut(25));
		
		// Sección de navegación
		JLabel lblNavegacion = new JLabel("NAVEGACIÓN");
		lblNavegacion.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblNavegacion.setForeground(new Color(149, 165, 166));
		lblNavegacion.setAlignmentX(Component.LEFT_ALIGNMENT);
		sidebar.add(lblNavegacion);
		sidebar.add(Box.createVerticalStrut(15));
		
		// Botones de navegación
		btnDashboard = createNavButton("📊  Dashboard", "Dashboard");
		btnHabitaciones = createNavButton("🛏️  Habitaciones", "Habitaciones");
		btnHuespedes = createNavButton("👥  Huéspedes", "Huespedes");
		btnRecepcion = createNavButton("🔔  Recepción", "Recepcion");
		btnReservas = createNavButton("📅  Reservas", "Reservas");
		
		sidebar.add(btnDashboard);
		sidebar.add(Box.createVerticalStrut(8));
		sidebar.add(btnHabitaciones);
		sidebar.add(Box.createVerticalStrut(8));
		sidebar.add(btnHuespedes);
		sidebar.add(Box.createVerticalStrut(8));
		sidebar.add(btnRecepcion);
		sidebar.add(Box.createVerticalStrut(8));
		sidebar.add(btnReservas);
		
		sidebar.add(Box.createVerticalStrut(30));
		
		sidebar.add(Box.createVerticalGlue());
		
		// Info del usuario
		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(41, 72, 111));
		userPanel.setLayout(new BorderLayout(10, 0));
		userPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
		userPanel.setMaximumSize(new Dimension(214, 65));
		userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblUserIcon = new JLabel("👤");
		lblUserIcon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		
		JLabel lblUser = new JLabel("<html><b>Admin</b><br><small style='color:#95a5a6'>admin@hotel.com</small></html>");
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblUser.setForeground(Color.WHITE);
		
		userPanel.add(lblUserIcon, BorderLayout.WEST);
		userPanel.add(lblUser, BorderLayout.CENTER);
		sidebar.add(userPanel);
		
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
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setMaximumSize(new Dimension(214, 48));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		btn.setBorder(new EmptyBorder(12, 15, 12, 15));
		
		// Efecto hover
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (!btn.getBackground().equals(new Color(52, 152, 219))) {
					btn.setBackground(new Color(41, 72, 111));
				}
			}
			public void mouseExited(MouseEvent e) {
				if (!btn.getBackground().equals(new Color(52, 152, 219))) {
					btn.setBackground(new Color(30, 58, 95));
				}
			}
		});
		
		// Acción de click
		btn.addActionListener(e -> {
			cardLayout.show(mainPanel, panelName);
			activeCard = panelName;
			setActiveButton(btn);
		});
		
		return btn;
	}
	
	private void setActiveButton(JButton activeBtn) {
		// Resetear todos los botones
		btnDashboard.setBackground(new Color(30, 58, 95));
		btnHabitaciones.setBackground(new Color(30, 58, 95));
		btnHuespedes.setBackground(new Color(30, 58, 95));
		btnRecepcion.setBackground(new Color(30, 58, 95));
		btnReservas.setBackground(new Color(30, 58, 95));
		
		// Establecer botón activo
		activeBtn.setBackground(new Color(52, 152, 219));
	}

	private void realizarBusquedaGlobal(String query) {
		switch (activeCard) {
			case "Huespedes":
				// If we implement an interface later it would be cleaner, but for now casting is fine
				// or assume we have the method available if we kept the reference
				panelHuespedes.filtrar(query);
				break;
			case "Reservas":
				panelReservas.filtrar(query);
				break;
			// Add other cases as needed
			default:
				System.out.println("Búsqueda no implementada para: " + activeCard);
				break;
		}
	}
}

