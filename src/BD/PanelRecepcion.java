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
		
		// Obtener datos reales
		int disponibles = db.contarHabitacionesPorEstado("DISPONIBLE");
		int ocupadas = db.contarHabitacionesPorEstado("OCUPADA");
		int huespedes = db.contarTotalHuespedes();
		int reservas = db.obtenerTodasReservas().size();
		
		centerPanel.add(createStatCard("Habitaciones Disponibles", String.valueOf(disponibles), "🛏️", new Color(46, 204, 113)));
		centerPanel.add(createStatCard("Habitaciones Ocupadas", String.valueOf(ocupadas), "🔑", new Color(231, 76, 60)));
		centerPanel.add(createStatCard("Huéspedes Actuales", String.valueOf(huespedes), "👥", new Color(52, 152, 219)));
		centerPanel.add(createStatCard("Reservas Activas", String.valueOf(reservas), "📋", new Color(155, 89, 182)));
		
		add(centerPanel, BorderLayout.CENTER);
		
		// Panel inferior con acciones rápidas
		JPanel bottomPanel = createQuickActionsPanel();
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("🔔 Panel de Recepción");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titulo.setForeground(new Color(30, 58, 95));
		
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
	
	private void mostrarCheckIn() {
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Check-in Rápido", true);
		dialog.setLayout(new BorderLayout());
		dialog.setSize(450, 450);
		dialog.setLocationRelativeTo(this);
		
		JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
		formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		formPanel.setBackground(Color.WHITE);
		
		JTextField txtNombre = new JTextField();
		JTextField txtApellido = new JTextField();
		JTextField txtDocumento = new JTextField();
		JTextField txtTelefono = new JTextField();
		
		// Habitaciones disponibles
		java.util.ArrayList<Habitacion> disponibles = db.obtenerHabitacionesPorEstado("DISPONIBLE");
		JComboBox<String> cmbHabitaciones = new JComboBox<>();
		for (Habitacion h : disponibles) {
			cmbHabitaciones.addItem(h.getNumero() + " (" + h.getCategoria() + ")");
		}
		
		formPanel.add(new JLabel("Nombre:")); formPanel.add(txtNombre);
		formPanel.add(new JLabel("Apellido:")); formPanel.add(txtApellido);
		formPanel.add(new JLabel("Documento:")); formPanel.add(txtDocumento);
		formPanel.add(new JLabel("Teléfono:")); formPanel.add(txtTelefono);
		formPanel.add(new JLabel("Habitación:")); formPanel.add(cmbHabitaciones);
		
		JButton btnGuardar = new JButton("Registrar Check-in");
		btnGuardar.setBackground(new Color(46, 204, 113));
		btnGuardar.setForeground(Color.WHITE);
		
		btnGuardar.addActionListener(e -> {
			if (txtNombre.getText().isEmpty() || txtDocumento.getText().isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Datos incompletos", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String habSeleccionada = (String) cmbHabitaciones.getSelectedItem();
			if (habSeleccionada == null) {
				JOptionPane.showMessageDialog(dialog, "No hay habitaciones disponibles", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String numeroHab = habSeleccionada.split(" ")[0]; // Extraer "NRO-XXX"
			
			Huesped nuevo = new Huesped(
				txtNombre.getText(), txtApellido.getText(), 
				txtDocumento.getText(), txtTelefono.getText(), ""
			);
			nuevo.setHabitacionAsignada(numeroHab);
			
			if (db.agregarHuesped(nuevo)) {
				db.actualizarEstadoHabitacion(numeroHab, "OCUPADA");
				JOptionPane.showMessageDialog(dialog, "Check-in realizado con éxito");
				dialog.dispose();
				// Recargar panel si es necesario (idealmente usar Observador, pero simple refresh funciona)
			} else {
				JOptionPane.showMessageDialog(dialog, "Error al registrar", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		dialog.add(formPanel, BorderLayout.CENTER);
		dialog.add(btnGuardar, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}
	
	private void mostrarNuevaReserva() {
		// Reutilizamos lógica similar a PanelReservas
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Reserva", true);
		dialog.setSize(400, 500);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());
		
		JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JTextField txtNombre = new JTextField();
		JTextField txtDoc = new JTextField();
		
		// Habitaciones (todas o disponibles)
		JComboBox<String> cmbHab = new JComboBox<>();
		for (Habitacion h : db.obtenerTodasHabitaciones()) {
			cmbHab.addItem(h.getNumero());
		}
		
		JTextField txtEntrada = new JTextField(java.time.LocalDate.now().toString());
		JTextField txtSalida = new JTextField(java.time.LocalDate.now().plusDays(1).toString());
		JTextField txtPrecio = new JTextField("100.0");
		
		formPanel.add(new JLabel("Huésped:")); formPanel.add(txtNombre);
		formPanel.add(new JLabel("Documento:")); formPanel.add(txtDoc);
		formPanel.add(new JLabel("Habitación:")); formPanel.add(cmbHab);
		formPanel.add(new JLabel("Entrada (YYYY-MM-DD):")); formPanel.add(txtEntrada);
		formPanel.add(new JLabel("Salida (YYYY-MM-DD):")); formPanel.add(txtSalida);
		formPanel.add(new JLabel("Precio Total:")); formPanel.add(txtPrecio);
		
		JButton btnGuardar = new JButton("Crear Reserva");
		btnGuardar.addActionListener(e -> {
			try {
				Reserva r = new Reserva(0, 
					txtNombre.getText(), txtDoc.getText(), 
					(String)cmbHab.getSelectedItem(),
					java.time.LocalDate.parse(txtEntrada.getText()),
					java.time.LocalDate.parse(txtSalida.getText()),
					"PENDIENTE",
					Double.parseDouble(txtPrecio.getText())
				);
				db.agregarReserva(r);
				JOptionPane.showMessageDialog(dialog, "Reserva creada");
				dialog.dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
			}
		});
		
		dialog.add(formPanel, BorderLayout.CENTER);
		dialog.add(btnGuardar, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}
	
	private void mostrarCambioHabitacion() {
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Cambio de Habitación", true);
		dialog.setSize(450, 300);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new BorderLayout());
		
		JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		// 1. Seleccionar habitación actual (Ocupada)
		java.util.ArrayList<Habitacion> ocupadas = db.obtenerHabitacionesPorEstado("OCUPADA");
		JComboBox<String> cmbOrigen = new JComboBox<>();
		for (Habitacion h : ocupadas) cmbOrigen.addItem(h.getNumero());
		
		// 2. Seleccionar destino (Disponible)
		java.util.ArrayList<Habitacion> disponibles = db.obtenerHabitacionesPorEstado("DISPONIBLE");
		JComboBox<String> cmbDestino = new JComboBox<>();
		for (Habitacion h : disponibles) cmbDestino.addItem(h.getNumero());
		
		panel.add(new JLabel("Habitación Actual (Ocupada):"));
		panel.add(cmbOrigen);
		panel.add(new JLabel("Nueva Habitación (Disponible):"));
		panel.add(cmbDestino);
		
		JButton btnCambiar = new JButton("Confirmar Cambio");
		btnCambiar.setBackground(new Color(230, 126, 34));
		btnCambiar.setForeground(Color.WHITE);
		
		btnCambiar.addActionListener(e -> {
			String origen = (String) cmbOrigen.getSelectedItem();
			String destino = (String) cmbDestino.getSelectedItem();
			
			if (origen == null || destino == null) {
				JOptionPane.showMessageDialog(dialog, "Seleccione habitaciones");
				return;
			}
			
			// Buscar huésped en habitación origen
			Huesped huesped = null;
			for (Huesped h : db.obtenerTodosHuespedes()) {
				if (h.getHabitacionAsignada() != null && h.getHabitacionAsignada().equals(origen)) {
					huesped = h;
					break;
				}
			}
			
			if (huesped != null) {
				// Actualizar todo
				huesped.setHabitacionAsignada(destino);
				db.actualizarHuesped(huesped.getDocumento(), huesped);
				db.actualizarEstadoHabitacion(origen, "DISPONIBLE"); // Libera antigua
				db.actualizarEstadoHabitacion(destino, "OCUPADA");   // Ocupa nueva
				
				JOptionPane.showMessageDialog(dialog, "Cambio realizado exitosamente");
				dialog.dispose();
			} else {
				JOptionPane.showMessageDialog(dialog, "No se encontró huésped en la habitación " + origen);
			}
		});
		
		dialog.add(panel, BorderLayout.CENTER);
		dialog.add(btnCambiar, BorderLayout.SOUTH);
		dialog.setVisible(true);
	}
}
