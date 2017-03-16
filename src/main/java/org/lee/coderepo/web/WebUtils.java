package org.lee.coderepo.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by lee on 2017/3/16.
 * web 工具类
 */
public class WebUtils {

	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();
		if ("0:0:0:0:0:0:0:1".equals(ip)) ip = "127.0.0.1";
		return ip;
	}
}
