package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.StationDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Station;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Station> stations = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            stations.add(getStation(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return stations;
    }

    @Override
    public Station findById(String id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, new ObjectId(id))).first();
        return document == null || document.isEmpty() ? null :getStation(document);
    }

    @Override
    public Station create(Station station) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_NAME, station.getName());
        collection.insertOne(document);

        station.setId(document.getObjectId(LABEL_ID).toHexString());
        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, station.getId()));
        return station;
    }

    @Override
    public Station update(Station station) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.findOneAndUpdate(eq(LABEL_ID, new ObjectId(station.getId())), new Document("$set", station));

        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, station.getId()));
        return station;
    }

    @Override
    public void delete(Station station) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, new ObjectId(station.getId())));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, station.getId()));
    }

    private Station getStation(Document document) {
        Station result = new Station();

        result.setId(document.getObjectId(LABEL_ID).toHexString());
        result.setName(document.getString(LABEL_NAME));

        return result;
    }
}
