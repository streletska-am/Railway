package dao.mysql;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import util.MySQLConfiguration;

import java.sql.Connection;
import java.sql.SQLException;

import static util.MySQLConfiguration.DATABASE;
import static util.MySQLConfiguration.DRIVER_CLASS_NAME;
import static util.MySQLConfiguration.PASSWORD;
import static util.MySQLConfiguration.URL;
import static util.MySQLConfiguration.USERNAME;


public class MySQLConnectionPool {
    private static MySQLConnectionPool instance;

    private DataSource source;

    private MySQLConnectionPool() throws SQLException {
        PoolProperties properties = new PoolProperties();
        properties.setDriverClassName(MySQLConfiguration.getInstance().getConfig(DRIVER_CLASS_NAME));
        properties.setUrl(MySQLConfiguration.getInstance().getConfig(URL)
                .concat(MySQLConfiguration.getInstance().getConfig(DATABASE)));
        properties.setUsername(MySQLConfiguration.getInstance().getConfig(USERNAME));
        properties.setPassword(MySQLConfiguration.getInstance().getConfig(PASSWORD));

        source = new DataSource();
        source.setPoolProperties(properties);
    }

    public static MySQLConnectionPool getInstance() throws SQLException {
        if (instance == null) {
            synchronized (MySQLConnectionPool.class) {
                if (instance == null) {
                    instance = new MySQLConnectionPool();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return source.getConnection();
    }
}
