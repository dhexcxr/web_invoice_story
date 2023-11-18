package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import web_invoice_story.Application;

import org.eclipse.swt.widgets.Button;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class ConfirmDialog extends Dialog {

	protected boolean[] result = {false, false};
	protected Shell shlClose;
	private Button btnDoNotAsk;
	private String message = "";
	private String titlebarText = "";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ConfirmDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public boolean[] open() {
		createContents();
		shlClose.open();
		shlClose.layout();
		Display display = getParent().getDisplay();
		while (!shlClose.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlClose = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlClose.setSize(224, 166);
		shlClose.setText(titlebarText);
		
		Gui.setDialogAtCenter(shlClose);
		
		shlClose.addListener(SWT.Close, event -> {
			Application.getLogger().log(Level.INFO, "Close RO Confirm dialog");
			if (btnDoNotAsk.getSelection()) {
				result[1] = true;
			}
		});
		shlClose.setLayout(new FormLayout());
		
		Button btnYes = new Button(shlClose, SWT.NONE);
		FormData fd_btnYes = new FormData();
		fd_btnYes.right = new FormAttachment(50, -5);
		btnYes.setLayoutData(fd_btnYes);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result[0] = true;
				shlClose.close();
			}
		});
		btnYes.setText("Yes");
		
		Button btnNo = new Button(shlClose, SWT.NONE);
		fd_btnYes.bottom = new FormAttachment(btnNo, 0, SWT.BOTTOM);
		fd_btnYes.top = new FormAttachment(0, 36);
		FormData fd_btnNo = new FormData();
		fd_btnNo.right = new FormAttachment(100, -10);
		fd_btnNo.left = new FormAttachment(50, 10);
		btnNo.setLayoutData(fd_btnNo);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result[0] = false;
				shlClose.close();
			}
		});
		btnNo.setText("No");
		
		btnDoNotAsk = new Button(shlClose, SWT.CHECK);		
		btnDoNotAsk.setText("Do not ask again");
		btnDoNotAsk.setAlignment(SWT.CENTER);
		fd_btnNo.bottom = new FormAttachment(100, -50);
		FormData fd_btnDoNotAsk = new FormData();
		fd_btnDoNotAsk.top = new FormAttachment(btnYes, 10);
//		int rightOffset = (btnDoNotAsk.getParent().computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2) + (btnDoNotAsk.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2);
		int rightOffset = (btnDoNotAsk.computeSize(SWT.DEFAULT, SWT.DEFAULT).x / 2);
		fd_btnDoNotAsk.right = new FormAttachment(50, rightOffset);
		btnDoNotAsk.setLayoutData(fd_btnDoNotAsk);
		
		
		Label lblReallyClose = new Label(shlClose, SWT.NONE);
		fd_btnNo.top = new FormAttachment(lblReallyClose, 6);
		fd_btnYes.left = new FormAttachment(lblReallyClose, 0, SWT.LEFT);
		FormData fd_lblReallyClose = new FormData();
		fd_lblReallyClose.bottom = new FormAttachment(0, 30);
		fd_lblReallyClose.right = new FormAttachment(100, -10);
		fd_lblReallyClose.top = new FormAttachment(0, 10);
		fd_lblReallyClose.left = new FormAttachment(0, 10);
		lblReallyClose.setLayoutData(fd_lblReallyClose);
		lblReallyClose.setAlignment(SWT.CENTER);
		lblReallyClose.setText(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setTitle(String text) {
		this.titlebarText = text;
	}
}
