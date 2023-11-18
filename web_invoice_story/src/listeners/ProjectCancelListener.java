package listeners;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Shell;

import gui.ConfirmDialog;
import gui.ProjectDialog;
import web_invoice_story.Application;

public class ProjectCancelListener extends MouseAdapter {

	private ProjectDialog roDialog;
	private Shell shell;
	
	public ProjectCancelListener(ProjectDialog roDialog) {
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
			roDialog.getTxtCustomerProject().setText("");
			roDialog.getTxtCustomerProject().setData(null);
			roDialog.getTxtProjectSiteProject().setText("");
			roDialog.getTxtProjectSiteProject().setData(null);
			roDialog.getTableJobsProject().removeAll();
			roDialog.disableJobTabs();
			roDialog.setDetailsToDelete(new HashMap<>());
			roDialog.reloadRo();
		}
	}

}
