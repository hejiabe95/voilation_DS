package com.zc.intf.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zc.intf.constant.Constant;



public class CoreHttpUtils {

	private static final Logger logger = LoggerFactory.getLogger(CoreHttpUtils.class);

	public static final int DEFAULT_TIMEOUT = 5000;

	public static String get(String requestUrl, Object body)  {
		try {
			return CoreHttpUtils.get(requestUrl, body, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public static String get(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.get(requestUrl, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String get(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String get(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String get(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "GET");
	}

	public static String post(String requestUrl, Object body) {
		try {
			return CoreHttpUtils.post(requestUrl, body, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String post(String requestUrl, Object body, String contentType) {
		try {
			return CoreHttpUtils.post(requestUrl, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String post(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "POST");
	}

	public static String post(String requestUrl, Map<String, String> headerMap, Object body, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, Constant.CHARSET_UTF8, timeout, "POST");
	}

	public static String put(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.put(requestUrl, body, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String put(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.put(requestUrl, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String put(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String put(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String put(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "PUT");
	}

	public static String delete(String requestUrl, Object body) throws IOException {
		return CoreHttpUtils.delete(requestUrl, body, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String delete(String requestUrl, Object body, String contentType) throws IOException {
		return CoreHttpUtils.delete(requestUrl, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT);
	}

	public static String delete(String requestUrl, Object body, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}

	public static String delete(String requestUrl, Object body, String contentType, String encoding, int timeout) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, null, body, contentType, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}

	public static String delete(String requestUrl, Map<String, String> headerMap, Object body) throws IOException {
		return CoreHttpUtils.callHttp(requestUrl, headerMap, body, null, Constant.CHARSET_UTF8, DEFAULT_TIMEOUT, "DELETE");
	}


	public static String callHttp(String requestUrl, Map<String, String> headerMap, Object body, String contentTypeString, String encoding, int timeout,
			String method) throws IOException {
		Authenticator.setDefault(new MyAuthenticator());
		String result = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			// Https访问方式时，需要增加SSL相关设置
			if (requestUrl.startsWith("https://")) {
				SSLContext sslContext = null;
				try {
					sslContext = SSLContext.getInstance("TLS"); // 或SSL
					X509TrustManager[] xtmArray = new X509TrustManager[] { new MyX509TrustManager() };
					sslContext.init(null, xtmArray, new java.security.SecureRandom());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
				if (sslContext != null) {
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				}
				HttpsURLConnection.setDefaultHostnameVerifier(new myHostnameVerifier());
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式
			conn.setRequestMethod(method);
			// 设置超时时间
			conn.setReadTimeout(timeout);
			// 设置请求头部
			if (contentTypeString != null && !contentTypeString.isEmpty()) {
				conn.setRequestProperty("Content-Type", contentTypeString);
			} else {
				conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			}
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			conn.connect();
			if (null != body) {
				String outputStr = null;
				if (body instanceof String) {
					outputStr = (String) body;
				} else {
					outputStr = JsonUtil.toJson(body);
				}
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes(encoding));
				outputStream.flush();
				outputStream.close();
			}
			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, encoding);
			bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			result = buffer.toString();

		} catch (IOException ex) {
			throw ex;
		} finally {
			// 释放资源
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				conn.disconnect();
			} catch (Exception e) {

			}
		}
		return result;
	}
	
	public static byte[] getInputStream(String requestUrl, Map<String, String> headerMap, Object body, String contentTypeString, String encoding, int timeout,
			String method) throws IOException {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			// Https访问方式时，需要增加SSL相关设置
			if (requestUrl.startsWith("https://")) {
				SSLContext sslContext = null;
				try {
					sslContext = SSLContext.getInstance("TLS"); // 或SSL
					X509TrustManager[] xtmArray = new X509TrustManager[] { new MyX509TrustManager() };
					sslContext.init(null, xtmArray, new java.security.SecureRandom());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
				if (sslContext != null) {
					HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
				}
				HttpsURLConnection.setDefaultHostnameVerifier(new myHostnameVerifier());
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式
			conn.setRequestMethod(method);
			// 设置超时时间
			conn.setReadTimeout(timeout);
			// 设置请求头部
			if (contentTypeString != null && !contentTypeString.isEmpty()) {
				conn.setRequestProperty("Content-Type", contentTypeString);
			} else {
				conn.setRequestProperty("Content-Type", "application/json");
			}
			if (headerMap != null) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					conn.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			conn.connect();
			if (null != body) {
				String outputStr = null;
				if (body instanceof String) {
					outputStr = (String) body;
				} else {
					outputStr = JsonUtil.toJson(body);
				}
				logger.info("请求参数+" + outputStr);
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(outputStr.getBytes(encoding));
				outputStream.flush();
				outputStream.close();
			}
			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			return IOUtils.toByteArray(inputStream);
		} catch (IOException ex) {
			throw ex;
		} finally {
			// 释放资源
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				logger.error("关闭流失败", e);
			}
			try {
				conn.disconnect();
			} catch (Exception e) {

			}
		}
	}

	static class MyX509TrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}

	/**
	 * 重写一个方法
	 */
	static class myHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	static class MyAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
			String username = "guest";
			String password = "guest";
			return new PasswordAuthentication(username, password.toCharArray());
		}
	}
	
	
	public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("contentType", "UTF-8");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
	
	
	/**
	   * post请求
	   * @param url
	   * @param json
	   * @return
	*/
	public static String doPostJson(String url,String json){
		HttpClient client = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url);
	    try {
	      StringEntity s = new StringEntity(json);
	      s.setContentEncoding("UTF-8");
	      s.setContentType("application/json");//发送json数据需要设置contentType
	      post.setEntity(s);
	      HttpResponse res = client.execute(post);
	      if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
	        String result = EntityUtils.toString(res.getEntity());// 返回json格式：
	        return result;
	      }else{
	    	return res.getStatusLine().getStatusCode()+"-发送POST请求出现异常";
	      }
	    } catch (Exception e) {
	      return e.getMessage();
	    }
	    
	  }
	
	/*public static String doPost(String url,Map<String,String> map,String charset){  
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        String result = null;  
        try{  
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);  
            //设置参数  
            List<NameValuePair> list = new ArrayList<NameValuePair>();  
            Iterator iterator = map.entrySet().iterator();  
            while(iterator.hasNext()){  
                Entry<String,String> elem = (Entry<String, String>) iterator.next();  
                list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));  
            }  
            if(list.size() > 0){  
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);  
                httpPost.setEntity(entity);  
            }  
            HttpResponse response = httpClient.execute(httpPost);  
            if(response != null){  
                HttpEntity resEntity = response.getEntity();  
                if(resEntity != null){  
                    result = EntityUtils.toString(resEntity,charset);  
                }  
            }  
        }catch(Exception ex){  
            ex.printStackTrace();  
        }  
        return result;  
    }*/
	
	
	
	public static String postJson(String urlString ,String jsonObject) {
        String returnJson="";
        try {
            // 创建连接
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);            
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");//**注意点1**，需要此格式，后边这个字符集可以不设置
            connection.connect();
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());
            out.write(jsonObject.getBytes("UTF-8"));
            out.flush();
            out.close();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"utf-8"));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                sb.append(lines);
            }        
            returnJson = sb.toString();
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnJson;
    }
}
