package org.lee.coderepo.csv;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2017/2/17.
 */
public class CsvUtilsTest {

	@Test
	public void readTest(){
		
		List<Map<String, Object>> dataList = CsvUtils.readToMap("/Users/lee/Downloads/test.csv", "utf-8");
		
		for (Map<String, Object> dataMap : dataList) {
			System.out.println(dataMap.toString());
		}
	}

	@Test
	public void writeTest(){

		List<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"100000","","No100000","","公司新闻","2016/8/23 17:12:09"});
		list.add(new String[]{"100001","","No100001","","热点资讯","2016/8/24 17:12:36"});
		list.add(new String[]{"100046","100001","No100046","1","银行动态","2016/8/1 10:36:31"});
		list.add(new String[]{"100052","100001","No100052","2","法律法规","2016/8/2 20:39:10"});

		CsvUtils.write("/Users/lee/Downloads/test_write.csv", "gbk", list);
	}

	@Test
	public void writeTest2(){

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> tmpMap = new HashMap<String, Object>();
		tmpMap.put("姓名", "李培林");
		tmpMap.put("手机", "18770023786");
		list.add(tmpMap);

		CsvUtils.writeMap("/Users/lee/Downloads/test_write.csv", "gbk", list);
	}

}
