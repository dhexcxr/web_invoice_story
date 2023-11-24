package objects;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoicePart extends DbObject {
	
	// invoicepart Database Table
	private Map<String, String> dataMap = new HashMap<>();
	private long invoicePartId = -1;
	private long partId;
	private String partNumber;
	private String description;
	private int quantity;
	private BigDecimal soldPrice = BigDecimal.valueOf(0);
	
	private Part part;
	
	private static final String FOREIGN_KEY = "invoicenum";
	
	public InvoicePart() {
		this.setLocationMatters(false);
	}
	
	public InvoicePart(Part part) {
		if (part == null) {
			return;
		}
		this.partId = part.getPartId();
		this.partNumber = part.getPartNumber();
		this.description = part.getDescription();
		this.quantity = 1;
		this.soldPrice = part.getRetail();
		this.part = part;
	}

	public long getInvoicePartId() {
		return invoicePartId;
	}

	public void setInvoicePartId(long invoicePartId) {
		this.invoicePartId = invoicePartId;
	}

	public long getPartId() {
		return partId;
	}

	public void setPartId(long partId) {
		this.partId = partId;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(BigDecimal soldPrice) {
		this.soldPrice = soldPrice;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		if (part != null && this.getPartId() != part.getPartId()) {
			this.partId = part.getPartId();
			this.partNumber = part.getPartNumber();
			this.description = part.getDescription();
			this.quantity = 1;
			this.soldPrice = part.getRetail();
			this.part = part;
		}
	}

	@Override
	public long getDbPk() {
		// when this is called in DbServices we'll always be making a new TableItem
				return -1;					// ie you can't open and edit Invoices
	}

	@Override
	public void setDbPk(long dbPk) {
		// similarly, we don't need to set the Primary Key
	}

	@Override
	public String getPkName() {
		return null;
	}
	
	public String getFkName() {
		return FOREIGN_KEY;
	}

	@Override
	public String getTableName() {
		return "invoicelineitem";		// TODO change all refs to this table to invoiceLineItem, because that's what its called in the DB now
	}

	@Override
	public Map<String, String> getDataMap() {
		dataMap.put("partid", Long.toString(partId));
		dataMap.put("description", description);
		dataMap.put("quantity", Integer.toString(quantity));
		dataMap.put("soldprice", soldPrice.toString());
		return dataMap;
	}
	
	@Override
	public List<? extends InvoicePart> search(String searchString) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public InvoicePart searchByPk(long invoicePartPrimaryKey) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "InvoicePart [dataMap=" + dataMap + ", invoicePartId=" + invoicePartId + ", partId=" + partId
				+ ", partNumber=" + partNumber + ", description=" + description + ", quantity=" + quantity
				+ ", soldPrice=" + soldPrice + ", part=" + part + "]";
	}
}
