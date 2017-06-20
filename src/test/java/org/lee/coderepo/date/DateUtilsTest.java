package org.lee.coderepo.date;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lee on 2017/2/21.
 */
public class DateUtilsTest {

	@Test
	public void formatTest(){
		
		Date date = DateUtils.parse("20170526150659", DateUtils.YYYYMMDDHHMMSS);
		System.out.println(date);
	}
}
