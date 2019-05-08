package util;

import java.util.ResourceBundle;

public class MongoDbConfiguration {
    private static MongoDbConfiguration INSTANCE;
    private static ResourceBundle bundle;
    private static final String BUNDLE_NAME = "mongodb";

    public static final String HOST = "config.host";
    public static final String PORT = "config.port";
    public static final String DATABASE = "config.database";

    public static final String AUTH_DATABASE = "config.auth.database";
    public static final String AUTH_USERNAME = "config.auth.username";
    public static final String AUTH_PASSWORD = "config.auth.password";


    private MongoDbConfiguration() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static MongoDbConfiguration getInstance() {
        if (INSTANCE == null) {
            synchronized (MongoDbConfiguration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MongoDbConfiguration();
                }
            }
        }

        return INSTANCE;
    }

    public String getConfig(String parameter) {
        return bundle.getString(parameter);
    }
}
