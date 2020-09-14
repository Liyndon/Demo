package com.core.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.core.util.PropertiesLoader;

/**
 * 生产者(单例模式)
 * 
 * @author Administrator
 * 
 */
public class KafkaProducer {
	private Producer<String, String> producer = null;
	private final static String TOPIC = "TEST-TOPIC";
	private PropertiesLoader loader = null;

	private KafkaProducer() {
		init();
	}

	public static KafkaProducer getInstance() {
		return new KafkaProducer();
	}

	private void init() {
		loader = new PropertiesLoader("/Kafka.properties");
		producer = new Producer<String, String>(new ProducerConfig(
				loader.getProperties()));
	}

	public void produce() {
		for (int i = 0; i < 100; i++) {
			String key = String.valueOf("key-" + i);
			String data = "hello kafka message " + key;
			producer.send(new KeyedMessage<String, String>(TOPIC, key, data));
			System.out.println(data);
		}
	}

	public static void main(String[] args) {
		// 消费
		KafkaConsumer consumer = KafkaConsumer.getInstance();
		consumer.consume();
	}
}
