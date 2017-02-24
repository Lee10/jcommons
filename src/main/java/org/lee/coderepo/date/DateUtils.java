package org.lee.coderepo.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lee on 2017/2/21.
 */
public class DateUtils {

	public final static SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");
	public final static SimpleDateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");
	public final static SimpleDateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static SimpleDateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat YYYY_MM_DD_HH = new SimpleDateFormat("yyyy-MM-dd HH");
	public final static SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public final static SimpleDateFormat YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String format(Date date){
		return YYYY_MM_DD_HH_MM_SS.format(date);
	}

	public static String format(Date date, SimpleDateFormat format){
		return format.format(date);
	}

	public static Date formatDate(Date date, SimpleDateFormat format){
		return parse(format(date, format), format);
	}

	public static Date parse(String source){
		try {
			return YYYY_MM_DD_HH_MM_SS.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parse(String source, SimpleDateFormat format){
		try {
			return format.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
