package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RequestDAO;
import dao.mysql.TypePlace;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Request;
import org.bson.Document;

import java.util.List;
import java.util.logging.Logger;

public class MongoDbRequestDAO implements RequestDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbRequestDAO.class.getName());
    private static final MongoDbRequestDAO INSTANCE = new MongoDbRequestDAO();

    private static final String COLLECTION_NAME = "request";

    private static final String LABEL_ID = "id";
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
        return null;
    }

    @Override
    public Request findById(String id) {
        return null;
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
        return null;
    }

    @Override
    public void delete(Request request) {

    }

    private Request getRequest(Document document){
        Request request = new Request();
        request.setId(document.getObjectId(LABEL_ID).toHexString());
        request.setTrainId(document.getString(LABEL_TRAIN_ID));
        request.setUserId(document.getString(LABEL_USER_ID));
        request.setPrice(document.getDouble(LABEL_PRICE));
        request.setType(TypePlace.valueOf(document.getString(LABEL_TYPE)));
        return request;
    }
}
