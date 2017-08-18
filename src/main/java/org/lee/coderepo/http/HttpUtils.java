package org.lee.coderepo.http;

import org.lee.coderepo.util.StringUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lee on 2017/2/24.
 */
public class HttpUtils {

	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final int DEFAULT_CONNECT_TIMEOUNT = 30000;
	private static final int DEFAULT_READ_TIMEOUNT = 30000;

	public static String doGet(String url, Map<String, String> params) throws IOException {
		return doGet(url, params, DEFAULT_CHARSET);
	}

	public static String doGet(String url, Map<String, String> params, String charset) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;
		try {
			String ctype = "application/x-www-form-urlencoded;charset=" + charset;
			String query = buildQuery(params, charset);
			conn = getConnection(buildGetUrl(url, query), METHOD_GET, ctype, (Map) null);
			rsp = getResponseAsString(conn);
		} finally {
			if (conn != null) conn.disconnect();
		}
		return rsp;
	}

	public static String doPost(String url, Map<String, String> params) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET, DEFAULT_CONNECT_TIMEOUNT, DEFAULT_READ_TIMEOUNT);
	}

	public static String doPost(String url, Map<String, String> params, int readTimeout) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET, DEFAULT_CONNECT_TIMEOUNT, readTimeout);
	}

	public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, charset, connectTimeout, readTimeout, (Map)null);
	}

	public static String doPost(String url, Map<String, String> params, String charset, int connectTimeout, int readTimeout, Map<String, String> headerMap) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(params, charset);
		byte[] content = new byte[1024];
		if(query != null) content = query.getBytes(charset);
		return _doPost(url, ctype, content, connectTimeout, readTimeout, headerMap);
	}

	private static String _doPost(String url, String ctype, byte[] content, int connectTimeout, int readTimeout, Map<String, String> headerMap) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			conn = getConnection(new URL(url), METHOD_POST, ctype, headerMap);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			out = conn.getOutputStream();
			out.write(content);
			rsp = getResponseAsString(conn);
		} finally {
			if(out != null) out.close();
			if(conn != null) conn.disconnect();
		}
		return rsp;
	}

	private static URL buildGetUrl(String strUrl, String query) throws IOException {
		URL url = new URL(strUrl);
		if (StringUtils.isEmpty(query)) return url;
		strUrl += StringUtils.isEmpty(url.getQuery()) ?
				strUrl.endsWith("?") ? query : ("?" + query) :
				strUrl.endsWith("&") ? query : ("&" + query);
		return new URL(strUrl);
	}

	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params != null && !params.isEmpty()) {
			StringBuilder query = new StringBuilder();
			boolean hasParam = false;
			String name = null, value = null;
			Map.Entry<String, String> entry = null;
			Iterator iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				entry = (Map.Entry) iterator.next();
				name = (String) entry.getKey();
				value = (String) entry.getValue();
				if (!StringUtils.isAllEmpty(new String[]{name, value})) {
					if (hasParam) query.append("&");
					else hasParam = true;
					query.append(name).append("=").append(URLEncoder.encode(value, charset));
				}
			}
			return query.toString();
		}
		return null;
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		InputStream es = conn.getErrorStream();
		if(es == null) return getStreamAsString(conn.getInputStream(), charset);

		String msg = getStreamAsString(es, charset);
		if(StringUtils.isNotEmpty(msg)) return msg;
		else throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
	}

	private static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();
			char[] buff = new char[1024];
			int read;
			while((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}
			return response.toString();
		} finally {
			if(stream != null) stream.close();
		}
	}

	private static String getResponseCharset(String ctype) {
		String charset = "UTF-8";
		if(!StringUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			String[] arr = params;
			int len = params.length;
			for(int i = 0; i < len; ++i) {
				String param = arr[i];
				param = param.trim();
				if(param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if(pair.length == 2 && !StringUtils.isEmpty(pair[1])) {
						charset = pair[1].trim();
					}
					break;
				}
			}
		}
		return charset;
	}

	private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headerMap) throws IOException {
		HttpURLConnection conn = null;
		if ("https".equals(url.getProtocol())) {
			SSLContext ctx = null;

			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(new KeyManager[0], new TrustManager[]{new HttpUtils.DefaultTrustManager()}, new SecureRandom());
			} catch (Exception e) {
				throw new IOException(e);
			}

			HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
			connHttps.setSSLSocketFactory(ctx.getSocketFactory());
			connHttps.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			conn = connHttps;
		} else {
			conn = (HttpURLConnection) url.openConnection();
		}

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
		conn.setRequestProperty("User-Agent", "top-sdk-java");
		conn.setRequestProperty("Content-Type", ctype);
		if (headerMap != null) {
			Iterator iterator = headerMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = (Map.Entry) iterator.next();
				((HttpURLConnection) conn).setRequestProperty((String) entry.getKey(), (String) entry.getValue());
			}
		}
		return (HttpURLConnection) conn;
	}

	private static class DefaultTrustManager implements X509TrustManager {
		private DefaultTrustManager() {}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	}
}
