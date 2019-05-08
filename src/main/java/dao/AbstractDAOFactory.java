package dao;

import dao.mongodb.MongoDbFactory;
import dao.mysql.MySQLFactory;

public class AbstractDAOFactory {
    public static DAOFactory createDAOFactory(DataBase dataBase) {
        DAOFactory factory = null;
        switch (dataBase) {
            case MYSQL:
                factory = new MySQLFactory();
                break;
            case MONGODB:
                factory = new MongoDbFactory();
                break;
        }

        return factory;
    }
}
