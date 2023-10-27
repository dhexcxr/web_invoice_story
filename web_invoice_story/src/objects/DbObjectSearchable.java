package objects;

import java.util.List;

public abstract class DbObjectSearchable {

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
	
	public abstract List<DbObjectSearchable> search(String searchString);
	
	public abstract DbObjectSearchable searchByPk(long primaryKey);
}
