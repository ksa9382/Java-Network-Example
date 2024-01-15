package org.example;

import org.apache.kafka.clients.producer.*;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String TOPIC = "test-topic";
    private static final Integer KEY = 1;

    public static void main( String[] args ) throws Exception
    {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File("")));
        Properties prop = new Properties();
        prop.load(bis);
        bis.close();

        Producer<String, String> producer = new KafkaProducer<String, String>(prop);

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, String.valueOf(KEY), "1234");
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                System.out.println("recordMetadata.toString() = " + recordMetadata.toString());
            }
        });

    }
}
