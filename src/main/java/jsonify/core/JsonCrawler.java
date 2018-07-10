package jsonify.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;

public class JsonCrawler {
	private final static Logger logger = Logger.getLogger(JsonCrawler.class);
	private static Settings settings;

	public static String gatherJsonAndMerge(String url) throws ClientProtocolException, IOException {
		settings = SettingsSingleton.getInstance();
		
		Integer counter = 1;
		Integer failedRequestInARow = 0;
		Integer failedRequestMax = Integer.parseInt(settings.getProperty("FAILED_REQUEST"));
		String jsonAux = new String();
		String jsonToRet = new String();
		String urlAux = new String();

		do {
			urlAux = url.concat(String.format("&desde=%d&hasta=%d", counter, counter));
			
			logger.warn("URL: " + urlAux);
			
			counter += 1;
			
			jsonAux = JsonGet.sendRequest(urlAux);
			
			if(jsonAux == null || jsonAux.isEmpty()) {
				failedRequestInARow += 1;
			} else {
				failedRequestInARow = 0;
			}
			
			jsonToRet = JsonMerge.mergeJSONObjects(jsonAux, jsonToRet);
		} while(failedRequestInARow < failedRequestMax || (jsonAux != null && !jsonAux.isEmpty()));

		return String.valueOf(jsonToRet);
	}
}