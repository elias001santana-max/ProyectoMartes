package IA;

import java.util.Map;

public class IaResponse {
    
    private String intent;
    private Map<String, String> entities;
    private boolean complete;
    private String message;
    
    public static IaResponse complete(String intent, Map<String, String> entities) {
        IaResponse r = new IaResponse();
        r.intent = intent;
        r.entities = entities;
        r.complete = true;
        r.message = renderTemplate(intent, entities);
        return r;
    }
    
    public static IaResponse missingData(String message) {
        IaResponse r = new IaResponse();
        r.complete = false;
        r.message = message;
        return r;
    }
    
    public static IaResponse outOfDomain() {
        IaResponse r = new IaResponse();
        r.complete = false;
        r.message = "Solo puedo ayudarte con la gestión del hotel.";
        return r;
    }
    
    public static IaResponse withMessage(String templateKey, Map<String, String> values) {
        IaResponse r = new IaResponse();
        r.complete = true;
        r.intent = templateKey;
        r.entities = values;
        r.message = renderTemplate(templateKey, values);
        return r;
    }
    
    private static String renderTemplate(String key, Map<String, String> values) {
        try {
            String filePath = "src/IA/responses.json";
            java.nio.file.Path path = java.nio.file.Paths.get(filePath);
            
            if (!java.nio.file.Files.exists(path)) {
                return generateFallbackResponse(key, values);
            }
            
            String json = new String(java.nio.file.Files.readAllBytes(path), 
                                     java.nio.charset.StandardCharsets.UTF_8);
            
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                "\"" + java.util.regex.Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
            java.util.regex.Matcher m = p.matcher(json);
            
            if (m.find()) {
                String template = m.group(1);
                
                if (values != null) {
                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        String placeholder = "{{" + entry.getKey() + "}}";
                        template = template.replace(placeholder, 
                            entry.getValue() != null ? entry.getValue() : "");
                    }
                }
                return template;
            } else {
                return generateFallbackResponse(key, values);
            }
            
        } catch (Exception e) {
            return generateFallbackResponse(key, values);
        }
    }
    
    private static String generateFallbackResponse(String key, Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return key;
        }
        StringBuilder sb = new StringBuilder(key).append(": ");
        for (Map.Entry<String, String> entry : values.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return sb.toString().trim();
    }
    
    // Getters
    public String getIntent() { 
        return intent; 
    }
    
    public Map<String, String> getEntities() { 
        return entities; 
    }
    
    public boolean isComplete() { 
        return complete; 
    }
    
    public String getMessage() { 
        return message; 
    }
    
    @Override
    public String toString() {
        return "IaResponse{" +
               "intent='" + intent + '\'' +
               ", entities=" + entities +
               ", complete=" + complete +
               ", message='" + message + '\'' +
               '}';
    }
}