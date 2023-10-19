package gui;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class AmountDueDialog extends Dialog{
	
	protected boolean cashiered;
	protected Shell shlAmountDue;
	private Text txtFinalTotal;
	private Text txtAmountTendered;

	public AmountDueDialog(Shell parent, int style) {
		super(parent, style);
	}

	public boolean open(String finalTotal) {
		createContents();
		txtFinalTotal.setText(finalTotal);
		if (new BigDecimal(finalTotal.replaceAll("[^-.0-9]", "")).compareTo(BigDecimal.valueOf(0)) < 0) {
			txtAmountTendered.setText("$0");
		}
		shlAmountDue.open();
		shlAmountDue.layout();
		Display display = getParent().getDisplay();
		while (!shlAmountDue.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return cashiered;
	}
	
	private void createContents() {
		shlAmountDue = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlAmountDue.setSize(450, 265);
		shlAmountDue.setText("Amount Due");
		
		Gui.setDialogAtCenter(shlAmountDue);
		
		Label lblTotalAmountDue = new Label(shlAmountDue, SWT.NONE);
		lblTotalAmountDue.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblTotalAmountDue.setBounds(10, 10, 189, 31);
		lblTotalAmountDue.setText("Total Amount Due:");
		
		Label lblAmountTendered = new Label(shlAmountDue, SWT.NONE);
		lblAmountTendered.setText("Amount Tendered:");
		lblAmountTendered.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblAmountTendered.setBounds(10, 47, 189, 31);
		
		txtFinalTotal = new Text(shlAmountDue, SWT.BORDER);
		txtFinalTotal.setEditable(false);
		txtFinalTotal.setBounds(205, 10, 189, 31);
		
		txtAmountTendered = new Text(shlAmountDue, SWT.BORDER);
		txtAmountTendered.setBounds(205, 47, 189, 31);
		
		Button btnCashier = new Button(shlAmountDue, SWT.NONE);
		btnCashier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {						// TODO don't allow anything but numbers
				BigDecimal total = new BigDecimal(txtFinalTotal.getText().replaceAll("[^-.0-9]", ""));
				BigDecimal tendered = new BigDecimal(txtAmountTendered.getText().replace("$", ""));
				BigDecimal chageDue = total.subtract(tendered);
				// if not enough money entered, complain
				if (chageDue.compareTo(BigDecimal.valueOf(0)) > 0) {
					MessageBox notEnoughTenderedMessageBox = new MessageBox(shlAmountDue, SWT.ICON_INFORMATION);
					notEnoughTenderedMessageBox.setText("Notice");
					notEnoughTenderedMessageBox.setMessage("Customer did not tender enough money...");
					notEnoughTenderedMessageBox.open();
					return;
				}	
				MessageBox changeDueMessageBox= new MessageBox(shlAmountDue, getStyle());
				changeDueMessageBox.setText("Change Due");
				changeDueMessageBox.setMessage("Change due to Customer: $" + chageDue.toString().replace("-", ""));
				changeDueMessageBox.open();
				shlAmountDue.close();
				cashiered = true;
			}
		});
		btnCashier.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnCashier.setBounds(147, 112, 275, 96);
		btnCashier.setText("Cashier");
		
		Button btnNewButton = new Button(shlAmountDue, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlAmountDue.close();
				cashiered = false;
			}
		});
		btnNewButton.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnNewButton.setBounds(10, 112, 131, 96);
		btnNewButton.setText("Cancel");
	}
}
