package listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import guiParts.InvoicePartTable;
import guiParts.MyTable;
import web_invoice_story.Application;

public class InvoicePartDeleteLineItemListener extends MouseAdapter{
	
	private MyTable invoicePartsTable;
	protected Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	
	public InvoicePartDeleteLineItemListener(MyTable partTableInvoice) {
		this(partTableInvoice, new Text(new Shell(), 0), new Text(new Shell(), 0), new Text(new Shell(), 0));
	}
	
	public InvoicePartDeleteLineItemListener(MyTable invoicePartsTable, Text txtPartsTotalInvoice, Text txtTaxInvoice,
			Text txtFinalTotal) {
		super();
		this.invoicePartsTable = invoicePartsTable;
		this.txtPartsTotalInvoice = txtPartsTotalInvoice;
		this.txtTaxInvoice = txtTaxInvoice;
		this.txtFinalTotal = txtFinalTotal;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (invoicePartsTable.getSelectionIndex() >= 0 && invoicePartsTable.getSelectionIndex() < invoicePartsTable.getItemCount() &&
													invoicePartsTable.getItem(invoicePartsTable.getSelectionIndex()).getData() != null) {
			int selectedIndex = invoicePartsTable.getSelectionIndex();
			invoicePartsTable.remove(selectedIndex);
			
			selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
			invoicePartsTable.setSelection(selectedIndex);
			invoicePartsTable.notifyListeners(SWT.MouseDown, new Event());
			
			// TODO the following copied from InvoicePartEditorEventListener, need to find a better way to calc invoice total
					// we could do this by making InvoicePartEditorEventListener.calculateInvoiceTotal a method of the invoicePartTable
					// but that would require some re-figuring for txtPartsTotalInvoice, txtTaxInvoice, and txtFinalTotal
			BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
			BigDecimal total = BigDecimal.valueOf(0);
			TableItem[] items = invoicePartsTable.getItems();
			for (TableItem item : items) {
				if (item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN).equals("")) {
					// ignore new TableItem at end of list with no part data set 
					break;
				}
				Application.getLogger().log(Level.INFO, "Total before: {0}", total);
				total = total.add(new BigDecimal(item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN)));
				Application.getLogger().log(Level.INFO, "Item price to BigDecimal: {0}", new BigDecimal(item.getText(InvoicePartTable.EXTENDED_PRICE_COLUMN)));
				Application.getLogger().log(Level.INFO, "Total after: {0}", total);
			}
			txtPartsTotalInvoice.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
			txtTaxInvoice.setText("$" + taxRate.multiply(total).setScale(2, RoundingMode.CEILING).toString());
			txtFinalTotal.setText("$" + taxRate.multiply(total).add(total).setScale(2, RoundingMode.CEILING).toString());
			// END copied section
		}
	}
}
