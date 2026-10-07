package db_config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import jakarta.servlet.ServletContext;

/**
 * Manages database connections using XML configuration.
 * Supports both classpath and servlet context for dbconfig.xml.
 */
public class DBConnection {

    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static boolean initialized = false;

    public static void initFromClasspath() {
        if (initialized) return;
        try (InputStream is = DBConnection.class.getClassLoader()
                .getResourceAsStream("db_config/dbconfig.xml")) {
            if (is == null) {
                throw new RuntimeException("dbconfig.xml not found in classpath (db_config/dbconfig.xml)");
            }
            initFromStream(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load dbconfig.xml from classpath", e);
        }
    }

    public static void initFromContext(ServletContext ctx) {
        if (initialized) return;
        try (InputStream is = ctx.getResourceAsStream("/WEB-INF/dbconfig.xml")) {
            if (is != null) {
                initFromStream(is);
                return;
            }
        } catch (Exception ignored) { }
        initFromClasspath();
    }

    private static void initFromStream(InputStream is) throws Exception {
        Properties props = new Properties();
        props.loadFromXML(is);
        driver = props.getProperty("driver", "com.mysql.cj.jdbc.Driver");
        url = props.getProperty("url");
        username = props.getProperty("username");
        password = props.getProperty("password");
        if (url == null || username == null)
            throw new RuntimeException("dbconfig.xml must contain url and username");
        Class.forName(driver);
        initialized = true;
    }

    public static Connection getConnection() throws SQLException {
        if (!initialized) initFromClasspath();
        return DriverManager.getConnection(url, username, password);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) { }
        }
    }
}
