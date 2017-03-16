package org.lee.coderepo.file;

import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by lee on 2017/3/6.
 */
public class FileUtilsTest {

	@Test
	public void testRename(){
		System.out.println(FileUtils.rename("/Users/lee/Downloads/place_market_return.txt", "1.txt"));
	}

}
