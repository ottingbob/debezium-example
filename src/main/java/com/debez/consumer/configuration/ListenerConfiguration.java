package com.debez.consumer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class ListenerConfiguration {

  private final ConsumerConfiguration consumerConfig;
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory;

  public ListenerConfiguration(ConsumerConfiguration consumerConfig) throws Exception {
    this.consumerConfig = consumerConfig;
    this.kafkaListenerContainerFactory = kafkaListenerContainerFactory(consumerConfig.consumerFactory);
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

  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory) throws Exception {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);

    // ContainerProperties props = factory.getContainerProperties();
    // props.getConsumerRebalanceListener();
    // props.setConsumerRebalanceListener(consumerRebalanceListener);
    // log.info(props.getClientId());

    return factory;
  }

}