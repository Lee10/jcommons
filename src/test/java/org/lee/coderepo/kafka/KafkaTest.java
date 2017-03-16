package org.lee.coderepo.kafka;

import org.junit.Test;
import org.lee.coderepo.date.DateUtils;

import java.util.Calendar;
import java.util.Properties;

/**
 * Created by lee on 2017/3/8.
 */
public class KafkaTest {

	@Test
	public void test(){

		SimpleProducer producer = new SimpleProducer("172.16.120.22:9092,172.16.120.23:9092,172.16.120.24:9092");
		int index = 0;
		while(true){

			producer.sendMessage("test", "{a:\"a\",b:\"b\",i: " + index++ + "}");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void consumerTest(){
		Properties props = new Properties();
		props.put("zookeeper.connect", "172.16.120.22:2181,172.16.120.23:2181,172.16.120.24:2181");
		props.put("group.id", "test");
		props.put("zookeeper.session.timeout.ms", "4000");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		props.put("socket.timeout.ms", "3600000");
		props.put("socket.receive.buffer.bytes", "1048576");
		props.put("fetch.message.max.bytes", "1048576");
		props.put("auto.commit.enable", "true");
		props.put("queued.max.message.chunks", "10");
		props.put("rebalance.max.retries", "10");
		props.put("fetch.min.bytes", "1");
		props.put("fetch.wait.max.ms", "100");
		props.put("rebalance.backoff.ms", "2000");
		props.put("refresh.leader.backoff.ms", "200");
		props.put("auto.offset.reset", "largest");
		props.put("consumer.timeout.ms", "-1");
		props.put("zookeeper.connection.timeout.ms", "60000");

		final int[] cnt = {0};
		SimpleConsumer consumer = new SimpleConsumer(props);
		consumer.execute("test", new ConsumerCallback() {
			@Override
			public void consume(String message) {
				System.out.println(DateUtils.format(Calendar.getInstance().getTime(), DateUtils.YYYY_MM_DD_HH_MM_SS) + cnt[0]++ + "->" + message);
			}
		});
	}



}
