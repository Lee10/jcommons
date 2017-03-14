package org.lee.coderepo.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lipeilin on 16/10/6 19:05.
 */
public class SimpleConsumer {

	private Properties props;
	public SimpleConsumer(Properties props){
		this.props = props;
	}

	public void execute(String topic, ConsumerCallback callback){

		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while(it.hasNext()) {
			callback.consume(new String(it.next().message()));
		}
	}
}
