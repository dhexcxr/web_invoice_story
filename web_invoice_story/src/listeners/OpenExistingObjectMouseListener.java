package listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import gui.NewCustomerDialog;
import gui.NewPartDialog;
import gui.NewUnitDialog;
import gui.RepairOrderDialog;
import objects.Customer;
import guiParts.MyTable;
import objects.Part;
import objects.RepairOrder;
import objects.Unit;
import web_invoice_story.Application;

public class OpenExistingObjectMouseListener extends MouseAdapter {

	MyTable table = null;
	Shell shell;


	public OpenExistingObjectMouseListener(MyTable table, Shell shell) {
		this.table = table;
		this.shell = shell;
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		if (e.getSource() instanceof Button) {
			mouseDoubleClick(e);
		}
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// open saved object for editing
		Application.getLogger().log(Level.INFO, "Double clicked to open an existing object");
		Object[] tableObjects = new Object[0];
		// TODO there might be a better way to check what type we're searching
			// just like in DbServices.searchForObject
		if (table.getColumn(0).getText().equals("First Name") && table.getSelection().length > 0) {
			openCustomer(table);
			tableObjects = new Customer[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("Owner") && table.getSelection().length > 0) {
			openUnit(table);
			tableObjects = new Unit[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("Part Number") && table.getSelection().length > 0) {
			openPart(table);
			tableObjects = new Part[table.getItems().length];
		} else if (table.getColumn(0).getText().equals("RO #") && table.getSelection().length > 0) {
			openRo(table);
			tableObjects = new RepairOrder[table.getItems().length];
		} else {		// if nothing is selected, return TODO i'd like to find a better way to do this than
			return;			// checking for all other conditions first, like if (selection == null)
		}

		// here, we repaint table with objects that are currently in it so we don't have to go out to the DB
		for (int i = 0; i < table.getItems().length; i++) {
			tableObjects[i] = table.getItems()[i].getData();
		}
		// TODO paint just at the selectedTableIndex, this will require us to implement paint(Object object, int selectedTableItemIndex)
			// in all MyTable objects as it is implemented in InvoicePartTable
		table.removeAll();
		table.paint(tableObjects);
	}

	private void openCustomer(Table table) {
		// get object saved in TableItem Data
		Customer customer = (Customer) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Opened: {0}", customer.getFirstName());
		Application.getLogger().log(Level.INFO, "Customer ID: {0}", Long.toString(customer.getCustomerId()));

		// open customer for editing
		NewCustomerDialog modifyExistingCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
		modifyExistingCustomerDialog.open(customer);
	}

	private void openUnit(Table table) {
		// get object saved in TableItem Data
		Unit unit = (Unit) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Open unit: {0}", unit.getMake());
		Application.getLogger().log(Level.INFO, "Unit Model: {0}", unit.getModel());
		Application.getLogger().log(Level.INFO, "Unit VIN: {0}", unit.getVin());

		NewUnitDialog modifyExistingUnitDialog = new NewUnitDialog(shell, SWT.NONE);
		modifyExistingUnitDialog.open(unit);
	}
	
	private void openPart(Table table) {
		// get object saved in TableItem Data
		Part part= (Part) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Open part: {0}", part.getPartNumber());
		Application.getLogger().log(Level.INFO, "Part Description: {0}", part.getDescription());
		
		NewPartDialog modifyExistingPartDialog = new NewPartDialog(shell, SWT.NONE);
		modifyExistingPartDialog.open(part);
	}
	
	private void openRo(Table table) {
		// get object saved in TableItem Data
		RepairOrder repairOrder= (RepairOrder) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Open RO: {0}", repairOrder.getRepairOrderId());
		
		RepairOrderDialog modifyExistingRepairOrder= new RepairOrderDialog(shell, SWT.NONE);
		modifyExistingRepairOrder.open(repairOrder);
	}
}
