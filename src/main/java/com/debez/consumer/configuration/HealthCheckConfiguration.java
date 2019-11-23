package com.debez.consumer.configuration;

import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.HashMap;

import static com.debez.consumer.configuration.ConsumerConstants.GROUP_ID;

@Slf4j
@Component
@ConditionalOnProperty(value = "kafka-config", havingValue = "running")
public class HealthCheckConfiguration {

  private final CountDownLatch countDownLatch;

  public HealthCheckConfiguration() throws InterruptedException {

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

  @Bean
  KafkaAdmin kafkaAdmin() {
    final Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    KafkaAdmin admin = new KafkaAdmin(props);
    admin.setFatalIfBrokerNotAvailable(true);
    admin.setCloseTimeout(1);

    return admin;
  }

  @Bean
  ApplicationRunner healthCheckRunner(KafkaAdmin admin) throws InterruptedException {
    log.info("Starting Health Check Bean");
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
}
