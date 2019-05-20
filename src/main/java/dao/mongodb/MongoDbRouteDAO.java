package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RouteDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Route;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbRouteDAO implements RouteDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbUserDAO.class.getName());
    private static final MongoDbRouteDAO INSTANCE = new MongoDbRouteDAO();

    private static final String COLLECTION_NAME = "routes";

    private static final String LABEL_ID = "id";
    private static final String LABEL_PRICE_ID = "price_id";

    private static final String LABEL_FROM_ID = "from_id";
    private static final String LABEL_TO_ID = "to_id";

    private static final String LABEL_FROM_TIME = "from_time";
    private static final String LABEL_TO_TIME = "to_time";

    private static final String LABEL_DISTANCE = "distance";

    private MongoDbRouteDAO() {
    }

    static MongoDbRouteDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Route> findAll() {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Route> routes = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            routes.add(getRoute(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return routes;
    }

    @Override
    public Route findById(Long id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, id)).first();
        return document == null || document.isEmpty() ? null : getRoute(document);
    }

    @Override
    public List<Route> findByFromId(Long id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        List<Route> routes = new ArrayList<>((int) collection.count());

        for (Document document : collection.find(eq(LABEL_FROM_ID, id))) {
            routes.add(getRoute(document));
        }

        return routes;
    }

    @Override
    public Route create(Route route) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ID, route.getId());
        document.put(LABEL_PRICE_ID, route.getPriceId());
        document.put(LABEL_FROM_ID, route.getFromId());
        document.put(LABEL_TO_ID, route.getToId());
        document.put(LABEL_FROM_TIME, route.getFromTime());
        document.put(LABEL_TO_TIME, route.getToTime());
        document.put(LABEL_DISTANCE, route.getDistance());
        collection.insertOne(document);

        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, route.getId()));
        return route;
    }

    @Override
    public Route update(Route route) {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public void delete(Route route) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, route.getId()));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, route.getId()));
    }

    private Route getRoute(Document document) {
        Route result = new Route();

        result.setId(document.get(LABEL_ID, Number.class).longValue());

        result.setPriceId(document.get(LABEL_PRICE_ID, Number.class).longValue());

        result.setFromId(document.get(LABEL_FROM_ID, Number.class).longValue());
        result.setToId(document.get(LABEL_TO_ID, Number.class).longValue());

        result.setFromTime(document.getString(LABEL_FROM_TIME));
        result.setToTime(document.getString(LABEL_TO_TIME));

        result.setDistance(document.get(LABEL_DISTANCE, Number.class).doubleValue());

        return result;
    }
}
