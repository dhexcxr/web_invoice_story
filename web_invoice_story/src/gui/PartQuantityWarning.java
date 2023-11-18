package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;

import objects.JobPart;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class PartQuantityWarning extends Dialog {

	protected Object result;
	protected Shell shlSellQuantityMore;
	private String affectedPartsList = "";

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PartQuantityWarning(Shell parent, int style) {
		super(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlSellQuantityMore.open();
		shlSellQuantityMore.layout();
		Display display = getParent().getDisplay();
		while (!shlSellQuantityMore.isDisposed()) {
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
		shlSellQuantityMore = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlSellQuantityMore.setSize(488, 316);
		shlSellQuantityMore.setText("Sell Quantity more than On Hand");
		
		Gui.setDialogAtCenter(shlSellQuantityMore);
		shlSellQuantityMore.setLayout(new FormLayout());
		
		Label lblSomePartsOn = new Label(shlSellQuantityMore, SWT.WRAP | SWT.CENTER);
		FormData fd_lblSomePartsOn = new FormData();
		fd_lblSomePartsOn.left = new FormAttachment(0, 10);
		fd_lblSomePartsOn.right = new FormAttachment(100, -10);
		fd_lblSomePartsOn.bottom = new FormAttachment(0, 74);
		fd_lblSomePartsOn.top = new FormAttachment(0, 10);
		lblSomePartsOn.setLayoutData(fd_lblSomePartsOn);
		lblSomePartsOn.setText("Some Parts' On Hand quantity has changed and is now less than the Sell quantity.\r\n\r\nSell quantity has been changed to On Hand quantity.");
		
		Button btnOk = new Button(shlSellQuantityMore, SWT.NONE);
		FormData fd_btnOk = new FormData();
		fd_btnOk.left = new FormAttachment(0, 199);
		fd_btnOk.right = new FormAttachment(100, -199);
		fd_btnOk.bottom = new FormAttachment(100, -10);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlSellQuantityMore.close();
			}
		});
		
		Label lblAffectedParts = new Label(shlSellQuantityMore, SWT.NONE);
		FormData fd_lblAffectedParts = new FormData();
		fd_lblAffectedParts.top = new FormAttachment(lblSomePartsOn, 6);
		fd_lblAffectedParts.left = new FormAttachment(lblSomePartsOn, 0, SWT.LEFT);
		fd_lblAffectedParts.bottom = new FormAttachment(0, 100);
		fd_lblAffectedParts.right = new FormAttachment(0, 105);
		lblAffectedParts.setLayoutData(fd_lblAffectedParts);
		lblAffectedParts.setText("Affected Parts:");
		
		Text textAffectedPartsList = new Text(shlSellQuantityMore, SWT.BORDER | SWT.WRAP);
		fd_btnOk.top = new FormAttachment(textAffectedPartsList, 6);
		FormData fd_textAffectedPartsList = new FormData();
		fd_textAffectedPartsList.top = new FormAttachment(lblAffectedParts, 6);
		fd_textAffectedPartsList.left = new FormAttachment(lblSomePartsOn, 0, SWT.LEFT);
		fd_textAffectedPartsList.bottom = new FormAttachment(0, 229);
		fd_textAffectedPartsList.right = new FormAttachment(0, 478);
		textAffectedPartsList.setLayoutData(fd_textAffectedPartsList);
		textAffectedPartsList.setEditable(false);
		textAffectedPartsList.setText(affectedPartsList);
	}

	public void setParts(Map<String, List<JobPart>> changedParts) {
		StringBuilder changedPartsList = new StringBuilder();
		for (Entry<String, List<JobPart>> part : changedParts.entrySet()) {
			changedPartsList.append("Job Name: " + part.getKey() + "\n");
			for (JobPart jobPart : part.getValue()) {
				changedPartsList.append("\tPart: " + jobPart.getPartNumber() + " - " + jobPart.getDescription() + "\n");
			}
		}
		affectedPartsList = changedPartsList.toString();
	}
}
