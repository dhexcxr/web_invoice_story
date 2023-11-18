package gui;

import java.awt.Point;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.InvoicePartTable;
import guiParts.JobLaborTable;
import guiParts.MyText;
import guiParts.ProjectJobTable;
import guiParts.ProjectJobTableItem;
import listeners.CustomerSearchListener;
import listeners.InvoicePartTableListener;
import listeners.JobDetailsModifiedListener;
import listeners.JobNameModifiedListener;
import listeners.ProjectCancelListener;
import listeners.ProjectCloseListener;
import listeners.ProjectJobAddListener;
import listeners.ProjectJobDeleteListener;
import listeners.ProjectJobSelectedListener;
import listeners.ProjectLaborAddListener;
import listeners.ProjectLaborDeleteListener;
import listeners.ProjectLaborTableListener;
import listeners.ProjectPartDeleteLineItemListener;
import listeners.RequiredTextBoxModifyListener;
import listeners.TextBoxFocusListener;
import listeners.UnitSearchListener;
import objects.Account;
import objects.AmountDueResponse;
import objects.AmountDueResponse.Result;
import objects.Customer;
import objects.Job;
import objects.JobLabor;
import objects.JobPart;
import objects.Project;
import objects.ProjectSite;
import web_invoice_story.Application;
import web_invoice_story.DbServices;

public class ProjectDialog extends Dialog {

	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name
	protected transient Object result;
	
	protected Shell shlProject;
	private Group grpTotals;
	private Group grpProjectNum;
	private Group grpCreatedDate;
	
	private Group grpCashieredDate;
	
	private Composite roButtonsComposite;
	private ProjectJobTable tableJobsProject;
	private InvoicePartTable jobPartsTable;
	
	private JobLaborTable jobLaborTable;
	private Button btnSaveRo;
	private Button btnCancel;
	private Button btnClose;
	private Button btnAddJob;
	private Button btnDeleteJob;
	private Button btnCashierRo; 
	private Button btnDeleteLineItem;
	private Button btnAddLaborLine;
	private Button btnDeleteLaborLine;

	private TabFolder tabFolderJobsProject;
	private MyText textRoNum;
	private MyText textCreatedDate;
	private MyText textCashieredDate;
	private MyText txtCustomerProject;
	private MyText txtProjectSiteProject;
	private MyText txtJobName;
	private MyText txtComplaints;
	private MyText txtResolution;
	private MyText txtReccomendations;
	private MyText txtSubTotalProject;
	private MyText textTaxProject;
	
	private MyText textFinalTotalProject;
	private CustomerSearchListener customerSearchListener;
	private UnitSearchListener unitSearchListener;
	private JobNameModifiedListener jobNameModifiedListener;
	private InvoicePartTableListener projectPartTableListener;
	private ProjectLaborTableListener projectLaborTableListener;
	private JobDetailsModifiedListener jobDetailsModifiedListener;
	private TextBoxFocusListener jobNameFocusListener;
	private TextBoxFocusListener complaintsFocusListener;
	private TextBoxFocusListener resolutionFocusListener;
	
	private TextBoxFocusListener reccomendationsFocusListener;
	private Label lblSubTotal;
	private Label lblTax;
	
	private Label lblFinalTotal;
	private FormData fd_tableJobsProject;
	private FormData fd_btnClose;
	
	private FormData fd_btnCashierRo;
	private long projectId = -1;
	private Map<String, List<Long>> detailsToDelete = new HashMap<>();
	private transient Project currentProject;

	private boolean cashiered = false;
	
	
	// TODO create isModified variable in this ProjectDialog object to track if ANYTHING in the underlying project has been modified
		// so we can ask the user if they want to save before closing ProjectDialog
	
	public ProjectDialog(Shell parent, int style) {
		super(parent, style);		// TODO set customer name or RO number in setText
		setText("Project Details");
	}
	
	public void addDetailsToDelete(String table, Long primaryKey) {
		detailsToDelete.computeIfAbsent(table, t -> new ArrayList<>()).add(primaryKey);
	}
	
	public void calcRoTotal() {
		BigDecimal total = BigDecimal.valueOf(0);
		for (TableItem currentItem: tableJobsProject.getItems()) {
			String totalString = currentItem.getText(ProjectJobTable.JOB_TOTAL_COLUMN).replaceAll(ONLY_DECIMALS, "");
			total = total.add(new BigDecimal(totalString));
		}
		txtSubTotalProject.setText("$" + total.setScale(2, RoundingMode.CEILING).toString());
		BigDecimal tax = total.multiply(BigDecimal.valueOf(0.065));
		textTaxProject.setText("$" + tax.setScale(2, RoundingMode.CEILING).toString());
		textFinalTotalProject.setText("$" + total.add(tax).setScale(2, RoundingMode.CEILING).toString());
	}

	public boolean cashiered() {
		return cashiered;
	}
	// END RO getters

	private void checkPartsOnHand(Job job, List<JobPart> jobParts) {
		// check each jobPart and see if current jobPart.quantity > jobPart.getPart.getOnHand, if so inform user
			// with warning dialog and change quantity to onHand
		boolean partsOnHandChanged = false;
		Map<String, List<JobPart>> changedParts = new HashMap<>();

		for (JobPart jobPart : jobParts) {
			if (jobPart.getQuantity() > jobPart.getPart().getOnHand()) {
				partsOnHandChanged = true;
				changedParts.computeIfAbsent(job.getJobName(), jp -> new ArrayList<>()).add(jobPart);
				jobPart.setQuantity(jobPart.getPart().getOnHand());
			}
		}
		if (partsOnHandChanged) {
			PartQuantityWarning partQuantityWarning = new PartQuantityWarning(shlProject, getStyle());
			partQuantityWarning.setParts(changedParts);
			partQuantityWarning.open();
		}		
	}
	
	private void createContents() {
		shlProject = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlProject.setText(getText());
		
		int width = 992;
		int height = 736;
		shlProject.setSize(width, height);
		shlProject.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (shlProject.getSize().x < new Point(width, height).x
						|| shlProject.getSize().y < new Point(width, height).y) {
					shlProject.setSize(width, height);	// set a minimum shell size
				}
				
			}
		});
		
		FormLayout fl_shlProject = new FormLayout();
		fl_shlProject.spacing = 5;
		fl_shlProject.marginWidth = 5;
		fl_shlProject.marginHeight = 5;
		shlProject.setLayout(fl_shlProject);
				
		Gui.setDialogAtCenter(shlProject);
		
//		roDetails();

		roControls();
		
		jobTabs();
		
		roDetails();
		
		setLayout();
	}
	
	public void disableJobTabs() {
		txtJobName.setEnabled(false);
		txtJobName.setText("Job Name...");
		txtComplaints.setEnabled(false);
		txtComplaints.setText("Complaints...");
		txtResolution.setEnabled(false);
		txtResolution.setText("Resolution...");
		txtReccomendations.setEnabled(false);
		txtReccomendations.setText("Reccomendations...");
		jobPartsTable.setEnabled(false);
		jobPartsTable.removeAll();
		jobLaborTable.setEnabled(false);
		jobLaborTable.removeAll();
		btnDeleteLineItem.setEnabled(false);
		btnAddLaborLine.setEnabled(false);
		btnDeleteLaborLine.setEnabled(false);
	}
	
	private void disableRoControls() {
		// disable editing of text boxes, disable buttons
			// remove listeners from Customer and ProjectSite text box and from Parts and Labor tables
		cashiered = true;
		txtCustomerProject.removeMouseListener(customerSearchListener);
		txtProjectSiteProject.removeMouseListener(unitSearchListener);
		btnCashierRo.setEnabled(false);
		btnAddJob.setEnabled(false);
		btnDeleteJob.setEnabled(false);
		btnCancel.setEnabled(false);
		btnSaveRo.setEnabled(false);
		btnDeleteLineItem.setEnabled(false);
		btnAddLaborLine.setEnabled(false);
		btnDeleteLaborLine.setEnabled(false);
		txtJobName.setEditable(false);
		txtJobName.removeFocusListener(jobNameFocusListener);
		txtComplaints.setEditable(false);
		txtComplaints.removeFocusListener(complaintsFocusListener);
		txtResolution.setEditable(false);
		txtResolution.removeFocusListener(resolutionFocusListener);
		txtReccomendations.setEditable(false);
		txtReccomendations.removeFocusListener(reccomendationsFocusListener);
		jobPartsTable.removeListener(SWT.MouseDown, projectPartTableListener);
		jobLaborTable.removeListener(SWT.MouseDown, projectLaborTableListener);		
	}
	
	public Map<String, List<Long>> getDetailsToDelete() {
		return detailsToDelete;
	}
	
	public List<Button> getJobDetailsButtons() {
		return List.of(btnDeleteLineItem, btnAddLaborLine, btnDeleteLaborLine);
	}
	
	public JobDetailsModifiedListener getJobDetailsModifiedListener() {
		return jobDetailsModifiedListener;
	}

	public List<MyText> getJobDetailsText() {
		return List.of(txtJobName, txtComplaints, txtResolution, txtReccomendations);
	}

	public JobLaborTable getJobLaborTable() {
		return jobLaborTable;
	}
	
	public JobNameModifiedListener getJobNameModifiedListener() {
		return jobNameModifiedListener;
	}
	
	public InvoicePartTable getJobPartsTable() {
		return jobPartsTable;
	}
	
	// RO getters
	public Shell getRoDialogShell() {
		return shlProject;
	}

	public TabFolder getTabFolderJobsProject() {
		return tabFolderJobsProject;
	}

	public ProjectJobTable getTableJobsProject() {
		return tableJobsProject;
	}
	
	public Text getTextCashieredDate() {
		return textCashieredDate;
	}

	public MyText getTxtCustomerProject() {
		return txtCustomerProject;
	}

	public MyText getTxtProjectSiteProject() {
		return txtProjectSiteProject;
	}
	
	private void jobTabs() {

		// Job tabs
		tabFolderJobsProject = new TabFolder(shlProject, SWT.NONE);
		FormData fd_tabFolderJobsProject = new FormData();
		fd_tabFolderJobsProject.top = new FormAttachment(65);
		fd_tabFolderJobsProject.bottom = new FormAttachment(100);
		fd_tabFolderJobsProject.right = new FormAttachment(100);
		fd_tabFolderJobsProject.left = new FormAttachment(0);
		tabFolderJobsProject.setLayoutData(fd_tabFolderJobsProject);

		// Job Details Tab
		TabItem tbtmJobDetails = new TabItem(tabFolderJobsProject, SWT.NONE);
		tbtmJobDetails.setText("Job Details");

		Composite jobDetailsComposite = new Composite(tabFolderJobsProject, SWT.NONE);
		tbtmJobDetails.setControl(jobDetailsComposite);
		FormLayout fl_jobDetailsComposite = new FormLayout();
		fl_jobDetailsComposite.spacing = 5;
		fl_jobDetailsComposite.marginWidth = 5;
		fl_jobDetailsComposite.marginHeight = 5;
		jobDetailsComposite.setLayout(fl_jobDetailsComposite);

		txtJobName = new MyText(jobDetailsComposite, SWT.BORDER);
		FormData fd_txtJobName = new FormData();
		fd_txtJobName.height = 24;
		fd_txtJobName.right = new FormAttachment(50, -5);
		fd_txtJobName.top = new FormAttachment(0, 5);
		fd_txtJobName.left = new FormAttachment(0);
		txtJobName.setLayoutData(fd_txtJobName);
		txtJobName.setText("Job Name...");
		txtJobName.setEnabled(false);

		txtComplaints = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtComplaints = new FormData();
		fd_txtComplaints.top = new FormAttachment(txtJobName);
		fd_txtComplaints.bottom = new FormAttachment(100);
		fd_txtComplaints.right = new FormAttachment(50, -5);
		fd_txtComplaints.left = new FormAttachment(0);
		txtComplaints.setLayoutData(fd_txtComplaints);
		txtComplaints.setText("Complaints...");
		txtComplaints.setEnabled(false);

		txtResolution = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtResolution = new FormData();
		fd_txtResolution.top = new FormAttachment(0, 5);
		fd_txtResolution.left = new FormAttachment(txtComplaints, 5);
		fd_txtResolution.bottom = new FormAttachment(50, -5);
		fd_txtResolution.right = new FormAttachment(100);
		txtResolution.setLayoutData(fd_txtResolution);
		txtResolution.setText("Resolution...");
		txtResolution.setEnabled(false);

		txtReccomendations = new MyText(jobDetailsComposite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtReccomendations = new FormData();
		fd_txtReccomendations.left = new FormAttachment(txtComplaints, 5);
		fd_txtReccomendations.bottom = new FormAttachment(100);
		fd_txtReccomendations.right = new FormAttachment(100);
		fd_txtReccomendations.top = new FormAttachment(50, 5);
		txtReccomendations.setLayoutData(fd_txtReccomendations);
		txtReccomendations.setText("Reccomendations...");
		txtReccomendations.setEnabled(false);
		// END Job Details Tab

		// Job Part tab
		TabItem tbtmParts = new TabItem(tabFolderJobsProject, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite jobPartsComposite = new Composite(tabFolderJobsProject, SWT.NONE);
		tbtmParts.setControl(jobPartsComposite);

		jobPartsTable = new InvoicePartTable(jobPartsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		jobPartsTable.setLinesVisible(true);
		jobPartsTable.setHeaderVisible(true);
		jobPartsTable.setBounds(10, 10, 915, 243);
		jobPartsTable.setEnabled(false);

		TableColumn tblclmnPartNumberInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnPartNumberInvoice.setWidth(126);
		tblclmnPartNumberInvoice.setText("Part Number");

		TableColumn tblclmnDescriptionInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnDescriptionInvoice.setWidth(262);
		tblclmnDescriptionInvoice.setText("Description");

		TableColumn tblclmnQuantityInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnQuantityInvoice.setWidth(91);
		tblclmnQuantityInvoice.setText("Quantity");

		TableColumn tblclmnOnHandInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnOnHandInvoice.setWidth(100);
		tblclmnOnHandInvoice.setText("On Hand");

		TableColumn tblclmnCostInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnCostInvoice.setWidth(100);
		tblclmnCostInvoice.setText("Cost");

		TableColumn tblclmnPartPriceInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnPartPriceInvoice.setWidth(110);
		tblclmnPartPriceInvoice.setText("Part Price");

		TableColumn tblclmnExtendedPriceInvoice = new TableColumn(jobPartsTable, SWT.NONE);
		tblclmnExtendedPriceInvoice.setWidth(114);
		tblclmnExtendedPriceInvoice.setText("Extended Price");
		// END Job Part tab

		// Labor tab
		TabItem tbtmLabor = new TabItem(tabFolderJobsProject, SWT.NONE);
		tbtmLabor.setText("Labor");

		Composite jobLaborComposite = new Composite(tabFolderJobsProject, SWT.NONE);
		tbtmLabor.setControl(jobLaborComposite);

		jobLaborTable = new JobLaborTable(jobLaborComposite, SWT.BORDER | SWT.FULL_SELECTION);
		jobLaborTable.setBounds(10, 10, 926, 243);
		jobLaborTable.setHeaderVisible(true);
		jobLaborTable.setLinesVisible(true);
		jobLaborTable.setEnabled(false);

		TableColumn tblclmnTechnician = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnTechnician.setWidth(100);
		tblclmnTechnician.setText("Technician");

		TableColumn tblclmnDescription = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnDescription.setWidth(507);
		tblclmnDescription.setText("Description");

		TableColumn tblclmnHours = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnHours.setWidth(100);
		tblclmnHours.setText("Hours");

		TableColumn tblclmnRate = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnRate.setWidth(100);
		tblclmnRate.setText("Rate");

		TableColumn tblclmnTotal = new TableColumn(jobLaborTable, SWT.NONE);
		tblclmnTotal.setWidth(100);
		tblclmnTotal.setText("Total");
		// END Labor tab
		// END Job tabs

		// Job Tabs Buttons
		// These buttons need to be before the TabFolder to be painted over top of it
		btnDeleteLineItem = new Button(shlProject, SWT.NONE);
		fd_tableJobsProject.bottom = new FormAttachment(btnDeleteLineItem, -4);
		FormData fd_btnDeleteLineItem = new FormData();
		fd_btnDeleteLineItem.height = 48;
		fd_btnDeleteLineItem.width = 128;
		fd_btnDeleteLineItem.right = new FormAttachment(60, -10);
		fd_btnDeleteLineItem.bottom = new FormAttachment(tabFolderJobsProject);
		btnDeleteLineItem.setLayoutData(fd_btnDeleteLineItem);
		btnDeleteLineItem.setText("Delete Line Item");
		btnDeleteLineItem.setVisible(false);
		btnDeleteLineItem.setEnabled(false);

		btnDeleteLaborLine = new Button(shlProject, SWT.NONE);
		FormData fd_btnDeleteLaborLine = new FormData();
		fd_btnDeleteLaborLine.height = 48;
		fd_btnDeleteLaborLine.width = 128;
		fd_btnDeleteLaborLine.right = new FormAttachment(60, -10);
		fd_btnDeleteLaborLine.bottom = new FormAttachment(tabFolderJobsProject);
		btnDeleteLaborLine.setLayoutData(fd_btnDeleteLaborLine);
		btnDeleteLaborLine.setText("Delete Labor");
		btnDeleteLaborLine.setVisible(false);
		btnDeleteLaborLine.setEnabled(false);

		btnAddLaborLine = new Button(shlProject, SWT.NONE);
		FormData fd_btnAddLaborLine = new FormData();
		fd_btnAddLaborLine.height = 48;
		fd_btnAddLaborLine.width = 128;
		fd_btnAddLaborLine.bottom = new FormAttachment(tabFolderJobsProject);
		fd_btnAddLaborLine.right = new FormAttachment(btnDeleteLaborLine, -10);
		btnAddLaborLine.setLayoutData(fd_btnAddLaborLine);
		btnAddLaborLine.setText("Add Labor");
		btnAddLaborLine.setVisible(false);
		btnAddLaborLine.setEnabled(false);
		// END Job Tabs Buttons
	}
	
	private void loadRoFromDb(Project project) {
		// set Dialog boxes and stuff from project fields
		if (project != null) {
			currentProject = project;
			projectId = project.getProjectId();
			textRoNum.setText(Long.toString(projectId));
			
			textCreatedDate.setText(project.getCreatedDate().toString());
			textCashieredDate.setText(project.getClosedDate() == null ? "" : project.getClosedDate().toString());
			
			if (!textCashieredDate.getText().equals("")) {
				disableRoControls();
			}
			
			if (project.getCustomerId() != 0) {
				txtCustomerProject.setData(new Customer().searchByPk(project.getCustomerId()));
				txtCustomerProject.setText(project.getCustomerName() + "\n" + project.getCustomerData());
			}

			if (project.getProjectSiteId() != 0) {
				ProjectSite roUnit = new ProjectSite().searchByPk(project.getProjectSiteId());
				txtProjectSiteProject.setData(roUnit);
				txtProjectSiteProject.setText(roUnit.getProjectSiteState() + " " + roUnit.getProjectSiteAddress() + "\n" +
						roUnit.getProjectSiteCity() + "\nVin: " + roUnit.getProjectSiteNotes() + "\nColor: " +
						roUnit.getProjectSiteZipCode());
			}

			List<Job> roJobs = loadRoJobsFromDb();
			project.setJobs(roJobs);
			tableJobsProject.setSelection(0);
			tableJobsProject.notifyListeners(SWT.Selection, new Event());
			this.calcRoTotal();
		}
	}
	
	private List<Job> loadRoJobsFromDb() {
		List<Job> roJobs = new ArrayList<>();
		
		for (Job job : new Job().searchForJobsByProjectId(projectId)) {
			
			long jobId = job.getJobId();
		
			// setup table item to hold RO Job
			ProjectJobTableItem jobTableItem = new ProjectJobTableItem(tableJobsProject, getStyle());

			// find and build labor
			job.setLabor(new JobLabor().searchJobLaborByProjectId(jobId));

			// find and build Parts
			List<JobPart> listOfJobParts = new JobPart().searchJobLaborByJobId(jobId);
			
			if (!cashiered) {
				checkPartsOnHand(job, listOfJobParts);
			}

			job.setJobParts(listOfJobParts);
			roJobs.add(job);
			jobTableItem.setData(job);
		}
		return roJobs;
	}
	
	public Object open() {
		createContents();
		setupListeners();
		shlProject.open();
		shlProject.layout();
		Display display = getParent().getDisplay();
		while (!shlProject.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(Project project) {
		createContents();
		setupListeners();
		
		loadRoFromDb(project);
		
		shlProject.open();
		shlProject.layout();
		Display display = getParent().getDisplay();
		while (!shlProject.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public void reloadRo() {
		loadRoFromDb(currentProject);
	}

	private void roControls() {
		// Jobs table
		tableJobsProject = new ProjectJobTable(shlProject, SWT.BORDER | SWT.FULL_SELECTION);
		fd_tableJobsProject = new FormData();
		fd_tableJobsProject.right = new FormAttachment(68);
		fd_tableJobsProject.left = new FormAttachment(0, 5);
		fd_tableJobsProject.top = new FormAttachment(0, 144);
		tableJobsProject.setLayoutData(fd_tableJobsProject);
		tableJobsProject.setHeaderVisible(true);
		tableJobsProject.setLinesVisible(true);
		tableJobsProject.setFocus();

		TableColumn tblclmnJobName = new TableColumn(tableJobsProject, SWT.NONE);
		tblclmnJobName.setWidth(330);
		tblclmnJobName.setText("Job Name");

		TableColumn tblclmnPartsTotal = new TableColumn(tableJobsProject, SWT.NONE);
		tblclmnPartsTotal.setWidth(100);
		tblclmnPartsTotal.setText("Parts Total");

		TableColumn tblclmnLaborTotal = new TableColumn(tableJobsProject, SWT.NONE);
		tblclmnLaborTotal.setWidth(100);
		tblclmnLaborTotal.setText("Labor Total");

		TableColumn tblclmnJobTotal = new TableColumn(tableJobsProject, SWT.NONE);
		tblclmnJobTotal.setWidth(111);
		tblclmnJobTotal.setText("Job Total");
		// END Jobs table

		// RO Controls
		btnCashierRo = new Button(shlProject, SWT.NONE);
		btnCashierRo.setSize(128,  128);
		fd_btnCashierRo = new FormData();
		fd_btnCashierRo.top = new FormAttachment(0, 10);
		btnCashierRo.setLayoutData(fd_btnCashierRo);
		btnCashierRo.setText("Cashier");
		
		btnClose = new Button(shlProject, SWT.NONE);
		fd_btnCashierRo.right = new FormAttachment(btnClose, -5);
		fd_btnClose = new FormData();
		fd_btnClose.top = new FormAttachment(0, 10);
		fd_btnClose.width = 64;
		btnClose.setLayoutData(fd_btnClose);
		btnClose.setText("Close");
		// END RO controls
	}
	
	private void roDetails() {
		
		grpProjectNum = new Group(shlProject, SWT.NONE);
		grpProjectNum.setText("Project #");
		
		textRoNum = new MyText(grpProjectNum, SWT.BORDER);
		textRoNum.setEditable(false);
		
		grpCreatedDate = new Group(shlProject, SWT.NONE);
		grpCreatedDate.setText("Created Date");
		
		textCreatedDate = new MyText(grpCreatedDate, SWT.BORDER);
		textCreatedDate.setEditable(false);
		
		grpCashieredDate = new Group(shlProject, SWT.NONE);
		grpCashieredDate.setText("Cashiered Date");
		
		textCashieredDate = new MyText(grpCashieredDate, SWT.BORDER);
		textCashieredDate.setEditable(false);
		
		// Customer Data
		txtCustomerProject = new MyText(shlProject, SWT.BORDER | SWT.WRAP);
		txtCustomerProject.setEditable(false);
		txtCustomerProject.setText("Customer...");
		txtCustomerProject.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		
		txtProjectSiteProject = new MyText(shlProject, SWT.BORDER | SWT.WRAP);
		txtProjectSiteProject.setEditable(false);
		txtProjectSiteProject.setText("ProjectSite...");
		txtProjectSiteProject.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		// END Customer Data

		// Totals
		grpTotals = new Group(shlProject, SWT.NONE);

		txtSubTotalProject = new MyText(grpTotals, SWT.BORDER);
		txtSubTotalProject.setEditable(false);
		txtSubTotalProject.setText("$0.00");

		textTaxProject = new MyText(grpTotals, SWT.BORDER);
		textTaxProject.setText("$0.00");
		textTaxProject.setEditable(false);

		textFinalTotalProject = new MyText(grpTotals, SWT.BORDER);
		textFinalTotalProject.setText("$0.00");
		textFinalTotalProject.setEditable(false);

		lblSubTotal = new Label(grpTotals, SWT.NONE);
		lblSubTotal.setText("Sub Total");

		lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setText("Tax");

		lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setText("Final Total");

		roButtonsComposite = new Composite(shlProject, SWT.NONE);

		btnAddJob = new Button(roButtonsComposite, SWT.NONE);
		btnAddJob.setText("Add Job");

		btnCancel = new Button(roButtonsComposite, SWT.NONE);
		btnCancel.setText("Cancel");

		btnDeleteJob = new Button(roButtonsComposite, SWT.CENTER | SWT.WRAP);
		btnDeleteJob.setText("Delete Job");

		btnSaveRo = new Button(roButtonsComposite, SWT.NONE);
		btnSaveRo.setText("Save");
		// END Totals
	}

	public void saveJob() {
		Job currentJob = (Job) tabFolderJobsProject.getData();

		for (TableItem currentPartTableItem : jobPartsTable.getItems()) {
			JobPart currentTIPart = (JobPart) currentPartTableItem.getData();
			List<JobPart> currentParts = currentJob.getJobParts();
			if (currentTIPart != null && !currentParts.contains(currentTIPart)) {
				currentJob.addJobPart((JobPart) currentPartTableItem.getData());
			}
		}

		for (TableItem currentLaborTableItem : jobLaborTable.getItems()) {
			if (!currentJob.getLabor().contains(currentLaborTableItem.getData()))
				currentJob.addLabor((JobLabor) currentLaborTableItem.getData());
		}
	}

	private void saveNewRo() {
		Project project = new Project();
//		project.setLocationId(Application.getSettings().getCurrentLocation());
		long currentLocationId = Application.getSettings().getCurrentLocation();
		long locationId = currentLocationId != 0 ? currentLocationId : ((Customer) txtCustomerProject.getData()).getLocationId();
		project.setLocationId(locationId);
		project.setCreatedDate(Timestamp.from(Instant.now()));
		textCreatedDate.setText(project.getCreatedDate().toString());
		saveRo(project);
	}
	
	private void saveRo(Project project) {
		if (txtCustomerProject.getData() != null && ((Customer) txtCustomerProject.getData()).getCustomerId() != -1) {
			project.setCustomerId(((Customer) txtCustomerProject.getData()).getCustomerId());
			project.setCustomerName(txtCustomerProject.getText(0, txtCustomerProject.getText().indexOf('\n')).split(","));
			project.setCustomerData(txtCustomerProject.getText().substring(txtCustomerProject.getText().indexOf('\n')));
		} else {
			// if we haven't selected a Customer, complain - an RO Customer is required
			MessageBox customerRequiredBox = new MessageBox(shlProject, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a Customer");
			customerRequiredBox.open();
			return;
		}
		
		if (txtProjectSiteProject.getData() != null && ((ProjectSite) txtProjectSiteProject.getData()).getProjectSiteId() != -1) {
			ProjectSite thisUnit = (ProjectSite) txtProjectSiteProject.getData();
			project.setProjectSiteId(thisUnit.getProjectSiteId());
			project.setProjectSiteAddress(thisUnit.getProjectSiteState());
			project.setProjectSiteCity(thisUnit.getProjectSiteAddress());
			project.setProjectSiteState(thisUnit.getProjectSiteCity());
			project.setProjectSiteZip(thisUnit.getProjectSiteNotes());
		} else {
			// if we haven't selected a ProjectSite, complain - an RO ProjectSite is required
			MessageBox customerRequiredBox = new MessageBox(shlProject, SWT.ICON_INFORMATION);
			customerRequiredBox.setText("Notice");
			customerRequiredBox.setMessage("Please select a ProjectSite");
			customerRequiredBox.open();
			return;
		}
		
		// TODO do something with the returned updateCount
		DbServices.deleteDetailsFromRo(detailsToDelete);
		
		if (tableJobsProject.getItemCount() > 0) {
			for (TableItem jobTableItem : tableJobsProject.getItems()) {
				if (!project.getJobs().contains(jobTableItem.getData())) {		// maybe use HashSet here
					project.addJob((Job) jobTableItem.getData());
				}
			}
		}
		
		project.setTax(new BigDecimal(textTaxProject.getText().replaceAll(ONLY_DECIMALS, "")));
		project.setTotal(new BigDecimal(textFinalTotalProject.getText().replaceAll(ONLY_DECIMALS, "")));
		
		// send Project object to DbServices
		DbServices.saveObject(project);
		projectId = project.getProjectId();
		textRoNum.setText(Long.toString(projectId));
		detailsToDelete = new HashMap<>();
		currentProject = project;
		result = project;
	}
	
	public void setDetailsToDelete(Map<String, List<Long>> newDetailsToDelete) {
		detailsToDelete = newDetailsToDelete;
	}
	
	private void setLayout() {
		
		grpProjectNum.setLayout(new FormLayout());
		FormData fd_grpProjectNum = new FormData();
		fd_grpProjectNum.bottom = new FormAttachment(10);
		fd_grpProjectNum.right = new FormAttachment(100);
		fd_grpProjectNum.top = new FormAttachment(0, -5);
		grpProjectNum.setLayoutData(fd_grpProjectNum);
		
		FormData fd_textRoNum = new FormData();
		fd_textRoNum.bottom = new FormAttachment(100, -5);
		fd_textRoNum.right = new FormAttachment(0, 100);
		fd_textRoNum.top = new FormAttachment(0);
		fd_textRoNum.left = new FormAttachment(0);
		textRoNum.setLayoutData(fd_textRoNum);
		
		grpCreatedDate.setLayout(new FormLayout());
		FormData fd_grpCreatedDate = new FormData();
		fd_grpCreatedDate.height = 36;
		fd_grpCreatedDate.right = new FormAttachment(100);
		fd_grpCreatedDate.left = new FormAttachment(tableJobsProject);
		fd_grpCreatedDate.bottom = new FormAttachment(grpCashieredDate, -6);
		grpCreatedDate.setLayoutData(fd_grpCreatedDate);
		
		FormData fd_textCreatedDate = new FormData();
		fd_textCreatedDate.bottom = new FormAttachment(100, -5);
		fd_textCreatedDate.right = new FormAttachment(100);
		fd_textCreatedDate.top = new FormAttachment(0);
		fd_textCreatedDate.left = new FormAttachment(0);
		textCreatedDate.setLayoutData(fd_textCreatedDate);
		
		grpCashieredDate.setLayout(new FormLayout());
		FormData fd_grpCashieredDate = new FormData();
		fd_grpCashieredDate.height = 36;
		fd_grpCashieredDate.right = new FormAttachment(100);
		fd_grpCashieredDate.left = new FormAttachment(tableJobsProject);
		grpCashieredDate.setLayoutData(fd_grpCashieredDate);
		
		FormData fd_textCashieredDate = new FormData();
		fd_textCashieredDate.bottom = new FormAttachment(100, -5);
		fd_textCashieredDate.right = new FormAttachment(100);
		fd_textCashieredDate.top = new FormAttachment(0);
		fd_textCashieredDate.left = new FormAttachment(0);
		textCashieredDate.setLayoutData(fd_textCashieredDate);
		
		FormData fd_txtCustomerProject = new FormData();
		fd_txtCustomerProject.left = new FormAttachment(0, 5);
		fd_txtCustomerProject.bottom = new FormAttachment(tableJobsProject, -6);
		fd_txtCustomerProject.top = new FormAttachment(0, 5);
		fd_txtCustomerProject.right = new FormAttachment(txtProjectSiteProject, -5);
		txtCustomerProject.setLayoutData(fd_txtCustomerProject);

		fd_btnClose.right = new FormAttachment(grpProjectNum, -16);
		fd_btnClose.bottom = new FormAttachment(10, -5);
		fd_btnCashierRo.bottom = new FormAttachment(10, -5);
		fd_btnCashierRo.left = new FormAttachment(txtProjectSiteProject, 11);
		
		FormData fd_txtProjectSiteProject = new FormData();
		fd_txtProjectSiteProject.left = new FormAttachment(35, -2);
		fd_txtProjectSiteProject.bottom = new FormAttachment(txtCustomerProject, 0, SWT.BOTTOM);
		fd_txtProjectSiteProject.right = new FormAttachment(68);
		fd_txtProjectSiteProject.top = new FormAttachment(0, 5);
		txtProjectSiteProject.setLayoutData(fd_txtProjectSiteProject);
		
		FormLayout fl_grpTotals = new FormLayout();
		fl_grpTotals.marginWidth = 5;
		fl_grpTotals.marginHeight = 5;
		grpTotals.setLayout(fl_grpTotals);
		FormData fd_grpTotals = new FormData();
		fd_grpTotals.bottom = new FormAttachment(tabFolderJobsProject, 30);
		fd_grpTotals.right = new FormAttachment(100);
		fd_grpTotals.left = new FormAttachment(60);
		grpTotals.setLayoutData(fd_grpTotals);
		
		FormData fd_txtSubTotalProject = new FormData();
		fd_txtSubTotalProject.bottom = new FormAttachment(100);
		fd_txtSubTotalProject.right = new FormAttachment(33, -2);
		fd_txtSubTotalProject.top = new FormAttachment(0, 21);
		fd_txtSubTotalProject.left = new FormAttachment(0);
		txtSubTotalProject.setLayoutData(fd_txtSubTotalProject);
		
		FormData fd_textTaxProject = new FormData();
		fd_textTaxProject.bottom = new FormAttachment(0, 47);
		fd_textTaxProject.right = new FormAttachment(66, -2);
		fd_textTaxProject.top = new FormAttachment(0, 21);
		fd_textTaxProject.left = new FormAttachment(33, 2);
		textTaxProject.setLayoutData(fd_textTaxProject);
		
		FormData fd_textFinalTotalProject = new FormData();
		fd_textFinalTotalProject.bottom = new FormAttachment(0, 47);
		fd_textFinalTotalProject.right = new FormAttachment(100);
		fd_textFinalTotalProject.top = new FormAttachment(0, 21);
		fd_textFinalTotalProject.left = new FormAttachment(66, 2);
		textFinalTotalProject.setLayoutData(fd_textFinalTotalProject);
		
		FormData fd_lblSubTotal = new FormData();
		fd_lblSubTotal.bottom = new FormAttachment(txtSubTotalProject, -6);
		fd_lblSubTotal.left = new FormAttachment(txtSubTotalProject, 0, SWT.LEFT);
		lblSubTotal.setLayoutData(fd_lblSubTotal);
		
		FormData fd_lblTax = new FormData();
		fd_lblTax.left = new FormAttachment(textTaxProject, 0, SWT.LEFT);
		lblTax.setLayoutData(fd_lblTax);
		
		FormData fd_lblFinalTotal = new FormData();
		fd_lblFinalTotal.left = new FormAttachment(textFinalTotalProject, 0, SWT.LEFT);
		lblFinalTotal.setLayoutData(fd_lblFinalTotal);
		
		fd_grpCashieredDate.bottom = new FormAttachment(roButtonsComposite, -6);
		roButtonsComposite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(grpTotals);
		fd_composite.top = new FormAttachment(35);
		fd_composite.left = new FormAttachment(tableJobsProject);
		fd_composite.right = new FormAttachment(100);
		roButtonsComposite.setLayoutData(fd_composite);
		
		FormData fd_btnAddJob = new FormData();
		fd_btnAddJob.bottom = new FormAttachment(100, -87);
		fd_btnAddJob.top = new FormAttachment(0, 5);
		fd_btnAddJob.right = new FormAttachment(btnSaveRo, -5);
		fd_btnAddJob.left = new FormAttachment(0, 5);
		btnAddJob.setLayoutData(fd_btnAddJob);
		
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(btnAddJob, 6);
		fd_btnCancel.bottom = new FormAttachment(100, -5);
		fd_btnCancel.left = new FormAttachment(0, 5);
		btnCancel.setLayoutData(fd_btnCancel);
		
		FormData fd_btnDeleteJob = new FormData();
		fd_btnDeleteJob.top = new FormAttachment(btnAddJob, 6);
		fd_btnDeleteJob.left = new FormAttachment(btnCancel, 5);
		fd_btnDeleteJob.right = new FormAttachment(btnSaveRo, -5);
		fd_btnDeleteJob.bottom = new FormAttachment(100, -5);
		btnDeleteJob.setLayoutData(fd_btnDeleteJob);
		
		FormData fd_btnSaveRo = new FormData();
		fd_btnSaveRo.left = new FormAttachment(50, 22);
		fd_btnSaveRo.bottom = new FormAttachment(100, -5);
		fd_btnSaveRo.top = new FormAttachment(0, 5);
		fd_btnSaveRo.right = new FormAttachment(100, -5);
		btnSaveRo.setLayoutData(fd_btnSaveRo);
		
	}
	
	private void setupListeners() {
		// LISTENERS
		// RO Controls
		shlProject.addListener(SWT.Traverse, event -> {		// disable pressing [Esc] to close the dialog
			if (event.detail == SWT.TRAVERSE_ESCAPE) {	// this doesn't seem to be working in the web version
				event.doit = false;
			}
		});
		
		shlProject.addListener(SWT.Close, new ProjectCloseListener(this));

		btnCancel.addMouseListener(new ProjectCancelListener(this));
		
		btnSaveRo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (projectId == -1) {
					saveNewRo();
				} else {
					saveRo(currentProject);
				}
			}
		});
		
		btnClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				shlProject.close();
			}
		});
		
		btnCashierRo.addMouseListener(new MouseAdapter() {
			// TODO refactor into it's own class
			@Override
			public void mouseDown(MouseEvent e) {
				// spawn amount due dialog box
				if (txtCustomerProject.getData() == null) {
					MessageBox customerRequiredBox = new MessageBox(shlProject, SWT.ICON_INFORMATION);
					customerRequiredBox.setText("Notice");
					customerRequiredBox.setMessage("Please select a Customer");
					customerRequiredBox.open();
					return;
				}
				AmountDueDialog amountDueDialog = new AmountDueDialog(shlProject, getStyle());
				AmountDueResponse cashieredNow = amountDueDialog.open(textFinalTotalProject);

				if (cashieredNow.getResult() != Result.CANCELED) {
					Application.getLogger().log(Level.INFO, "RO Cashiered");
					currentProject.setClosedDate(Timestamp.from(Instant.now()));
					cashiered = true;
					saveRo(currentProject);
					
					if (cashieredNow.getResult() == Result.CHARGED) {
						currentProject.setAmountCharged(cashieredNow.getAmountCharged());
						
						Account accountChargedTo = new Account().searchByPk(currentProject.getCustomerId());
						accountChargedTo.chargeNewTransaction(currentProject);

						accountChargedTo.save();		// since we've already saved the new Transaction above, this is really only saving the new balance on the Account
					}
					
					shlProject.close();
				}
			}
		});
		
		customerSearchListener = new CustomerSearchListener(txtCustomerProject);
		txtCustomerProject.addMouseListener(customerSearchListener);
		txtCustomerProject.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerProject));

		unitSearchListener = new UnitSearchListener(txtProjectSiteProject, txtCustomerProject);
			// TODO make sure this doesn't break when there isn't a customer selected already
		txtProjectSiteProject.addMouseListener(unitSearchListener);
		txtProjectSiteProject.addModifyListener(new RequiredTextBoxModifyListener(txtProjectSiteProject));
		// END RO Controls
		
		// Job Controls
		btnAddJob.addMouseListener(new ProjectJobAddListener(this));

		btnDeleteJob.addMouseListener(new ProjectJobDeleteListener(this));

		tableJobsProject.addSelectionListener(new ProjectJobSelectedListener(this));

		btnDeleteLineItem.addMouseListener(new ProjectPartDeleteLineItemListener(jobPartsTable, tableJobsProject, this));

		btnAddLaborLine.addMouseListener(new ProjectLaborAddListener(this));

		btnDeleteLaborLine.addMouseListener(new ProjectLaborDeleteListener(this));

		tabFolderJobsProject.addSelectionListener(new SelectionAdapter() {
			// TODO refactor into it's own class
			@Override		// set visibility of Tab function buttons
			public void widgetSelected(SelectionEvent e) {
				if (tabFolderJobsProject.getSelectionIndex() == 0) {		// Job Details tab
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsProject.getSelectionIndex() == 1) {		// Parts tab
					Application.getLogger().log(Level.INFO, "Project Parts tab selected");
					btnDeleteLineItem.setVisible(true);
					btnAddLaborLine.setVisible(false);
					btnDeleteLaborLine.setVisible(false);
				} else if (tabFolderJobsProject.getSelectionIndex() == 2) {		// Labor tab
					Application.getLogger().log(Level.INFO, "Project Labor tab selected");
					btnDeleteLineItem.setVisible(false);
					btnAddLaborLine.setVisible(true);
					btnDeleteLaborLine.setVisible(true);
				}
			}
		});

		jobNameModifiedListener = new JobNameModifiedListener(txtJobName, tableJobsProject);
		txtJobName.addModifyListener(jobNameModifiedListener);
		jobNameFocusListener = new TextBoxFocusListener(txtJobName);
		txtJobName.addFocusListener(jobNameFocusListener);

		complaintsFocusListener = new TextBoxFocusListener(txtComplaints);
		txtComplaints.addFocusListener(complaintsFocusListener);

		resolutionFocusListener = new TextBoxFocusListener(txtResolution);
		txtResolution.addFocusListener(resolutionFocusListener);
		
		reccomendationsFocusListener = new TextBoxFocusListener(txtReccomendations);
		txtReccomendations.addFocusListener(reccomendationsFocusListener);

		projectPartTableListener = new InvoicePartTableListener(jobPartsTable, tableJobsProject, this, shlProject);
		jobPartsTable.addListener(SWT.MouseDown, projectPartTableListener);

		projectLaborTableListener = new ProjectLaborTableListener(jobLaborTable, tableJobsProject, this);
		jobLaborTable.addListener(SWT.MouseDown, projectLaborTableListener);

		jobDetailsModifiedListener = new JobDetailsModifiedListener(this);
		txtJobName.addModifyListener(jobDetailsModifiedListener);
		txtComplaints.addModifyListener(jobDetailsModifiedListener);
		txtResolution.addModifyListener(jobDetailsModifiedListener);
		txtReccomendations.addModifyListener(jobDetailsModifiedListener);
		
		// END LISTENERS
	}
}
