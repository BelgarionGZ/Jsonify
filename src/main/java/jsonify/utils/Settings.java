package jsonify.utils;

import java.io.IOException;
import java.util.Properties;

public class Settings extends Properties {

	private static final long serialVersionUID = 1L;

	public Settings() throws IOException {
		getProperties("settings.properties");
	}

	private void getProperties(String file) throws IOException {
		load(getClass().getResourceAsStream("/" + file));
	}
}