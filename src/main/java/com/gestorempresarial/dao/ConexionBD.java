// com.gestorempresarial.dao.ConexionBD.java
package main.java.com.gestorempresarial.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {
    
    private static Connection conexion = null;
    private static Properties propiedades = null;
    
    // Configuración por defecto (fallback)
    private static String URL = "jdbc:mysql://localhost:3306/gestor_empresarial";
    private static String USUARIO = "root";
    private static String CONTRASENA = "password";
    
    static {
        cargarConfiguracion();
    }
    
    private static void cargarConfiguracion() {
        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            
            if (input != null) {
                propiedades = new Properties();
                propiedades.load(input);
                
                // Cargar configuración desde archivo
                String host = propiedades.getProperty("db.host", "localhost");
                String port = propiedades.getProperty("db.port", "3306");
                String name = propiedades.getProperty("db.name", "gestor_empresarial");
                URL = String.format("jdbc:mysql://%s:%s/%s", host, port, name);
                
                // Agregar parámetros adicionales
                String useSSL = propiedades.getProperty("db.useSSL", "false");
                String timezone = propiedades.getProperty("db.serverTimezone", "America/Bogota");
                URL += "?useSSL=" + useSSL + "&serverTimezone=" + timezone;
                
                USUARIO = propiedades.getProperty("db.username", "root");
                CONTRASENA = propiedades.getProperty("db.password", "password");
                
                System.out.println("✅ Configuración cargada desde database.properties");
            } else {
                System.out.println("⚠️ No se encontró database.properties, usando valores por defecto");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al cargar configuración: " + e.getMessage());
        }
    }
    
    private ConexionBD() {
        // Constructor privado para patrón Singleton
    }
    
    public static Connection obtenerConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("✅ Conexión a base de datos establecida");
            } catch (ClassNotFoundException e) {
                System.err.println("❌ Error: Driver JDBC no encontrado");
                System.err.println("   Asegúrate de tener mysql-connector-java.jar en la carpeta lib/");
                throw new SQLException("Driver JDBC no encontrado", e);
            }
        }
        return conexion;
    }
    
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("✅ Conexión a base de datos cerrada");
            } catch (SQLException e) {
                System.err.println("❌ Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    // Método para probar la conexión
    public static boolean probarConexion() {
        try {
            Connection conn = obtenerConexion();
            boolean conectado = conn != null && !conn.isClosed();
            if (conectado) {
                System.out.println("✅ Conexión a BD funcionando correctamente");
            }
            return conectado;
        } catch (SQLException e) {
            System.err.println("❌ No se pudo conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }
}