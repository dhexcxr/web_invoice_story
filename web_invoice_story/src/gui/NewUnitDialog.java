package gui;

import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.MyText;
import listeners.CustomerSearchListener;
import listeners.InfoTextBoxModifyListener;
import listeners.RequiredTextBoxModifyListener;
import listeners.VinModifyListener;
import objects.Customer;
import objects.ProjectSite;
import web_invoice_story.Application;
import web_invoice_story.DbServices;

public class NewUnitDialog extends Dialog {

	protected transient Object result;
	protected Shell shlNewUnit;
	private MyText txtOwner;
	private MyText txtMake;
	private MyText txtModelName;
	private MyText txtMileage;
	private MyText txtVinNumber;
	private MyText txtModel;
	private MyText txtYear;
	private MyText txtColor;
	private MyText txtNotes;
	private Button btnSaveUnit;
	private Button btnCancel;
	// TODO i think this is a hack way to do this, unitId is used in Mouse Down listener for Save button
		// to determine if we need to call addNew... or saveExisting....
	private long unitId = -1;
	private long customerId = -1;
	private transient ProjectSite projectSite;

	public NewUnitDialog(Shell parent, int style) {
		super(parent, style);
	}

	public Object open() {
		createContents();
		setupListeners();
		shlNewUnit.open();
		shlNewUnit.layout();
		Display display = getParent().getDisplay();
		while (!shlNewUnit.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(ProjectSite projectSite) {
		createContents();

		// set txtBoxes when opening a current ProjectSite for editing
		// TODO remove all these null checks
			// and make sure projectSite checks for a null in getters with ternaries, i.e. make == null ? "" : make 
		if (projectSite.getOwner() != null) {
			txtOwner.setText(projectSite.getOwner());
			txtOwner.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		}
		if (projectSite.getAddressProjectSite() != null) {
			txtMake.setText(projectSite.getAddressProjectSite());
			txtMake.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		}			
//		if (projectSite.getModelName() != null)
//			txtModelName.setText(projectSite.getModelName());
//		if (projectSite.getMileage() != 0)
//			txtMileage.setText(Integer.toString(projectSite.getMileage()));
		if (projectSite.getNotesProjectSite() != null)
			txtVinNumber.setText(projectSite.getNotesProjectSite());
		if (projectSite.getCityProjectSiteString() != null)
			txtModel.setText(projectSite.getCityProjectSiteString());
//		if (projectSite.getStateProjectSite() != 0)
//			txtYear.setText(Integer.toString(projectSite.getStateProjectSite()));
		if (projectSite.getZipCodeProjectSiteString() != null)
			txtColor.setText(projectSite.getZipCodeProjectSiteString());
//		if (projectSite.getNotes() != null)
//			txtNotes.setText(projectSite.getNotes());
		if (projectSite.getProjectSiteId() != 0)
			unitId = projectSite.getProjectSiteId();
		if (projectSite.getCustomerId() != 0)
			customerId = projectSite.getCustomerId();
		this.projectSite = projectSite;
		
		txtOwner.setData(new Customer().searchByPk(customerId));
		
		setupListeners();
		// open Edit ProjectSite dialog, customized for editing a current ProjectSite
		shlNewUnit.setText("Edit ProjectSite Details");
		shlNewUnit.open();
		shlNewUnit.layout();
		Display display = getParent().getDisplay();
		while (!shlNewUnit.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlNewUnit = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlNewUnit.setText("Add ProjectSite");
		shlNewUnit.setSize(592, 516);
		
		Gui.setDialogAtCenter(shlNewUnit);
				
		// ProjectSite text boxes
		Label lblOwner = new Label(shlNewUnit, SWT.NONE);
		lblOwner.setBounds(10, 10, 70, 20);
		lblOwner.setText("Owner:");
		txtOwner = new MyText(shlNewUnit, SWT.BORDER);
		txtOwner.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtOwner.setText("");
		txtOwner.setBounds(10, 36, 554, 26);
		txtOwner.setEditable(false);
		
		Label lblMake = new Label(shlNewUnit, SWT.NONE);
		lblMake.setBounds(10, 68, 70, 20);
		lblMake.setText("Make:");
		txtMake = new MyText(shlNewUnit, SWT.BORDER);
		txtMake.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtMake.setBounds(10, 94, 270, 26);
		
		Label lblModel = new Label(shlNewUnit, SWT.NONE);
		lblModel.setBounds(294, 68, 70, 20);
		lblModel.setText("Model:");
		txtModel = new MyText(shlNewUnit, SWT.BORDER);
		txtModel.setBounds(286, 94, 270, 26);
		
		Label lblModelName = new Label(shlNewUnit, SWT.NONE);
		lblModelName.setBounds(10, 126, 90, 20);
		lblModelName.setText("Model Name:");
		txtModelName = new MyText(shlNewUnit, SWT.BORDER);
		txtModelName.setBounds(10, 152, 270, 26);
		
		Label lblYear = new Label(shlNewUnit, SWT.NONE);
		lblYear.setBounds(286, 126, 70, 20);
		lblYear.setText("Year:");
		txtYear = new MyText(shlNewUnit, SWT.BORDER);
		txtYear.setBounds(286, 152, 270, 26);
		
		Label lblMileage = new Label(shlNewUnit, SWT.NONE);
		lblMileage.setBounds(10, 184, 70, 20);
		lblMileage.setText("Mileage:");
		txtMileage = new MyText(shlNewUnit, SWT.BORDER);
		txtMileage.setBounds(10, 210, 270, 26);
		
		Label lblColor = new Label(shlNewUnit, SWT.NONE);
		lblColor.setBounds(286, 184, 70, 20);
		lblColor.setText("Color:");
		txtColor = new MyText(shlNewUnit, SWT.BORDER);
		txtColor.setBounds(286, 210, 270, 26);
		
		Label lblVinNumber = new Label(shlNewUnit, SWT.NONE);
		lblVinNumber.setBounds(10, 242, 90, 20);
		lblVinNumber.setText("Vin Number:");
		txtVinNumber = new MyText(shlNewUnit, SWT.BORDER);
		txtVinNumber.setBounds(10, 268, 554, 26);
				
		Label lblNotes = new Label(shlNewUnit, SWT.NONE);
		lblNotes.setBounds(10, 300, 70, 20);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlNewUnit, SWT.BORDER | SWT.WRAP);
		txtNotes.setBounds(10, 326, 554, 92);
		
		// ProjectSite dialog controls
		Label label = new Label(shlNewUnit, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 424, 546, 5);
		
		btnSaveUnit = new Button(shlNewUnit, SWT.NONE);
		btnSaveUnit.setText("Save ProjectSite");
		btnSaveUnit.setBounds(10, 435, 413, 26);
				
		btnCancel = new Button(shlNewUnit, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(429, 435, 135, 26);
				
		shlNewUnit.setTabList(new Control[]{txtOwner, txtMake, txtModel, txtModelName, txtYear,
								txtMileage, txtColor, txtVinNumber, txtNotes, btnSaveUnit, btnCancel});
	}
	
	private void setupListeners() {
		txtOwner.addModifyListener(new RequiredTextBoxModifyListener(txtOwner));
		txtOwner.addMouseListener(new CustomerSearchListener(txtOwner));
		txtMake.addModifyListener(new RequiredTextBoxModifyListener(txtMake));
		txtModel.addModifyListener(new InfoTextBoxModifyListener(txtModel));
		txtModelName.addModifyListener(new InfoTextBoxModifyListener(txtModelName));
		txtYear.addModifyListener(new InfoTextBoxModifyListener(txtYear));
		txtMileage.addModifyListener(new InfoTextBoxModifyListener(txtMileage));
		txtColor.addModifyListener(new InfoTextBoxModifyListener(txtColor));
		txtVinNumber.addModifyListener(new VinModifyListener(txtVinNumber));
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));
		
		btnSaveUnit.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (unitId == -1) {
					// save new ProjectSite with current text boxes
					addNewUnit();
				} else {
					Application.getLogger().log(Level.INFO, "Save existing projectSite");
					// save modifications to existing customer
					saveUnit(projectSite);
				}
			}
		});
		
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlNewUnit.close();
			}
		});
	}

	protected void addNewUnit() {
		saveUnit(new ProjectSite());	
	}
	
	protected void saveUnit(ProjectSite projectSite) {
		if (txtOwner.getData() != null && ((Customer) txtOwner.getData()).getCustomerId() != -1) {
			projectSite.setCustomerId(((Customer) txtOwner.getData()).getCustomerId());
			projectSite.setOwner(txtOwner.getText());
		} else {
			// if we haven't selected an Owner, complain - a ProjectSite Owner is required
			MessageBox ownerRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			ownerRequirementBox.setText("Notice");
			ownerRequirementBox.setMessage("Please select an Owner");
			ownerRequirementBox.open();
			return;
		}
		
		// TODO we could add an "&& !txtMake.getText().equals(txtMake.startingText)" so that we wouldn't unnecessarily save this
			// if it hasn't changed, but that would be a lot of extra code for no real benefit
		if (txtMake.getText().length() > 0) {
			projectSite.setAddressProjectSite(txtMake.getText());
		} else {
			// dialog box stating make is required
			MessageBox makeRequirementBox = new MessageBox(shlNewUnit, SWT.ICON_INFORMATION);
			makeRequirementBox.setText("Notice");
			makeRequirementBox.setMessage("Please enter a Make/Manufacturer");
			makeRequirementBox.open();
			return;
		}
		
//		projectSite.setModelName(txtModelName.getText());
		
		if (txtMileage.isModified()) {
			try {
				projectSite.setStateProjectSite(txtMileage.getText());
			} catch (Exception e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}

		projectSite.setNotesProjectSite(txtVinNumber.getText());

		projectSite.setCityProjectSite(txtModel.getText());

		if (txtYear.isModified()) {
			try {
				projectSite.setStateProjectSite(txtYear.getText());
			} catch (Exception e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}

		projectSite.setZipCodeProjectSite(txtColor.getText());

//		if (txtNotes.isModified()) {
//			projectSite.setNotes(txtNotes.getText());
//		}
		
		DbServices.saveObject(projectSite);
		result = projectSite;
		shlNewUnit.close();
	}
}