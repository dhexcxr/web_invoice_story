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
//	private String propFileName = "/Users/mcglass/eclipse-rcp-workspace3/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/../../../../../git/web_invoice_story/web_invoice_story/bin/resources/settings.properties";
	private Properties prop;
	private String database = "";
	private boolean skipCancelConfirm = false;
	private boolean skipCloseConfirm = false;
	private int currentLocation = 0;
	
	// To add and save new Settings, add a private field to class
		// use prop object to load property from file into field and create
		// public getters and setters to change Settings field from app code 

	// open settings file on instantiation, load file key/value pairs into Settings prop and fields
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
			currentLocation = Integer.parseInt(prop.getProperty("currentLocation"));
			
			inputStream.close();
		} catch (Exception e) {
			Application.getLogger().log(Level.SEVERE, "Exception {0}", e.toString());
		}
	}

	// return database url
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

	public int getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(int currentLocation) {
		this.currentLocation = currentLocation;
		prop.setProperty("currentLocation", Integer.toString(currentLocation));
	}

	// save current Settings.prop into file
	public void save() {											// HACK because just opening propFileName like we do above doesn't work with FileOutputStream for some reason
		try (FileOutputStream settingsFileOuput = new FileOutputStream("/Users/mcglass/eclipse-rcp-workspace3/.metadata/.plugins/org.eclipse.pde.core/.bundle_pool/../../../../../git/web_invoice_story/web_invoice_story/bin/resources/settings.properties")) {
			prop.store(settingsFileOuput, "Service Salamander Settings");
			settingsFileOuput.flush();
			settingsFileOuput.close();
		} catch (IOException e) {
			Application.getLogger().log(Level.SEVERE, e.toString());
		}
	}	
}
