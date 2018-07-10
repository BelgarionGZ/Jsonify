package jsonify;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import jsonify.gui.GUI;

public class App {
	public static void main(String[] args) throws ClientProtocolException, IOException {
		GUI.begin();
	}
}