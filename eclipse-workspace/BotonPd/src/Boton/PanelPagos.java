package Boton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PanelPagos extends JPanel {
    private ArrayList<Pagos> pagos;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseManager db;
    
    public PanelPagos() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        
        db = DatabaseManager.getInstance();
        pagos = new ArrayList<>();
        
        // Panel superior
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Tabla de pagos
        createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cargar datos
        cargarDatos();
        
        // REGISTRAR ESTE PANEL PARA ACTUALIZACIONES AUTOMÁTICAS
        db.addRefreshListener(this::cargarDatos);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("💰 Gestión de Pagos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(30, 58, 95));
        
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
        
        btnSearch.addActionListener(e -> filtrar(searchField.getText()));
        searchField.addActionListener(e -> filtrar(searchField.getText()));
        
        searchPanel.add(new JLabel("Buscar por huésped o método: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        
        panel.add(titulo, BorderLayout.WEST);
        panel.add(searchPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void createTable() {
        String[] columnNames = {"ID Pago", "Nombre Huésped", "Número de Habitación", "Monto", "Método de Pago", "Fecha de Pago"};
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
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180); // Nombre
        table.getColumnModel().getColumn(2).setPreferredWidth(130); // Número habitación
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Monto
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Método
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Fecha
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 246, 250));
        
        JButton btnAgregar = createButton("? Registrar Pago", new Color(46, 204, 113));
        JButton btnEliminar = createButton("??? Eliminar", new Color(231, 76, 60));
        JButton btnActualizar = createButton("?? Actualizar", new Color(52, 73, 94));
        JButton btnPDF = createButton("?? PDF", new Color(220, 53, 69));
        
        btnAgregar.addActionListener(e -> registrarPago());
        btnEliminar.addActionListener(e -> eliminarPago());
        btnActualizar.addActionListener(e -> cargarDatos());
        btnPDF.addActionListener(e -> exportarAPDF());
        
        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnActualizar);
        panel.add(btnPDF);
        
        return panel;
    }
    
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    private void cargarDatos() {
        pagos = db.obtenerTodosPagos();
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Pagos p : pagos) {
            Object[] row = {
                p.getIdPago(),
                p.getNombreHuesped(),
                p.getNumeroHabitacion() != null ? p.getNumeroHabitacion() : "Sin asignar",
                String.format("$%.2f", p.getMonto()),
                p.getMetodoPago(),
                p.getFechaPago().format(formatter)
            };
            tableModel.addRow(row);
        }
    }
    
    private void registrarPago() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Registrar Pago", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        // OBTENER NOMBRES DE HUÉSPEDES DE LA BASE DE DATOS
        ArrayList<String> nombresHuespedes = new ArrayList<>();
        nombresHuespedes.add("-- Seleccione un huésped --");
        for (Huesped h : db.obtenerTodosHuespedes()) {
            nombresHuespedes.add(h.getNombreCompleto());
        }
        
        JComboBox<String> cmbNombreHuesped = new JComboBox<>(nombresHuespedes.toArray(new String[0]));
        cmbNombreHuesped.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField txtMonto = new JTextField();
        
        String[] metodosPago = {"Efectivo", "Tarjeta de Crédito", "Tarjeta de Débito", "Transferencia", "Cheque"};
        JComboBox<String> cmbMetodoPago = new JComboBox<>(metodosPago);
        cmbMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Usar JSpinner para la fecha
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spinnerFecha = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy");
        spinnerFecha.setEditor(dateEditor);
        spinnerFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // OBTENER NÚM EROS DE HABITACIÓN
        ArrayList<String> numerosHabitaciones = new ArrayList<>();
        numerosHabitaciones.add("-- Sin habitación --");
        for (Habitacion hab : db.obtenerTodasHabitaciones()) {
            numerosHabitaciones.add(hab.getNumero());
        }
        
        JComboBox<String> cmbNumeroHabitacion = new JComboBox<>(numerosHabitaciones.toArray(new String[0]));
        cmbNumeroHabitacion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        formPanel.add(new JLabel("Nombre del Huésped:"));
        formPanel.add(cmbNombreHuesped);
        formPanel.add(new JLabel("Número de Habitación:"));
        formPanel.add(cmbNumeroHabitacion);
        formPanel.add(new JLabel("Monto:"));
        formPanel.add(txtMonto);
        formPanel.add(new JLabel("Método de Pago:"));
        formPanel.add(cmbMetodoPago);
        formPanel.add(new JLabel("Fecha de Pago:"));
        formPanel.add(spinnerFecha);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnGuardar = createButton("Guardar", new Color(46, 204, 113));
        JButton btnCancelar = createButton("Cancelar", new Color(231, 76, 60));
        
        btnGuardar.addActionListener(e -> {
            if (cmbNombreHuesped.getSelectedIndex() == 0 || txtMonto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Debe seleccionar un huésped y el monto es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double monto;
            try {
                monto = Double.parseDouble(txtMonto.getText().trim());
                if (monto <= 0) {
                    JOptionPane.showMessageDialog(dialog, "El monto debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Monto inválido. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            java.util.Date selectedDate = (java.util.Date) spinnerFecha.getValue();
            LocalDate fecha = LocalDate.ofInstant(selectedDate.toInstant(), 
                java.time.ZoneId.systemDefault());
            
            String numeroHabitacion = null;
            if (cmbNumeroHabitacion.getSelectedIndex() > 0) {
                numeroHabitacion = cmbNumeroHabitacion.getSelectedItem().toString();
            }
            
            Pagos nuevoPago = new Pagos(
                0, // ID autogenerado
                cmbNombreHuesped.getSelectedItem().toString(),
                monto,
                cmbMetodoPago.getSelectedItem().toString(),
                fecha,
                numeroHabitacion
            );
            
            if (db.agregarPago(nuevoPago)) {
                // SI SE SELECCIONÓ UNA HABITACIÓN, CAMBIARLA A OCUPADA
                if (numeroHabitacion != null) {
                    boolean estadoActualizado = db.actualizarEstadoHabitacion(numeroHabitacion, "OCUPADA");
                    
                    if (estadoActualizado) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Pago registrado exitosamente.\nLa habitación " + numeroHabitacion + " ahora está OCUPADA.", 
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Pago registrado, pero hubo un problema al actualizar el estado de la habitación.", 
                            "Advertencia", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "Pago registrado exitosamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al registrar pago", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarPago() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un pago para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPago = (int) table.getValueAt(selectedRow, 0);
        String nombreHuesped = (String) table.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar el pago #" + idPago + " de " + nombreHuesped + "?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (db.eliminarPago(idPago)) {
                JOptionPane.showMessageDialog(this, "Pago eliminado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar pago", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void filtrar(String query) {
        if (query == null || query.trim().isEmpty()) {
            cargarDatos();
            return;
        }
        
        pagos = db.buscarPagos(query);
        actualizarTabla();
    }
    
    private void exportarAPDF() {
        try {
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + "\\Desktop\\Reporte_Pagos_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";
            
            java.io.FileWriter writer = new java.io.FileWriter(desktopPath);
            java.io.PrintWriter printWriter = new java.io.PrintWriter(writer);
            
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>Reporte de Pagos - Hotel M4</title>");
            printWriter.println("<style>");
            printWriter.println("@media print { .no-print { display: none; } }");
            printWriter.println("body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }");
            printWriter.println("h1 { text-align: center; color: #1e3a5f; margin-bottom: 5px; }");
            printWriter.println("h2 { text-align: center; color: #666; font-size: 14px; margin-top: 5px; }");
            printWriter.println("table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-top: 20px; }");
            printWriter.println("th { background: #1e3a5f; color: white; padding: 12px; text-align: left; font-weight: bold; }");
            printWriter.println("td { padding: 10px; border-bottom: 1px solid #ddd; }");
            printWriter.println("tr:hover { background: #f0f0f0; }");
            printWriter.println(".total { text-align: right; margin-top: 20px; font-size: 16px; font-weight: bold; }");
            printWriter.println(".btn-pdf { background: #e74c3c; color: white; border: none; padding: 15px 30px; font-size: 16px; cursor: pointer; border-radius: 5px; margin: 20px auto; display: block; }");
            printWriter.println(".btn-pdf:hover { background: #c0392b; }");
            printWriter.println("</style>");
            printWriter.println("<script>");
            printWriter.println("function imprimirPDF() { window.print(); }");
            printWriter.println("window.onload = function() { setTimeout(imprimirPDF, 500); };");
            printWriter.println("</script>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<div style='text-align: center; margin-bottom: 20px;'>");
            printWriter.println("<img src='file:///" + userHome.replace("\\", "/") + "/eclipse-workspace/BotonPd/logo_hotel.png' style='width: 150px; height: 150px;' />");
            printWriter.println("</div>");
            printWriter.println("<button class='btn-pdf no-print' onclick='imprimirPDF()'>Descargar como PDF</button>");
            printWriter.println("<h1>REPORTE DE PAGOS - HOTEL M4</h1>");
            printWriter.println("<h2>Fecha: " + java.time.LocalDate.now() + "</h2>");
            printWriter.println("<table>");
            printWriter.println("<thead><tr><th>ID PAGO</th><th>NOMBRE HUESPED</th><th>NUMERO DE HABITACION</th><th>MONTO</th><th>METODO DE PAGO</th><th>FECHA DE PAGO</th></tr></thead>");
            printWriter.println("<tbody>");
            
            for (Pagos p : pagos) {
                printWriter.println("<tr>");
                printWriter.println("<td>" + p.getIdPago() + "</td>");
                printWriter.println("<td>" + p.getNombreHuesped() + "</td>");
                printWriter.println("<td>" + (p.getNumeroHabitacion() != null ? p.getNumeroHabitacion() : "Sin asignar") + "</td>");
                printWriter.println("<td>$" + String.format("%.2f", p.getMonto()) + "</td>");
                printWriter.println("<td>" + p.getMetodoPago() + "</td>");
                printWriter.println("<td>" + p.getFechaPago() + "</td>");
                printWriter.println("</tr>");
            }
            
            printWriter.println("</tbody></table>");
            printWriter.println("<p class='total'>Total de pagos: " + pagos.size() + "</p>");
            printWriter.println("</body></html>");
            printWriter.close();
            
            java.awt.Desktop.getDesktop().browse(new java.io.File(desktopPath).toURI());
            
            JOptionPane.showMessageDialog(this, 
                "Reporte abierto en navegador.\nEn el dialogo de impresion:\n- Destino: Guardar como PDF\n- Click en Guardar", 
                "Guardar como PDF", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al generar reporte: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}


