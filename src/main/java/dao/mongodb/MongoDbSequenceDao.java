package dao.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import dao.SequenceDao;
import dao.SequenceException;
import lombok.AllArgsConstructor;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

@AllArgsConstructor
public class MongoDbSequenceDao implements SequenceDao {

    private static final String LABEL_ID = "id";
    private static final String LABEL_SEQUENCE = "sequence";

    private String sequenceId;

    @Override
    public Long getNextSequenceId() throws SequenceException {
        MongoCollection<Document> collection = MongoDbConnectionPool.getInstance().getConnection()
                .getCollection(sequenceId);

        Document document = collection.find(eq(LABEL_ID, sequenceId)).first();
        if (document == null || document.isEmpty()) {
            throw new SequenceException(String.format("Not found sequence for key '%s'", sequenceId));
        }

        BasicDBObject query = new BasicDBObject().append(LABEL_ID, document.getString(LABEL_ID));

        long val = document.get(LABEL_SEQUENCE, Number.class).longValue() + 1;
        BasicDBObject target = new BasicDBObject();
        target.append("$set", new BasicDBObject().append(LABEL_SEQUENCE, val));

        collection.updateOne(query, target);
        return val;
    }
}
