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
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class NewProjectSiteDialog extends Dialog {

	protected transient Object result;
	protected Shell shlNewProjectSite;
	private MyText txtOwner;
	private MyText txtAddress;
	private MyText txtModelName;
	private MyText txtState;
//	private MyText txtNotes;
	private MyText txtCity;
	private MyText txtYear;
	private MyText txtZipCode;
	private MyText txtNotes;
	private Button btnSaveUnit;
	private Button btnCancel;
	// TODO i think this is a hack way to do this, projectSiteId is used in Mouse Down listener for Save button
		// to determine if we need to call addNew... or saveExisting....
	private long projectSiteId = -1;
	private long customerId = -1;
	private transient ProjectSite projectSite;

	public NewProjectSiteDialog(Shell parent, int style) {
		super(parent, style);
	}

	public Object open() {
		createContents();
		setupListeners();
		shlNewProjectSite.open();
		shlNewProjectSite.layout();
		Display display = getParent().getDisplay();
		while (!shlNewProjectSite.isDisposed()) {
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
		if (projectSite.getProjectSiteAddress() != null) {
			txtAddress.setText(projectSite.getProjectSiteAddress());
			txtAddress.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		}			
//		if (projectSite.getModelName() != null)
//			txtModelName.setText(projectSite.getModelName());
//		if (projectSite.getMileage() != 0)
//			txtState.setText(Integer.toString(projectSite.getMileage()));
		if (projectSite.getProjectSiteNotes() != null)
			txtNotes.setText(projectSite.getProjectSiteNotes());
		if (projectSite.getProjectSiteCity() != null)
			txtCity.setText(projectSite.getProjectSiteCity());
		if (projectSite.getProjectSiteState() != null)
			txtState.setText(projectSite.getProjectSiteState());
		if (projectSite.getProjectSiteZipCode() != null)
			txtZipCode.setText(projectSite.getProjectSiteZipCode());
//		if (projectSite.getNotes() != null)
//			txtNotes.setText(projectSite.getNotes());
		if (projectSite.getProjectSiteId() != 0)
			projectSiteId = projectSite.getProjectSiteId();
		if (projectSite.getCustomerId() != 0)
			customerId = projectSite.getCustomerId();
		this.projectSite = projectSite;
		
		txtOwner.setData(new Customer().searchByPk(customerId));
		
		setupListeners();
		// open Edit ProjectSite dialog, customized for editing a current ProjectSite
		shlNewProjectSite.setText("Edit Project Site Details");
		shlNewProjectSite.open();
		shlNewProjectSite.layout();
		Display display = getParent().getDisplay();
		while (!shlNewProjectSite.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlNewProjectSite = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlNewProjectSite.setText("Add Project Site");
		shlNewProjectSite.setSize(592, 399);
		
		Gui.setDialogAtCenter(shlNewProjectSite);
		shlNewProjectSite.setLayout(new FormLayout());
				
		// ProjectSite text boxes
		Label lblOwner = new Label(shlNewProjectSite, SWT.NONE);
		FormData fd_lblOwner = new FormData();
		fd_lblOwner.bottom = new FormAttachment(0, 30);
		fd_lblOwner.right = new FormAttachment(0, 80);
		fd_lblOwner.top = new FormAttachment(0, 10);
		fd_lblOwner.left = new FormAttachment(0, 10);
		lblOwner.setLayoutData(fd_lblOwner);
		lblOwner.setText("Owner:");
		txtOwner = new MyText(shlNewProjectSite, SWT.BORDER);
		FormData fd_txtOwner = new FormData();
		fd_txtOwner.bottom = new FormAttachment(0, 62);
		fd_txtOwner.right = new FormAttachment(0, 582);
		fd_txtOwner.top = new FormAttachment(0, 36);
		fd_txtOwner.left = new FormAttachment(0, 10);
		txtOwner.setLayoutData(fd_txtOwner);
		txtOwner.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtOwner.setText("");
		txtOwner.setEditable(false);
		
		Label lblAddress = new Label(shlNewProjectSite, SWT.NONE);
		FormData fd_lblAddress = new FormData();
		fd_lblAddress.bottom = new FormAttachment(0, 88);
		fd_lblAddress.right = new FormAttachment(0, 80);
		fd_lblAddress.top = new FormAttachment(0, 68);
		fd_lblAddress.left = new FormAttachment(0, 10);
		lblAddress.setLayoutData(fd_lblAddress);
		lblAddress.setText("Address:");
		txtAddress = new MyText(shlNewProjectSite, SWT.BORDER);
		FormData fd_txtAddress = new FormData();
		fd_txtAddress.bottom = new FormAttachment(0, 120);
		fd_txtAddress.right = new FormAttachment(100, -10);
		fd_txtAddress.top = new FormAttachment(0, 94);
		fd_txtAddress.left = new FormAttachment(0, 10);
		txtAddress.setLayoutData(fd_txtAddress);
		txtAddress.setBackground(SWTResourceManager.getColor(255, 102, 102));
		
		Label lblCity = new Label(shlNewProjectSite, SWT.NONE);
		FormData fd_lblCity = new FormData();
		fd_lblCity.bottom = new FormAttachment(txtAddress, 26, SWT.BOTTOM);
		fd_lblCity.top = new FormAttachment(txtAddress, 6);
		fd_lblCity.left = new FormAttachment(lblOwner, 0, SWT.LEFT);
		fd_lblCity.right = new FormAttachment(100, -512);
		lblCity.setLayoutData(fd_lblCity);
		lblCity.setText("City:");
		txtCity = new MyText(shlNewProjectSite, SWT.BORDER);
		FormData fd_txtCity = new FormData();
		fd_txtCity.top = new FormAttachment(lblCity);
		fd_txtCity.left = new FormAttachment(lblOwner, 0, SWT.LEFT);
		fd_txtCity.right = new FormAttachment(0, 288);
		txtCity.setLayoutData(fd_txtCity);
		
//		Label lblModelName = new Label(shlNewProjectSite, SWT.NONE);
//		FormData fd_lblModelName = new FormData();
//		fd_lblModelName.bottom = new FormAttachment(0, 146);
//		fd_lblModelName.right = new FormAttachment(0, 100);
//		fd_lblModelName.top = new FormAttachment(0, 126);
//		fd_lblModelName.left = new FormAttachment(0, 10);
//		lblModelName.setLayoutData(fd_lblModelName);
//		lblModelName.setText("Model Name:");
//		txtModelName = new MyText(shlNewProjectSite, SWT.BORDER);
//		FormData fd_txtModelName = new FormData();
//		fd_txtModelName.bottom = new FormAttachment(0, 178);
//		fd_txtModelName.right = new FormAttachment(0, 288);
//		fd_txtModelName.top = new FormAttachment(0, 152);
//		fd_txtModelName.left = new FormAttachment(0, 10);
//		txtModelName.setLayoutData(fd_txtModelName);
		
//		Label lblYear = new Label(shlNewProjectSite, SWT.NONE);
//		FormData fd_lblYear = new FormData();
//		fd_lblYear.top = new FormAttachment(txtCity, 6);
//		fd_lblYear.right = new FormAttachment(lblCity, 0, SWT.RIGHT);
//		fd_lblYear.bottom = new FormAttachment(0, 146);
//		fd_lblYear.left = new FormAttachment(0, 304);
//		lblYear.setLayoutData(fd_lblYear);
//		lblYear.setText("Year:");
//		txtYear = new MyText(shlNewProjectSite, SWT.BORDER);
//		FormData fd_txtYear = new FormData();
//		fd_txtYear.bottom = new FormAttachment(0, 178);
//		fd_txtYear.right = new FormAttachment(0, 582);
//		fd_txtYear.top = new FormAttachment(0, 152);
//		fd_txtYear.left = new FormAttachment(0, 304);
//		txtYear.setLayoutData(fd_txtYear);
		
		Label lblState = new Label(shlNewProjectSite, SWT.NONE);
		FormData fd_lblState = new FormData();
		fd_lblState.left = new FormAttachment(lblCity, 214);
		fd_lblState.top = new FormAttachment(txtAddress, 6);
		lblState.setLayoutData(fd_lblState);
		lblState.setText("State:");
		txtState = new MyText(shlNewProjectSite, SWT.BORDER);
		FormData fd_txtState = new FormData();
		fd_txtState.bottom = new FormAttachment(0, 172);
		fd_txtState.right = new FormAttachment(100, -234);
		fd_txtState.top = new FormAttachment(0, 146);
		fd_txtState.left = new FormAttachment(0, 294);
		txtState.setLayoutData(fd_txtState);
		
		Label lblZipCode = new Label(shlNewProjectSite, SWT.NONE);
		fd_lblState.right = new FormAttachment(100, -228);
		FormData fd_lblZipCode = new FormData();
		fd_lblZipCode.top = new FormAttachment(lblCity, 0, SWT.TOP);
		fd_lblZipCode.left = new FormAttachment(lblState, 6);
		fd_lblZipCode.right = new FormAttachment(100, -152);
		lblZipCode.setLayoutData(fd_lblZipCode);
		lblZipCode.setText("Zip Code:");
		txtZipCode = new MyText(shlNewProjectSite, SWT.BORDER);
		FormData fd_txZipCode = new FormData();
		fd_txZipCode.left = new FormAttachment(0, 368);
		fd_txZipCode.right = new FormAttachment(100, -10);
		fd_txZipCode.top = new FormAttachment(0, 146);
		fd_txZipCode.bottom = new FormAttachment(0, 172);
		txtZipCode.setLayoutData(fd_txZipCode);
		
//		Label lblVinNumber = new Label(shlNewProjectSite, SWT.NONE);
//		FormData fd_lblVinNumber = new FormData();
//		fd_lblVinNumber.bottom = new FormAttachment(0, 262);
//		fd_lblVinNumber.right = new FormAttachment(0, 100);
//		fd_lblVinNumber.top = new FormAttachment(0, 242);
//		fd_lblVinNumber.left = new FormAttachment(0, 10);
//		lblVinNumber.setLayoutData(fd_lblVinNumber);
//		lblVinNumber.setText("Vin Number:");
//		txtNotes = new MyText(shlNewProjectSite, SWT.BORDER);
//		FormData fd_txtVinNumber = new FormData();
//		fd_txtVinNumber.bottom = new FormAttachment(0, 294);
//		fd_txtVinNumber.right = new FormAttachment(0, 582);
//		fd_txtVinNumber.top = new FormAttachment(0, 268);
//		fd_txtVinNumber.left = new FormAttachment(0, 10);
//		txtNotes.setLayoutData(fd_txtVinNumber);
				
		Label lblNotes = new Label(shlNewProjectSite, SWT.NONE);
		fd_txtCity.bottom = new FormAttachment(lblNotes, -6);
		FormData fd_lblNotes = new FormData();
		fd_lblNotes.top = new FormAttachment(0, 178);
		fd_lblNotes.left = new FormAttachment(lblOwner, 0, SWT.LEFT);
		fd_lblNotes.bottom = new FormAttachment(0, 198);
		fd_lblNotes.right = new FormAttachment(0, 80);
		lblNotes.setLayoutData(fd_lblNotes);
		lblNotes.setText("Notes:");
		txtNotes = new MyText(shlNewProjectSite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtNotes = new FormData();
		fd_txtNotes.top = new FormAttachment(lblNotes, 6);
		fd_txtNotes.left = new FormAttachment(lblOwner, 0, SWT.LEFT);
		fd_txtNotes.bottom = new FormAttachment(0, 296);
		fd_txtNotes.right = new FormAttachment(0, 582);
		txtNotes.setLayoutData(fd_txtNotes);
		
		// ProjectSite dialog controls
		Label label = new Label(shlNewProjectSite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 302);
		fd_label.right = new FormAttachment(100, -10);
		fd_label.left = new FormAttachment(0, 10);
		label.setLayoutData(fd_label);
		
		btnSaveUnit = new Button(shlNewProjectSite, SWT.NONE);
		FormData fd_btnSaveUnit = new FormData();
		fd_btnSaveUnit.top = new FormAttachment(label, 10);
		fd_btnSaveUnit.bottom = new FormAttachment(100, -10);
		fd_btnSaveUnit.left = new FormAttachment(lblOwner, 0, SWT.LEFT);
		fd_btnSaveUnit.right = new FormAttachment(0, 423);
		btnSaveUnit.setLayoutData(fd_btnSaveUnit);
		btnSaveUnit.setText("Save Project Site");
				
		btnCancel = new Button(shlNewProjectSite, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(label, 10);
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.right = new FormAttachment(txtOwner, 0, SWT.RIGHT);
		fd_btnCancel.left = new FormAttachment(0, 429);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
				
		shlNewProjectSite.setTabList(new Control[]{txtOwner, txtAddress, txtCity,
								txtState, txtZipCode, txtNotes, btnSaveUnit, btnCancel});
	}
	
	private void setupListeners() {
		txtOwner.addModifyListener(new RequiredTextBoxModifyListener(txtOwner));
		txtOwner.addMouseListener(new CustomerSearchListener(txtOwner));
		txtAddress.addModifyListener(new RequiredTextBoxModifyListener(txtAddress));
		txtCity.addModifyListener(new InfoTextBoxModifyListener(txtCity));
//		txtModelName.addModifyListener(new InfoTextBoxModifyListener(txtModelName));
//		txtYear.addModifyListener(new InfoTextBoxModifyListener(txtYear));
		txtState.addModifyListener(new InfoTextBoxModifyListener(txtState));
		txtZipCode.addModifyListener(new InfoTextBoxModifyListener(txtZipCode));
//		txtNotes.addModifyListener(new VinModifyListener(txtNotes));		// note needed for general notes
		txtNotes.addModifyListener(new InfoTextBoxModifyListener(txtNotes));
		
		btnSaveUnit.addMouseListener(new MouseAdapter() {		// in-line listener
			@Override
			public void mouseDown(MouseEvent e) {
				if (projectSiteId == -1) {
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
				shlNewProjectSite.close();
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
			MessageBox ownerRequirementBox = new MessageBox(shlNewProjectSite, SWT.ICON_INFORMATION);
			ownerRequirementBox.setText("Notice");
			ownerRequirementBox.setMessage("Please select an Owner");
			ownerRequirementBox.open();
			return;
		}
		
		// TODO we could add an "&& !txtAddress.getText().equals(txtAddress.startingText)" so that we wouldn't unnecessarily save this
			// if it hasn't changed, but that would be a lot of extra code for no real benefit
		if (txtAddress.getText().length() > 0) {
			projectSite.setProjectSiteAddress(txtAddress.getText());
		} else {
			// dialog box stating make is required
			MessageBox makeRequirementBox = new MessageBox(shlNewProjectSite, SWT.ICON_INFORMATION);
			makeRequirementBox.setText("Notice");
			makeRequirementBox.setMessage("Please enter a Make/Manufacturer");
			makeRequirementBox.open();
			return;
		}
		
//		projectSite.setModelName(txtModelName.getText());
		
		if (txtState.isModified()) {
			try {
				projectSite.setProjectSiteState(txtState.getText());
			} catch (Exception e) {
				Application.getLogger().log(Level.FINER, e.getMessage(), e);
			}
		}

		projectSite.setProjectSiteNotes(txtNotes.getText());

		projectSite.setProjectSiteCity(txtCity.getText());

//		if (txtYear.isModified()) {
//			try {
//				projectSite.setStateProjectSite(txtYear.getText());
//			} catch (Exception e) {
//				Application.getLogger().log(Level.FINER, e.getMessage(), e);
//			}
//		}

		projectSite.setProjectSiteZipCode(txtZipCode.getText());

//		if (txtNotes.isModified()) {
//			projectSite.setNotes(txtNotes.getText());
//		}
		
		DbServices.saveObject(projectSite);
		result = projectSite;
		shlNewProjectSite.close();
	}
}