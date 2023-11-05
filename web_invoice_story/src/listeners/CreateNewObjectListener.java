package listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import gui.NewCustomerDialog;
import gui.NewPartDialog;
import gui.NewUnitDialog;
import gui.RepairOrderDialog;
import guiParts.MyTable;
import guiParts.MyText;
import objects.DbObjectSearchable;
import web_invoice_story.Application;

// TODO rename this something better (or refactor more), 'cause we're not just creating objects here
public class CreateNewObjectListener extends MouseAdapter{
	
	MyTable table = null;
	MyText searchBox = null;
	Shell shell;
	
	public CreateNewObjectListener(MyTable table, MyText searchBox, Shell shell) {
		this.table = table;
		this.searchBox = searchBox;
		this.shell = shell;
	}
	
	
	@Override
	public void mouseDown(MouseEvent e) {
		
		// TODO change this to pass in a new Object instead of checking table type, like in Object searches
		Application.getLogger().log(Level.INFO, "Mouse down event on a table, first column: {0}", table.getColumn(0).getText());
		
		Object newObject = null;
		List<DbObjectSearchable> newTableContents = new ArrayList<>();
		if (table.getColumn(0).getText().equals("First Name")) {
			NewCustomerDialog addNewCustomerDialog = new NewCustomerDialog(shell, SWT.NONE);
			newObject = addNewCustomerDialog.open();
		} else if (table.getColumn(0).getText().equals("Owner")) {
			NewUnitDialog addNewUnitDialog = new NewUnitDialog(shell, SWT.NONE);
			newObject = addNewUnitDialog.open();
		} else if (table.getColumn(0).getText().equals("Part Number")) {
			NewPartDialog addNewPartDialog = new NewPartDialog(shell, SWT.NONE);
			newObject = addNewPartDialog.open();
		} else if (table.getColumn(0).getText().equals("Project #")) {
			RepairOrderDialog addNewRepairOrderDialog = new RepairOrderDialog(shell, SWT.NONE);
			newObject = addNewRepairOrderDialog.open();
		}
		
		// repaint table - get current data in TableItems, insert the new data at the end, then call table.paint
			// TODO if there is no search query in SearchBox do not add new object to table
		for (int i = 0; i < table.getItems().length; i++) {
			newTableContents.add((DbObjectSearchable) table.getItems()[i].getData());
		}
		newTableContents.add(table.getItems().length, (DbObjectSearchable) newObject);
		table.removeAll();
		table.paint(newTableContents);
	}	
}
