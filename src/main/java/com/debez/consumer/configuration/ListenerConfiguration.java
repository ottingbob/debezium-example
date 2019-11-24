package com.debez.consumer.configuration;

import java.util.Collection;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.ContainerProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configurable
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ListenerConfiguration {

  @Autowired
  public ConsumerFactory<Object, Object> configFactory;

  @Bean("listener-factory")
  @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
  public ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory(
      ConsumerFactory<Object, Object> configFactory) throws Exception {
      // ConcurrentKafkaListenerContainerFactoryConfigurer configurer) throws Exception {
    final ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(configFactory);
    log.info("ListenerFactory bootstrap.servers [{}]", configFactory.getConfigurationProperties().get("bootstrap.servers"));

    // configurer.configure(factory, configFactory);

    // ContainerProperties props = factory.getContainerProperties();
    // props.setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {
    //   @Override
    //   public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
    //     log.warn("onPartitionsRevokedBeforeCommit()");
    //   }
  
    //   @Override
    //   public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
    //     log.warn("onPartitionsRevokedAfterCommit()");
    //   }
  
    //   @Override
    //   public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
    //     log.warn("onPartitionsAssigned()");
    //   }
    // });

    return factory;
  }

  // @Bean
  // public ConsumerAwareRebalanceListener rebalanceListener() {
  //   return new ConsumerAwareRebalanceListener() {
  //     // System.out.println("New ConsumerAwareRebalanceListener");
  //   };
  // }

  // @Bean
  // public ConcurrentKafkaListenerContainerFactoryConfigurer kafkaListenerContainerFactoryConfigurer(
  //     ConsumerAwareRebalanceListener rebalanceListener) {
  //   return new ConcurrentKafkaListenerContainerFactoryConfigurer() {
  //     @Override
  //     public void configure(ConcurrentKafkaListenerContainerFactory<Object, Object> kafkaListenerContainerFactory,
  //         ConsumerFactory<Object, Object> configFactory) {
  //       super.configure(kafkaListenerContainerFactory, configFactory);
  //       kafkaListenerContainerFactory.getContainerProperties()
  //         .setConsumerRebalanceListener(rebalanceListener);
  //     }
  //   };
  // }


  public class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
    // private Consumer<?,?> consumer;

    // public SaveOffsetsOnRebalance(Consumer<?,?> consumer) {
    //     this.consumer = consumer;
    // }

    // public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
    //   log.error("onPartitionsRevokedBeforeCommit()");
    // }

    // public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
    //   log.error("onPartitionsRevokedAfterCommit()");
    // }

    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        // save the offsets in an external store using some custom code not described here
        // for(TopicPartition partition: partitions)
        //    saveOffsetInExternalStore(consumer.position(partition));
        log.error("onPartitionsRevoked()");
    }

    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        // read the offsets from an external store using some custom code not described here
        // for(TopicPartition partition: partitions)
        //    consumer.seek(partition, readOffsetFromExternalStore(partition));
        log.error("onPartitionsAssigned()");
    }
  }

  // @Bean 
  // public ConsumerRebalanceListener rebalanceListener() {
  //   return new SaveOffsetsOnRebalance();
  // }

}