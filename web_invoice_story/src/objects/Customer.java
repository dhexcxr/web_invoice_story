package objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class Customer extends DbObjectSearchable implements DbObjectSavable {
	
	// might not need this
	private long customerId = -1;
	private String firstName;
	private String lastName;
	private String address;
	private String city;
	private String state;
	private String zipCode;
	private String homePhone;
	private String workPhone;
	private String cellPhone;
	private String email;

	// this is now used here and in PartInvoiceEditorEventListener, and in PhoneNumberTextBoxModifyListener
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	public Customer() {
		// empty default constructor
	}
	
	public long getDbPk() {
		// return Customer object's primary key
		return getCustomerId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Customer object's primary key
		customerId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Customer table's primary key field name
		return "customerId";
	}
	
	@Override
	public String getTableName() {
		// get Customer table's name
		return "customer";
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName == null ? "" : firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName == null ? "" : lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address == null ? "" : address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city == null ? "" : city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state == null ? "" : state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode == null ? "" : zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getHomePhone() {
		return homePhone == null ? "" : homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone == null ? "" : workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}

	public String getCellPhone() {
		return cellPhone == null ? "" : cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		
		// return helper data for saving and updating Customer objects
		Map<String, String> dataMap = new HashMap<>();
		if (customerId != -1) {
			dataMap.put("customerId", Long.toString(customerId));
		}
		dataMap.put("firstName", firstName);
		dataMap.put("lastName", lastName);
		dataMap.put("address", address);
		dataMap.put("city", city);
		dataMap.put("state", state);
		dataMap.put("zipCode", zipCode);
		dataMap.put("homePhone", homePhone);
		dataMap.put("workPhone", workPhone);
		dataMap.put("cellPhone", cellPhone);
		dataMap.put("email", email);
				
		return dataMap;
	}

	protected static void populateCustomerTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		new Customer().search("");
	}
	
	public String setPhoneNumberFormat(String inputNumber) {
		// automatically add dashes to phone numbers in the correct places
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
	
	@Override
	public List<Customer> search(String searchString) {
		// search for a Customer from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT firstName, lastName, address, city, state, zipcode,
				homePhone, workPhone, cellPhone, email, customerId
				FROM web_invoice_story.customer
				WHERE firstName LIKE ? OR homePhone LIKE ? OR lastName LIKE ? OR workPhone LIKE ? OR address LIKE ? OR cellPhone LIKE ?
				OR city LIKE ? OR zipcode LIKE ? OR state LIKE ?;""";
		super.outerSearchQueryAppendix = " AND customerId IN (";
		super.querySubStringIndices[0] = 7;
		super.querySubStringIndices[1] = 99;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public Customer searchByPk(long customerId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(customerId);
		super.searchQuery = """
				SELECT firstName, lastName, address, city, state, zipcode, homePhone, workPhone, cellPhone, email, customerId
				FROM web_invoice_story.customer WHERE customerId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<Customer> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Customer
		List<Customer> customerSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			customerSearchResults.add(packObject(object));
		}
		
		return customerSearchResults;
	}
	
	private Customer packObject(DbObjectSearchable dbObjectSearchable) {
		// helper to cast List of DbObjectSearchable to List of Customer
			if (dbObjectSearchable instanceof Customer customer) {
				return customer;
			}
			throw new IllegalArgumentException("objects.Customer.packObject(): dbObjectSearchable is not a Customer");
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", firstName=" + firstName + ", lastName=" + lastName + ", address=" + address + ", city=" + city + ", state=" + state + ", zipCode=" + zipCode + ", homePhone=" + homePhone + ", workPhone=" + workPhone + ", cellPhone=" + cellPhone + ", email="
		+ email + "]";
	}
}
