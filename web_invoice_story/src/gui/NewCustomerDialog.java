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
		shlNewCustomer = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlNewCustomer.setSize(592, 389);
		shlNewCustomer.setText("New Customer");
		
		Gui.setDialogAtCenter(shlNewCustomer);
		
		// customer detail text boxes
		Label lblFirstname = new Label(shlNewCustomer, SWT.NONE);
		lblFirstname.setBounds(10, 10, 192, 20);
		lblFirstname.setText("FirstName:");
		txtFirstName = new MyText(shlNewCustomer, SWT.BORDER);
		txtFirstName.setBounds(10, 36, 272, 26);
				
		Label lblLastNameCompanyName = new Label(shlNewCustomer, SWT.NONE);
		lblLastNameCompanyName.setBounds(292, 10, 192, 20);
		lblLastNameCompanyName.setText("Last Name/Company Name:");
		txtLastName = new MyText(shlNewCustomer, SWT.BORDER);
		txtLastName.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtLastName.setBounds(292, 36, 272, 26);
		
		Label lblAddress = new Label(shlNewCustomer, SWT.NONE);
		lblAddress.setBounds(10, 68, 56, 20);
		lblAddress.setText("Address:");
		txtAddress = new MyText(shlNewCustomer, SWT.BORDER);
		txtAddress.setBounds(10, 94, 554, 26);
		
		Label lblCity = new Label(shlNewCustomer, SWT.NONE);
		lblCity.setBounds(10, 126, 192, 20);
		lblCity.setText("City:");
		txtCity = new MyText(shlNewCustomer, SWT.BORDER);
		txtCity.setBounds(10, 152, 272, 26);
		
		Label lblState = new Label(shlNewCustomer, SWT.NONE);
		lblState.setBounds(292, 126, 37, 20);
		lblState.setText("State:");
		txtState = new MyText(shlNewCustomer, SWT.BORDER);
		txtState.setBounds(292, 152, 74, 26);
		
		Label lblZipCode = new Label(shlNewCustomer, SWT.NONE);
		lblZipCode.setBounds(372, 126, 192, 20);
		lblZipCode.setText("Zip Code:");
		txtZipCode = new MyText(shlNewCustomer, SWT.BORDER);
		txtZipCode.setBounds(372, 152, 192, 26);
		
		Label lblHomePhone = new Label(shlNewCustomer, SWT.NONE);
		lblHomePhone.setBounds(10, 184, 192, 20);
		lblHomePhone.setText("Home Phone:");
		txtHomePhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtHomePhone.setBounds(10, 210, 272, 26);
		
		Label lblWorkPhone = new Label(shlNewCustomer, SWT.NONE);
		lblWorkPhone.setBounds(292, 184, 192, 20);
		lblWorkPhone.setText("Work Phone:");
		txtWorkPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtWorkPhone.setBounds(292, 210, 272, 26);
		
		Label lblCellPhone = new Label(shlNewCustomer, SWT.NONE);
		lblCellPhone.setBounds(10, 242, 192, 20);
		lblCellPhone.setText("Cell Phone:");
		txtCellPhone = new MyText(shlNewCustomer, SWT.BORDER);
		txtCellPhone.setBounds(10, 268, 272, 26);
		
		
		Label lblEmail = new Label(shlNewCustomer, SWT.NONE);
		lblEmail.setBounds(292, 242, 192, 20);
		lblEmail.setText("E-Mail:");
		txtEmail = new MyText(shlNewCustomer, SWT.BORDER);
		txtEmail.setBounds(292, 268, 272, 26);
		
		// New Customer dialog controls
		Label label = new Label(shlNewCustomer, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 300, 554, 2);
		
		btnSaveCustomerButton = new Button(shlNewCustomer, SWT.NONE);
		btnSaveCustomerButton.setText("Save Customer");
		btnSaveCustomerButton.setBounds(10, 308, 345, 26);
		
		btnCancel = new Button(shlNewCustomer, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(361, 308, 203, 26);
		
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

		DbServices.saveObject(customer);
		result = customer;
		shlNewCustomer.close();
	}
}
