package service;

import dao.AbstractDAOFactory;
import dao.DAOFactory;
import dao.DataBase;
import model.entity.Route;
import model.entity.Station;
import util.Configuration;

import java.util.logging.Logger;

import static util.Configuration.PROFILE_DATABASE;

public class StationService {
    private static final Logger LOG = Logger.getLogger(StationService.class.getName());
    private static StationService INSTANCE;

    private DAOFactory factory;

    private StationService() {
        DataBase dataBase = DataBase.fromValue(Configuration.getInstance().getConfig(PROFILE_DATABASE));
        factory = AbstractDAOFactory.createDAOFactory(dataBase);
    }

    public static StationService getInstance() {
        if (INSTANCE == null) {
            synchronized (StationService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StationService();
                }
            }
        }

        return INSTANCE;
    }

    Station findFromStation(Route route) {
        return factory.createStationDAO().findById(route.getFromId());
    }

    Station findToStation(Route route) {
        return factory.createStationDAO().findById(route.getToId());
    }
}
