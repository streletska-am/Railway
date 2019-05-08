package dao.mongodb;

import dao.DataBase;
import util.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static dao.DataBase.MONGODB;
import static util.Configuration.PROFILE_DATABASE;

@WebListener
public class MongoDBContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if (MONGODB == DataBase.fromValue(Configuration.getInstance().getConfig(PROFILE_DATABASE))) {
            MongoDbConnectionPool.getInstance();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (MONGODB == DataBase.fromValue(Configuration.getInstance().getConfig(PROFILE_DATABASE))) {
            MongoDbConnectionPool.getInstance().close();
        }
    }
}
