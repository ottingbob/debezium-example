package com.debez.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import com.debez.consumer.models.*;

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

  private KafkaTemplate<String, DebezRecord> kafkaTemplate;

  public ProducerFactory<String, DebezRecord> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:3333");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  public KafkaTemplate<String, DebezRecord> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Test
  public void testKafkaTemplate() {
    Consumer<String, DebezRecord> consumer = runConsumer();
    
    After after = new After().withEmail("after@gmail.com").withId(123L)
      .withLastName("last_name").withFirstName("first_name");
    Before before = new Before().withEmail("before@gmail.com").withId(123L)
      .withLastName("last_name").withFirstName("first_name");
    Source source = new Source().withVersion("0.10.0.Final").withConnector("mysql").withName("dbserver1")
      .withTsMs(12345L).withSnapshot("false").withDb("inventory").withTable("customers").withServerId(111L)
      .withFile("mysql-bin.000003").withPos(708L).withRow(0L);
    Payload payload = new Payload().withOp("u").withSource(source).withAfter(after)
      .withBefore(before).withTsMs(System.currentTimeMillis());
    DebezRecord sendRecord = new DebezRecord().withPayload(payload).withSchema(new Schema().withFields(new ArrayList<Field>()));

    try {
      kafkaTemplate = kafkaTemplate();
      kafkaTemplate.send(TOPIC_NAME, sendRecord);
    } catch (Exception ex) {
      LOG.info("Exception found: {}", ex.getMessage());
    }

    ConsumerRecord<String, DebezRecord> receivedRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC_NAME);
    LOG.info("Received record from consumer: [{}]", receivedRecord.value().toString());  
    DebezRecord record = receivedRecord.value();

    Assert.assertEquals(sendRecord.getSchema(), record.getSchema());
    Assert.assertEquals(sendRecord.getPayload(), record.getPayload());
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

  public Consumer<String, DebezRecord> runConsumer() {
    Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
    configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    Consumer<String, DebezRecord> consumer = 
      new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), 
        new JsonDeserializer<>(DebezRecord.class)).createConsumer();
    consumer.subscribe(Collections.singleton(TOPIC_NAME));
    return consumer;
  }

}
