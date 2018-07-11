package jsonify.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import org.apache.log4j.Logger;

import jsonify.monitors.InfoMonitor;
import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;

public class GUI {
	private final static Logger logger = Logger.getLogger(GUI.class);
	private JButton button;
	private JComboBox<String> comboBox;
	private JFrame frame;
	private JPanel bottomPanel;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JScrollPane scrollPanel;
	private JTextArea textArea;
	private static Settings settings;

	private void addButton() {
		button = new JButton("GO");

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.1;

		topPanel.add(button, c);
	}

	private void addComboBox() {
		comboBox = new JComboBox<String>();
		comboBox.setMaximumSize(comboBox.getPreferredSize());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		c.weightx = 0.9;

		topPanel.add(comboBox, c);
	}

	private void addTextArea() {
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		bottomPanel.add(textArea, BorderLayout.CENTER);
	}

	public static void begin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					logger.warn("ERROR: " + e);
				}
			}
		});
	}

	private void bindAction() {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String siteSelected = (String) comboBox.getSelectedItem();
				InfoMonitor infoMonitor = new InfoMonitor(textArea, siteSelected);
				infoMonitor.start();
			}
		});
	}

	private void createBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
	}

	private void createBottomPanelScroll() {
		Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		Border margin = BorderFactory.createEmptyBorder(5, 2, 0, 2);

		scrollPanel = new JScrollPane(bottomPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setBorder(new CompoundBorder(margin, border));

		mainPanel.add(scrollPanel, BorderLayout.CENTER);
	}

	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());

		frame.add(mainPanel);
	}

	private void createTopPanel() {
		Border aux = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
		Border border = BorderFactory.createTitledBorder(aux, "Select site");
		Border margin = BorderFactory.createEmptyBorder(0, 0, 5, 0);

		topPanel = new JPanel();
		topPanel.setBorder(new CompoundBorder(margin, border));
		topPanel.setLayout(new GridBagLayout());

		mainPanel.add(topPanel, BorderLayout.PAGE_START);
	}

	public GUI() throws IOException {
		initialize();
	}

	private void initialize() throws IOException {
		settings = SettingsSingleton.getInstance();

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Jsonify");

		createMainPanel();
		createTopPanel();
		createBottomPanel();
		createBottomPanelScroll();

		addComboBox();
		addButton();
		addTextArea();

		loadValuesInComboBox();

		bindAction();
	}

	private void loadValuesInComboBox() {
		String sitesAvailable = settings.getProperty("SITES_AVAILABLE");
		List<String> sitesAvailableArray = Arrays.asList(sitesAvailable.split(","));

		if (sitesAvailableArray.size() > 0) {
			comboBox.addItem("ALL");
		}

		for (String site : sitesAvailableArray) {
			comboBox.addItem(site);
		}
	}
}