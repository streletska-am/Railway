package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.UserDAO;
import dao.mysql.util.LogMessageDAOUtil;
import model.entity.User;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbUserDAO implements UserDAO {
    private static final Logger LOG = Logger.getLogger(MongoDbUserDAO.class.getName());
    private static final MongoDbUserDAO INSTANCE = new MongoDbUserDAO();

    private static final String COLLECTION_NAME = "users";

    private static final String LABEL_ID = "id";
    private static final String LABEL_EMAIL = "email";
    private static final String LABEL_PASSWORD = "password";
    private static final String LABEL_NAME = "name";
    private static final String LABEL_SURNAME = "surname";
    private static final String LABEL_PHONE = "phone";
    private static final String LABEL_ADMIN = "admin";

    private MongoDbUserDAO() {
    }

    static MongoDbUserDAO getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAll() {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);
        List<User> users = new ArrayList<>((int) collection.count());

        for (Document document : collection.find()) {
            users.add(getUser(document));
        }

        LOG.info(LogMessageDAOUtil.createInfoFindAll(COLLECTION_NAME));
        return users;
    }

    @Override
    public User findById(Long id) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_ID, id)).first();
        return document == null || document.isEmpty() ? null : getUser(document);
    }

    @Override
    public User findByEmail(String login) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = collection.find(eq(LABEL_EMAIL, login)).first();
        return document == null || document.isEmpty() ? null : getUser(document);
    }

    @Override
    public User create(User user) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        Document document = new Document();
        document.put(LABEL_ID, user.getId());
        document.put(LABEL_EMAIL, user.getEmail());
        document.put(LABEL_PASSWORD, user.getPassword());
        document.put(LABEL_NAME, user.getName());
        document.put(LABEL_SURNAME, user.getSurname());
        document.put(LABEL_PHONE, user.getPhone());
        document.put(LABEL_ADMIN, user.getAdmin());
        collection.insertOne(document);

        LOG.info(LogMessageDAOUtil.createInfoCreate(COLLECTION_NAME, user.getId()));
        return user;
    }

    @Override
    public User update(User user) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.findOneAndUpdate(eq(LABEL_ID, user.getId()), new Document("$set", user));

        LOG.info(LogMessageDAOUtil.createInfoUpdate(COLLECTION_NAME, user.getId()));
        return user;
    }

    @Override
    public void delete(User user) {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(COLLECTION_NAME);

        collection.deleteOne(eq(LABEL_ID, user.getId()));

        LOG.info(LogMessageDAOUtil.createInfoDelete(COLLECTION_NAME, user.getId()));
    }

    private User getUser(Document document) {
        User result = new User();
        result.setId(document.get(LABEL_ID, Number.class).longValue());

        result.setEmail(document.getString(LABEL_EMAIL));
        result.setPassword(document.getString(LABEL_PASSWORD));
        result.setName(document.getString(LABEL_NAME));
        result.setSurname(document.getString(LABEL_SURNAME));
        result.setPhone(document.getString(LABEL_PHONE));

        result.setAdmin(document.getBoolean(LABEL_ADMIN));
        return result;
    }
}
