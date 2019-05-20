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

    private static final String LABEL_OBJECT_ID = "_id";
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
        SequenceId sequenceId = getSequenceId(document);
        BasicDBObject searchQuery = new BasicDBObject().append("id", sequenceId.getId());

        sequenceId.setSequence(sequenceId.getSequence() + 1);
        BasicDBObject newDocument = new BasicDBObject();
        newDocument.append("$set", new BasicDBObject().append("sequence", sequenceId.getSequence()));

        collection.updateOne(searchQuery, newDocument);
        return sequenceId.getSequence();
    }

    private SequenceId getSequenceId(Document document) {
        SequenceId result = new SequenceId();
        result.setObjectId(document.getObjectId(LABEL_OBJECT_ID));
        result.setId(document.getString(LABEL_ID));
        result.setSequence(document.get(LABEL_SEQUENCE, Number.class).longValue());
        return result;
    }
}
