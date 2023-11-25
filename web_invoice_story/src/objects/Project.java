package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class Project extends DbObjectWithLocation {
	
	// TODO make a 1 line invoice when cashiering Projects
	
	private long projectId = -1;
	private long customerId;
	private Customer customer;
	private String customerName;
	private String customerData;
	private long projectSiteId;
	private ProjectSite projectSite;
	private String projectSiteAddress;
	private String projectSiteCity;
	private String projectSiteState;
	private String projectSiteZip;
	private List<Job> jobs;
	private Timestamp createdDate;
	private Timestamp closedDate;
	private BigDecimal tax;
	private BigDecimal total;
//	private boolean cashiered;		// we might be able to use the presence of closedDate to tell if it has been cashiered
	
	private BigDecimal amountCharged;
	
	
	public Project() {
		this.setLocationMatters(true);
	}
	
	@Override
	public long getDbPk() {
		return getProjectId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		projectId = dbPk;
	}
	
	@Override
	public String getPkName() {
		return "projectId";
	}

	@Override
	public String getTableName() {
		return "project";
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long repairOrderId) {
		this.projectId = repairOrderId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerName() {
		return customerName == null ? "" : customerName;
	}

	public void setCustomerName(String firstname, String lastname) {
		if (firstname != null) {
			firstname = firstname.equals("") ? firstname : ", " + firstname;
		}
		this.customerName = lastname + firstname;
	}
	
	public void setCustomerName(String[] name) {
		if (name[1] != null) {
			name[1] = name[1].equals("") ? name[1] : ", " + name[1].replaceFirst("[^a-zA-Z0-9'-]", "");
		}
		this.customerName = name[0] + name[1];
	}

	public String getCustomerData() {
		return customerData == null ? "" : customerData;
	}

	public void setCustomerData(String customerData) {
		this.customerData = customerData;
	}

	public long getProjectSiteId() {
		return projectSiteId;
	}

	public void setProjectSiteId(long projectSiteId) {
		this.projectSiteId = projectSiteId;
	}

	public ProjectSite getProjectSite() {
		return projectSite;
	}

	public void setProjectSite(ProjectSite projectSite) {
		this.projectSite = projectSite;
	}

	public String getProjectSiteAddress() {
		return projectSiteAddress == null ? "" : projectSiteAddress;
	}

	public void setProjectSiteAddress(String projectSiteAddress) {
		this.projectSiteAddress = projectSiteAddress;
	}

	public String getProjectSiteCity() {
		return projectSiteCity == null ? "" : projectSiteCity;
	}

	public void setProjectSiteCity(String projectSiteCity) {
		this.projectSiteCity = projectSiteCity;
	}

	public String getProjectSiteState() {
		return projectSiteState == null ? "" : projectSiteState;
	}

	public void setProjectSiteState(String projectSiteState) {
		this.projectSiteState = projectSiteState;
	}

	public String getProjectSiteZip() {
		return projectSiteZip == null ? "" : projectSiteZip;
	}

	public void setProjectSiteZip(String projectSiteZip) {
		this.projectSiteZip = projectSiteZip;
	}

	public String getProjectSiteData() {
		return getProjectSiteAddress() + " " + getProjectSiteCity() + " " + getProjectSiteState() + "\n" + getProjectSiteZip();
	}
	
	public List<Job> getJobs() {
		if (jobs == null) {
			jobs = new ArrayList<>();
		}
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}
	
	public String getJobNames() {
		StringBuilder jobNames = new StringBuilder();
		for (Job job : getJobs()) {
			if (jobNames.length() > 0) {
				jobNames.append(", ");
			}
			jobNames.append(job.getJobName());
		}
		return jobNames.toString();
	}
	
	public void addJob(Job job) {
		if (jobs == null) {
			jobs = new ArrayList<>();
		}
		jobs.add(job);
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getClosedDate() {
		return closedDate;
	}

	public void setClosedDate(Timestamp closedDate) {
		this.closedDate = closedDate;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}
	
	public Map<String, String> getDataMap() {
		Map<String, String> dataMap = new HashMap<>();
		if (projectId != -1) {
			dataMap.put("projectId", Long.toString(projectId));
		}
		dataMap.put("locationId", Long.toString(locationId));
		dataMap.put("customerid", Long.toString(customerId));
		dataMap.put("customerdata", customerData);
		dataMap.put("projectSiteId", Long.toString(projectSiteId));
		
		if (createdDate != null) {
			dataMap.put("createdTime", createdDate.toString());
		}
		
		if (closedDate != null) {
			dataMap.put("closedTime", closedDate.toString());
		}
		
		dataMap.put("tax", tax.toString());
		dataMap.put("total", total.toString());
		return dataMap;
	}

	@Override
	public List<Project> search(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT project.projectId, project.customerId, project.locationId,
				customer.firstname, customer.lastname, customer.address, customer.city, customer.state, customer.zipcode, customer.homephone, customer.cellphone, customer.email,
				project.projectSiteId, projectSite.address, projectSite.city, projectSite.state, projectSite.zipCode,
				project.createdTime, project.closedTime, project.cashiered
				FROM web_invoice_story.project
				JOIN web_invoice_story.customer ON project.customerid = customer.customerid
				JOIN web_invoice_story.projectSite ON project.projectSiteId = projectSite.projectSiteId
				WHERE project.projectId LIKE ? OR customer.lastname LIKE ? OR customer.firstname LIKE ? OR customer.homephone LIKE ? OR customer.cellphone LIKE ? OR projectSite.address LIKE ? OR projectSite.city LIKE ? OR projectSite.state LIKE ? OR projectSite.zipCode LIKE ?;""";
		super.outerSearchQueryAppendix = " AND projectId IN (";
		super.querySubStringIndices[0] = 13;
		super.querySubStringIndices[1] = 210;
		
		return packObjects(DbServices.searchForObject(this));
	}

	@Override
	public Project searchByPk(long primaryKey) {		// NOTE not used anywhere
		super.searchString = Long.toString(primaryKey);
		super.searchQuery = """
				SELECT project.projectId, project.customerId, project.locationId,
				customer.firstname, customer.lastname, customer.address, customer.city, customer.state, customer.zipcode, customer.homephone, customer.cellphone, customer.email,
				project.projectSiteId, projectSite.address, projectSite.city, projectSite.state, projectSite.zipCode,
				project.createdTime, project.closedTime, project.cashiered
				FROM web_invoice_story.project
				WHERE project.projectId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
		
		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make this an abstract method in DbObject
	private List<Project> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<Project> repairOrderSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
					// TODO use this shortened form of instanceof casting in the Tables
			repairOrderSearchResults.add(packObject(object));
		}
		
		return repairOrderSearchResults;
	}
	
	private Project packObject(DbObject searchResult) {
		if (searchResult instanceof Project project) {
			return project;
		}
		throw new IllegalArgumentException("objects.Project.packObject(): dbObjectSearchable is not a Project");
	}
}
