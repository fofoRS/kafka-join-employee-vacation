package org.kafka.join.demo;

import io.quarkus.kafka.client.serialization.JsonbSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.kafka.join.demo.annotations.MongoSink;
import org.kafka.join.demo.connector.DataStreamSink;
import org.kafka.join.demo.model.EmployeeVacationInformation;
import org.kafka.join.demo.model.VacationRequestResult;
import org.kafka.join.demo.annotations.InMemorySink;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;


@ApplicationScoped
public class VacationResultEventHandler {

    /*
        takes only the vacation request that are approved  and update user profile vacation days available
     */

    @MongoSink
    @Inject
    private DataStreamSink<String, VacationRequestResult> sink;

    @Produces
    public Topology createVacationResultTopology() {
        JsonbSerde<VacationRequestResult> vacationRequestResultSerde =
                new JsonbSerde<>(VacationRequestResult.class);
        JsonbSerde<EmployeeVacationInformation> vacationInformationSerde =
                new JsonbSerde<>(EmployeeVacationInformation.class);
        StreamsBuilder streamsBuilder = new StreamsBuilder();

        streamsBuilder
                .stream(
                        "vacationRequestsOutcome",
                        Consumed.with(Serdes.String(),vacationRequestResultSerde))
                .filter((key,vacationRequestResult) -> vacationRequestResult.getStatus().equals("ACCEPTED"))
                .peek((key,vacationResult) -> {
                    sink.apply(key,vacationResult);
                    System.out.println("Message key: " + key);
                });
        return streamsBuilder.build();
    }
}
