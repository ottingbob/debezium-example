package com.debez.consumer.configuration;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.debez.consumer.configuration.ConsumerConstants.*;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ListenerConfiguration {

  @Autowired
  public ConsumerFactory<Object, Object> configFactory;
  // @Qualifier("consumer-factory")

  // @Bean("consumer-factory")
  // @Primary
  // public ConsumerFactory<Object, Object> consumerFactory() {
  //   final Map<String, Object> props = new HashMap<>();

  //   String kafkaUrl = System.getenv("KAFKA_URL");
  //   log.info("Read KAFKA_URL env to be [{}]", kafkaUrl);
  //   if (kafkaUrl == null) {
  //     kafkaUrl = "localhost:9092";
  //     log.info("Set bootstrap_servers to be [{}]", kafkaUrl);
  //   }

  //   props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
  //   props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

  //   props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
  //   props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

  //   return new DefaultKafkaConsumerFactory<>(props);
  // }

  // private ConsumerConfiguration consumerConfig;
  // public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

  // public ListenerConfiguration(ConsumerConfiguration consumerConfig) throws Exception {
  //   this.consumerConfig = consumerConfig;
  // }

  @Bean("listener-factory")
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<Object, Object> configFactory) throws Exception {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(configFactory);
    log.info("Factory bootstrap.servers [{}]", configFactory.getConfigurationProperties().get("bootstrap.servers"));

    // ContainerProperties props = factory.getContainerProperties();
    // props.getConsumerRebalanceListener();
    // props.setConsumerRebalanceListener(consumerRebalanceListener);
    // log.info(props.getClientId());

    return factory;
  }

  // @PostConstruct
  // public void init() throws Exception {
  //   log.info("Factory bean [{}]", configFactory.getConfigurationProperties().get("bootstrap.servers"));

  //   log.info("Factory name [{}]", consumerConfig.getName());
  //   // log.info("Factory bootstrap.servers [{}]", consumerConfig.consumerFactory
  //   //   .getConfigurationProperties().get("bootstrap.servers"));
  //   // this.kafkaListenerContainerFactory = kafkaListenerContainerFactory(consumerConfig.consumerFactory);
  // }

  // public class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
  //   private Consumer<?,?> consumer;

  //   public SaveOffsetsOnRebalance(Consumer<?,?> consumer) {
  //       this.consumer = consumer;
  //   }

  //   public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
  //       // save the offsets in an external store using some custom code not described here
  //       for(TopicPartition partition: partitions)
  //          saveOffsetInExternalStore(consumer.position(partition));
  //   }

  //   public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
  //       // read the offsets from an external store using some custom code not described here
  //       for(TopicPartition partition: partitions)
  //          consumer.seek(partition, readOffsetFromExternalStore(partition));
  //   }
  // }

}