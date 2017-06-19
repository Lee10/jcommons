package org.lee.coderepo.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lee on 2017/2/21.
 */
public class DateUtils {
	
	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String YYYYMMDDHH = "yyyyMMddHH";
	public final static String YYYYMMDDHHMM = "yyyyMMddHHmm";
	public final static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM_DD_HH = "yyyy-MM-dd HH";
	public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date){
		return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
	}
	
	public static String format(Date date, String formatStr){
		return new SimpleDateFormat(formatStr).format(date);
	}
	
	public static Date formatDate(Date date, String formatStr){
		return parse(format(date, formatStr), formatStr);
	}
	
	public static Date parse(String source){
		try {
			return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parse(String source, String formatStr){
		try {
			return new SimpleDateFormat(formatStr).parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date parse(long timestamp) {
		return parse(format(new Date(timestamp)));
	}
	
	public static Date parse(long timestamp, String formatStr) {
		return parse(format(new Date(timestamp), formatStr), formatStr);
	}
}
