package org.kafka.join.demo.connector;

import org.kafka.join.demo.annotations.InMemorySink;
import org.kafka.join.demo.model.DataStreamRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
@InMemorySink
public class PrintDataSinkImplementor<String, V extends DataStreamRecord> implements DataStreamSink<String,V> {

    private static final Logger logger = LoggerFactory.getLogger(PrintDataSinkImplementor.class);

    @Override
    public void apply(String key, V value) {
        logger.info("Printing data with key {} and value {}", key, value.deserializeStreamRecord());
    }
}
