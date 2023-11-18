package gui;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableColumn;

import listeners.CreateNewObjectListener;
import listeners.CustomerSearchListener;
import listeners.DeleteEnableListener;
import listeners.DeleteObjectListener;
import listeners.InvoicePartDeleteLineItemListener;
import listeners.InfoTextBoxModifyListener;
import listeners.OpenExistingObjectMouseListener;
import listeners.InvoicePartTableListener;
import listeners.RequiredTextBoxModifyListener;
import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import objects.Account;
import objects.AccountPayment;
import objects.AccountTransaction;
import objects.AmountDueResponse;
import objects.AmountDueResponse.Result;
import objects.Customer;
import objects.Invoice;
import objects.InvoicePart;
import objects.Project;
import objects.Part;
import objects.ProjectSite;
import objects.StoreLocation;
import objects.Invoice.InvoiceResult;
import web_invoice_story.Application;
import web_invoice_story.DbServices;

import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.AccountTable;
import guiParts.CustomerTable;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;
import guiParts.MyTable;
import guiParts.MyText;
import guiParts.PartInventoryTable;
import guiParts.RepairOrderTable;
import guiParts.StoreLocationCombo;
import guiParts.ProjectSiteTable;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class Gui extends Composite {

	private Shell shell;
	private MyText txtCustomerInvoice;
	private MyText txtInvoiceNotes;
	private MyText txtPartsTotalInvoice;
	private MyText txtTaxInvoice;
	private MyText txtFinalTotal;
	private MyText textCategoryInvoice;
	private MyText textSupplierInvoice;
	private MyText textPartNotesInvoice;
	private InvoicePartTable invoicePartsTable;

	public Gui(Composite parent, int style) {
		super(parent, style);
		
		this.shell = (Shell) parent;
		setLayout(new FormLayout());

		// Tab control widget
		TabFolder tabFolderGui = new TabFolder(this, SWT.NONE);
		FormData fd_tabFolderGui = new FormData();
		fd_tabFolderGui.bottom = new FormAttachment(100);
		fd_tabFolderGui.right = new FormAttachment(100);
		fd_tabFolderGui.top = new FormAttachment(0);
		fd_tabFolderGui.left = new FormAttachment(0);
		tabFolderGui.setLayoutData(fd_tabFolderGui);

		rosTab(tabFolderGui);
		partsTab(tabFolderGui);
		customersTab(tabFolderGui);
		reportsTab(tabFolderGui);
		settingsTab(tabFolderGui);
	}
	
	private void rosTab(TabFolder tabFolder) {
		TabItem tbtmProjects = new TabItem(tabFolder, SWT.NONE);
		tbtmProjects.setText("Projects");

		Composite projectsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmProjects.setControl(projectsComposite);
		projectsComposite.setLayout(new FormLayout());

		// Table for RO search results
		RepairOrderTable projectTable = new RepairOrderTable(projectsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_projectTable = new FormData();
		fd_projectTable.right = new FormAttachment(100, -10);
		fd_projectTable.bottom = new FormAttachment(100, -10);
		fd_projectTable.top = new FormAttachment(0, 74);
		fd_projectTable.left = new FormAttachment(0, 10);
		projectTable.setLayoutData(fd_projectTable);
		projectTable.setHeaderVisible(true);
		projectTable.setLinesVisible(true);
		projectTable.addMouseListener(new OpenExistingObjectMouseListener(projectTable, shell));
		projectTable.addMenuDetectListener(new DeleteEnableListener(projectTable));

		TableColumn tblclmnProjectProject = new TableColumn(projectTable, SWT.NONE);
		tblclmnProjectProject.setText("Project #");
		tblclmnProjectProject.setWidth(75);
		tblclmnProjectProject.addSelectionListener(new TableColumnSortListener(tblclmnProjectProject));

		TableColumn tblclmnCustomerRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnCustomerRo.setText("Customer");
		tblclmnCustomerRo.setWidth(164);
		tblclmnCustomerRo.addSelectionListener(new TableColumnSortListener(tblclmnCustomerRo));

		TableColumn tblclmnYearRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnYearRo.setText("Job Site Address");
		tblclmnYearRo.setWidth(204);
		tblclmnYearRo.addSelectionListener(new TableColumnSortListener(tblclmnYearRo));

		TableColumn tblclmnMakeRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnMakeRo.setText("City");
		tblclmnMakeRo.setWidth(165);
		tblclmnMakeRo.addSelectionListener(new TableColumnSortListener(tblclmnMakeRo));

		TableColumn tblclmnModelRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnModelRo.setText("State");
		tblclmnModelRo.setWidth(79);
		tblclmnModelRo.addSelectionListener(new TableColumnSortListener(tblclmnModelRo));

		TableColumn tblclmnJobSiteRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnJobSiteRo.setText("Zip");
		tblclmnJobSiteRo.setWidth(90);
		tblclmnJobSiteRo.addSelectionListener(new TableColumnSortListener(tblclmnJobSiteRo));

		TableColumn tblclmnJobsRo = new TableColumn(projectTable, SWT.NONE);
		tblclmnJobsRo.setText("Jobs");
		tblclmnJobsRo.setWidth(212);
		tblclmnJobsRo.addSelectionListener(new TableColumnSortListener(tblclmnJobsRo));

		// Delete right click menu
		setupRightClickMenu(projectTable, "Repair Order");
		
		// RO controls
		MyText projectSearchBox = new MyText(projectsComposite, SWT.BORDER);
		FormData fd_projectSearchBox = new FormData();
		fd_projectSearchBox.bottom = new FormAttachment(0, 33);
		fd_projectSearchBox.left = new FormAttachment(0, 10);
		fd_projectSearchBox.top = new FormAttachment(0, 10);
		projectSearchBox.setLayoutData(fd_projectSearchBox);
		projectSearchBox.setText("Search...");
		projectSearchBox.addModifyListener(new SearchTextBoxListeners(projectSearchBox, projectTable, new Project()));
		projectSearchBox.addFocusListener(new TextBoxFocusListener(projectSearchBox));

		Button btnNewProject = new Button(projectsComposite, SWT.NONE);
		fd_projectSearchBox.right = new FormAttachment(btnNewProject, -10);
		FormData fd_btnNewProject = new FormData();
		fd_btnNewProject.left = new FormAttachment(100, -161);
		fd_btnNewProject.bottom = new FormAttachment(0, 33);
		fd_btnNewProject.right = new FormAttachment(100, -10);
		fd_btnNewProject.top = new FormAttachment(0, 10);
		btnNewProject.setLayoutData(fd_btnNewProject);
		btnNewProject.setText("New Project");
		btnNewProject.addMouseListener(new CreateNewObjectListener(projectTable, projectSearchBox, shell));

		Button btnNewEstimate = new Button(projectsComposite, SWT.NONE);
		FormData fd_btnNewEstimate = new FormData();
		fd_btnNewEstimate.top = new FormAttachment(0, 43);
		fd_btnNewEstimate.bottom = new FormAttachment(0, 65);
		fd_btnNewEstimate.left = new FormAttachment(100, -161);
		fd_btnNewEstimate.right = new FormAttachment(100, -10);
		
		btnNewEstimate.setLayoutData(fd_btnNewEstimate);
		btnNewEstimate.setText("New Estimate");

		Button btnDeleteProject = new Button(projectsComposite, SWT.NONE);
		FormData fd_btnDeleteProject = new FormData();
		fd_btnDeleteProject.bottom = new FormAttachment(0, 65);
		fd_btnDeleteProject.left = new FormAttachment(100, -322);
		fd_btnDeleteProject.right = new FormAttachment(100, -171);
		fd_btnDeleteProject.top = new FormAttachment(0, 43);
		btnDeleteProject.setLayoutData(fd_btnDeleteProject);
		btnDeleteProject.setText("Delete Project");
		btnDeleteProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// delete Repair Order
			}
		});

		Button btnShowCashiered = new Button(projectsComposite, SWT.CHECK);
		FormData fd_btnShowCashiered = new FormData();
		fd_btnShowCashiered.bottom = new FormAttachment(0, 65);
		fd_btnShowCashiered.top = new FormAttachment(0, 43);
		fd_btnShowCashiered.left = new FormAttachment(0, 10);
		btnShowCashiered.setLayoutData(fd_btnShowCashiered);
		btnShowCashiered.setText("Show Cashiered");

		Button btnShowEstimates = new Button(projectsComposite, SWT.CHECK);
		FormData fd_btnShowEstimates = new FormData();
		fd_btnShowEstimates.left = new FormAttachment(btnShowCashiered, 10);
		fd_btnShowEstimates.bottom = new FormAttachment(0, 65);
		fd_btnShowEstimates.top = new FormAttachment(0, 43);
		btnShowEstimates.setLayoutData(fd_btnShowEstimates);
		btnShowEstimates.setText("Show Estimates");
	}
	
	private void partsTab(TabFolder tabFolder) {
		TabItem tbtmParts = new TabItem(tabFolder, SWT.NONE);
		tbtmParts.setText("Parts");

		Composite partsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmParts.setControl(partsComposite);
		FormLayout fl_partsComposite = new FormLayout();
		fl_partsComposite.spacing = 5;
		fl_partsComposite.marginWidth = 5;
		fl_partsComposite.marginHeight = 5;
		partsComposite.setLayout(fl_partsComposite);
		
		TabFolder tabFolderParts = new TabFolder(partsComposite, SWT.NONE);
		FormData fd_tabFolderParts = new FormData();
		fd_tabFolderParts.bottom = new FormAttachment(100);
		fd_tabFolderParts.right = new FormAttachment(100);
		fd_tabFolderParts.top = new FormAttachment(0);
		fd_tabFolderParts.left = new FormAttachment(0);
		tabFolderParts.setLayoutData(fd_tabFolderParts);
		
		// Inventory Tab
		TabItem tbtmInventory = new TabItem(tabFolderParts, SWT.NONE);
		tbtmInventory.setText("Inventory");
		
		Composite inventoryComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmInventory.setControl(inventoryComposite);
		FormLayout fl_inventoryComposite = new FormLayout();
		fl_inventoryComposite.spacing = 5;
		fl_inventoryComposite.marginWidth = 5;
		fl_inventoryComposite.marginHeight = 5;
		inventoryComposite.setLayout(fl_inventoryComposite);
		
		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		
		MyTable partTableInventory = new PartInventoryTable(inventoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_partTableInventory = new FormData();
		fd_partTableInventory.top = new FormAttachment(partSearchTextBox);
		fd_partTableInventory.bottom = new FormAttachment(100);
		fd_partTableInventory.right = new FormAttachment(100);
		fd_partTableInventory.left = new FormAttachment(0);
		partTableInventory.setLayoutData(fd_partTableInventory);
		partTableInventory.setLinesVisible(true);
		partTableInventory.setHeaderVisible(true);
		partTableInventory.addMouseListener(new OpenExistingObjectMouseListener(partTableInventory, shell));
		partTableInventory.addMenuDetectListener(new DeleteEnableListener(partTableInventory));
		
		TableColumn tblclmnPartNumberInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnPartNumberInventory.setWidth(126);
		tblclmnPartNumberInventory.setText("Part Number");
		tblclmnPartNumberInventory.addSelectionListener(new TableColumnSortListener(tblclmnPartNumberInventory));
		
		TableColumn tblclmnDescriptionInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnDescriptionInventory.setWidth(275);
		tblclmnDescriptionInventory.setText("Description");
		tblclmnDescriptionInventory.addSelectionListener(new TableColumnSortListener(tblclmnDescriptionInventory));
		
		TableColumn tblclmnOnHandInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnOnHandInventory.setWidth(100);
		tblclmnOnHandInventory.setText("On Hand");
		tblclmnOnHandInventory.addSelectionListener(new TableColumnSortListener(tblclmnOnHandInventory));
		
		TableColumn tblclmnSupplierInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnSupplierInventory.setWidth(109);
		tblclmnSupplierInventory.setText("Supplier");
		tblclmnSupplierInventory.addSelectionListener(new TableColumnSortListener(tblclmnSupplierInventory));
		
		TableColumn tblclmnCategoryInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnCategoryInventory.setWidth(115);
		tblclmnCategoryInventory.setText("Category");
		tblclmnCategoryInventory.addSelectionListener(new TableColumnSortListener(tblclmnCategoryInventory));
		
		TableColumn tblclmnCostInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnCostInventory.setWidth(100);
		tblclmnCostInventory.setText("Cost");
		tblclmnCostInventory.addSelectionListener(new TableColumnSortListener(tblclmnCostInventory));
		
		TableColumn tblclmnRetailPriceInventory = new TableColumn(partTableInventory, SWT.NONE);
		tblclmnRetailPriceInventory.setWidth(110);
		tblclmnRetailPriceInventory.setText("Retail Price");
		tblclmnRetailPriceInventory.addSelectionListener(new TableColumnSortListener(tblclmnRetailPriceInventory));
		
		// Delete right click menu
		setupRightClickMenu(partTableInventory, "Part");
		
		// Inventory controls		
		Button btnNewPart = new Button(inventoryComposite, SWT.NONE);
		FormData fd_btnNewPart = new FormData();
		fd_btnNewPart.height = 28;
		fd_btnNewPart.width = 112;
		fd_btnNewPart.right = new FormAttachment(100);
		fd_btnNewPart.top = new FormAttachment(0, -1);
		btnNewPart.setLayoutData(fd_btnNewPart);
		btnNewPart.setText("New Part");
		btnNewPart.addMouseListener(new CreateNewObjectListener(partTableInventory, partSearchTextBox, shell));
		
//		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		FormData fd_partSearchTextBox = new FormData();
		fd_partSearchTextBox.right = new FormAttachment(btnNewPart);
		fd_partSearchTextBox.height = 16;
		fd_partSearchTextBox.top = new FormAttachment(0);
		fd_partSearchTextBox.left = new FormAttachment(0);
		partSearchTextBox.setLayoutData(fd_partSearchTextBox);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTableInventory, new Part()));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
		// END Inventory tab
		
		// START Invoice tab
		TabItem tbtmInvoice = new TabItem(tabFolderParts, SWT.NONE);
		tbtmInvoice.setText("Invoice");
		
		Composite invoiceComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmInvoice.setControl(invoiceComposite);
		FormLayout fl_invoiceComposite = new FormLayout();
		fl_invoiceComposite.marginWidth = 5;
		fl_invoiceComposite.marginHeight = 5;
		fl_invoiceComposite.spacing = 5;
		invoiceComposite.setLayout(fl_invoiceComposite);
		
		txtCustomerInvoice = new MyText(invoiceComposite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtCustomerInvoice = new FormData();
		fd_txtCustomerInvoice.height = 96;
		fd_txtCustomerInvoice.right = new FormAttachment(51, -2);
		fd_txtCustomerInvoice.top = new FormAttachment(0);
		fd_txtCustomerInvoice.left = new FormAttachment(0);
		txtCustomerInvoice.setLayoutData(fd_txtCustomerInvoice);
		txtCustomerInvoice.setText("Customer...");
		txtCustomerInvoice.setEditable(false);
		txtCustomerInvoice.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtCustomerInvoice.addModifyListener(new RequiredTextBoxModifyListener(txtCustomerInvoice));
		txtCustomerInvoice.addMouseListener(new CustomerSearchListener(txtCustomerInvoice));
		
		txtInvoiceNotes = new MyText(invoiceComposite, SWT.BORDER | SWT.WRAP);
		FormData fd_txtInvoiceNotes = new FormData();
		fd_txtInvoiceNotes.height = 96;
		fd_txtInvoiceNotes.right = new FormAttachment(100);
		fd_txtInvoiceNotes.left = new FormAttachment(51, 2);
		fd_txtInvoiceNotes.top = new FormAttachment(0);
		txtInvoiceNotes.setLayoutData(fd_txtInvoiceNotes);
		txtInvoiceNotes.setText("Invoice Notes...");
		txtInvoiceNotes.addModifyListener(new InfoTextBoxModifyListener(txtInvoiceNotes));
		txtInvoiceNotes.addFocusListener(new TextBoxFocusListener(txtInvoiceNotes));
		
		Group grpTotals = new Group(invoiceComposite, SWT.NONE);
		FormLayout fl_grpTotals = new FormLayout();
		fl_grpTotals.spacing = 5;
		fl_grpTotals.marginWidth = 5;
		fl_grpTotals.marginHeight = 5;
		grpTotals.setLayout(fl_grpTotals);
		FormData fd_grpTotals = new FormData();
		fd_grpTotals.height = 100;
		fd_grpTotals.bottom = new FormAttachment(100);
		fd_grpTotals.right = new FormAttachment(100);
		grpTotals.setLayoutData(fd_grpTotals);
		grpTotals.setText("Totals");
		
		txtPartsTotalInvoice = new MyText(grpTotals, SWT.BORDER);
		FormData fd_txtPartsTotalInvoice = new FormData();
		fd_txtPartsTotalInvoice.width = 96;
		fd_txtPartsTotalInvoice.top = new FormAttachment(0);
		fd_txtPartsTotalInvoice.right = new FormAttachment(100);
		txtPartsTotalInvoice.setLayoutData(fd_txtPartsTotalInvoice);
		txtPartsTotalInvoice.setEditable(false);
		
		Label lblPartsTotal = new Label(grpTotals, SWT.NONE);
		FormData fd_lblPartsTotal = new FormData();
		fd_lblPartsTotal.top = new FormAttachment(txtPartsTotalInvoice, 5, SWT.TOP);
		fd_lblPartsTotal.right = new FormAttachment(txtPartsTotalInvoice);
		fd_lblPartsTotal.bottom = new FormAttachment(txtPartsTotalInvoice, 0, SWT.BOTTOM);
		lblPartsTotal.setLayoutData(fd_lblPartsTotal);
		lblPartsTotal.setText("Parts Total:");
		
		txtTaxInvoice = new MyText(grpTotals, SWT.BORDER);
		FormData fd_txtTaxInvoice = new FormData();
		fd_txtTaxInvoice.top = new FormAttachment(lblPartsTotal, 2);
		fd_txtTaxInvoice.left = new FormAttachment(txtPartsTotalInvoice, 0, SWT.LEFT);
		fd_txtTaxInvoice.right = new FormAttachment(100);
		txtTaxInvoice.setLayoutData(fd_txtTaxInvoice);
		txtTaxInvoice.setEditable(false);
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		FormData fd_lblTax = new FormData();
		fd_lblTax.top = new FormAttachment(txtTaxInvoice, 5, SWT.TOP);
		fd_lblTax.right = new FormAttachment(txtTaxInvoice);
		fd_lblTax.bottom = new FormAttachment(txtTaxInvoice, 0, SWT.BOTTOM);
		lblTax.setLayoutData(fd_lblTax);
		lblTax.setText("Tax:");
		
		txtFinalTotal = new MyText(grpTotals, SWT.BORDER);
		FormData fd_txtFinalTotal = new FormData();
		fd_txtFinalTotal.left = new FormAttachment(txtPartsTotalInvoice, 0, SWT.LEFT);
		fd_txtFinalTotal.top = new FormAttachment(txtTaxInvoice);
		fd_txtFinalTotal.right = new FormAttachment(100);
		txtFinalTotal.setLayoutData(fd_txtFinalTotal);
		txtFinalTotal.setEditable(false);
		
		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		FormData fd_lblFinalTotal = new FormData();
		fd_lblFinalTotal.top = new FormAttachment(txtFinalTotal, 5, SWT.TOP);
		fd_lblFinalTotal.bottom = new FormAttachment(txtFinalTotal, 0, SWT.BOTTOM);
		fd_lblFinalTotal.right = new FormAttachment(txtFinalTotal);
		lblFinalTotal.setLayoutData(fd_lblFinalTotal);
		lblFinalTotal.setText("Final Total:");
		
		Group grpSelectedPart = new Group(invoiceComposite, SWT.NONE);
		FormLayout fl_grpSelectedPart = new FormLayout();
		fl_grpSelectedPart.marginWidth = 5;
		fl_grpSelectedPart.marginTop = 5;
		fl_grpSelectedPart.marginHeight = 5;
		grpSelectedPart.setLayout(fl_grpSelectedPart);
		FormData fd_grpSelectedPart = new FormData();
		fd_grpSelectedPart.height = 100;
		fd_grpSelectedPart.bottom = new FormAttachment(100);
		fd_grpSelectedPart.right = new FormAttachment(40);
		fd_grpSelectedPart.left = new FormAttachment(0);
		grpSelectedPart.setLayoutData(fd_grpSelectedPart);
		grpSelectedPart.setText("Selected Part");
		
		Label lblNotes = new Label(grpSelectedPart, SWT.NONE);
		FormData fd_lblNotes = new FormData();
		fd_lblNotes.bottom = new FormAttachment(0, 6);
		fd_lblNotes.right = new FormAttachment(0, 42);
		fd_lblNotes.top = new FormAttachment(0, -10);
		fd_lblNotes.left = new FormAttachment(0);
		lblNotes.setLayoutData(fd_lblNotes);
		lblNotes.setLayoutData(fd_lblNotes);
		lblNotes.setText("Notes:");
		
		textSupplierInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		FormData fd_textSupplierInvoice = new FormData();
		fd_textSupplierInvoice.bottom = new FormAttachment(lblNotes, 0, SWT.BOTTOM);
		fd_textSupplierInvoice.top = new FormAttachment(lblNotes, 0, SWT.TOP);
		fd_textSupplierInvoice.right = new FormAttachment(100);
		fd_textSupplierInvoice.left = new FormAttachment(80);
		textSupplierInvoice.setLayoutData(fd_textSupplierInvoice);
		textSupplierInvoice.setEditable(false);
		textSupplierInvoice.setText("");
		
		textCategoryInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		FormData fd_textCategoryInvoice = new FormData();
		fd_textCategoryInvoice.bottom = new FormAttachment(textSupplierInvoice, 22, SWT.BOTTOM);
		fd_textCategoryInvoice.top = new FormAttachment(textSupplierInvoice, 5);
		fd_textCategoryInvoice.right = new FormAttachment(textSupplierInvoice, 0, SWT.RIGHT);
		fd_textCategoryInvoice.left = new FormAttachment(textSupplierInvoice, 0, SWT.LEFT);
		textCategoryInvoice.setLayoutData(fd_textCategoryInvoice);
		textCategoryInvoice.setEditable(false);
		
		Label lblSupplier = new Label(grpSelectedPart, SWT.NONE);
		lblSupplier.setAlignment(SWT.RIGHT);
		FormData fd_lblSupplier = new FormData();
		fd_lblSupplier.bottom = new FormAttachment(lblNotes, 5, SWT.BOTTOM);
		fd_lblSupplier.right = new FormAttachment(textSupplierInvoice, -2);
		fd_lblSupplier.top = new FormAttachment(lblNotes, 0, SWT.TOP);
		lblSupplier.setLayoutData(fd_lblSupplier);
		lblSupplier.setText("Supplier:");
		
		Label lblCategory = new Label(grpSelectedPart, SWT.NONE);
		FormData fd_lblCategory = new FormData();
		fd_lblCategory.right = new FormAttachment(textCategoryInvoice);
		fd_lblCategory.bottom = new FormAttachment(lblSupplier, 16, SWT.BOTTOM);
		fd_lblCategory.top = new FormAttachment(lblSupplier);
		lblCategory.setLayoutData(fd_lblCategory);
		lblCategory.setText("Category:");
		
		textPartNotesInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		FormData fd_textPartNotesInvoice = new FormData();
		fd_textPartNotesInvoice.right = new FormAttachment(lblCategory, -20);
		fd_textPartNotesInvoice.top = new FormAttachment(lblNotes);
		fd_textPartNotesInvoice.bottom = new FormAttachment(100);
		fd_textPartNotesInvoice.left = new FormAttachment(0);
		textPartNotesInvoice.setLayoutData(fd_textPartNotesInvoice);
		textPartNotesInvoice.setEditable(false);
		
		Button btnDeleteLineItem = new Button(grpSelectedPart, SWT.NONE);
		FormData fd_btnDeleteLineItem = new FormData();
		fd_btnDeleteLineItem.bottom = new FormAttachment(textPartNotesInvoice, 0, SWT.BOTTOM);
		fd_btnDeleteLineItem.top = new FormAttachment(textCategoryInvoice, 10);
		fd_btnDeleteLineItem.left = new FormAttachment(textPartNotesInvoice, 10);
		fd_btnDeleteLineItem.right = new FormAttachment(100, 5);
		btnDeleteLineItem.setLayoutData(fd_btnDeleteLineItem);
		btnDeleteLineItem.setText("Delete Line Item");
		
		invoicePartsTable = new InvoicePartTable(invoiceComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_invoicePartsTable = new FormData();
		fd_invoicePartsTable.bottom = new FormAttachment(grpSelectedPart);
		fd_invoicePartsTable.top = new FormAttachment(txtInvoiceNotes);
		fd_invoicePartsTable.right = new FormAttachment(100);
		fd_invoicePartsTable.left = new FormAttachment(0);
		invoicePartsTable.setLayoutData(fd_invoicePartsTable);
		invoicePartsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				invoicePartsTable.getItemCount();
				invoicePartsTable.getSelectionIndex();
				if (invoicePartsTable.getSelectionIndex() >= 0 &&
						invoicePartsTable.getSelectionIndex() < invoicePartsTable.getItemCount() &&
							invoicePartsTable.getItem(invoicePartsTable.getSelectionIndex()).getData() != null) {
					Part selectedPart = ((InvoicePart) invoicePartsTable.getItem(invoicePartsTable.getSelectionIndex()).getData()).getPart();
					textCategoryInvoice.setText(selectedPart.getCategory());
					textSupplierInvoice.setText(selectedPart.getSupplier());
					textPartNotesInvoice.setText(selectedPart.getNotes());
				} else {
					textCategoryInvoice.setText("");
					textSupplierInvoice.setText("");
					textPartNotesInvoice.setText("");
				}
			}
		});
		
		List<MyText> invoiceDetailText = new ArrayList<>();
		invoiceDetailText.add(txtPartsTotalInvoice);
		invoiceDetailText.add(txtTaxInvoice);
		invoiceDetailText.add(txtFinalTotal);
		invoiceDetailText.add(textCategoryInvoice);
		invoiceDetailText.add(textSupplierInvoice);
		invoiceDetailText.add(textPartNotesInvoice);
	    invoicePartsTable.addListener(SWT.MouseDown, new InvoicePartTableListener(invoicePartsTable, invoiceDetailText, shell));
	    
		invoicePartsTable.setLinesVisible(true);
		invoicePartsTable.setHeaderVisible(true);
		
		TableColumn tblclmnPartNumberInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnPartNumberInvoice.setWidth(126);
		tblclmnPartNumberInvoice.setText("Part Number");
		
		TableColumn tblclmnDescriptionInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnDescriptionInvoice.setWidth(275);
		tblclmnDescriptionInvoice.setText("Description");
		
		TableColumn tblclmnQuantityInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnQuantityInvoice.setWidth(100);
		tblclmnQuantityInvoice.setText("Quantity");
		
		TableColumn tblclmnOnHandInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnOnHandInvoice.setWidth(100);
		tblclmnOnHandInvoice.setText("On Hand");
		
		TableColumn tblclmnCostInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnCostInvoice.setWidth(100);
		tblclmnCostInvoice.setText("Cost");
		
		TableColumn tblclmnPartPriceInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnPartPriceInvoice.setWidth(110);
		tblclmnPartPriceInvoice.setText("Part Price");
		
		TableColumn tblclmnExtendedPriceInvoice = new TableColumn(invoicePartsTable, SWT.NONE);
		tblclmnExtendedPriceInvoice.setWidth(114);
		tblclmnExtendedPriceInvoice.setText("Extended Price");

		Button btnCancel = new Button(invoiceComposite, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.height = 48;
		fd_btnCancel.width = 128;
		fd_btnCancel.right = new FormAttachment(grpTotals);
		fd_btnCancel.bottom = new FormAttachment(100);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				clearInvoiceGui();
			}
		});
		btnCancel.setText("Cancel");

		Button btnCashier = new Button(invoiceComposite, SWT.NONE);
		FormData fd_btnCashier = new FormData();
		fd_btnCashier.width = 128;
		fd_btnCashier.top = new FormAttachment(grpTotals, 0, SWT.TOP);
		fd_btnCashier.right = new FormAttachment(grpTotals);
		fd_btnCashier.bottom = new FormAttachment(btnCancel);
		btnCashier.setLayoutData(fd_btnCashier);
		btnCashier.addMouseListener(new MouseAdapter() {	// NOTE this needs to be its own class so we can add this listener to this
			@Override											// the RO cashier button, and the AccountPayment button
			public void mouseDown(MouseEvent e) {
				// spawn amount due dialog box
				if (txtCustomerInvoice.getData() == null) {
					MessageBox customerRequiredBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					customerRequiredBox.setText("Notice");
					customerRequiredBox.setMessage("Please select a Customer");
					customerRequiredBox.open();
					return;
				} else if (invoicePartsTable.getItemCount() == 1) {
					MessageBox noPartsBox = new MessageBox(shell, SWT.ICON_INFORMATION);
					noPartsBox.setText("Notice");
					noPartsBox.setMessage("Enter a part to invoice");
					noPartsBox.open();
					return;
				}
				AmountDueDialog amountDueDialog = new AmountDueDialog(shell, getStyle());
				AmountDueResponse invoiceResult = amountDueDialog.open(txtFinalTotal);
				
				if (invoiceResult.getResult() != Result.CANCELED) {
					Invoice cashieredInvoice = new Invoice();
					cashieredInvoice.setCustomerId(((Customer) txtCustomerInvoice.getData()).getCustomerId());
					cashieredInvoice.setCustomerData(txtCustomerInvoice.getText());
					cashieredInvoice.setLocationId(Application.getSettings().getCurrentLocation());
					cashieredInvoice.setNotes(txtInvoiceNotes.getText());
					cashieredInvoice.setTax(new BigDecimal(txtTaxInvoice.getText().replace("$", "")));
					cashieredInvoice.setCashiereDateTime(Timestamp.from(Instant.now()));
					cashieredInvoice.setCashiered(true);
					
					cashieredInvoice.setTableLineItems(invoicePartsTable.getItems());	// probably not needed anymore
					cashieredInvoice.setPartsFromTableItems(invoicePartsTable.getItems());
					
//					cashieredInvoice.setCashierResult(invoiceResult);
					
					// send invoice obj to DbServices
					// TODO i think this can be done with the Invoice object now
					DbServices.saveObject(cashieredInvoice);
					
					if (invoiceResult.getResult() == Result.CHARGED) {
						// NOTE we might do this by first getting the Account, then, via new Account methods I will hafta make
							// simply pass it the invoice (or the cashiered Project/RO in that case)
							// the Account object will then take the responsibility of creating the new transaciton
							// and adding it to its current List<AccountTransactions> of transactions
							// then either it will save it self, or after we pass it the Invoice/Project/RO
							// we will call Account.save() here, and it will be saved
							// then DbServices will see that it's an Account object and save the Transactions separately
							// sorta like we do InvoiceParts/InvoiceLineItems now
								// though in this case we'll need to figure out some way to save only the latest transaction
								// not every single one that is in the List<AccountTransaction>
							// this will probably be cleaner overall, but I think right now I'm
							// more focused on getting this *working*

							// hopefully this refactor won't be too expensive in the future
						
							// i think in this case we really need to save the ammountCharged onto the Invoice
								// like I had tentativly postulated at one point
						cashieredInvoice.setAmountCharged(invoiceResult.getAmountCharged());
						
						Account accountChargedTo = new Account().searchByPk(cashieredInvoice.getCustomerId());
						accountChargedTo.chargeNewTransaction(cashieredInvoice);

						accountChargedTo.save();		// since we've already saved the new Transaction above, this is really only saving the new balance on the Account
					}
					
					clearInvoiceGui();
				}
			}
		});
		btnCashier.setText("Cashier");
		
		btnDeleteLineItem.addMouseListener(new InvoicePartDeleteLineItemListener(
				invoicePartsTable, txtPartsTotalInvoice, txtTaxInvoice, txtFinalTotal));
		
		Button btnSearchForInvoice = new Button(invoiceComposite, SWT.NONE);
		FormData fd_btnSearchForInvoice = new FormData();
		fd_btnSearchForInvoice.width = 128;
		fd_btnSearchForInvoice.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		fd_btnSearchForInvoice.right = new FormAttachment(btnCancel);
		fd_btnSearchForInvoice.bottom = new FormAttachment(100);
		btnSearchForInvoice.setLayoutData(fd_btnSearchForInvoice);
		btnSearchForInvoice.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// open invoice search dialog
				InvoiceSearchDialog invoiceSearchDialog = new InvoiceSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
				@SuppressWarnings("unused")
				Invoice invoiceToEdit = (Invoice) invoiceSearchDialog.open();		// TODO check all casts
				// populate all invoice tab fields with data from invoice to edit
			}
		});
		btnSearchForInvoice.setText("Invoice Search");
		// END Invoice Tab
		
		// START Order tab
		TabItem tbtmOrder = new TabItem(tabFolderParts, SWT.NONE);
		tbtmOrder.setText("Order");
		
		Composite orderComposite = new Composite(tabFolderParts, SWT.NONE);
		tbtmOrder.setControl(orderComposite);
		orderComposite.setLayout(new FormLayout());
		// END Order Tab
	}
	
	protected void clearInvoiceGui() {
		txtCustomerInvoice.setData(null);
		txtCustomerInvoice.setText("Customer...");
		txtCustomerInvoice.setBackground(SWTResourceManager.getColor(255, 102, 102));
		txtInvoiceNotes.setText("Invoice Notes...");
		txtPartsTotalInvoice.setText("0.00");
		txtTaxInvoice.setText("0.00");
		txtFinalTotal.setText("0.00");
		textCategoryInvoice.setText("");
		textSupplierInvoice.setText("");
		textPartNotesInvoice.setText("");
		invoicePartsTable.removeAll();
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(invoicePartsTable, SWT.NONE);	// so we can add parts		
	}

	private void customersTab(TabFolder tabFolder) {
		TabItem tbtmCustomers = new TabItem(tabFolder, SWT.NONE);
		tbtmCustomers.setText("Customers/Accounts");
		
		Composite customerComposite = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerComposite);
		FormLayout fl_customerComposite = new FormLayout();
		fl_customerComposite.spacing = 5;
		fl_customerComposite.marginWidth = 5;
		fl_customerComposite.marginHeight = 5;
		customerComposite.setLayout(fl_customerComposite);
		
		TabFolder tabFolderCustomer = new TabFolder(customerComposite, SWT.NONE);
		FormData fd_tabFolderCustomer = new FormData();
		fd_tabFolderCustomer.bottom = new FormAttachment(100);
		fd_tabFolderCustomer.right = new FormAttachment(100);
		fd_tabFolderCustomer.top = new FormAttachment(0);
		fd_tabFolderCustomer.left = new FormAttachment(0);
		tabFolderCustomer.setLayoutData(fd_tabFolderCustomer);
		FormData fd_tabFolderParts = new FormData();
		fd_tabFolderParts.bottom = new FormAttachment(100);
		fd_tabFolderParts.right = new FormAttachment(100);
		fd_tabFolderParts.top = new FormAttachment(0);
		fd_tabFolderParts.left = new FormAttachment(0);
		tabFolderCustomer.setLayoutData(fd_tabFolderParts);

		// Customer Detail Tab
		TabItem tbtmCustomerDetail = new TabItem(tabFolderCustomer, SWT.NONE);
		tbtmCustomerDetail.setText("Customers");
		
		Composite customerDetailComposite = new Composite(tabFolderCustomer, SWT.NONE);
		tbtmCustomerDetail.setControl(customerDetailComposite);
		FormLayout fl_customerDetailComposite = new FormLayout();
		fl_customerDetailComposite.spacing = 5;
		fl_customerDetailComposite.marginWidth = 5;
		fl_customerDetailComposite.marginHeight = 5;
		customerDetailComposite.setLayout(fl_customerDetailComposite);

		MyText customerSearchTextBox = new MyText(customerDetailComposite, SWT.BORDER);
		
		// Table for Customer search results
		MyTable customerTable = new CustomerTable(customerDetailComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_customerTable = new FormData();
		fd_customerTable.top = new FormAttachment(customerSearchTextBox);
		fd_customerTable.bottom = new FormAttachment(100);
		fd_customerTable.right = new FormAttachment(100);
		fd_customerTable.left = new FormAttachment(0);
		customerTable.setLayoutData(fd_customerTable);
		customerTable.setHeaderVisible(true);
		customerTable.setLinesVisible(true);
		customerTable.addMouseListener(new OpenExistingObjectMouseListener(customerTable, shell));
		customerTable.addMenuDetectListener(new DeleteEnableListener(customerTable));

		TableColumn tblclmnFirstNameCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnFirstNameCustomer.setText("First Name");
		tblclmnFirstNameCustomer.setWidth(100);
		tblclmnFirstNameCustomer.addSelectionListener(new TableColumnSortListener(tblclmnFirstNameCustomer));

		TableColumn tblclmnLastNameCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnLastNameCustomer.setText("Last Name");
		tblclmnLastNameCustomer.setWidth(100);
		tblclmnLastNameCustomer.addSelectionListener(new TableColumnSortListener(tblclmnLastNameCustomer));

		TableColumn tblclmnAddressCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnAddressCustomer.setText("Address");
		tblclmnAddressCustomer.setWidth(171);
		tblclmnAddressCustomer.addSelectionListener(new TableColumnSortListener(tblclmnAddressCustomer));
		

		TableColumn tblclmnCityCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCityCustomer.setText("City");
		tblclmnCityCustomer.setWidth(100);
		tblclmnCityCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCityCustomer));

		TableColumn tblclmnStateCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnStateCustomer.setText("State");
		tblclmnStateCustomer.setWidth(59);
		tblclmnStateCustomer.addSelectionListener(new TableColumnSortListener(tblclmnStateCustomer));

		TableColumn tblclmnZipCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnZipCustomer.setText("Zip");
		tblclmnZipCustomer.setWidth(79);
		tblclmnZipCustomer.addSelectionListener(new TableColumnSortListener(tblclmnZipCustomer));

		TableColumn tblclmnHomePhoneCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhoneCustomer.setText("Home Phone");
		tblclmnHomePhoneCustomer.setWidth(131);
		tblclmnHomePhoneCustomer.addSelectionListener(new TableColumnSortListener(tblclmnHomePhoneCustomer));

		TableColumn tblclmnCellPhoneCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCellPhoneCustomer.setText("Cell Phone");
		tblclmnCellPhoneCustomer.setWidth(100);
		tblclmnCellPhoneCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCellPhoneCustomer));

		TableColumn tblclmnEmailCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnEmailCustomer.setText("E-mail");
		tblclmnEmailCustomer.setWidth(100);
		tblclmnEmailCustomer.addSelectionListener(new TableColumnSortListener(tblclmnEmailCustomer));

		// Delete right click menu
		setupRightClickMenu(customerTable, "Customer");
		
		// Customer controls
		Button btnNewCustomer = new Button(customerDetailComposite, SWT.NONE);
		FormData fd_btnNewCustomer = new FormData();
		fd_btnNewCustomer.height = 28;
		fd_btnNewCustomer.width = 112;
		fd_btnNewCustomer.right = new FormAttachment(100);
		fd_btnNewCustomer.top = new FormAttachment(0, -1);
		btnNewCustomer.setLayoutData(fd_btnNewCustomer);
		btnNewCustomer.setText("New Customer");
		btnNewCustomer.addMouseListener(new CreateNewObjectListener(customerTable, customerSearchTextBox, shell));
		
//		MyText customerSearchTextBox = new MyText(customerDetailComposite, SWT.BORDER);
		FormData fd_customerSearchTextBox = new FormData();
		fd_customerSearchTextBox.height = 16;
		fd_customerSearchTextBox.right = new FormAttachment(btnNewCustomer);
		fd_customerSearchTextBox.top = new FormAttachment(0);
		fd_customerSearchTextBox.left = new FormAttachment(0);
		customerSearchTextBox.setLayoutData(fd_customerSearchTextBox);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable, new Customer()));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));
		TabItem tbtmProjectSite = new TabItem(tabFolderCustomer, SWT.NONE);
		tbtmProjectSite.setText("Project Sites");
// END customer tab
		
// Job Site tab
		Composite projectSiteComposite = new Composite(tabFolderCustomer, SWT.NONE);
		tbtmProjectSite.setControl(projectSiteComposite);
		FormLayout fl_projectSiteComposite = new FormLayout();
		fl_projectSiteComposite.spacing = 5;
		fl_projectSiteComposite.marginWidth = 5;
		fl_projectSiteComposite.marginHeight = 5;
		projectSiteComposite.setLayout(fl_projectSiteComposite);
		
		MyText projectSiteSearchBox = new MyText(projectSiteComposite, SWT.BORDER);

		// Table for ProjectSite search results
		MyTable projectSiteTable = new ProjectSiteTable(projectSiteComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_projectSiteTable = new FormData();
		fd_projectSiteTable.top = new FormAttachment(projectSiteSearchBox);
		fd_projectSiteTable.bottom = new FormAttachment(100);
		fd_projectSiteTable.right = new FormAttachment(100);
		fd_projectSiteTable.left = new FormAttachment(0);
		projectSiteTable.setLayoutData(fd_projectSiteTable);
		projectSiteTable.setHeaderVisible(true);
		projectSiteTable.setLinesVisible(true);
		projectSiteTable.addMouseListener(new OpenExistingObjectMouseListener(projectSiteTable, shell));
		projectSiteTable.addMenuDetectListener(new DeleteEnableListener(projectSiteTable));

		TableColumn tblclmnOwnerJobSite = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnOwnerJobSite.setText("Owner");
		tblclmnOwnerJobSite.setWidth(200);
		tblclmnOwnerJobSite.addSelectionListener(new TableColumnSortListener(tblclmnOwnerJobSite));

		TableColumn tblclmnMakeUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnMakeUnit.setText("Address");
		tblclmnMakeUnit.setWidth(220);
		tblclmnMakeUnit.addSelectionListener(new TableColumnSortListener(tblclmnMakeUnit));

		TableColumn tblclmnModelUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnModelUnit.setText("City");
		tblclmnModelUnit.setWidth(160);
		tblclmnModelUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelUnit));

		TableColumn tblclmnModelNameUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnModelNameUnit.setText("State");
		tblclmnModelNameUnit.setWidth(60);
		tblclmnModelNameUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelNameUnit));

		TableColumn tblclmnYearUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnYearUnit.setText("Zip");
		tblclmnYearUnit.setWidth(70);
		tblclmnYearUnit.addSelectionListener(new TableColumnSortListener(tblclmnYearUnit));

		TableColumn tblclmnMileageUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnMileageUnit.setText("Notes");
		tblclmnMileageUnit.setWidth(256);
		tblclmnMileageUnit.addSelectionListener(new TableColumnSortListener(tblclmnMileageUnit));

		// Delete right click menu
		setupRightClickMenu(projectSiteTable, "ProjectSite");
		
		Button btnNewProjectSite = new Button(projectSiteComposite, SWT.NONE);
		FormData fd_btnNewProjectSite = new FormData();
		fd_btnNewProjectSite.height = 28;
		fd_btnNewProjectSite.width = 128;
		fd_btnNewProjectSite.top = new FormAttachment(0, -1);
		fd_btnNewProjectSite.right = new FormAttachment(100);
		btnNewProjectSite.setLayoutData(fd_btnNewProjectSite);
		btnNewProjectSite.setText("New Project Site");
		btnNewProjectSite.addMouseListener(new CreateNewObjectListener(projectSiteTable, projectSiteSearchBox, shell));

		// ProjectSite controls
//		MyText jobSiteSearchBox = new MyText(unitsComposite, SWT.BORDER);
		FormData fd_projectSiteSearchBox = new FormData();
		fd_projectSiteSearchBox.right = new FormAttachment(btnNewProjectSite);
		fd_projectSiteSearchBox.left = new FormAttachment(0);
		fd_projectSiteSearchBox.height = 16;
		fd_projectSiteSearchBox.top = new FormAttachment(0);
		projectSiteSearchBox.setLayoutData(fd_projectSiteSearchBox);
		projectSiteSearchBox.setText("Search...");
		projectSiteSearchBox.addModifyListener(new SearchTextBoxListeners(projectSiteSearchBox, projectSiteTable, new ProjectSite()));
		projectSiteSearchBox.addFocusListener(new TextBoxFocusListener(projectSiteSearchBox));
// END Job Site tab

		// Accounts folder
		TabItem tbtmAccountsDetail = new TabItem(tabFolderCustomer, SWT.NONE);
		tbtmAccountsDetail.setText("Accounts");

		Composite accountsDetailComposite = new Composite(tabFolderCustomer, SWT.NONE);
		tbtmAccountsDetail.setControl(accountsDetailComposite);
		FormLayout fl_accountsDetailComposite = new FormLayout();
		fl_accountsDetailComposite.spacing = 5;
		fl_accountsDetailComposite.marginWidth = 5;
		fl_accountsDetailComposite.marginHeight = 5;
		accountsDetailComposite.setLayout(fl_accountsDetailComposite);
		
		Button btnNewAccountPayment = new Button(accountsDetailComposite, SWT.NONE);
		
		MyText accountSearchTextBox = new MyText(accountsDetailComposite, SWT.BORDER);
		
		AccountTable accountTable = new AccountTable(accountsDetailComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_accountTable = new FormData();
		fd_accountTable.top = new FormAttachment(accountSearchTextBox);
		fd_accountTable.bottom = new FormAttachment(90);
		fd_accountTable.right = new FormAttachment(100);
		fd_accountTable.left = new FormAttachment(0);
		accountTable.setLayoutData(fd_accountTable);
		accountTable.setHeaderVisible(true);
		accountTable.setLinesVisible(true);
		accountTable.addMouseListener(new OpenExistingObjectMouseListener(accountTable, shell));
		accountTable.addMenuDetectListener(new DeleteEnableListener(accountTable));
		accountTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (accountTable.getSelectionIndex() >= 0 && accountTable.getSelectionIndex() < accountTable.getItemCount()
						&& accountTable.getItem(accountTable.getSelectionIndex()).getData() != null
						&& (!((Account) accountTable.getItem(accountTable.getSelectionIndex()).getData())
							.getBalance().equals(BigDecimal.valueOf(0.00).setScale(2)))) {
					btnNewAccountPayment.setEnabled(true);
				} else {
					btnNewAccountPayment.setEnabled(false);
				}
			}
		});
		
		TableColumn tblclmnAccountNumber = new TableColumn(accountTable, SWT.NONE);
		tblclmnAccountNumber.setWidth(94);
		tblclmnAccountNumber.setText("Account #");
		tblclmnAccountNumber.addSelectionListener(new TableColumnSortListener(tblclmnAccountNumber));

		
		
		TableColumn tblclmnAccountHolderName = new TableColumn(accountTable, SWT.NONE);
		tblclmnAccountHolderName.setWidth(165);
		tblclmnAccountHolderName.setText("Account Holder");
		tblclmnAccountHolderName.addSelectionListener(new TableColumnSortListener(tblclmnAccountHolderName));
		
		TableColumn tblclmnLastCharge = new TableColumn(accountTable, SWT.NONE);
		tblclmnLastCharge.setWidth(189);
		tblclmnLastCharge.setText("Account Name");
		tblclmnLastCharge.addSelectionListener(new TableColumnSortListener(tblclmnLastCharge));
		
		TableColumn tblclmnLastPayment = new TableColumn(accountTable, SWT.NONE);
		tblclmnLastPayment.setWidth(214);
		tblclmnLastPayment.setText("Last Charge");
		tblclmnLastPayment.addSelectionListener(new TableColumnSortListener(tblclmnLastPayment));
		
		TableColumn tblclmnBalance = new TableColumn(accountTable, SWT.NONE);
		tblclmnBalance.setWidth(220);
		tblclmnBalance.setText("Last Payment");
		tblclmnBalance.addSelectionListener(new TableColumnSortListener(tblclmnBalance));
		
		TableColumn tblclmnHomePhoneCustomer_1 = new TableColumn(accountTable, SWT.NONE);
		tblclmnHomePhoneCustomer_1.setWidth(110);
		tblclmnHomePhoneCustomer_1.setText("Balance");
		tblclmnHomePhoneCustomer_1.addSelectionListener(new TableColumnSortListener(tblclmnHomePhoneCustomer_1));
		
		// Delete right click menu
//		setupRightClickMenu(accountTable, "Account");
		
		Button btnNewAccount = new Button(accountsDetailComposite, SWT.NONE);
		FormData fd_btnNewAccount = new FormData();
		fd_btnNewAccount.height = 28;
		fd_btnNewAccount.width = 112;
		fd_btnNewAccount.top = new FormAttachment(0, -1);
		fd_btnNewAccount.right = new FormAttachment(100);
		btnNewAccount.setLayoutData(fd_btnNewAccount);
		btnNewAccount.setText("New Account");
		btnNewAccount.addMouseListener(new CreateNewObjectListener(accountTable, accountSearchTextBox, shell));

		FormData fd_accountSearchTextBox = new FormData();
		fd_accountSearchTextBox.height = 16;
		fd_accountSearchTextBox.right = new FormAttachment(btnNewAccount);
		fd_accountSearchTextBox.top = new FormAttachment(0);
		fd_accountSearchTextBox.left = new FormAttachment(0);
		accountSearchTextBox.setLayoutData(fd_accountSearchTextBox);
		accountSearchTextBox.setText("Search...");
		accountSearchTextBox.addModifyListener(new SearchTextBoxListeners(accountSearchTextBox, accountTable, new Account()));
		accountSearchTextBox.addFocusListener(new TextBoxFocusListener(accountSearchTextBox));
		
//		btnNewAccountPayment = new Button(accountsDetailComposite, SWT.NONE);
		FormData fd_btnNewAccountPayment = new FormData();
		fd_btnNewAccountPayment.top = new FormAttachment(accountTable);
		fd_btnNewAccountPayment.bottom = new FormAttachment(100);
		fd_btnNewAccountPayment.right = new FormAttachment(accountTable, 0, SWT.RIGHT);
		btnNewAccountPayment.setLayoutData(fd_btnNewAccountPayment);
		btnNewAccountPayment.addMouseListener(new MouseAdapter() {	// NOTE this needs to be its own class so we can add this listener to this
			@Override											// the RO cashier button, and the AccountPayment button
			public void mouseDown(MouseEvent e) {
				// spawn amount due dialog box
//				if (txtCustomerInvoice.getData() == null) {	// Invoice Cashier button
//					MessageBox customerRequiredBox = new MessageBox(shell, SWT.ICON_INFORMATION);
//					customerRequiredBox.setText("Notice");
//					customerRequiredBox.setMessage("Please select a Customer");
//					customerRequiredBox.open();
//					return;
//				} else if (invoicePartsTable.getItemCount() == 1) {
//					MessageBox noPartsBox = new MessageBox(shell, SWT.ICON_INFORMATION);
//					noPartsBox.setText("Notice");
//					noPartsBox.setMessage("Enter a part to invoice");
//					noPartsBox.open();
//					return;
//				}
				
				// RO Cashier has different conditionals
				
				// Account Payment
				BigDecimal accountBalance = ((Account) accountTable.getItem(accountTable.getSelectionIndex()).getData()).getBalance();
				AmountDueDialog amountDueDialog = new AmountDueDialog(shell, getStyle());
				AmountDueResponse response = amountDueDialog.open(accountBalance);
				
				if (response.getResult() == Result.PAYMENT) {
					// TODO make payment object, kinda link Invoice
						// get Account we're paying and instantiate object
					int currentAccountSelectionIndex = accountTable.getSelectionIndex();
					if (currentAccountSelectionIndex >= 0 && currentAccountSelectionIndex < accountTable.getItemCount()
							&& accountTable.getItem(currentAccountSelectionIndex).getData() != null) {
						Account account = (Account) accountTable.getItem(currentAccountSelectionIndex).getData();
						
						AccountPayment newPayment = new AccountPayment();
						newPayment.setCustomerId(account.getCustomerId());
						newPayment.setCustomerData(account.getAccountHolder());
						long currentLocationId = Application.getSettings().getCurrentLocation();
						long locationId = currentLocationId != 0 ? currentLocationId : new Customer().searchByPk(account.getCustomerId()).getLocationId();
						newPayment.setLocationId(locationId);
						newPayment.setNotes("Account: " + account.getAccountNumber() + "\nPayment");
						newPayment.setTax(BigDecimal.valueOf(0).setScale(2));
						newPayment.setCashiereDateTime(Timestamp.from(Instant.now()));
						newPayment.setCashiered(true);
						newPayment.setAmountCharged(response.getAmountCharged());
						
						// don't bother saving the newPayment
							// actually we might want to
							// it depends on whether or not we want an
							// i think we'll need to, the Transaction has Invoice as a foreign key
						newPayment.save();
						
						account.chargeNewTransaction(newPayment);
						account.save();
						
						// repaint
							// TODO add a method to repaint just 1 item
						accountTable.paint(account);
						accountTable.setSelection(currentAccountSelectionIndex);	// set selection after painting to same index we selected before
																					// this breaks if sort column changes sort order
//						int i = 0;	// go through all items, find where current item has now been placed, set that as selection
//						TableItem[] currentItems = accountTable.getItems();
//						while (i < currentItems.length) {
//							if (currentItems[i].getData().equals(account)) {
//								break;
//							}
//						}
						
					}
						// thisAccount.chargeTransaction(thisPayment)
						// thisAccount.save()
				} else if (response.getResult() != Result.CANCELED) {
					Invoice cashieredInvoice = new Invoice();
					cashieredInvoice.setCustomerId(((Customer) txtCustomerInvoice.getData()).getCustomerId());
					cashieredInvoice.setCustomerData(txtCustomerInvoice.getText());
					cashieredInvoice.setLocationId(Application.getSettings().getCurrentLocation());
					cashieredInvoice.setNotes(txtInvoiceNotes.getText());
					cashieredInvoice.setTax(new BigDecimal(txtTaxInvoice.getText().replace("$", "")));
					cashieredInvoice.setCashiereDateTime(Timestamp.from(Instant.now()));
					cashieredInvoice.setCashiered(true);
					
					cashieredInvoice.setTableLineItems(invoicePartsTable.getItems());	// probably not needed anymore
					cashieredInvoice.setPartsFromTableItems(invoicePartsTable.getItems());
					
//					cashieredInvoice.setCashierResult(invoiceResult);
					
					// send invoice obj to DbServices
					// TODO i think this can be done with the Invoice object now
					DbServices.saveObject(cashieredInvoice);
					
					if (response.getResult() == Result.CHARGED) {
						// NOTE we might do this by first getting the Account, then, via new Account methods I will hafta make
							// simply pass it the invoice (or the cashiered Project/RO in that case)
							// the Account object will then take the responsibility of creating the new transaciton
							// and adding it to its current List<AccountTransactions> of transactions
							// then either it will save it self, or after we pass it the Invoice/Project/RO
							// we will call Account.save() here, and it will be saved
							// then DbServices will see that it's an Account object and save the Transactions separately
							// sorta like we do InvoiceParts/InvoiceLineItems now
								// though in this case we'll need to figure out some way to save only the latest transaction
								// not every single one that is in the List<AccountTransaction>
							// this will probably be cleaner overall, but I think right now I'm
							// more focused on getting this *working*

							// hopefully this refactor won't be too expensive in the future
						
							// i think in this case we really need to save the ammountCharged onto the Invoice
								// like I had tentativly postulated at one point
						cashieredInvoice.setAmountCharged(BigDecimal.valueOf(Long.parseLong(txtFinalTotal.getText())));
						
						Account accountChargedTo = new Account().searchByPk(cashieredInvoice.getCustomerId());
						accountChargedTo.chargeNewTransaction(cashieredInvoice);

						accountChargedTo.save();		// since we've already saved the new Transaction above, this is really only saving the new balance on the Account
					}
					
					clearInvoiceGui();
				}
			}
		});
		btnNewAccountPayment.setText("New Payment");
		btnNewAccountPayment.setEnabled(false);		// don't enable until we select an account
	}
	
	private void reportsTab(TabFolder tabFolder) {
		TabItem tbtmReports = new TabItem(tabFolder, SWT.NONE);
		tbtmReports.setText("Reports");

		Composite reportsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmReports.setControl(reportsComposite);

		Label lblNotImplimentedYet = new Label(reportsComposite, SWT.NONE);
		lblNotImplimentedYet.setText("Not Implimented Yet...");
		lblNotImplimentedYet.setBounds(10, 10, 147, 20);
	}
	
	private void settingsTab(TabFolder tabFolder) {
		TabItem tbtmSettings = new TabItem(tabFolder, SWT.NONE);
		tbtmSettings.setText("Settings");
		
		Composite settingsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmSettings.setControl(settingsComposite);
		FormLayout fl_settingsComposite = new FormLayout();
		fl_settingsComposite.spacing = 5;
		fl_settingsComposite.marginWidth = 5;
		fl_settingsComposite.marginHeight = 5;
		settingsComposite.setLayout(fl_settingsComposite);
		
		Group grpResetConfirmDialog = new Group(settingsComposite, SWT.NONE);
		FormData fd_grpResetConfirmDialog = new FormData();
		fd_grpResetConfirmDialog.top = new FormAttachment(0);
		fd_grpResetConfirmDialog.left = new FormAttachment(0);
		grpResetConfirmDialog.setLayoutData(fd_grpResetConfirmDialog);
		grpResetConfirmDialog.setLayout(new FormLayout());
		
		Button btnResetConfirmDialog = new Button(grpResetConfirmDialog, SWT.NONE);
		FormData fd_btnResetConfirmDialog = new FormData();
		fd_btnResetConfirmDialog.bottom = new FormAttachment(0, 42);
		fd_btnResetConfirmDialog.right = new FormAttachment(0, 265);
		fd_btnResetConfirmDialog.top = new FormAttachment(0, 12);
		fd_btnResetConfirmDialog.left = new FormAttachment(0, 5);
		btnResetConfirmDialog.setLayoutData(fd_btnResetConfirmDialog);
		btnResetConfirmDialog.setText("Reset Confirmation Dialog Settings");
		btnResetConfirmDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Application.getSettings().setSkipCancelConfirm(false);
				Application.getSettings().setSkipCloseConfirm(false);
			}
		});
		
		Label lblResetConfirmDialog = new Label(grpResetConfirmDialog, SWT.WRAP);
		FormData fd_lblResetConfirmDialog = new FormData();
		fd_lblResetConfirmDialog.bottom = new FormAttachment(0, 90);
		fd_lblResetConfirmDialog.right = new FormAttachment(0, 315);
		fd_lblResetConfirmDialog.top = new FormAttachment(0, 48);
		fd_lblResetConfirmDialog.left = new FormAttachment(0, 5);
		lblResetConfirmDialog.setLayoutData(fd_lblResetConfirmDialog);
		lblResetConfirmDialog.setText("This will re-enable the Repair Order Close and Cancel Changes confirmation dialog boxes.");
		
		Group grpCurrentLocation = new Group(settingsComposite, SWT.NONE);
		FormLayout fl_grpCurrentLocation = new FormLayout();
		fl_grpCurrentLocation.spacing = 5;
		fl_grpCurrentLocation.marginWidth = 5;
		fl_grpCurrentLocation.marginHeight = 5;
		grpCurrentLocation.setLayout(fl_grpCurrentLocation);
		FormData fd_grpCurrentLocation = new FormData();
		fd_grpCurrentLocation.top = new FormAttachment(grpResetConfirmDialog);
		grpCurrentLocation.setLayoutData(fd_grpCurrentLocation);
		
		StoreLocationCombo comboCurrentLocation = new StoreLocationCombo(new StoreLocation().getAllStoreLocations(), grpCurrentLocation, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboCurrentLocation.select(Application.getSettings().getCurrentLocation());		// TODO decide how to make sure currentSelection always == locationID
		FormData fd_comboCurrentLocation = new FormData();
		fd_comboCurrentLocation.height = 24;
		fd_comboCurrentLocation.width = 256;
		fd_comboCurrentLocation.top = new FormAttachment(0, 5);
		fd_comboCurrentLocation.left = new FormAttachment(0, 5);
		comboCurrentLocation.setLayoutData(fd_comboCurrentLocation);
		
		comboCurrentLocation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int newLocationSelection = comboCurrentLocation.getSelectedLocationId();
				Application.getSettings().setCurrentLocation(newLocationSelection);		// TODO make sure we save this to settings on close
				Application.getSettings().save();
			}
		});
		
	}
	
	private void setupRightClickMenu(MyTable table, String deleteString) {
		Menu popupMenu = new Menu(table);
		MenuItem deletePart = new MenuItem(popupMenu, SWT.NONE);
		deletePart.setText("Delete " + deleteString);
		deletePart.setEnabled(false);
		deletePart.addSelectionListener(new DeleteObjectListener(table));
		table.setMenu(popupMenu);
	}
		
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public static void setDialogAtCenter(Shell dialogShell) {
		Rectangle parentRec = dialogShell.getParent().getBounds();
		Point parentCenter = new Point(parentRec.x + parentRec.width / 2, parentRec.y + parentRec.height / 2);
		dialogShell.setLocation(parentCenter.x - dialogShell.getBounds().width / 2,
				parentCenter.y - dialogShell.getBounds().height / 2);
	}
}
