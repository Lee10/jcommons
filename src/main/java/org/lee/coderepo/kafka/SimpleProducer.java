package org.lee.coderepo.kafka;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * Created by lipeilin on 16/10/6 18:41.
 */
public class SimpleProducer {

	private Producer<String, String> producer;

	public SimpleProducer(String broker){

		Properties props = new Properties();
		props.put("metadata.broker.list", broker);
		props.put("serializer.class", "kafka.serializer.StringEncoder");

		producer = new Producer<String, String>(new ProducerConfig(props));
	}

	public void sendMessage(String topic, String message) {

		if(producer != null) producer.send(new KeyedMessage<String, String>(topic, message));
	}

	public void sendMessage(String topic, String key, String message){
		if(producer != null) producer.send(new KeyedMessage<String, String>(topic, key, message));
	}
}
