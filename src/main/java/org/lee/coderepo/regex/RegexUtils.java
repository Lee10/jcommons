package org.lee.coderepo.regex;

import org.lee.coderepo.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lee on 2017/5/27.
 */
public class RegexUtils {
	
	public static final String INTEGER = "^-?(([1-9]\\d*$)|0)";
	public static final String INTEGER_POSITIVE = "^[1-9]\\d*|0$";
	public static final String INTEGER_NEGATIVE = "^-[1-9]\\d*|0$";
	public static final String DOUBLE = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
	
	public static String escapeExprSpecialWord(String keyword) {
		if(StringUtils.isEmpty(keyword)) return keyword;
		String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
		for (String key : fbsArr) {
			if (keyword.contains(key)) keyword = keyword.replace(key, "\\" + key);
		}
		return keyword;
	}
	
	public static  boolean match(String str, String pattern){
		if(null == str || str.trim().length()<=0) return false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}
}
