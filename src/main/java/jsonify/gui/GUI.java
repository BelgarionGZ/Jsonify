package jsonify.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jsonify.core.JsonCrawler;
import jsonify.utils.Settings;
import jsonify.utils.SettingsSingleton;

public class GUI {
	private JButton button;
	private JComboBox<String> comboBox;
	private JFrame frame;
	private JPanel bottomPanel;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JScrollPane scrollPanel;
	private JTextArea textArea;
	private Settings settings;
	
	private void addButton() {
		button = new JButton("GO");
		
		topPanel.add(button);
	}
	
	private void addComboBox() {
		comboBox = new JComboBox<String>();
		comboBox.setMaximumSize(comboBox.getPreferredSize());
		
		topPanel.add(comboBox);
	}
	
	private void addTextArea() {
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
	
		bottomPanel.add(textArea);
	}
	
	public static void begin() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void bindAction() {
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = new String();
				
				try {
					String site = (String) comboBox.getSelectedItem();
					result = JsonCrawler.gatherJsonAndMerge(settings.getProperty("URL") + site);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				textArea.setText(result);
			}
		});
	}
	
	private void createBottomPanel() {
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout());
	}
	
	private void createBottomPanelScroll() {
		scrollPanel = new JScrollPane(bottomPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainPanel.add(scrollPanel, BorderLayout.CENTER);
	}
	
	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		mainPanel.setLayout(new BorderLayout());
		
		frame.add(mainPanel);
	}
	
	private void createTopPanel() {
		topPanel = new JPanel();
		topPanel.setBorder(BorderFactory.createTitledBorder("Select site"));
		topPanel.setLayout(new FlowLayout());
		
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
		String[] sitesAvailableArray = sitesAvailable.split(",");
		
		for(String site : sitesAvailableArray) {
			comboBox.addItem(site);
		}
	}
}