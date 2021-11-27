package kafka.consumer.configuration;

public class KafkaProducerConfig {
    public final static String applicationId = "producer";
    public final static String bootstrapServers = "localhost:9092,localhost:9093";
    public static final String boringPeopleTopic = "boring-people-topic";
    public static final String awesomePeopleTopic = "awesome-people-topic";
}
