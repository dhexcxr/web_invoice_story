package objects;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import web_invoice_story.DbServices;

public abstract class DbObject {

	protected String searchString;
	protected String searchQuery;
	protected String outerSearchQueryAppendix;
	protected int[] querySubStringIndices = new int[2];

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

	public abstract List<? extends DbObject> search(String searchString);
	
	public abstract DbObject searchByPk(long primaryKey);
	
	public abstract long getDbPk();

	public abstract void setDbPk(long dbPk);
	
	public abstract String getPkName();
	
	public abstract String getTableName();
	
	// TODO make this a concrete method, do this by making ArrayList of fields in the concrete subclasses that can get pulled
		// into this map, then also an ArrayList of the methods/functions to get the string values to insert, or maybe
		// just a function that returns an ArrayList of the string values
	public abstract Map<String, String> getDataMap();
	
	public int delete() {
		return DbServices.deleteObject(this);
	}

	public void save() {
		DbServices.saveObject(this);
	}

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object obj);

}
