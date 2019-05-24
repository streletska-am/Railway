package util;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import dao.AbstractDAOFactory;
import dao.DAOFactory;
import dao.DataBase;
import dao.PriceDAO;
import dao.RequestDAO;
import dao.RouteDAO;
import dao.StationDAO;
import dao.TrainDAO;
import dao.UserDAO;
import dao.mysql.TypePlace;
import model.entity.Price;
import model.entity.Request;
import model.entity.Route;
import model.entity.Station;
import model.entity.Train;
import model.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.Configuration.PROFILE_DATABASE;

public class DatabasePopulator {

    private static final int STATION_SIZE = 32;
    private static final int ROUTES_INITIAL_SIZE = STATION_SIZE * STATION_SIZE;
    private static final int PRICES_SIZE = IntStream.range(1, STATION_SIZE).sum();
    private static final int TRAINS_SIZE = STATION_SIZE / 4;

    private static final int BERTH_FREE_MIN_SIZE = 30;
    private static final int COMPARTMENT_FREE_MIN_SIZE = 30;
    private static final int DELUXE_FREE_MIN_SIZE = 30;

    private static final int BERTH_FACTOR_MAX = 1;
    private static final int COMPARTMENT_FACTOR_MAX = 2;
    private static final int DELUXE_FACTOR_MAX = 3;

    private static final int NUMBER_OF_DECIMALS = 2;

    private static final int DISTANCE_MIN = 100;
    private static final int DISTANCE_MAX = 1_000;

    private static final int DAYS_TO_BUY_TICKET = 30;
    private static final int ROUT_MAX_DURATION_HOURS = 24;

    private static final String TYPICAL_PASSWORD = "root";

    private static final String PHONE_NUMBER_FORMAT = "(0##) ###-##-##";

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void main(String[] args) {
        DataBase dataBase = DataBase.fromValue(Configuration.getInstance().getConfig(PROFILE_DATABASE));
        DAOFactory daoFactory = AbstractDAOFactory.createDAOFactory(dataBase);

        final StationDAO stationDAO = daoFactory.createStationDAO();
        final List<Station> stations = IntStream.range(1, STATION_SIZE + 1)
                .collect(createList(STATION_SIZE),
                        (list, id) -> list.add(stationDAO.create(createStation(id))),
                        doNothingAfter());

        final PriceDAO priceDAO = daoFactory.createPriceDAO();
        final List<Price> prices = IntStream.range(1, PRICES_SIZE + 1)
                .collect(createList(PRICES_SIZE),
                        (list, id) -> list.add(priceDAO.create(createPrice(id))),
                        doNothingAfter());

        final RouteDAO routeDAO = daoFactory.createRouteDAO();
        final List<Route> routesTemp = IntStream.range(1, ROUTES_INITIAL_SIZE + 1)
                .collect(createList(ROUTES_INITIAL_SIZE),
                        (list, id) -> {
                            Number fakeNumber = new Faker(new Random(id)).number();
                            Price price = prices.get(id % PRICES_SIZE);
                            Station from = stations.get(id % STATION_SIZE);
                            Station to = stations.get(fakeNumber.numberBetween(0, STATION_SIZE - 1));
                            Route route = routeDAO.create(createRoute(id, price, from, to));
                            list.add(route);
                        },
                        doNothingAfter());
        final List<Route> routes = routesTemp.stream()
                .filter(route -> !route.getFromId().equals(route.getToId()))
                .collect(Collectors.toList());

        final UserDAO userDAO = daoFactory.createUserDAO();
        userDAO.create(createUser(0, true));
        final List<User> users = IntStream.range(1, routes.size() + 1)
                .collect(createList(routes.size()),
                        (list, id) -> list.add(userDAO.create(createUser(id, false))),
                        doNothingAfter());

        final TrainDAO trainDAO = daoFactory.createTrainDAO();
        final List<Train> trains = IntStream.range(1, routes.size() + 1)
                .collect(createList(TRAINS_SIZE),
                        (list, id) -> {
                            Route route = routes.get(id - 1);
                            long trainId = id % TRAINS_SIZE + 1;
                            if (list.size() <= TRAINS_SIZE && !containsTrainId(trainId, list)) {
                                list.add(trainDAO.create(createTrain(trainId, route)));
                            }
                        },
                        doNothingAfter());

        final RequestDAO requestDAO = daoFactory.createRequestDAO();
        IntStream.range(1, routes.size() + 1)
                .forEach(id -> {
                    User user = users.get(id - 1);
                    Train train = trains.get(id % TRAINS_SIZE);
                    Price price = prices.get(id % PRICES_SIZE);
                    Route route = routes.get(id - 1);
                    requestDAO.create(createRequest(id, user, train, price, route, TypePlace.get(id % TypePlace.count())));
                });
    }

    private static User createUser(long id, boolean isAdmin) {
        Faker faker = new Faker(Locale.ENGLISH, new Random(id));
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String cellPhone = faker.numerify(PHONE_NUMBER_FORMAT);
        String password = DigestUtils.md5Hex(TYPICAL_PASSWORD);
        String email = firstName.toLowerCase() + '.' + lastName.toLowerCase() + "@gmail.com";

        return new User(id, firstName, lastName, cellPhone, email, password, isAdmin);
    }

    private static Station createStation(long id) {
        Faker faker = new Faker(Locale.ENGLISH, new Random(id));
        String cityName = faker.address().cityName();

        return new Station(id, cityName);
    }

    private static Price createPrice(long id) {
        Faker faker = new Faker(Locale.ENGLISH, new Random(id));
        double berthFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 0, BERTH_FACTOR_MAX);
        double compartmentFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 1, COMPARTMENT_FACTOR_MAX);
        double deluxeFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 2, DELUXE_FACTOR_MAX);

        return new Price(id, compartmentFactor, deluxeFactor, berthFactor);
    }

    private static Route createRoute(long id, Price price, Station from, Station to) {
        Faker faker = new Faker(Locale.ENGLISH, new Random(id));
        Date fromDate = faker.date().future(DAYS_TO_BUY_TICKET, 1, TimeUnit.DAYS);
        Date toDate = faker.date().future(ROUT_MAX_DURATION_HOURS, TimeUnit.HOURS, fromDate);
        double distance = faker.number().randomDouble(NUMBER_OF_DECIMALS, DISTANCE_MIN, DISTANCE_MAX);

        return new Route(id, price.getId(), from.getId(), to.getId(), FORMATTER.format(fromDate), FORMATTER.format(toDate), distance);
    }

    private static Train createTrain(long id, Route route) {
        Faker faker = new Faker(Locale.ENGLISH, new Random(id));
        int compartmentFree = faker.number().numberBetween(COMPARTMENT_FREE_MIN_SIZE, COMPARTMENT_FREE_MIN_SIZE + 20);
        int deluxeFree = faker.number().numberBetween(DELUXE_FREE_MIN_SIZE, DELUXE_FREE_MIN_SIZE + 10);
        int berthFree = faker.number().numberBetween(BERTH_FREE_MIN_SIZE, 30);

        return new Train(id, route.getId(), compartmentFree, deluxeFree, berthFree);
    }

    private static Request createRequest(long id, User user, Train train, Price price, Route route, TypePlace typePlace) {
        double value;
        switch (typePlace) {
            case C:
                value = price.getCompartmentFactor() * route.getDistance();
                break;
            case B:
                value = price.getBerthFactor() * route.getDistance();
                break;
            default:
                value = price.getDeluxeFactor() * route.getDistance();
        }

        return new Request(id, user.getId(), train.getId(), typePlace, scale(value));
    }

    private static boolean containsTrainId(long trainId, List<Train> trains) {
        for (Train train : trains) {
            if (train.getId() == trainId) {
                return true;
            }
        }
        return false;
    }

    private static double scale(double value) {
        return new BigDecimal(value).setScale(NUMBER_OF_DECIMALS, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static <T> Supplier<ArrayList<T>> createList(final int size) {
        return () -> new ArrayList<>(size);
    }

    private static <T> BiConsumer<ArrayList<T>, ArrayList<T>> doNothingAfter() {
        return (x1, x2) -> {};
    }
}
