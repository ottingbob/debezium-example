package com.debez.consumer;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;
import java.util.HashMap;

import com.debez.consumer.models.DebezRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@EnableKafka
@Configurable
@Configuration
public class ConsumerService {
 
  public static final String TOPIC_NAME = "dbserver1.inventory.customers"; 
  public static final String GROUP_ID = "debez-consumer-group"; 

  public ConsumerService() {
    log.info("Starting ConsumerService with TOPIC_NAME: {}", TOPIC_NAME);
  }

  @Bean
  public ConsumerFactory<String, String> consumerFactory() {
      final Map<String, Object> props = new HashMap<>();
    // props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    // StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    
    return new DefaultKafkaConsumerFactory<>(props);
    // return new DefaultKafkaConsumerFactory<String, DebezRecord>(
    //   props, new StringDeserializer(), new JsonDeserializer<>(DebezRecord.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory =
      new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID  )
  public void listen(final String record) throws Exception {
    // log.info("{}", record.)
    // log.info("Received Messasge in group foo: {}", record);

    final ObjectMapper mapper = new ObjectMapper();
    DebezRecord dRecord = mapper.readValue(record.toString(), DebezRecord.class);

    log.info("First Name Before: [{}]", dRecord.getPayload().getBefore().getFirstName());
    log.info("First Name After: [{}]", dRecord.getPayload().getAfter().getFirstName());

    // log.info(dRecord.getSchema().toString());
  }
}