package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RouteDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Route;
import model.entity.dto.RouteDTO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

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
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, new ObjectId(id))).first();
        return document == null || document.isEmpty() ? null : getRoute(document);
    }

    @Override
    public List<Route> findByFromId(String id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        List<Route> routes = new ArrayList<>((int) collection.count());

        for (Document document : collection.find(eq(LABEL_FROM_ID, new ObjectId(id)))) {
            routes.add(getRoute(document));
        }

        return routes;
    }

    @Override
    public Route create(Route route) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_PRICE_ID, new ObjectId(route.getPriceId()));
        document.put(LABEL_FROM_ID, new ObjectId(route.getFromId()));
        document.put(LABEL_TO_ID, new ObjectId(route.getToId()));
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
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        RouteDTO routeDTO=new RouteDTO();
        routeDTO.setId(new ObjectId(route.getId()));
        routeDTO.setFromId(new ObjectId(route.getFromId()));
        routeDTO.setPriceId(new ObjectId(route.getPriceId()));
        routeDTO.setDistance(route.getDistance());
        routeDTO.setToTime(route.getToTime());
        routeDTO.setToId(new ObjectId(route.getToId()));
        routeDTO.setFromTime(route.getFromTime());
        collection.findOneAndUpdate(eq(LABEL_ID, routeDTO.getId()), new Document("$set", routeDTO));


        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, route.getId()));
        return route;
    }

    @Override
    public void delete(Route route) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, new ObjectId(route.getId())));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, route.getId()));
    }

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
