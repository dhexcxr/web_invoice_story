package objects;

import java.util.Map;

import web_invoice_story.DbServices;


public interface DbObjectSavable {		// TODO combine this with DbObjectSearchable
	
	public long getDbPk();
	
	public void setDbPk(long dbPk);
	
	public String getPkName();
	
	public String getTableName();
	
	public Map<String, String> getDataMap();
	
	public default int delete() {
		return DbServices.deleteObject(this);
	}

	public default void save() {
		DbServices.saveObject(this);
	}
}
