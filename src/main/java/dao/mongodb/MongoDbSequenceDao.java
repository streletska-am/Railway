package dao.mongodb;

import com.mongodb.client.MongoCollection;
import dao.SequenceDao;
import dao.SequenceException;
import lombok.AllArgsConstructor;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

@AllArgsConstructor
public class MongoDbSequenceDao implements SequenceDao {

    private static final String LABEL_ID = "_id";
    private static final String LABEL_SEQUENCE = "seq";

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
        sequenceId.setSeq(sequenceId.getSeq() + 1);
        collection.findOneAndUpdate(eq(LABEL_ID, sequenceId.getId()), new Document("$set", sequenceId));

        return sequenceId.getSeq();
    }

    private SequenceId getSequenceId(Document document) {
        SequenceId result = new SequenceId();
        result.setId(document.getObjectId(LABEL_ID));
        result.setSeq(document.getLong(LABEL_SEQUENCE));
        return result;
    }
}
