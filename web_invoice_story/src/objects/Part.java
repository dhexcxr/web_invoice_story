package objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

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
		// empty default constructor
	}
	
	public long getDbPk() {
		// return Part object's primary key
		return getPartId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Part object's primary key
		partId = dbPk;
	}
	
	public String getPkName() {
		// get Part table's primary key field name
		return "partId";
	}
	
	public String getTableName() {
		// get Part table's name
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
		
		// return helper data for saving and updating Part objects
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

	@Override
	public List<Part> search(String searchString) {
		// search for a Part from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT part.partId, part.partNumber, part.supplier, part.category, part.description, part.notes, part.cost, part.retail, part.onHand
				FROM web_invoice_story.part
				WHERE part.partNumber LIKE ? OR part.supplier LIKE ? OR part.category LIKE ? OR part.description LIKE ?;""";
		super.outerSearchQueryAppendix = " AND part.partId IN (";
		super.querySubStringIndices[0] = 13;
		super.querySubStringIndices[1] = 87;

//		return packObjects(DbServices.searchForObject(this));
		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public Part searchByPk(long partNum) {
		// search for a Part from the database using a primary key
		super.searchString = Long.toString(partNum);
		super.searchQuery = """
				SELECT part.partId, part.partNumber, part.supplier, part.category, part.description, part.notes, part.cost, part.retail, part.onHand
				FROM web_invoice_story.part
				WHERE part.partId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	private List<Part> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Part
		List<Part> partSearchResults = new ArrayList<>();

		for (DbObjectSearchable object : searchResults) {
			partSearchResults.add(packObject(object));
		}
		return partSearchResults;
	}
	
	private Part packObject(DbObjectSearchable dbObjectSearchable) {
		// helper to cast DbObjectSearchable to Part
			if (dbObjectSearchable instanceof Part part) {
				return part;
			}
			throw new IllegalArgumentException("objects.Part.packObject(): dbObjectSearchable is not a Part");
	}

	@Override
	public String toString() {
		return "Part [partId=" + partId + ", partNumber=" + partNumber + ", supplier=" + supplier + ", category="
				+ category + ", description=" + description + ", notes=" + notes + ", cost=" + cost + ", retail="
				+ retail + ", onHand=" + onHand + "]";
	}
}
