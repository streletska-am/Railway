package dao;


public interface DAOFactory {
    PriceDAO createPriceDAO();

    RequestDAO createRequestDAO();

    RouteDAO createRouteDAO();

    TrainDAO createTrainDAO();

    UserDAO createUserDAO();

    StationDAO createStationDAO();
}
