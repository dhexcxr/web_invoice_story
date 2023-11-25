package gui;

import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import objects.Invoice;
import guiParts.InvoiceSearchResultsTable;
import guiParts.MyText;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class InvoiceSearchDialog extends Dialog {
	
	Shell shlInvoiceSearch;
	Object result;

	public InvoiceSearchDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public Object open() {
		createContents();
		shlInvoiceSearch.open();
		shlInvoiceSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlInvoiceSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createContents() {
		shlInvoiceSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlInvoiceSearch.setText("Invoice Search");
		
		int width = 653;
		int height = 410;
		shlInvoiceSearch.setSize(width, height);
		shlInvoiceSearch.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (shlInvoiceSearch.getSize().x < new Point(width, height).x
						|| shlInvoiceSearch.getSize().y < new Point(width, height).y) {
					shlInvoiceSearch.setSize(width, height);	// set a minimum shell size
				}
				
			}
		});
		
		Gui.setDialogAtCenter(shlInvoiceSearch);
		shlInvoiceSearch.setLayout(new FormLayout());
		
		InvoiceSearchResultsTable invoiceTable = new InvoiceSearchResultsTable(shlInvoiceSearch, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_invoiceTable = new FormData();
		fd_invoiceTable.bottom = new FormAttachment(100, -64);
		fd_invoiceTable.left = new FormAttachment(0, 10);
		invoiceTable.setLayoutData(fd_invoiceTable);
		invoiceTable.setLinesVisible(true);
		invoiceTable.setHeaderVisible(true);
		invoiceTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (invoiceTable.getSelection().length > 0) {
					// return Invoice selected from Customer Search Dialog table on double click
					result =  invoiceTable.getSelection()[0].getData();
					shlInvoiceSearch.close();
				}
			}
		});
				
		MyText invoiceSearchTextBox = new MyText(shlInvoiceSearch, SWT.BORDER);
		fd_invoiceTable.top = new FormAttachment(invoiceSearchTextBox, 6);
		fd_invoiceTable.right = new FormAttachment(invoiceSearchTextBox, 0, SWT.RIGHT);
		FormData fd_invoiceSearchTextBox = new FormData();
		fd_invoiceSearchTextBox.bottom = new FormAttachment(0, 32);
		fd_invoiceSearchTextBox.right = new FormAttachment(100, -10);
		fd_invoiceSearchTextBox.top = new FormAttachment(0, 10);
		fd_invoiceSearchTextBox.left = new FormAttachment(0, 10);
		invoiceSearchTextBox.setLayoutData(fd_invoiceSearchTextBox);
		invoiceSearchTextBox.setText("Search...");
		invoiceSearchTextBox.setFocus();
		invoiceSearchTextBox.addFocusListener(new TextBoxFocusListener(invoiceSearchTextBox));
		invoiceSearchTextBox.addModifyListener(new SearchTextBoxListeners(invoiceSearchTextBox, invoiceTable, new Invoice()));
		
		TableColumn tblclmnInvoiceNumber = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnInvoiceNumber.setWidth(83);
		tblclmnInvoiceNumber.setText("Invoice #");
		tblclmnInvoiceNumber.addSelectionListener(new TableColumnSortListener(tblclmnInvoiceNumber));
		
		TableColumn tblclmnCustomer = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnCustomer.setWidth(180);
		tblclmnCustomer.setText("Customer");
		tblclmnCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCustomer));
		
		TableColumn tblclmnCashierDatetime = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnCashierDatetime.setWidth(152);
		tblclmnCashierDatetime.setText("Cashier Date/Time");
		tblclmnCashierDatetime.addSelectionListener(new TableColumnSortListener(tblclmnCashierDatetime));
		
		TableColumn tblclmnLineItems = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnLineItems.setWidth(82);
		tblclmnLineItems.setText("Line Items");
		tblclmnLineItems.addSelectionListener(new TableColumnSortListener(tblclmnLineItems));
		
		TableColumn tblclmnTotal = new TableColumn(invoiceTable, SWT.NONE);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		tblclmnTotal.addSelectionListener(new TableColumnSortListener(tblclmnTotal));

		// dialog  controls
		Button btnOpenInvoice = new Button(shlInvoiceSearch, SWT.NONE);
		FormData fd_btnOpenInvoice = new FormData();
		fd_btnOpenInvoice.top = new FormAttachment(invoiceTable, 6);
		fd_btnOpenInvoice.left = new FormAttachment(invoiceTable, 0, SWT.LEFT);
		fd_btnOpenInvoice.bottom = new FormAttachment(100, -10);
		fd_btnOpenInvoice.right = new FormAttachment(0, 266);
		btnOpenInvoice.setLayoutData(fd_btnOpenInvoice);
		btnOpenInvoice.setText("Open Invoice");
		btnOpenInvoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (invoiceTable.getSelection().length > 0) {
					// TODO do something with this invoice, like display it in the UI
					result =  invoiceTable.getSelection()[0].getData();
					shlInvoiceSearch.close();
				}
			}
		});

		Button btnCancel = new Button(shlInvoiceSearch, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(btnOpenInvoice, 0, SWT.BOTTOM);
		fd_btnCancel.top = new FormAttachment(invoiceTable, 6);
		fd_btnCancel.right = new FormAttachment(invoiceTable, 0, SWT.RIGHT);
		fd_btnCancel.left = new FormAttachment(100, -266);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlInvoiceSearch.close();
			}
		});
	}

}
