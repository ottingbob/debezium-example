package com.debez.consumer.alternate;

import java.util.Collection;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RebalanceListener implements ConsumerRebalanceListener {
  
  private Collection<TopicPartition> assigned;

  @Override
  public void onPartitionsRevoked(Collection<TopicPartition> collection) {
    log.warn("onPartitionsRevoked()");
  }

  @Override
  public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
    this.assigned = partitions;
    log.warn("onPartitionsAssigned()");
  }
}