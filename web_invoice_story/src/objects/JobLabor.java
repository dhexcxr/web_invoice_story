package objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class JobLabor extends DbObjectSearchable implements DbObjectSavable {
	
	// joblabor Database Table
	
	private long jobLaborId = -1;
	private long jobId;
	private String technician;
	private String description;
	private BigDecimal hours = BigDecimal.valueOf(0);
	private BigDecimal laborRate = BigDecimal.valueOf(0);
	
	private Map<String, String> dataMap = new HashMap<>();
	
	public JobLabor() {
		this("", "", BigDecimal.valueOf(0), BigDecimal.valueOf(100));
	}

	public JobLabor(String technician, String description, BigDecimal hours, BigDecimal laborRate) {
		super();
		this.technician = technician;
		this.description = description;
		this.hours = hours;
		this.laborRate = laborRate;
	}

	public long getJobLaborId() {
		return jobLaborId;
	}

	public void setJobLaborId(long jobLaborId) {
		this.jobLaborId = jobLaborId;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getHours() {
		return hours;
	}

	public void setHours(BigDecimal hours) {
		this.hours = hours;
	}

	public BigDecimal getLaborRate() {
		return laborRate;
	}

	public void setLaborRate(BigDecimal laborRate) {
		this.laborRate = laborRate;
	}

	@Override
	public long getDbPk() {
		return getJobLaborId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		jobLaborId = dbPk;
	}

	@Override
	public String getPkName() {
		return "joblaborid";
	}

	@Override
	public String getTableName() {
		return "joblabor";
	}

	@Override
	public Map<String, String> getDataMap() {
		if (dataMap.isEmpty()) {
			if (jobLaborId != -1) {
				dataMap.put("joblaborid", Long.toString(jobLaborId));
			}
			dataMap.put("description", description);
			dataMap.put("hours", hours.toString());
			dataMap.put("hourrate", laborRate.toString());
			dataMap.put("technician", technician);
		}
		return dataMap;
	}

	@Override
	public List<JobLabor> search(String searchString) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public JobLabor searchByPk(long primaryKey) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	public List<JobLabor> searchJobLaborByRoId(long roId) {
		super.searchString = Long.toString(roId);
		super.searchQuery = "SELECT joblaborid, jobid, description, hours, hourrate, technician FROM web_invoice_story.joblabor WHERE jobid = ?;";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObjects(DbServices.searchForObjectsByPk(this));
	}
	
	private List<JobLabor> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of JobLabor
		List<JobLabor> jobLaborSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			if (object instanceof JobLabor jobLabor) {
				jobLaborSearchResults.add(jobLabor);
			}
		}
		
		return jobLaborSearchResults;
	}

}
