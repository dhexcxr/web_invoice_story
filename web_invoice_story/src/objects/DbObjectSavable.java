package objects;

import java.util.Map;

public interface DbObjectSavable {
	
	public long getDbPk();
	
	public void setDbPk(long dbPk);
	
	public String getPkName();
	
	public String getTableName();
	
	public Map<String, String> getDataMap();
}
