package jsonify.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
	public static void write(String data, String fileName) throws IOException {
		File file = new File(fileName + ".txt");
		file.createNewFile();

		FileWriter fr = new FileWriter(file);
		fr.write(data);
		fr.close();
	}
}