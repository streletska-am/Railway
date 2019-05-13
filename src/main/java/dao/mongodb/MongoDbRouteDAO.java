package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RouteDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Route;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MongoDbRouteDAO implements RouteDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbUserDAO.class.getName());
    private static final MongoDbRouteDAO INSTANCE = new MongoDbRouteDAO();

    private static final String COLLECTION_NAME = "routes";

    private static final String LABEL_ID = "_id";
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
    public Route findById(String id) {
        return null;
    }

    @Override
    public List<Route> findByFromId(String id) {
        return null;
    }

    @Override
    public Route create(Route route) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_PRICE_ID, route.getPriceId());
        document.put(LABEL_FROM_ID, route.getFromId());
        document.put(LABEL_TO_ID, route.getToId());
        document.put(LABEL_FROM_TIME, route.getFromTime());
        document.put(LABEL_TO_TIME, route.getToTime());
        document.put(LABEL_DISTANCE, route.getDistance());
        collection.insertOne(document);

        route.setId(document.getObjectId(LABEL_ID).toHexString());
        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, route.getId()));
        return route;
    }

    @Override
    public Route update(Route route) {
        return null;
    }

    @Override
    public void delete(Route route) {

    }
    private List<Route> findByParameter(String label, Long parameter){return null;}

    private Route getRoute(Document document) {
        Route result = new Route();
        result.setId(document.getObjectId(LABEL_ID).toHexString());

        result.setPriceId(document.getObjectId(LABEL_PRICE_ID).toHexString());

        result.setFromId(document.getObjectId(LABEL_FROM_ID).toHexString());
        result.setToId(document.getObjectId(LABEL_TO_ID).toHexString());

        result.setFromTime(document.getString(LABEL_FROM_TIME));
        result.setToTime(document.getString(LABEL_TO_TIME));

        result.setDistance(document.getDouble(LABEL_DISTANCE));

        return result;
    }
}
