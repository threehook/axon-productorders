package nl.tvdven.productorders.config;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.extensions.mongo.DefaultMongoTemplate;
import org.axonframework.extensions.mongo.MongoTemplate;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.MongoFactory;
import org.axonframework.extensions.mongo.eventsourcing.eventstore.documentperevent.DocumentPerEventStorageStrategy;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by Edison Xu on 2017/3/7.
 */
@Configuration
public class AxonConfiguration {

    @Value("${mongodb.url}")
    private String mongoUrl;

    @Value("${mongodb.dbname}")
    private String mongoDbName;

    @Value("${mongodb.events.collection.name}")
    private String eventsCollectionName;

    @Value("${mongodb.events.snapshot.collection.name}")
    private String snapshotCollectionName;

    @Bean
    @Qualifier("eventSerializer")
    public Serializer axonJsonSerializer() {
        return JacksonSerializer.builder().build();
    }

    // The `MongoEventStorageEngine` stores each event in a separate MongoDB document
    @Bean
    public EventStorageEngine eventStorageEngine() {
        return MongoEventStorageEngine.builder()
                .eventSerializer(axonJsonSerializer())
                .mongoTemplate(axonMongoTemplate())
                .storageStrategy(new DocumentPerEventStorageStrategy())
                .build();
    }

    @Bean(name = "axonMongoTemplate")
    public MongoTemplate axonMongoTemplate() {
        return DefaultMongoTemplate.builder()
                .mongoDatabase(mongoClient())
                .domainEventsCollectionName(eventsCollectionName)
                .snapshotEventsCollectionName(snapshotCollectionName)
                .build();
    }

    @Bean
    public MongoClient mongoClient() {
        MongoFactory mongoFactory = new MongoFactory();
        mongoFactory.setMongoAddresses(Arrays.asList(new ServerAddress(mongoUrl)));
        return mongoFactory.createMongo();
    }

    /*@Bean
    public SagaStore sagaStore(){
        org.axonframework.mongo.eventhandling.saga.repository.MongoTemplate mongoTemplate =
                new org.axonframework.mongo.eventhandling.saga.repository.DefaultMongoTemplate(mongoClient());
        return new MongoSagaStore(mongoTemplate, axonJsonSerializer());
    }*/
}
