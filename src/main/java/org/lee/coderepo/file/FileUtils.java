package org.lee.coderepo.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2017/2/14.
 * file 操作类
 */
public class FileUtils {
	
	public static String fileSeparator = System.getProperty("file.separator");
	public static String lineSeparator = System.getProperty("line.separator");
	
	/***
	 * 递归获取指定目录下的所有的文件（不包括文件夹）
	 *
	 * @param obj
	 * @return
	 */
	public static ArrayList<File> getAllFiles(String dirPath) {
		File dir = new File(dirPath);
		
		ArrayList<File> files = new ArrayList<File>();
		
		if (dir.isDirectory()) {
			File[] fileArr = dir.listFiles();
			for (int i = 0; i < fileArr.length; i++) {
				File f = fileArr[i];
				if (f.isFile()) {
					files.add(f);
				} else {
					files.addAll(getAllFiles(f.getPath()));
				}
			}
		}
		return files;
	}
	
	/**
	 * 获取指定目录下的所有文件(不包括子文件夹)
	 *
	 * @param dirPath
	 * @return
	 */
	public static ArrayList<File> getDirFiles(String dirPath) {
		File path = new File(dirPath);
		File[] fileArr = path.listFiles();
		ArrayList<File> files = new ArrayList<File>();
		
		for (File f : fileArr) {
			if (f.isFile()) {
				files.add(f);
			}
		}
		return files;
	}
	
	/**
	 * 获取指定目录下特定文件后缀名的文件列表(不包括子文件夹)
	 *
	 * @param dirPath 目录路径
	 * @param suffix  文件后缀
	 * @return
	 */
	public static ArrayList<File> getDirFiles(String dirPath, final String suffix) {
		File path = new File(dirPath);
		File[] fileArr = path.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowerName = name.toLowerCase();
				String lowerSuffix = suffix.toLowerCase();
				if (lowerName.endsWith(lowerSuffix)) {
					return true;
				}
				return false;
			}
			
		});
		ArrayList<File> files = new ArrayList<File>();
		
		for (File f : fileArr) {
			if (f.isFile()) {
				files.add(f);
			}
		}
		return files;
	}
	
	/**
	 * 以默认编码格式(utf-8)读取指定文件中的内容
	 *
	 * @param fileName 文件名称
	 * @return
	 * @throws Exception
	 */
	public static List<String> read(String fileName) throws Exception {
		return read(fileName, "utf-8");
	}
	
	/**
	 * 读取指定文件中的内容
	 *
	 * @param fileName 文件名称
	 * @param encode   编码格式
	 * @return
	 * @throws Exception
	 */
	public static List<String> read(String fileName, String encode) throws Exception {
		return read(new FileInputStream(fileName), encode);
	}
	
	/**
	 * 读取指定文件中的内容
	 *
	 * @param file   文件对象
	 * @param encode 编码格式
	 * @return
	 * @throws Exception
	 */
	public static List<String> read(File file, String encode) throws Exception {
		return read(new FileInputStream(file), encode);
	}
	
	/**
	 * 读取数据流中的内容
	 *
	 * @param in     数据流
	 * @param encode 编码格式
	 * @return
	 * @throws IOException
	 */
	public static List<String> read(InputStream in, String encode) throws IOException {
		List<String> list = new ArrayList<String>();
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, encode));
		try {
			while (bufferedReader.ready()) {
				list.add(bufferedReader.readLine());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			bufferedReader.close();
		}
		return list;
	}
	
	/**
	 * 读取文件为字节数组
	 * @param fileName 文件路径
	 * @return
	 */
	public static byte[] readToByte(String fileName) {
		
		FileInputStream in = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		
		byte[] temp = new byte[1024];
		int size = 0;
		
		try {
			in = new FileInputStream(fileName);
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
		}catch (IOException e){
			e.printStackTrace();
		}finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return out.toByteArray();
	}
	
	/**
	 * 将数据dataList以追加的方式写入指定filename中
	 *
	 * @param fileName 文件名称
	 * @param dataList 数据集合
	 * @return
	 * @throws IOException
	 */
	public static boolean write(String fileName, List<String> dataList) throws IOException {
		return write(fileName, dataList, true);
	}
	
	/**
	 * 将数据dataList写入指定filename中, 并可选择是否追加
	 *
	 * @param fileName 文件路径
	 * @param dataList 数据集合
	 * @param isCover  是否追加， true: 在文件末尾追加数据，false:从头开始覆盖文本
	 * @return
	 * @throws IOException
	 */
	public static boolean write(String fileName, List<String> dataList, boolean isAdd) throws IOException {
		boolean result = false;
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, isAdd);
			for (String str : dataList) {
				writer.write(str + "\r\n");
			}
			result = true;
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (writer != null) writer.close();
		}
		return result;
	}
	
	/**
	 * 在文件末尾追加一行文本 text
	 *
	 * @param fileName 文件路径
	 * @param text     文本
	 * @return
	 * @throws IOException
	 */
	public static boolean append(String fileName, String text) throws IOException {
		boolean result = false;
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, true);
			writer.write(text + "\r\n");
			result = true;
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (writer != null) writer.close();
		}
		return result;
	}
	
	/**
	 * 将byte[]写入指定filename中, 并可选择是否追加
	 * @param fileName 文件路径
	 * @param text 字节数组
	 * @return
	 */
	public static boolean write(String fileName, byte[] text) {
		boolean result = false;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(text);
			result = true;
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/**
	 * 修改文件名
	 *
	 * @param fileName    文件路径
	 * @param newFileName 新文件名
	 * @return
	 */
	public static boolean rename(String fileName, String newFileName) {
		File file = new File(fileName);
		return file.exists() ? file.renameTo(new File(file.getParent() + fileSeparator + newFileName)) : false;
	}
	
	/**
	 * 获取文件的编码格式
	 *
	 * @param filePath 文件绝对路径
	 * @return String 文件编码
	 */
	public static String getFileCharset(String filePath) {
		
		File file = new File(filePath);
		
		if (!file.exists()) {
			System.out.println("File not found.");
		}
		// 默认编码格式为GBK
		String charset = "GBK";
		
		FileInputStream is = null;
		BufferedInputStream bis = null;
		
		try {
			byte[] first3Bytes = new byte[3];
			
			boolean checked = false;
			
			is = new FileInputStream(file);
			
			bis = new BufferedInputStream(is);
			
			bis.mark(0);
			
			int read = bis.read(first3Bytes, 0, 3);
			
			if (-1 == read) {
				charset = "GBK";
			} else if (first3Bytes[0] == (byte) 0xFF
					&& first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE
					&& first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE";
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF
					&& first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8";
				checked = true;
			}
			bis.reset();
			
			if (!checked) {
				int loc = 0;
				while ((read = bis.read()) != -1) {
					
					loc++;
					if (read >= 0xF0) break;
					if (0x80 <= read && read <= 0xBF) break;
					if (0x80 <= read && read <= 0xDF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) continue;
						else break;
					} else if (0xE0 <= read && read <= 0xEF) {
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else {
								break;
							}
						} else {
							break;
						}
					}
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) bis.close();
				if (is != null) is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return charset;
	}
}
