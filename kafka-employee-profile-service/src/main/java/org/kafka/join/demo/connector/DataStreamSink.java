package org.kafka.join.demo.connector;

import org.kafka.join.demo.model.DataStreamRecord;

public interface DataStreamSink<K,V extends DataStreamRecord> {
    void apply(K key, V value);
}
