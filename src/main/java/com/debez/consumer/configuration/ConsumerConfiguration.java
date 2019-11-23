package com.debez.consumer.configuration;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.debez.consumer.Operation;
import com.debez.consumer.models.*;
import static com.debez.consumer.configuration.ConsumerConstants.*;


@Slf4j
@Configuration
public class ConsumerConfiguration {

  public ConsumerFactory<String, String> consumerFactory = consumerFactory();

  public ConsumerFactory<String, String> consumerFactory() {
    final Map<String, Object> props = new HashMap<>();

    String kafkaUrl = System.getenv("KAFKA_URL");
    if (kafkaUrl == null) {
      kafkaUrl = "localhost:9092";
    }

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props);
  }

}