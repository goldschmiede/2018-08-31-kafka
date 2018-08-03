package gs;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class CountConsumer extends Thread{
    private final Properties props = new Properties();
    private final KafkaConsumer<String, String> consumer;

    public CountConsumer(){
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker-1.k.anderscore.com:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "testgroup2");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "500");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);

        consumer = new KafkaConsumer<String, String>(props);
    }


    public void run() {
        consumer.subscribe(Arrays.asList("WordsWithCountsTopic"));
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
            records.forEach(record -> System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value()));
        }
    }
}
