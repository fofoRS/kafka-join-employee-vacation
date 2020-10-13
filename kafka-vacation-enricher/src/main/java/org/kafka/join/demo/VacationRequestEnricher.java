package org.kafka.join.demo;


import io.quarkus.kafka.client.serialization.JsonbSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.kafka.join.demo.model.EmployeeVacationDays;
import org.kafka.join.demo.model.VacationRequests;
import org.kafka.join.demo.model.VacationRequestAggregate;
import org.kafka.join.demo.model.VacationRequestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.LocalDate;

@ApplicationScoped
public class VacationRequestEnricher {

    Logger logger = LoggerFactory.getLogger(VacationRequestEnricher.class);

    @Produces
    public Topology createStreamTopology() {

        JsonbSerde<EmployeeVacationDays> daysAvailableSerde = new JsonbSerde<>(EmployeeVacationDays.class);
        JsonbSerde<VacationRequests> vacationRequestSerde = new JsonbSerde<>(VacationRequests.class);
        JsonbSerde<VacationRequestResult>  vacationResultSerde= new JsonbSerde<>(VacationRequestResult.class);

        StreamsBuilder topologyBuilder = new StreamsBuilder();

        KStream<String, VacationRequests> vacationRequestStream =
                topologyBuilder.stream(
                        "vacationRequests",
                        Consumed.with(Serdes.String(),vacationRequestSerde));

        GlobalKTable<String, EmployeeVacationDays> employeeDaysKTable =
                topologyBuilder.globalTable(
                        "employeeVacationInformation",
                        Consumed.with(Serdes.String(),daysAvailableSerde));


        KStream<String, VacationRequestResult> vacationRequestResultStream =
                vacationRequestStream.join(
                        employeeDaysKTable,
                        (tableKey,tableValue) -> tableKey,
                        (streamValue,tableValue) ->
                                new VacationRequestAggregate(
                                        streamValue.getEmployee(),
                                        streamValue.getVacations(),
                                        tableValue.getHiredDate(),
                                        tableValue.getDaysAvailable()))
                        .mapValues(this::validateVacationRequest);

        vacationRequestResultStream.to(
                "vacationRequestsOutcome",
                Produced.with(Serdes.String(),vacationResultSerde));
        return topologyBuilder.build();
    }

    private VacationRequestResult validateVacationRequest(VacationRequestAggregate vacationRequest) {
        LocalDate hiredDate = vacationRequest.getHiredDate();
        int daysRequested = vacationRequest.getVacations();
        int daysAvailable = vacationRequest.getDaysAvailable();

        long daysWorking = LocalDate.now().toEpochDay() - hiredDate.toEpochDay();
        String requestStatus;
        String reason;
        if(daysWorking <= 365) {
            logger.info("Employee {}, hiredDate {}",vacationRequest.getEmployee(),hiredDate);
            requestStatus = "DENIED";
            reason = "Less than one year working in the company.";
        } else {
            /* if not exceed the 5% of the total days available, the request is accepted*/
            float percentageOfDaysRequest = (daysAvailable * daysRequested) / 100;

            if(daysRequested <= daysAvailable) {
                requestStatus = "ACCEPTED";
                reason = "";
            } else if (percentageOfDaysRequest <= 0.2) {
                requestStatus = "ACCEPTED";
                reason = "";
            } else {
                requestStatus = "DENIED";
                reason = "Exceed the days available for vacations.";
            }
        }
        return new VacationRequestResult(
                vacationRequest.getEmployee(),
                daysRequested,daysAvailable,requestStatus,reason);
    }
}
