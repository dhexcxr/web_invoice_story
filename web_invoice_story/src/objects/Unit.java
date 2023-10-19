package objects;

import java.util.HashMap;
import java.util.Map;

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
	
	public Unit(String searchString) {
		super.searchString = searchString;
		super.searchQuery = """
				SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
				c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId 
				WHERE c.firstName LIKE ? OR c.lastName LIKE ? OR u.vin LIKE ? OR u.make LIKE ? OR u.model LIKE ? OR u.year LIKE ?;""";
		super.outerSearchQueryAppendix = " AND u.unitId IN (";
		super.querySubStringIndecies[0] = 15;
		super.querySubStringIndecies[1] = 128;
	}
	
	public Unit(Long unitId) {
		super.searchString = Long.toString(unitId);
		super.searchQuery = """
				SELECT u.unitId, u.customerId, u.make, u.model, u.modelname, u.year, u.mileage, u.color, u.vin, u.notes, 
				c.lastName, c.firstName FROM cis2901c.unit AS u JOIN cis2901c.customer AS c ON u.customerId = c.customerId
				WHERE unitid = ?;""";
		super.outerSearchQueryAppendix = " AND u.unitId IN (";
		super.querySubStringIndecies[0] = 15;
		super.querySubStringIndecies[1] = 128;
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
}
