package kr.hyperlinkpool.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.hyperlinkpool.constants.NodeConstants;
import kr.hyperlinkpool.i18n.MessageFactory;

public class HttpUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	public static boolean validateUri(String uri) {
		String regex = "^(https?):\\/\\/([^:\\/\\s]+)(:([^\\/]*))?((\\/[^\\s/\\/]+)*)?\\/?([^#\\s\\?]*)(\\?([^#\\s]*))?(#(\\w*))?$";
		boolean result = Pattern.matches(regex, uri);
		return result;
	}
	
	public static String urlContentsLoad(String uri) {
		URL url;
		URLConnection urlConnection = null;
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		String result = null;
		try {
			url = new URL(uri);
			urlConnection = url.openConnection();
			inputStream = urlConnection.getInputStream();
			byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[4096];
			while(inputStream.read(buffer) > -1) {
				byteArrayOutputStream.write(buffer);
			}
			
			result = new String(byteArrayOutputStream.toByteArray(), Charset.forName("UTF-8")).trim();
		} catch (MalformedURLException e) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("URL 형식이 아닙니다.", "M00141"), true);
		} catch (IOException e) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("통신 중 오류가 발생했습니다.", "M00142"), true);
		} finally {
			try {
				if(byteArrayOutputStream != null) {
					byteArrayOutputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("통신 중 오류가 발생했습니다.", "M00142"), true);
			}
		}
		return result;
	}
	
	public static Map<String, String> readNodeMeticsInfo(String uri) {
		URL url;
		URLConnection urlConnection = null;
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		BufferedInputStream bufferedInputStream = null;
		Map<String, String> nodeMetricsInfo = null;
		try {
			url = new URL(uri);
			urlConnection = url.openConnection();
			inputStream = urlConnection.getInputStream();
			bufferedInputStream = new BufferedInputStream(inputStream);
			inputStreamReader = new InputStreamReader(bufferedInputStream, Charset.forName("UTF-8"));
			bufferedReader = new BufferedReader(inputStreamReader);
			String buffer = null;
			nodeMetricsInfo = new HashMap<String, String>();
			while((buffer = bufferedReader.readLine()) != null) {
				String[] splitData = buffer.trim().split(NodeConstants.BLANK_SPACE);
				logger.info("Key : " + splitData[0].trim() + " / Value : " + splitData[1].trim());
				nodeMetricsInfo.put(splitData[0].trim(), splitData[1].trim());
			}
		} catch (MalformedURLException e) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("URL 형식이 아닙니다.", "M00141"), true);
		} catch (IOException e) {
			MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("통신 중 오류가 발생했습니다.", "M00142"), true);
		} finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
				if(inputStreamReader != null) {
					inputStreamReader.close();
				}
				if(bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("통신 중 오류가 발생했습니다.", "M00142"), true);
			}
		}
		return nodeMetricsInfo;
	}
	
	public static JSONObject getPoolInfomation(String poolId) {
		HttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(NodeConstants.RETRIEVE_ADAPOOLS_YOROI_POOLS_INFO_API);
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("search", poolId));
		params.add(new BasicNameValuePair("sort", "score"));
		params.add(new BasicNameValuePair("limit", "256"));
		
		HttpResponse response = null;
		HttpEntity entity = null;;
		
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		BufferedInputStream bufferedInputStream = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
				bufferedInputStream = new BufferedInputStream(inputStream);
				inputStreamReader = new InputStreamReader(bufferedInputStream, Charset.forName("UTF-8"));
				bufferedReader = new BufferedReader(inputStreamReader);
				String buffer = null;
				while((buffer = bufferedReader.readLine()) != null) {
					stringBuilder.append(buffer);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
				if(inputStreamReader != null) {
					inputStreamReader.close();
				}
				if(bufferedInputStream != null) {
					bufferedInputStream.close();
				}
				if(inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				MessagePrompter.promptMessage(MessageFactory.getInstance().getMessage("통신 중 오류가 발생했습니다.", "M00142"), true);
			}
		}
		
		String result = stringBuilder.toString();
		JSONObject jsonObject = null;
		if(result != null && result.length() > 0) {
			jsonObject = new JSONObject(stringBuilder.toString());
		}
		return jsonObject;
	}
}