	package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.TableItem;

import web_invoice_story.DbServices;

public class Invoice extends DbObjectSearchable implements DbObjectSavable {

	private long invoiceNum = -1;
	private long customerId;
	private String customerName;
	private String customerData;
	private String notes;
	private Timestamp cashiereDateTime;
	private BigDecimal tax;
	private BigDecimal total;
	private boolean cashiered;
	
	private TableItem[] tableLineItems;
	private Part[] parts;
	
	public Invoice() {
		// empty default constructor
	}
	
	@Override
	public long getDbPk() {
//		return invoiceNum;		// when this is called in DbServices we'll always be making a new invoice
		return -1;					// ie you can't edit an old invoice
	}
	
	@Override
	public void setDbPk(long dbPk) {
		invoiceNum = dbPk;
	}

	@Override
	public String getPkName() {
		// get Invoice table's primary key field name
		return "invoicenum";
	}

	@Override
	public String getTableName() {
		// get Invoice table's name
		return "invoice";
	}
	
	public long getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(long invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String lastname, String firstname) {
		if (firstname != null) {
			firstname = firstname.equals("") ? firstname : ", " + firstname;
		}
		this.customerName = lastname + firstname;
	}

	public String getCustomerData() {
		return customerData;
	}

	public void setCustomerData(String customerData) {
		this.customerData = customerData;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Timestamp getCashiereDateTime() {
		return cashiereDateTime;
	}

	public void setCashiereDateTime(Timestamp cashiereDateTime) {
		this.cashiereDateTime = cashiereDateTime;
	}

	public boolean isCashiered() {
		return cashiered;
	}

	public void setCashiered(boolean cashiered) {
		this.cashiered = cashiered;
	}

	public TableItem[] getTableLineItems() {
		return tableLineItems;
	}

	public void setTableLineItems(TableItem[] tableItems) {
		this.tableLineItems = tableItems;
	}

	public Part[] getParts() {
		return parts;
	}

	public void setParts(Part[] parts) {
		this.parts = parts;
	}
	
	@SuppressWarnings("unused")
	private BigDecimal getPartsTotal() {
		// not used right now, need to populate Part[] parts before this is usable
		BigDecimal partsTotal = new BigDecimal(0);
		for (int i = 0; i < this.getParts().length; i++) {
			partsTotal = partsTotal.add(this.getParts()[i].getRetail());
		}
		return partsTotal;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	@Override
	public Map<String, String> getDataMap() {
		// return helper data for saving and updating Invoice objects
		Map<String, String> dataMap = new HashMap<>();
		if (invoiceNum != -1) {
			dataMap.put("invoicenum", Long.toString(invoiceNum));
		}
		dataMap.put("customerid", Long.toString(customerId));
		dataMap.put("customerdata", customerData);
		dataMap.put("notes", notes);
		dataMap.put("tax", tax.toString());
		dataMap.put("cashiereddate", cashiereDateTime.toString());
		dataMap.put("cashiered", cashiered ? "1" : "0");
		return dataMap;
	}
	
	@Override
	public List<DbObjectSearchable> search(String searchString) {
		// search for a Invoice from the database using a search string
		super.searchString = searchString;
		super.searchQuery = """
				SELECT i.invoicenum, i.customerid, c.lastname, c.firstname,
				c.address, c.city, c.state, c.zipcode,
				c.homephone, c.cellphone, c.email, i.notes, i.tax,
				i.cashiereddate, i.cashiered, ip.count, ip.total + i.tax 
				FROM cis2901c.invoice AS i JOIN cis2901c.customer AS c
				ON i.customerid = c.customerid 
				JOIN (SELECT invoicenum, COUNT(partid) AS count, SUM(quantity *
				soldprice) AS total FROM cis2901c.invoicepart GROUP BY invoicenum) AS ip 
				ON i.invoicenum = ip.invoicenum WHERE c.lastname LIKE ? OR
				c.firstname LIKE ? OR i.invoicenum LIKE ?;""";
		super.outerSearchQueryAppendix = " AND i.invoicenum IN (";
		super.querySubStringIndices[0] = 19;
		super.querySubStringIndices[1] = 207;
		
		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override		// NOTE not used anywhere
	public DbObjectSearchable searchByPk(long invoiceNum) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	private List<DbObjectSearchable> packObjects(List<DbObjectSearchable> searchResults) {
		// helper to cast List of DbObjectSearchable to List of Invoice
		List<DbObjectSearchable> invoiceSearchResults = new ArrayList<>();
		
		for (DbObjectSearchable object : searchResults) {
			if (object instanceof Invoice invoice) {
				invoiceSearchResults.add(invoice);
			}
		}
		
		return invoiceSearchResults;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceNum=" + invoiceNum + ", customerId=" +
		customerId + ", customerName=" + customerName
		+ ", customerData=" + customerData + ", notes=" + notes + ", cashiereDateTime=" + cashiereDateTime
		+ ", tax=" + tax + ", total=" + total + ", cashiered=" + cashiered + ", parts=" + Arrays.toString(parts) + "]";
	}
}
