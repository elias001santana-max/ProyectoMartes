package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelHuespedes extends JPanel {
	private ArrayList<Huesped> huespedes;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public PanelHuespedes() {
		setLayout(new BorderLayout());
		setBackground(new Color(236, 240, 241));
		
		huespedes = new ArrayList<>();
		
		// Panel superior
		JPanel topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		
		// Tabla de huéspedes
		createTable();
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		add(scrollPane, BorderLayout.CENTER);
		
		// Panel de botones
		JPanel buttonPanel = createButtonPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		
		// Cargar datos de ejemplo
		cargarDatosEjemplo();
	}
	
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));
		
		JLabel titulo = new JLabel("Gestión de Huéspedes");
		titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
		titulo.setForeground(new Color(44, 62, 80));
		
		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		searchPanel.setBackground(Color.WHITE);
		
		JTextField searchField = new JTextField(20);
		searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		searchField.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
			new EmptyBorder(5, 10, 5, 10)
		));
		
		JButton btnSearch = new JButton("🔍 Buscar");
		btnSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		btnSearch.setBackground(new Color(52, 152, 219));
		btnSearch.setForeground(Color.WHITE);
		btnSearch.setFocusPainted(false);
		btnSearch.setBorderPainted(false);
		btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		searchPanel.add(new JLabel("Buscar: "));
		searchPanel.add(searchField);
		searchPanel.add(btnSearch);
		
		panel.add(titulo, BorderLayout.WEST);
		panel.add(searchPanel, BorderLayout.EAST);
		
		return panel;
	}
	
	private void createTable() {
		String[] columnNames = {"Nombre", "Apellido", "Documento", "Teléfono", "Habitación", "Estado"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		table = new JTable(tableModel);
		table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		table.setRowHeight(35);
		table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
		table.getTableHeader().setBackground(new Color(52, 73, 94));
		table.getTableHeader().setForeground(Color.WHITE);
		table.setSelectionBackground(new Color(52, 152, 219));
		table.setSelectionForeground(Color.WHITE);
		table.setGridColor(new Color(189, 195, 199));
		
		// Configurar anchos de columna
		table.getColumnModel().getColumn(0).setPreferredWidth(120); // Nombre
		table.getColumnModel().getColumn(1).setPreferredWidth(120); // Apellido
		table.getColumnModel().getColumn(2).setPreferredWidth(100); // Documento
		table.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
		table.getColumnModel().getColumn(4).setPreferredWidth(100); // Habitación
		table.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		panel.setBackground(new Color(236, 240, 241));
		
		JButton btnAgregar = createButton("➕ Agregar Huésped", new Color(46, 204, 113));
		JButton btnEditar = createButton("✏️ Editar", new Color(52, 152, 219));
		JButton btnEliminar = createButton("🗑️ Eliminar", new Color(231, 76, 60));
		JButton btnCheckIn = createButton("✅ Check-in", new Color(155, 89, 182));
		JButton btnCheckOut = createButton("🚪 Check-out", new Color(230, 126, 34));
		
		panel.add(btnAgregar);
		panel.add(btnEditar);
		panel.add(btnEliminar);
		panel.add(btnCheckIn);
		panel.add(btnCheckOut);
		
		return panel;
	}
	
	private JButton createButton(String text, Color color) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
		btn.setBackground(color);
		btn.setForeground(Color.WHITE);
		btn.setPreferredSize(new Dimension(160, 40));
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		return btn;
	}
	
	private void cargarDatosEjemplo() {
		Huesped h1 = new Huesped("Juan", "Pérez", "12345678", "555-0101", "juan@email.com");
		h1.setHabitacionAsignada("NRO-001");
		huespedes.add(h1);
		
		Huesped h2 = new Huesped("María", "González", "87654321", "555-0102", "maria@email.com");
		h2.setHabitacionAsignada("NRO-005");
		huespedes.add(h2);
		
		Huesped h3 = new Huesped("Carlos", "Rodríguez", "11223344", "555-0103", "carlos@email.com");
		h3.setHabitacionAsignada("NRO-010");
		huespedes.add(h3);
		
		actualizarTabla();
	}
	
	private void actualizarTabla() {
		tableModel.setRowCount(0);
		for (Huesped h : huespedes) {
			Object[] row = {
				h.getNombre(),
				h.getApellido(),
				h.getDocumento(),
				h.getTelefono(),
				h.getHabitacionAsignada().isEmpty() ? "Sin asignar" : h.getHabitacionAsignada(),
				h.getHabitacionAsignada().isEmpty() ? "Registrado" : "Hospedado"
			};
			tableModel.addRow(row);
		}
	}
}
