package com.debez.consumer.oldconfig;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.HashMap;

import static com.debez.consumer.configuration.ConsumerConstants.*;


@Slf4j
@Configuration("consumer-config")
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ConsumerConfiguration {

  @Bean
  public ConsumerFactory<Object, Object> consumerFactory() {
    final Map<String, Object> props = new HashMap<>();

    String kafkaUrl = System.getenv("KAFKA_URL");
    log.info("Read KAFKA_URL env to be [{}]", kafkaUrl);
    if (StringUtil.isNullOrEmpty(kafkaUrl)) {
      kafkaUrl = "localhost:9092";
      log.info("Set bootstrap_servers to be [{}]", kafkaUrl);
    }

    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

    return new DefaultKafkaConsumerFactory<>(props);
  }

}