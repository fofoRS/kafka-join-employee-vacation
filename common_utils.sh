
./bin/kafka-topics.sh --zookeeper zookeeper:2181 --create --topic employeeVacationInformation \
  --partitions 3 --replication-factor 1 --config cleanup.policy=compact

./bin/kafka-topics.sh --zookeeper zookeeper:2181 --create --topic vacationRequests \
--partitions 1 --replication-factor 1

./bin/kafka-topics.sh --zookeeper zookeeper:2181 --create --topic vacationRequestsOutcome \
--partitions 1 --replication-factor 1 --config cleanup.policy=compact


#Kafka producer for employee-information topic

./bin/kafka-console-producer.sh --broker-list localhost:9092 \
--topic employeeVacationInformation \
--property "parse.key=true" \
--property "key.separator=,"

Rodolfo, {"firstName": "Rodolfo", "lastName": "Rojas", "hiredDate": "2020-09-21", "daysAvailable":5}
Emma,  {"firstName": "Emma", "lastName": "Rojas", "hiredDate": "2019-09-21", "daysAvailable":8}
Priscila, {"firstName": "Priscila", "lastName": "Jimenez", "hiredDate": "2015-09-21", "daysAvailable":11}

# kafka consumer for employee-information
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic employeeVacationInformation \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer


# kafka consumer for vacation-request
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic vacationRequests \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

# kafka consumer for vacation-request
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 \
    --topic vacationRequestsOutcome \
    --from-beginning \
    --formatter kafka.tools.DefaultMessageFormatter \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer




./bin/kafka-topics.sh --zookeeper zookeeper:2181 --create --topic file-topic-test \
--partitions 1 --replication-factor 1

./bin/kafka-console-producer.sh --broker-list localhost:9092 \
--topic file-topic-test

# download kafka mongo connect
curl -L -o mongo-kafka-connect.jar https://search.maven.org/remotecontent?filepath=org/mongodb/kafka/mongo-kafka-connect/1.2.0/mongo-kafka-connect-1.2.0-all.jar