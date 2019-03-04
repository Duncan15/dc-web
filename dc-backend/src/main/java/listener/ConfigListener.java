package listener;

import services.ConfigService;
import util.DBUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.util.Properties;

/**
 * listener for configuration
 */
@WebListener
public class ConfigListener implements ServletContextListener {
    // Public constructor is required by servlet spec
    public ConfigListener() {
    }
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ConfigService.load();
        Properties prop = ConfigService.getBackMap();
        String mysqlURL = prop.getProperty("mysqlURL");
        String mysqlUserName = prop.getProperty("mysqlUserName");
        String mysqlPassword = prop.getProperty("mysqlPassword");
        DBUtil.config(mysqlURL, mysqlUserName, mysqlPassword);
        System.out.println("configure db finish");

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
