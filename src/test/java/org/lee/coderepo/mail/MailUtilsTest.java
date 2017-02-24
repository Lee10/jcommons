package org.lee.coderepo.mail;

import org.junit.Test;

/**
 * Created by lee on 2017/2/14.
 */
public class MailUtilsTest {

	@Test
	public void sendMailTest() {

		try {

			MailUtils.send("smtp.huitone.com", "lipeilin@huitone.com",
			               "lee211419!", "测试附件", "test",
			               "lipeilin@huitone.com", null);
		} catch (MailException e) {
			e.printStackTrace();
		}

		System.out.println("OK");

	}

}
