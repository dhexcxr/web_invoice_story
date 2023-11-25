package objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class Employee extends DbObjectWithLocation {
	
	// might not need this
	private long employeeId = -1;
	private String firstName;
	private String lastName;
	private String jobTitle;
	private BigDecimal payRate;

	// this is now used here and in PartInvoiceEditorEventListener, and in PhoneNumberTextBoxModifyListener
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	public Employee() {
		this.setLocationMatters(true);
	}
	
	public long getDbPk() {
		// return Employee object's primary key
		return getEmployeeId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Employee object's primary key
		employeeId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Employee table's primary key field name
		return "employeeId";
	}
	
	@Override
	public String getTableName() {
		// get Employee table's name
		return "employee";
	}
	
	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
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
	
	public String getFullName() {
		String fullName = lastName;
		if (firstName != null && firstName.length() != 0) {
			fullName = fullName + ", " + firstName;
		}
		return fullName;
	}

	public String getJobTitle() {
		return jobTitle == null ? "" : jobTitle;
	}

	public void setJobTitle(String address) {
		this.jobTitle = address;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public Map<String, String> getDataMap() {
		
		// return helper data for saving and updating Employee objects
		Map<String, String> dataMap = new HashMap<>();
		if (employeeId != -1) {
			dataMap.put("employeeId", Long.toString(getEmployeeId()));
		}
		dataMap.put("locationId", Long.toString(getLocationId()));
		dataMap.put("firstName", getFirstName());
		dataMap.put("lastName", getLastName());
		dataMap.put("jobTitle", getJobTitle());
		dataMap.put("payRate", getPayRate().toString());

		return dataMap;
	}

//	protected static void populateEmployeeTable(Table table) {
//		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
//		new Employee().search("");
//	}
	
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
	public List<Employee> search(String searchString) {
		// search for an Employee from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT employee.firstName, employee.lastName, employee.jobTitle, employee.payRate, employee.employeeId, employee.locationId
				FROM web_invoice_story.employee
				WHERE employee.firstName LIKE ? OR employee.lastName LIKE ? OR employee.jobTitle LIKE ? OR employee.userName LIKE ?;""";
		super.outerSearchQueryAppendix = " AND employee.employeeId IN (";
		super.querySubStringIndices[0] = 7;
		super.querySubStringIndices[1] = 99;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public Employee searchByPk(long employeeId) {
		// search for an Employee from the database using a primary key
		super.searchString = Long.toString(employeeId);
		super.searchQuery = """
				SELECT employee.firstName, employee.lastName, employee.jobTitle, employee.payRate, employee.employeeId, employee.locationId
				FROM web_invoice_story.employee
				WHERE employee.employeeId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<Employee> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<Employee> employeeSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			employeeSearchResults.add(packObject(object));
		}
		
		return employeeSearchResults;
	}
	
	private Employee packObject(DbObject dbObject) {
		// helper to cast List of DbObject to List of Customer
			if (dbObject instanceof Employee employee) {
				return employee;
			}
			throw new IllegalArgumentException("objects.Employee.packObject(): dbObjectSearchable is not a Employee");
	}

	@Override
	public String toString() {
		return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", jobTitle=" + jobTitle + ", payRate=" + payRate + "]";
	}
}
