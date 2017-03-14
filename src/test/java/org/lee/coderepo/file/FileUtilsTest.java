package org.lee.coderepo.file;

import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by lee on 2017/3/6.
 */
public class FileUtilsTest {

	@Test
	public void testMerge(){

		try {
			FileUtils.merge(null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}




	}

}
