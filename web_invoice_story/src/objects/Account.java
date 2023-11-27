package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.widgets.Table;

import web_invoice_story.DbServices;

public class Account extends DbObject {
	
	// might not need this
	private long accountId = -1;
	private long customerId;
	private String accountHolder;
	private String accountNumber;
	private String accountName;
	private BigDecimal balance;
	
	private String lastCharge;
	private String lastPayment;
	
	private List<AccountTransaction> transactions;

	
	public Account() {
	}
	
	public long getDbPk() {
		// return Customer object's primary key
		return getAccountId();
	}
	
	@Override
	public void setDbPk(long dbPk) {
		// set Customer object's primary key
		accountId = dbPk;
	}
	
	@Override
	public String getPkName() {
		// get Customer table's primary key field name
		return "accountId";
	}
	
	@Override
	public String getTableName() {
		// get Customer table's name
		return "account";
	}
	
	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long customerId) {
		this.accountId = customerId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setCustomerName(String lastname, String firstname) {
			if (firstname != null) {
				firstname = firstname.equals("") ? firstname : ", " + firstname;
			}
			this.accountHolder = lastname + firstname;
		}

	public String getAccountNumber() {
		return accountNumber == null ? "" : accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName == null ? "" : accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getBalance() {
		return balance /* == null ? BigDecimal.valueOf(0) : balance */;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public String getLastCharge() {
		return lastCharge;
	}

	public void setLastCharge(String lastCharge) {
		this.lastCharge = lastCharge;
	}

	public String getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(String lastPayment) {
		this.lastPayment = lastPayment;
	}

	public List<AccountTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<AccountTransaction> transactions) {
		this.transactions = transactions;
	}
	
	public void chargeNewTransaction(Project cashieredProject) {
		// TODO create invoice to charge Project to
		
		Invoice projectAccountChargeInvoice = new Invoice();
		projectAccountChargeInvoice.setCustomerId(cashieredProject.getCustomerId());
		projectAccountChargeInvoice.setCustomerData(cashieredProject.getCustomerData());
		projectAccountChargeInvoice.setLocationId(cashieredProject.getLocationId());
		projectAccountChargeInvoice.setNotes("Charge Project # " + cashieredProject.getProjectId() + " to account");
		projectAccountChargeInvoice.setTax(BigDecimal.valueOf(0).setScale(2));
		projectAccountChargeInvoice.setCashiereDateTime(cashieredProject.getClosedDate());
		projectAccountChargeInvoice.setCashiered(true);
		projectAccountChargeInvoice.setAmountCharged(cashieredProject.getAmountCharged());
		projectAccountChargeInvoice.save();
		
		AccountTransaction newTransaction = new AccountTransaction();
		newTransaction.setInvoiceId(projectAccountChargeInvoice.getInvoiceNum());
		newTransaction.setAmount(cashieredProject.getAmountCharged());
		newTransaction.setDescription("Project");
		chargeNewTransaction(newTransaction);
	}
	
	public void chargeNewTransaction(Invoice cashieredInvoice) {
		AccountTransaction newTransaction = new AccountTransaction();
		newTransaction.setInvoiceId(cashieredInvoice.getInvoiceNum());
		newTransaction.setAmount(cashieredInvoice.getAmountCharged());
		if (cashieredInvoice instanceof AccountPayment) {
			newTransaction.setDescription("Payment");
			setLastPayment(cashieredInvoice.getCashiereDateTime() + ": " + cashieredInvoice.getAmountCharged());
		} else {
			newTransaction.setDescription("Invoice");
			setLastCharge(cashieredInvoice.getCashiereDateTime() + ": " + cashieredInvoice.getAmountCharged());
		}
		chargeNewTransaction(newTransaction);
	}
	
	public void chargeNewTransaction(AccountTransaction newTransaction) {
		newTransaction.setAccountId(this.getAccountId());
		newTransaction.setTransactionDate(Timestamp.from(Instant.now()));
//		newTransaction.save();
		
		// make sure newTransaction is not already in this.transactions
		if (this.transactions.contains(newTransaction)) {
			// maybe tell the dev/calling method that this Transaction is already a part of this Account
			return;
		}
		
		// save the transaction to this Account object and to the database
			// alternatively, I could just send the entire Account object to the DbServices
			// and let it figure out which transactions are already saved in the DB via a query
			// to see which ID its missing.....or I just look for the object with the ID set to -1
			// like I've been doing this entire time.....duh
				// we'll mess with that later
		this.transactions.add(newTransaction);
		newTransaction.save();
		
		// calculate the new Account balance
		this.setBalance(this.getBalance().add(newTransaction.getAmount()));
		
		// create new Account Balance History row for database
		AccountBalanceHx newBalanceHxLine = new AccountBalanceHx();
		newBalanceHxLine.setAccountId(this.getAccountId());
		newBalanceHxLine.setDate(newTransaction.getTransactionDate());
		newBalanceHxLine.setBalance(this.getBalance());
		newBalanceHxLine.save();

	}

	public Map<String, String> getDataMap() {
		// TODO we might need to make these Maps into <String, Object>, then instanceof on the Object value
			// to see how to treat it when building PreparedStatements with setParameter (of course I already changed all Customer fields to String)
		
		// return helper data for saving and updating Customer objects
		Map<String, String> dataMap = new HashMap<>();
		if (accountId != -1) {
			dataMap.put("accountId", Long.toString(accountId));
		}
		dataMap.put("customerId", Long.toString(customerId));
		dataMap.put("accountNumber", accountNumber);
		dataMap.put("accountName", accountName);
		dataMap.put("balance", balance.toString());
				
		return dataMap;
	}

	protected static void populateAccountTable(Table table) {
		// not yet used, could populate "" in searchForObject call with current txtSearchBox.getText()
		new Account().search("");
	}
	
	@Override
	public List<Account> search(String searchString) {
		// search for a Customer from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT account.accountId, account.customerId, customer.lastname, customer.firstname, account.accountNumber, account.accountName, account.balance, lastPayDetails.transactionDate, lastPayDetails.amount, lastChargeDetails.transactionDate, lastChargeDetails.amount
				FROM web_invoice_story.account
				LEFT JOIN web_invoice_story.customer on customer.customerId = account.customerId
				LEFT JOIN (select max(transactionId) as transactionId, accountId
				FROM web_invoice_story.accountTransaction
				WHERE description = 'Payment'
				GROUP BY accountId) lastPayId on lastPayId.accountId = account.accountId
				LEFT JOIN web_invoice_story.accountTransaction lastPayDetails ON lastPayDetails.transactionId = lastPayId.transactionId 
				LEFT JOIN (select max(transactionId) as transactionId, accountId
				FROM web_invoice_story.accountTransaction
				WHERE description <> 'Payment'
				GROUP BY accountId) lastChargeId on lastChargeId.accountId = account.accountId
				LEFT JOIN web_invoice_story.accountTransaction lastChargeDetails ON lastChargeDetails.transactionId = lastChargeId.transactionId
				WHERE customer.firstName LIKE ? OR customer.lastName LIKE ? OR customer.homePhone LIKE ? OR account.accountNumber LIKE ? OR account.accountName LIKE ?;""";
		super.outerSearchQueryAppendix = " AND account.accountId IN (";
		super.querySubStringIndices[0] = 24;
		super.querySubStringIndices[1] = 145;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override
	public Account searchByPk(long customerId) {		// NOTE when fixing that bug change the name of this to SearchByPk or something
		// search for a Customer from the database using a primary key
		super.searchString = Long.toString(customerId);
		super.searchQuery = """
				SELECT account.accountId, account.customerId, customer.lastname, customer.firstname, account.accountNumber, account.accountName, account.balance, lastPayDetails.transactionDate, lastPayDetails.amount, lastChargeDetails.transactionDate, lastChargeDetails.amount
				FROM web_invoice_story.account
				LEFT JOIN web_invoice_story.customer on customer.customerId = account.customerId
				LEFT JOIN (select max(transactionId) as transactionId, accountId
				FROM web_invoice_story.accountTransaction
				WHERE description = 'Payment'
				GROUP BY accountId) lastPayId on lastPayId.accountId = account.accountId
				LEFT JOIN web_invoice_story.accountTransaction lastPayDetails ON lastPayDetails.transactionId = lastPayId.transactionId 
				LEFT JOIN (select max(transactionId) as transactionId, accountId
				FROM web_invoice_story.accountTransaction
				WHERE description <> 'Payment'
				GROUP BY accountId) lastChargeId on lastChargeId.accountId = account.accountId
				LEFT JOIN web_invoice_story.accountTransaction lastChargeDetails ON lastChargeDetails.transactionId = lastChargeId.transactionId
				WHERE account.customerId = ?;""";	// NOTE this is searching by customerId, not the actual PK
		super.outerSearchQueryAppendix = "";		// i'm not following precendent and I should probably make a new method for this
		super.querySubStringIndices[0] = 0;
		super.querySubStringIndices[1] = 0;

		return packObject(DbServices.searchForObjectByPk(this));
	}
	
	// TODO make these methods of superclass
		// another NOTE these are not used now but may be useful if I remove the two search methods
			// from the super class and make them return objects of the Class they're called on
	private List<Account> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Customer
		List<Account> accountSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			accountSearchResults.add(packObject(object));
		}
		
		return accountSearchResults;
	}
	
	private Account packObject(DbObject dbObject) {
		// helper to cast List of DbObject to List of Customer
			if (dbObject instanceof Account account) {
				return account;
			}
			throw new IllegalArgumentException("objects.Account.packObject(): dbObjectSearchable is not an Account");
	}

	@Override
	public int hashCode() {		// NOTE this doesn't check all object fields, we need to actually build the entire object from DB in order to compare all fields
		return Objects.hash(accountHolder, accountId, accountName, accountNumber, customerId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		return Objects.equals(accountHolder, other.accountHolder) && accountId == other.accountId
				&& Objects.equals(accountName, other.accountName) && Objects.equals(accountNumber, other.accountNumber)
				&& customerId == other.customerId;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", customerId=" + customerId + ", accountNumber=" + accountNumber + ", accountName=" + accountName + ", balance=" + balance + "]";
	}
}
