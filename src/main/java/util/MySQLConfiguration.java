package util;

import java.util.ResourceBundle;

public class MySQLConfiguration {
    private static MySQLConfiguration INSTANCE;
    private static ResourceBundle bundle;
    private static final String BUNDLE_NAME = "mysql";

    public static final String DRIVER_CLASS_NAME = "config.driverClassName";
    public static final String URL = "config.url";
    public static final String USERNAME = "config.username";
    public static final String PASSWORD = "config.password";
    public static final String DATABASE = "config.database";

    private MySQLConfiguration() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static MySQLConfiguration getInstance() {
        if (INSTANCE == null) {
            synchronized (MySQLConfiguration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MySQLConfiguration();
                }
            }
        }

        return INSTANCE;
    }

    public String getConfig(String parameter) {
        return bundle.getString(parameter);
    }
}
