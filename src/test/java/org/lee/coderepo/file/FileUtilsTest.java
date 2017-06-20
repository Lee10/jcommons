package org.lee.coderepo.file;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lee on 2017/3/6.
 */
public class FileUtilsTest {

	@Test
	public void testRename(){
		System.out.println(FileUtils.rename("/Users/lee/Downloads/place_market_return.txt", "1.txt"));
	}

	@Test
	public void test(){

		byte[] byteArr = FileUtils.readToByte("/Users/lee/Downloads/新短信群发平台测试.xlsx");
		System.out.println(byteArr.toString());
		
		FileUtils.write("/Users/lee/Downloads/新短信群发平台测试.xls", byteArr);
	}

}
