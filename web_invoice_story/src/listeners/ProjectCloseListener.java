package listeners;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import gui.ConfirmDialog;
import gui.ProjectDialog;
import web_invoice_story.Application;

public class ProjectCloseListener implements Listener {
	
	private ProjectDialog roDialog;
	private Shell shell;

	public ProjectCloseListener(ProjectDialog roDialog) {
		this.roDialog = roDialog;
		this.shell = roDialog.getRoDialogShell();
	}

	@Override
	public void handleEvent(Event event) {
		Application.getLogger().log(Level.INFO, "Close RO {0}", event.widget);
		boolean skipCloseConfirm = Application.getSettings().skipCloseConfirm();
		boolean close = true;
		if (!skipCloseConfirm && !roDialog.cashiered()) {
			ConfirmDialog confirmDialogBox = new ConfirmDialog (shell, SWT.APPLICATION_MODAL);
			confirmDialogBox.setTitle("Close Repair Order?");
			confirmDialogBox.setMessage("Close the Repair Order?");
			boolean[] response = confirmDialogBox.open();
			close = response[0];
			skipCloseConfirm = response[1];
			
			if (skipCloseConfirm) {
				// do not ask to confirm again
				Application.getSettings().setSkipCloseConfirm(true);
			}
		}
		event.doit = close;
	}

}
