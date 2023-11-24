package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class StoreLocation extends DbObjectSearchable {
	
	// might not need this
	private long locationId = -1;
	private String nameStore;
	private String addressStore;
	private String cityStore;
	private String stateStore;
	private String zipCodeStore;
	private String phoneStore;

	// this is now used here and in PartInvoiceEditorEventListener, and in PhoneNumberTextBoxModifyListener
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	public StoreLocation() {
		// empty default constructor
	}
	
	// TODO maybe make a method to make an All Locations objects
	public StoreLocation(long locationId, String name) {
		this.setStoreLocationId(locationId);
		this.setName(name);
	}
	
	public long getDbPk() {
		// return Customer object's primary key
		return getStoreLocationId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Customer object's primary key
		locationId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Customer table's primary key field name
		return "locationId";
	}
	
	@Override
	public String getTableName() {
		// get Customer table's name
		return "storeLocation";
	}
	
	public long getStoreLocationId() {
		return locationId;
	}

	public void setStoreLocationId(long locationId) {
		this.locationId = locationId;
	}

	public String getName() {
		return nameStore == null ? "" : nameStore;
	}

	public void setName(String nameStore) {
		this.nameStore = nameStore;
	}

	public String getAddress() {
		return addressStore == null ? "" : addressStore;
	}

	public void setAddress(String address) {
		this.addressStore = address;
	}

	public String getCity() {
		return cityStore == null ? "" : cityStore;
	}

	public void setCity(String city) {
		this.cityStore = city;
	}

	public String getState() {
		return stateStore == null ? "" : stateStore;
	}

	public void setState(String state) {
		this.stateStore = state;
	}

	public String getZipCode() {
		return zipCodeStore == null ? "" : zipCodeStore;
	}

	public void setZipCode(String zipCode) {
		this.zipCodeStore = zipCode;
	}
	
	public String getPhoneStore() {
		return phoneStore == null ? "" : phoneStore;
	}

	public void setPhoneStore(String phoneStore) {
		this.phoneStore = phoneStore;
	}

	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		
		// return helper data for saving and updating Customer objects
		Map<String, String> dataMap = new HashMap<>();
		if (locationId != -1) {
			dataMap.put("customerId", Long.toString(locationId));
		}
		dataMap.put("name", nameStore);
		dataMap.put("addresse", addressStore);
		dataMap.put("city", cityStore);
		dataMap.put("state", stateStore);
		dataMap.put("zipCode", zipCodeStore);
		dataMap.put("phone", phoneStore);
		return dataMap;
	}

	protected static void populateCustomerTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		new StoreLocation().search("");
	}
	
	public String setPhoneNumberFormat(String inputNumber) {
		// automatically add dashes to phoneStore numbers in the correct places
		if (inputNumber != null) {
			inputNumber = (inputNumber.replaceAll(NOT_NUMBERS, ""));
			if (inputNumber.length() >= 7 && inputNumber.length() < 10) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d+)", SEVEN_DIGIT_PHONE));
			} else if (inputNumber.length() < 11) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", TEN_DIGIT_PHONE));
			} else if (inputNumber.length() == 11) {
				inputNumber = (inputNumber.replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (inputNumber.length() == 12) {
				inputNumber = (inputNumber.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (inputNumber.length() == 13) {
				inputNumber = (inputNumber.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			}
		}
		return inputNumber;
	}
	
	// TODO big idea, make all these search methods static, so we don't need to instantiate a new Object everytime we search
		// saving wouldn't be static, but I think searching can be
	public List<StoreLocation> getAllStoreLocations() {
		super.searchString = "";
		super.searchQuery = """
				SELECT storeLocation.locationId, storeLocation.name, storeLocation.address, storeLocation.city, storeLocation.state, storeLocation.zipcode, storeLocation.phone
				FROM web_invoice_story.storeLocation""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;
		
		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public List<StoreLocation> search(String searchString) {
		// search for a Customer from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT storeLocation.name, storeLocation.address, storeLocation.city, storeLocation.state, storeLocation.zipcode, storeLocation.phone
				FROM web_invoice_story.storeLocation
				WHERE storeLocation.name LIKE ? OR storeLocation.address LIKE ? OR storeLocation.city LIKE ? OR storeLocation.state LIKE ? OR storeLocation.zipcode LIKE ? OR storeLocation.phone LIKE ?""";
		super.outerSearchQueryAppendix = " AND locationId IN (";
		super.querySubStringIndices[0] = 7;
		super.querySubStringIndices[1] = 99;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public StoreLocation searchByPk(long locationId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(locationId);
		super.searchQuery = """
				SELECT storeLocation.name, storeLocation.address, storeLocation.city, storeLocation.state, storeLocation.zipcode, storeLocation.phone
				FROM web_invoice_story.storeLocation
				WHERE storeLocation.locationId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<StoreLocation> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Customer
		List<StoreLocation> storeLocationSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			storeLocationSearchResults.add(packObject(object));
		}
		
		return storeLocationSearchResults;
	}
	
	private StoreLocation packObject(DbObjectSearchable dbObjectSearchable) {
		// helper to cast List of DbObjectSearchable to List of Customer
			if (dbObjectSearchable instanceof StoreLocation storeLocation) {
				return storeLocation;
			}
			throw new IllegalArgumentException("objects.Customer.packObject(): dbObjectSearchable is not a StoreLocation");
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + locationId + ", firstName=" + nameStore + ", addressStore=" + addressStore
				+ ", cityStore=" + cityStore + ", stateStore=" + stateStore + ", zipCodeStore=" + zipCodeStore + ", phone=" + phoneStore + "]";
	}
}
