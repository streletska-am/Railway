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
        return null;
    }

    @Override
    public RequestDAO createRequestDAO() {
        return null;
    }

    @Override
    public RouteDAO createRouteDAO() {
        return MongoDbRouteDAO.getInstance();
    }

    @Override
    public TrainDAO createTrainDAO() {
        return null;
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
