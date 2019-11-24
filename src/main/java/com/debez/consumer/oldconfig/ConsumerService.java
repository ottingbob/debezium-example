package com.debez.consumer.oldconfig;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

import javax.annotation.PostConstruct;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import com.debez.consumer.Operation;
import com.debez.consumer.models.DebezRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.debez.consumer.configuration.ConsumerConstants.*;

@Slf4j
@EnableKafka
// @Configurable
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ConsumerService {

  @Autowired
  public ConcurrentKafkaListenerContainerFactory<Object, Object> factory;

  @Autowired
  private KafkaListenerEndpointRegistry registry;

  @PostConstruct
  public void init() {
    log.info("Starting ConsumerService with TOPIC_NAME: {}", TOPIC_NAME);
    log.info("Factory bootstrap-servers [{}]", factory.getConsumerFactory()
      .getConfigurationProperties().get("bootstrap.servers"));

    log.info("Registry Container Count [{}]", registry.getAllListenerContainers().size());

    registry.getListenerContainers().stream().forEach(container -> {
      log.info("Listener ID: [{}]", ((MessageListenerContainer) container).getListenerId());
    });

    factory.getContainerProperties().setConsumerRebalanceListener(new ConsumerAwareRebalanceListener() {

      @Override
      public void onPartitionsRevokedBeforeCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        SaveOffsetsOnRebalance listener = new SaveOffsetsOnRebalance(consumer);
        listener.onPartitionsAssigned(partitions);
        // log.warn("onPartitionsRevokedBeforeCommit()");
      }
  
      @Override
      public void onPartitionsRevokedAfterCommit(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        log.warn("onPartitionsRevokedAfterCommit()");
      }
  
      @Override
      public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
        // log.warn("onPartitionsAssigned()");
        SaveOffsetsOnRebalance listener = new SaveOffsetsOnRebalance(consumer);
        listener.onPartitionsAssigned(partitions);
      }
    });  

    // factory.getContainerProperties().setConsumerRebalanceListener(new SaveOffsetsOnRebalance());
    ConsumerRebalanceListener listener = factory.getContainerProperties().getConsumerRebalanceListener();
    listener.onPartitionsAssigned(Collections.singletonList(new TopicPartition("clown_town", 5)));
  }



  public class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {
    private Consumer<?,?> consumer;

    public SaveOffsetsOnRebalance(Consumer<?,?> consumer) {
        this.consumer = consumer;
    }

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

  @KafkaListener(id = "listener", topics = TOPIC_NAME, groupId = GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
  public void listen(@Payload(required = false) final String record) throws Exception {

    // It looks like after the DELETE Operation it sends an additional NULL message which
    // is why I annotation listen with the @Payload required prop to be false
    if (record == null) {
      log.warn("Received a null record");
      return;
    }

    final ObjectMapper mapper = new ObjectMapper();
    final DebezRecord dRecord = mapper.readValue(record.toString(), DebezRecord.class);

    final Map<String, Operation> operationMap = new HashMap<>() {
      private static final long serialVersionUID = 1L;
      {
        put("u", Operation.UPDATE);
        put("c", Operation.INSERT);
        put("d", Operation.DELETE);
      }
    };
    final Operation operation = operationMap.get(dRecord.getPayload().getOp());

    // Operations are `u` (pdate), `c` (nsert), `d` (elete)
    log.info("Operation: {}", operation.name());
    switch (operation) {
      case UPDATE:
        log.info("First Name Before: [{}]", dRecord.getPayload().getBefore().getFirstName());
        log.info("First Name After: [{}]", dRecord.getPayload().getAfter().getFirstName());
        break;
      case INSERT:
      log.info("Insert Record New First Name: [{}]", dRecord.getPayload().getAfter().getFirstName());  
      break;
      case DELETE:
        log.info("Delete Record Removed First Name: [{}]", dRecord.getPayload().toString());
        break;
    }
  }
}
