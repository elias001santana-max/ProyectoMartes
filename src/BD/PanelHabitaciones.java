package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PanelHabitaciones extends JPanel {
	private ArrayList<Habitacion> habitaciones;
	private JPanel gridPanel;
	
	public PanelHabitaciones() {
		setLayout(new BorderLayout());
		setBackground(new Color(236, 240, 241));
		
		// Inicializar habitaciones
		inicializarHabitaciones();
		
		// Panel superior con título y filtros
		JPanel topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		// Panel de grid de habitaciones
		gridPanel = new JPanel(new GridLayout(4, 4, 20, 20));
		gridPanel.setBackground(new Color(236, 240, 241));
		gridPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// Crear tarjetas de habitaciones
		for (Habitacion hab : habitaciones) {
			gridPanel.add(createRoomCard(hab));
		}
		
		add(gridPanel, BorderLayout.CENTER);
	}
	
	private void inicializarHabitaciones() {
		habitaciones = new ArrayList<>();
		String[] categorias = {"INDIVIDUAL", "DOBLE", "SUITE", "PRESIDENCIAL"};
		String[] estados = {"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"};
		
		for (int i = 1; i <= 16; i++) {
			String numero = String.format("NRO-%03d", i);
			String categoria = categorias[(i - 1) % 4];
			String estado = i % 5 == 0 ? "OCUPADA" : (i % 7 == 0 ? "MANTENIMIENTO" : "DISPONIBLE");
			double precio = 50 + (i * 10);
			
			habitaciones.add(new Habitacion(numero, categoria, estado, precio));
		}
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("Habitaciones");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titulo.setForeground(new Color(44, 62, 80));
		
		JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		filterPanel.setBackground(Color.WHITE);
		
		JButton btnTodas = createFilterButton("Todas");
		JButton btnDisponibles = createFilterButton("Disponibles");
		JButton btnOcupadas = createFilterButton("Ocupadas");
		
		filterPanel.add(btnTodas);
		filterPanel.add(btnDisponibles);
		filterPanel.add(btnOcupadas);
		
		panel.add(titulo, BorderLayout.WEST);
		panel.add(filterPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private JButton createFilterButton(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btn.setForeground(Color.WHITE);
		btn.setBackground(new Color(52, 152, 219));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setPreferredSize(new Dimension(120, 35));
		
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(new Color(41, 128, 185));
			}
			public void mouseExited(MouseEvent e) {
				btn.setBackground(new Color(52, 152, 219));
			}
		});
		
		return btn;
	}
	
	private JPanel createRoomCard(Habitacion habitacion) {
		JPanel card = new JPanel();
		card.setLayout(new BorderLayout(10, 10));
		card.setBackground(Color.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
			new EmptyBorder(15, 15, 15, 15)
		));
		card.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Panel superior con número
		JPanel topCard = new JPanel(new BorderLayout());
		topCard.setBackground(Color.WHITE);
		
		JLabel lblNumero = new JLabel(habitacion.getNumero());
		lblNumero.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblNumero.setForeground(new Color(44, 62, 80));
		
		JLabel lblIcon = new JLabel("🛏️");
		lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		
		topCard.add(lblNumero, BorderLayout.WEST);
		topCard.add(lblIcon, BorderLayout.EAST);
		
		// Panel central con categoría
		JPanel centerCard = new JPanel(new BorderLayout());
		centerCard.setBackground(Color.WHITE);
		
		JLabel lblCategoria = new JLabel(habitacion.getCategoria());
		lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblCategoria.setForeground(new Color(127, 140, 141));
		
		centerCard.add(lblCategoria, BorderLayout.NORTH);
		
		// Panel inferior con estado
		JPanel bottomCard = new JPanel(new BorderLayout());
		bottomCard.setBackground(Color.WHITE);
		
		JButton btnEstado = new JButton(habitacion.getEstado());
		btnEstado.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnEstado.setForeground(Color.WHITE);
		btnEstado.setFocusPainted(false);
		btnEstado.setBorderPainted(false);
		btnEstado.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		// Colores según estado
		Color colorEstado;
		switch (habitacion.getEstado()) {
			case "DISPONIBLE":
				colorEstado = new Color(46, 204, 113); // Verde
				break;
			case "OCUPADA":
				colorEstado = new Color(231, 76, 60); // Rojo
				break;
			case "MANTENIMIENTO":
				colorEstado = new Color(149, 165, 166); // Gris
				break;
			default:
				colorEstado = new Color(52, 152, 219);
		}
		
		btnEstado.setBackground(colorEstado);
		
		bottomCard.add(btnEstado, BorderLayout.CENTER);
		
		// Agregar paneles al card
		card.add(topCard, BorderLayout.NORTH);
		card.add(centerCard, BorderLayout.CENTER);
		card.add(bottomCard, BorderLayout.SOUTH);
		
		// Efecto hover
		card.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				card.setBackground(new Color(245, 247, 250));
				topCard.setBackground(new Color(245, 247, 250));
				centerCard.setBackground(new Color(245, 247, 250));
				bottomCard.setBackground(new Color(245, 247, 250));
			}
			public void mouseExited(MouseEvent e) {
				card.setBackground(Color.WHITE);
				topCard.setBackground(Color.WHITE);
				centerCard.setBackground(Color.WHITE);
				bottomCard.setBackground(Color.WHITE);
			}
			public void mouseClicked(MouseEvent e) {
				mostrarDetallesHabitacion(habitacion);
			}
		});
		
		return card;
	}
	
	private void mostrarDetallesHabitacion(Habitacion habitacion) {
		String mensaje = "Habitación: " + habitacion.getNumero() + "\n" +
						 "Categoría: " + habitacion.getCategoria() + "\n" +
						 "Estado: " + habitacion.getEstado() + "\n" +
						 "Precio por noche: $" + habitacion.getPrecio();
		
		JOptionPane.showMessageDialog(this, mensaje, "Detalles de Habitación", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public ArrayList<Habitacion> getHabitaciones() {
		return habitaciones;
	}
}
