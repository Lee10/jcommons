package org.lee.coderepo.util;

/**
 * Created by 10064350 on 2017/8/18.
 * 字符串操作工具类
 */
public class StringUtils {

	public static boolean isEmpty(CharSequence cs){
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(CharSequence cs){
		return !isEmpty(cs);
	}

	public static boolean isAllEmpty(CharSequence... css){
		if(css == null || css.length == 0) return true;
		int len = css.length;
		for(int i = 0; i < len; ++i) {
			CharSequence cs = css[i];
			if(isNotEmpty(cs)) {
				return false;
			}
		}
		return true;
	}

	public static boolean endsWith(CharSequence str, CharSequence suffix){
		if(isNotEmpty(str) && isNotEmpty(suffix)){
			if(str.length() < suffix.length()) return false;
			else {
				CharSequence endStr = str.subSequence(str.length() - suffix.length(), str.length());
				int len = suffix.length();
				for (int i = 0; i < len; i++) {
					if(endStr.charAt(i) != suffix.charAt(i)) return false;
				}
				return true;
			}
		}else return isEmpty(str) && isEmpty(suffix);
	}
}
