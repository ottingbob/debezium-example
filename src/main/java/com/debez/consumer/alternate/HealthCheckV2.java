package com.debez.consumer.alternate;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

import static com.debez.consumer.configuration.ConsumerConstants.GROUP_ID;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka-config", havingValue = "alternate")
@AutoConfigureAfter(AlternateService.class)
public class HealthCheckV2 {

  @Autowired
  public ConsumerFactory<Object, Object> consumerFactory;

  private final CountDownLatch countDownLatch;

  public HealthCheckV2() throws InterruptedException {
    this.countDownLatch = new CountDownLatch(1);
    
    // Health Check Manages the CountDownLatch
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
  }

  private Map<String, Object> copyMap(Map<String, Object> sourceMap) {
    Map<String, Object> newMap = new HashMap<>();

    for(Entry<String, Object> entry : sourceMap.entrySet()) {
      newMap.put(entry.getKey(), entry.getValue());
    }

    return newMap;
  }

  @Bean
  ApplicationRunner healthCheckRunner(ConsumerFactory<Object, Object> consumerFactory) throws InterruptedException {
    log.info("Starting Health Check Bean");

    Map<String, Object> props = copyMap(consumerFactory.getConfigurationProperties());
    props.remove("key.deserializer");
    props.remove("value.deserializer");
    props.remove("group.id");
    KafkaAdmin admin = new KafkaAdmin(props);

    log.info("Received boostrap servers config: [{}]", admin.getConfig().get("bootstrap.servers"));
    admin.setFatalIfBrokerNotAvailable(true);

    // So this gets only checked at initialization ... pretty bad implementation
    AdminClient client1 = AdminClient.create(admin.getConfig());
    DescribeClusterResult result = client1.describeCluster();
    log.info("Consumer Groups Status: [{}]", client1.listConsumerGroups() != null);
    int elapsedTimeoutMs = 0;

    while(!result.controller().isDone()) {
      if (elapsedTimeoutMs % 10 == 0 && elapsedTimeoutMs != 0) {
        log.info("result.controller().isDone(): {}", result.controller().isDone());

        if (elapsedTimeoutMs % 100 == 0 && elapsedTimeoutMs != 0) {
          log.info("Close it down");
          countDownLatch.countDown();
          break;
        }
      }

      elapsedTimeoutMs += 1;
      Thread.sleep(100L);
    }
    client1.close();
    log.info("Broker is up! Stopping health check connection");

    return args -> {
      try (AdminClient client = AdminClient.create(admin.getConfig())) {
        log.info("Received boostrap servers config: [{}]", admin.getConfig().get("bootstrap.servers"));
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
}
