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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class AmountDueDialog extends Dialog{
	
	protected boolean cashiered;
	protected Shell shlAmountDue;
	private Text txtFinalTotal;
	private Text txtAmountTendered;
	private Text txtAmountCharged;

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
		shlAmountDue = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlAmountDue.setSize(450, 280);
		shlAmountDue.setText("Amount Due");
		
		Gui.setDialogAtCenter(shlAmountDue);
		FormLayout fl_shlAmountDue = new FormLayout();
		fl_shlAmountDue.spacing = 5;
		fl_shlAmountDue.marginWidth = 5;
		fl_shlAmountDue.marginHeight = 5;
		shlAmountDue.setLayout(fl_shlAmountDue);
		
		txtFinalTotal = new Text(shlAmountDue, SWT.BORDER);
		FormData fd_txtFinalTotal = new FormData();
		fd_txtFinalTotal.height = 24;
		fd_txtFinalTotal.right = new FormAttachment(95);
		fd_txtFinalTotal.top = new FormAttachment(0, 5);
		fd_txtFinalTotal.left = new FormAttachment(45);
		txtFinalTotal.setLayoutData(fd_txtFinalTotal);
		txtFinalTotal.setEditable(false);
		
		txtAmountTendered = new Text(shlAmountDue, SWT.BORDER);
		FormData fd_txtAmountTendered = new FormData();
		fd_txtAmountTendered.right = new FormAttachment(txtFinalTotal, 0, SWT.RIGHT);
		fd_txtAmountTendered.left = new FormAttachment(txtFinalTotal, 0, SWT.LEFT);
		fd_txtAmountTendered.height = 24;
		fd_txtAmountTendered.top = new FormAttachment(txtFinalTotal);
		txtAmountTendered.setLayoutData(fd_txtAmountTendered);

		Label lblTotalAmountDue = new Label(shlAmountDue, SWT.NONE);
		FormData fd_lblTotalAmountDue = new FormData();
		fd_lblTotalAmountDue.right = new FormAttachment(txtAmountTendered, -10);
		fd_lblTotalAmountDue.top = new FormAttachment(txtFinalTotal, 0, SWT.CENTER);
		lblTotalAmountDue.setLayoutData(fd_lblTotalAmountDue);
		lblTotalAmountDue.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblTotalAmountDue.setText("Total Amount Due:");
		
		Label lblAmountTendered = new Label(shlAmountDue, SWT.NONE);
		FormData fd_lblAmountTendered = new FormData();
		fd_lblAmountTendered.right = new FormAttachment(lblTotalAmountDue, 0, SWT.RIGHT);
		fd_lblAmountTendered.top = new FormAttachment(txtAmountTendered, 0, SWT.CENTER);
		lblAmountTendered.setLayoutData(fd_lblAmountTendered);
		lblAmountTendered.setText("Amount Tendered:");
		lblAmountTendered.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		
		txtAmountCharged = new Text(shlAmountDue, SWT.BORDER);
		FormData fd_txtAmountCharged = new FormData();
		fd_txtAmountCharged.left = new FormAttachment(txtAmountTendered, 0, SWT.LEFT);
		fd_txtAmountCharged.right = new FormAttachment(txtAmountTendered, 0, SWT.RIGHT);
		fd_txtAmountCharged.top = new FormAttachment(txtAmountTendered);
		fd_txtAmountCharged.height = 24;
		txtAmountCharged.setLayoutData(fd_txtAmountCharged);
		
		Button btnChargeRemainderToAcct = new Button(shlAmountDue, SWT.CHECK);
		btnChargeRemainderToAcct.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		FormData fd_btnChargeRemainderToAcct = new FormData();
		fd_btnChargeRemainderToAcct.right = new FormAttachment(lblAmountTendered, 0, SWT.RIGHT);
		fd_btnChargeRemainderToAcct.top = new FormAttachment(txtAmountCharged, 0, SWT.CENTER);
		btnChargeRemainderToAcct.setLayoutData(fd_btnChargeRemainderToAcct);
		btnChargeRemainderToAcct.setText("Charge to Account");
		
		Button btnCashier = new Button(shlAmountDue, SWT.NONE);
		FormData fd_btnCashier = new FormData();
		fd_btnCashier.height = 96;
		fd_btnCashier.width = 256;
		fd_btnCashier.top = new FormAttachment(txtAmountCharged);
		fd_btnCashier.right = new FormAttachment(100, -5);
		btnCashier.setLayoutData(fd_btnCashier);
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
		btnCashier.setText("Cashier");
		
		Button btnCancel = new Button(shlAmountDue, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.width = 160;
		fd_btnCancel.height = 96;
		fd_btnCancel.top = new FormAttachment(btnCashier, 0, SWT.TOP);
		fd_btnCancel.left = new FormAttachment(0, 5);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shlAmountDue.close();
				cashiered = false;
			}
		});
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnCancel.setText("Cancel");
	}
}
