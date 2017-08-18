package com.lee.coderepo.json;

import org.junit.Test;
import org.lee.coderepo.json.JsonUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10064350 on 2017/8/18.
 */
public class JsonUtilsTest {

	@Test
	public void testToJson(){

		Person person = new Person();
		person.setName("Lee10");
		person.setAge(24);
		person.setBrithday(new Date());

		person.setWift(new Person("lee", 22));

		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("aaa", 12));
		personList.add(new Person("bbb", 23));

		Map<String, Person> personMap = new HashMap<String, Person>();
		personMap.put("1", new Person("aaa", 12));
		personMap.put("2", new Person("aadfs", 12));
		personMap.put("3", new Person("aaafaa", 12));


		List<Map<String, Person>> list2 = new ArrayList<Map<String, Person>>();
		list2.add(personMap);

		person.setFamily(personList);

		System.out.println(JsonUtils.build("yyyy-mm-dd HH:mm:ss").toJson(person));
	}

	@Test
	public void testToBean(){

		String json = "{\"name\":\"李培林\",\"age\":24,\"wift\":{\"name\":\"lee\",\"age\":22},\"family\":[{\"name\":\"aaa\",\"age\":12},{\"name\":\"bbb\",\"age\":23}]}";
		Person person = JsonUtils.build().toBean(json, Person.class);
		System.out.println(person.getName());
	}

	@Test
	public void testToList(){

//		String json = "[{\"name\":\"aaa\"},{\"name\":\"bbb\",\"age\":23}]";
		String json = "[{\"1\":{\"name\":\"aaa\",\"age\":12},\"2\":{\"name\":\"aadfs\",\"age\":12},\"3\":{\"name\":\"aaafaa\",\"age\":12}}]";
		List<Map<String, Person>> personList = JsonUtils.build().toList(json);
		for (Map<String, Person> map : personList) {
			for (Map.Entry<String, Person> entry : map.entrySet()) {
				System.out.println(entry.getKey() + "->" + JsonUtils.build().toJson(entry.getValue()));
			}
		}
	}

	@Test
	public void testToMap(){

		String json = "{\"1\":{\"name\":\"aaa\",\"age\":12},\"2\":{\"name\":\"aadfs\",\"age\":12},\"3\":{\"name\":\"aaafaa\",\"age\":12}}";
		Map<String, Person> map = JsonUtils.build().toMap(json);
		for (Map.Entry<String, Person> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "->" + JsonUtils.build().toJson(entry.getValue()));
		}
	}
}
