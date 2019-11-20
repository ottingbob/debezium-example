package com.debez.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Assert;
// import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

// import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@RunWith(SpringRunner.class)
@EnableKafka
@SpringBootTest
@EmbeddedKafka(partitions = 1, controlledShutdown = false,
  brokerProperties = {"listeners=PLAINTEXT://localhost:3333", "port=3333"},
  topics = { KTTest.TOPIC_NAME })
public class KTTest {

  public static final Logger LOG = LoggerFactory.getLogger(KTTest.class);
  public static final String TOPIC_NAME = "clown_town";

  @Autowired
  EmbeddedKafkaBroker embeddedKafkaBroker;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Test
  public void testKafkaTemplate() {
    Consumer<String, String> consumer = runConsumer();
    kafkaTemplate.send(TOPIC_NAME, "test");

    ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, TOPIC_NAME);
    Assert.assertEquals("test", record.value());
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = "GROUP-ID-1")
  public void listen(String message) {
    LOG.info("Recived message in group 1: {}", message);
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = "GROUP-ID-2")
  public void listen2(String message) {
    LOG.info("Recived message in group 2: {}", message);
  }

  @KafkaListener(topics = TOPIC_NAME, groupId = "GROUP-ID-3")
  public void listen3(String message) {
    LOG.info("Recived message in group 3: {}", message);
  }

  public Consumer<String, String> runConsumer() {
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    Consumer<String, String> consumer = 
      new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
    consumer.subscribe(Collections.singleton(TOPIC_NAME));
    return consumer;
  }

}
