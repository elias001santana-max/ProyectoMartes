package BD;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class INTERFAZ {

	private JFrame frame;
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private JButton btnDashboard, btnHabitaciones, btnHuespedes, btnRecepcion;

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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("MI HOTEL - Sistema de Gestión Hotelera");
		frame.setBounds(100, 100, 1200, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new BorderLayout());
		
		// Sidebar izquierdo
		JPanel sidebar = createSidebar();
		frame.getContentPane().add(sidebar, BorderLayout.WEST);
		
		// Panel principal con CardLayout
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		// Agregar paneles
		mainPanel.add(new PanelDashboard(), "Dashboard");
		mainPanel.add(new PanelHabitaciones(), "Habitaciones");
		mainPanel.add(new PanelHuespedes(), "Huespedes");
		mainPanel.add(new PanelRecepcion(), "Recepcion");
		
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		
		// Mostrar dashboard por defecto
		cardLayout.show(mainPanel, "Dashboard");
		setActiveButton(btnDashboard);
	}
	
	private JPanel createSidebar() {
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(new Color(44, 62, 80));
		sidebar.setPreferredSize(new Dimension(220, 800));
		sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
		
		// Logo/Título
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(new Color(44, 62, 80));
		logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
		logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JLabel lblLogo = new JLabel("🏨");
		lblLogo.setFont(new Font("Segoe UI", Font.PLAIN, 48));
		lblLogo.setForeground(Color.WHITE);
		
		JLabel lblTitulo = new JLabel("MI HOTEL");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTitulo.setForeground(Color.WHITE);
		
		JLabel lblSubtitulo = new JLabel("Gestión Hotelera");
		lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubtitulo.setForeground(new Color(189, 195, 199));
		
		logoPanel.add(lblLogo);
		logoPanel.add(Box.createVerticalStrut(5));
		logoPanel.add(lblTitulo);
		logoPanel.add(lblSubtitulo);
		
		sidebar.add(logoPanel);
		sidebar.add(Box.createVerticalStrut(40));
		
		// Separador
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(52, 73, 94));
		separator.setMaximumSize(new Dimension(190, 1));
		sidebar.add(separator);
		sidebar.add(Box.createVerticalStrut(20));
		
		// Botones de navegación
		btnDashboard = createNavButton("📊  Dashboard", "Dashboard");
		btnHabitaciones = createNavButton("🛏️  Habitaciones", "Habitaciones");
		btnHuespedes = createNavButton("👥  Huéspedes", "Huespedes");
		btnRecepcion = createNavButton("🔔  Recepción", "Recepcion");
		
		sidebar.add(btnDashboard);
		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(btnHabitaciones);
		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(btnHuespedes);
		sidebar.add(Box.createVerticalStrut(10));
		sidebar.add(btnRecepcion);
		
		sidebar.add(Box.createVerticalGlue());
		
		// Info del usuario (opcional)
		JPanel userPanel = new JPanel();
		userPanel.setBackground(new Color(52, 73, 94));
		userPanel.setLayout(new BorderLayout());
		userPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		userPanel.setMaximumSize(new Dimension(190, 60));
		
		JLabel lblUser = new JLabel("<html>👤 Admin<br><small>admin@hotel.com</small></html>");
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblUser.setForeground(Color.WHITE);
		
		userPanel.add(lblUser, BorderLayout.CENTER);
		sidebar.add(userPanel);
		
		return sidebar;
	}
	
	private JButton createNavButton(String text, String panelName) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		btn.setForeground(Color.WHITE);
		btn.setBackground(new Color(44, 62, 80));
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setMaximumSize(new Dimension(190, 45));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Efecto hover
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (btn.getBackground().equals(new Color(44, 62, 80))) {
					btn.setBackground(new Color(52, 73, 94));
				}
			}
			public void mouseExited(MouseEvent e) {
				if (btn.getBackground().equals(new Color(52, 73, 94))) {
					btn.setBackground(new Color(44, 62, 80));
				}
			}
		});
		
		// Acción de click
		btn.addActionListener(e -> {
			cardLayout.show(mainPanel, panelName);
			setActiveButton(btn);
		});
		
		return btn;
	}
	
	private void setActiveButton(JButton activeBtn) {
		// Resetear todos los botones
		btnDashboard.setBackground(new Color(44, 62, 80));
		btnHabitaciones.setBackground(new Color(44, 62, 80));
		btnHuespedes.setBackground(new Color(44, 62, 80));
		btnRecepcion.setBackground(new Color(44, 62, 80));
		
		// Establecer botón activo
		activeBtn.setBackground(new Color(52, 152, 219));
	}
}

