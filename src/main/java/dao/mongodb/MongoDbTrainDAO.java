package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.TrainDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Train;
import model.entity.dto.TrainDTO;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbTrainDAO implements TrainDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbTrainDAO.class.getName());
    private static final MongoDbTrainDAO INSTANCE = new MongoDbTrainDAO();

    private static final String COLLECTION_NAME = "trains";

    private static final String LABEL_ID = "_id";
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
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Train> trains = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            trains.add(getTrain(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return trains;
    }

    @Override
    public List<Train> findByRoute(String route_id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Train> trains = new ArrayList<>((int) collection.count());

        for (Document document : collection.find(eq(LABEL_ROUTE_ID,new ObjectId(route_id)))) {
            trains.add(getTrain(document));
        }
        return trains;
    }

    @Override
    public Train findById(String id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, new ObjectId(id))).first();
        return document == null || document.isEmpty() ? null :getTrain(document);
    }

    @Override
    public Train create(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ROUTE_ID, new ObjectId(train.getRouteId()));
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
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        TrainDTO trainDTO= new TrainDTO();
        trainDTO.setId(new ObjectId(train.getId()));
        trainDTO.setRouteId(new ObjectId(train.getRouteId()));
        trainDTO.setBerthFree(train.getBerthFree());
        trainDTO.setCompartmentFree(train.getCompartmentFree());
        trainDTO.setDeluxeFree(train.getDeluxeFree());
        collection.findOneAndUpdate(eq(LABEL_ID, trainDTO.getId()), new Document("$set", trainDTO));

        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, train.getId()));
        return train;
    }

    @Override
    public void delete(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, new ObjectId(train.getId())));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, train.getId()));
    }

    private Train getTrain(Document document) {
        Train result = new Train();

        result.setId(document.getObjectId(LABEL_ID).toHexString());
        result.setRouteId(document.getObjectId(LABEL_ROUTE_ID).toHexString());
        result.setBerthFree( document.getInteger(LABEL_BERTH_FREE));
        result.setCompartmentFree(document.getInteger(LABEL_COMPARTMENT_FREE));
        result.setDeluxeFree( document.getInteger(LABEL_DELUXE_FREE));

        return result;
    }
}
