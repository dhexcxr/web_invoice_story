package listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import objects.Customer;
import objects.DbObjectSearchable;
import objects.Invoice;
import guiParts.MyTable;
import guiParts.MyText;
import objects.Part;
import objects.RepairOrder;
import objects.Unit;

public class SearchTextBoxListeners implements ModifyListener {
	
	private MyText searchBox;
	private MyTable table;
	private DbObjectSearchable searchObject;
	private String textBoxStartingText;
	
	public SearchTextBoxListeners(MyText textBox, MyTable table) {
		this.searchBox = textBox;
		this.table = table;
	}
	
	public SearchTextBoxListeners(MyText textBox, MyTable table, DbObjectSearchable searchObject) {
		this.searchBox = textBox;
		this.table = table;
		this.searchObject = searchObject;
		this.textBoxStartingText = textBox.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// TODO refactor this listener, call searchObject.setSearchString(searchBox.getText()) then pass that to 
			// DbServices.searchForObject(searchObject), then we can remove all these conditionals
		int queryLength = searchBox.getText().trim().length();
		if (queryLength > 0 && !searchBox.getText().equals(textBoxStartingText)) {
			table.removeAll();
			
			// TODO i think I can remove all of these
			if (searchObject instanceof Customer) {
				searchObject = new Customer();
			} else if (searchObject instanceof Part) {
				searchObject = new Part(searchBox.getText());
			} else if (searchObject instanceof Unit) {
				searchObject = new Unit(searchBox.getText());
			} else if (searchObject instanceof Invoice) {
				searchObject = new Invoice(searchBox.getText());
			} else if (searchObject instanceof RepairOrder) {
				searchObject = new RepairOrder(searchBox.getText());
			}
//			table.paint(DbServices.searchForObject(searchObject));
			table.paint(searchObject.search(searchBox.getText()));
		} else if (queryLength == 0) {
			table.removeAll();
		}
	}
}
