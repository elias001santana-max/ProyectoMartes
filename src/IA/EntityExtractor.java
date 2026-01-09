package IA;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityExtractor {

        private static final Pattern ROOM_PATTERN =
            Pattern.compile("NRO-\\d{3}"); // Ejemplo: NRO-001
        private static final Pattern RESERVA_ID_PATTERN =
            Pattern.compile("#(\\d+)");

    public Map<String, String> extract(String input) {

        Map<String, String> entities = new HashMap<>();

        // Habitación
        Matcher roomMatcher = ROOM_PATTERN.matcher(input);
        if (roomMatcher.find()) {
            entities.put("habitacion", roomMatcher.group());
        }

        // ID de reserva
        Matcher reservaMatcher = RESERVA_ID_PATTERN.matcher(input);
        if (reservaMatcher.find()) {
            entities.put("reserva_id", reservaMatcher.group(1));
        }

        // Nombre (mejor heurística: después de 'a', 'para', 'de', 'del cliente', etc.)
        String lower = input.toLowerCase();
        if (lower.contains("cliente ")) {
            String possibleName = input.substring(lower.indexOf("cliente ") + 8).trim();
            if (possibleName.length() > 2) entities.put("nombre", possibleName);
        } else if (lower.contains("huesped ")) {
            String possibleName = input.substring(lower.indexOf("huesped ") + 8).trim();
            if (possibleName.length() > 2) entities.put("nombre", possibleName);
        } else if (lower.contains("para ")) {
            String possibleName = input.substring(lower.indexOf("para ") + 5).trim();
            if (possibleName.length() > 2) entities.put("nombre", possibleName);
        } else if (lower.contains("a ")) {
            String possibleName = input.substring(lower.indexOf("a ") + 2).trim();
            if (possibleName.length() > 2) entities.put("nombre", possibleName);
        }

        // Estado
        if (lower.contains("disponible")) entities.put("estado", "DISPONIBLE");
        if (lower.contains("ocupada")) entities.put("estado", "OCUPADA");
        if (lower.contains("mantenimiento")) entities.put("estado", "MANTENIMIENTO");

        return entities;
    }
}