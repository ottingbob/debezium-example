package com.debez.consumer.alternate;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConditionalOnProperty(value = "kafka-config", havingValue = "alternate")
public class Listener {

  @KafkaListener(topics = "dbserver1.inventory.customers", groupId = "webservice")
  public void listen(String record) {
    log.info("I received a record: [{}]", record);
  }

}