package org.lee.coderepo.json;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/2/14.
 * json 操作类
 */
public class JsonUtils {

	public static String toString(Object obj) {
		if (obj == null) return "";
		if (obj instanceof List || obj instanceof Map) return JSONArray.fromObject(obj).toString();
		return JSONObject.fromObject(obj).toString();
	}

	public static String toString(Object obj, JsonConfig jsonConfig) {
		if (obj == null) return "";
		if (obj instanceof List || obj instanceof Map) return JSONArray.fromObject(obj, jsonConfig).toString();
		return JSONObject.fromObject(obj, jsonConfig).toString();
	}
	
	public static <T> T toBean(String json, Class<T> clazz) {
		
		T t = null;
		try {
			JSONObject jsonObj = JSONObject.fromObject(json);
			String[] dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss"};    // 登记时间格式，以防止JSONObject将时间解析成当前系统时间
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
			t = (T) JSONObject.toBean(jsonObj, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public static <T> T toBean(String json, Class<T> clazz, Map<String, Class<T>> classMap) {
		
		T t = null;
		try {
			JSONObject jsonObj = JSONObject.fromObject(json);
			String[] dateFormats = new String[]{"yyyy-MM-dd HH:mm:ss"};    // 登记时间格式，以防止JSONObject将时间解析成当前系统时间
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
			t = (T) JSONObject.toBean(jsonObj, clazz, classMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		//最外层解析
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			//如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				Iterator<JSONObject> it = ((JSONArray) v).iterator();
				while (it.hasNext()) {
					JSONObject json2 = it.next();
					list.add(toMap(json2.toString()));
				}
				map.put(k.toString(), list);
			} else {
				map.put(k.toString(), v);
			}
		}
		return map;
	}

}
