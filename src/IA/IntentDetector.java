package IA;

import java.util.HashMap;
import java.util.Map;

public class IntentDetector {

    private final Map<String, String[]> intents = new HashMap<>();

        public IntentDetector() {
        intents.put("SALUDO", new String[]{
            "hola", "buenos dias", "buenas tardes", "buenas noches", "que tal", "saludos", "hey", "holi", "holis", "buen dia", "buenas"
        });
        intents.put("AGREGAR_CLIENTE", new String[]{
            "agrega", "registrar", "añadir", "nuevo cliente", "nuevo huésped"
        });
        intents.put("CONTAR_CLIENTES", new String[]{
            "cuántos clientes", "cuántos huéspedes", "número de clientes", "número de huéspedes"
        });
        intents.put("CONSULTAR_DISPONIBILIDAD", new String[]{
            "habitaciones disponibles", "cuántas habitaciones disponibles", "disponibilidad de habitaciones"
        });
        intents.put("CONSULTAR_OCUPADAS", new String[]{
            "habitaciones ocupadas", "cuáles están ocupadas", "qué habitaciones están ocupadas"
        });
        intents.put("CONSULTAR_MANTENIMIENTO", new String[]{
            "habitaciones en mantenimiento", "qué habitaciones están en mantenimiento", "mantenimiento"
        });
        intents.put("LIBERAR_HABITACION", new String[]{
            "liberar habitación", "ya no está ocupada", "desocupar habitación"
        });
        intents.put("LIBERAR_MANTENIMIENTO", new String[]{
            "quitar mantenimiento", "ya no está en mantenimiento", "habilitar habitación"
        });
        intents.put("ESTADO_HABITACION", new String[]{
            "estado de la habitación", "cómo está la habitación", "qué estado tiene la habitación"
        });
        intents.put("DETALLE_RESERVA", new String[]{
            "detalle de reserva", "información de reserva", "consulta reserva"
        });
        intents.put("LISTAR_CLIENTES", new String[]{
            "listar clientes", "mostrar huéspedes", "ver todos los clientes"
        });
        intents.put("LISTAR_RESERVAS", new String[]{
            "listar reservas", "mostrar reservas", "ver todas las reservas"
        });
        intents.put("LISTAR_HABITACIONES", new String[]{
            "listar habitaciones", "mostrar habitaciones", "ver todas las habitaciones"
        });
        intents.put("HISTORIAL_CLIENTE", new String[]{
            "historial de cliente", "historial de huésped", "historial de un cliente"
        });
        intents.put("HISTORIAL_HABITACION", new String[]{
            "historial de habitación", "historial de una habitación"
        });
        }

    public String detectIntent(String input) {
        String normalized = input.toLowerCase()
            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
            .replace("ü", "u").replace("ñ", "n");

        // Sinónimos y palabras clave adicionales
        String[][] synonyms = {
            {"agregar", "añadir", "registrar", "nuevo", "incluir"},
            {"cliente", "huesped", "persona", "usuario"},
            {"disponible", "libre", "desocupada"},
            {"ocupada", "ocupado", "usada", "reservada"},
            {"mantenimiento", "reparacion", "arreglo", "fuera de servicio"},
            {"habitacion", "cuarto", "sala", "pieza"},
            {"reserva", "apartado", "booking"},
            {"listar", "mostrar", "ver", "ensename", "dame"},
            {"historial", "historia", "registro", "pasado"},
            {"liberar", "desocupar", "habilitar", "quitar"},
            {"estado", "situacion", "condicion"}
        };
        for (String[] group : synonyms) {
            for (String word : group) {
                normalized = normalized.replace(word, group[0]);
            }
        }

        for (Map.Entry<String, String[]> entry : intents.entrySet()) {
            for (String pattern : entry.getValue()) {
                String patNorm = pattern.toLowerCase()
                    .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
                    .replace("ü", "u").replace("ñ", "n");
                if (normalized.contains(patNorm)) {
                    return entry.getKey();
                }
                // Coincidencia parcial: si todas las palabras del patrón están en la frase
                String[] patWords = patNorm.split(" ");
                boolean allPresent = true;
                for (String w : patWords) {
                    if (!normalized.contains(w)) {
                        allPresent = false;
                        break;
                    }
                }
                if (allPresent) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }
}