package jsonify.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

import jsonify.entities.Asset;
import jsonify.utils.JsonConverter;
import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;
import jsonify.utils.WriteFile;

public class JsonCrawler {
	private final static Logger logger = Logger.getLogger(JsonCrawler.class);
	private static Settings settings;

	public static void gatherJsonAndMerge(JTextArea textArea, String site) throws IOException {
		settings = SettingsSingleton.getInstance();

		Integer counter = 1;
		Integer failedRequestInARow = 0;
		Integer failedRequestMax = Integer.parseInt(settings.getProperty("FAILED_REQUEST"));
		List<Asset> jsonAuxList = null;
		List<String> sitesAvailableArray = null;
		Set<String> firstLevelCategories = new HashSet<String>();
		String jsonAux = new String();
		String jsonToStore = new String();
		String sitesAvailable = new String();
		String url = settings.getProperty("URL");
		String urlAux = new String();

		if (site.equals("ALL")) {
			sitesAvailable = settings.getProperty("SITES_AVAILABLE");
			sitesAvailableArray = Arrays.asList(sitesAvailable.split(","));
		} else {
			sitesAvailableArray = new ArrayList<String>();
			sitesAvailableArray.add(site);
		}

		textArea.append("Beginning" + "\n");

		for (String siteAux : sitesAvailableArray) {
			counter = 1;
			failedRequestInARow = 0;
			jsonAux = new String();
			jsonToStore = new String();
			urlAux = new String();

			textArea.append("Beginning site: " + siteAux + "\n");

			do {
				urlAux = url.concat(siteAux).concat(String.format("&desde=%d&hasta=%d", counter, counter));

				textArea.append("Processing site: " + siteAux + " ----- " + "Element: " + counter + "\n");
				textArea.update(textArea.getGraphics());

				logger.warn("Processing URL: " + urlAux);

				counter += 1;

				jsonAux = JsonGet.sendRequest(urlAux);

				if (jsonAux == null || jsonAux.isEmpty()) {
					failedRequestInARow += 1;
				} else {
					failedRequestInARow = 0;

					jsonAuxList = JsonConverter.convertFromJson(jsonAux, new TypeToken<List<Asset>>() {
					}.getType());

					for (Asset asset : jsonAuxList) {
						String firstLevelCategory = asset.getCategoriaPrimerNivel();

						if (firstLevelCategory != null && !firstLevelCategory.isEmpty()) {
							firstLevelCategories.add(firstLevelCategory);
						}
					}

					jsonToStore = JsonMerge.mergeJSONObjects(jsonAux, jsonToStore);
				}
			} while (failedRequestInARow < failedRequestMax || (jsonAux != null && !jsonAux.isEmpty()));

			textArea.append("Ending site: " + siteAux + "\n");

			WriteFile.write(jsonToStore, siteAux);
		}

		textArea.append("Beginning first level categories: " + "\n");

		for (String aux : firstLevelCategories) {
			textArea.append(aux + "\n");
		}

		WriteFile.write(firstLevelCategories.toString(), "FirstLevelCategories");

		textArea.append("Ending first level categories: " + "\n");

		textArea.append("Ending" + "\n");
	}
}