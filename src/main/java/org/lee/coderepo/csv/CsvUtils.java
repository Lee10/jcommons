package org.lee.coderepo.csv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lee on 2017/2/17.
 * csv 操作类
 */
public class CsvUtils {

	/**
	 * 读取csv 文件数据，并转换为map集合
	 * @param filepath 文件路径
	 * @param encode 文件编码
	 * @return
	 * @throws IOException
	 */
	public final static List<Map<String, Object>> readToMap(String filepath, String encode) {

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		CsvReader reader = null;
		try {
			reader = new CsvReader(filepath, ',', Charset.forName(encode));
			reader.readHeaders();
			String[] headers = reader.getHeaders();
			
			while (reader.readRecord()) {
				
				Map<String, Object> tmpMap = new LinkedHashMap<String, Object>();
				String[] values = reader.getValues();
				for (int i = 0; i < headers.length; i++) {
					if (StringUtils.isNotEmpty(headers[i]) && !isAllEmpty(values)) {
						tmpMap.put(headers[i], values[i]);
					}
				}
				if (!tmpMap.isEmpty()) resultList.add(tmpMap);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if(reader != null) reader.close();
		}
		return resultList;
	}

	public final static boolean writeMap(String filepath, String encode, List<Map<String, Object>> dataList){

		int index = 0;
		String[] tmpArr = null;
		List<String[]> tmpList = new ArrayList<String[]>();
		for (Map<String, Object> dataMap : dataList) {
			if(tmpList != null && !tmpList.isEmpty()){
				tmpArr = new String[dataMap.size()];
				for (String key : dataMap.keySet()){
					tmpArr[index++] = key;
				}
				tmpList.add(tmpArr);
			}
			index = 0;
			tmpArr = new String[dataMap.size()];
			for (String key : dataMap.keySet()){
				tmpArr[index++] = String.valueOf(dataMap.get(key));
			}
			tmpList.add(tmpArr);
		}
		return tmpList != null && !tmpList.isEmpty()? write(filepath, encode, tmpList) : false;
	}

	public final static boolean write(String filepath, String encode, List<String[]> dataList){
		CsvWriter writer = null;
		try {
			writer = new CsvWriter(filepath, ',', Charset.forName(encode));
			for (String[] strings : dataList) {
				writer.writeRecord(strings);
			}
			writer.flush();
			return true;
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(writer != null) writer.close();
		}
		return false;
	}

	private final static boolean isAllEmpty(String[] strs){
		Set<String> strSet = new HashSet<String>(Arrays.asList(strs));
		if(strSet.size() < 1) return true;
		else if(strSet.size() == 1 && StringUtils.isEmpty(new ArrayList<String>(strSet).get(0))) return true;
		else return false;
	}

}
