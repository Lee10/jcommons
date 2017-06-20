package org.lee.coderepo.http;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lee on 2017/2/24.
 */
public class HttpUtilsTest {

	@Test
	public void testPost(){


		Map<String, String> params = new HashMap<String, String>();
		params.put("targetNum", "18684962585");
		params.put("sentNum", "10010");
		params.put("smsContent", "测试短信");
		params.put("tagId", "");
		params.put("channelId", "10010");
		try {
//			String result = HttpUtils.post("http://172.16.120.28:8081/cac-provider/placeMarket/submit", params, "utf-8");
			String result = HttpUtils.post("http://172.16.120.17:8081/cac-provider/placeMarket/submit", params, "utf-8");
//			String result = HttpUtils.post("http://localhost:8082/cac-provider/placeMarket/submit", jsonObject.toString());
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void testPostPayload(){
		
		String url = "http://157.122.53.228:60021/api/userinfo?token=88D144E16004326D760E4F5812B653E28F8F476FFB6CA975EEA269416E2D14919EA49488B07D1634291F257E091374A2&lang=zh_CN";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("attendanceCard", "999");
		jsonObject.put("email", "lzw@huitone.com");
		jsonObject.put("employeeId", "999");
		jsonObject.put("employeeName", "999");
		jsonObject.put("orgId", "6");
		jsonObject.put("roleId", "3");
		
		String result = null;
		try {
			result = HttpUtils.postPayload(url, jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("result->" + result);
		
	}

	@Test
	public void testGet1(){

		try {
			String result = HttpUtils.get("http://localhost:8080/lweb/user/list?name=lee");
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGet2(){

		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("name", "lee");
			params.put("mail", "lipeilin211@163.com");

			String result = HttpUtils.get("http://localhost:8080/lweb/user/list", params, "utf-8");

			System.out.println(result);
		}catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetIp(){
	
		String str = "2006053383";
		long l = Long.parseLong(str);
		String l2 = Long.toBinaryString(l);//获取2006053383 的二进制数字
		System.out.println(l2);
		if(l2.length() < 32){
			String t = "00000000" + l2;
			l2 = t.substring(t.length() - 32, t.length());
		}
		System.out.println(l2);
		
		BigInteger ip1 = new BigInteger(l2.substring(0, 8), 2);// 截取前八位并转成十进制
		BigInteger ip2 = new BigInteger(l2.substring(8, 16), 2);
		BigInteger ip3 = new BigInteger(l2.substring(16, 24), 2);
		BigInteger ip4 = new BigInteger(l2.substring(24, 32), 2);
		String ip = ip1 + "." + ip2 + "." + ip3 + "." + ip4;
		System.out.println(ip);
	}
	
	@Test
	public void testInteger(){
		String str = "00111011";
		BigInteger l = new BigInteger(str, 2);
		System.out.println(l);
	}

}
