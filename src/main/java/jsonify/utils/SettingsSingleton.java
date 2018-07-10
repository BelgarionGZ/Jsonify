package jsonify.utils;

import java.io.IOException;

public class SettingsSingleton {
	private static Settings settings;

	public static synchronized Settings getInstance() throws IOException {
		if (settings == null) {
			settings = new Settings();
		}

		return settings;
	}
}