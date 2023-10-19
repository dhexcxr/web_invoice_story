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
		shlClose.setSize(224, 144);
		shlClose.setText(titlebarText);
		
		Gui.setDialogAtCenter(shlClose);
		
		shlClose.addListener(SWT.Close, event -> {
			Application.getLogger().log(Level.INFO, "Close RO Confirm dialog");
			if (btnDoNotAsk.getSelection()) {
				result[1] = true;
			}
		});
		
		Button btnYes = new Button(shlClose, SWT.NONE);
		btnYes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result[0] = true;
				shlClose.close();
			}
		});
		btnYes.setBounds(10, 36, 90, 30);
		btnYes.setText("Yes");
		
		Button btnNo = new Button(shlClose, SWT.NONE);
		btnNo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result[0] = false;
				shlClose.close();
			}
		});
		btnNo.setBounds(109, 36, 90, 30);
		btnNo.setText("No");
		
		btnDoNotAsk = new Button(shlClose, SWT.CHECK);
		btnDoNotAsk.setBounds(37, 72, 143, 20);
		btnDoNotAsk.setText("Do not ask again");
		
		Label lblReallyClose = new Label(shlClose, SWT.NONE);
		lblReallyClose.setAlignment(SWT.CENTER);
		lblReallyClose.setBounds(10, 10, 189, 20);
		lblReallyClose.setText(message);
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setTitle(String text) {
		this.titlebarText = text;
	}
}
