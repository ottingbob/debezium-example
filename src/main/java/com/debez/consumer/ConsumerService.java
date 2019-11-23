package com.debez.consumer;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.NetworkClient;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.HashMap;

import com.debez.consumer.models.DebezRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@EnableKafka
@Configurable
@Configuration
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class ConsumerService {

  public static final String TOPIC_NAME = "dbserver1.inventory.customers";
  public static final String GROUP_ID = "debez-consumer-group";

  private CountDownLatch countDownLatch;

  public ConsumerService() throws InterruptedException {
    log.info("Starting ConsumerService with TOPIC_NAME: {}", TOPIC_NAME);
    this.countDownLatch = new CountDownLatch(1);
    // NetworkClient nc = new NetworkClient();
    Runnable runnable = () -> {
      try {
        this.countDownLatch.await();
        System.exit(0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
    Thread t = new Thread(runnable);
    t.start();
    // runnable.run();
  }

  @Bean
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

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() throws Exception {
    final ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());

    // ContainerProperties props = factory.getContainerProperties();
    // log.info(props.getClientId());

    // RetryTemplate template = new RetryTemplate();
    // TimeoutRetryPolicy policy = new TimeoutRetryPolicy();
    // policy.setTimeout(3L);
    // template.setRetryPolicy(policy);

    // try {
    //   template.execute(new RetryCallback<String, Exception>() {
    //     @Override
    //     public String doWithRetry(RetryContext context) throws Exception {
    //       // TODO Auto-generated method stub
    //       return "nice";
    //     }
    //   });
    // } catch(Exception ex) {
    //   log.error("niceerrrr");
    // }

    // factory.setRetryTemplate(template);

    // factory.setRecoveryCallback(new RecoveryCallback<Object>() {
    //   public Object recover(RetryContext context) throws Exception {
    //     log.info("Recovery time");
    //     return "nice";
    //   }
    // });
    // factory.setApplicationContext();
    // Set a custom ConsumerRebalanceListener


    // ConsumerRebalanceListener listener = props.getConsumerRebalanceListener();
    // listener

    return factory;
  }

  @Bean KafkaAdmin kafkaAdmin() {
    final Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    KafkaAdmin admin = new KafkaAdmin(props);
    admin.setFatalIfBrokerNotAvailable(true);
    admin.setCloseTimeout(1);

    return admin;
  }

  @Bean
  public ApplicationRunner runner(KafkaAdmin admin) throws InterruptedException {
    log.info("Starting Health Check Bean");
    admin.setFatalIfBrokerNotAvailable(true);

    // So this gets only checked at initialization ... pretty bad implementation
    AdminClient client1 = AdminClient.create(admin.getConfig());
    DescribeClusterResult result = client1.describeCluster();
    int elapsedTimeoutMs = 0;

    while(!result.controller().isDone()) {
      if (elapsedTimeoutMs % 10 == 0 && elapsedTimeoutMs != 0) {
        log.info("result.controller().isDone(): {}", result.controller().isDone());

        if (elapsedTimeoutMs % 100 == 0 && elapsedTimeoutMs != 0) {
          log.info("Close it down");
          countDownLatch.countDown();
        }
      }

      elapsedTimeoutMs += 1;
      Thread.sleep(100L);
    }
    client1.close();
    log.info("Broker is up! Stopping health check connection");

    return args -> {
      try (AdminClient client = AdminClient.create(admin.getConfig())) {
        log.warn(client.toString());
        while(true) {
          Map<String, ConsumerGroupDescription> map =
            client.describeConsumerGroups(Collections.singletonList(GROUP_ID))
              .all().get(10, TimeUnit.SECONDS);
          log.info(map.toString());
          System.in.read();
          Thread.sleep(10000L);
        }
      } catch (Exception ex) {
        log.error("gotem");
      }
    };
  }

  // @StreamListener(Sink.INPUT)
  // @StreamListener(Sink.INPUT) 
  // public void foo(String in){
  //   System.out.println(in);
  // }

  // @EventListener
  // public void events(KafkaEvent event) {
  //   System.out.println(event);
  // }

  @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID  )
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