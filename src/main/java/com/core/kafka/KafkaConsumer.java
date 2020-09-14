package com.core.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import com.core.util.PropertiesLoader;

/**
 * 消费者（单例模式）
 * 
 * @author Administrator
 * 
 */
public class KafkaConsumer {

	private final String path = "/Kafka.properties";
	private ConsumerConnector consumer;
	private PropertiesLoader props = null;
	private final static String TOPIC = "TEST-TOPIC";

	private KafkaConsumer() {
		props = new PropertiesLoader(path);
		ConsumerConfig config = new ConsumerConfig(props.getProperties());
		consumer = kafka.consumer.Consumer.createJavaConsumerConnector(config);
	}

	public static KafkaConsumer getInstance() {
		return new KafkaConsumer();
	}

	public void consume() {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(TOPIC, new Integer(1));
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(
				new VerifiableProperties());

		Map<String, List<KafkaStream<String, String>>> consumerMap = consumer
				.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);
		KafkaStream<String, String> stream = consumerMap.get(TOPIC).get(0);
		ConsumerIterator<String, String> it = stream.iterator();
		while (it.hasNext())
			System.out.println(it.next().message());
	}

	public static void main(String[] args) {
		// 生产
		KafkaProducer producer = KafkaProducer.getInstance();
		producer.produce();

	}

}