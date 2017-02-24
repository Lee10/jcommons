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

		Date currentDate = Calendar.getInstance().getTime();
		System.out.println(DateUtils.format(currentDate, DateUtils.YYYY_MM_DD));
		System.out.println(DateUtils.format(currentDate, DateUtils.YYYY_MM_DD_HH));
	}
}
