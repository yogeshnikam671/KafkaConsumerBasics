package kafka.consumer.configuration;

public class KafkaConsumerConfig {
    public static final String applicationId = "Aditya";
    public static final String bootstrapServers = "localhost:9092,localhost:9093";
    public static final String groupId = "people-group";
    public static final String autoOffsetReset = "earliest";
    public static final String[] topicsToConsume = { "people-topic" };
    public static final String boringPeopleTopic = "boring-people-topic";
    public static final String awesomePeopleTopic = "awesome-people-topic";
}
