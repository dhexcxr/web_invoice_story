package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class ProjectSite extends DbObjectSearchable implements DbObjectSavable{
	
	// might not need this
	private long projectSiteId = -1;
	private long customerId;
	private String addressProjectSite;
	private String cityProjectSite;
	private String modelName;
	private String stateProjectSite;
	private int mileage;
	private String zipCodeProjectSite;
	private String notesProjectSite;
	private String notes;
	
	private String owner;
	
	public ProjectSite() {
	}
	
	public long getDbPk() {
		return getProjectSiteId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		projectSiteId = dbPk;
	}
	
	public String getPkName() {
		return "projectSiteId";
	}
	
	public String getTableName() {
		return "projectSite";
	}

	public long getProjectSiteId() {
		return projectSiteId;
	}

	public void setProjectSiteId(long unitId) {
		this.projectSiteId = unitId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getAddressProjectSite() {
		return addressProjectSite == null ? "" : addressProjectSite;
	}

	public void setAddressProjectSite(String addressProjectSiteString) {
		this.addressProjectSite = addressProjectSiteString;
	}

	public String getCityProjectSiteString() {
		return cityProjectSite == null ? "" : cityProjectSite;
	}

	public void setCityProjectSite(String cityProjectSite) {
		this.cityProjectSite = cityProjectSite;
	}

	public String getStateProjectSite() {
		return stateProjectSite;
	}

	public void setStateProjectSite(String stateProjectSite) {
		this.stateProjectSite = stateProjectSite;
	}

	public String getZipCodeProjectSiteString() {
		return zipCodeProjectSite == null ? "" : zipCodeProjectSite;
	}

	public void setZipCodeProjectSite(String zipCodeProjectSite) {
		this.zipCodeProjectSite = zipCodeProjectSite;
	}

	public String getNotesProjectSite() {
		return notesProjectSite == null ? "" : notesProjectSite;
	}

	public void setNotesProjectSite(String notesProjectSite) {
		this.notesProjectSite = notesProjectSite;
	}

//	public String getNotes() {
//		return notes == null ? "" : notes;
//	}
//
//	public void setNotes(String notes) {
//		this.notes = notes;
//	}
	
	public String getOwner() {
		return owner == null ? "" : owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO we might need to addressProjectSite these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter
		Map<String, String> dataMap = new HashMap<>();
		if (projectSiteId != -1) {
			dataMap.put("projectSiteId", Long.toString(projectSiteId));
		}
		dataMap.put("customerId", Long.toString(customerId));
		dataMap.put("addressProjectSite", addressProjectSite);
		dataMap.put("cityProjectSite", cityProjectSite);
		dataMap.put("stateProjectSite", stateProjectSite);
		dataMap.put("zipCodeProjectSite", zipCodeProjectSite);
		dataMap.put("notesProjectSite", notesProjectSite);
		
		return dataMap;
	}

	@Override
	public List<ProjectSite> search(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT u.projectSiteId, u.customerId, u.address, u.city, u.state, u.zipCode, u.notes, c.lastName, c.firstName
				FROM web_invoice_story.projectSite AS u JOIN web_invoice_story.customer AS c ON u.customerId = c.customerId 
				WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.address LIKE ? OR u.city LIKE ? OR u.state LIKE ? OR u.zipCode LIKE ?;""";
		super.outerSearchQueryAppendix = " AND u.projectSiteId IN (";
		super.querySubStringIndices[0] = 15;
		super.querySubStringIndices[1] = 128;
		
		return packObjects(DbServices.searchForObject(this));
	}

	@Override
	public ProjectSite searchByPk(long primaryKey) {		// TODO NOTE actually looking at all of these, we could probably leave
		super.searchString = Long.toString(primaryKey);				// all the super. declarations in the constructor, maybe addressProjectSite a separate var for the searchByPk query,
		super.searchQuery = """
				SELECT u.projectSiteId, u.customerId, u.address, u.city, u.state, u.zipCode, u.notes, c.lastName, c.firstName 
				FROM web_invoice_story.projectSite AS u JOIN web_invoice_story.customer AS c ON u.customerId = c.customerId
				WHERE projectSiteId = ?;""";
		super.outerSearchQueryAppendix = " AND u.projectSiteId IN (";
		super.querySubStringIndices[0] = 15;					// and move these 2 search() function implementations into the DbObjectSearchable too
		super.querySubStringIndices[1] = 128;						// we just need to look at who calls what and where is a specific DbObjectSearchable expected
																	// and where can we give it the generic ? extends DbObjectSearchable
																	// I thought of this because if this function here returns a List<? extends DbObjectSearchable instead
		return packObject(DbServices.searchForObjectByPk(this));				// of a List<ProjectSite> nothing seems to complain, though I didn't actually test anything
	}
	
	private List<ProjectSite> packObjects(List<DbObjectSearchable> searchResults) {		// TODO NOTE look at how all of these are implemented
		// helper to cast List of DbObjectSearchable to List of Customer				// this could probably go into DbObjectSearchable
		List<ProjectSite> unitSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			if (object instanceof ProjectSite projectSite) {
				unitSearchResults.add(projectSite);
			}
		}
		
		return unitSearchResults;
	}
	
	private ProjectSite packObject(DbObjectSearchable searchResult) {
		if (searchResult instanceof ProjectSite projectSite) {
			return projectSite;
		}
		throw new IllegalArgumentException("objects.ProjectSite.packObject(): dbObjectSearchable is not a ProjectSite");
	}
}
