package org.lee.coderepo.http;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by lee on 2017/2/24.
 */
public class HttpUtilsTest {

	@Test
	public void getTest(){

		String serialNo = "18684962585";
		String text = "测试链接";
		try {
			String result = HttpUtils.doGet("http://open.hn165.com/cac-provider/sms/send", "targetNum=" + serialNo + "&content=" + text, "utf-8", true);
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
