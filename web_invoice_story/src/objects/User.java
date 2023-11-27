package objects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class User extends DbObject {
	
	// might not need this
	private long userId = -1;
	private long employeeId;
	private String userName;
	private String passwordHash;
	
	public User() {
	}
	
	public long getDbPk() {
		// return Employee object's primary key
		return getUserId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Employee object's primary key
		userId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Employee table's primary key field name
		return "userId";
	}
	
	@Override
	public String getTableName() {
		// get Employee table's name
		return "user";
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long employeeId) {
		this.userId = employeeId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getUserName() {
		return userName == null ? "" : userName;
	}

	public void setUserName(String city) {
		this.userName = city;
	}

	public String getPasswordHash() {
		return passwordHash == null ? "" : passwordHash;
	}

	public void setPasswordHash(String state) {
		this.passwordHash = state;
	}

	public Map<String, String> getDataMap() {
		
		// return helper data for saving and updating Employee objects
		Map<String, String> dataMap = new HashMap<>();
		if (userId != -1) {
			dataMap.put("userId", Long.toString(userId));
		}
		dataMap.put("employeeId", Long.toString(employeeId));
		dataMap.put("userName", userName);
		dataMap.put("passwordHash", passwordHash);

		return dataMap;
	}

//	protected static void populateEmployeeTable(Table table) {
//		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
//		new Employee().search("");
//	}
	
	@Override
	public List<User> search(String searchString) {
		// search for an Employee from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT user.employeeId, user.userName, user.passwordHash, user.userId
				FROM web_invoice_story.user
				WHERE user.userName LIKE ?;""";
		super.outerSearchQueryAppendix = " AND user.userId IN (";
		super.querySubStringIndices[0] = 7;
		super.querySubStringIndices[1] = 99;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public User searchByPk(long userId) {
		// search for an user from the database using a primary key
		super.searchString = Long.toString(employeeId);
		super.searchQuery = """
				SELECT user.employeeId, user.userName, user.passwordHash, user.userId
				FROM web_invoice_story.user
				WHERE user.userId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<User> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<User> userSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			userSearchResults.add(packObject(object));
		}
		
		return userSearchResults;
	}
	
	private User packObject(DbObject dbObject) {
		// helper to cast List of DbObject to List of Customer
			if (dbObject instanceof User user) {
				return user;
			}
			throw new IllegalArgumentException("objects.User.packObject(): dbObjectSearchable is not a User");
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeId, passwordHash, userId, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		return employeeId == other.employeeId && Objects.equals(passwordHash, other.passwordHash)
				&& userId == other.userId && Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "Employee [userId=" + userId + ", employeeId=" + employeeId + ", userName=" + userName + ", passwordHash=" + passwordHash + "]";
	}
}
