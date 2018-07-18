package jsonify.monitors;

import java.io.IOException;

import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import jsonify.core.JsonCrawler;

public class InfoMonitor extends Thread {
	private final static Logger logger = Logger.getLogger(InfoMonitor.class);
	private JTextArea textArea;
	private String site;

	public InfoMonitor(JTextArea textAreaAux, String siteSelected) {
		textArea = textAreaAux;
		site = siteSelected;
	}

	@Override
	public void run() {
		try {
			JsonCrawler.gatherJsonAndMerge(textArea, site);
		} catch (IOException e) {
			logger.error("ERROR: " + e);
		}
	}
}