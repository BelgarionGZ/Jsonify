package jsonify.core;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class JsonGet {
	public static String sendRequest(String url) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();

		HttpUriRequest request = new HttpGet(url);
		request.addHeader(HttpHeaders.ACCEPT, "application/json");

		HttpResponse response = client.execute(request);

		HttpEntity entity = response.getEntity();

		String responseString = EntityUtils.toString(entity);

		return responseString.trim();
	}
}