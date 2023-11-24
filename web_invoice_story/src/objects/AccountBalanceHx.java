package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class AccountBalanceHx extends DbObject {
	
	// might not need this
	private long balanceHxId = -1;
	private long accountId;
	private Timestamp date;
	private BigDecimal balance;

	
	public AccountBalanceHx() {
		this.setLocationMatters(false);
	}
	
	public long getDbPk() {
		// return object's primary key
		return getBalanceHxId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Customer object's primary key
		balanceHxId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Customer table's primary key field name
		return "balanceHxId";
	}
	
	@Override
	public String getTableName() {
		// get Customer table's name
		return "accountBalanceHx";
	}
	
	public long getBalanceHxId() {
		return balanceHxId;
	}

	public void setBalanceHxId(long customerId) {
		this.balanceHxId = customerId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp transactionDate) {
		this.date = transactionDate;
	}

	public BigDecimal getBalance() {
		return balance == null ? BigDecimal.valueOf(0) : balance;
	}
	
	public void setBalance(BigDecimal amount) {
		this.balance = amount;
	}

	
	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		
		// return helper data for saving and updating Customer objects
		Map<String, String> dataMap = new HashMap<>();
		if (balanceHxId != -1) {
			dataMap.put("balanceHxId", Long.toString(balanceHxId));
		}
		dataMap.put("accountId", Long.toString(accountId));
		
		if (date != null) {
			dataMap.put("date", date.toString());
		}

		dataMap.put("balance", balance.toString());
				
		return dataMap;
	}

	protected static void populateAccountTransactionTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		new AccountBalanceHx().search("");
	}
	
	@Override
	public List<AccountBalanceHx> search(String searchString) {
		// search for a Customer from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT accountBalanceHx.balanceHxId, accountBalanceHx.accountId, accountBalanceHx.date, accountBalanceHx.balance
				FROM web_invoice_story.accountBalanceHx
				WHERE accountBalanceHx.balanceHxId LIKE ? OR accountBalanceHx.accountId LIKE ? OR accountBalanceHx.date LIKE ? OR accountBalanceHx.balance LIKE ?;""";
		super.outerSearchQueryAppendix = " AND accountBalanceHx.transactionId IN (";
		super.querySubStringIndices[0] = 35;
		super.querySubStringIndices[1] = 113;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public AccountBalanceHx searchByPk(long transactionId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(transactionId);
		super.searchQuery = """
				SELECT accountBalanceHx.balanceHxId, accountBalanceHx.accountId, accountBalanceHx.date, accountBalanceHx.balance
				FROM web_invoice_story.accountBalanceHx
				WHERE accountBalanceHx.balanceHxId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	public List<AccountBalanceHx> searchBalanceHxByAccount(long accountId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(accountId);
		super.searchQuery = """
				SELECT accountBalanceHx.balanceHxId, accountBalanceHx.accountId, accountBalanceHx.date, accountBalanceHx.balance
				FROM web_invoice_story.accountBalanceHx
				WHERE accountBalanceHx.accountId = ?
				ORDER BY accountBalanceHx.date;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObjects(DbServices.searchForObject(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<AccountBalanceHx> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<AccountBalanceHx> transactionSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			transactionSearchResults.add(packObject(object));
		}
		
		return transactionSearchResults;
	}
	
	private AccountBalanceHx packObject(DbObject dbObject) {
		// helper to cast List of DbObject to List of Customer
			if (dbObject instanceof AccountBalanceHx accountTransaction) {
				return accountTransaction;
			}
			throw new IllegalArgumentException("objects.AccountBalanceHx.packObject(): dbObjectSearchable is not an AccountBalanceHx");
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountBalanceHx other = (AccountBalanceHx) obj;
		if (this.balanceHxId != other.getBalanceHxId())
			return false;
		if (this.accountId != other.getAccountId())
			return false;
		if (!this.date.equals(other.getDate()))
			return false;
		if (!this.balance.equals(other.getBalance()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Account Transacion [balanceHxId=" + balanceHxId + ", accountId=" + accountId + ", date=" + date + ", balance=" + balance + "]";
	}
}
