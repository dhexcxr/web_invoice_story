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
		shlSellQuantityMore.setSize(488, 344);
		shlSellQuantityMore.setText("Sell Quantity more than On Hand");
		
		Gui.setDialogAtCenter(shlSellQuantityMore);
		
		Label lblSomePartsOn = new Label(shlSellQuantityMore, SWT.WRAP | SWT.CENTER);
		lblSomePartsOn.setBounds(10, 10, 455, 89);
		lblSomePartsOn.setText("Some Parts' On Hand quantity has changed and is now less than the Sell quantity.\r\n\r\nSell quantity has been changed to On Hand quantity.");
		
		Button btnOk = new Button(shlSellQuantityMore, SWT.NONE);
		btnOk.setBounds(192, 260, 90, 30);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlSellQuantityMore.close();
			}
		});
		
		Label lblAffectedParts = new Label(shlSellQuantityMore, SWT.NONE);
		lblAffectedParts.setBounds(10, 105, 95, 20);
		lblAffectedParts.setText("Affected Parts:");
		
		Text textAffectedPartsList = new Text(shlSellQuantityMore, SWT.BORDER | SWT.WRAP);
		textAffectedPartsList.setEditable(false);
		textAffectedPartsList.setBounds(10, 131, 455, 123);
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
