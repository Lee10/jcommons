package org.lee.coderepo.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
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
	
	/**
	 * 压缩文件为zip 格式
	 * @param inFileName
	 * @param outFileName
	 * @return
	 */
	public static boolean zip(String inFileName, String outFileName) {
		boolean flag = false;
		ZipInputStream zipInputStream = null;
		FileOutputStream out = null;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(inFileName));
			out = new FileOutputStream(outFileName);
			byte[] bt = new byte[BUFFER];
			int length = 0;
			while ((length = zipInputStream.read(bt)) > 0) {
				out.write(bt, 0, length);
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
			System.out.println(e.getMessage());
		} finally {
			try {
				zipInputStream.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 解压zip格式压缩文件
	 * @param zipFile
	 * @param descDir
	 * @throws IOException
	 */
	public static void unZipFiles(File zipFile, String descDir) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		ZipFile zip = new ZipFile(zipFile);
		for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = null;
			OutputStream out = null;
			try {
				in = zip.getInputStream(entry);
				String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
				;
				//判断路径是否存在,不存在则创建文件路径
				File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
				if (!file.exists()) {
					file.mkdirs();
				}
				//判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
				if (new File(outPath).isDirectory()) continue;
				
				out = new FileOutputStream(outPath);
				byte[] buf1 = new byte[1024];
				int len;
				while ((len = in.read(buf1)) > 0) {
					out.write(buf1, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (in != null) in.close();
				if (out != null) out.close();
			}
		}
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
