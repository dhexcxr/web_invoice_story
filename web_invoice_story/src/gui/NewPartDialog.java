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

public class NewPartDialog extends Dialog {

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
		shlAddPart = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlAddPart.setSize(592, 408);
		shlAddPart.setText("Add Part");
		
		Gui.setDialogAtCenter(shlAddPart);

		Label lblPartNumber = new Label(shlAddPart, SWT.NONE);
		lblPartNumber.setBounds(10, 10, 86, 20);
		lblPartNumber.setText("Part Number:");
		txtPartNumber = new MyText(shlAddPart, SWT.BORDER);
		txtPartNumber.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtPartNumber.setBounds(10, 36, 270, 26);
		
		Label lblSupplier = new Label(shlAddPart, SWT.NONE);
		lblSupplier.setBounds(286, 10, 70, 20);
		lblSupplier.setText("Supplier:");
		txtSupplier = new MyText(shlAddPart, SWT.BORDER);
		txtSupplier.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtSupplier.setBounds(286, 36, 142, 26);
		
		Label lblCategory = new Label(shlAddPart, SWT.NONE);
		lblCategory.setBounds(434, 10, 70, 20);
		lblCategory.setText("Category:");
		txtCategory = new MyText(shlAddPart, SWT.BORDER);
		txtCategory.setBounds(434, 36, 130, 26);
		
		Label lblPartDescription = new Label(shlAddPart, SWT.NONE);
		lblPartDescription.setBounds(10, 68, 108, 20);
		lblPartDescription.setText("Part Description:");
		txtDescription = new MyText(shlAddPart, SWT.BORDER);
		txtDescription.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtDescription.setBounds(10, 94, 554, 26);
		
		Label lblQuantityOnHand = new Label(shlAddPart, SWT.NONE);
		lblQuantityOnHand.setBounds(10, 126, 122, 20);
		lblQuantityOnHand.setText("Quantity On Hand:");
		txtOnHand = new MyText(shlAddPart, SWT.BORDER);
		txtOnHand.setBounds(10, 152, 170, 26);
		
		Label lblCost = new Label(shlAddPart, SWT.NONE);
		lblCost.setBounds(202, 126, 70, 20);
		lblCost.setText("Cost:");	
		txtCost = new MyText(shlAddPart, SWT.BORDER);
		txtCost.setBounds(202, 152, 170, 26);
		
		Label lblRetailPrice = new Label(shlAddPart, SWT.NONE);
		lblRetailPrice.setBounds(394, 126, 77, 20);
		lblRetailPrice.setText("Retail Price:");
		txtRetail = new MyText(shlAddPart, SWT.BORDER);
		txtRetail.setBounds(394, 152, 170, 26);
		
		Label lblNotes = new Label(shlAddPart, SWT.NONE);
		lblNotes.setBounds(10, 184, 70, 20);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlAddPart, SWT.BORDER | SWT.WRAP);
		txtNotes.setBounds(10, 210, 554, 104);
				
		// New Part dialog controls
		btnSavePart = new Button(shlAddPart, SWT.NONE);
		btnSavePart.setText("Save Part");
		btnSavePart.setBounds(10, 328, 413, 26);
				
		btnCancel = new Button(shlAddPart, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 328, 135, 26);
		
		Label label = new Label(shlAddPart, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 320, 554, 2);
		
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
