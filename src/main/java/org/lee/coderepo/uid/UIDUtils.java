package org.lee.coderepo.uid;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by lee on 2017/2/28.
 */
public class UIDUtils {

	private final static Object locks = new Object();
	private static AtomicLong uniqueId = new AtomicLong(0);
	private static final int IDLENGHT = 24;
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");

	private UIDUtils(){}

	public static String getId(){
		return getId(IDLENGHT);
	}

	public static String getId(int size){
		size = size < 24? 24 : size;
		StringBuffer sb = new StringBuffer(size);
		synchronized (locks) {
			sb.append(dateFormat.format(Calendar.getInstance().getTime()));
			sb.append(uniqueId.incrementAndGet());
			if (sb.length() > size) {
				uniqueId = new AtomicLong(0);
				return getId(size);
			}
			sb.append(RandomStringUtils.randomNumeric(size - sb.toString().length()));
		}
		return sb.toString();
	}
}
