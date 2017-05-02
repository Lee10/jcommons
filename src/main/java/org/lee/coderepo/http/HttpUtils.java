package org.lee.coderepo.http;

import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout)
		                                    .setSocketTimeout(socketTimeout).build();
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
	public static String get(String url) throws IOException {

		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpGet);
		return response.getStatusLine().getStatusCode() == 200 ? readResponse(response.getEntity().getContent()) : "";
	}

	/**
	 * GET 请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static String get(String url, Map<String, String> params) throws IOException, URISyntaxException {
		return get(url, params, "utf-8");
	}

	/**
	 * GET 请求
	 *
	 * @param url     请求地址
	 * @param params  请求参数
	 * @param charset 请求编码
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> params, String charset)
			throws URISyntaxException, IOException {

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			paramList.add(new BasicNameValuePair(key, params.get(key)));
		}
		String queryString = URLEncodedUtils.format(paramList, charset);
		return get(url + "?" + queryString);
	}

	/**
	 * POST 请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @return
	 */
	public static String post(String url, Map<String, String> params) throws IOException {
		return post(url, params, "utf-8");
	}

	/**
	 * POST 请求
	 *
	 * @param url     请求地址
	 * @param params  请求参数
	 * @param charset 请求编码
	 * @return
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> params, String charset)
			throws IOException {

		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			paramList.add(new BasicNameValuePair(key, params.get(key)));
		}

		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(paramList, charset));

		CloseableHttpResponse response = httpClient.execute(post);
		return response.getStatusLine().getStatusCode() == 200 ? readResponse(response.getEntity().getContent()) : "";
	}
	
	public static String postPayload(String url, String params) throws IOException {
		
		HttpPost post = new HttpPost(url);
		if(StringUtils.isNotEmpty(params)){
			post.setEntity(new StringEntity(params, "text/plain", "utf-8"));
		}
		CloseableHttpResponse response = httpClient.execute(post);
		return response.getStatusLine().getStatusCode() == 200 ? readResponse(response.getEntity().getContent()) : "";
	}

	/**
	 * 下载文件并返回文件路径
	 *
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) response.close();
			EntityUtils.consume(entity);
		}
		return "";
	}

	/**
	 * 读取流数据
	 * @param in 数据流
	 * @return
	 * @throws IOException
	 */
	private static String readResponse(InputStream in) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		StringBuffer lineBuffer = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			lineBuffer.append(line);
		}
		return lineBuffer.toString();
	}

}
