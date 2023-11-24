package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.SWTResourceManager;

import listeners.InfoTextBoxModifyListener;
import listeners.PhoneNumberTextBoxModifyListener;
import listeners.RequiredTextBoxModifyListener;
import objects.Customer;
import web_invoice_story.Application;
import web_invoice_story.DbServices;
import guiParts.MyText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class NewCustomerDialog extends Dialog {

	protected Object result;
	protected Shell shlNewCustomer;
	private MyText txtFirstName;
	private MyText txtAddress;
	private MyText txtCity;
	private MyText txtState;
	private MyText txtZipCode;
	private MyText txtLastName;
	private MyText txtHomePhone;
	private MyText txtWorkPhone;
	private MyText txtCellPhone;
	private MyText txtEmail;
	private Button btnSaveCustomerButton;
	private Button btnCancel;
	// TODO i think this is a hack way to do this, customerId is used in Mouse Down listener for Save button
		// to determine if we need to call addNew... or saveExisting....
	private long customerId = -1;
	private Customer customer;

	public NewCustomerDialog(Shell parent, int style) {
		super(parent, style);
	}
	
	public Object open() {
		createContents();
		setupListeners();
		shlNewCustomer.open();
		shlNewCustomer.layout();
		Display display = getParent().getDisplay();
		while (!shlNewCustomer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(Customer customer) {
		createContents();
		
		if (customer.getFirstName().length() > 0)
			txtFirstName.setText(customer.getFirstName());
		if (customer.getAddress().length() > 0)
			txtAddress.setText(customer.getAddress());
		if (customer.getCity().length() > 0)
			txtCity.setText(customer.getCity());
		if (customer.getState().length() > 0)
			txtState.setText(customer.getState());
		if (customer.getZipCode().length() > 0)
			txtZipCode.setText(customer.getZipCode());
		if (customer.getLastName().length() > 0)
			txtLastName.setText(customer.getLastName());
		if (customer.getHomePhone().length() > 0)
			txtHomePhone.setText(customer.getHomePhone());
		if (customer.getWorkPhone().length() > 0)
			txtWorkPhone.setText(customer.getWorkPhone());
		if (customer.getCellPhone().length() > 0)
			txtCellPhone.setText(customer.getCellPhone());
		if (customer.getEmail().length() > 0)
			txtEmail.setText(customer.getEmail());
		this.customerId = customer.getCustomerId();
		this.customer = customer;
		
		setupListeners();
		
		// open new New Customer dialog, customize for editing current customer
		shlNewCustomer.setText("Edit Customer Details");
		shlNewCustomer.open();
		shlNewCustomer.layout();
		Display display = getParent().getDisplay();
		while (!shlNewCustomer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlNewCustomer = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlNewCustomer.setSize(592, 389);
		shlNewCustomer.setText("New Customer");
		
		Gui.setDialogAtCenter(shlNewCustomer);
		shlNewCustomer.setLayout(new FormLayout());
		
		// customer detail text boxes
		Label lblFirstname = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblFirstname = new FormData();
		fd_lblFirstname.bottom = new FormAttachment(0, 30);
		fd_lblFirstname.right = new FormAttachment(0, 202);
		fd_lblFirstname.top = new FormAttachment(0, 10);
		fd_lblFirstname.left = new FormAttachment(0, 10);
		lblFirstname.setLayoutData(fd_lblFirstname);
		lblFirstname.setText("FirstName:");
		txtFirstName = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtFirstName = new FormData();
		fd_txtFirstName.bottom = new FormAttachment(0, 62);
		fd_txtFirstName.right = new FormAttachment(0, 282);
		fd_txtFirstName.top = new FormAttachment(0, 36);
		fd_txtFirstName.left = new FormAttachment(0, 10);
		txtFirstName.setLayoutData(fd_txtFirstName);
				
		Label lblLastNameCompanyName = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblLastNameCompanyName = new FormData();
		fd_lblLastNameCompanyName.bottom = new FormAttachment(0, 30);
		fd_lblLastNameCompanyName.top = new FormAttachment(0, 10);
		fd_lblLastNameCompanyName.left = new FormAttachment(0, 292);
		lblLastNameCompanyName.setLayoutData(fd_lblLastNameCompanyName);
		lblLastNameCompanyName.setText("Last Name/Company Name:");
		txtLastName = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtLastName = new FormData();
		fd_txtLastName.bottom = new FormAttachment(0, 62);
		fd_txtLastName.right = new FormAttachment(0, 582);
		fd_txtLastName.top = new FormAttachment(0, 36);
		fd_txtLastName.left = new FormAttachment(0, 292);
		txtLastName.setLayoutData(fd_txtLastName);		// TODO fix, this stays red when opening a customer with a last name already
		txtLastName.setBackground(SWTResourceManager.getColor(255, 102, 102));	// TODO fix right side alignment
		
		Label lblAddress = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblAddress = new FormData();
		fd_lblAddress.bottom = new FormAttachment(0, 88);
		fd_lblAddress.right = new FormAttachment(0, 110);
		fd_lblAddress.top = new FormAttachment(0, 68);
		fd_lblAddress.left = new FormAttachment(0, 10);
		lblAddress.setLayoutData(fd_lblAddress);
		lblAddress.setText("Address:");
		txtAddress = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtAddress = new FormData();
		fd_txtAddress.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_txtAddress.bottom = new FormAttachment(0, 120);
		fd_txtAddress.top = new FormAttachment(0, 94);
		fd_txtAddress.left = new FormAttachment(0, 10);
		txtAddress.setLayoutData(fd_txtAddress);
		
		Label lblCity = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblCity = new FormData();
		fd_lblCity.bottom = new FormAttachment(0, 146);
		fd_lblCity.right = new FormAttachment(0, 202);
		fd_lblCity.top = new FormAttachment(0, 126);
		fd_lblCity.left = new FormAttachment(0, 10);
		lblCity.setLayoutData(fd_lblCity);
		lblCity.setText("City:");
		txtCity = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtCity = new FormData();
		fd_txtCity.bottom = new FormAttachment(0, 178);
		fd_txtCity.right = new FormAttachment(0, 282);
		fd_txtCity.top = new FormAttachment(0, 152);
		fd_txtCity.left = new FormAttachment(0, 10);
		txtCity.setLayoutData(fd_txtCity);
		
		Label lblState = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblState = new FormData();
		fd_lblState.bottom = new FormAttachment(0, 146);
		fd_lblState.right = new FormAttachment(0, 356);
		fd_lblState.top = new FormAttachment(0, 126);
		fd_lblState.left = new FormAttachment(0, 292);
		lblState.setLayoutData(fd_lblState);
		lblState.setText("State:");
		txtState = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtState = new FormData();
		fd_txtState.bottom = new FormAttachment(0, 178);
		fd_txtState.top = new FormAttachment(0, 152);
		fd_txtState.left = new FormAttachment(0, 292);
		txtState.setLayoutData(fd_txtState);
		
		Label lblZipCode = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblZipCode = new FormData();
		fd_lblZipCode.top = new FormAttachment(txtAddress, 6);
		fd_lblZipCode.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_lblZipCode.bottom = new FormAttachment(0, 146);
		fd_lblZipCode.left = new FormAttachment(0, 390);
		lblZipCode.setLayoutData(fd_lblZipCode);
		lblZipCode.setText("Zip Code:");
		txtZipCode = new MyText(shlNewCustomer, SWT.BORDER);
		fd_txtState.right = new FormAttachment(txtZipCode, -6);
		FormData fd_txtZipCode = new FormData();
		fd_txtZipCode.top = new FormAttachment(txtCity, 0, SWT.TOP);
		fd_txtZipCode.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_txtZipCode.bottom = new FormAttachment(0, 178);
		fd_txtZipCode.left = new FormAttachment(0, 390);
		txtZipCode.setLayoutData(fd_txtZipCode);
		
		Label lblHomePhone = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblHomePhone = new FormData();
		fd_lblHomePhone.bottom = new FormAttachment(0, 204);
		fd_lblHomePhone.right = new FormAttachment(0, 202);
		fd_lblHomePhone.top = new FormAttachment(0, 184);
		fd_lblHomePhone.left = new FormAttachment(0, 10);
		lblHomePhone.setLayoutData(fd_lblHomePhone);
		lblHomePhone.setText("Home Phone:");
		txtHomePhone = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtHomePhone = new FormData();
		fd_txtHomePhone.bottom = new FormAttachment(0, 236);
		fd_txtHomePhone.right = new FormAttachment(0, 282);
		fd_txtHomePhone.top = new FormAttachment(0, 210);
		fd_txtHomePhone.left = new FormAttachment(0, 10);
		txtHomePhone.setLayoutData(fd_txtHomePhone);
		
		Label lblWorkPhone = new Label(shlNewCustomer, SWT.NONE);
		fd_lblLastNameCompanyName.right = new FormAttachment(lblWorkPhone, 29, SWT.RIGHT);
		FormData fd_lblWorkPhone = new FormData();
		fd_lblWorkPhone.bottom = new FormAttachment(0, 204);
		fd_lblWorkPhone.right = new FormAttachment(0, 484);
		fd_lblWorkPhone.top = new FormAttachment(0, 184);
		fd_lblWorkPhone.left = new FormAttachment(0, 292);
		lblWorkPhone.setLayoutData(fd_lblWorkPhone);
		lblWorkPhone.setText("Work Phone:");
		txtWorkPhone = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtWorkPhone = new FormData();
		fd_txtWorkPhone.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_txtWorkPhone.bottom = new FormAttachment(0, 236);
		fd_txtWorkPhone.top = new FormAttachment(0, 210);
		fd_txtWorkPhone.left = new FormAttachment(0, 292);
		txtWorkPhone.setLayoutData(fd_txtWorkPhone);
		
		Label lblCellPhone = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblCellPhone = new FormData();
		fd_lblCellPhone.bottom = new FormAttachment(0, 262);
		fd_lblCellPhone.right = new FormAttachment(0, 202);
		fd_lblCellPhone.top = new FormAttachment(0, 242);
		fd_lblCellPhone.left = new FormAttachment(0, 10);
		lblCellPhone.setLayoutData(fd_lblCellPhone);
		lblCellPhone.setText("Cell Phone:");
		txtCellPhone = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtCellPhone = new FormData();
		fd_txtCellPhone.bottom = new FormAttachment(0, 294);
		fd_txtCellPhone.right = new FormAttachment(0, 282);
		fd_txtCellPhone.top = new FormAttachment(0, 268);
		fd_txtCellPhone.left = new FormAttachment(0, 10);
		txtCellPhone.setLayoutData(fd_txtCellPhone);
		
		
		Label lblEmail = new Label(shlNewCustomer, SWT.NONE);
		FormData fd_lblEmail = new FormData();
		fd_lblEmail.bottom = new FormAttachment(0, 262);
		fd_lblEmail.right = new FormAttachment(0, 484);
		fd_lblEmail.top = new FormAttachment(0, 242);
		fd_lblEmail.left = new FormAttachment(0, 292);
		lblEmail.setLayoutData(fd_lblEmail);
		lblEmail.setText("E-Mail:");
		txtEmail = new MyText(shlNewCustomer, SWT.BORDER);
		FormData fd_txtEmail = new FormData();
		fd_txtEmail.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_txtEmail.bottom = new FormAttachment(0, 294);
		fd_txtEmail.top = new FormAttachment(0, 268);
		fd_txtEmail.left = new FormAttachment(0, 292);
		txtEmail.setLayoutData(fd_txtEmail);
		
		// New Customer dialog controls
		Label label = new Label(shlNewCustomer, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(txtCellPhone, 8, SWT.BOTTOM);
		fd_label.top = new FormAttachment(txtCellPhone, 6);
		fd_label.right = new FormAttachment(txtLastName, 0, SWT.RIGHT);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		
		btnSaveCustomerButton = new Button(shlNewCustomer, SWT.NONE);
		FormData fd_btnSaveCustomerButton = new FormData();
		fd_btnSaveCustomerButton.right = new FormAttachment(0, 355);
		fd_btnSaveCustomerButton.top = new FormAttachment(0, 308);
		fd_btnSaveCustomerButton.left = new FormAttachment(0, 10);
		btnSaveCustomerButton.setLayoutData(fd_btnSaveCustomerButton);
		btnSaveCustomerButton.setText("Save Customer");
		
		btnCancel = new Button(shlNewCustomer, SWT.NONE);
		fd_btnSaveCustomerButton.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.left = new FormAttachment(btnSaveCustomerButton, 6);
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.top = new FormAttachment(0, 308);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		shlNewCustomer.setTabList(new Control[]{txtFirstName, txtLastName, txtAddress, txtCity, txtState, txtZipCode, txtHomePhone, txtWorkPhone, txtCellPhone, txtEmail, btnSaveCustomerButton, btnCancel});
	}
	
	private void setupListeners() {
		txtFirstName.addModifyListener(new InfoTextBoxModifyListener(txtFirstName));
		txtLastName.addModifyListener(new RequiredTextBoxModifyListener(txtLastName));
//		txtLastName.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		txtAddress.addModifyListener(new InfoTextBoxModifyListener(txtAddress));
		txtCity.addModifyListener(new InfoTextBoxModifyListener(txtCity));
		txtState.addModifyListener(new InfoTextBoxModifyListener(txtState));
		txtZipCode.addModifyListener(new InfoTextBoxModifyListener(txtZipCode));
		txtHomePhone.addModifyListener(new PhoneNumberTextBoxModifyListener(txtHomePhone));
		txtWorkPhone.addModifyListener(new PhoneNumberTextBoxModifyListener(txtWorkPhone));
		txtCellPhone.addModifyListener(new PhoneNumberTextBoxModifyListener(txtCellPhone));
		txtEmail.addModifyListener(new InfoTextBoxModifyListener(txtEmail));
		
		btnSaveCustomerButton.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (customerId == -1) {
					// create a new Customer
					addNewCustomer();
				} else {
					Application.getLogger().log(Level.INFO, "Save existing customer");
					// save modifications to existing customer
					saveCustomer(customer);
				}
			}
		});
		
		btnCancel.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewCustomer.close();
			}
		});
	}
	
	public void addNewCustomer() {
		saveCustomer(new Customer());
	}
	
	public void saveCustomer(Customer customer) {
		
		if (txtLastName.getText().length() > 0) {
			customer.setLastName(txtLastName.getText());
		} else {
			// dialog box stating last name is required
			MessageBox lastNameRequirementBox = new MessageBox(shlNewCustomer, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Last Name or Company Name");
			lastNameRequirementBox.open();
			return;
		}
		
		if(txtFirstName.isModified()) {
			customer.setFirstName(txtFirstName.getText());
		}
		
		if (txtAddress.isModified()) {
			customer.setAddress(txtAddress.getText());
		}

		if (txtCity.isModified()) {
			customer.setCity(txtCity.getText());
		}
		
		if (txtState.isModified()) {
			customer.setState(txtState.getText());
		}
		
		if (txtZipCode.isModified()) {
				customer.setZipCode(txtZipCode.getText());
		}
		
		 
		if (txtHomePhone.isModified()) {		// maybe set phone nums to string in DB
				customer.setHomePhone(txtHomePhone.getText());
		}
		
		if (txtWorkPhone.isModified()) {
			customer.setWorkPhone(txtWorkPhone.getText());
		}
		
		if (txtCellPhone.isModified()) {
			customer.setCellPhone(txtCellPhone.getText());
		}
		
		if (txtEmail.isModified()) {	// TODO make dialog box, if txtEmail.isModified, check its format
			customer.setEmail(txtEmail.getText());					// vs regex to ensure (name)@(domain).(tld)
		}
		
		customer.setLocationId(Application.getSettings().getCurrentLocation());

		DbServices.saveObject(customer);
		result = customer;
		shlNewCustomer.close();
	}
}
