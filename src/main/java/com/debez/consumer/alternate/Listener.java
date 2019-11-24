package com.debez.consumer.alternate;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;

import com.debez.consumer.Operation;
import com.debez.consumer.models.DebezRecord;
import static com.debez.consumer.configuration.ConsumerConstants.*;


@Slf4j
@ConditionalOnProperty(value = "kafka-config", havingValue = "alternate")
public class Listener {

  @KafkaListener(id = "listener", topics = TOPIC_NAME, groupId = GROUP_ID)
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