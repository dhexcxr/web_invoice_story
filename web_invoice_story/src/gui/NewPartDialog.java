package gui;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import listeners.InfoTextBoxModifyListener;
import listeners.RequiredTextBoxModifyListener;
import guiParts.MyText;
import objects.Part;
import web_invoice_story.Application;
import web_invoice_story.DbServices;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class NewPartDialog extends Dialog {		// NOTE does not have correcet spacing 

	protected Object result;
	protected Shell shlAddPart;
	private MyText txtPartNumber;
	private MyText txtSupplier;
	private MyText txtDescription;
	private MyText txtOnHand;
	private MyText txtNotes;
	private MyText txtCategory;
	private MyText txtCost;
	private MyText txtRetail;
	private Button btnSavePart;
	private Button btnCancel;
	
	private long partId = -1;
	private Part part;

	public NewPartDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	public Object open() {
		createContents();
		setupListeners();
		shlAddPart.open();
		shlAddPart.layout();
		Display display = getParent().getDisplay();
		while (!shlAddPart.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(Part part) {
		createContents();

		// TODO decide if I want to include checks for the presence of part number, supplier, and description
			// technically, there should never be a part in existance that does not have those attributes
		this.partId = part.getPartId();
		this.txtPartNumber.setText(part.getPartNumber());
		txtPartNumber.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		this.txtSupplier.setText(part.getSupplier());
		txtSupplier.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		this.txtDescription.setText(part.getDescription());
		txtDescription.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		if (part.getOnHand() != -1) {
			this.txtOnHand.setText(Integer.toString(part.getOnHand()));
		}
		this.txtNotes.setText(part.getNotes());
		this.txtCategory.setText(part.getCategory());
		this.txtCost.setText(part.getCost().toString());
		this.txtRetail.setText(part.getRetail().toString());

		this.part = part;
		
		setupListeners();
		
		// open new New Part dialog, customize for editing current customer
		shlAddPart.setText("Edit Part Details");
		shlAddPart.open();
		shlAddPart.layout();
		Display display = getParent().getDisplay();
		while (!shlAddPart.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlAddPart = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlAddPart.setSize(592, 408);
		shlAddPart.setText("Add Part");
		
		Gui.setDialogAtCenter(shlAddPart);
		shlAddPart.setLayout(new FormLayout());

		Label lblPartNumber = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblPartNumber = new FormData();
		fd_lblPartNumber.left = new FormAttachment(0, 10);
		fd_lblPartNumber.bottom = new FormAttachment(0, 30);
		fd_lblPartNumber.top = new FormAttachment(0, 10);
		lblPartNumber.setLayoutData(fd_lblPartNumber);
		lblPartNumber.setText("Part Number:");
		txtPartNumber = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtPartNumber = new FormData();
		fd_txtPartNumber.bottom = new FormAttachment(0, 62);
		fd_txtPartNumber.right = new FormAttachment(0, 280);
		fd_txtPartNumber.top = new FormAttachment(0, 36);
		fd_txtPartNumber.left = new FormAttachment(0, 10);
		txtPartNumber.setLayoutData(fd_txtPartNumber);
		txtPartNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));
		
		Label lblSupplier = new Label(shlAddPart, SWT.NONE);
		fd_lblPartNumber.right = new FormAttachment(lblSupplier, -154);
		FormData fd_lblSupplier = new FormData();
		fd_lblSupplier.bottom = new FormAttachment(0, 30);
		fd_lblSupplier.right = new FormAttachment(0, 356);
		fd_lblSupplier.top = new FormAttachment(0, 10);
		fd_lblSupplier.left = new FormAttachment(0, 286);
		lblSupplier.setLayoutData(fd_lblSupplier);
		lblSupplier.setText("Supplier:");
		txtSupplier = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtSupplier = new FormData();
		fd_txtSupplier.bottom = new FormAttachment(0, 62);
		fd_txtSupplier.right = new FormAttachment(0, 428);
		fd_txtSupplier.top = new FormAttachment(0, 36);
		fd_txtSupplier.left = new FormAttachment(0, 286);
		txtSupplier.setLayoutData(fd_txtSupplier);
		txtSupplier.setBackground(SWTResourceManager.getColor(255, 102, 102));
		
		Label lblCategory = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblCategory = new FormData();
		fd_lblCategory.bottom = new FormAttachment(0, 30);
		fd_lblCategory.right = new FormAttachment(0, 504);
		fd_lblCategory.top = new FormAttachment(0, 10);
		fd_lblCategory.left = new FormAttachment(0, 434);
		lblCategory.setLayoutData(fd_lblCategory);
		lblCategory.setText("Category:");
		txtCategory = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtCategory = new FormData();
		fd_txtCategory.bottom = new FormAttachment(0, 62);
		fd_txtCategory.right = new FormAttachment(0, 582);
		fd_txtCategory.top = new FormAttachment(0, 36);
		fd_txtCategory.left = new FormAttachment(0, 434);
		txtCategory.setLayoutData(fd_txtCategory);
		
		Label lblPartDescription = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblPartDescription = new FormData();
		fd_lblPartDescription.right = new FormAttachment(lblPartNumber, 0, SWT.RIGHT);
		fd_lblPartDescription.bottom = new FormAttachment(0, 88);
		fd_lblPartDescription.top = new FormAttachment(0, 68);
		fd_lblPartDescription.left = new FormAttachment(0, 10);
		lblPartDescription.setLayoutData(fd_lblPartDescription);
		lblPartDescription.setText("Part Description:");
		txtDescription = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtDescription = new FormData();
		fd_txtDescription.right = new FormAttachment(txtCategory, 0, SWT.RIGHT);
		fd_txtDescription.bottom = new FormAttachment(0, 120);
		fd_txtDescription.top = new FormAttachment(0, 94);
		fd_txtDescription.left = new FormAttachment(0, 10);
		txtDescription.setLayoutData(fd_txtDescription);
		txtDescription.setBackground(SWTResourceManager.getColor(255, 102, 102));
		
		Label lblQuantityOnHand = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblQuantityOnHand = new FormData();
		fd_lblQuantityOnHand.right = new FormAttachment(lblPartNumber, 31, SWT.RIGHT);
		fd_lblQuantityOnHand.bottom = new FormAttachment(0, 146);
		fd_lblQuantityOnHand.top = new FormAttachment(0, 126);
		fd_lblQuantityOnHand.left = new FormAttachment(0, 10);
		lblQuantityOnHand.setLayoutData(fd_lblQuantityOnHand);
		lblQuantityOnHand.setText("Quantity On Hand:");
		txtOnHand = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtOnHand = new FormData();
		fd_txtOnHand.bottom = new FormAttachment(0, 178);
		fd_txtOnHand.right = new FormAttachment(0, 180);
		fd_txtOnHand.top = new FormAttachment(0, 152);
		fd_txtOnHand.left = new FormAttachment(0, 10);
		txtOnHand.setLayoutData(fd_txtOnHand);
		
		Label lblCost = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblCost = new FormData();
		fd_lblCost.bottom = new FormAttachment(0, 146);
		fd_lblCost.right = new FormAttachment(0, 272);
		fd_lblCost.top = new FormAttachment(0, 126);
		fd_lblCost.left = new FormAttachment(0, 202);
		lblCost.setLayoutData(fd_lblCost);
		lblCost.setText("Cost:");	
		txtCost = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtCost = new FormData();
		fd_txtCost.bottom = new FormAttachment(0, 178);
		fd_txtCost.right = new FormAttachment(0, 372);
		fd_txtCost.top = new FormAttachment(0, 152);
		fd_txtCost.left = new FormAttachment(0, 202);
		txtCost.setLayoutData(fd_txtCost);
		
		Label lblRetailPrice = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblRetailPrice = new FormData();
		fd_lblRetailPrice.right = new FormAttachment(lblCategory, 0, SWT.RIGHT);
		fd_lblRetailPrice.bottom = new FormAttachment(0, 146);
		fd_lblRetailPrice.top = new FormAttachment(0, 126);
		fd_lblRetailPrice.left = new FormAttachment(0, 394);
		lblRetailPrice.setLayoutData(fd_lblRetailPrice);
		lblRetailPrice.setText("Retail Price:");
		txtRetail = new MyText(shlAddPart, SWT.BORDER);
		FormData fd_txtRetail = new FormData();
		fd_txtRetail.right = new FormAttachment(txtCategory, 0, SWT.RIGHT);
		fd_txtRetail.bottom = new FormAttachment(0, 178);
		fd_txtRetail.top = new FormAttachment(0, 152);
		fd_txtRetail.left = new FormAttachment(0, 394);
		txtRetail.setLayoutData(fd_txtRetail);
		
		Label lblNotes = new Label(shlAddPart, SWT.NONE);
		FormData fd_lblNotes = new FormData();
		fd_lblNotes.bottom = new FormAttachment(0, 204);
		fd_lblNotes.right = new FormAttachment(0, 80);
		fd_lblNotes.top = new FormAttachment(0, 184);
		fd_lblNotes.left = new FormAttachment(0, 10);
		lblNotes.setLayoutData(fd_lblNotes);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlAddPart, SWT.BORDER | SWT.WRAP);
		FormData fd_txtNotes = new FormData();
		fd_txtNotes.right = new FormAttachment(txtCategory, 0, SWT.RIGHT);
		fd_txtNotes.bottom = new FormAttachment(0, 314);
		fd_txtNotes.top = new FormAttachment(0, 210);
		fd_txtNotes.left = new FormAttachment(0, 10);
		txtNotes.setLayoutData(fd_txtNotes);
				
		// New Part dialog controls
		btnSavePart = new Button(shlAddPart, SWT.NONE);
		FormData fd_btnSavePart = new FormData();
		fd_btnSavePart.right = new FormAttachment(0, 423);
		fd_btnSavePart.top = new FormAttachment(0, 328);
		fd_btnSavePart.left = new FormAttachment(0, 10);
		btnSavePart.setLayoutData(fd_btnSavePart);
		btnSavePart.setText("Save Part");
				
		btnCancel = new Button(shlAddPart, SWT.NONE);
		fd_btnSavePart.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(txtCategory, 0, SWT.RIGHT);
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.top = new FormAttachment(0, 328);
		fd_btnCancel.left = new FormAttachment(0, 429);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		Label label = new Label(shlAddPart, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(txtNotes, 8, SWT.BOTTOM);
		fd_label.top = new FormAttachment(txtNotes, 6);
		fd_label.right = new FormAttachment(txtCategory, 0, SWT.RIGHT);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		
		shlAddPart.setTabList(new Control[]{txtPartNumber, txtSupplier, txtCategory, txtDescription,
										txtOnHand, txtCost, txtRetail, txtNotes, btnSavePart, btnCancel});
	}
	
	private void setupListeners() {
		txtPartNumber.addModifyListener(new RequiredTextBoxModifyListener(txtPartNumber));
		txtSupplier.addModifyListener(new RequiredTextBoxModifyListener(txtSupplier));
		txtCategory.addModifyListener(new InfoTextBoxModifyListener(txtCategory));
		txtDescription.addModifyListener(new RequiredTextBoxModifyListener(txtDescription));
		txtOnHand.addModifyListener(new InfoTextBoxModifyListener(txtOnHand));
		txtCost.addModifyListener(new InfoTextBoxModifyListener(txtCost));
		txtRetail.addModifyListener(new InfoTextBoxModifyListener(txtRetail));
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));

		btnSavePart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (partId == -1) {
					addNewPart();
				} else {
					savePart(part);
				}
			}
		});
		
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlAddPart.close();
			}
		});
	}

	protected void addNewPart() {
		savePart(new Part());
	}

	private void savePart(Part part) {
		if (txtPartNumber.getText().length() > 0) {
			part.setPartNumber(txtPartNumber.getText());
		} else {
			// dialog box stating part number is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Part Number");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtSupplier.getText().length() > 0) {
			part.setSupplier(txtSupplier.getText());
		} else {
			// dialog box stating supplier is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Supplier");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtCategory.isModified()) {
			part.setCategory(txtCategory.getText());
		}
		
		if (txtDescription.getText().length() > 0) {
			part.setDescription(txtDescription.getText());
		} else {
			// dialog box stating description is required
			MessageBox lastNameRequirementBox = new MessageBox(shlAddPart, SWT.ICON_INFORMATION);
			lastNameRequirementBox.setText("Notice");
			lastNameRequirementBox.setMessage("Please enter a Part Description");
			lastNameRequirementBox.open();
			return;
		}
		
		if (txtNotes.isModified()) {
			part.setNotes(txtNotes.getText());
		}
		
		if (txtRetail.isModified()) {
			try {
				part.setRetail(new BigDecimal(txtRetail.getText()));
			} catch (NumberFormatException e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		if (txtCost.isModified()) {
			try {
				part.setCost(new BigDecimal(txtCost.getText()));
			} catch (NumberFormatException e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		if (txtOnHand.isModified()) {
			try {
				part.setOnHand(Integer.parseInt(txtOnHand.getText()));
			} catch (NumberFormatException e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}
		
		DbServices.saveObject(part);
		result = part;
		shlAddPart.close();
	}
}
