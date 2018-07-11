package jsonify.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class JsonConverter {
	private static final Gson gson = new Gson();

	public static <T> T convertFromJson(String toConvert, Class<T> clazz) {
		return gson.fromJson(toConvert, clazz);
	}

	public static <T> T convertFromJson(String toConvert, Type typeOfT) {
		return gson.fromJson(toConvert, typeOfT);
	}

	public static String convertToJson(Object toConvert) {
		return gson.toJson(toConvert);
	}
}