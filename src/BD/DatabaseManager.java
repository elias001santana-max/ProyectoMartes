package BD;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
	private static DatabaseManager instance;
	private Connection connection;
	private static final String DB_PATH = "src/BD/BDHotel.db";
	
	private DatabaseManager() {
		conectar();
		crearTablasNecesarias();
	}
	
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
			// Ensure connection is closed when app exits
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (instance != null) {
					instance.cerrarConexion();
				}
			}));
		}
		return instance;
	}
	
	private void conectar() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
			System.out.println("✓ Conexión exitosa a la base de datos");
		} catch (Exception e) {
			System.err.println("✗ Error al conectar con la base de datos: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void crearTablasNecesarias() {
		try {
			Statement stmt = connection.createStatement();
			
			// Las tablas se crearán solo si no existen
			
			
			// 1. Tabla Habitaciones
			String sqlHabitaciones = "CREATE TABLE IF NOT EXISTS habitaciones (" +
					"numero TEXT PRIMARY KEY," +
					"categoria TEXT NOT NULL," +
					"estado TEXT NOT NULL," + // DISPONIBLE, OCUPADA, MANTENIMIENTO
					"precio REAL NOT NULL" +
					")";
			stmt.execute(sqlHabitaciones);
			
			// 2. Tabla Huéspedes
			String sqlHuespedes = "CREATE TABLE IF NOT EXISTS huespedes (" +
					"documento TEXT PRIMARY KEY," +
					"nombre TEXT NOT NULL," +
					"apellido TEXT NOT NULL," +
					"telefono TEXT," +
					"email TEXT," +
					"habitacion_asignada TEXT" +
					")";
			stmt.execute(sqlHuespedes);
			
			// 3. Tabla Reservas
			String sqlReservas = "CREATE TABLE IF NOT EXISTS reservas (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"huesped_nombre TEXT NOT NULL," +
					"huesped_documento TEXT NOT NULL," +
					"habitacion_numero TEXT NOT NULL," +
					"fecha_entrada TEXT NOT NULL," +
					"fecha_salida TEXT NOT NULL," +
					"estado TEXT NOT NULL," + // PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA
					"precio_total REAL NOT NULL," +
					"metodo_pago TEXT," +
					"observaciones TEXT" +
					")";
			stmt.execute(sqlReservas);
			
			// Insertar datos iniciales de habitaciones si la tabla está vacía
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM habitaciones");
			if (rs.next() && rs.getInt(1) == 0) {
				insertarHabitacionesIniciales();
			}
			
			System.out.println("✓ Tablas verificadas/creadas correctamente");
		} catch (SQLException e) {
			System.err.println("✗ Error al crear tablas: " + e.getMessage());
		}
	}
	
	private void insertarHabitacionesIniciales() {
		String[] categorias = {"INDIVIDUAL", "DOBLE", "SUITE", "PRESIDENCIAL"};
		
		try {
			connection.setAutoCommit(false);
			String sql = "INSERT INTO habitaciones (numero, categoria, estado, precio) VALUES (?, ?, ?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			for (int i = 1; i <= 16; i++) {
				String numero = String.format("NRO-%03d", i);
				String categoria = categorias[(i - 1) % 4];
				String estado = "DISPONIBLE"; // Inicialmente todas disponibles
				double precio = 50 + (i * 10);
				
				// Simulamos algunas ocupadas y en mantenimiento para que se vea actividad
				if (i % 5 == 0) estado = "OCUPADA";
				else if (i % 7 == 0) estado = "MANTENIMIENTO";
				
				pstmt.setString(1, numero);
				pstmt.setString(2, categoria);
				pstmt.setString(3, estado);
				pstmt.setDouble(4, precio);
				pstmt.addBatch();
				
				// Si está ocupada, insertamos un huésped ficticio para esa habitación
				if (estado.equals("OCUPADA")) {
					insertarHuespedFicticio(numero, i);
				}
			}
			
			pstmt.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);
			System.out.println("✓ Datos iniciales insertados");
		} catch (SQLException e) {
			System.err.println("Error al insertar datos iniciales: " + e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void insertarHuespedFicticio(String habitacion, int i) {
		try {
			String sql = "INSERT INTO huespedes (documento, nombre, apellido, telefono, email, habitacion_asignada) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "DOC-" + (1000 + i));
			pstmt.setString(2, "Huesped");
			pstmt.setString(3, "Prueba " + i);
			pstmt.setString(4, "555-01" + String.format("%02d", i));
			pstmt.setString(5, "huesped" + i + "@hotel.com");
			pstmt.setString(6, habitacion);
			pstmt.executeUpdate();
			
			// También crear una reserva activa para este huésped
			String sqlRes = "INSERT INTO reservas (huesped_nombre, huesped_documento, habitacion_numero, fecha_entrada, fecha_salida, estado, precio_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmtRes = connection.prepareStatement(sqlRes);
			pstmtRes.setString(1, "Huesped Prueba " + i);
			pstmtRes.setString(2, "DOC-" + (1000 + i));
			pstmtRes.setString(3, habitacion);
			pstmtRes.setString(4, LocalDate.now().toString());
			pstmtRes.setString(5, LocalDate.now().plusDays(3).toString());
			pstmtRes.setString(6, "CONFIRMADA");
			pstmtRes.setDouble(7, 300.0);
			pstmtRes.executeUpdate();
			
		} catch (SQLException e) {
			System.err.println("Error creating dummy guest: " + e.getMessage());
		}
	}
	
	// ==================== HABITACIONES ====================
	
	public ArrayList<Habitacion> obtenerTodasHabitaciones() {
		ArrayList<Habitacion> habitaciones = new ArrayList<>();
		try {
			String sql = "SELECT numero, categoria, estado, precio FROM habitaciones";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Habitacion hab = new Habitacion(
					rs.getString("numero"),
					rs.getString("categoria"),
					rs.getString("estado"),
					rs.getDouble("precio")
				);
				habitaciones.add(hab);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener habitaciones: " + e.getMessage());
		}
		return habitaciones;
	}
	
	public ArrayList<Habitacion> obtenerHabitacionesPorEstado(String estado) {
		ArrayList<Habitacion> habitaciones = new ArrayList<>();
		try {
			String sql = "SELECT numero, categoria, estado, precio FROM habitaciones WHERE estado = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, estado);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Habitacion hab = new Habitacion(
					rs.getString("numero"),
					rs.getString("categoria"),
					rs.getString("estado"),
					rs.getDouble("precio")
				);
				habitaciones.add(hab);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener habitaciones por estado: " + e.getMessage());
		}
		return habitaciones;
	}
	
	public boolean actualizarEstadoHabitacion(String numero, String nuevoEstado) {
		try {
			String sql = "UPDATE habitaciones SET estado = ? WHERE numero = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, nuevoEstado);
			pstmt.setString(2, numero);
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar estado de habitación: " + e.getMessage());
			return false;
		}
	}

	public ArrayList<Habitacion> buscarHabitaciones(String query) {
		ArrayList<Habitacion> habitaciones = new ArrayList<>();
		try {
			String sql = "SELECT * FROM habitaciones WHERE numero LIKE ? OR categoria LIKE ? OR estado LIKE ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			String searchPattern = "%" + query + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Habitacion hab = new Habitacion(
					rs.getString("numero"),
					rs.getString("categoria"),
					rs.getString("estado"),
					rs.getDouble("precio")
				);
				habitaciones.add(hab);
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar habitaciones: " + e.getMessage());
		}
		return habitaciones;
	}
	
	// ==================== HUÉSPEDES ====================
	
	public ArrayList<Huesped> obtenerTodosHuespedes() {
		ArrayList<Huesped> huespedes = new ArrayList<>();
		try {
			String sql = "SELECT nombre, apellido, documento, telefono, email, habitacion_asignada FROM huespedes";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				Huesped h = new Huesped(
					rs.getString("nombre"),
					rs.getString("apellido"),
					rs.getString("documento"),
					rs.getString("telefono"),
					rs.getString("email")
				);
				String habAsignada = rs.getString("habitacion_asignada");
				if (habAsignada != null && !habAsignada.isEmpty()) {
					h.setHabitacionAsignada(habAsignada);
				}
				huespedes.add(h);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener huéspedes: " + e.getMessage());
		}
		return huespedes;
	}
	
	public boolean agregarHuesped(Huesped huesped) {
		try {
			String sql = "INSERT INTO huespedes (nombre, apellido, documento, telefono, email, habitacion_asignada) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, huesped.getNombre());
			pstmt.setString(2, huesped.getApellido());
			pstmt.setString(3, huesped.getDocumento());
			pstmt.setString(4, huesped.getTelefono());
			pstmt.setString(5, huesped.getEmail());
			pstmt.setString(6, huesped.getHabitacionAsignada());
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al agregar huésped: " + e.getMessage());
			return false;
		}
	}
	
	public boolean actualizarHuesped(String documento, Huesped huesped) {
		try {
			String sql = "UPDATE huespedes SET nombre = ?, apellido = ?, telefono = ?, email = ?, habitacion_asignada = ? WHERE documento = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, huesped.getNombre());
			pstmt.setString(2, huesped.getApellido());
			pstmt.setString(3, huesped.getTelefono());
			pstmt.setString(4, huesped.getEmail());
			pstmt.setString(5, huesped.getHabitacionAsignada());
			pstmt.setString(6, documento);
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar huésped: " + e.getMessage());
			return false;
		}
	}
	
	public boolean eliminarHuesped(String documento) {
		try {
			String sql = "DELETE FROM huespedes WHERE documento = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, documento);
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al eliminar huésped: " + e.getMessage());
			return false;
		}
	}

	public ArrayList<Huesped> buscarHuespedes(String query) {
		ArrayList<Huesped> huespedes = new ArrayList<>();
		try {
			String sql = "SELECT * FROM huespedes WHERE nombre LIKE ? OR apellido LIKE ? OR documento LIKE ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			String searchPattern = "%" + query + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Huesped h = new Huesped(
					rs.getString("nombre"),
					rs.getString("apellido"),
					rs.getString("documento"),
					rs.getString("telefono"),
					rs.getString("email")
				);
				String habAsignada = rs.getString("habitacion_asignada");
				if (habAsignada != null && !habAsignada.isEmpty()) {
					h.setHabitacionAsignada(habAsignada);
				}
				huespedes.add(h);
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar huéspedes: " + e.getMessage());
		}
		return huespedes;
	}
	
	// ==================== RESERVAS ====================
	
	public ArrayList<Reserva> obtenerTodasReservas() {
		ArrayList<Reserva> reservas = new ArrayList<>();
		try {
			String sql = "SELECT * FROM reservas ORDER BY fecha_entrada DESC";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
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
				reservas.add(r);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener reservas: " + e.getMessage());
		}
		return reservas;
	}
	
	public boolean agregarReserva(Reserva reserva) {
		try {
			String sql = "INSERT INTO reservas (huesped_nombre, huesped_documento, habitacion_numero, fecha_entrada, fecha_salida, estado, precio_total, metodo_pago, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, reserva.getHuespedNombre());
			pstmt.setString(2, reserva.getHuespedDocumento());
			pstmt.setString(3, reserva.getHabitacionNumero());
			pstmt.setString(4, reserva.getFechaEntrada().toString());
			pstmt.setString(5, reserva.getFechaSalida().toString());
			pstmt.setString(6, reserva.getEstado());
			pstmt.setDouble(7, reserva.getPrecioTotal());
			pstmt.setString(8, reserva.getMetodoPago());
			pstmt.setString(9, reserva.getObservaciones());
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al agregar reserva: " + e.getMessage());
			return false;
		}
	}
	
	public boolean actualizarEstadoReserva(int id, String nuevoEstado) {
		try {
			String sql = "UPDATE reservas SET estado = ? WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, nuevoEstado);
			pstmt.setInt(2, id);
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar estado de reserva: " + e.getMessage());
			return false;
		}
	}

	public boolean actualizarReserva(Reserva reserva) {
		try {
			String sql = "UPDATE reservas SET huesped_nombre = ?, huesped_documento = ?, habitacion_numero = ?, fecha_entrada = ?, fecha_salida = ?, precio_total = ? WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, reserva.getHuespedNombre());
			pstmt.setString(2, reserva.getHuespedDocumento());
			pstmt.setString(3, reserva.getHabitacionNumero());
			pstmt.setString(4, reserva.getFechaEntrada().toString());
			pstmt.setString(5, reserva.getFechaSalida().toString());
			pstmt.setDouble(6, reserva.getPrecioTotal());
			pstmt.setInt(7, reserva.getId());
			
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al actualizar reserva: " + e.getMessage());
			return false;
		}
	}

	public boolean eliminarReserva(int id) {
		try {
			String sql = "DELETE FROM reservas WHERE id = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, id);
			int filasAfectadas = pstmt.executeUpdate();
			return filasAfectadas > 0;
		} catch (SQLException e) {
			System.err.println("Error al eliminar reserva: " + e.getMessage());
			return false;
		}
	}

	public ArrayList<Reserva> obtenerReservasPorEstado(String estado) {
		ArrayList<Reserva> reservas = new ArrayList<>();
		try {
			String sql = "SELECT * FROM reservas WHERE estado = ? ORDER BY fecha_entrada DESC";
			PreparedStatement pstmt = connection.prepareStatement(sql);
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
				r.setMetodoPago(rs.getString("metodo_pago"));
				r.setObservaciones(rs.getString("observaciones"));
				reservas.add(r);
			}
		} catch (SQLException e) {
			System.err.println("Error al obtener reservas por estado: " + e.getMessage());
		}
		return reservas;
	}

	public ArrayList<Reserva> buscarReservas(String query) {
		ArrayList<Reserva> reservas = new ArrayList<>();
		try {
			String sql = "SELECT * FROM reservas WHERE huesped_nombre LIKE ? OR huesped_documento LIKE ? OR habitacion_numero LIKE ? ORDER BY fecha_entrada DESC";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			String searchPattern = "%" + query + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
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
				r.setMetodoPago(rs.getString("metodo_pago"));
				r.setObservaciones(rs.getString("observaciones"));
				reservas.add(r);
			}
		} catch (SQLException e) {
			System.err.println("Error al buscar reservas: " + e.getMessage());
		}
		return reservas;
	}
	
	// ==================== ESTADÍSTICAS ====================
	
	public int contarHabitacionesPorEstado(String estado) {
		try {
			String sql = "SELECT COUNT(*) as total FROM habitaciones WHERE estado = ?";
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, estado);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			System.err.println("Error al contar habitaciones: " + e.getMessage());
		}
		return 0;
	}
	
	public int contarTotalHuespedes() {
		try {
			String sql = "SELECT COUNT(*) as total FROM huespedes";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("total");
			}
		} catch (SQLException e) {
			System.err.println("Error al contar huéspedes: " + e.getMessage());
		}
		return 0;
	}
	
	public double calcularOcupacion() {
		try {
			String sql = "SELECT COUNT(*) as total FROM habitaciones";
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			int total = rs.next() ? rs.getInt("total") : 0;
			
			if (total == 0) return 0;
			
			int ocupadas = contarHabitacionesPorEstado("OCUPADA");
			return (ocupadas * 100.0) / total;
		} catch (SQLException e) {
			System.err.println("Error al calcular ocupación: " + e.getMessage());
		}
		return 0;
	}
	
	public void cerrarConexion() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
				System.out.println("✓ Conexión cerrada");
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar conexión: " + e.getMessage());
		}
	}
}
