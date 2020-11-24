package org.kafka.join.demo.connector;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import org.kafka.join.demo.annotations.InMemorySink;
import org.kafka.join.demo.annotations.MongoSink;
import org.kafka.join.demo.model.DataStreamRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
@MongoSink
public class MongoDbSinkImplementor <String, V extends DataStreamRecord>
        implements DataStreamSink<String, V> {

    Logger logger = LoggerFactory.getLogger(MongoDbSinkImplementor.class);

    private boolean isPanacheEntityInstance(DataStreamRecord record) {
        Class<ReactivePanacheMongoEntity> reactivePanacheEntityClazz  =
                ReactivePanacheMongoEntity.class;
        Class<PanacheMongoEntity> panacheEntityClazz =
                PanacheMongoEntity.class;

       return  reactivePanacheEntityClazz.isInstance(record) ||
        panacheEntityClazz.isInstance(record);
    }

    @Override
    public void apply(String key, V value) {
        if(!isPanacheEntityInstance(value)) {
            logger.info("Sink key {} into mongo!!!", key);
            ReactivePanacheMongoEntity.persist(value).onItemOrFailure()
                    .transform((result,exception) -> {
                        if(exception != null) {
                            logger.error("Entity failed during persisting", exception);
                        } else {
                            logger.info("The sink on the record entity was success!!!");
                        }
                        return "Successs";
                    }).subscribe();
        } else {
            logger.info("No ReactivePanacheMongoEntity instance!!!!");
        }
    }
}
