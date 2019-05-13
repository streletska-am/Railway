package dao.mongodb;

import dao.DAOFactory;
import dao.PriceDAO;
import dao.RequestDAO;
import dao.RouteDAO;
import dao.StationDAO;
import dao.TrainDAO;
import dao.UserDAO;

public class MongoDbFactory implements DAOFactory {

    @Override
    public PriceDAO createPriceDAO() {
        return MongoDbPriceDAO.getInstance();
    }

    @Override
    public RequestDAO createRequestDAO() {
        return MongoDbRequestDAO.getInstance();
    }

    @Override
    public RouteDAO createRouteDAO() {
        return MongoDbRouteDAO.getInstance();
    }

    @Override
    public TrainDAO createTrainDAO() { return MongoDbTrainDAO.getInstance();
    }

    @Override
    public UserDAO createUserDAO() {
        return MongoDbUserDAO.getInstance();
    }

    @Override
    public StationDAO createStationDAO() {
        return MongoDbStationDAO.getInstance();
    }
}
