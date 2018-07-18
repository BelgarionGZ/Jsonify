package jsonify.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;

public class JsonGet {
	private final static Logger logger = Logger.getLogger(JsonGet.class);
	private static Settings settings;

	public static String sendRequest(String url) throws IOException {
		settings = SettingsSingleton.getInstance();

		Integer timeOut = Integer.parseInt(settings.getProperty("TIMEOUT"));
		String responseString = new String();

		RequestConfig.Builder requestBuilder = RequestConfig.custom();
		requestBuilder = requestBuilder.setConnectTimeout(timeOut);
		requestBuilder = requestBuilder.setSocketTimeout(timeOut);
		requestBuilder = requestBuilder.setConnectionRequestTimeout(timeOut);

		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setDefaultRequestConfig(requestBuilder.build());

		HttpClient client = builder.build();

		HttpUriRequest request = new HttpGet(url);
		request.addHeader(HttpHeaders.ACCEPT, "application/json");

		Boolean done = false;
		Integer failTimes = Integer.valueOf(1);
		Integer maxFailTimes = Integer.valueOf(settings.getProperty("MAX_FAIL_TIMES"));

		HttpResponse response = null;
		Integer statusCode = Integer.valueOf(0);

		while (!done && failTimes <= maxFailTimes) {
			try {
				logger.info("ATTEMPT NUMBER: " + failTimes);
				response = client.execute(request);
				statusCode = response.getStatusLine().getStatusCode();
				done = true;
			} catch (Exception e) {
				logger.error("ERROR: " + e);
				failTimes += 1;
			}
		}

		if (statusCode.equals(HttpStatus.SC_OK)) {
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity).trim();
		}

		return responseString;
	}
}