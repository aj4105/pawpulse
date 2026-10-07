package listeners;

import db_config.DBConnection;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/**
 * Initializes database configuration from WEB-INF/dbconfig.xml on application startup.
 */
public class DBContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DBConnection.initFromContext(sce.getServletContext());
        } catch (Exception e) {
            try {
                DBConnection.initFromClasspath();
            } catch (Exception e2) {
                throw new RuntimeException("DB config init failed", e2);
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Optional: close any connection pool
    }
}
