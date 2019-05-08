package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.StationDAO;
import model.entity.Station;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbStationDAO implements StationDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbUserDAO.class.getName());
    private static final MongoDbStationDAO INSTANCE = new MongoDbStationDAO();

    private static final String COLLECTION_NAME = "stations";

    private static final String LABEL_ID = "_id";
    private static final String LABEL_NAME = "name";

    private MongoDbStationDAO() {
    }

    static MongoDbStationDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Station> findAll() {
        return null;
    }

    @Override
    public Station findById(String id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, new ObjectId(id))).first();
        return getStation(document);
    }

    @Override
    public Station create(Station station) {
        return null;
    }

    @Override
    public Station update(Station station) {
        return null;
    }

    @Override
    public void delete(Station station) {

    }

    private Station getStation(Document document) {
        Station result = new Station();

        result.setId(document.getObjectId(LABEL_ID).toHexString());
        result.setName(document.getString(LABEL_NAME));

        return result;
    }
}
