package BD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class PanelReservas extends JPanel {
    private ArrayList<Reserva> reservas;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseManager db;
    
    public PanelReservas() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        
        db = DatabaseManager.getInstance();
        reservas = new ArrayList<>();
        
        // Panel superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Tabla de reservas
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cargar datos
        cargarReservas();
        
        // REGISTRAR ESTE PANEL PARA ACTUALIZACIONES AUTOMÁTICAS
        db.addRefreshListener(this::cargarReservas);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("📅 Gestión de Reservas");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 58, 95));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBackground(Color.WHITE);
        
        JComboBox<String> filtroEstado = new JComboBox<>(new String[] {
            "Todas", "PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"
        });
        filtroEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filtroEstado.setPreferredSize(new Dimension(150, 35));
        
        JButton btnFiltrar = createStyledButton("Filtrar", new Color(52, 152, 219));
        
        filterPanel.add(new JLabel("Estado: "));
        filterPanel.add(filtroEstado);
        filterPanel.add(btnFiltrar);
        
        btnFiltrar.addActionListener(e -> {
            String estadoInfo = (String) filtroEstado.getSelectedItem();
            if (estadoInfo != null && !estadoInfo.equals("Todas")) {
                reservas = db.obtenerReservasPorEstado(estadoInfo);
            } else {
                reservas = db.obtenerTodasReservas();
            }
            actualizarTabla();
        });
        
        panel.add(titulo, BorderLayout.WEST);
        panel.add(filterPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void createTable() {
        String[] columnNames = {"ID", "Huésped", "Documento", "Habitación", "Entrada", "Salida", "Noches", "Estado", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(30, 58, 95));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setOpaque(true);
        
        // Custom renderer to ensure header colors are visible
        javax.swing.table.DefaultTableCellRenderer headerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(30, 58, 95));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(JLabel.LEFT);
        
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(189, 195, 199));
        
        // Configurar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // Huésped
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Documento
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Habitación
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Entrada
        table.getColumnModel().getColumn(5).setPreferredWidth(100);  // Salida
        table.getColumnModel().getColumn(6).setPreferredWidth(70);   // Noches
        table.getColumnModel().getColumn(7).setPreferredWidth(120);  // Estado
        table.getColumnModel().getColumn(8).setPreferredWidth(100);  // Total
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 246, 250));
        
        JButton btnNueva = createStyledButton("➕ Nueva Reserva", new Color(46, 204, 113));
        JButton btnEditar = createStyledButton("✏️ Editar", new Color(52, 152, 219));
        JButton btnEliminar = createStyledButton("🗑️ Eliminar", new Color(231, 76, 60));
        JButton btnActualizar = createStyledButton("🔄 Actualizar", new Color(52, 73, 94));
        
        btnNueva.addActionListener(e -> nuevaReserva());
        btnEditar.addActionListener(e -> editarReserva());
        btnEliminar.addActionListener(e -> eliminarReserva());
        btnActualizar.addActionListener(e -> cargarReservas());
        
        panel.add(btnNueva);
        panel.add(btnEditar);
        panel.add(btnEliminar);
        panel.add(btnActualizar);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }
    
    private void cargarReservas() {
        reservas = db.obtenerTodasReservas();
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        for (Reserva r : reservas) {
            Object[] row = {
                r.getId(),
                r.getHuespedNombre(),
                r.getHuespedDocumento(),
                r.getHabitacionNumero(),
                r.getFechaEntrada(),
                r.getFechaSalida(),
                r.getNochesReservadas(),
                r.getEstado(),
                String.format("$%.2f", r.getPrecioTotal())
            };
            tableModel.addRow(row);
        }
    }
    
    private void nuevaReserva() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nueva Reserva", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        JTextField txtNombre = new JTextField();
        JTextField txtDocumento = new JTextField();
        JTextField txtHabitacion = new JTextField();
        JTextField txtEntrada = new JTextField(LocalDate.now().toString());
        JTextField txtSalida = new JTextField(LocalDate.now().plusDays(1).toString());
        JTextField txtPrecio = new JTextField();
        
        formPanel.add(new JLabel("Nombre del Huésped:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Documento:"));
        formPanel.add(txtDocumento);
        formPanel.add(new JLabel("Habitación:"));
        formPanel.add(txtHabitacion);
        formPanel.add(new JLabel("Fecha Entrada (YYYY-MM-DD):"));
        formPanel.add(txtEntrada);
        formPanel.add(new JLabel("Fecha Salida (YYYY-MM-DD):"));
        formPanel.add(txtSalida);
        formPanel.add(new JLabel("Precio Total:"));
        formPanel.add(txtPrecio);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnGuardar = createStyledButton("Guardar", new Color(46, 204, 113));
        JButton btnCancelar = createStyledButton("Cancelar", new Color(231, 76, 60));
        
        btnGuardar.addActionListener(e -> {
            try {
                Reserva nueva = new Reserva(
                    0,
                    txtNombre.getText(),
                    txtDocumento.getText(),
                    txtHabitacion.getText(),
                    LocalDate.parse(txtEntrada.getText()),
                    LocalDate.parse(txtSalida.getText()),
                    "PENDIENTE",
                    Double.parseDouble(txtPrecio.getText())
                );
                
                if (db.agregarReserva(nueva)) {
                    JOptionPane.showMessageDialog(dialog, "Reserva creada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarReservas();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al crear la reserva", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarReserva() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) table.getValueAt(selectedRow, 0);
        String huesped = (String) table.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar la reserva de " + huesped + "?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (db.eliminarReserva(id)) {
                JOptionPane.showMessageDialog(this, "Reserva eliminada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarReservas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editarReserva() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) table.getValueAt(selectedRow, 0);
        Reserva reserva = null;
        for (Reserva r : reservas) {
            if (r.getId() == id) {
                reserva = r;
                break;
            }
        }
        
        if (reserva == null) {
            cargarReservas();
            return;
        }
        
        final Reserva reservaFinal = reserva;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Reserva", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        JTextField txtNombre = new JTextField(reservaFinal.getHuespedNombre());
        JTextField txtDocumento = new JTextField(reservaFinal.getHuespedDocumento());
        JTextField txtHabitacion = new JTextField(reservaFinal.getHabitacionNumero());
        JTextField txtEntrada = new JTextField(reservaFinal.getFechaEntrada().toString());
        JTextField txtSalida = new JTextField(reservaFinal.getFechaSalida().toString());
        JTextField txtPrecio = new JTextField(String.valueOf(reservaFinal.getPrecioTotal()));
        
        JComboBox<String> comboEstado = new JComboBox<>(new String[] {
            "PENDIENTE", "CONFIRMADA", "CANCELADA", "COMPLETADA"
        });
        comboEstado.setSelectedItem(reservaFinal.getEstado());
        comboEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboEstado.setBackground(Color.WHITE);
        
        formPanel.add(new JLabel("Nombre del Huésped:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Documento:"));
        formPanel.add(txtDocumento);
        formPanel.add(new JLabel("Habitación:"));
        formPanel.add(txtHabitacion);
        formPanel.add(new JLabel("Fecha Entrada (YYYY-MM-DD):"));
        formPanel.add(txtEntrada);
        formPanel.add(new JLabel("Fecha Salida (YYYY-MM-DD):"));
        formPanel.add(txtSalida);
        formPanel.add(new JLabel("Precio Total:"));
        formPanel.add(txtPrecio);
        formPanel.add(new JLabel("Estado:"));
        formPanel.add(comboEstado);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnGuardar = createStyledButton("Guardar Cambios", new Color(52, 152, 219));
        JButton btnCancelar = createStyledButton("Cancelar", new Color(231, 76, 60));
        
        btnGuardar.addActionListener(e -> {
            try {
                Reserva actualizada = new Reserva(
                    id,
                    txtNombre.getText(),
                    txtDocumento.getText(),
                    txtHabitacion.getText(),
                    LocalDate.parse(txtEntrada.getText()),
                    LocalDate.parse(txtSalida.getText()),
                    (String) comboEstado.getSelectedItem(),
                    Double.parseDouble(txtPrecio.getText())
                );
                
                if (db.actualizarReserva(actualizada)) {
                    JOptionPane.showMessageDialog(dialog, "Reserva actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarReservas();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al actualizar la reserva", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error en los datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    public void filtrar(String query) {
        if (query == null || query.trim().isEmpty() || query.equals("Buscar huéspedes, habitaciones, reservas...")) {
            cargarReservas();
            return;
        }
        
        reservas = db.buscarReservas(query);
        actualizarTabla();
    }
}