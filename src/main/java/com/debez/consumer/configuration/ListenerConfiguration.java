package com.debez.consumer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ListenerConfiguration {

  @Autowired
  public ConsumerFactory<Object, Object> configFactory;

  @Bean("listener-factory")
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<Object, Object> configFactory) throws Exception {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(configFactory);
    log.info("ListenerFactory bootstrap.servers [{}]", configFactory.getConfigurationProperties().get("bootstrap.servers"));

    // ContainerProperties props = factory.getContainerProperties();
    // props.getConsumerRebalanceListener();
    // props.setConsumerRebalanceListener(consumerRebalanceListener);
    // log.info(props.getClientId());

    return factory;
  }

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