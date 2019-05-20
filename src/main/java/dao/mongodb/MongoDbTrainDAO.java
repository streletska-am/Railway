package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.TrainDAO;
import dao.mongodb.dto.TrainDto;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Train;
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

    private static final String LABEL_OBJECT_ID = "_id";
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
    public List<Train> findByRoute(Long route_id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Train> trains = new ArrayList<>((int) collection.count());

        for (Document document : collection.find(eq(LABEL_ROUTE_ID, route_id))) {
            trains.add(getTrain(document));
        }
        return trains;
    }

    @Override
    public Train findById(Long id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, id)).first();
        return document == null || document.isEmpty() ? null : getTrain(document);
    }

    @Override
    public Train create(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ID, train.getId());
        document.put(LABEL_ROUTE_ID, train.getRouteId());
        document.put(LABEL_COMPARTMENT_FREE, train.getCompartmentFree());
        document.put(LABEL_BERTH_FREE, train.getBerthFree());
        document.put(LABEL_DELUXE_FREE, train.getDeluxeFree());
        collection.insertOne(document);

        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, train.getId()));
        return train;
    }

    @Override
    public Train update(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        TrainDto trainDto = new TrainDto(train);
        collection.findOneAndUpdate(eq(LABEL_ID, trainDto.getId()), new Document("$set", trainDto));

        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, trainDto.getId()));
        return train;
    }

    @Override
    public void delete(Train train) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, train.getId()));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, train.getId()));
    }

    private TrainDto getTrain(Document document) {
        TrainDto result = new TrainDto();

        result.setObjectId(new ObjectId(document.getString(LABEL_OBJECT_ID)));
        result.setId(document.get(LABEL_ID, Number.class).longValue());
        result.setRouteId(document.get(LABEL_ROUTE_ID, Number.class).longValue());
        result.setBerthFree(document.get(LABEL_BERTH_FREE, Number.class).intValue());
        result.setCompartmentFree(document.get(LABEL_COMPARTMENT_FREE, Number.class).intValue());
        result.setDeluxeFree(document.get(LABEL_DELUXE_FREE, Number.class).intValue());

        return result;
    }
}
