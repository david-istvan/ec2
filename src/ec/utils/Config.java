package ec.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ec.data.model.ConfigEntry;

public class Config {
	private String inputPath;
	private String outputPath;
	private List<String> dataSetNames;
	private List<ConfigEntry> inputDataSet;
	private static Config instance;

	private Config() {
		this.dataSetNames = new ArrayList<String>();
		this.inputDataSet = new ArrayList<ConfigEntry>();
		readConfig();
	}

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	private void readConfig() {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("ce-config.xml"));
			doc.getDocumentElement().normalize();

			Node inputDirectory = (doc.getElementsByTagName("input-directory")).item(0);
			Node outputDirectory = (doc.getElementsByTagName("output-directory")).item(0);

			this.inputPath = inputDirectory.getChildNodes().item(0).getNodeValue().toString().trim().replace("\\", "\\\\");
			this.outputPath = outputDirectory.getChildNodes().item(0).getNodeValue().toString().trim().replace("\\", "\\\\");

			Node inputFiles = (doc.getElementsByTagName("input-files")).item(0);
			if (!(Boolean.parseBoolean(((Element) inputFiles).getAttribute("all").toString().trim()))) {
				NodeList fileList = doc.getElementsByTagName("file");
				
				//FIXME: shit
				for (int i = 0; i < fileList.getLength(); i++) {
					Node file = fileList.item(i);
					
					Element fileElement = (Element)file;
					
					NodeList nameList = fileElement.getElementsByTagName("name");
					Element nameElement = (Element)nameList.item(0);
					NodeList nameTextList = nameElement.getChildNodes();
					
					NodeList classesList = fileElement.getElementsByTagName("classes");
					Element classesElement = (Element)classesList.item(0);
					NodeList classesTextList = classesElement.getChildNodes();
					
					
					String fileName = nameTextList.item(0).getNodeValue().toString().trim();
					int classes = Integer.parseInt(classesTextList.item(0).getNodeValue().toString().trim());
					//dataSetNames.add(file.getChildNodes().item(0).getNodeValue().toString().trim());
					inputDataSet.add(new ConfigEntry(fileName, classes));
				}
			}
			else {
				File dir = new File(this.inputPath);
				String[] children = dir.list();
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						this.dataSetNames.add(children[i]);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getInputPath() {
		return inputPath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public List<String> getDataSetNames() {
		return dataSetNames;
	}

	public List<ConfigEntry> getInputDataSet() {
		return inputDataSet;
	}
}