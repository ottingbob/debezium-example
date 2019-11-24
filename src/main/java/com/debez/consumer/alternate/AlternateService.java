package com.debez.consumer.alternate;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Service;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import static com.debez.consumer.configuration.ConsumerConstants.*;

@Slf4j
@EnableAutoConfiguration
@EnableKafka
@Service
@ConditionalOnProperty(value = "kafka-config", havingValue = "alternate")
public class AlternateService {

  @Bean
  public RebalanceListener rebalanceListener() {
    return new RebalanceListener();
  }

  @Bean
  ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(RebalanceListener rebalanceListener) {
    ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.getContainerProperties().setConsumerRebalanceListener(rebalanceListener);
    return factory;
  }

  @Bean
  public ConsumerFactory<Object, Object> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfigs());
  }

  @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>();

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
    return props;
  }

  @Bean
  public Listener listener() {
    return new Listener();
  }

}