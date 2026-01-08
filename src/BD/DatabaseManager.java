package BD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class DatabaseManager {
    private static DatabaseManager instance;
    private String url;
    
    // Listeners para notificar cambios
    private List<Runnable> refreshListeners = new ArrayList<>();
    
    private DatabaseManager() {
        // Usar ruta relativa que siempre funciona
        url = "jdbc:sqlite:hotel.db";
        System.out.println("Base de datos: hotel.db (en directorio actual)");
        inicializarBaseDatos();
    }
    
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    // ============ MÉTODOS PARA ACTUALIZACIÓN AUTOMÁTICA ============
    public void addRefreshListener(Runnable listener) {
        if (!refreshListeners.contains(listener)) {
            refreshListeners.add(listener);
        }
    }
    
    private void notifyDataChanged() {
        for (Runnable listener : refreshListeners) {
            try {
                listener.run();
            } catch (Exception e) {
                // Silenciar errores para no romper la aplicación
            }
        }
    }
    // ======================================================================
    
    private void inicializarBaseDatos() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            
            // Tabla de habitaciones
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS habitaciones (
                    numero TEXT PRIMARY KEY,
                    categoria TEXT,
                    estado TEXT,
                    precio REAL,
                    huesped TEXT DEFAULT ''
                )
            """);
            
            // Tabla de huéspedes
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS huespedes (
                    documento TEXT PRIMARY KEY,
                    nombre TEXT,
                    apellido TEXT,
                    telefono TEXT,
                    email TEXT,
                    fecha_entrada TEXT,
                    fecha_salida TEXT,
                    habitacion_asignada TEXT DEFAULT ''
                )
            """);
            
            // Tabla de reservas
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS reservas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    huesped_nombre TEXT,
                    huesped_documento TEXT,
                    habitacion_numero TEXT,
                    fecha_entrada TEXT,
                    fecha_salida TEXT,
                    estado TEXT,
                    precio_total REAL,
                    metodo_pago TEXT DEFAULT '',
                    observaciones TEXT DEFAULT ''
                )
            """);
            
            // Inicializar habitaciones si no existen
            if (!existenHabitaciones()) {
                inicializarHabitaciones(conn);
            }
            
        } catch (SQLException e) {
            System.err.println("Error inicializando base de datos: " + e.getMessage());
            // Si hay error con archivo, usar memoria
            if (e.getMessage().contains("unable to open database file")) {
                System.err.println("Usando base de datos en memoria...");
                url = "jdbc:sqlite::memory:";
                inicializarBaseDatos(); // Reintentar con memoria
            }
        }
    }
    
    private boolean existenHabitaciones() {
        String sql = "SELECT COUNT(*) FROM habitaciones";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    
    private void inicializarHabitaciones(Connection conn) throws SQLException {
        String[][] habitaciones = {
            {"NRO-001", "Suite Presidencial", "DISPONIBLE", "350.0"},
            {"NRO-002", "Suite Ejecutiva", "DISPONIBLE", "250.0"},
            {"NRO-003", "Suite Ejecutiva", "DISPONIBLE", "250.0"},
            {"NRO-004", "Habitación Estándar", "DISPONIBLE", "120.0"},
            {"NRO-005", "Habitación Estándar", "DISPONIBLE", "120.0"},
            {"NRO-006", "Habitación Estándar", "DISPONIBLE", "120.0"},
            {"NRO-007", "Habitación Económica", "DISPONIBLE", "80.0"},
            {"NRO-008", "Habitación Económica", "DISPONIBLE", "80.0"},
            {"NRO-009", "Habitación Económica", "DISPONIBLE", "80.0"},
            {"NRO-010", "Habitación Económica", "DISPONIBLE", "80.0"}
        };
        
        String sql = "INSERT INTO habitaciones(numero, categoria, estado, precio) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] hab : habitaciones) {
                pstmt.setString(1, hab[0]);
                pstmt.setString(2, hab[1]);
                pstmt.setString(3, hab[2]);
                pstmt.setDouble(4, Double.parseDouble(hab[3]));
                pstmt.executeUpdate();
            }
        }
    }
    
    // ==================== MÉTODOS PARA HUÉSPEDES ====================
    
    public boolean agregarHuesped(Huesped huesped) {
        String sql = """
            INSERT INTO huespedes(documento, nombre, apellido, telefono, email, 
                                  fecha_entrada, fecha_salida, habitacion_asignada) 
            VALUES(?, ?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, huesped.getDocumento());
            pstmt.setString(2, huesped.getNombre());
            pstmt.setString(3, huesped.getApellido());
            pstmt.setString(4, huesped.getTelefono());
            pstmt.setString(5, huesped.getEmail());
            pstmt.setString(6, huesped.getFechaEntrada() != null ? 
                huesped.getFechaEntrada().toString() : null);
            pstmt.setString(7, huesped.getFechaSalida() != null ? 
                huesped.getFechaSalida().toString() : null);
            pstmt.setString(8, huesped.getHabitacionAsignada());
            
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<Huesped> obtenerTodosHuespedes() {
        ArrayList<Huesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM huespedes ORDER BY nombre";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Huesped h = new Huesped(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email")
                );
                
                String fechaEntrada = rs.getString("fecha_entrada");
                String fechaSalida = rs.getString("fecha_salida");
                
                if (fechaEntrada != null) {
                    h.setFechaEntrada(LocalDate.parse(fechaEntrada));
                }
                if (fechaSalida != null) {
                    h.setFechaSalida(LocalDate.parse(fechaSalida));
                }
                
                h.setHabitacionAsignada(rs.getString("habitacion_asignada"));
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Huesped> buscarHuespedes(String query) {
        ArrayList<Huesped> lista = new ArrayList<>();
        String sql = "SELECT * FROM huespedes WHERE nombre LIKE ? OR apellido LIKE ? OR documento LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            pstmt.setString(3, likeQuery);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Huesped h = new Huesped(
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("documento"),
                    rs.getString("telefono"),
                    rs.getString("email")
                );
                h.setHabitacionAsignada(rs.getString("habitacion_asignada"));
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public int contarTotalHuespedes() {
        String sql = "SELECT COUNT(*) FROM huespedes";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
    
    public boolean eliminarHuesped(String documento) {
        String sql = "DELETE FROM huespedes WHERE documento = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, documento);
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarHuesped(String documentoOriginal, Huesped nuevo) {
        String sql = """
            UPDATE huespedes SET 
                nombre = ?, 
                apellido = ?, 
                telefono = ?, 
                email = ?, 
                habitacion_asignada = ?
            WHERE documento = ?
        """;
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevo.getNombre());
            pstmt.setString(2, nuevo.getApellido());
            pstmt.setString(3, nuevo.getTelefono());
            pstmt.setString(4, nuevo.getEmail());
            pstmt.setString(5, nuevo.getHabitacionAsignada());
            pstmt.setString(6, documentoOriginal);
            
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== MÉTODOS PARA HABITACIONES ====================
    
    public ArrayList<Habitacion> obtenerTodasHabitaciones() {
        ArrayList<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones ORDER BY numero";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Habitacion hab = new Habitacion(
                    rs.getString("numero"),
                    rs.getString("categoria"),
                    rs.getString("estado"),
                    rs.getDouble("precio")
                );
                hab.setHuesped(rs.getString("huesped"));
                lista.add(hab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Habitacion> obtenerHabitacionesPorEstado(String estado) {
        ArrayList<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones WHERE estado = ? ORDER BY numero";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Habitacion hab = new Habitacion(
                    rs.getString("numero"),
                    rs.getString("categoria"),
                    rs.getString("estado"),
                    rs.getDouble("precio")
                );
                hab.setHuesped(rs.getString("huesped"));
                lista.add(hab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Habitacion> buscarHabitaciones(String query) {
        ArrayList<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT * FROM habitaciones WHERE numero LIKE ? OR estado LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Habitacion hab = new Habitacion(
                    rs.getString("numero"),
                    rs.getString("categoria"),
                    rs.getString("estado"),
                    rs.getDouble("precio")
                );
                hab.setHuesped(rs.getString("huesped"));
                lista.add(hab);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean actualizarEstadoHabitacion(String numero, String nuevoEstado) {
        String sql = "UPDATE habitaciones SET estado = ? WHERE numero = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, numero);
            
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int contarHabitacionesPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM habitaciones WHERE estado = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1);
        } catch (SQLException e) {
            return 0;
        }
    }
    
    public double calcularOcupacion() {
        int total = obtenerTodasHabitaciones().size();
        int ocupadas = contarHabitacionesPorEstado("OCUPADA");
        if (total == 0) return 0.0;
        return (ocupadas * 100.0) / total;
    }
    
    // ==================== MÉTODOS PARA RESERVAS ====================
    
    public boolean agregarReserva(Reserva reserva) {
        String sql = """
            INSERT INTO reservas(huesped_nombre, huesped_documento, habitacion_numero,
                               fecha_entrada, fecha_salida, estado, precio_total)
            VALUES(?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reserva.getHuespedNombre());
            pstmt.setString(2, reserva.getHuespedDocumento());
            pstmt.setString(3, reserva.getHabitacionNumero());
            pstmt.setString(4, reserva.getFechaEntrada().toString());
            pstmt.setString(5, reserva.getFechaSalida().toString());
            pstmt.setString(6, reserva.getEstado());
            pstmt.setDouble(7, reserva.getPrecioTotal());
            
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<Reserva> obtenerTodasReservas() {
        ArrayList<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas ORDER BY fecha_entrada DESC";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reserva r = new Reserva(
                    rs.getInt("id"),
                    rs.getString("huesped_nombre"),
                    rs.getString("huesped_documento"),
                    rs.getString("habitacion_numero"),
                    LocalDate.parse(rs.getString("fecha_entrada")),
                    LocalDate.parse(rs.getString("fecha_salida")),
                    rs.getString("estado"),
                    rs.getDouble("precio_total")
                );
                r.setMetodoPago(rs.getString("metodo_pago"));
                r.setObservaciones(rs.getString("observaciones"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Reserva> obtenerReservasPorEstado(String estado) {
        ArrayList<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE estado = ? ORDER BY fecha_entrada DESC";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Reserva r = new Reserva(
                    rs.getInt("id"),
                    rs.getString("huesped_nombre"),
                    rs.getString("huesped_documento"),
                    rs.getString("habitacion_numero"),
                    LocalDate.parse(rs.getString("fecha_entrada")),
                    LocalDate.parse(rs.getString("fecha_salida")),
                    rs.getString("estado"),
                    rs.getDouble("precio_total")
                );
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Reserva> buscarReservas(String query) {
        ArrayList<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM reservas WHERE huesped_nombre LIKE ? OR habitacion_numero LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva(
                    rs.getInt("id"),
                    rs.getString("huesped_nombre"),
                    rs.getString("huesped_documento"),
                    rs.getString("habitacion_numero"),
                    LocalDate.parse(rs.getString("fecha_entrada")),
                    LocalDate.parse(rs.getString("fecha_salida")),
                    rs.getString("estado"),
                    rs.getDouble("precio_total")
                );
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean eliminarReserva(int id) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarReserva(Reserva reserva) {
        String sql = """
            UPDATE reservas SET 
                huesped_nombre = ?,
                huesped_documento = ?,
                habitacion_numero = ?,
                fecha_entrada = ?,
                fecha_salida = ?,
                estado = ?,
                precio_total = ?
            WHERE id = ?
        """;
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reserva.getHuespedNombre());
            pstmt.setString(2, reserva.getHuespedDocumento());
            pstmt.setString(3, reserva.getHabitacionNumero());
            pstmt.setString(4, reserva.getFechaEntrada().toString());
            pstmt.setString(5, reserva.getFechaSalida().toString());
            pstmt.setString(6, reserva.getEstado());
            pstmt.setDouble(7, reserva.getPrecioTotal());
            pstmt.setInt(8, reserva.getId());
            
            boolean exito = pstmt.executeUpdate() > 0;
            if (exito) {
                notifyDataChanged();
            }
            return exito;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}