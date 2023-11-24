package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class ProjectSite extends DbObject {
	
	// might not need this
	private long projectSiteId = -1;
	private long customerId;
	private String addressProjectSite;
	private String cityProjectSite;
	private String stateProjectSite;
	private String zipCodeProjectSite;
	private String notesProjectSite;
	
	private String owner;
	
	public ProjectSite() {
		this.setLocationMatters(false);
		this.setLocationThroughCustomer(true);
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

	public void setProjectSiteId(long projectSiteId) {
		this.projectSiteId = projectSiteId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getProjectSiteAddress() {
		return addressProjectSite == null ? "" : addressProjectSite;
	}

	public void setProjectSiteAddress(String addressProjectSiteString) {
		this.addressProjectSite = addressProjectSiteString;
	}

	public String getProjectSiteCity() {
		return cityProjectSite == null ? "" : cityProjectSite;
	}

	public void setProjectSiteCity(String cityProjectSite) {
		this.cityProjectSite = cityProjectSite;
	}

	public String getProjectSiteState() {
		return stateProjectSite;
	}

	public void setProjectSiteState(String stateProjectSite) {
		this.stateProjectSite = stateProjectSite;
	}

	public String getProjectSiteZipCode() {
		return zipCodeProjectSite == null ? "" : zipCodeProjectSite;
	}

	public void setProjectSiteZipCode(String zipCodeProjectSite) {
		this.zipCodeProjectSite = zipCodeProjectSite;
	}

	public String getProjectSiteNotes() {
		return notesProjectSite == null ? "" : notesProjectSite;
	}

	public void setProjectSiteNotes(String notesProjectSite) {
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
				SELECT projectSite.projectSiteId, projectSite.customerId, projectSite.address, projectSite.city, projectSite.state, projectSite.zipCode, projectSite.notes, customer.lastName, customer.firstName
				FROM web_invoice_story.projectSite
				JOIN web_invoice_story.customer ON projectSite.customerId = customer.customerId 
				WHERE customer.firstName LIKE ? OR customer.lastName LIKE ? OR projectSite.address LIKE ? OR projectSite.city LIKE ? OR projectSite.state LIKE ? OR projectSite.zipCode LIKE ?;""";
		super.outerSearchQueryAppendix = " AND projectSite.projectSiteId IN (";
		super.querySubStringIndices[0] = 32;
		super.querySubStringIndices[1] = 193;
		
		return packObjects(DbServices.searchForObject(this));
	}

	@Override
	public ProjectSite searchByPk(long primaryKey) {		// TODO NOTE actually looking at all of these, we could probably leave
		super.searchString = Long.toString(primaryKey);				// all the super. declarations in the constructor, maybe addressProjectSite a separate var for the searchByPk query,
		super.searchQuery = """
				SELECT projectSite.projectSiteId, projectSite.customerId, projectSite.address, projectSite.city, projectSite.state, projectSite.zipCode, projectSite.notes, customer.lastName, customer.firstName 
				FROM web_invoice_story.projectSite
				INNER JOIN web_invoice_story.customer ON projectSite.customerId = customer.customerId
				WHERE projectSiteId = ?;""";
		super.outerSearchQueryAppendix = " AND projectSite.projectSiteId IN (";
		super.querySubStringIndices[0] = 32;					// and move these 2 search() function implementations into the DbObject too
		super.querySubStringIndices[1] = 193;						// we just need to look at who calls what and where is a specific DbObject expected
																	// and where can we give it the generic ? extends DbObject
																	// I thought of this because if this function here returns a List<? extends DbObject instead
		return packObject(DbServices.searchForObjectByPk(this));				// of a List<ProjectSite> nothing seems to complain, though I didn't actually test anything
	}
	
	private List<ProjectSite> packObjects(List<DbObject> searchResults) {		// TODO NOTE look at how all of these are implemented
		// helper to cast List of DbObject to List of Customer				// this could probably go into DbObject
		List<ProjectSite> projectSiteSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			if (object instanceof ProjectSite projectSite) {
				projectSiteSearchResults.add(projectSite);
			}
		}
		
		return projectSiteSearchResults;
	}
	
	private ProjectSite packObject(DbObject searchResult) {
		if (searchResult instanceof ProjectSite projectSite) {
			return projectSite;
		}
		throw new IllegalArgumentException("objects.ProjectSite.packObject(): dbObjectSearchable is not a ProjectSite");
	}
}
