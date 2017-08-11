package org.lee.coderepo.ftp;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by lee on 2017/2/14.
 */
public class FtpUtils {

	private final static String encoding = "UTF-8";

	public final static boolean downloadFile(String host, String username, String password, String ftppath, String filepath){
		return downloadFile(host, "21", username, password, ftppath, filepath, false);
	}

	public final static boolean downloadFile(String host, String port, String username, String password, String ftppath, String filepath, boolean isActive){

		port = StringUtils.defaultIfEmpty(port, port);
		FTPClient client = connectServer(host, port, username, password, isActive);
		if(client != null) {
			try {
				File targetFile = new File(filepath);
				client.changeWorkingDirectory(new String(ftppath.getBytes(encoding), "iso-8859-1"));
				FTPFile[] files = client.listFiles();
				for (FTPFile file : files) {
					if(file.getName().equals(targetFile.getName())){
						OutputStream is = new FileOutputStream(targetFile);
						client.retrieveFile(file.getName(), is);
						is.close();
						client.disconnect();
						return true;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(client.isConnected()) client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public final static boolean uploadFile(String host, String username, String password, String ftppath, String filepath){
		return uploadFile(host, "21", username, password, ftppath, filepath, false);
	}

	public final static boolean uploadFile(String host, String port, String username, String password, String ftppath, String filepath, boolean isActive){

		port = StringUtils.defaultIfEmpty(port, port);
		FTPClient client = connectServer(host, port, username, password, isActive);
		if(client != null) {
			try {

				File file = new File(filepath);

				client.changeWorkingDirectory(new String(ftppath.getBytes(encoding), "iso-8859-1"));
				FileInputStream input = new FileInputStream(file);
				return client.storeFile(new String(file.getName().getBytes(encoding),"iso-8859-1"), input);
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					if(client.isConnected()) client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	private final static FTPClient connectServer(String ip, String port, String username, String password, boolean isActive) {

		FTPClient ftpClient = new FTPClient();

		try {
			int reply;
			ftpClient.connect(ip, Integer.valueOf(port));
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftpClient.login(username, password);// 登录
			// 设置文件传输类型为二进制
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			if(isActive) ftpClient.enterLocalActiveMode(); //设置为主动模式登陆
			else ftpClient.enterLocalPassiveMode(); //设置为被动模式登陆

			// 获取ftp登录应答代码
			reply = ftpClient.getReplyCode();
			// 验证是否登陆成功
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return null;
			}
			return ftpClient;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
