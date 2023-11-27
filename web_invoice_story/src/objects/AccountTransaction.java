package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class AccountTransaction extends DbObject {
	
	// might not need this
	private long transactionId = -1;
	private long accountId;
	private long invoiceId;
	private Timestamp transactionDate;
	private String description;
	private BigDecimal amount;

	
	public AccountTransaction() {
	}
	
	public long getDbPk() {
		// return object's primary key
		return getTransactionId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Customer object's primary key
		transactionId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Customer table's primary key field name
		return "transactionId";
	}
	
	@Override
	public String getTableName() {
		// get Customer table's name
		return "accounttransaction";
	}
	
	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDescription() {
		return description == null ? "" : description;
	}

	public void setDescription(String accountName) {
		this.description = accountName;
	}

	public BigDecimal getAmount() {
		return amount == null ? BigDecimal.valueOf(0) : amount;
	}
	
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		
		// return helper data for saving and updating Customer objects
		Map<String, String> dataMap = new HashMap<>();
		if (transactionId != -1) {
			dataMap.put("transactionId", Long.toString(transactionId));
		}
		dataMap.put("accountId", Long.toString(accountId));
		dataMap.put("invoiceId", Long.toString(invoiceId));
		
		if (transactionDate != null) {
			dataMap.put("transactionDate", transactionDate.toString());
		}
		
		dataMap.put("description", description);
		dataMap.put("amount", amount.toString());
				
		return dataMap;
	}

	protected static void populateAccountTransactionTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		new AccountTransaction().search("");
	}
	
	@Override
	public List<AccountTransaction> search(String searchString) {
		// search for a Customer from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT accountTransaction.transactionId, accountTransaction.accountId, accountTransaction.invoiceId, accountTransaction.transactionDate, accountTransaction.description, accountTransaction.amount
				FROM web_invoice_story.accountTransaction
				LEFT JOIN web_invoice_story.account
				WHERE account.accountNumber LIKE ? OR accountTransaction.invoiceId LIKE ? OR accountTransaction.transactionDate LIKE ? OR accountTransaction.description LIKE ?;""";
		super.outerSearchQueryAppendix = " AND accountTransaction.transactionId IN (";
		super.querySubStringIndices[0] = 39;
		super.querySubStringIndices[1] = 195;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public AccountTransaction searchByPk(long transactionId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(transactionId);
		super.searchQuery = """
				SELECT accountTransaction.transactionId, accountTransaction.accountId, accountTransaction.invoiceId, accountTransaction.transactionDate, accountTransaction.description, accountTransaction.amount
				FROM web_invoice_story.accountTransaction
				WHERE accountTransaction.transactionId = ?;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	public List<AccountTransaction> searchTransactionsByAccount(long accountId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(accountId);
		super.searchQuery = """
				SELECT accountTransaction.transactionId, accountTransaction.accountId, accountTransaction.invoiceId, accountTransaction.transactionDate, accountTransaction.description, accountTransaction.amount
				FROM web_invoice_story.accountTransaction
				WHERE accountTransaction.accountId = ?
				ORDER BY accountTransaction.transactionDate;""";
		super.outerSearchQueryAppendix = "";
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObjects(DbServices.searchForObject(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<AccountTransaction> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<AccountTransaction> transactionSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			transactionSearchResults.add(packObject(object));
		}
		
		return transactionSearchResults;
	}
	
	private AccountTransaction packObject(DbObject dbObject) {
		// helper to cast List of DbObject to List of Customer
			if (dbObject instanceof AccountTransaction accountTransaction) {
				return accountTransaction;
			}
			throw new IllegalArgumentException("objects.AccountTransaction.packObject(): dbObjectSearchable is not an AccountTransaction");
	}

	@Override
	public int hashCode() {
		return Objects.hash(accountId, amount, description, invoiceId, transactionDate, transactionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AccountTransaction)) {
			return false;
		}
		AccountTransaction other = (AccountTransaction) obj;
		return accountId == other.accountId && Objects.equals(amount, other.amount)
				&& Objects.equals(description, other.description) && invoiceId == other.invoiceId
				&& Objects.equals(transactionDate, other.transactionDate) && transactionId == other.transactionId;
	}
	
	

	@Override
	public String toString() {
		return "Account Transacion [transactionId=" + transactionId + ", accountId=" + accountId + ", invoiceId=" + invoiceId + ", transactionDate=" + transactionDate + ", description=" + description + ", amount=" + amount + "]";
	}
}
