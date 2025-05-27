package com.astonlabs;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.*;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class AppTest {

    private static final String TOPIC = "test-topic";


    @Container
    public static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.0")
    );

    private KafkaProducer<String, String> producer;
    private KafkaConsumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        // Настройка продюсера
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(producerProps);

        // Настройка консюмера
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(List.of(TOPIC));
    }

    @Test
    void testSendMessageToKafka() {
        // отправка
        String message = "Hello from TestContainers!";
        producer.send(new ProducerRecord<>(TOPIC, message));
        producer.flush();

        // получение
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        assertFalse(records.isEmpty(), "No records received from Kafka");

        Optional<String> receivedMessage = StreamSupport.stream(records.records(TOPIC).spliterator(), false)
                .map(ConsumerRecord::value)
                .findFirst();

        assertEquals(message, receivedMessage.orElse(null));
    }

    @AfterEach
    void tearDown() {
        producer.close();
        consumer.close();
    }
}
