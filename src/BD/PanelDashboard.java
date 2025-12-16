package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelDashboard extends JPanel {
	
	public PanelDashboard() {
		setLayout(new BorderLayout());
		setBackground(new Color(236, 240, 241));
		
		// Panel superior
		JPanel topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		// Panel central con resumen
		JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 20));
		centerPanel.setBackground(new Color(236, 240, 241));
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		centerPanel.add(createSummaryCard("Total Habitaciones", "16", new Color(52, 152, 219)));
		centerPanel.add(createSummaryCard("Ocupación Actual", "18.75%", new Color(46, 204, 113)));
		centerPanel.add(createSummaryCard("Huéspedes Totales", "5", new Color(155, 89, 182)));
		
		add(centerPanel, BorderLayout.CENTER);
		
		// Panel de actividad reciente
		JPanel activityPanel = createActivityPanel();
		add(activityPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("Dashboard");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titulo.setForeground(new Color(44, 62, 80));
		
		JLabel subtitulo = new JLabel("Bienvenido al Sistema de Gestión Hotelera");
		subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		subtitulo.setForeground(new Color(127, 140, 141));
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
		titlePanel.setBackground(Color.WHITE);
		titlePanel.add(titulo);
		titlePanel.add(subtitulo);
		
		panel.add(titlePanel, BorderLayout.WEST);
		
		return panel;
	}
	
	private JPanel createSummaryCard(String titulo, String valor, Color color) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout());
		card.setBackground(color);
		card.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		lblTitulo.setForeground(Color.WHITE);
		
		JLabel lblValor = new JLabel(valor);
		lblValor.setFont(new Font("Segoe UI", Font.BOLD, 48));
		lblValor.setForeground(Color.WHITE);
		
		card.add(lblTitulo, BorderLayout.NORTH);
		card.add(lblValor, BorderLayout.CENTER);
		
		return card;
	}
	
	private JPanel createActivityPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("Actividad Reciente");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titulo.setForeground(new Color(44, 62, 80));
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		textArea.setBackground(new Color(245, 247, 250));
		textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
		textArea.setText(
			"✓ Check-in: Juan Pérez - NRO-001\n" +
			"✓ Reserva: María González - NRO-005\n" +
			"✓ Check-in: Carlos Rodríguez - NRO-010\n" +
			"• Habitación NRO-007 en mantenimiento\n"
		);
		
		panel.add(titulo, BorderLayout.NORTH);
		panel.add(textArea, BorderLayout.CENTER);
		
		return panel;
	}
}
