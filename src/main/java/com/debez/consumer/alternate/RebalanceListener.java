package com.debez.consumer.alternate;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConditionalOnProperty(value = "kafka-config", havingValue = "alternate")
public class RebalanceListener implements ConsumerRebalanceListener {

  private Collection<TopicPartition> assigned;

  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> collection) {
    log.info("onPartitionsRevoked()");
  }

  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
    this.assigned = partitions;
    log.info("onPartitionsAssigned()");
  }
}