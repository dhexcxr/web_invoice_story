package listeners;

import java.util.ArrayList;
import java.util.List;
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
import guiParts.MyTable;
import objects.Customer;
import objects.DbObjectSearchable;
import objects.Part;
import objects.Project;
import objects.ProjectSite;
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
		List<DbObjectSearchable> tableObjects = new ArrayList<>();
		// TODO there might be a better way to check what type we're searching
			// just like in DbServices.searchForObject
		// LATER NOTE probably doing an instanceof on the table object we saved up there
		if (table.getColumn(0).getText().equals("First Name") && table.getSelection().length > 0) {
			openCustomer(table);	// followup to LATER NOTE - the thing to do is push this method up to the tables as an abstract openObjects(), then just call table.openObjects()
		} else if (table.getColumn(0).getText().equals("Owner") && table.getSelection().length > 0) {
			openUnit(table);
		} else if (table.getColumn(0).getText().equals("Part Number") && table.getSelection().length > 0) {
			openPart(table);
		} else if (table.getColumn(0).getText().equals("RO #") && table.getSelection().length > 0) {
			openRo(table);
		} else {		// if nothing is selected, return TODO i'd like to find a better way to do this than
			return;			// checking for all other conditions first, like if (selection == null)
		}

		// here, we repaint table with objects that are currently in it so we don't have to go out to the DB
		for (int i = 0; i < table.getItems().length; i++) {
			tableObjects.add((DbObjectSearchable) table.getItems()[i].getData());
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
		ProjectSite projectSite = (ProjectSite) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Open unit: {0}", projectSite.getAddressProjectSite());
		Application.getLogger().log(Level.INFO, "ProjectSite Model: {0}", projectSite.getCityProjectSiteString());
		Application.getLogger().log(Level.INFO, "ProjectSite VIN: {0}", projectSite.getNotesProjectSite());

		NewUnitDialog modifyExistingUnitDialog = new NewUnitDialog(shell, SWT.NONE);
		modifyExistingUnitDialog.open(projectSite);
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
		Project project= (Project) table.getSelection()[0].getData();
		Application.getLogger().log(Level.INFO, "Open RO: {0}", project.getProjectId());
		
		RepairOrderDialog modifyExistingRepairOrder= new RepairOrderDialog(shell, SWT.NONE);
		modifyExistingRepairOrder.open(project);
	}
	
	// TODO make one of these for Account except its probably not for much editing, just for displaying transactions and things
		// it'll also make the repaint easier
}
