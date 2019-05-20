package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.PriceDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.Price;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbPriceDAO implements PriceDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbPriceDAO.class.getName());
    private static final MongoDbPriceDAO INSTANCE = new MongoDbPriceDAO();
    private static final String COLLECTION_NAME = "prices";

    private static final String LABEL_ID = "id";
    private static final String LABEL_BERTH_FACTOR = "berth_factor";
    private static final String LABEL_COMPARTMENT_FACTOR = "compartment_factor";
    private static final String LABEL_DELUXE_FACTOR = "deluxe_factor";

    private MongoDbPriceDAO() {
    }

    static MongoDbPriceDAO getInstance() {
        return INSTANCE;
    }


    private Price getPrice(Document document) {
        Price result = new Price();
        result.setId(document.get(LABEL_ID, Number.class).longValue());
        result.setBerthFactor(document.get(LABEL_BERTH_FACTOR, Number.class).doubleValue());
        result.setCompartmentFactor(document.get(LABEL_COMPARTMENT_FACTOR, Number.class).doubleValue());
        result.setDeluxeFactor(document.get(LABEL_DELUXE_FACTOR, Number.class).doubleValue());

        return result;
    }

    @Override
    public List<Price> findAll() {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<Price> prices = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            prices.add(getPrice(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return prices;
    }

    @Override
    public Price findById(Long id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, id)).first();
        return document == null || document.isEmpty() ? null : getPrice(document);
    }

    @Override
    public Price create(Price price) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ID, price.getId());
        document.put(LABEL_BERTH_FACTOR, price.getBerthFactor());
        document.put(LABEL_COMPARTMENT_FACTOR, price.getCompartmentFactor());
        document.put(LABEL_DELUXE_FACTOR, price.getDeluxeFactor());
        collection.insertOne(document);

        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, price.getId()));
        return price;
    }

    @Override
    public Price update(Price price) {
        throw new IllegalStateException("Not implemented yet!");
    }

    @Override
    public void delete(Price price) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, price.getId()));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, price.getId()));
    }
}
