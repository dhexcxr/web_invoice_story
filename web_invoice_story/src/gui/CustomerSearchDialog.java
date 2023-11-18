package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;

import listeners.OpenExistingObjectMouseListener;
import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import objects.Customer;
import guiParts.CustomerTable;
import guiParts.MyText;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

// TODO copy most of Customer tab into here, i.e. search and table display, when a selection is made in table
		// return Customer object to insert data into RO or unit info 

// TODO add lsiteners so that hitting [Enter] on a selected Customer in the table will select it and return
	// also the same thing for hitting [Enter] on the Select Customer button
	// also, allow [Esc] to close dialog

public class CustomerSearchDialog extends Dialog {

	protected Object result;		// TODO probably change this to Customer, wouldn't need cast for results
	protected Shell shlCustomerSearch;
	private CustomerTable customerTable;
	private MyText customerSearchTextBox;
	
	public CustomerSearchDialog(Shell parent, int style) {
		super(parent, style);
	}

	public Object open() {
		createContents();
		shlCustomerSearch.open();
		shlCustomerSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlCustomerSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(String customer) {
		createContents();
		
		// populate Search Text Box with selected Owner
		customerSearchTextBox.setText(customer);
		shlCustomerSearch.open();
		shlCustomerSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlCustomerSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlCustomerSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlCustomerSearch.setText("Customer Search");
		
		int width = 946;
		int height = 410;
		shlCustomerSearch.setSize(width, height);
		shlCustomerSearch.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (shlCustomerSearch.getSize().x < new Point(width, height).x
						|| shlCustomerSearch.getSize().y < new Point(width, height).y) {
					shlCustomerSearch.setSize(width, height);	// set a minimum shell size
				}
				
			}
		});
		
		Gui.setDialogAtCenter(shlCustomerSearch);
		shlCustomerSearch.setLayout(new FormLayout());
						
		// search results table
		customerTable = new CustomerTable(shlCustomerSearch, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_customerTable = new FormData();
		fd_customerTable.left = new FormAttachment(0, 10);
		fd_customerTable.right = new FormAttachment(100, -10);
		customerTable.setLayoutData(fd_customerTable);
		customerTable.setLinesVisible(true);
		customerTable.setHeaderVisible(true);
		customerTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.close();
				}
			}
		});
		
		TableColumn tblclmnFirstName = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstName.setText("First Name");
		tblclmnFirstName.setWidth(100);
		tblclmnFirstName.addSelectionListener(new TableColumnSortListener(tblclmnFirstName));
		
		TableColumn tblclmnLastName = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastName.setText("Last Name");
		tblclmnLastName.setWidth(100);
		tblclmnLastName.addSelectionListener(new TableColumnSortListener(tblclmnLastName));
		
		TableColumn tblclmnAddress = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddress.setText("Address");
		tblclmnAddress.setWidth(100);
		tblclmnAddress.addSelectionListener(new TableColumnSortListener(tblclmnAddress));
		
		TableColumn tblclmnCity = new TableColumn(customerTable, SWT.NONE);
		tblclmnCity.setText("City");
		tblclmnCity.setWidth(100);
		tblclmnCity.addSelectionListener(new TableColumnSortListener(tblclmnCity));
		
		TableColumn tblclmnState = new TableColumn(customerTable, SWT.NONE);
		tblclmnState.setText("State");
		tblclmnState.setWidth(100);
		tblclmnState.addSelectionListener(new TableColumnSortListener(tblclmnState));
		
		TableColumn tblclmnZip = new TableColumn(customerTable, SWT.NONE);
		tblclmnZip.setText("Zip");
		tblclmnZip.setWidth(100);
		tblclmnZip.addSelectionListener(new TableColumnSortListener(tblclmnZip));
		
		TableColumn tblclmnHomePhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhone.setText("Home Phone");
		tblclmnHomePhone.setWidth(100);
		tblclmnHomePhone.addSelectionListener(new TableColumnSortListener(tblclmnHomePhone));
		
		TableColumn tblclmnCellPhone = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhone.setText("Cell Phone");
		tblclmnCellPhone.setWidth(100);
		tblclmnCellPhone.addSelectionListener(new TableColumnSortListener(tblclmnCellPhone));
		
		TableColumn tblclmnEmail = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmail.setText("E-mail");
		tblclmnEmail.setWidth(100);
		tblclmnEmail.addSelectionListener(new TableColumnSortListener(tblclmnEmail));
		
		customerSearchTextBox = new MyText(shlCustomerSearch, SWT.BORDER);
		fd_customerTable.top = new FormAttachment(customerSearchTextBox, 6);
		FormData fd_customerSearchTextBox = new FormData();
		fd_customerSearchTextBox.bottom = new FormAttachment(0, 32);
		fd_customerSearchTextBox.right = new FormAttachment(100, -10);
		fd_customerSearchTextBox.top = new FormAttachment(0, 10);
		fd_customerSearchTextBox.left = new FormAttachment(0, 10);
//		fd_customerSearchTextBox.bottom = new FormAttachment(0, 36);
//		fd_customerSearchTextBox.right = new FormAttachment(0, 919);
//		fd_customerSearchTextBox.top = new FormAttachment(0, 10);
//		fd_customerSearchTextBox.left = new FormAttachment(0, 10);
		customerSearchTextBox.setLayoutData(fd_customerSearchTextBox);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setFocus();
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable, new Customer()));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));

		// dialog  controls
		Button btnSelectCustomer = new Button(shlCustomerSearch, SWT.NONE);
		fd_customerTable.bottom = new FormAttachment(100, -64);
		FormData fd_btnSelectCustomer = new FormData();
		fd_btnSelectCustomer.top = new FormAttachment(customerTable, 10);
		fd_btnSelectCustomer.right = new FormAttachment(0, 266);
		fd_btnSelectCustomer.bottom = new FormAttachment(100, -10);
		fd_btnSelectCustomer.left = new FormAttachment(0, 10);
		btnSelectCustomer.setLayoutData(fd_btnSelectCustomer);
		btnSelectCustomer.setText("Select Customer");
		btnSelectCustomer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (customerTable.getSelection().length > 0) {
					result =  customerTable.getSelection()[0].getData();
					shlCustomerSearch.close();
				}
			}
		});
		
		Button btnEditCustomer = new Button(shlCustomerSearch, SWT.NONE);
		btnEditCustomer.setText("Edit Selected Customer");
		FormData fd_btnEditCustomer = new FormData();
		fd_btnEditCustomer.top = new FormAttachment(customerTable, 10);
		fd_btnEditCustomer.left = new FormAttachment(btnSelectCustomer, 48);
		fd_btnEditCustomer.bottom = new FormAttachment(100, -10);
		fd_btnEditCustomer.right = new FormAttachment(0, 631);
		btnEditCustomer.setLayoutData(fd_btnEditCustomer);
		btnEditCustomer.addMouseListener(new OpenExistingObjectMouseListener(customerTable, shlCustomerSearch));

		Button btnCancel = new Button(shlCustomerSearch, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(btnSelectCustomer, 0, SWT.BOTTOM);
		fd_btnCancel.top = new FormAttachment(btnSelectCustomer, 0, SWT.TOP);
		fd_btnCancel.right = new FormAttachment(customerTable, 0, SWT.RIGHT);
		fd_btnCancel.left = new FormAttachment(100, -266);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		shlCustomerSearch.setTabList(new Control[]{customerSearchTextBox, customerTable, btnSelectCustomer, btnCancel});
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlCustomerSearch.close();
			}
		});
	}
}
