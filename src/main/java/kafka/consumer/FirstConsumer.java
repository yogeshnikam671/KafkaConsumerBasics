package kafka.consumer;

import kafka.consumer.configuration.KafkaConsumerConfig;
import kafka.consumer.deserializer.JSONDeserializer;
import kafka.consumer.models.People;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class FirstConsumer {
    private static Logger logger = LoggerFactory.getLogger(FirstConsumer.class.getName());

    public static void main(String[] args) {
        String[] stringTopicsToConsume = { "aditya-topic" };
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, KafkaConsumerConfig.applicationId);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConfig.bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "string-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConsumerConfig.autoOffsetReset);


        KafkaConsumer<Integer, String> consumer = new KafkaConsumer<Integer, String>(props);

        consumer.subscribe(Arrays.asList(stringTopicsToConsume));
        logger.info("Listening to topic --> " + Arrays.toString(stringTopicsToConsume));

        while(true) {
            ConsumerRecords<Integer, String> messages = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<Integer, String> message : messages) {
                logger.info("The message received is --> " + message.value());
            }
        }
    }
}
