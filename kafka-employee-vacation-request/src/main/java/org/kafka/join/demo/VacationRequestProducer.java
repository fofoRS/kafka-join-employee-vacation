package org.kafka.join.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.OutgoingKafkaRecord;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Arrays.asList;

@ApplicationScoped
public class VacationRequestProducer {


    @Outgoing(value = "vacationRequests")
    public Multi<OutgoingKafkaRecord<String, String>> publishVacationRequest() {
        return Multi.createFrom().ticks()
                .every(Duration.ofSeconds(10))
                .onItem().transform(tick -> buildVacationRequestMessage())
                .onItem()
                .transform(requestMessageMap -> {
            ObjectMapper mapper = new ObjectMapper();
            try {
                return KafkaRecord.of(
                        (String)requestMessageMap.get("employee"),
                        mapper.writeValueAsString(requestMessageMap));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return KafkaRecord.of("","");
            }
        }).onOverflow().buffer(10);
    }

    private Map<String,Object> buildVacationRequestMessage() {
        List<String> employees = asList("Priscila","Emma", "Rodolfo");
        int randomEmployee = ThreadLocalRandom.current().nextInt(0,3);
        int randomVacationDays = ThreadLocalRandom.current().nextInt(0,20);
        String employee = employees.get(randomEmployee);
        return new HashMap<>() {{
            put("employee",employee);
            put("vacations",randomVacationDays);
        }};
    }
}
