package dao;


public interface DAOFactory {
    PriceDAO createPriceDAO();

    RequestDAO createRequestDAO();

    RouteDAO createRouteDAO();

    TrainDAO createTrainDAO();

    UserDAO createUserDAO();

    StationDAO createStationDAO();

    default SequenceDao createSequenceDao(String sequenceName) {
        return () -> null;
    }
}
