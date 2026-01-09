package IA;

import java.util.Map;
import BD.DatabaseManager;
import BD.Habitacion;
import BD.Huesped;
import BD.Reserva;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class NlpEngine {

    private final IntentDetector intentDetector;
    private final EntityExtractor entityExtractor;
    private final ContextManager contextManager;

    public NlpEngine() {
        this.intentDetector = new IntentDetector();
        this.entityExtractor = new EntityExtractor();
        this.contextManager = new ContextManager();
    }

    public IaResponse process(String userInput) {
        String intent = intentDetector.detectIntent(userInput);
        if (intent == null) {
            return IaResponse.outOfDomain();
        }
        Map<String, String> entities = entityExtractor.extract(userInput);
        contextManager.update(intent, entities);
        if (!contextManager.isComplete(intent)) {
            return IaResponse.missingData(contextManager.getMissingMessage());
        }

        DatabaseManager db = DatabaseManager.getInstance();
        try {
            switch (intent) {
                case "SALUDO": {
                    return IaResponse.withMessage("Saludo", Map.of());
                }
                case "AGREGAR_CLIENTE": {
                    String nombre = entities.getOrDefault("nombre", "");
                    String habitacion = entities.getOrDefault("habitacion", "");
                    
                    // Crear documento único para evitar conflictos
                    String documento = "IA-" + System.currentTimeMillis() % 10000;
                    
                    Huesped h = new Huesped(nombre, "Apellido", documento, "000-0000000", "ia@hotel.com");
                    h.setHabitacionAsignada(habitacion);
                    
                    // Agregar huésped y actualizar estado de habitación
                    boolean agregado = db.agregarHuesped(h);
                    boolean actualizado = db.actualizarEstadoHabitacion(habitacion, "OCUPADA");
                    
                    if (agregado && actualizado) {
                        return IaResponse.withMessage("Agregar_Cliente_OK", Map.of("habitacion", habitacion));
                    } else {
                        return IaResponse.withMessage("Error_Procesamiento", Map.of());
                    }
                }
                case "CONTAR_CLIENTES": {
                    int cantidad = db.contarTotalHuespedes();
                    return IaResponse.withMessage("Contar_Clientes", Map.of("cantidad", String.valueOf(cantidad)));
                }
                case "CONSULTAR_DISPONIBILIDAD": {
                    ArrayList<Habitacion> disponibles = db.obtenerHabitacionesPorEstado("DISPONIBLE");
                    String lista = disponibles.stream()
                        .map(Habitacion::getNumero)
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay habitaciones disponibles";
                    }
                    return IaResponse.withMessage("Habitaciones_Disponibles", Map.of("habitaciones", lista));
                }
                case "CONSULTAR_OCUPADAS": {
                    ArrayList<Habitacion> ocupadas = db.obtenerHabitacionesPorEstado("OCUPADA");
                    String lista = ocupadas.stream()
                        .map(Habitacion::getNumero)
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay habitaciones ocupadas";
                    }
                    return IaResponse.withMessage("Habitaciones_Ocupadas", Map.of("habitaciones", lista));
                }
                case "CONSULTAR_MANTENIMIENTO": {
                    ArrayList<Habitacion> mant = db.obtenerHabitacionesPorEstado("MANTENIMIENTO");
                    String lista = mant.stream()
                        .map(Habitacion::getNumero)
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay habitaciones en mantenimiento";
                    }
                    return IaResponse.withMessage("Habitaciones_Mantenimiento", Map.of("habitaciones", lista));
                }
                case "LIBERAR_HABITACION": {
                    String habitacion = entities.getOrDefault("habitacion", "");
                    boolean actualizado = db.actualizarEstadoHabitacion(habitacion, "DISPONIBLE");
                    
                    if (actualizado) {
                        return IaResponse.withMessage("Habitacion_Liberada", Map.of("habitacion", habitacion));
                    } else {
                        return IaResponse.withMessage("Error_Procesamiento", Map.of());
                    }
                }
                case "LIBERAR_MANTENIMIENTO": {
                    String habitacion = entities.getOrDefault("habitacion", "");
                    boolean actualizado = db.actualizarEstadoHabitacion(habitacion, "DISPONIBLE");
                    
                    if (actualizado) {
                        return IaResponse.withMessage("Mantenimiento_Liberado", Map.of("habitacion", habitacion));
                    } else {
                        return IaResponse.withMessage("Error_Procesamiento", Map.of());
                    }
                }
                case "ESTADO_HABITACION": {
                    String habitacion = entities.getOrDefault("habitacion", "");
                    ArrayList<Habitacion> lista = db.buscarHabitaciones(habitacion);
                    String estado = lista.isEmpty() ? "No encontrada" : lista.get(0).getEstado();
                    return IaResponse.withMessage("Estado_Habitacion", Map.of("habitacion", habitacion, "estado", estado));
                }
                case "DETALLE_RESERVA": {
                    String id = entities.getOrDefault("reserva_id", "");
                    ArrayList<Reserva> reservas = db.obtenerTodasReservas();
                    Reserva r = reservas.stream()
                        .filter(res -> String.valueOf(res.getId()).equals(id))
                        .findFirst()
                        .orElse(null);
                    
                    if (r == null) {
                        return IaResponse.withMessage("Detalle_Reserva", Map.of(
                            "id", id, 
                            "nombre", "No encontrado", 
                            "habitacion", "-", 
                            "entrada", "-", 
                            "salida", "-", 
                            "estado", "No encontrada"
                        ));
                    }
                    
                    return IaResponse.withMessage("Detalle_Reserva", Map.of(
                        "id", String.valueOf(r.getId()),
                        "nombre", r.getHuespedNombre(),
                        "habitacion", r.getHabitacionNumero(),
                        "entrada", r.getFechaEntrada().toString(),
                        "salida", r.getFechaSalida().toString(),
                        "estado", r.getEstado()
                    ));
                }
                case "LISTAR_CLIENTES": {
                    ArrayList<Huesped> huespedes = db.obtenerTodosHuespedes();
                    String lista = huespedes.stream()
                        .map(Huesped::getNombre)
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay clientes registrados";
                    }
                    return IaResponse.withMessage("Lista_Clientes", Map.of("clientes", lista));
                }
                case "LISTAR_RESERVAS": {
                    ArrayList<Reserva> reservas = db.obtenerTodasReservas();
                    String lista = reservas.stream()
                        .map(r -> "#" + r.getId() + " (" + r.getHuespedNombre() + ")")
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay reservas";
                    }
                    return IaResponse.withMessage("Lista_Reservas", Map.of("reservas", lista));
                }
                case "LISTAR_HABITACIONES": {
                    ArrayList<Habitacion> habitaciones = db.obtenerTodasHabitaciones();
                    String lista = habitaciones.stream()
                        .map(Habitacion::getNumero)
                        .collect(Collectors.joining(", "));
                    if (lista.isEmpty()) {
                        lista = "No hay habitaciones";
                    }
                    return IaResponse.withMessage("Lista_Habitaciones", Map.of("habitaciones", lista));
                }
                case "HISTORIAL_CLIENTE": {
                    String nombre = entities.getOrDefault("nombre", "");
                    ArrayList<Reserva> reservas = db.obtenerTodasReservas();
                    String historial = reservas.stream()
                        .filter(r -> r.getHuespedNombre().toLowerCase().contains(nombre.toLowerCase()))
                        .map(r -> "#" + r.getId() + " (" + r.getHabitacionNumero() + ")")
                        .collect(Collectors.joining(", "));
                    if (historial.isEmpty()) {
                        historial = "No tiene historial";
                    }
                    return IaResponse.withMessage("Historial_Cliente", Map.of("nombre", nombre, "historial", historial));
                }
                case "HISTORIAL_HABITACION": {
                    String habitacion = entities.getOrDefault("habitacion", "");
                    ArrayList<Reserva> reservas = db.obtenerTodasReservas();
                    String historial = reservas.stream()
                        .filter(r -> r.getHabitacionNumero().equalsIgnoreCase(habitacion))
                        .map(r -> "#" + r.getId() + " (" + r.getHuespedNombre() + ")")
                        .collect(Collectors.joining(", "));
                    if (historial.isEmpty()) {
                        historial = "No tiene historial";
                    }
                    return IaResponse.withMessage("Historial_Habitacion", Map.of("habitacion", habitacion, "historial", historial));
                }
                default:
                    return IaResponse.outOfDomain();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return IaResponse.withMessage("Error_Procesamiento", Map.of());
        }
    }
}