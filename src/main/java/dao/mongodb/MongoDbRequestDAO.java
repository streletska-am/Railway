package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RequestDAO;
import dao.mysql.TypePlace;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Request;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbRequestDAO implements RequestDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbRequestDAO.class.getName());
    private static final MongoDbRequestDAO INSTANCE = new MongoDbRequestDAO();

    private static final String COLLECTION_NAME = "requests";

    private static final String LABEL_ID = "_id";
    private static final String LABEL_USER_ID = "user_id";
    private static final String LABEL_TRAIN_ID = "train_id";
    private static final String LABEL_TYPE = "type";
    private static final String LABEL_PRICE = "price";

    private MongoDbRequestDAO() {
    }

    static MongoDbRequestDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Request> findAll() {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Request> requests = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            requests.add(getRequest(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return requests;
    }

    @Override
    public Request findById(String id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, new ObjectId(id))).first();
        return getRequest(document);
    }

    @Override
    public Request create(Request request) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_USER_ID, request.getUserId());
        document.put(LABEL_TRAIN_ID, request.getTrainId());
        document.put(LABEL_TYPE, request.getType());
        document.put(LABEL_PRICE, request.getPrice());
        collection.insertOne(document);

        request.setId(document.getObjectId(LABEL_ID).toHexString());
        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, request.getId()));
        return request;
    }

    @Override
    public Request update(Request request) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.findOneAndUpdate(eq(LABEL_ID, new ObjectId(request.getId())), new Document("$set", request));

        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, request.getId()));
        return request;
    }

    @Override
    public void delete(Request request) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, new ObjectId(request.getId())));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, request.getId()));
    }

    private Request getRequest(Document document){
        Request request = new Request();
        System.out.println(document);
        request.setId(document.get(LABEL_ID).toString());
        request.setTrainId(document.getString(LABEL_TRAIN_ID));
        request.setUserId(document.getString(LABEL_USER_ID));
        request.setPrice(document.getDouble(LABEL_PRICE));
        request.setType(TypePlace.valueOf(document.getString(LABEL_TYPE)));
        return request;
    }
}
