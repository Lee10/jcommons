package org.lee.coderepo.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by lee on 2017/2/14.
 */
public class GZipUtils {

	public static final int BUFFER = 2048;

	/**
	 * 压缩文件
	 * @param infileName 被压缩文件路径名
	 * @param outfileName 压缩后文件路径名
	 * @return
	 */
	public static boolean compFile(String infileName,String outfileName) {
		boolean flag = false;
		GZIPOutputStream gzip = null;
		FileInputStream in = null;
		try {
			gzip = new GZIPOutputStream(new FileOutputStream(outfileName));
			in = new FileInputStream(infileName);
			byte[] bt = new byte[BUFFER];
			int length = 0;
			while((length=in.read(bt))>0){
				gzip.write(bt, 0, length);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}finally {
			try {
				in.close();
				gzip.flush();
				gzip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 解压缩文件
	 * @param infileName 被压缩文件路径名
	 * @param outfileName 压缩后文件路径名
	 * @return
	 */
	public static boolean decompFile(String infileName,String outfileName){
		boolean flag = false;
		GZIPInputStream gzip = null;
		FileOutputStream out = null;
		try {
			gzip = new GZIPInputStream(new FileInputStream(infileName));
			out = new FileOutputStream(outfileName);
			byte[] bt = new byte[BUFFER];
			int length = 0;
			while((length=gzip.read(bt))>0){
				out.write(bt, 0, length);
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}finally {
			try {
				gzip.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 压缩文件夹
	 * @param fileName 被压缩路径名
	 * @param outfileName 压缩后文件路径名
	 * @return
	 */
	public static boolean compFolder(String fileName, String outfileName) {
		boolean result = false;
		File f = new File(fileName);
		ZipOutputStream zip = null;
		try {
			zip = new ZipOutputStream(new FileOutputStream(outfileName));
			result = put(f, zip, "");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				zip.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private static boolean put(File f, ZipOutputStream out, String dir) {
		boolean result = false;
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			dir = (dir.length() == 0 ? "" : dir + "/") + f.getName();
			for (int i = 0; i < files.length; i++) {

				result = put(files[i], out, dir);
				//如果失败,返回失败信息
				if (!result) {
					return result;
				}
			}
		} else {
			byte[] data = new byte[BUFFER];
			FileInputStream fi = null;
			BufferedInputStream temp = null;
			try {
				fi = new FileInputStream(f);
				temp = new BufferedInputStream(fi, BUFFER);
				dir = (dir.length() == 0 ? "" : dir + "/") + f.getName();

				ZipEntry entry = new ZipEntry(dir);
				out.putNextEntry(entry);
				int count;
				while ((count = temp.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				out.flush();
				result = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					temp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
