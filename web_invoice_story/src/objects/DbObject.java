package objects;

import java.util.List;
import java.util.Map;

import web_invoice_story.Application;
import web_invoice_story.DbServices;

public abstract class DbObject {

	protected String searchString;
	protected String searchQuery;
	protected String outerSearchQueryAppendix;
	protected int[] querySubStringIndices = new int[2];
	
	private boolean locationMatters = false;
	private boolean locationThroughCustomer = false;

	public String getSearchString() {
		return searchString;
	}
	
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public String[] getWordsFromSearchString() {
		// return each individual word from user's search query string
		return searchString.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}

	public String getSearchQuery() {
		// return subclass's correct DB search query 
		int selectedLocation = Application.getSettings().getCurrentLocation();
		
		if (selectedLocation != 0 && !(this instanceof StoreLocation) && !(this instanceof User) && !(this instanceof ProjectSite)
				&& !(this instanceof Account) && !(this instanceof AccountTransaction) && !(this instanceof AccountBalanceHx)
				&& !(this instanceof Job) && !(this instanceof JobLabor) && !(this instanceof JobPart)) {
			// modify search query to only show results from selected location
			StringBuilder searchQueryWithLoc = new StringBuilder(searchQuery);
			
			// add location details
			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf("WHERE") - 1,
					"\nINNER JOIN web_invoice_story.storeLocation on storeLocation.locationId = "
					+ this.getTableName() + ".locationId"
					+ " AND storeLocation.locationId = " + selectedLocation);
//			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf(";"), " AND sl.locationId = " + selectedLocation);
			searchQuery = searchQueryWithLoc.toString();
		} else if (selectedLocation != 0 && (this instanceof ProjectSite)) {
			// modify search query to only show results from selected location
			StringBuilder searchQueryWithLoc = new StringBuilder(searchQuery);
			
			// add location details
			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf("WHERE") - 1,
					"\nINNER JOIN web_invoice_story.storeLocation on storeLocation.locationId = customer.locationId"
					+ " AND storeLocation.locationId = " + selectedLocation);
//			searchQueryWithLoc.insert(searchQueryWithLoc.indexOf(";"), " AND sl.locationId = " + selectedLocation);
			searchQuery = searchQueryWithLoc.toString();
		}
		
		StringBuilder innserSearchQuery = new StringBuilder(searchQuery);
		for (int i = 1; i < getWordsFromSearchString().length; i++) {
			innserSearchQuery.delete(querySubStringIndices[0], querySubStringIndices[1]);
			innserSearchQuery.insert(0, getOuterSearchQuery());
			innserSearchQuery.replace(innserSearchQuery.length() - 1, innserSearchQuery.length(), ");");
		}
		return innserSearchQuery.toString();
	}

	private String getOuterSearchQuery() {
		// help build full search query
		StringBuilder outerSearchQuery = new StringBuilder(searchQuery);
		outerSearchQuery.delete(outerSearchQuery.length() - 1, outerSearchQuery.length());
		outerSearchQuery.append(outerSearchQueryAppendix);
		return outerSearchQuery.toString();
	}
	
	// TODO see about making a castDown() function that automatically casts a DbObjectSaveable to its true subtype
	
	public boolean doesLocationMatter() {
		return locationMatters;
	}

	public void setLocationMatters(boolean locationMatters) {
		this.locationMatters = locationMatters;
	}

	public boolean isLocationThroughCustomer() {
		return locationThroughCustomer;
	}

	public void setLocationThroughCustomer(boolean locationThroughCustomer) {
		this.locationThroughCustomer = locationThroughCustomer;
	}

	public abstract List<? extends DbObject> search(String searchString);
	
	public abstract DbObject searchByPk(long primaryKey);
	
	public abstract long getDbPk();

	public abstract void setDbPk(long dbPk);
	
	public abstract String getPkName();
	
	public abstract String getTableName();
	
	public abstract Map<String, String> getDataMap();
	
	public int delete() {
		return DbServices.deleteObject(this);
	}

	public void save() {
		DbServices.saveObject(this);
	}

}
