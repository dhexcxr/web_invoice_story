package objects;

import web_invoice_story.DbServices;

public class DbObjectSearchable {

	protected String searchString;
	protected String searchQuery;
	protected String outerSearchQueryAppendix;
	protected int[] querySubStringIndices = new int[2];

	public String getSearchString() {
		return searchString;
	}

	public String[] getWordsFromSearchString() {
		return DbServices.sanitizer(searchString);
	}

	public String getSearchQuery() {
		StringBuilder innserSearchQuery = new StringBuilder(searchQuery);
		for (int i = 1; i < getWordsFromSearchString().length; i++) {
			innserSearchQuery.delete(querySubStringIndices[0], querySubStringIndices[1]);
			innserSearchQuery.insert(0, getOuterSearchQuery());
			innserSearchQuery.replace(innserSearchQuery.length() - 1, innserSearchQuery.length(), ");");
		}
		return innserSearchQuery.toString();
	}

	public String getOuterSearchQuery() {
		StringBuilder outerSearchQuery = new StringBuilder(searchQuery);
		outerSearchQuery.delete(outerSearchQuery.length() - 1, outerSearchQuery.length());
		outerSearchQuery.append(outerSearchQueryAppendix);
		return outerSearchQuery.toString();
	} 
}
