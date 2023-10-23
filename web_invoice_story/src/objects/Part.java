package objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Part extends DbObjectSearchable implements DbObjectSavable{
	private long partId = -1;
	private String partNumber;
	private String supplier;
	private String category;
	private String description;
	private String notes;
	private BigDecimal cost = new BigDecimal(0);
	private BigDecimal retail = new BigDecimal(0);
	private int onHand = -1;
	
	public Part() {
	}
	
	public Part(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part  
				WHERE partNumber LIKE ? OR supplier LIKE ? OR category LIKE ? OR description LIKE ?;""";
		super.outerSearchQueryAppendix = " AND partId IN (";
		super.querySubStringIndices[0] = 13;
		super.querySubStringIndices[1] = 87;
	}
	
	public Part(long partId) {
		super.searchString = Long.toString(partId);
		super.searchQuery = """
				SELECT partId, partNumber, supplier, category, description, notes, cost, retail, onHand FROM cis2901c.part  
				WHERE partId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
	}
	
	public long getDbPk() {
		return getPartId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		partId = dbPk;
	}
	
	public String getPkName() {
		return "partId";
	}
	
	public String getTableName() {
		return "part";
	}
	
	public long getPartId() {
		return partId;
	}
	
	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getPartNumber() {
		return partNumber == null ? "error" : partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getSupplier() {
		return supplier == null ? "" : supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getCategory() {
		return category == null ? "" : category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description == null ? "error" : description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes == null ? "" : notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getCost() {
		return cost == null ? BigDecimal.valueOf(0) : cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost == null ? BigDecimal.valueOf(0) : cost;
	}

	public BigDecimal getRetail() {
		return retail == null ? BigDecimal.valueOf(0) : retail;
	}

	public void setRetail(BigDecimal retail) {
		this.retail = retail == null ? BigDecimal.valueOf(0) : retail;
	}

	public int getOnHand() {
		return onHand == -1 ? 0 : onHand;
	}

	public void setOnHand(int onHand) {
		this.onHand = onHand;
	}

	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter
		Map<String, String> dataMap = new HashMap<>();
		
		if (partId != -1) {
			dataMap.put("partId", Long.toString(partId));
		}
		dataMap.put("partNumber", partNumber);
		dataMap.put("supplier", supplier);
		dataMap.put("category", category);
		dataMap.put("description", description);
		dataMap.put("notes", notes);
		dataMap.put("cost", cost.toString());
		dataMap.put("retail", retail.toString());
		dataMap.put("onHand", Integer.toString(onHand));
		
		return dataMap;
	}
}
