package kafka.consumer;

import kafka.consumer.configuration.KafkaConsumerConfig;
import kafka.consumer.configuration.KafkaProducerConfig;
import kafka.consumer.deserializer.JSONDeserializer;
import kafka.consumer.models.Hobby;
import kafka.consumer.models.People;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class JSONConsumer {
    private static Logger logger = LoggerFactory.getLogger(JSONConsumer.class.getName());

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, KafkaConsumerConfig.applicationId);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConsumerConfig.bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JSONDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConsumerConfig.groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConsumerConfig.autoOffsetReset);

        props.put(JSONDeserializer.VALUE_CLASS_NAME_CONFIG, People.class);


        KafkaConsumer<Integer, People> consumer = new KafkaConsumer<Integer, People>(props);

        consumer.subscribe(Arrays.asList(KafkaConsumerConfig.topicsToConsume));
        logger.info("Listening to topic --> " + KafkaConsumerConfig.topicsToConsume[0]);


        while(true) {
            ConsumerRecords<Integer, People> messages = consumer.poll(Duration.ofMillis(100));

            for(ConsumerRecord<Integer, People> message : messages) {
                logger.info("The message received is --> " + message.value());

                KafkaProducer<Integer, String> producer = getProducer();
                People people = message.value();
                if(people.getHobbies() == null)
                    publishToBoringPeopleTopic(people.getName(), producer);
                else
                    publishToAwesomePeopleTopic(people.getName(), producer);

                displayPeopleInfo(people, logger);
                producer.close();
            }
        }
    }

    public static void displayPeopleInfo(People people, Logger logger) {
        logger.info("This is amazing, see what I got -->");
        logger.info("Name: " + people.getName());
        logger.info("Age: " + people.getAge());
        logger.info("Hobbies: ");
        if(people.getHobbies() != null) {
            for(Hobby hobby: people.getHobbies()) {
                logger.info(hobby.getHobbyName() + " Reason: " + hobby.getReason());
            }
        }
    }

    public static KafkaProducer<Integer, String> getProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProducerConfig.applicationId);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConfig.bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<Integer, String>(props);
    }

    public static void publishToBoringPeopleTopic(String name, KafkaProducer<Integer, String> producer) {
        ProducerRecord<Integer, String> boringPeople
                = new ProducerRecord<Integer, String>(KafkaProducerConfig.boringPeopleTopic, name);
        producer.send(boringPeople);
    }

    public static void publishToAwesomePeopleTopic(String name, KafkaProducer<Integer, String> producer) {
        ProducerRecord<Integer, String> awesomePeople
                = new ProducerRecord<Integer, String>(KafkaProducerConfig.awesomePeopleTopic, name);
        producer.send(awesomePeople);
    }
}

















