package org.lee.coderepo.kafka;

/**
 * Created by lipeilin on 16/10/6 19:07.
 */
public interface ConsumerCallback {

	void consume(String message);
}
