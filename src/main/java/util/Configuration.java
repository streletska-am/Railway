package util;

import java.util.ResourceBundle;

public class Configuration {
    private static Configuration INSTANCE;
    private static ResourceBundle bundle;
    private static final String BUNDLE_NAME = "config";

    public static final String REGISTER = "config.register";
    public static final String LOGIN = "config.login";
    public static final String ERROR = "config.error";

    public static final String DATE = "config.date";
    public static final String ORDER = "config.order";
    public static final String TICKET = "config.ticket";

    public static final String ADMIN = "config.users";
    public static final String TICKETS_ADMIN = "config.ticketAdmin";

    public static final String PROFILE_DATABASE = "config.profile.database";

    private Configuration() {
        bundle = ResourceBundle.getBundle(BUNDLE_NAME);
    }

    public static Configuration getInstance() {
        if (INSTANCE == null) {
            synchronized (Configuration.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Configuration();
                }
            }
        }

        return INSTANCE;
    }

    public String getConfig(String parameter) {
        return bundle.getString(parameter);
    }
}
