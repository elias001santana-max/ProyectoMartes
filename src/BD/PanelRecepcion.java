package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRecepcion extends JPanel {
	
	public PanelRecepcion() {
		setLayout(new BorderLayout());
		setBackground(new Color(236, 240, 241));
		
		// Panel superior
		JPanel topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		// Panel central con estadísticas
		JPanel centerPanel = new JPanel(new GridLayout(2, 2, 20, 20));
		centerPanel.setBackground(new Color(236, 240, 241));
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		centerPanel.add(createStatCard("Habitaciones Disponibles", "12", "🛏️", new Color(46, 204, 113)));
		centerPanel.add(createStatCard("Habitaciones Ocupadas", "3", "🔑", new Color(231, 76, 60)));
		centerPanel.add(createStatCard("Huéspedes Actuales", "5", "👥", new Color(52, 152, 219)));
		centerPanel.add(createStatCard("Ingresos del Mes", "$12,500", "💰", new Color(155, 89, 182)));
		
		add(centerPanel, BorderLayout.CENTER);
		
		// Panel inferior con acciones rápidas
		JPanel bottomPanel = createQuickActionsPanel();
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("Panel de Recepción");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titulo.setForeground(new Color(44, 62, 80));
		
		JLabel fecha = new JLabel("📅 " + java.time.LocalDate.now().toString());
		fecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		fecha.setForeground(new Color(127, 140, 141));
		
		panel.add(titulo, BorderLayout.WEST);
		panel.add(fecha, BorderLayout.EAST);
		
		return panel;
	}
	
	private JPanel createStatCard(String titulo, String valor, String icon, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout(15, 15));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(color, 2),
			new EmptyBorder(25, 25, 25, 25)
		));
		
		// Icono
		JLabel lblIcon = new JLabel(icon);
		lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
		
		// Panel de texto
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
		panel.setBackground(new Color(236, 240, 241));
		
		JButton btnCheckIn = createActionButton("Check-in Rápido", new Color(46, 204, 113));
		JButton btnReserva = createActionButton("Nueva Reserva", new Color(52, 152, 219));
		JButton btnCambioHab = createActionButton("Cambio de Habitación", new Color(230, 126, 34));
		
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
}
