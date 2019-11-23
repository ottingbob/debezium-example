package com.debez.consumer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;
import java.util.HashMap;

import com.debez.consumer.configuration.HealthCheckConfiguration;
import com.debez.consumer.models.DebezRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.debez.consumer.configuration.ConsumerConstants.*;

@Slf4j
@EnableKafka
@Configurable
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ConsumerService {

  private final HealthCheckConfiguration healthCheck;

  public ConsumerService(HealthCheckConfiguration healthCheck) {
  // public ConsumerService() {
    log.info("Starting ConsumerService with TOPIC_NAME: {}", TOPIC_NAME);
    this.healthCheck = healthCheck;
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
  public void listen(@Payload(required = false) final String record) throws Exception {
    // It looks like after the DELETE Operation it sends an additional NULL message which
    // is why I annotation listen with the @Payload required prop to be false
    if (record == null) {
      log.warn("Received a null record");
      return;
    }

    final ObjectMapper mapper = new ObjectMapper();
    final DebezRecord dRecord = mapper.readValue(record.toString(), DebezRecord.class);

    final Map<String, Operation> operationMap = new HashMap<>() {
      private static final long serialVersionUID = 1L;
      {
        put("u", Operation.UPDATE);
        put("c", Operation.INSERT);
        put("d", Operation.DELETE);
      }
    };
    final Operation operation = operationMap.get(dRecord.getPayload().getOp());

    // Operations are `u` (pdate), `c` (nsert), `d` (elete)
    log.info("Operation: {}", operation.name());
    switch (operation) {
      case UPDATE:
        log.info("First Name Before: [{}]", dRecord.getPayload().getBefore().getFirstName());
        log.info("First Name After: [{}]", dRecord.getPayload().getAfter().getFirstName());
        break;
      case INSERT:
      log.info("Insert Record New First Name: [{}]", dRecord.getPayload().getAfter().getFirstName());  
      break;
      case DELETE:
        log.info("Delete Record Removed First Name: [{}]", dRecord.getPayload().toString());
        break;
    }
  }
}
