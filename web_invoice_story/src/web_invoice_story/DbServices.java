package web_invoice_story;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import gui.Splash;
import objects.Account;
import objects.AccountTransaction;
import objects.Customer;
import objects.DbObjectSavable;
import objects.DbObjectSearchable;
import objects.Employee;
import objects.Invoice;
import objects.InvoicePart;
import objects.Job;
import objects.JobLabor;
import objects.JobPart;
import objects.Part;
import objects.Project;
import objects.ProjectSite;
import objects.StoreLocation;
import objects.User;

public class DbServices {
	// TODO add status icon somewhere to show when we're connected to DB
		// set icon in initial connectToDb method call
		// maybe check isConnected before every getDbConnection call, and set icon then too
	
	private static Connection mainDbConnection = null;
	private static final int SQL_FAILURE = -1;
	private static final int MAX_RESULTS = 255;		// max search return results
	
	private DbServices() {
	}
	
	// START General DB methods
	public static boolean isConnected() {
		boolean isConnected = false;
		try {
			isConnected = mainDbConnection.isValid(5);
		} catch (SQLException e) {
			Application.getLogger().log(Level.FINER, "DbServices.isConnected: mainDbConnection SQL Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Application.getLogger().log(Level.SEVERE, "DbServices.isConnected: mainDbConnection Null Pointer Exception");
			e.printStackTrace();
		}
		return isConnected;
	}
	
	public static Connection getDbConnection() {
		// global database connection
		return mainDbConnection;		
	}
	
	public static void connectToDb() {
		// TODO decide if I want to setAutoCommit to false before returning Connection, every call would have to connection.commit
			// or rollback if an exception is caught
		// TODO offer some customization options in application like actual DB options
		Application.getLogger().log(Level.INFO, "Connecting to Database");
		String url = Application.getSettings().getDatabase();
		String user = "TestUser";
		String pass = "test";
		
		int i = 0;
		while (!isConnected() && i < 3) {		// try to connect 3 times
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				mainDbConnection = DriverManager.getConnection(url, user, pass);
			} catch (SQLException e) {
				Application.getLogger().log(Level.SEVERE, "Error connecting to database");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		if (!isConnected()) {
			Application.getLogger().log(Level.SEVERE, "DB Connection error dialog box");
			MessageBox dbConnectionError = new MessageBox(new Shell(), SWT.ERROR);
			dbConnectionError.setText("DB Error");
			dbConnectionError.setMessage("Unable to connect to database...");
			dbConnectionError.open();
		}
	}
	
	public static void disconnectFromDb() {
		// clean up, close database connection
		try {
			mainDbConnection.close();
		} catch (SQLException e) {
			Application.getLogger().log(Level.FINER, "DbServices.disconnectFromDb: mainDbConnection SQL Exception");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Application.getLogger().log(Level.SEVERE, "DbServices.disconnectFromDb: mainDbConnection Null Pointer Exception");
			e.printStackTrace();
		}
	}
	// END General DB methods
	
	// START Object Save/Update DB methods
	public static int deleteObject(DbObjectSavable dbObject) {
		if (!isConnected()) {
			connectToDb();
		}
		if (dbObject == null) {
			return 0;		// no object passed in, so no object deleted
		}
		StringBuilder queryString = new StringBuilder();		// TODO this query (below) is potentially dangerous, i'm not sanitizing getDbPk() with PrepareStatement
		queryString.append("DELETE FROM web_invoice_story." + dbObject.getTableName() + " WHERE " + dbObject.getPkName() + " = " + dbObject.getDbPk());
		return sendQueryToDb(queryString);
	}
	
	public static void saveObject(DbObjectSavable dbObject) {
		// TODO have saveObject also return updateCount from sendQueryToDb
		if (!isConnected()) {
			connectToDb();
		}

		if (dbObject == null) {
			return;
		}
		StringBuilder queryString = new StringBuilder();
		boolean creatingNewObject = (dbObject.getDbPk() == -1);
		// start building query string
		if (creatingNewObject) {
			queryString.append("INSERT INTO web_invoice_story." + dbObject.getTableName() + " () VALUES ();" );
		} else {
			queryString.append("UPDATE web_invoice_story." + dbObject.getTableName() + " SET WHERE " + dbObject.getPkName() + " = " + dbObject.getDbPk() + ";");
		}
		// check which object data is present and add those fields to query string
		List<String> dbFields = insertFieldsIntoQuery(queryString, dbObject, creatingNewObject);

		// build query Statement and fill parameters
		sendQueryToDb(queryString, dbFields);
		
		if (creatingNewObject) {
			// get latest dbObject.Table.dbPk, set dbObject.setPk
			dbObject.setDbPk(getLastSavedDbObjectPk(dbObject));
		}
		
		if (dbObject instanceof Invoice) {		// TODO turn off auto commit until all invoice queries have completed
			saveInvoiceLineItems(dbObject);
		} else if (dbObject instanceof Project thisRo) {
			if (thisRo.getDbPk() == -1 ) {
				thisRo.setDbPk(getLastRoNum());
			}
			saveRoJobs(thisRo);
			if (thisRo.getClosedDate() != null) {
				sellRoPartsOutOfInventory(thisRo.getProjectId());
			}
		} else if (dbObject instanceof Job) {
			if (dbObject.getDbPk() == -1 ) {
				dbObject.setDbPk(getLastJobId());
			}
			saveJobParts(dbObject);
			saveJobLabor(dbObject);
		}
	}
	
	private static long getLastSavedDbObjectPk(DbObjectSavable dbObject) {
		// get primary key of last object saved into dbObject's database table
		ResultSet results = null;
		try (PreparedStatement lastDbObjectPkStatement = DbServices.getDbConnection().prepareStatement(
									"SELECT MAX(" + dbObject.getPkName() + ") FROM web_invoice_story." + dbObject.getTableName() + ";")) {
			results = lastDbObjectPkStatement.executeQuery();
			while (results.next()) {
				return results.getLong(1);		// we should always return here
			}
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: getLastSavedDbObjectPk");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}

	private static int getLastInvoiceNum() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(invoicenum) FROM web_invoice_story.invoice;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: getLastInvoiceNum");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}

	private static int getLastRoNum() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(projectId) FROM web_invoice_story.project;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: getLastRoNum");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}

	private static int getLastJobId() {
		ResultSet results = null;
		try (PreparedStatement lastInvoiceNumStatement = DbServices.getDbConnection().prepareStatement("SELECT MAX(jobid) FROM web_invoice_story.job;")) {
			results = lastInvoiceNumStatement.executeQuery();
			while (results.next()) {
				return results.getInt(1);		// we should always return here
			}
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: getLastJobId");
			e.printStackTrace();
		}
		// if we get here something went wrong
		return SQL_FAILURE;
	}

	private static void saveInvoiceLineItems(DbObjectSavable dbObject) {
		long invoiceNumber = getLastSavedDbObjectPk(dbObject);	// NOTE it looks like I set the Invoice PK right before I call this
		// insert individual part invoice line items into invoicepart table		// we probably don;t need this func call
		// update onHand of part table
		Part[] invoiceLineParts = ((Invoice) dbObject).getParts();
		for (Part invoiceLinePart : invoiceLineParts) {
			if (invoiceLinePart == null) {
				break;
			}
			InvoicePart invoicePart = new InvoicePart(invoiceLinePart);
			invoicePart.getDataMap().put("invoicenum", Long.toString(invoiceNumber));
			saveObject(invoicePart);	// TODO this needs to be invoicePart.save()
		}
		sellInvoicePartsOutOfInventory(invoiceNumber);
	}
	
	private static void saveRoJobs(DbObjectSavable dbObject ) {
		long roNum = dbObject.getDbPk();
		if (roNum == -1) {
			roNum = getLastRoNum();
		}
		List<Job> roJobs = ((Project) dbObject).getJobs();
		for (Job job : roJobs) {
			job.getDataMap().put("projectId", Long.toString(roNum));
			DbServices.saveObject(job);
		}
	}
	
	private static void saveJobParts(DbObjectSavable dbObject) {
		long jobId = dbObject.getDbPk();
		if (jobId == -1) {
			jobId = getLastJobId();
		}
		List<JobPart> jobParts  = ((Job) dbObject).getJobParts();
		for (JobPart jobPart : jobParts) {
			jobPart.getDataMap().put("jobid", Long.toString(jobId));
			DbServices.saveObject(jobPart);
		}
	}
	
	private static void saveJobLabor(DbObjectSavable dbObject) {
		long jobId = dbObject.getDbPk();
		if (jobId == -1) {
			jobId = getLastJobId();
		}
		List<JobLabor> jobLabor = ((Job) dbObject).getLabor();
		for (JobLabor labor : jobLabor) {
			labor.getDataMap().put("jobid", Long.toString(jobId));
			DbServices.saveObject(labor);
		}
	}
	
	private static void sellInvoicePartsOutOfInventory(long invoiceNumber) {
		// update onHand of each part on invoice [invoiceNumber]
			// TODO add prefix to these database fields, see if I can combine with sellRoPartsOutOfInventory() below
		// update onHand of each part on invoice [invoiceNumber]
		StringBuilder query = new StringBuilder("UPDATE part p JOIN invoicelineitem ip ON p.partid = ip.partid "
				+ "SET p.onhand = p.onhand - ip.quantity WHERE ip.invoicenum = ?;");
		sendQueryToDb(query, new ArrayList<>(List.of(Long.toString(invoiceNumber))));
		// TODO these need to go somewhere else, probably in InvoicePart
		// TODO alos, these need to have the tables and fields corrected
	}
	
	private static void sellRoPartsOutOfInventory(long roNumber) {
		StringBuilder query = new StringBuilder("UPDATE web_invoice_story.part p JOIN web_invoice_story.jobpart jp ON p.partid = jp.partid "
				+ "JOIN web_invoice_story.job j ON jp.jobid = j.jobid "
				+ "SET p.onhand = p.onhand - jp.quantity WHERE j.projectId = ?;");
		sendQueryToDb(query, new ArrayList<>(List.of(Long.toString(roNumber))));
		// TODO these need to go somewhere else, probably in JobPart 
	}
	
	private static int sendQueryToDb(StringBuilder queryString) {
		return sendQueryToDb(queryString, new ArrayList<>());
	}

	private static int sendQueryToDb(StringBuilder queryString, List<String> dbFields) {
		// used when saving new objects or updating existing objects
		int updateCount = 0;
		String statementString = queryString.toString();
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(statementString)) {
			int parameterIndex = 1;
			for(String fieldData : dbFields) {		// set query values
				statement.setString(parameterIndex, fieldData);
				parameterIndex++;
			}
			statementString = statement.toString();
			statement.execute();		// execute save or update statement 
			updateCount = statement.getUpdateCount();
			Application.getLogger().log(Level.INFO, "SQL Query: {0}\nUpdate Count: {1}", new Object[] {statementString, updateCount});
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: {0}", statementString);
			e.printStackTrace();
		}
		return updateCount;		// return number of database table rows that were saved or updated
	}
	
	private static List<String> insertFieldsIntoQuery(StringBuilder queryString, DbObjectSavable dbObject, boolean creatingNewObject) {
		// insert into the SQL query only the fields where we have data we need to save 
		List<String> results = new ArrayList<>();
		for (Map.Entry<String, String> entry : dbObject.getDataMap().entrySet()) {
			if (entry.getValue() != null) {
				if (creatingNewObject) {
					insertQueryHelper(queryString, entry.getKey(), "?");
				} else {
					updateQueryHelper(queryString, entry.getKey(), "?");
				}
				results.add(entry.getValue().trim());		// TODO if we're always saving with .trim() there will be no spaces, trim() isn't necessary here
			}
		}
		return results;
	}
	
	private static void updateQueryHelper(StringBuilder queryString, String columnName, String data) {
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.lastIndexOf("WHERE");	// find where to insert fields and data
		if (queryString.charAt(columnInsertionPoint - 2) == '?') {		// see if there are already other data
			column.append(", ");											// and we need to add a comma
		}
		column.append(columnName + " = " + data + " ");
		queryString.insert(columnInsertionPoint, column);		// insert into query
	}
	
	private static void insertQueryHelper(StringBuilder queryString, String columnName, String data) {
		StringBuilder column = new StringBuilder();
		int columnInsertionPoint = queryString.indexOf(")");		// find where to insert field
		if (queryString.charAt(columnInsertionPoint - 1) != '(') {		// see if there are already other data
			column.append(", ");											// and we need to add a comma
		}
		column.append(columnName);
		queryString.insert(columnInsertionPoint, column);
		
		StringBuilder field = new StringBuilder();		// find where to insert data
		int fieldInsertionPoint = queryString.lastIndexOf(")");
		if (queryString.charAt(fieldInsertionPoint - 1) != '(') {		// see if there are already other data
			field.append(", ");												// and we need to add a comma
		}
		field.append(data);
		queryString.insert(fieldInsertionPoint, field);
	}
	// END Object Save/Update DB methods

	private static int deleteObjectsByPk(String tableName, String primaryKeyName, Long primaryKey) {
		int updateCount = 0;
		if (!isConnected()) {
			connectToDb();
		}
		String deleteQuery = "DELETE FROM " + tableName + " WHERE " + primaryKeyName + " = ?;";
		
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(deleteQuery)) {
			statement.setLong(1, primaryKey);
			deleteQuery = statement.toString();
			statement.execute();
			updateCount = statement.getUpdateCount();
			Application.getLogger().log(Level.INFO, "Delete from {0}. Count: {1}", new Object[] {tableName, updateCount});
		} catch (SQLException e) {
			// TODO print deleteQuery or statement
			Application.getLogger().log(Level.SEVERE, "SQL Error: {0}", deleteQuery);
			e.printStackTrace();
		}
		return updateCount;
	}

	public static int deleteDetailsFromRo(Map<String, List<Long>> detailsToDelete) {
		int updateCount = 0;
		for (Map.Entry<String, List<Long>> entry : detailsToDelete.entrySet()) {
			for (Long primaryKey : entry.getValue()) {
				updateCount += deleteObjectsByPk(entry.getKey(), entry.getKey() + "id", primaryKey);
			}
		}
		
		return updateCount;
	}
	
	public static String[] sanitizer(String searchQuery) {
		// simple regex to remove chars i don't want to search for
			// !!!! this is not to be taken as SQL Injection protection
		return searchQuery.replaceAll("[^a-zA-Z0-9 %'-]", "").split(" ");
	}
	
	public static DbObjectSearchable searchForObjectByPk(DbObjectSearchable object) {
		return searchForObjectsByPk(object).get(0);
	}
	
	// TODO rename to searchForManyObjectsByPk
	public static List<DbObjectSearchable> searchForObjectsByPk(DbObjectSearchable object) {
		if (!isConnected()) {
			connectToDb();
		}
		
		List<DbObjectSearchable> results = new ArrayList<>(1);		// TODO we can move this into each DbSearchableObjects in our application, make an object.getSelectedIdQuery()
											// this will allow us to make a polymorphic select by Primary Key search - MOSTLY DONE
		String queryString = object.getSearchQuery();
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString)) {
			statement.setLong(1, Long.parseLong(object.getSearchString()));
			queryString = statement.toString();	// fill queryString with completed query for debugging 
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object);
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: {0}", queryString);
			e.printStackTrace();
		}
		return results;	
	}
	
	// START Object Search/Build DB methods
	public static List<DbObjectSearchable> searchForObject(DbObjectSearchable object) {			// TODO rename to searchByQueryString
		if (object.getWordsFromSearchString().length <= 0) {		// if there is no searchString set in object
			throw new IllegalArgumentException("ERROR - DbServices.searchForObject(object): no search string in object");
		}
		if (!isConnected()) {
			connectToDb();
		}
		
		List<DbObjectSearchable> results = new ArrayList<>(1);
		String queryString = object.getSearchQuery();
		try (PreparedStatement statement = DbServices.getDbConnection().prepareStatement(queryString)) {
			statement.setMaxRows(MAX_RESULTS);
			fillStatementParameters(statement, object);
			queryString = statement.toString();
			ResultSet queryResultSet = statement.executeQuery();
			results = buildFoundObjects(queryResultSet, object);
		} catch (SQLException e) {
			Application.getLogger().log(Level.SEVERE, "SQL Error: {0}", queryString);
			e.printStackTrace();
		}
//		return results;		// TODO make sure everything that calls this handles the null return condition
		return results;
	}
	
	private static void fillStatementParameters(PreparedStatement statement, DbObjectSearchable object) throws SQLException {
		// fill search fields in queryString
		ParameterMetaData parameterMetaData = statement.getParameterMetaData();
		int parameterCount = parameterMetaData.getParameterCount();
		int queryWord = object.getWordsFromSearchString().length - 1;
		for (int j = 1; j <= parameterCount; j++) {
			// each word in searchString is surrounded by % SQL wildcard so that we match on any substring that user entered
			statement.setString(j, "%" + object.getWordsFromSearchString()[queryWord] + "%");
			int parametersPerQuery = parameterCount / object.getWordsFromSearchString().length; 
			if (j % parametersPerQuery == 0) {
				queryWord--;
			}
		}
	}
	
	private static List<DbObjectSearchable> buildFoundObjects(ResultSet queryResultSet, DbObjectSearchable object) throws SQLException {
		// build correct object out of the SQL query ResultSet
		List<DbObjectSearchable> results = new ArrayList<>();
		if (object instanceof Customer) {
			results = buildCustomers(queryResultSet);		// TODO push buildObjects(queryResultSet) up to superclass
		} else if (object instanceof Employee) {
			results = buildEmployees(queryResultSet);
		} else if (object instanceof User) {
			results = buildUsers(queryResultSet);
		} else if (object instanceof StoreLocation) {
			results = buildStoreLocations(queryResultSet);
		} else if (object instanceof Part) {
			results = buildParts(queryResultSet);
		} else if (object instanceof ProjectSite) {
			results = buildProjectSites(queryResultSet);
		} else if (object instanceof Invoice) {
			results = buildInvoices(queryResultSet);
		} else if (object instanceof Project) {
			results = buildRepairOrders(queryResultSet);
		} else if (object instanceof Job) {
			results = buildJobs(queryResultSet);
		} else if (object instanceof JobLabor) {
			results = buildJobLabors(queryResultSet);
		} else if (object instanceof JobPart) {
			results = buildJobParts(queryResultSet);
		} else if (object instanceof Account) {
			results = buildAccounts(queryResultSet);
		} else if (object instanceof AccountTransaction) {
			results = buildAccountTransactions(queryResultSet);
		}
		return results;
	}
	

	// TODO move these builders into the DbObjectSearchable, probably as a static method (as I am thinking of making all the search methods)
		// then i can just pass the results to the static method without doing a bunch of instanceof as in buildFoundObjects()
		// e.g. buildFoundObjects would just need to have one line: object.buildObjects(resultSet);
	private static List<DbObjectSearchable> buildCustomers(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Customer customer = new Customer();
			customer.setFirstName(queryResultSet.getString(1));
			customer.setLastName(queryResultSet.getString(2));
			customer.setAddress(queryResultSet.getString(3));
			customer.setCity(queryResultSet.getString(4));
			customer.setState(queryResultSet.getString(5));			
			customer.setZipCode(queryResultSet.getString(6));
			customer.setHomePhone(queryResultSet.getString(7));
			customer.setWorkPhone(queryResultSet.getString(8));
			customer.setCellPhone(queryResultSet.getString(9));
			customer.setEmail(queryResultSet.getString(10));
			customer.setCustomerId(queryResultSet.getLong(11));
			customer.setLocationId(queryResultSet.getLong(12));

			results.add(i, customer);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildEmployees(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Employee employee = new Employee();
			employee.setFirstName(queryResultSet.getString(1));
			employee.setLastName(queryResultSet.getString(2));
			employee.setJobTitle(queryResultSet.getString(3));
			employee.setPayRate(queryResultSet.getBigDecimal(4));
			employee.setEmployeeId(queryResultSet.getLong(5));
			employee.setLocationId(queryResultSet.getLong(6));

			results.add(i, employee);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildUsers(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			User employee = new User();
			employee.setEmployeeId(queryResultSet.getLong(1));
			employee.setUserName(queryResultSet.getString(2));
			employee.setPasswordHash(queryResultSet.getString(3));
			employee.setUserId(queryResultSet.getLong(4));

			results.add(i, employee);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildStoreLocations(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			StoreLocation storeLocation = new StoreLocation();
			storeLocation.setStoreLocationId(queryResultSet.getLong(1));
			storeLocation.setName(queryResultSet.getString(2));
			storeLocation.setAddress(queryResultSet.getString(3));
			storeLocation.setCity(queryResultSet.getString(4));
			storeLocation.setState(queryResultSet.getString(5));			
			storeLocation.setZipCode(queryResultSet.getString(6));
			storeLocation.setPhoneStore(queryResultSet.getString(7));

			results.add(i, storeLocation);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildParts(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			
			Part part = new Part();
			part.setPartId(queryResultSet.getInt(1));
			part.setPartNumber(queryResultSet.getString(2));
			part.setSupplier(queryResultSet.getString(3));
			part.setCategory(queryResultSet.getString(4));
			part.setDescription(queryResultSet.getString(5));
			part.setNotes(queryResultSet.getString(6));
			part.setCost(queryResultSet.getBigDecimal(7));
			part.setRetail(queryResultSet.getBigDecimal(8));
			part.setOnHand(queryResultSet.getInt(9));
			
			results.add(i,part);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildProjectSites(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			
			ProjectSite projectSite = new ProjectSite();
			projectSite.setProjectSiteId(queryResultSet.getLong(1));
			projectSite.setCustomerId(queryResultSet.getLong(2));
			projectSite.setProjectSiteAddress(queryResultSet.getString(3));
			projectSite.setProjectSiteCity(queryResultSet.getString(4));
			projectSite.setProjectSiteState(queryResultSet.getString(5));
			projectSite.setProjectSiteZipCode(queryResultSet.getString(6));
			projectSite.setProjectSiteNotes(queryResultSet.getString(7));
			
			String lastName = queryResultSet.getString(8);
			String firstName = queryResultSet.getString(9);
			String owner = lastName;
			if (firstName != null && firstName.length() != 0) {
				owner = lastName + ", " + firstName;
			}
			projectSite.setOwner(owner);
			
			results.add(i, projectSite);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildInvoices(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Invoice invoice = new Invoice();
			invoice.setInvoiceNum(queryResultSet.getInt(1));
			invoice.setCustomerId(queryResultSet.getLong(2));
			
			String lastname = queryResultSet.getString(3);
			String firstname = queryResultSet.getString(4);
			invoice.setCustomerName(lastname, firstname);
			
			// TODO see if we can use TextBlocks here
			String customerData = queryResultSet.getString(5) + "\n" + queryResultSet.getString(6) + ", " +
					queryResultSet.getString(7) + " " + queryResultSet.getString(8) + "\n" + queryResultSet.getString(9) + "\n" +
						queryResultSet.getString(10) + "\n" + queryResultSet.getString(11);
			invoice.setCustomerData(customerData);
			
			invoice.setNotes(queryResultSet.getString(12));
			invoice.setTax(queryResultSet.getBigDecimal(13));
			invoice.setCashiereDateTime(queryResultSet.getTimestamp(14));
			invoice.setCashiered(queryResultSet.getBoolean(15));
			// TODO populate invoice.parts[]
			int lineItemCount = queryResultSet.getInt(16);
			invoice.setParts(new Part[lineItemCount]);
			
			invoice.setTotal(queryResultSet.getBigDecimal(17)); 

			results.add(i, invoice);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildRepairOrders(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Project project = new Project();
			project.setProjectId(queryResultSet.getLong(1));
			project.setCustomerId(queryResultSet.getLong(2));
			project.setLocationId(queryResultSet.getLong(3));
			
			String lastname = queryResultSet.getString(4);
			String firstname = queryResultSet.getString(5);
			project.setCustomerName(lastname, firstname);
			
			// TODO see if we can use TextBlocks here
			String customerData = queryResultSet.getString(6) + "\n" + queryResultSet.getString(7) + ", " +
					queryResultSet.getString(8) + " " + queryResultSet.getString(9) + "\n" + queryResultSet.getString(10) + "\n" +
					queryResultSet.getString(11) + "\n" + queryResultSet.getString(12);
			project.setCustomerData(customerData);
			
			project.setProjectSiteId(queryResultSet.getLong(13));
			project.setProjectSiteAddress(queryResultSet.getString(14));
			project.setProjectSiteCity(queryResultSet.getString(15));
			project.setProjectSiteState(queryResultSet.getString(16));
			project.setProjectSiteZip(queryResultSet.getString(17));
			
			project.setCreatedDate(queryResultSet.getTimestamp(18));
			project.setClosedDate(queryResultSet.getTimestamp(19));
			
			List<Job> roJobs =  new Job().searchForJobsByProjectId(project.getProjectId());
			roJobs.removeAll(Collections.singleton(null));		// NOTE i don;t think we need this anymore
			
			project.setJobs(roJobs);
						
			results.add(i, project);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildJobs(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Job job = new Job();
			job.setJobId(queryResultSet.getLong(1));
			job.setProjectId(queryResultSet.getLong(2));
			job.setJobName(queryResultSet.getString(3));
			job.setDescription(queryResultSet.getString(4));
			job.setResolution(queryResultSet.getString(5));
			job.setReccomendations(queryResultSet.getString(6));
			
			results.add(i, job);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildJobLabors(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			JobLabor jobLabor = new JobLabor();
			jobLabor.setJobLaborId(queryResultSet.getLong(1));
			jobLabor.setJobId(queryResultSet.getLong(2));
			jobLabor.setDescription(queryResultSet.getString(3));
			jobLabor.setHours(queryResultSet.getBigDecimal(4));
			jobLabor.setLaborRate(queryResultSet.getBigDecimal(5));
			jobLabor.setInstaller(queryResultSet.getString(6));
						
			results.add(i, jobLabor);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildJobParts(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			JobPart jobPart = new JobPart();
			jobPart.setJobPartId(queryResultSet.getLong(1));
			jobPart.setJobId(queryResultSet.getLong(2));
			jobPart.setPartId(queryResultSet.getLong(3));
			jobPart.setPartNumber(queryResultSet.getString(4));
			jobPart.setDescription(queryResultSet.getString(5));
			jobPart.setQuantity(queryResultSet.getInt(6));
			jobPart.setSoldPrice(queryResultSet.getBigDecimal(7));
			jobPart.setPart((Part) new Part().searchByPk(jobPart.getPartId()));
			
			results.add(i, jobPart);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildAccounts(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);
		int i = 0;
		while (queryResultSet.next()) {
			Account account = new Account();
			account.setAccountId(queryResultSet.getLong(1));
			account.setCustomerId(queryResultSet.getLong(2));
			
			String lastname = queryResultSet.getString(3);
			String firstname = queryResultSet.getString(4);
			account.setCustomerName(lastname, firstname);
			
			account.setAccountNumber(queryResultSet.getString(5));
			account.setAccountName(queryResultSet.getString(6));
			account.setBalance(queryResultSet.getBigDecimal(7));
			
			
			Timestamp lastChargeDate = queryResultSet.getTimestamp(8);
			if (lastChargeDate != null) {
				StringBuilder lastCharge = new StringBuilder(lastChargeDate.toLocalDateTime().toLocalDate().toString());
				lastCharge.append(": $");
				lastCharge.append(queryResultSet.getBigDecimal(9).toString());
				account.setLastCharge(lastCharge.toString());
			}
			
			Timestamp lastPaymentDate = queryResultSet.getTimestamp(10);
			if (lastPaymentDate != null) {
				StringBuilder lastPayment = new StringBuilder(lastPaymentDate.toLocalDateTime().toLocalDate().toString());
				lastPayment.append(": $");
				lastPayment.append(queryResultSet.getBigDecimal(11).toString());
				account.setLastPayment(lastPayment.toString());
			}
			
			account.setTransactions(new AccountTransaction().searchTransactionsByAccount(account.getAccountId()));
			
			results.add(i, account);
			i++;
		}
		return results;
	}
	
	private static List<DbObjectSearchable> buildAccountTransactions(ResultSet queryResultSet) throws SQLException {
		List<DbObjectSearchable> results = new ArrayList<>(MAX_RESULTS);	// TODO probably take these MAX_RESULTS out of these instantiations
		int i = 0;
		while (queryResultSet.next()) {
			AccountTransaction accountTransaction = new AccountTransaction();
			accountTransaction.setTransactionId(queryResultSet.getLong(1));
			accountTransaction.setAccountId(queryResultSet.getLong(2));
			accountTransaction.setInvoiceId(queryResultSet.getLong(3));
			accountTransaction.setTransactionDate(queryResultSet.getTimestamp(4));
			accountTransaction.setDescription(queryResultSet.getString(5));
			accountTransaction.setAmount(queryResultSet.getBigDecimal(6));
			
			results.add(i, accountTransaction);
			i++;
		}
		return results;
	}
}
