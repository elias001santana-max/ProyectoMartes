package Boton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelHuespedes extends JPanel {
    private ArrayList<Huesped> huespedes;
    private JTable table;
    private DefaultTableModel tableModel;
    private DatabaseManager db;
    
    public PanelHuespedes() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        
        db = DatabaseManager.getInstance();
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
        
        // Cargar datos
        cargarDatos();
        
        // REGISTRAR ESTE PANEL PARA ACTUALIZACIONES AUTOMÁTICAS
        db.addRefreshListener(this::cargarDatos);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titulo = new JLabel("👥 Gestión de Huéspedes");
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
        table.getColumnModel().getColumn(0).setPreferredWidth(120); // Nombre
        table.getColumnModel().getColumn(1).setPreferredWidth(120); // Apellido
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Documento
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Teléfono
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Habitación
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Estado
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 246, 250));
        
        JButton btnAgregar = createButton("➕ Agregar Huésped", new Color(46, 204, 113));
        JButton btnEditar = createButton("✏️ Editar", new Color(52, 152, 219));
        JButton btnEliminar = createButton("🗑️ Eliminar", new Color(231, 76, 60));
        JButton btnActualizar = createButton("🔄 Actualizar", new Color(52, 73, 94));
        JButton btnPDF = createButton("📄 PDF", new Color(220, 53, 69));
        
        btnAgregar.addActionListener(e -> agregarHuesped());
        btnEditar.addActionListener(e -> editarHuesped());
        btnEliminar.addActionListener(e -> eliminarHuesped());
        btnActualizar.addActionListener(e -> cargarDatos());
        btnPDF.addActionListener(e -> exportarAPDF());
        
        panel.add(btnAgregar);
        panel.add(btnEditar);
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
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return btn;
    }
    
    private void cargarDatos() {
        huespedes = db.obtenerTodosHuespedes();
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
    
    private void agregarHuesped() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Agregar Huésped", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        JTextField txtNombre = new JTextField();
        JTextField txtApellido = new JTextField();
        JTextField txtDocumento = new JTextField();
        JTextField txtTelefono = new JTextField();
        JTextField txtEmail = new JTextField();
        
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtApellido);
        formPanel.add(new JLabel("Documento:"));
        formPanel.add(txtDocumento);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnGuardar = createButton("Guardar", new Color(46, 204, 113));
        JButton btnCancelar = createButton("Cancelar", new Color(231, 76, 60));
        
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty() || txtDocumento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nombre y documento son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Huesped nuevo = new Huesped(
                txtNombre.getText(),
                txtApellido.getText(),
                txtDocumento.getText(),
                txtTelefono.getText(),
                txtEmail.getText()
            );
            
            if (db.agregarHuesped(nuevo)) {
                JOptionPane.showMessageDialog(dialog, "Huésped agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al agregar huésped", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editarHuesped() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un huésped para editar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String documento = (String) table.getValueAt(selectedRow, 2);
        
        Huesped huesped = null;
        for (Huesped h : huespedes) {
            if (h.getDocumento().equals(documento)) {
                huesped = h;
                break;
            }
        }
        
        if (huesped == null) {
            cargarDatos();
            for (Huesped h : huespedes) {
                if (h.getDocumento().equals(documento)) {
                    huesped = h;
                    break;
                }
            }
        }
        
        if (huesped == null) {
            String nombre = (String) table.getValueAt(selectedRow, 0);
            String apellido = (String) table.getValueAt(selectedRow, 1);
            String telefono = (String) table.getValueAt(selectedRow, 3);
            huesped = new Huesped(nombre, apellido, documento, telefono, "");
            String habitacion = (String) table.getValueAt(selectedRow, 4);
            if (!habitacion.equals("Sin asignar")) {
                huesped.setHabitacionAsignada(habitacion);
            }
        }
        
        final Huesped huespedFinal = huesped;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Huésped", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);
        
        JTextField txtNombre = new JTextField(huespedFinal.getNombre());
        JTextField txtApellido = new JTextField(huespedFinal.getApellido());
        JTextField txtDocumento = new JTextField(huespedFinal.getDocumento());
        txtDocumento.setEditable(false);
        txtDocumento.setBackground(new Color(240, 240, 240));
        
        JTextField txtTelefono = new JTextField(huespedFinal.getTelefono());
        JTextField txtEmail = new JTextField(huespedFinal.getEmail());
        
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Apellido:"));
        formPanel.add(txtApellido);
        formPanel.add(new JLabel("Documento:"));
        formPanel.add(txtDocumento);
        formPanel.add(new JLabel("Teléfono:"));
        formPanel.add(txtTelefono);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnGuardar = createButton("Guardar Cambios", new Color(52, 152, 219));
        JButton btnCancelar = createButton("Cancelar", new Color(231, 76, 60));
        
        btnGuardar.addActionListener(e -> {
            if (txtNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Huesped actualizado = new Huesped(
                txtNombre.getText(),
                txtApellido.getText(),
                txtDocumento.getText(),
                txtTelefono.getText(),
                txtEmail.getText()
            );
            actualizado.setHabitacionAsignada(huespedFinal.getHabitacionAsignada());
            
            if (db.actualizarHuesped(huespedFinal.getDocumento(), actualizado)) {
                JOptionPane.showMessageDialog(dialog, "Huésped actualizado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error al actualizar huésped", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void eliminarHuesped() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un huésped para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String documento = (String) table.getValueAt(selectedRow, 2);
        String nombre = (String) table.getValueAt(selectedRow, 0);
        String apellido = (String) table.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar a " + nombre + " " + apellido + "?\nEsta acción no se puede deshacer.", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // PRIMERO: Obtener el nombre completo del huésped antes de eliminarlo
            Huesped huespedAEliminar = null;
            for (Huesped h : huespedes) {
                if (h.getDocumento().equals(documento)) {
                    huespedAEliminar = h;
                    break;
                }
            }
            
            if (huespedAEliminar == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el huésped", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String nombreCompleto = huespedAEliminar.getNombreCompleto();
            
            // SEGUNDO: Eliminar todos los pagos asociados a este huésped
            db.eliminarPagosPorNombreHuesped(nombreCompleto);
            
            // TERCERO: Eliminar el huésped
            if (db.eliminarHuesped(documento)) {
                JOptionPane.showMessageDialog(this, 
                    "Huésped eliminado exitosamente.\nSe eliminaron también todos los pagos asociados.", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar huésped.\nVerifique si tiene reservas activas.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void filtrar(String query) {
        if (query == null || query.trim().isEmpty() || query.equals("Buscar huéspedes, habitaciones, reservas...")) {
            cargarDatos();
            return;
        }
        
        huespedes = db.buscarHuespedes(query);
        actualizarTabla();
    }
    
    private void exportarAPDF() {
        try {
            String userHome = System.getProperty("user.home");
            String desktopPath = userHome + "\\Desktop\\Reporte_Huespedes_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".html";
            
            java.io.FileWriter writer = new java.io.FileWriter(desktopPath);
            java.io.PrintWriter printWriter = new java.io.PrintWriter(writer);
            
            printWriter.println("<!DOCTYPE html>");
            printWriter.println("<html>");
            printWriter.println("<head>");
            printWriter.println("<meta charset='UTF-8'>");
            printWriter.println("<title>Reporte de HUESPEDES - Hotel M4</title>");
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
            printWriter.println("function imprimirPDF() {");
            printWriter.println("  window.print();");
            printWriter.println("}");
            printWriter.println("window.onload = function() {");
            printWriter.println("  setTimeout(imprimirPDF, 500);");
            printWriter.println("};");
            printWriter.println("</script>");
            printWriter.println("</head>");
            printWriter.println("<body>");
            printWriter.println("<div style='text-align: center; margin-bottom: 20px;'>");
            printWriter.println("<img src='file:///" + userHome.replace("\\", "/") + "/eclipse-workspace/BotonPd/logo_hotel.png' style='width: 150px; height: 150px;' />");
            printWriter.println("</div>");
            printWriter.println("<button class='btn-pdf no-print' onclick='imprimirPDF()'>Descargar como PDF</button>");
            printWriter.println("<h1>?? REPORTE DE HUESPEDES - HOTEL M4</h1>");
            printWriter.println("<h2>Fecha: " + java.time.LocalDate.now() + "</h2>");
            printWriter.println("<table>");
            printWriter.println("<thead><tr>");
            printWriter.println("<th>NOMBRE</th><th>APELLIDO</th><th>DOCUMENTO</th><th>TELEFONO</th><th>HABITACION</th><th>ESTADO</th>");
            printWriter.println("</tr></thead>");
            printWriter.println("<tbody>");
            
            for (Huesped h : huespedes) {
                printWriter.println("<tr>");
                printWriter.println("<td>" + h.getNombre() + "</td>");
                printWriter.println("<td>" + h.getApellido() + "</td>");
                printWriter.println("<td>" + h.getDocumento() + "</td>");
                printWriter.println("<td>" + h.getTelefono() + "</td>");
                printWriter.println("<td>" + (h.getHabitacionAsignada() != null ? h.getHabitacionAsignada() : "Sin asignar") + "</td>");
                printWriter.println("<td>Registrado</td>");
                printWriter.println("</tr>");
            }
            
            printWriter.println("</tbody>");
            printWriter.println("</table>");
            printWriter.println("<p class='total'>Total de HUESPEDES: " + huespedes.size() + "</p>");
            printWriter.println("</body>");
            printWriter.println("</html>");
            
            printWriter.close();
            
            // Abrir en el navegador
            java.awt.Desktop.getDesktop().browse(new java.io.File(desktopPath).toURI());
            
            JOptionPane.showMessageDialog(this, 
                "Reporte abierto en navegador.\nEn el di�logo de impresi�n:\n- Destino: Guardar como PDF\n- Click en Guardar", 
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







