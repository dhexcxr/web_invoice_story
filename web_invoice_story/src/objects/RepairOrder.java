package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class RepairOrder extends DbObjectSearchable implements DbObjectSavable{
	
	private long repairOrderId = -1;
	private long customerId;
	private Customer customer;
	private String customerName;
	private String customerData;
	private long unitId;
	private Unit unit;
	private String unitYear;
	private String unitMake;
	private String unitModel;
	private String unitVin;
	private List<Job> jobs;
	private Timestamp createdDate;
	private Timestamp closedDate;
	private BigDecimal tax;
	private BigDecimal total;
//	private boolean cashiered;		// we might be able to use the presence of closedDate to tell if it has been cashiered
	
	
	public RepairOrder() {
	}

	public long getRepairOrderId() {
		return repairOrderId;
	}

	public void setRepairOrderId(long repairOrderId) {
		this.repairOrderId = repairOrderId;
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

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getUnitYear() {
		return unitYear == null ? "" : unitYear;
	}

	public void setUnitYear(String unitYear) {
		this.unitYear = unitYear;
	}

	public String getUnitMake() {
		return unitMake == null ? "" : unitMake;
	}

	public void setUnitMake(String unitMake) {
		this.unitMake = unitMake;
	}

	public String getUnitModel() {
		return unitModel == null ? "" : unitModel;
	}

	public void setUnitModel(String unitModel) {
		this.unitModel = unitModel;
	}

	public String getUnitVin() {
		return unitVin == null ? "" : unitVin;
	}

	public void setUnitVin(String unitVin) {
		this.unitVin = unitVin;
	}

	public String getUnitData() {
		return getUnitYear() + " " + getUnitMake() + " " + getUnitModel() + "\n" + getUnitVin();
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

	@Override
	public long getDbPk() {
		return getRepairOrderId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		repairOrderId = dbPk;
	}
	
	@Override
	public String getPkName() {
		return "roid";
	}

	@Override
	public String getTableName() {
		return "ro";
	}
	
	public Map<String, String> getDataMap() {
		Map<String, String> dataMap = new HashMap<>();
		if (repairOrderId != -1) {
			dataMap.put("roid", Long.toString(repairOrderId));
		}
		dataMap.put("customerid", Long.toString(customerId));
		dataMap.put("customerdata", customerData);
		dataMap.put("unitid", Long.toString(unitId));
		
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
	public List<RepairOrder> search(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT r.roId, r.customerId,
				c.firstname, c.lastname, c.address, c.city, c.state, c.zipcode, c.homephone, c.cellphone, c.email,
				r.unitId, u.year, u.make, u.model, u.vin,
				r.createdTime, r.closedTime, r.cashiered
				FROM cis2901c.ro AS r JOIN cis2901c.customer AS c ON r.customerid = c.customerid
				JOIN cis2901c.unit AS u ON r.unitId = u.unitId
				WHERE r.roId LIKE ? OR c.lastname LIKE ? OR c.firstname LIKE ? OR c.homephone LIKE ? OR c.cellphone LIKE ? OR u.year LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.vin;""";
		super.outerSearchQueryAppendix = " AND roId IN (";
		super.querySubStringIndices[0] = 13;
		super.querySubStringIndices[1] = 210;
		
		return packObjects(DbServices.searchForObject(this));
	}

	@Override
	public RepairOrder searchByPk(long primaryKey) {		// NOTE not used anywhere
		super.searchString = Long.toString(primaryKey);
		super.searchQuery = """
				SELECT r.roId, r.customerId,
				c.firstname, c.lastname, c.address, c.city, c.state, c.zipcode, c.homephone, c.cellphone, c.email,
				r.unitId, u.year, u.make, u.model, u.vin,
				r.createdTime, r.closedTime, r.cashiered
				FROM cis2901c.ro WHERE r.roId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
		
		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make this an abstract method in DbObjectSearchable
	private List<RepairOrder> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Customer
		List<RepairOrder> repairOrderSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
					// TODO use this shortened form of instanceof casting in the Tables
			repairOrderSearchResults.add(packObject(object));
		}
		
		return repairOrderSearchResults;
	}
	
	private RepairOrder packObject(DbObjectSearchable searchResult) {
		if (searchResult instanceof RepairOrder repairOrder) {
			return repairOrder;
		}
		throw new IllegalArgumentException("objects.RepairOrder.packObject(): dbObjectSearchable is not a RepairOrder");
	}
}
