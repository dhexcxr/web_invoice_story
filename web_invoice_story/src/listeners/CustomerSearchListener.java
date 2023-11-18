package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.CustomerSearchDialog;
import objects.Customer;
import guiParts.MyText;

public class CustomerSearchListener extends MouseAdapter{
	
	MyText txtBox;
	String txtBoxStartingText;
	
	public CustomerSearchListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open Customer Search dialog with Owner text box double click
		CustomerSearchDialog customerSearchDialog = new CustomerSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		Customer selectedCustomer;
		if (!txtBox.getText().equals(txtBoxStartingText)) {
			// if we're editing a current project site, pass Owner name to Customer Search Dialog
			selectedCustomer = (Customer) customerSearchDialog.open(txtBox.getText().split("\n")[0]);
		} else {
			// open normal, empty Customer Search Dialog
			selectedCustomer = (Customer) customerSearchDialog.open();
		}
		if (selectedCustomer == null) {
			return;
		}
//		if (selectedCustomer instanceof Customer) {
			// get only last name if there is no first name
			StringBuilder customerData = new StringBuilder(selectedCustomer.getLastName());
			if (selectedCustomer.getFirstName() != null) {
				customerData.append(selectedCustomer.getFirstName().equals("") ? "" :", " + selectedCustomer.getFirstName());
			}
			// TODO we could test for txtBoxStartingText.getEditable
			if (txtBoxStartingText.equals("")) {		// Owner text box in New ProjectSite dialog
				txtBox.setText(customerData.toString());
			} else if (txtBoxStartingText.equals("Customer...")) {		// Customer text box in Repair Order, and Part Invoice tab
				customerData.append("\n" + selectedCustomer.getAddress() + "\n");
				if (selectedCustomer.getCity().equals("") || (selectedCustomer.getState().equals("") && selectedCustomer.getZipCode().equals(""))) {
					customerData.append(selectedCustomer.getCity() + " " + selectedCustomer.getState() + " " + selectedCustomer.getZipCode());
				} else {
					customerData.append(selectedCustomer.getCity() + ", " +	selectedCustomer.getState() + " " + selectedCustomer.getZipCode());
				}
				txtBox.setText(customerData + "\n" + selectedCustomer.getHomePhone() + "\n" + 
						selectedCustomer.getCellPhone() + "\n" + selectedCustomer.getEmail());
			}
			txtBox.setData(selectedCustomer);
//		}
	}

}
