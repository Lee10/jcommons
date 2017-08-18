package org.lee.coderepo.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/2/14.
 * json 操作类
 */
public class JsonUtils {

	private Gson gson = null;

	private JsonUtils(){
		gson = new Gson();
	}

	private JsonUtils(String format){
		gson = new GsonBuilder().setDateFormat(format).serializeNulls().create();
	}

	public static JsonUtils build(){
		return new JsonUtils();
	}

	public static JsonUtils build(String dateFormat){
		return new JsonUtils(dateFormat);
	}

	public String toJson(Object obj){
		return gson != null? gson.toJson(obj) : "";
	}

	public <T> T toBean(String json, Class<T> clazz){
		return gson != null? gson.fromJson(json, clazz) : null;
	}

	public <T> List<T> toList(String json, Class<T> clazz){
		List<T> objList = new ArrayList<T>();
		if(gson != null){
			JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
			for (JsonElement jsonElement : jsonArray) {
				objList.add(gson.fromJson(jsonElement, clazz));
			}
		}
		return objList;
	}

	public <T> List<Map<String, T>> toList(String json){
		List<Map<String, T>> mapList = new ArrayList<Map<String, T>>();
		if(gson != null){
			return gson.fromJson(json, new TypeToken<List<Map<String, T>>>(){}.getType());
		}
		return mapList;
	}

	public <T> Map<String, T> toMap(String json){
		if(gson != null){
			return gson.fromJson(json, new TypeToken<Map<String, T>>(){}.getType());
		}
		return new HashMap<String, T>();
	}

}
