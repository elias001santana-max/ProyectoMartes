package IA;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {

    private final Map<String, String> entities = new HashMap<>();

    public void update(String intent, Map<String, String> newEntities) {
        entities.putAll(newEntities);
    }

    public boolean isComplete(String intent) {
        switch (intent) {
            case "AGREGAR_CLIENTE":
                return entities.containsKey("habitacion") && entities.containsKey("nombre");
            case "LIBERAR_HABITACION":
            case "LIBERAR_MANTENIMIENTO":
            case "ESTADO_HABITACION":
            case "HISTORIAL_HABITACION":
                return entities.containsKey("habitacion");
            case "DETALLE_RESERVA":
                return entities.containsKey("reserva_id");
            case "HISTORIAL_CLIENTE":
                return entities.containsKey("nombre");
            default:
                return true;
        }
    }

    public Map<String, String> getEntities() {
        return entities;
    }

    public String getMissingMessage() {
        if (!entities.containsKey("nombre")) {
            return "¿Cuál es el nombre del cliente?";
        }
        if (!entities.containsKey("habitacion")) {
            return "¿En qué habitación?";
        }
        if (!entities.containsKey("reserva_id")) {
            return "¿Cuál es el número de reserva?";
        }
        return "Falta información.";
    }
}