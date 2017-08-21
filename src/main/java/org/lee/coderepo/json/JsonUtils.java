package org.lee.coderepo.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by 10064350 on 2017/8/21.
 */
public class JsonUtils {

	private static ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		//去掉默认的时间戳格式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		//设置为中国上海时区
		objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		//反序列化时，属性不存在的兼容处理
		objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//序列化时，日期的统一格式
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static <T> T jsonToBean(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz) {
		Map<String, T> result = new HashMap<String, T>();
		try {
			Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {});
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				result.put(entry.getKey(), mapToBean(entry.getValue(), clazz));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> List<T> jsonToList(String json, Class<T> clazz) {
		List<T> resultList = new ArrayList<T>();
		try {
			List<Map<String, Object>> list = objectMapper.readValue(json, new TypeReference<List<T>>() {
			});
			for (Map<String, Object> objMap : list) {
				resultList.add(mapToBean(objMap, clazz));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public static <T> T mapToBean(Map map, Class<T> clazz) {
		return objectMapper.convertValue(map, clazz);
	}

}
