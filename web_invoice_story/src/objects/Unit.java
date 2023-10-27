package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import web_invoice_story.DbServices;

public class Unit extends DbObjectSearchable implements DbObjectSavable{
	
	// might not need this
	private long unitId = -1;
	private long customerId;
	private String make;
	private String model;
	private String modelName;
	private int year;
	private int mileage;
	private String color;
	private String vin;
	private String notes;
	
	private String owner;
	
	public Unit() {
	}
	
	public long getDbPk() {
		return getUnitId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		unitId = dbPk;
	}
	
	public String getPkName() {
		return "unitId";
	}
	
	public String getTableName() {
		return "unit";
	}

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getMake() {
		return make == null ? "" : make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model == null ? "" : model;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getModelName() {
		return modelName == null ? "" : modelName;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int modelYear) {
		this.year = modelYear;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public String getColor() {
		return color == null ? "" : color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getVin() {
		return vin == null ? "" : vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getNotes() {
		return notes == null ? "" : notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getOwner() {
		return owner == null ? "" : owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Override
	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter
		Map<String, String> dataMap = new HashMap<>();
		if (unitId != -1) {
			dataMap.put("unitId", Long.toString(unitId));
		}
		dataMap.put("customerId", Long.toString(customerId));
		dataMap.put("make", make);
		dataMap.put("model", model);
		dataMap.put("modelName", modelName);
		dataMap.put("year", Integer.toString(year));
		dataMap.put("mileage", Integer.toString(mileage));
		dataMap.put("color", color);
		dataMap.put("vin", vin);
		dataMap.put("notes", notes);
		
		return dataMap;
	}

	@Override
	public List<DbObjectSearchable> search(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
				c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId 
				WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;""";
		super.outerSearchQueryAppendix = " AND u.unitId IN (";
		super.querySubStringIndices[0] = 15;
		super.querySubStringIndices[1] = 128;
		
		return packObjects(DbServices.searchForObject(this));
	}

	@Override
	public DbObjectSearchable searchByPk(long primaryKey) {		// TODO NOTE actually looking at all of these, we could probably leave
		super.searchString = Long.toString(primaryKey);				// all the super. declarations in the constructor, maybe make a separate var for the searchByPk query,
		super.searchQuery = """
				SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
				c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId
				WHERE unitId = ?;""";
		super.outerSearchQueryAppendix = " AND u.unitId IN (";
		super.querySubStringIndices[0] = 15;					// and move these 2 search() function implementations into the DbObjectSearchable too
		super.querySubStringIndices[1] = 128;						// we just need to look at who calls what and where is a specific DbObjectSearchable expected
																	// and where can we give it the generic ? extends DbObjectSearchable
																	// I thought of this because if this function here returns a List<? extends DbObjectSearchable instead
		return DbServices.searchForObjectByPk(this);				// of a List<Unit> nothing seems to complain, though I didn't actually test anything
	}
	
	private List<DbObjectSearchable> packObjects(List<DbObjectSearchable> searchResults) {		// TODO NOTE look at how all of these are implemented
		// helper to cast List of DbObjectSearchable to List of Customer				// this could probably go into DbObjectSearchable
		List<DbObjectSearchable> unitSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			if (object instanceof Unit unit) {
				unitSearchResults.add(unit);
			}
		}
		
		return unitSearchResults;
	}
}
