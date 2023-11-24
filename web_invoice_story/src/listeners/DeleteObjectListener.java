package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import objects.DbObjectSearchable;
import web_invoice_story.DbServices;
import guiParts.MyTable;

public class DeleteObjectListener extends SelectionAdapter {

	private MyTable table;
	
	public DeleteObjectListener(MyTable table) {
		this.table = table;
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		int linesUpdated = 0;
		if (table.getSelectionIndex() >= 0 && table.getSelectionIndex() < table.getItemCount() &&
				table.getItem(table.getSelectionIndex()).getData() != null) {
			linesUpdated = DbServices.deleteObject((DbObjectSearchable) table.getItem(table.getSelectionIndex()).getData());
		}
		if (linesUpdated == 0) {
			MessageBox objectDeleteError = new MessageBox(new Shell(), SWT.ERROR);
			objectDeleteError.setText("Error");
			objectDeleteError.setMessage("Unable to delete object...");
			objectDeleteError.open();
		} else {
			table.remove(table.getSelectionIndex());
		}
	}
}
