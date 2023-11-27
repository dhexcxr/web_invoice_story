package gui;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.MyText;
import objects.AmountDueResponse;
import objects.AmountDueResponse.Result;
import objects.Invoice;
import objects.Invoice.InvoiceResult;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;

public class AmountDueDialog extends Dialog{
	
	protected boolean cashiered;
	protected Shell shlAmountDue;
	private Text txtFinalTotal;
	private Text txtAmountTendered;
	private Text txtAmountCharged;
	Label lblTotalAmountDue;
	Label lblAmountTendered;
	
	private BigDecimal amountCharged;
	
	private Button btnChargeRemainderToAcct;
	private Button btnCashier;
	private Button btnCancel;
	
	
	private AmountDueResponse response = new AmountDueResponse();		// NOTE this should allow to remove the hack txt fields i currently use to return the Ammount Charged

	public AmountDueDialog(Shell parent, int style) {
		super(parent, style);
	}

	public BigDecimal getAmountCharged() {
		return amountCharged;
	}

	public void setAmountCharged(BigDecimal amountCharged) {
		this.amountCharged = amountCharged;
	}
	
	public AmountDueResponse open(MyText finalTotalText) {
		return open(finalTotalText.getText());
	}

	public AmountDueResponse open(String finalTotal) {
		createContents();
		txtFinalTotal.setText(finalTotal);
		shlAmountDue.setTabList(new Control[]{txtFinalTotal, txtAmountTendered, btnChargeRemainderToAcct, txtAmountCharged, btnCancel, btnCashier});
		if (new BigDecimal(finalTotal.replaceAll("[^-.0-9]", "")).compareTo(BigDecimal.valueOf(0)) < 0) {
			txtAmountTendered.setText("$0");
			txtAmountTendered.setEditable(false);		// if we're issuing a refund, don't allow any edit of Amount Tendered
		}
		shlAmountDue.open();
		shlAmountDue.layout();
		Display display = getParent().getDisplay();
		while (!shlAmountDue.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return response;
	}
	
	public AmountDueResponse open(BigDecimal accountBalance) {		// once we finish refactoring we'll go back to only 1 of these
												// or maybe 2, this one with BigDecimal parameter for Account Payments
					// make class properties to hold the Message and Dialog Title text
		createContents();
		response.setResult(Result.PAYMENT);
		
		lblTotalAmountDue.setText("Current Account Balance");
		txtFinalTotal.setText("$" + accountBalance.toString());
		btnChargeRemainderToAcct.setVisible(false);		// remove options to charge account payments to the account
		txtAmountCharged.setVisible(false);
		
		btnCashier.setText("Payment");
		
		shlAmountDue.setTabList(new Control[]{txtFinalTotal, txtAmountTendered, btnChargeRemainderToAcct, txtAmountCharged, btnCancel, btnCashier});
//		if (new BigDecimal(finalTotal.replaceAll("[^-.0-9]", "")).compareTo(BigDecimal.valueOf(0)) < 0) {
//			txtAmountTendered.setText("$0");
//		}
		shlAmountDue.open();
		shlAmountDue.layout();
		Display display = getParent().getDisplay();
		while (!shlAmountDue.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return response;
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
		txtAmountTendered.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				BigDecimal total = new BigDecimal(txtFinalTotal.getText().replaceAll("[^-.0-9]", ""));	// TODO i need to figure out a better way to handle "valid" chars like - and . that aren't digits
				BigDecimal tendered = new BigDecimal(txtAmountTendered.getText().replaceAll("[^0-9]", "").length() == 0 ? "0" : txtAmountTendered.getText().replaceAll("[^.0-9]", ""));
				if (tendered.compareTo(total) >= 0) {	// if customer is tendering more than total charge
					btnChargeRemainderToAcct.setSelection(false);	// unselect Charge to Account
					btnChargeRemainderToAcct.setEnabled(false);		// disable option to charge to account
					txtAmountCharged.setText("");		// clear calculated amount to charge
				} else {
					btnChargeRemainderToAcct.setEnabled(true);
					btnChargeRemainderToAcct.notifyListeners(SWT.Selection, new Event());	// calculate new amount charged, might just make a method that does this
				}
			}
		});
		FormData fd_txtAmountTendered = new FormData();
		fd_txtAmountTendered.right = new FormAttachment(txtFinalTotal, 0, SWT.RIGHT);
		fd_txtAmountTendered.left = new FormAttachment(txtFinalTotal, 0, SWT.LEFT);
		fd_txtAmountTendered.height = 24;
		fd_txtAmountTendered.top = new FormAttachment(txtFinalTotal);
		txtAmountTendered.setLayoutData(fd_txtAmountTendered);

		lblTotalAmountDue = new Label(shlAmountDue, SWT.NONE);
		FormData fd_lblTotalAmountDue = new FormData();
		fd_lblTotalAmountDue.right = new FormAttachment(txtAmountTendered, -10);
		fd_lblTotalAmountDue.top = new FormAttachment(txtFinalTotal, 0, SWT.CENTER);
		lblTotalAmountDue.setLayoutData(fd_lblTotalAmountDue);
		lblTotalAmountDue.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblTotalAmountDue.setText("Total Amount Due:");
		
		lblAmountTendered = new Label(shlAmountDue, SWT.NONE);
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
		txtAmountCharged.setEditable(false);
		txtAmountCharged.setBackground(SWTResourceManager.getColor(239, 239, 239));
		
		btnChargeRemainderToAcct = new Button(shlAmountDue, SWT.CHECK);
		btnChargeRemainderToAcct.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// calculate remainder of total and charge to account
				BigDecimal total = new BigDecimal(txtFinalTotal.getText().replaceAll("[^-.0-9]", ""));	// TODO i need to figure out a better way to handle "valid" chars like - and . that aren't digits
				BigDecimal tendered = new BigDecimal(txtAmountTendered.getText().replaceAll("[^0-9]", "").length() == 0 ? "0" : txtAmountTendered.getText().replaceAll("[^-.0-9]", ""));
				
				if (btnChargeRemainderToAcct.getSelection()) {
					txtAmountCharged.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					txtAmountCharged.setText(total.subtract(tendered).setScale(2, RoundingMode.HALF_UP).toString());
				} else {	// if this is unchecked now
					txtAmountCharged.setText("");			// clear the Amount Charged box
				}
			}
		});
		btnChargeRemainderToAcct.setFont(SWTResourceManager.getFont(".AppleSystemUIFont", 14, SWT.NORMAL));
		FormData fd_btnChargeRemainderToAcct = new FormData();
		fd_btnChargeRemainderToAcct.right = new FormAttachment(lblAmountTendered, 0, SWT.RIGHT);
		fd_btnChargeRemainderToAcct.top = new FormAttachment(txtAmountCharged, 0, SWT.CENTER);
		btnChargeRemainderToAcct.setLayoutData(fd_btnChargeRemainderToAcct);
		btnChargeRemainderToAcct.setText("Charge to Account");
		
		btnCashier = new Button(shlAmountDue, SWT.NONE);
		FormData fd_btnCashier = new FormData();
		fd_btnCashier.height = 96;
		fd_btnCashier.width = 256;
		fd_btnCashier.top = new FormAttachment(txtAmountCharged);
		fd_btnCashier.right = new FormAttachment(100, -5);
		btnCashier.setLayoutData(fd_btnCashier);
		btnCashier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				// prepare to tell the user the result
				BigDecimal total = new BigDecimal(txtFinalTotal.getText().replaceAll("[^-.0-9]", ""));
				BigDecimal tendered = new BigDecimal(txtAmountTendered.getText().replaceAll("[^-.0-9]", ""));
				MessageBox changeDueMessageBox= new MessageBox(shlAmountDue, getStyle());
				
				if (response.getResult() == Result.PAYMENT) {
					// paying an invoice to pay down account balance
					if (tendered.compareTo(total) > 0) {
						response.setAmountCharged(tendered.negate());
						changeDueMessageBox.setText("Notice");
						changeDueMessageBox.setMessage("Please enter a smaller amount\nYou can't pay off more than you owe...");
					} else if (tendered.compareTo(BigDecimal.valueOf(0)) <= 0) {
						MessageBox pleaseEnterAmountTenderedMessageBox = new MessageBox(shlAmountDue, SWT.ICON_INFORMATION);
						pleaseEnterAmountTenderedMessageBox.setText("Notice");
						pleaseEnterAmountTenderedMessageBox.setMessage("Please enter tendered amount...");
						pleaseEnterAmountTenderedMessageBox.open();
						return;
					} else {
						response.setAmountCharged(tendered.negate());
						changeDueMessageBox.setText("Thank you");
						changeDueMessageBox.setMessage("Thank you for the payment");
					}
				} else {
					// paying a normal invoice or project
					// calculate any change
					BigDecimal chageDue = total.subtract(tendered);

					if (btnChargeRemainderToAcct.getSelection()) {
						chageDue = chageDue.subtract(new BigDecimal(txtAmountCharged.getText().replaceAll("[^-.0-9]", "")));

					}

					// if not enough money tendered, complain
					if (chageDue.compareTo(BigDecimal.valueOf(0)) > 0) {
						MessageBox notEnoughTenderedMessageBox = new MessageBox(shlAmountDue, SWT.ICON_INFORMATION);
						notEnoughTenderedMessageBox.setText("Notice");
						notEnoughTenderedMessageBox.setMessage("Customer did not tender enough money...");
						notEnoughTenderedMessageBox.open();
						return;
					} else if (chageDue.compareTo(BigDecimal.valueOf(0)) == 0) {
						changeDueMessageBox.setText("Change Due");
						changeDueMessageBox.setMessage("No change due");
						if(btnChargeRemainderToAcct.getSelection()) {
							// NOTE this is an absolutely, beautifully terrible hack way to return the amount charged back to the Gui
							// now we can use it to set the AcountTransaction.ammount
							response.setAmountCharged(new BigDecimal(txtAmountCharged.getText().replaceAll("[^-.0-9]", "")));
							response.setResult(Result.CHARGED);
						} else {
							response.setResult(Result.NOCHANGE);
						}
					} else if (chageDue.compareTo(BigDecimal.valueOf(0)) < 0) {
						changeDueMessageBox.setText("Change Due");
						changeDueMessageBox.setMessage("Change due to Customer: $" + chageDue.toString().replace("-", ""));
						response.setResult(Result.WITHCHANGE);
					}
				}
				changeDueMessageBox.open();
				shlAmountDue.close();
			}
		});
		btnCashier.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnCashier.setText("Cashier");

		btnCancel = new Button(shlAmountDue, SWT.NONE);
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
				response.setResult(Result.CANCELED);
			}
		});
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnCancel.setText("Cancel");
	}
}
