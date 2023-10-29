package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class Job extends DbObjectSearchable implements DbObjectSavable {
	
	private long jobId = -1;
	private long roId;
	private	String jobName;
	private String complaints;
	private String resolution;
	private String reccomendations;
	
	private List<JobPart> jobParts = new ArrayList<>();
	private List<Part> parts = new ArrayList<>();
	// TODO change this to TableLineItems, probabaly make a new Object that extends InvoiceTableLineItem so we can override dataMap and searchQuery to provide correct DB tables 
	private List<JobLabor> labor;
	
	private Map<String, String> dataMap = new HashMap<>();

	public Job() {
		// TODO Auto-generated constructor stub
	}

	public Job(String jobName, String complaints, String resolution, String reccomendations, List<Part> parts,
			List<JobLabor> labor) {
		super();
		this.jobName = jobName;
		this.complaints = complaints;
		this.resolution = resolution;
		this.reccomendations = reccomendations;
		this.parts = parts;
		this.labor = labor;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public long getRoId() {
		return roId;
	}

	public void setRoId(long roId) {
		this.roId = roId;
	}

	public String getJobName() {
		return jobName == null ? "" : jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getComplaints() {
		return complaints == null ? "" : complaints;
	}

	public void setComplaints(String complaints) {
		this.complaints = complaints;
	}

	public String getResolution() {
		return resolution == null ? "" : resolution;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getReccomendations() {
		return reccomendations == null ? "" : reccomendations;
	}

	public void setReccomendations(String reccomendations) {
		this.reccomendations = reccomendations;
	}
	
	public List<Part> getParts() {		// TODO this is a Big F-n Kludge, maybe try to refactor the calls to this method to getJobParts and have
											// the calling methods handle it, or maybe this is more elegant
		if (jobParts == null) {
			parts = new ArrayList<>();
		} else {
			for (JobPart jobPart : jobParts) {
				if (jobPart.getPart() != null && !parts.contains(jobPart.getPart())) {
					parts.add(jobPart.getPart());
				}
			}
		}
		return parts;
	}

//	public void setParts(List<Part> parts) {
//		this.parts = parts;
//	}
	
	public void addPart(Part part) {
		if (part == null) {
			return;
		}
		if (!getParts().contains(part)) {
			addJobPart(new JobPart(part));
		}
	}
	
//	public void deletePart(Part part) {
//		this.parts.remove(part);
//	}

	public List<JobPart> getJobParts() {
		return jobParts;
	}

	public void setJobParts(List<JobPart> jobParts) {
		this.jobParts = jobParts;
	}
	
	public void addJobPart(JobPart jobPart) {
		if (!jobParts.contains(jobPart)) {
			jobParts.add(jobPart);
		}
	}

	public List<JobLabor> getLabor() {
		if (labor == null) {
			labor = new ArrayList<>();
		}
		return labor;
	}

	public void setLabor(List<JobLabor> labor) {
		this.labor = labor;
	}
	
	public void addLabor(JobLabor labor) {
		if (labor == null) {
			return;
		}
		if (!getLabor().contains(labor)) {
			this.labor.add(labor);
		}
	}
	
	public void deleteLabor(JobLabor labor) {
		this.labor.remove(labor);
	}

	@Override
	public long getDbPk() {
		return getJobId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		jobId = dbPk;
	}

	@Override
	public String getPkName() {
		return "jobid";
	}

	@Override
	public String getTableName() {
		return "job";
	}

	@Override
	public Map<String, String> getDataMap() {
		if (dataMap.isEmpty() ) {
			if (jobId != -1) {
				dataMap.put("jobid", Long.toString(jobId));
			}
			dataMap.put("name", jobName);
			dataMap.put("complaint", complaints);
			dataMap.put("resolution", resolution);
			dataMap.put("recommendations", reccomendations);
		}
		return dataMap;
	}
	
	@Override
	public List<Job> search(String searchString) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Job searchByPk(long primaryKey) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	public List<Job> searchForJobsByRoId(long roId) {
		super.searchString = Long.toString(roId);
		super.searchQuery = "SELECT jobid, roid, name, complaint, resolution, recommendations FROM web_invoice_story.job WHERE roid = ?;";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
		
		return packObjects(DbServices.searchForObjectsByPk(this));
	}
	
	private List<Job> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Job
		List<Job> jobSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			if (object instanceof Job job) {
				jobSearchResults.add(job);
			}
		}
		
		return jobSearchResults;
	}
	
}
