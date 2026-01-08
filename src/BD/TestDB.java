package BD;

import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:C:/Users/usuario/Desktop/ProyectoMartes/BDHotel.db";
        
        System.out.println("=== TEST DE BASE DE DATOS ===\n");
        
        // Verificar si la BD existe y tiene la tabla
        try (Connection con = DriverManager.getConnection(url);
             Statement stmt = con.createStatement()) {
            
            System.out.println("OK Conexion a BD exitosa");
            
            // Verificar tabla usuarios
            ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='usuarios'");
            if (rs.next()) {
                System.out.println("OK Tabla usuarios existe");
            } else {
                System.out.println("ERROR Tabla usuarios NO existe");
                return;
            }
            
            // Contar usuarios
            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM usuarios");
            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("OK Total de usuarios: " + total);
            }
            
            // Mostrar todos los usuarios
            rs = stmt.executeQuery("SELECT usuario, nombre FROM usuarios");
            System.out.println("\n=== USUARIOS REGISTRADOS ===");
            boolean hayUsuarios = false;
            while (rs.next()) {
                hayUsuarios = true;
                System.out.println("- Usuario: " + rs.getString("usuario") + ", Nombre: " + rs.getString("nombre"));
            }
            
            if (!hayUsuarios) {
                System.out.println("ADVERTENCIA: NO HAY USUARIOS REGISTRADOS");
                System.out.println("ADVERTENCIA: Debes registrarte primero antes de iniciar sesion");
            }
            
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
