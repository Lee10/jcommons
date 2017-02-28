package org.lee.coderepo.json;

import net.sf.json.JsonConfig;
import org.junit.Test;
import org.lee.coderepo.excel.Person;

import java.util.Date;

/**
 * Created by lee on 2017/2/28.
 */
public class JsonUtilsTest {

	@Test
	public void dateToString(){

		Person person = new Person("lily", 21, false, new Date());

		//未使用时间格式化
		//{"age":21,"brithday":{"date":28,"day":2,"hours":17,"minutes":36,"month":1,"seconds":55,"time":1488274615900,"timezoneOffset":-480,"year":117},"name":"lily","sex":false}
		System.out.println(JsonUtils.toString(person));

		//使用时间格式化
		//{"age":21,"brithday":"2017-02-28 17:36:55","name":"lily","sex":false}
		DateJsonValueProcessor processor = new DateJsonValueProcessor("yyyy-MM-dd HH:mm:ss");
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Date.class, processor);
		System.out.println(JsonUtils.toString(person, config));
	}

}
