package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.RequestDAO;
import dao.mysql.TypePlace;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Request;
import model.entity.dto.RequestDTO;
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
        return document == null || document.isEmpty() ? null : getRequest(document);
    }

    @Override
    public Request create(Request request) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_USER_ID, new ObjectId(request.getUserId()));
        document.put(LABEL_TRAIN_ID, new ObjectId(request.getTrainId()));
        document.put(LABEL_TYPE, request.getType().toString());
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
        RequestDTO requestDTO=new RequestDTO();
        requestDTO.setId(new ObjectId(request.getId()));
        requestDTO.setUserId(new ObjectId(request.getUserId()));
        requestDTO.setTrainId(new ObjectId(request.getTrainId()));
        requestDTO.setPrice(request.getPrice());
        requestDTO.setType(request.getType().toString());

        collection.findOneAndUpdate(eq(LABEL_ID, requestDTO.getId()), new Document("$set", requestDTO));

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
        request.setId(document.getObjectId(LABEL_ID).toHexString());
        request.setTrainId(document.getObjectId(LABEL_TRAIN_ID).toHexString());
        request.setUserId(document.getObjectId(LABEL_USER_ID).toHexString());
        request.setPrice(document.get(LABEL_PRICE, Number.class).doubleValue());
        request.setType(TypePlace.valueOf(document.getString(LABEL_TYPE)));
        return request;
    }
}
