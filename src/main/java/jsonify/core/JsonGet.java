package jsonify.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
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

	public static String sendRequest(String url) throws ClientProtocolException, IOException {
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

		Integer counter = Integer.valueOf(0);
		Integer statusCode = Integer.valueOf(0);

		HttpResponse response = null;

		do {
			counter += 1;
			logger.info("Attempt number: " + counter);

			response = client.execute(request);
			statusCode = response.getStatusLine().getStatusCode();
		} while (!statusCode.equals(HttpStatus.SC_OK) && counter < 5);

		if (statusCode.equals(HttpStatus.SC_OK)) {
			HttpEntity entity = response.getEntity();
			responseString = EntityUtils.toString(entity).trim();
		}

		return responseString;
	}
}