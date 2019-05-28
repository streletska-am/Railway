package util;

import com.github.javafaker.Faker;
import com.github.javafaker.Number;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dao.mysql.TypePlace;
import model.entity.Price;
import model.entity.Request;
import model.entity.Route;
import model.entity.Station;
import model.entity.Train;
import model.entity.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

public class JsonDataGenerator {

    private static final int STATION_SIZE = 164;
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

    private static final String TYPICAL_PASSWORD_HASH = md5Hex("root");

    private static final String TYPICAL_PHONE_NUMBER = "(012) 345-67-89";

    private static final String TYPICAL_EMAIL = "example@gmail.com";

    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private static final Type STATIONS_TYPE = new TypeToken<List<Station>>() {}.getType();

    private static final Type PRICES_TYPE = new TypeToken<List<Price>>() {}.getType();

    private static final Type ROUTES_TYPE = new TypeToken<List<Route>>() {}.getType();

    private static final Type USERS_TYPE = new TypeToken<List<User>>() {}.getType();

    private static final Type TRAINS_TYPE = new TypeToken<List<Train>>() {}.getType();

    private static final Type REQUESTS_TYPE = new TypeToken<List<Request>>() {}.getType();

    private static final Gson GSON = new Gson();

    private static final int FAKERS_INITIAL_SIZE = ROUTES_INITIAL_SIZE;

    private static final Map<Long, Faker> FAKERS_BY_ID = new HashMap<>(FAKERS_INITIAL_SIZE);

    public static void main(String[] args) throws IOException {
        for (long id = 1, fakersBound = FAKERS_INITIAL_SIZE + 1; id < fakersBound; id++) {
            FAKERS_BY_ID.put(id, new Faker(Locale.ENGLISH, new Random(id)));
        }
        System.out.println("Initialized fakers with size of " + FAKERS_BY_ID.size());
        System.out.println();


        List<Station> stations = new ArrayList<>(STATION_SIZE);
        for (int id = 1, stationsBound = STATION_SIZE + 1; id < stationsBound; id++) {
            stations.add(createStation(id));
        }
        System.out.println("Generated stations with size of " + stations.size());
        serializeToJson(stations, STATIONS_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\stations.json");
        System.out.println("Successfully imported stations to JSON");
        System.out.println();


        List<Price> prices = new ArrayList<>(PRICES_SIZE);
        for (int id = 1, pricesBound = PRICES_SIZE + 1; id < pricesBound; id++) {
            prices.add(createPrice(id));
        }
        System.out.println("Generated prices with size of " + prices.size());
        serializeToJson(prices, PRICES_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\prices.json");
        System.out.println("Successfully imported prices to JSON");
        System.out.println();


        List<Route> unfilteredRoutes = new ArrayList<>(ROUTES_INITIAL_SIZE);
        for (int id = 1, unfilteredRoutesBound = ROUTES_INITIAL_SIZE + 1; id < unfilteredRoutesBound; id++) {
            Number fakeNumber = FAKERS_BY_ID.get((long) id).number();
            Price price = prices.get(id % prices.size());
            Station from = stations.get(id % stations.size());
            Station to = stations.get(fakeNumber.numberBetween(0, stations.size() - 1));
            Route route = createRoute(id, price, from, to);
            unfilteredRoutes.add(route);
        }
        List<Route> routes = new ArrayList<>(unfilteredRoutes.size());
        for (int i = 0, routesBound = unfilteredRoutes.size(); i < routesBound; i++) {
            Route route = unfilteredRoutes.get(i);
            if (!route.getFromId().equals(route.getToId())) {
                routes.add(route);
            }
        }
        System.out.println("Generated routes with size of " + routes.size());
        serializeToJson(routes, ROUTES_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\routes.json");
        System.out.println("Successfully imported routes to JSON");
        System.out.println();


        Map<Long, Train> trainsByTrainId = new HashMap<>(TRAINS_SIZE);
        for (int id = 1, routesBound = routes.size() + 1; id < routesBound; id++) {
            Route route = routes.get(id - 1);
            Long trainId = (long) (id % TRAINS_SIZE + 1);
            if (!trainsByTrainId.containsKey(trainId)) {
                trainsByTrainId.put(trainId, createTrain(trainId, route));
            }
        }
        System.out.println("Generated trains with size of " + trainsByTrainId.size());
        serializeToJson(trainsByTrainId.values(), TRAINS_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\trains.json");
        System.out.println("Successfully imported trains to JSON");
        System.out.println();


        List<User> users = new ArrayList<>(routes.size() + 2);
        for (int id = 1, usersBound = routes.size() + 1; id < usersBound; id++) {
            users.add(createUser(id, false));
        }
        users.add(createUser(routes.size() + 1, true));
        System.out.println("Generated users with size of " + users.size());
        serializeToJson(users, USERS_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\users.json");
        System.out.println("Successfully imported users to JSON");
        System.out.println();


        List<Request> requests = new ArrayList<>(routes.size() + 1);
        for (int id = 1, requestsBound = routes.size() + 1; id < requestsBound; id++) {
            User user = users.get(id - 1);
            Train train = trainsByTrainId.get((long) (id % trainsByTrainId.size() + 1));
            Price price = prices.get(id % prices.size());
            Route route = routes.get(id - 1);
            requests.add(createRequest(id, user, train, price, route, TypePlace.get(id % TypePlace.SIZE)));
        }
        System.out.println("Generated requests with size of " + requests.size());
        serializeToJson(requests, REQUESTS_TYPE, "C:\\Users\\Vitalii_Chereshnia\\Desktop\\json\\requests.json");
        System.out.println("Successfully imported requests to JSON");
        System.out.println();
    }

    private static User createUser(long id, boolean isAdmin) {
        Faker faker = FAKERS_BY_ID.get(id);
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        return new User(id, firstName, lastName, TYPICAL_PHONE_NUMBER, TYPICAL_EMAIL, TYPICAL_PASSWORD_HASH, isAdmin);
    }

    private static Station createStation(long id) {
        Faker faker = FAKERS_BY_ID.get(id);
        String cityName = faker.address().cityName();

        return new Station(id, cityName);
    }

    private static Price createPrice(long id) {
        Faker faker = FAKERS_BY_ID.get(id);
        double berthFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 0, BERTH_FACTOR_MAX);
        double compartmentFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 1, COMPARTMENT_FACTOR_MAX);
        double deluxeFactor = faker.number().randomDouble(NUMBER_OF_DECIMALS, 2, DELUXE_FACTOR_MAX);

        return new Price(id, compartmentFactor, deluxeFactor, berthFactor);
    }

    private static Route createRoute(long id, Price price, Station from, Station to) {
        Faker faker = FAKERS_BY_ID.get(id);
        Date fromDate = faker.date().future(DAYS_TO_BUY_TICKET, 1, TimeUnit.DAYS);
        Date toDate = faker.date().future(ROUT_MAX_DURATION_HOURS, TimeUnit.HOURS, fromDate);
        double distance = faker.number().randomDouble(NUMBER_OF_DECIMALS, DISTANCE_MIN, DISTANCE_MAX);

        return new Route(id, price.getId(), from.getId(), to.getId(), FORMATTER.format(fromDate), FORMATTER.format(toDate), distance);
    }

    private static Train createTrain(long id, Route route) {
        Faker faker = FAKERS_BY_ID.get(id);
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

        return new Request(id, user.getId(), train.getId(), typePlace, new BigDecimal(value).setScale(NUMBER_OF_DECIMALS, BigDecimal.ROUND_HALF_UP).doubleValue());
    }

    private static void serializeToJson(Object object, Type type, String fileName) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            GSON.toJson(object, type, writer);
        }
    }
}
