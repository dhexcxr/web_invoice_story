package guiParts;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

import objects.DbObjectSearchable;
import objects.InvoicePart;
import objects.Part;
import web_invoice_story.Application;

public class InvoicePartTable extends MyTable{
	
	// TODO find out where these are being used and see if I want to move them somewhere else, these are table indices for Invoice Table columns
	public static final int PART_NUMBER_COLUMN = 0;
	public static final int QUANTITY_COLUMN = 2;
	public static final int PART_PRICE_COLUMN = 5;
	public static final int EXTENDED_PRICE_COLUMN = 6;

	public InvoicePartTable(Composite parent, int style) {
		super(parent, style);
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(this, SWT.NONE);	// so we can add parts
	}
	
	@Override
	public void paint(DbObjectSearchable object) {		// NOTE not used
		// build each TableItem to fill Unit Table
			paint(object, this.getSelectionIndex());
	}

	@Override
	public void paint(DbObjectSearchable object, int selectedTableItemIndex) {		// NOTE this IS used, I think when a Invoice Line Item is edited, this helps replace the single new line
		// this is called only when adding a new part to a blank TableItem
		// build each TableItem to fill Unit Table
			if (object == null) {
				return;
			}
			InvoicePart invoicePart = (InvoicePart) object;
			Part part = invoicePart.getPart();
			TableItem tableItem = this.getItem(selectedTableItemIndex);
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(invoicePart.getQuantity()),
					Integer.toString(part.getOnHand()),	part.getCost().toString(), part.getRetail().toString(),
							part.getRetail().toString()});
			tableItem.setData(object);
	}
	
	@Override
	public void paint(List<? extends DbObjectSearchable> objectResults) {
		// build each TableItem to fill Unit Table
		for (DbObjectSearchable object : objectResults) {
			if (object == null || !(object  instanceof InvoicePart invoicePart) || invoicePart.getPart() == null ) {
				break;
			}
			Part part = invoicePart.getPart();
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(), Integer.toString(1),
													Integer.toString(part.getOnHand()), part.getCost().toString(),
														part.getRetail().toString(), part.getRetail().toString()});
			tableItem.setData(invoicePart);
		}
	}
	
	@Override
	public void sort(int sortColumn) {
		// invoice table doesn't sort, only display parts in order they were entered
		Application.getLogger().log(Level.WARNING, "Sort method should not be called on Part Invoice Table");
	}
}
