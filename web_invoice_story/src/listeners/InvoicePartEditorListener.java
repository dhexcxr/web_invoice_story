package listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import gui.PartSearchDialog;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;
import guiParts.MyText;
import objects.DbObjectSearchable;
import objects.InvoicePart;
import objects.JobPart;
import objects.Part;
import web_invoice_story.Application;

public class InvoicePartEditorListener implements Listener {

	private InvoicePartTable partInvoiceTable;
	private TableItem selectedTableItem;
	protected int selectedTableItemIndex;
	private int selectedColumnIndex;
	private Text editorTxtBox;
	
	private Text txtPartsTotalInvoice;
	private Text txtTaxInvoice;
	private Text txtFinalTotal;
	private Text textCategoryInvoice;
	private Text textSupplierInvoice;
	private Text textNotesInvoice;
	private Shell parent;
	private InvoicePartTableListener invoicePartTableListener;

	// this allows us to ignore the FocusOut listener so we don't call PartSearchDialog twice
	// if there is text in Part Number column when we "double" click to open Search box
	protected boolean ignoreFocusOut = false;

	private static final String ONLY_DECIMALS = "[^-0-9.]"; // find a better name

	public InvoicePartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex, int selectedColumnIndex,
										Text editorTxtBox, List<MyText> invoiceDetailText, Shell parent, InvoicePartTableListener invoicePartTableListener) {
		this.partInvoiceTable = partInvoiceTable;
		this.selectedTableItem = partInvoiceTable.getItem(selectedTableItemIndex);
		this.selectedTableItemIndex = selectedTableItemIndex;
		this.selectedColumnIndex = selectedColumnIndex;
		this.editorTxtBox = editorTxtBox;

		this.txtPartsTotalInvoice = invoiceDetailText.get(0);
		this.txtTaxInvoice = invoiceDetailText.get(1);
		this.txtFinalTotal = invoiceDetailText.get(2);
		this.textCategoryInvoice = invoiceDetailText.get(3);
		this.textSupplierInvoice = invoiceDetailText.get(4);
		this.textNotesInvoice = invoiceDetailText.get(5);
		
		this.parent = parent;
		this.invoicePartTableListener = invoicePartTableListener;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO streamline all these ignoreFocusOuts
		if (ignoreFocusOut) {
			return;
		}
		if (event.type == SWT.MouseDown ) {
			if (selectedColumnIndex == InvoicePartTable.PART_NUMBER_COLUMN) {
				ignoreFocusOut = true;
				PartSearchDialog partSearchDialog = new PartSearchDialog(parent,SWT.NONE);
					// NOTE TODO to fix bug in RO Job Parts, this needs to be a JobPart when we're opening the PartSearchDialog from an RO
					// basically, we need to devise a way to tell that we're calling this from an RO
					// we could just hack a new handleEvent() just for ROs kinda like we did for the InvoicePartTableListener constructor
				InvoicePart selectedPart = new InvoicePart((Part) partSearchDialog.open(editorTxtBox.getText()));
				if (selectedPart.getPart() != null) {
					editorTxtBox.setText(selectedPart.getPartNumber());
					paintInvoiceLines(selectedPart); 
					calculateInvoiceTotal();
				}
				ignoreFocusOut = false;
				editorTxtBox.dispose();
			}
		} else if (event.type == SWT.FocusOut) {
			setColumnData();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_ESCAPE) {
			editorTxtBox.dispose();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_TAB_NEXT) {
			setColumnData();
			invoicePartTableListener.tabbed(selectedColumnIndex);
		} else if (event.type == SWT.Traverse) {
			setColumnData();
		}
	}
	
	private void setColumnData() {
		if (selectedColumnIndex == InvoicePartTable.PART_NUMBER_COLUMN && !ignoreFocusOut) {
			// we entered a part number
			findPartNumber();
			calculateInvoiceTotal();
		} else if (selectedColumnIndex == InvoicePartTable.QUANTITY_COLUMN &&
				(selectedTableItem.getData() != null && !editorTxtBox.getText().equals(""))) {
			// we entered a part quantity
				setPartQuantity(selectedTableItem);
				calculateInvoiceTotal();
		} else if (selectedColumnIndex == InvoicePartTable.PART_PRICE_COLUMN &&
				(selectedTableItem.getData() != null && !editorTxtBox.getText().equals(""))) {
			// we entered a part price
			setPartPrice(selectedTableItem);
			calculateInvoiceTotal();
		}
		editorTxtBox.dispose();
	}

	private void findPartNumber() {
		ignoreFocusOut = true;
		Application.getLogger().log(Level.INFO, "findPartNumber called");
		// search for part, populate selected TableItem fields, add new TableItem, calculate total
		if (editorTxtBox.getText().length() > 0) {
					// NOTE the change here is how we drop a DbServices.search() call for an DbObjectSearchable().search() call
						// now we don't have to perform a hacky cast
			List<Part> partResults = new Part().search(editorTxtBox.getText());
			InvoicePart editedLineItem = (InvoicePart) selectedTableItem.getData();
			if (editedLineItem == null) {
				editedLineItem = new InvoicePart();
			} else if (editedLineItem instanceof JobPart && editedLineItem.getPart() == null) {
				// if this is a new, blank JobPart, setData(null) so we can correctly add
					// another new blank line in paintInvoiceLines(InvoicePart editedLineItem)  
				selectedTableItem.setData(null);
			}
			if (partResults.size() == 1 && partResults.get(0) instanceof Part part) {		// if there's only 1 result
					editedLineItem.setPart(part);
			} else {
				// if there's more than 1 result, or no results returned, call Item Search Box and show result
				PartSearchDialog partSearchDialog = new PartSearchDialog(parent,SWT.NONE);
				Part part = (Part) partSearchDialog.open(editorTxtBox.getText());
				if (part == null) {
					ignoreFocusOut = false;
					return;
				}
				editedLineItem.setPart(part);
			}			
			paintInvoiceLines(editedLineItem);
		}
		ignoreFocusOut = false;
	}

	private void paintInvoiceLines(InvoicePart editedLineItem) {
		Application.getLogger().log(Level.INFO, "paintInvoiceLines called");
		
		textCategoryInvoice.setText(editedLineItem.getPart().getCategory());
		textSupplierInvoice.setText(editedLineItem.getPart().getSupplier());
		textNotesInvoice.setText(editedLineItem.getPart().getNotes());
		
		if (duplicatePartPresent(editedLineItem)) {
			return;
		}					// TODO I'm pretty sure we can just use selectedTableItem.getData() here
		if ((InvoicePart) partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
			// if we're editing an empty TableItem line item
			@SuppressWarnings("unused")		// this adds another new, empty TableItem at the end of the Invoice Line Items so we can continue selecting and adding parts
			TableItem tableItem = new InvoicePartTableItem(partInvoiceTable, SWT.NONE, partInvoiceTable.getItemCount());
		}
		editedLineItem.setQuantity(checkValidQuantity(editedLineItem, editedLineItem.getQuantity()));
		partInvoiceTable.paint(editedLineItem, selectedTableItemIndex);
	}
	
	private boolean duplicatePartPresent(InvoicePart editedLineItem) {
		for (TableItem tableItem : partInvoiceTable.getItems()) {
			if (editedLineItem.getPart().getPartNumber().equals(tableItem.getText(InvoicePartTableItem.PART_NUMBER_COLUMN))
					&& !tableItem.equals(selectedTableItem)) {
				if (selectedTableItem.getData() != null) {
					partInvoiceTable.remove(partInvoiceTable.indexOf(selectedTableItem));
				}
				selectedTableItem = tableItem;
				partInvoiceTable.setSelection(selectedTableItem);
				int quantity = Integer.parseInt(tableItem.getText(InvoicePartTableItem.QUANTITY_COLUMN));
				tableItem.setText(InvoicePartTableItem.QUANTITY_COLUMN, Integer.toString(quantity + 1));
				setPartQuantity(selectedTableItem, quantity + 1);
				ignoreFocusOut = false;
				return true;
			}
		}
		return false;
	}

	private void setPartQuantity(TableItem item) {
		String newQuantity = editorTxtBox.getText().replaceAll(ONLY_DECIMALS, "");
		String currentQuantity = selectedTableItem.getText(InvoicePartTableItem.QUANTITY_COLUMN);
		int quantity = newQuantity.equals("") ? 
				Integer.parseInt(currentQuantity) : Integer.parseInt(newQuantity);
		setPartQuantity(item, quantity);
	}

	private void setPartQuantity(TableItem item, int quantity) {
		InvoicePart selectedInvoicePart = (InvoicePart) selectedTableItem.getData();
		quantity = checkValidQuantity(selectedInvoicePart, quantity);
		String newQuantity = Integer.toString(quantity);
		item.setText(InvoicePartTable.QUANTITY_COLUMN, newQuantity);
		selectedInvoicePart.setQuantity(quantity);
		BigDecimal extendedPrice = new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN)).multiply(new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN)));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, (extendedPrice.toString()));
	}
	
	private int checkValidQuantity(InvoicePart selectedInvoicePart, int quantity) {
		// checjk if sell quantity is greater than Part On Hand
		Part selectedPart = selectedInvoicePart.getPart();
		if (quantity > selectedPart.getOnHand()) {
			// if quantity entered is more than OnHand, pop up dialog telling user as much and set to OnHand
			ignoreFocusOut = true;
			MessageBox onHandWarningDialogBox = new MessageBox(parent, SWT.ICON_INFORMATION);
			onHandWarningDialogBox.setText("Notice");
			onHandWarningDialogBox.setMessage("Sell Quantity is greater than On Hand Quantity\n\nSell Quantity set to On Hand Quantity");
			onHandWarningDialogBox.open();
			quantity = selectedPart.getOnHand();
			ignoreFocusOut = false;
		}
		return quantity;
	}

	private void setPartPrice(TableItem item) {							   // TODO fix this,   v   , probably needs to be ONLY_DECIMALS
		ignoreFocusOut = true;
		InvoicePart selectedInvoicePart = (InvoicePart) selectedTableItem.getData();
		String partPrice = editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ?
								item.getText(InvoicePartTable.PART_PRICE_COLUMN) : editorTxtBox.getText().replaceAll(ONLY_DECIMALS, "");
		item.setText(InvoicePartTable.PART_PRICE_COLUMN, partPrice);
		selectedInvoicePart.setSoldPrice(new BigDecimal(partPrice));
		BigDecimal extendedPrice = new BigDecimal(item.getText(InvoicePartTable.QUANTITY_COLUMN))
										.multiply(new BigDecimal(item.getText(InvoicePartTable.PART_PRICE_COLUMN)));
		item.setText(InvoicePartTable.EXTENDED_PRICE_COLUMN, extendedPrice.toString());

		ignoreFocusOut = false;
	}

	private void calculateInvoiceTotal() {
		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = partInvoiceTable.getItems();
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
	}

}
