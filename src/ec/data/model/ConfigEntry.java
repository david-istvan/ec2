package ec.data.model;

public class ConfigEntry {
	private String dataSetName;
	private int classes;
	
	public ConfigEntry(String dataSetName, int classes) {
		this.dataSetName = dataSetName;
		this.classes = classes;
	}
	
	public String getDataSetName() {
		return dataSetName;
	}

	public int getClasses() {
		return classes;
	}	
}