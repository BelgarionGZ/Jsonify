package jsonify.core;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jsonify.entities.Asset;
import jsonify.utils.JsonConverter;

public class JsonMerge {
	public static String mergeJSONObjects(String json1, String json2) {
		List<Asset> aux = new ArrayList<Asset>();
		String resultJson = new String();

		List<Asset> firstList = JsonConverter.convertFromJson(json1, new TypeToken<List<Asset>>() {
		}.getType());
		List<Asset> secondList = JsonConverter.convertFromJson(json2, new TypeToken<List<Asset>>() {
		}.getType());

		if (firstList != null && !firstList.isEmpty()) {
			aux.addAll(firstList);
		}

		if (secondList != null && !secondList.isEmpty()) {
			aux.addAll(secondList);
		}

		if (aux != null && !aux.isEmpty()) {
			resultJson = new Gson().toJson(aux);
		}

		return resultJson;
	}
}