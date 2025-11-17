package utn.programacion2.TPintegrador.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de configuración para la conexión a la base de datos MySQL
 */
public class DatabaseConnection {
    private static final String URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/UTN_integradorProg2");
    private static final String USER = System.getProperty("db.user", "root");
    private static final String PASSWORD = System.getProperty("db.password", "");
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
            // Validar configuración tempranamente (fail-fast)
            validarConfig();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver de MySQL", e);
        }
    }

    /**
     * Obtiene una conexión a la base de datos
     * @return Connection objeto de conexión
     * @throws SQLException si ocurre un error al conectar
     */
    public static Connection conectarDB() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Cierra la conexión de forma segura
     * @param c conexión a cerrar
     */
    public static void desconectarDB(Connection c) {
        if (null != c) {
            try {
                c.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    private static void validarConfig() {
        if (null == URL || URL.isBlank()) {
            throw new IllegalStateException("La URL de la base de datos no está configurada");
        }
        if (null == USER || USER.isBlank()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado");
        }
        if (null == PASSWORD) {
            throw new IllegalStateException("La contraseña de la base de datos no está configurada");
        }
    }
}
