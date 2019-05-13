package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.PriceDAO;
import dao.RequestDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Price;
import model.entity.Request;
import org.bson.Document;

import java.util.List;
import java.util.logging.Logger;

public class MongoDbPriceDAO implements PriceDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbPriceDAO.class.getName());
    private static final MongoDbPriceDAO INSTANCE = new MongoDbPriceDAO();
    private static final String COLLECTION_NAME = "price";

    private static final String LABEL_ID = "id";
    private static final String LABEL_BERTH_FACTOR = "berth_factor";
    private static final String LABEL_COMPARTMENT_FACTOR = "compartment_factor";
    private static final String LABEL_DELUXE_FACTOR = "deluxe_factor";

    private MongoDbPriceDAO() {
    }

    static MongoDbPriceDAO getInstance() {
        return INSTANCE;
    }



    private Price getPrice(Document document){
        Price result = new Price();
        result.setId(document.getObjectId(LABEL_ID).toHexString());
        result.setBerthFactor(document.getDouble(LABEL_BERTH_FACTOR));
        result.setCompartmentFactor(document.getDouble(LABEL_COMPARTMENT_FACTOR));
        result.setDeluxeFactor(document.getDouble(LABEL_DELUXE_FACTOR));

        return result;
    }

    @Override
    public List<Price> findAll() {
        return null;
    }

    @Override
    public Price findById(String id) {
        return null;
    }

    @Override
    public Price create(Price price) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_BERTH_FACTOR, price.getBerthFactor());
        document.put(LABEL_COMPARTMENT_FACTOR, price.getCompartmentFactor());
        document.put(LABEL_DELUXE_FACTOR, price.getDeluxeFactor());
        collection.insertOne(document);

        price.setId(document.getObjectId(LABEL_ID).toHexString());
        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, price.getId()));
        return price;
    }

    @Override
    public Price update(Price price) {
        return null;
    }

    @Override
    public void delete(Price price) {

    }
}
