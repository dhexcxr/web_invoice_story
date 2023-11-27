	package objects;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.swt.widgets.TableItem;

import web_invoice_story.DbServices;

public class Invoice extends DbObjectWithLocation {

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
	
	private BigDecimal amountCharged;
	
	public enum InvoiceResult {
		WITHCHANGE,
		NOCHANGE,
		CHARGED,
		CANCELED,
		PAYMENT
	}
	
	public Invoice() {
		this.setLocationMatters(true);
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
		return parts == null ? new Part[0] : parts;
	}

	public void setParts(Part[] parts) {
		this.parts = parts;
	}
	
	public void setPartsFromTableItems(TableItem[] items) {
		Part[] partsFromTableItems = new Part[items.length];
		// hack, I need to change Part[] to List<Part>, and get all calling methods to handle the change
		int i = 0;
		while(items[i].getData() != null) {
			partsFromTableItems[i] = ((InvoicePart) items[i].getData()).getPart();
			i++;
		}
		this.setParts(partsFromTableItems);
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

	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}

	@Override
	public Map<String, String> getDataMap() {
		// return helper data for saving and updating Invoice objects
		Map<String, String> dataMap = new HashMap<>();
		if (invoiceNum != -1) {
			dataMap.put("invoicenum", Long.toString(getInvoiceNum()));
		}
		dataMap.put("customerid", Long.toString(getCustomerId()));
		dataMap.put("customerdata", getCustomerData());
		dataMap.put("locationId", Long.toString(getLocationId()));
		dataMap.put("notes", getNotes());
		dataMap.put("tax", getTax().toString());
		dataMap.put("cashiereddate", getCashiereDateTime().toString());
		dataMap.put("cashiered", isCashiered() ? "1" : "0");
		return dataMap;
	}
	
	@Override
	public List<Invoice> search(String searchString) {
		// search for a Invoice from the database using a search string
		super.searchString = searchString;		// NOTE TODO why does this query NOT have locationId, Customer, Employee and Project do
		super.searchQuery = """
				SELECT invoice.invoicenum, invoice.customerid, customer.lastname, customer.firstname,
				customer.address, customer.city, customer.state, customer.zipcode,
				customer.homephone, customer.cellphone, customer.email, invoice.notes, invoice.tax,
				invoice.cashiereddate, invoice.cashiered, invoicelineitem.count, invoicelineitem.total + invoice.tax 
				FROM web_invoice_story.invoice
				JOIN web_invoice_story.customer ON invoice.customerid = customer.customerid 
				JOIN (SELECT invoicenum, COUNT(partid) AS count, SUM(quantity * soldprice) AS total
				\tFROM web_invoice_story.invoicelineitem GROUP BY invoicenum) AS invoicelineitem ON invoice.invoicenum = invoicelineitem.invoicenum
				WHERE customer.lastname LIKE ? OR customer.firstname LIKE ? OR invoice.invoicenum LIKE ?;""";
		super.outerSearchQueryAppendix = " AND invoice.invoicenum IN (";
		super.querySubStringIndices[0] = 25;
		super.querySubStringIndices[1] = 330;

		return packObjects(DbServices.searchForObject(this));
	}
	
	@Override		// NOTE not used anywhere
	public Invoice searchByPk(long invoiceNum) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	private List<Invoice> packObjects(List<DbObject> searchResults) {
		// helper to cast List of DbObject to List of Invoice
		List<Invoice> invoiceSearchResults = new ArrayList<>();
		
		for (DbObject object : searchResults) {
			if (object instanceof Invoice invoice) {
				invoiceSearchResults.add(invoice);
			}
		}
		
		return invoiceSearchResults;
	}

	@Override
	public int hashCode() {		// NOTE this doesn't compare TableLineItems because that doesn't implement equals()
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(parts);
		result = prime * result + Objects.hash(amountCharged, cashiereDateTime, cashiered, customerData, customerId,
				customerName, invoiceNum, notes, tax, total);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Invoice)) {
			return false;
		}
		Invoice other = (Invoice) obj;
		return Objects.equals(amountCharged, other.amountCharged)
				&& Objects.equals(cashiereDateTime, other.cashiereDateTime) && cashiered == other.cashiered
				&& Objects.equals(customerData, other.customerData) && customerId == other.customerId
				&& Objects.equals(customerName, other.customerName) && invoiceNum == other.invoiceNum
				&& Objects.equals(notes, other.notes) && Arrays.equals(parts, other.parts)
				&& Objects.equals(tax, other.tax) && Objects.equals(total, other.total);
	}

	@Override
	public String toString() {
		return "Invoice [invoiceNum=" + invoiceNum + ", customerId=" +
		customerId + ", customerName=" + customerName
		+ ", customerData=" + customerData + ", notes=" + notes + ", cashiereDateTime=" + cashiereDateTime
		+ ", tax=" + tax + ", total=" + total + ", cashiered=" + cashiered + ", parts=" + Arrays.toString(parts) + "]";
	}
}
