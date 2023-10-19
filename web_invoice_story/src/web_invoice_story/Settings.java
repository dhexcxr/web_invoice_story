package web_invoice_story;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

public class Settings {
	
	private InputStream inputStream;
	private String propFileName = "resources/settings.properties";
	private Properties prop;
	private String database = "";
	private boolean skipCancelConfirm = false;
	private boolean skipCloseConfirm = false;
	

	public Settings() {
		try {
			prop = new Properties();
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("settings file '" + propFileName + "' not found");
			}
			
			database = prop.getProperty("database");
			skipCancelConfirm = Boolean.parseBoolean(prop.getProperty("skipCancelConfirm"));
			skipCloseConfirm = Boolean.parseBoolean(prop.getProperty("skipCloseConfirm"));		
		} catch (Exception e) {
			Application.getLogger().log(Level.SEVERE, "Exception {0}", e.toString());
		}
	}
	
	public String getDatabase() {
		return database;
	}
	
	public boolean skipCancelConfirm() {
		return skipCancelConfirm;
	}
	
	public void setSkipCancelConfirm(boolean newSetting) {
		skipCancelConfirm = newSetting;
		prop.setProperty("skipCancelConfirm", Boolean.toString(skipCancelConfirm));
	}
	
	public boolean skipCloseConfirm() {
		return skipCloseConfirm;
	}
	
	public void setSkipCloseConfirm(boolean newSetting) {
		skipCloseConfirm = newSetting;
		prop.setProperty("skipCloseConfirm", Boolean.toString(skipCloseConfirm));
	}
	
	public void save() {
		try (FileOutputStream settingsFileOuput = new FileOutputStream(propFileName)) {
			prop.store(settingsFileOuput, "Service Salamander Settings");
		} catch (IOException e) {
			Application.getLogger().log(Level.SEVERE, e.toString());
		}
	}

}
