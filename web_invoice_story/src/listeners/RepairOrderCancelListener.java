package listeners;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import gui.ConfirmDialog;
import gui.RepairOrderDialog;
import web_invoice_story.Application;

public class RepairOrderCancelListener extends MouseAdapter {

	private RepairOrderDialog roDialog;
	private Shell shell;
	
	public RepairOrderCancelListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.shell = roDialog.getRoDialogShell();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		boolean skipCancelConfirm = Application.getSettings().skipCancelConfirm();
		boolean cancelChanges = true;
		if (!skipCancelConfirm) {
			ConfirmDialog confirmDialogBox = new ConfirmDialog (shell, SWT.APPLICATION_MODAL);
			confirmDialogBox.setTitle("Discard Changes?");
			confirmDialogBox.setMessage("Discard unsaved changes?");
			boolean[] response = confirmDialogBox.open();
			cancelChanges = response[0];
			skipCancelConfirm = response[1];
			
			if (skipCancelConfirm) {
				// do not ask to confirm again
				Application.getSettings().setSkipCancelConfirm(true);
			}
		}
		
		if (cancelChanges) {
			// TODO remove customer and unit
			roDialog.getTxtCustomerRepairOrder().setText("");
			roDialog.getTxtCustomerRepairOrder().setData(null);
			roDialog.getTxtUnitRepairOrder().setText("");
			roDialog.getTxtUnitRepairOrder().setData(null);
			roDialog.getTableJobsRepairOrder().removeAll();
			roDialog.disableJobTabs();
			roDialog.setDetailsToDelete(new HashMap<>());
			roDialog.reloadRo();
		}
	}

}
