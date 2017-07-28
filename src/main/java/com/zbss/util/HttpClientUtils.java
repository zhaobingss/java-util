package com.zbss.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.SSLContext;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * @author zbss
 * @desc Http工具类 注意：方法返回的数据对GZIP已经进行了解压缩
 * @date 2016-4-23 下午2:39:31
 */
public class HttpClientUtils {

	private static final String GET = "GET";
	private static final String POST = "POST";
	private static final Boolean HTTP = false;
	private static final Boolean HTTPS = true;
	
	/**
	 * HTTP GET 请求
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param timeOut 超时（毫秒）
	 * @return
	 * @throws Exception
	 */
	public static Result httpGet(String url, Map<String, Object> headers, Integer timeOut) 
			throws Exception{
		return get(HTTP, url, headers, timeOut);
	}
	
	/**
	 * HTTPS GET 请求
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param timeOut 超时（毫秒）
	 * @return
	 * @throws Exception
	 */
	public static Result httpsGet(String url, Map<String, Object> headers, Integer timeOut) 
			throws Exception{
		return get(HTTPS, url, headers, timeOut);
	}
	
	/**
	 * HTTP　POST 请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static Result httpPost(String url, String param, Map<String, Object> headers, Integer timeOut)
			throws Exception {
		return post(HTTP, url, param, headers, timeOut);
	}
	
	/**
	 * HTTPS　POST 请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public static Result httpsPost(String url, String param, Map<String, Object> headers, Integer timeOut)
			throws Exception {
		return post(HTTPS, url, param, headers, timeOut);
	}

	/**
	 * 上传文件
	 * @param serverUrl 要上传的地址
	 * @param headers 请求头
	 * @param boundary 必须的boundary
	 * @param fieldItems form表单里正常的数据
	 * @param fileItems form表单里的文件
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(String serverUrl, Map<String, String> headers, String boundary,
			List<FormFieldItem> fieldItems, List<FormFileItem> fileItems) throws Exception{
		// 向服务器发送post请求
		URL url = new URL(serverUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(20000);
		conn.setReadTimeout(20000);
		
		//设置请求头
		setHeader(conn, headers);
	
		StringBuffer contentBody = new StringBuffer();// 请求体
		contentBody.append(boundary);
		OutputStream out = conn.getOutputStream();
	
		// 1. 处理文字形式的POST请求
		for (FormFieldItem fieldItem : fieldItems){
			contentBody.append("\r\n")
					.append("Content-Disposition: form-data; name=\"")
					.append(fieldItem.getKey() + "\"")
					.append("\r\n")
					.append("\r\n")
					.append(fieldItem.getValue())
					.append("\r\n")
					.append(boundary);
		}
	
		out.write(contentBody.toString().getBytes("utf-8"));
	
		// 2. 处理文件上传
		for (FormFileItem fileItem : fileItems){
			contentBody = new StringBuffer();
			contentBody.append("\r\n")
					.append("Content-Disposition:form-data; name=\"")
					.append(fileItem.getFormFiledName() + "\"; ") // form中field的名称
					.append("filename=\"")
					.append(fileItem.getFileName() + "\"") 		// 上传文件的文件名，包括目录
					.append("\r\n")
					.append("Content-Type:application/octet-stream")
					.append("\r\n\r\n");
	
			out.write(contentBody.toString().getBytes("utf-8"));
	
			// 开始真正向服务器写文件
			File file = fileItem.getFile();
			DataInputStream dis = new DataInputStream(new FileInputStream(file));
	
			int bytes = 0;
			byte[] bufferOut = new byte[(int) file.length()];
			bytes = dis.read(bufferOut);
			out.write(bufferOut, 0, bytes);
	
			dis.close();
			contentBody.append(boundary);
			out.write(contentBody.toString().getBytes("utf-8"));
		}
	
		out.write((boundary+"--\r\n").getBytes("utf-8"));
	
		// 3. 写结尾
		String endBoundary = "\r\n" + boundary + "--\r\n";
		out.write(endBoundary.getBytes("utf-8"));
		out.flush();
		out.close();
	
		// 4. 从服务器获得回答的内容
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		if (encoding != null && encoding.contains("gzip")){// 处理gzip压缩的内容
			in = new GZIPInputStream(in);
		}
	
		StringBuffer sb = new StringBuffer();
		String strLine = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
		while ((strLine = reader.readLine()) != null){
			sb.append(strLine+"\n");
		}
		
		return sb.toString();
	}

	/**
	 * GET 请求
	 * @param isHttps 是否为https请求
	 * @param url 请求地址
	 * @param headers 请求头
	 * @param timeOut 超时（毫秒）
	 * @return
	 * @throws Exception
	 */
	private static Result get(boolean isHttps, String url, Map<String, Object> headers, Integer timeOut)
			throws Exception {
		HttpRequestBase request = getRequest(url, null, timeOut, headers, GET);
		return http(isHttps, request);
	}

	/**
	 * POST 请求
	 * @param isHttps 是否为https请求
	 * @param url 请求地址
	 * @param param 请求参数
	 * @param headers 请求头
	 * @param timeOut 超时（毫秒）
	 * @return
	 * @throws Exception
	 */
	private static Result post(boolean isHttps, String url, String param, Map<String, Object> headers, Integer timeOut)
			throws Exception {
		HttpRequestBase request = getRequest(url, param, timeOut, headers, POST);
		return http(isHttps, request);
	}

	/**
	 * 发送请求
	 * @param isHttps
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static Result http(boolean isHttps, HttpRequestBase request) throws Exception {
		Result result = new Result();

		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		CookieStore cookieStore = new BasicCookieStore();

		httpClient = createClient(isHttps, cookieStore);
		response = httpClient.execute(request);

		List<Cookie> cookieList = cookieStore.getCookies();
		Header[] responseHeaders = response.getAllHeaders();

		// cookie
		StringBuffer cookie = new StringBuffer();
		if (cookieList != null && cookieList.size() > 0) {
			for (Cookie co : cookieList) {
				cookie.append(co.getName()).append("=").append(co.getValue()).append(";");
			}
		}
		result.setCookies(cookie.toString());

		// 响应头
		Map<String, Object> resHeaderMap = new HashMap<>();
		if (null != responseHeaders && responseHeaders.length > 1) {
			for (Header header : responseHeaders) {
				resHeaderMap.put(header.getName(), header.getValue());
			}
			resHeaderMap.put("Cookie", cookie.toString());
		}
		result.setResponseHeaders(resHeaderMap);

		// 响应体
		HttpEntity entity = response.getEntity();
		String body = "";
		if (entity != null) {
			body = EntityUtils.toString(entity);
		} else {
			body = response.getStatusLine().toString();
		}
		result.setBody(body);

		EntityUtils.consume(entity);

		if (response != null)
			response.close();
		if (httpClient != null)
			httpClient.close();

		return result;
	}

	/**
	 * 设置请求内容
	 * @param url 请求地址
	 * @param param 请求参数
	 * @param timeOut 超时时间（毫秒）
	 * @param headers 请求头
	 * @param type 请求方式，GET 或者 POST
	 * @return 返回设置好的请求内容
	 */
	private static HttpRequestBase getRequest(String url, String param, Integer timeOut, Map<String, Object> headers,
			String type) {
		HttpRequestBase request = null;
		if (GET.equals(type)) {
			request = new HttpGet(url);
		}

		if (POST.equals(type)) {
			request = new HttpPost(url);
		}

		// 超时、cookie设置
		if (timeOut != null) {
			RequestConfig requestConfig = RequestConfig.custom() //
					.setSocketTimeout(timeOut) //
					.setConnectTimeout(timeOut) //
					.setCookieSpec(CookieSpecs.STANDARD_STRICT) //
					.build();
			request.setConfig(requestConfig);
		}

		// 请求头设置
		if (headers != null) {
			for (Entry<String, Object> entry : headers.entrySet()) {
				request.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}

		if (param != null && POST.equals(type)) {
			((HttpPost) request).setEntity(new StringEntity(param, "utf-8"));
		}
		return request;
	}

	/**
	 * 创建httpClient
	 * @param isHttps 是否为https请求
	 * @return
	 * @throws Exception
	 */
	private static CloseableHttpClient createClient(boolean isHttps, CookieStore cookieStore) throws Exception {
		if (isHttps)
			return createHttpsClient(cookieStore);
		return createHttpClient(cookieStore);
	}

	/**
	 * 创建http客户端
	 * @return
	 */
	private static CloseableHttpClient createHttpClient(CookieStore cookieStore) {
		return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * 创建https客户端
	 * @return
	 * @throws Exception
	 */
	private static CloseableHttpClient createHttpsClient(CookieStore cookieStore) throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).build();
	}
	
	
	/**
	 * 设置请求头
	 * @param conn
	 * @param headers
	 */
	private static void setHeader(HttpURLConnection conn, Map<String, String> headers){
		if (null != headers && !headers.isEmpty()){
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
			}
		}
	}
	
	/**
	 * 文件表单类
	 * @author zhaobing
	 * @Date 2017年3月31日 下午12:55:30
	 */
	class FormFileItem{
		private String formFiledName;
		private File file;

		public FormFileItem(File file, String formFiledName){
			this.file = file;
			this.formFiledName = formFiledName;
		}
		public String getFormFiledName() {
			return formFiledName;
		}
		public void setFormFiledName(String formFiledName) {
			this.formFiledName = formFiledName;
		}
		public File getFile() {
			return file;
		}
		public void setFile(File file) {
			this.file = file;
		}
		public String getFileName(){
			return file.getName();
		}
	}
	
	/**
	 * 普通表单
	 * @author zhaobing
	 * @Date 2017年3月31日 下午12:54:54
	 */
	class FormFieldItem{
		private String key;
		private String value;
		public FormFieldItem(String key, String value){
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * 请求返回结果类
	 * @author zhaobing
	 */
	public static class Result {
		private String body;
		private String cookies;
		private Map<String, Object> responseHeaders;

		public String getBody() {
			return body;
		}
		public String getBody(String charset) throws Exception {
			return new String(body.getBytes(charset));
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getCookies() {
			return cookies;
		}
		public void setCookies(String cookies) {
			this.cookies = cookies;
		}
		public Map<String, Object> getResponseHeaders() {
			return responseHeaders;
		}
		public void setResponseHeaders(Map<String, Object> responseHeaders) {
			this.responseHeaders = responseHeaders;
		}
	}
	
	/**
	 * 不允许实例化
	 */
	private HttpClientUtils(){
		
	}

	public static void main(String[] args) throws Exception {
		Result r = HttpClientUtils.httpGet("https://www.baidu.com", null, 20000);
		System.out.println(r.getBody("iso-8859-1"));
	}
}