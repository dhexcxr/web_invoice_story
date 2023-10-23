package objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
	
	public JobLabor(long jobId) {
		super.searchString = Long.toString(jobId);
		super.searchQuery = "SELECT joblaborid, jobid, description, hours, hourrate, technician FROM cis2901c.joblabor WHERE jobid = ?;";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
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

}
