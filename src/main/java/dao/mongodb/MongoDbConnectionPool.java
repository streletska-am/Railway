package dao.mongodb;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import util.MongoDbConfiguration;

import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static util.MongoDbConfiguration.*;

public class MongoDbConnectionPool {
    private static MongoDbConnectionPool instance;

    private MongoClient client;

    private MongoDbConnectionPool() {
        ServerAddress address = new ServerAddress(
                MongoDbConfiguration.getInstance().getConfig(HOST),
                Integer.parseInt(MongoDbConfiguration.getInstance().getConfig(PORT))
        );

        MongoCredential credential = MongoCredential.createCredential(
                MongoDbConfiguration.getInstance().getConfig(AUTH_USERNAME),
                MongoDbConfiguration.getInstance().getConfig(AUTH_DATABASE),
                MongoDbConfiguration.getInstance().getConfig(AUTH_PASSWORD).toCharArray()
        );

        MongoClientOptions options = MongoClientOptions.builder()
                .sslEnabled(false)
                .build();

        client = new MongoClient(address, singletonList(credential), options);
    }

    public static MongoDbConnectionPool getInstance() {
        if (instance == null) {
            synchronized (MongoDbConnectionPool.class) {
                if (instance == null) {
                    instance = new MongoDbConnectionPool();
                }
            }
        }
        return instance;
    }

    public MongoDatabase getConnection() {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        return client.getDatabase(MongoDbConfiguration.getInstance().getConfig(DATABASE)).withCodecRegistry(pojoCodecRegistry);
    }

    public void close() {
        client.close();
    }
}
