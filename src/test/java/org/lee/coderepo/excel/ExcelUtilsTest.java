package org.lee.coderepo.excel;

import org.junit.Test;
import org.lee.coderepo.file.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/2/14.
 */
public class ExcelUtilsTest {

	@Test
	public void writeObjectTest(){

		List<Person> personList = new ArrayList<Person>();
		personList.add(new Person("lily", 21, false, new Date()));
		personList.add(new Person("tom", 22, true, new Date()));

		LinkedHashMap<String, String> titleMap = new LinkedHashMap<String, String>();
		titleMap.put("姓名", "name");
		titleMap.put("年龄", "age");
		titleMap.put("性别", "sex");
		titleMap.put("生日", "brithday");

		try {
			ExcelUtils.write("/Users/lee/Downloads/数据汇总-/test/writeObjectTest.xls", titleMap, personList);
		} catch (ExcelException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void readExcel(){

		try {
			List<Map<String, Object>> dataList = ExcelUtils.readToMap("/Users/lee/Downloads/格力开户明细1.xlsx");
			List<String> sentList = FileUtils.read("/Users/lee/Downloads/已发送.txt");

			for (Map<String, Object> dataMap : dataList) {
				dataMap.put("是否发送", sentList.contains(dataMap.get("号码"))? "已发送" : "");
			}

			ExcelUtils.write("/Users/lee/Downloads/格力开户号码发送情况.xlsx", dataList);

			System.out.println("OK");

		} catch (ExcelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
