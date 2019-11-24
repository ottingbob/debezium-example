package com.debez.consumer.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@ConditionalOnProperty(value = "kafka-config", havingValue = "example")
public class KafkaController {

  private final KafkaTemplate<String, Object> template;
  private final String topicName;
  private final int messagesPerRequest;
  private CountDownLatch latch;

  public KafkaController(
      final KafkaTemplate<String, Object> template,
      @Value("${tpd.topic-name}") final String topicName,
      @Value("${tpd.messages-per-request}") final int messagesPerRequest) {
    this.template = template;
    this.topicName = topicName;
    this.messagesPerRequest = messagesPerRequest;
  }

  @GetMapping("/hello")
  public String hello() throws Exception {
    latch = new CountDownLatch(messagesPerRequest);
    IntStream.range(0, messagesPerRequest)
      .forEach(i -> this.template.send(topicName, String.valueOf(i), 
        new PracticalAdvice("A piece of advice", i)));
    latch.await(60, TimeUnit.SECONDS);
    log.info("All messages received!");
    return "Hello Route!";
  }

  @KafkaListener(topics = "advice-topic", groupId = "json", containerFactory = "kafkaListenerContainerFactory")
  public void listenAsObject(final ConsumerRecord<String, PracticalAdvice> cr, @Payload final PracticalAdvice payload) {
    log.info("Logger 1 [JSON] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
        typeIdHeader(cr.headers()), payload, cr.toString());
  }

  @KafkaListener(topics = "advice-topic", groupId = "string", containerFactory = "kafkaListenerStringContainerFactory")
  public void listenasString(final ConsumerRecord<String, String> cr, @Payload final String payload) {
    log.info("Logger 2 [String] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
        typeIdHeader(cr.headers()), payload, cr.toString());
    latch.countDown();
  }

  @KafkaListener(topics = "advice-topic", groupId = "bytearray", containerFactory = "kafkaListenerByteArrayContainerFactory")
  public void listenAsByteArray(final ConsumerRecord<String, byte[]> cr, @Payload final byte[] payload) {
    log.info("Logger 3 [ByteArray] received key {}: Type [{}] | Payload: {} | Record: {}", cr.key(),
        typeIdHeader(cr.headers()), payload, cr.toString());
    latch.countDown();
  }

  private static String typeIdHeader(final Headers headers) {
    return StreamSupport.stream(headers.spliterator(), false)
      .filter(header -> header.key().equals("__TypeId__"))
      .findFirst().map(header -> new String(header.value())).orElse("N/A");
  }

/**
 *  private static String typeIdHeader(Headers headers) {
        return StreamSupport.stream(headers.spliterator(), false)
                .filter(header -> header.key().equals("__TypeId__"))
                .findFirst().map(header -> new String(header.value())).orElse("N/A");
    }
 * 
 */
}
