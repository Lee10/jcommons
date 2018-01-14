package org.lee.coderepo.web;

import org.lee.coderepo.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * web 工具类
 *
 * @author Lee
 * @date 2017/3/16
 */
public class WebUtils {

	public static String getIpAddr(HttpServletRequest request) {

		String ip = request.getHeader(WebConstant.X_REAL_IP);
		if(StringUtils.isEmpty(ip) || WebConstant.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader(WebConstant.X_FORWARDED_FOR);
		}
		if(StringUtils.isEmpty(ip) || WebConstant.UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader(WebConstant.PROXY_CLIENT_IP);
		}
		if(StringUtils.isEmpty(ip) || WebConstant.UNKNOWN.equalsIgnoreCase(ip)){
			ip = request.getHeader(WebConstant.WL_PROXY_CLIENT_IP);
		}
		if(WebConstant.LOCAL_IPV6.equalsIgnoreCase(ip)){
			ip = WebConstant.LOCAL_IPV4;
		}
		return ip;
	}
}
