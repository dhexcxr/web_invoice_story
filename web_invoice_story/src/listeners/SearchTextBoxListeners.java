package listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import guiParts.MyTable;
import guiParts.MyText;
import objects.DbObjectSearchable;

public class SearchTextBoxListeners implements ModifyListener {
	
	// TODO see if i can disable right-click browser menu in the SearchTextBox, and the Mac autocomplete
	
	private MyText searchBox;
	private MyTable table;
	private transient DbObjectSearchable searchObject;
	private String textBoxStartingText;
	
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
			table.paint(searchObject.search(searchBox.getText()));
		} else if (queryLength == 0) {
			table.removeAll();
		}
	}
}
