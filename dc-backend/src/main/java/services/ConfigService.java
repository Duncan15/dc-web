package services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Properties;

public class ConfigService {
    /**
     * base addresss of work directory
     */
    private static String baseWorkDir;
    /**
     * the map which contain all the key-value pairs, which keep the same with the properties file
     */
    private static Properties backMap = new Properties();

    /**
     * to be used in the initial start up
     */
    public static void load() {
        try (InputStream inputStream = ConfigService.class.getResourceAsStream("/application.properties")) {
            backMap.load(inputStream);
        } catch (IOException ex) {
            //ignored
        }
        baseWorkDir = backMap.getProperty("baseWorkDir");
    }
    public static void save() {
        try (OutputStream outputStream = new FileOutputStream(ConfigService.class.getResource("/application.properties").getFile())) {
            backMap.store(outputStream, "");
        } catch (IOException ex) {
            //ignored
        }
    }

    /**
     * configure the db parameter
     * @param mysqlURL
     * @param mysqlUserName
     * @param mysqlPassword
     */
    public static void setDB(String mysqlURL, String mysqlUserName, String mysqlPassword) {
        mysqlURL = Objects.requireNonNull(mysqlURL);
        mysqlUserName = Objects.requireNonNull(mysqlUserName);
        mysqlPassword = Objects.requireNonNull(mysqlPassword);
        backMap.put("mysqlURL", mysqlURL);
        backMap.put("mysqlUserName", mysqlUserName);
        backMap.put("mysqlPassword", mysqlPassword);
        save();
    }

    /**
     * configure the base address of work directory
     * @param dir
     */
    public static void setBaseWorkDir(String dir) {
        baseWorkDir = Objects.requireNonNull(dir);
        backMap.put("baseWorkDir", baseWorkDir);
        save();
    }

    public static String getBaseWorkDir() {
        if(baseWorkDir == null) load();
        return baseWorkDir;
    }

    public static Properties getBackMap() {
        if(backMap == null) load();
        return backMap;
    }


}
