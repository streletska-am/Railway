package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.TrainDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Train;
import org.bson.Document;

import java.util.List;
import java.util.logging.Logger;

public class MongoDbTrainDAO implements TrainDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbTrainDAO.class.getName());
    private static final MongoDbTrainDAO INSTANCE = new MongoDbTrainDAO();

    private static final String COLLECTION_NAME = "train";

    private static final String LABEL_ID = "id";
    private static final String LABEL_ROUTE_ID = "route_id";
    private static final String LABEL_COMPARTMENT_FREE = "compartment_free";
    private static final String LABEL_BERTH_FREE = "berth_free";
    private static final String LABEL_DELUXE_FREE = "deluxe_free";

    private MongoDbTrainDAO() {
    }

    static MongoDbTrainDAO getInstance() {
        return INSTANCE;
    }
    @Override
    public List<Train> findAll() {
        return null;
    }

    @Override
    public List<Train> findByRoute(String route_id) {
        return null;
    }

    @Override
    public Train findById(String id) {
        return null;
    }

    @Override
    public Train create(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ROUTE_ID, train.getRouteId());
        document.put(LABEL_COMPARTMENT_FREE, train.getCompartmentFree());
        document.put(LABEL_BERTH_FREE, train.getBerthFree());
        document.put(LABEL_DELUXE_FREE, train.getDeluxeFree());
        collection.insertOne(document);

        train.setId(document.getObjectId(LABEL_ID).toHexString());
        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, train.getId()));
        return train;
    }

    @Override
    public Train update(Train train) {
        return null;
    }

    @Override
    public void delete(Train train) {

    }

    private Train getTrain(Document document) {
        Train result = new Train();

        result.setId(document.getObjectId(LABEL_ID).toHexString());
        result.setRouteId(document.getString(LABEL_ROUTE_ID));
        result.setBerthFree(document.getLong(LABEL_BERTH_FREE));
        result.setCompartmentFree(document.getLong(LABEL_COMPARTMENT_FREE));
        result.setDeluxeFree(document.getLong(LABEL_DELUXE_FREE));

        return result;
    }
}
