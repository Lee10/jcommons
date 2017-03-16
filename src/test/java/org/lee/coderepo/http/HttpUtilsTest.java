package org.lee.coderepo.http;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
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
		params.put("targetNum", "18770023786");
		params.put("sentNum", "10010");
		params.put("smsContent", "测试短信");
		params.put("tagId", "");
		params.put("channelId", "10010");
		try {
			String result = HttpUtils.post("http://172.16.120.28:8081/cac-provider/placeMarket/submit", params, "utf-8");
//			String result = HttpUtils.post("http://localhost:8082/cac-provider/placeMarket/submit", jsonObject.toString());
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}

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

}
