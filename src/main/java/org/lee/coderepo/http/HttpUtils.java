package org.lee.coderepo.http;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lee on 2017/2/24.
 */
public class HttpUtils {

	private static final int connectTimeout = 10000;
	private static final int socketTimeout = 10000;
	private static final int connectionRequestTimeout = 10000;
	private static CloseableHttpClient httpClient = null;

	public static final String JSON_CONTENT_TYPE = "application/json";

	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	/**
	 * Get 请求
	 *
	 * @param url 请求地址
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url) throws ClientProtocolException, IOException {

		HttpGet httpGet = new HttpGet(url);

		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity, "UTF-8") : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};
		return httpClient.execute(httpGet, responseHandler);
	}

	/**
	 * POST 请求
	 * @param url  请求地址
	 * @param json 请求入参，json格式
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String json) throws ClientProtocolException, IOException {
		return post(url, json, JSON_CONTENT_TYPE, null, null);
	}

	/**
	 * POST 请求
	 * @param url           请求地址
	 * @param json          请求入参
	 * @param encodeCharset 编码格式
	 * @param decodeCharset 解码格式
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String json, String encodeCharset, String decodeCharset) throws ClientProtocolException, IOException {
		return post(url, json, JSON_CONTENT_TYPE, encodeCharset, decodeCharset);
	}

	/**
	 * POST 请求
	 * @param url           请求地址
	 * @param params        请求参数
	 * @param contentType   数据类型
	 * @param encodeCharset 编码格式
	 * @param decodeCharset 解码格式
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String params, String contentType, String encodeCharset, String decodeCharset) throws ClientProtocolException, IOException {

		HttpPost httpPost = new HttpPost(url);

		encodeCharset = StringUtils.isNotEmpty(encodeCharset) ? encodeCharset : "UTF-8";
		decodeCharset = StringUtils.isNotEmpty(decodeCharset) ? decodeCharset : "UTF-8";

		StringEntity entity = new StringEntity(params, encodeCharset);
		entity.setContentEncoding(encodeCharset);
		entity.setContentType(contentType);
		httpPost.setEntity(entity);

		final String finalDecodeCharset = decodeCharset;
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity, finalDecodeCharset) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};

		return httpClient.execute(httpPost, responseHandler);
	}

	/**
	 * 下载文件并返回文件路径
	 * @param url      下载url
	 * @param fileName 保存的文件名（可以为null）
	 */
	public static String download(String url, String path, String fileName) throws IOException {
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			HttpGet httpget = new HttpGet(url);
			response = httpClient.execute(httpget);
			entity = response.getEntity();

			// 下载
			if (entity.isStreaming()) {
				String destFileName = "data";
				if (StringUtils.isNotEmpty(fileName)) {
					destFileName = fileName;
				} else if (response.containsHeader("Content-Disposition")) {
					String dstStr = response.getLastHeader("Content-Disposition").getValue();
					dstStr = new String(dstStr.getBytes(Consts.ISO_8859_1), Consts.UTF_8);
					//使用正则截取
					Pattern p = Pattern.compile("filename=\"?(.+?)\"?$");
					Matcher m = p.matcher(dstStr);
					if (m.find()) {
						destFileName = m.group(1);
					}
				} else {
					destFileName = url.substring(url.lastIndexOf("/") + 1);
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(path + destFileName);
					entity.writeTo(fos);
					return path + destFileName;
				} finally {
					fos.close();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(response != null) response.close();
			EntityUtils.consume(entity);
		}
		return "";
	}

}
