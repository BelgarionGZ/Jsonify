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
import jsonify.entities.KO;
import jsonify.utils.DateUtils;
import jsonify.utils.JsonConverter;
import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;
import jsonify.utils.WriteFile;

public class JsonCrawler {
	private final static Logger logger = Logger.getLogger(JsonCrawler.class);
	private static Settings settings;

	public static void gatherJsonAndMerge(JTextArea textArea, String site) throws IOException {
		settings = SettingsSingleton.getInstance();

		Boolean ko = false;
		Integer counter = 1;
		Integer emptyRequestsInARow = 0;
		Integer extraRequestsToDetectEnd = Integer.parseInt(settings.getProperty("EXTRA_REQUESTS_TO_DETECT_END"));
		KO jsonAuxKO = null;
		List<Asset> jsonAuxList = null;
		List<String> sitesAvailableArray = null;
		Set<String> assetIds = new HashSet<String>();
		Set<String> firstLevelCategories = new HashSet<String>();
		String assetId = new String();
		String firstLevelCategory = new String();
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
			assetIds = new HashSet<String>();
			counter = 1;
			emptyRequestsInARow = 0;
			jsonAux = new String();
			jsonToStore = new String();
			ko = false;
			urlAux = new String();

			textArea.append("Beginning site: " + siteAux + "\n");

			do {
				assetId = new String();
				firstLevelCategory = new String();

				urlAux = url.concat(siteAux).concat(String.format("&desde=%d&hasta=%d", counter, counter));

				textArea.append("[" + DateUtils.getCurrentLocalDateTimeStamp() + "]" + " ----- " + "Processing site: "
						+ siteAux + " ----- " + "Element: " + counter + "\n");
				textArea.update(textArea.getGraphics());

				logger.info("Processing URL: " + urlAux);

				counter += 1;

				jsonAux = JsonGet.sendRequest(urlAux);

				try {
					jsonAuxKO = JsonConverter.convertFromJson(jsonAux, new TypeToken<KO>() {
					}.getType());
					ko = jsonAuxKO.getResponse().getStatus().equals("KO");
				} catch (Exception e) {
					ko = false;
				}

				if (jsonAux == null || jsonAux.isEmpty() || ko) {
					emptyRequestsInARow += 1;
				} else {
					emptyRequestsInARow = 0;

					jsonAuxList = JsonConverter.convertFromJson(jsonAux, new TypeToken<List<Asset>>() {
					}.getType());

					for (Asset asset : jsonAuxList) {
						assetId = asset.getAssetid();
						firstLevelCategory = asset.getCategoriaPrimerNivel();

						if (firstLevelCategory != null && !firstLevelCategory.isEmpty()) {
							firstLevelCategories.add(firstLevelCategory);
						}
					}

					if (assetIds != null && assetId != null && !assetId.isEmpty()) {
						if (!assetIds.contains(assetId)) {
							assetIds.add(assetId);
							jsonToStore = JsonMerge.mergeJSONObjects(jsonAux, jsonToStore);
						} else {
							logger.info("Asset repeated: " + assetId);
							textArea.append("Asset repeated: " + assetId + "\n");
						}
					}
				}
			} while (emptyRequestsInARow < extraRequestsToDetectEnd);

			if (emptyRequestsInARow >= extraRequestsToDetectEnd) {
				WriteFile.write(jsonToStore, siteAux);
			} else {
				textArea.append("There was a problem getting site: " + siteAux + "\n");
			}

			textArea.append("Assets indexed: " + assetIds.size() + "\n");
			textArea.append("Ending site: " + siteAux + "\n");
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