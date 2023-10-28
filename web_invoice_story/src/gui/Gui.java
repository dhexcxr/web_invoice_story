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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
import objects.Customer;
import objects.Invoice;
import objects.InvoicePart;
import objects.RepairOrder;
import objects.Part;
import objects.Unit;
import web_invoice_story.Application;
import web_invoice_story.DbServices;

import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.CustomerTable;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;
import guiParts.MyTable;
import guiParts.MyText;
import guiParts.PartInventoryTable;
import guiParts.RepairOrderTable;
import guiParts.UnitTable;

import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
		unitsTab(tabFolderGui);
		reportsTab(tabFolderGui);
		settingsTab(tabFolderGui);
	}
	
	private void rosTab(TabFolder tabFolder) {
		TabItem tbtmRepairOrders = new TabItem(tabFolder, SWT.NONE);
		tbtmRepairOrders.setText("Repair Orders");

		Composite repairOrdersComposite = new Composite(tabFolder, SWT.NONE);
		tbtmRepairOrders.setControl(repairOrdersComposite);
		repairOrdersComposite.setLayout(new FormLayout());

		// Table for RO search results
		RepairOrderTable roTable = new RepairOrderTable(repairOrdersComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_roTable = new FormData();
		fd_roTable.right = new FormAttachment(100, -10);
		fd_roTable.bottom = new FormAttachment(100, -10);
		fd_roTable.top = new FormAttachment(0, 74);
		fd_roTable.left = new FormAttachment(0, 10);
		roTable.setLayoutData(fd_roTable);
		roTable.setHeaderVisible(true);
		roTable.setLinesVisible(true);
		roTable.addMouseListener(new OpenExistingObjectMouseListener(roTable, shell));
		roTable.addMenuDetectListener(new DeleteEnableListener(roTable));

		TableColumn tblclmnRoRo = new TableColumn(roTable, SWT.NONE);
		tblclmnRoRo.setText("RO #");
		tblclmnRoRo.setWidth(75);
		tblclmnRoRo.addSelectionListener(new TableColumnSortListener(tblclmnRoRo));

		TableColumn tblclmnCustomerRo = new TableColumn(roTable, SWT.NONE);
		tblclmnCustomerRo.setText("Customer");
		tblclmnCustomerRo.setWidth(164);
		tblclmnCustomerRo.addSelectionListener(new TableColumnSortListener(tblclmnCustomerRo));

		TableColumn tblclmnYearRo = new TableColumn(roTable, SWT.NONE);
		tblclmnYearRo.setText("Year");
		tblclmnYearRo.setWidth(65);
		tblclmnYearRo.addSelectionListener(new TableColumnSortListener(tblclmnYearRo));

		TableColumn tblclmnMakeRo = new TableColumn(roTable, SWT.NONE);
		tblclmnMakeRo.setText("Make");
		tblclmnMakeRo.setWidth(109);
		tblclmnMakeRo.addSelectionListener(new TableColumnSortListener(tblclmnMakeRo));

		TableColumn tblclmnModelRo = new TableColumn(roTable, SWT.NONE);
		tblclmnModelRo.setText("Model");
		tblclmnModelRo.setWidth(113);
		tblclmnModelRo.addSelectionListener(new TableColumnSortListener(tblclmnModelRo));

		TableColumn tblclmnVinRo = new TableColumn(roTable, SWT.NONE);
		tblclmnVinRo.setText("VIN");
		tblclmnVinRo.setWidth(222);
		tblclmnVinRo.addSelectionListener(new TableColumnSortListener(tblclmnVinRo));

		TableColumn tblclmnJobsRo = new TableColumn(roTable, SWT.NONE);
		tblclmnJobsRo.setText("Jobs");
		tblclmnJobsRo.setWidth(212);
		tblclmnJobsRo.addSelectionListener(new TableColumnSortListener(tblclmnJobsRo));

		// Delete right click menu
		setupRightClickMenu(roTable, "Repair Order");
		
		// RO controls
		MyText roSearchBox = new MyText(repairOrdersComposite, SWT.BORDER);
		FormData fd_roSearchBox = new FormData();
		fd_roSearchBox.bottom = new FormAttachment(0, 33);
		fd_roSearchBox.left = new FormAttachment(0, 10);
		fd_roSearchBox.top = new FormAttachment(0, 10);
		roSearchBox.setLayoutData(fd_roSearchBox);
		roSearchBox.setText("Search...");
		roSearchBox.addModifyListener(new SearchTextBoxListeners(roSearchBox, roTable, new RepairOrder()));
		roSearchBox.addFocusListener(new TextBoxFocusListener(roSearchBox));

		Button btnNewRepairOrder = new Button(repairOrdersComposite, SWT.NONE);
		fd_roSearchBox.right = new FormAttachment(btnNewRepairOrder, -10);
		FormData fd_btnNewRepairOrder = new FormData();
		fd_btnNewRepairOrder.left = new FormAttachment(100, -161);
		fd_btnNewRepairOrder.bottom = new FormAttachment(0, 33);
		fd_btnNewRepairOrder.right = new FormAttachment(100, -10);
		fd_btnNewRepairOrder.top = new FormAttachment(0, 10);
		btnNewRepairOrder.setLayoutData(fd_btnNewRepairOrder);
		btnNewRepairOrder.setText("New Repair Order");
		btnNewRepairOrder.addMouseListener(new CreateNewObjectListener(roTable, roSearchBox, shell));

		Button btnNewEstimate = new Button(repairOrdersComposite, SWT.NONE);
		FormData fd_btnNewEstimate = new FormData();
		fd_btnNewEstimate.top = new FormAttachment(0, 43);
		fd_btnNewEstimate.bottom = new FormAttachment(0, 65);
		fd_btnNewEstimate.left = new FormAttachment(100, -161);
		fd_btnNewEstimate.right = new FormAttachment(100, -10);
		
		btnNewEstimate.setLayoutData(fd_btnNewEstimate);
		btnNewEstimate.setText("New Estimate");

		Button btnDeleteRepairOrder = new Button(repairOrdersComposite, SWT.NONE);
		FormData fd_btnDeleteRepairOrder = new FormData();
		fd_btnDeleteRepairOrder.bottom = new FormAttachment(0, 65);
		fd_btnDeleteRepairOrder.left = new FormAttachment(100, -322);
		fd_btnDeleteRepairOrder.right = new FormAttachment(100, -171);
		fd_btnDeleteRepairOrder.top = new FormAttachment(0, 43);
		btnDeleteRepairOrder.setLayoutData(fd_btnDeleteRepairOrder);
		btnDeleteRepairOrder.setText("Delete Repair Order");
		btnDeleteRepairOrder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// delete Repair Order
			}
		});

		Button btnShowCashiered = new Button(repairOrdersComposite, SWT.CHECK);
		FormData fd_btnShowCashiered = new FormData();
		fd_btnShowCashiered.bottom = new FormAttachment(0, 65);
		fd_btnShowCashiered.top = new FormAttachment(0, 43);
		fd_btnShowCashiered.left = new FormAttachment(0, 10);
		btnShowCashiered.setLayoutData(fd_btnShowCashiered);
		btnShowCashiered.setText("Show Cashiered");

		Button btnShowEstimates = new Button(repairOrdersComposite, SWT.CHECK);
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
		inventoryComposite.setLayout(new FormLayout());
		
		MyTable partTableInventory = new PartInventoryTable(inventoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_partTableInventory = new FormData();
		fd_partTableInventory.bottom = new FormAttachment(0, 652);
		fd_partTableInventory.right = new FormAttachment(0, 958);
		fd_partTableInventory.top = new FormAttachment(0, 42);
		fd_partTableInventory.left = new FormAttachment(0, 10);
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
		MyText partSearchTextBox = new MyText(inventoryComposite, SWT.BORDER);
		FormData fd_partSearchTextBox = new FormData();
		fd_partSearchTextBox.bottom = new FormAttachment(0, 36);
		fd_partSearchTextBox.right = new FormAttachment(0, 871);
		fd_partSearchTextBox.top = new FormAttachment(0, 10);
		fd_partSearchTextBox.left = new FormAttachment(0, 10);
		partSearchTextBox.setLayoutData(fd_partSearchTextBox);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTableInventory, new Part()));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
		
		Button btnNewPart = new Button(inventoryComposite, SWT.NONE);
		FormData fd_btnNewPart = new FormData();
		fd_btnNewPart.bottom = new FormAttachment(0, 36);
		fd_btnNewPart.right = new FormAttachment(0, 958);
		fd_btnNewPart.top = new FormAttachment(0, 10);
		fd_btnNewPart.left = new FormAttachment(0, 877);
		btnNewPart.setLayoutData(fd_btnNewPart);
		btnNewPart.setText("New Part");
		btnNewPart.addMouseListener(new CreateNewObjectListener(partTableInventory, partSearchTextBox, shell));
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
		FormData fd_grpTotals = new FormData();
		fd_grpTotals.height = 100;
		fd_grpTotals.bottom = new FormAttachment(100);
		fd_grpTotals.right = new FormAttachment(100);
		grpTotals.setLayoutData(fd_grpTotals);
		grpTotals.setText("Totals");
		
		txtPartsTotalInvoice = new MyText(grpTotals, SWT.BORDER);
		txtPartsTotalInvoice.setEditable(false);
		txtPartsTotalInvoice.setBounds(87, 22, 128, 26);
		
		Label lblPartsTotal = new Label(grpTotals, SWT.NONE);
		lblPartsTotal.setBounds(10, 25, 71, 20);
		lblPartsTotal.setText("Parts Total:");
		
		txtTaxInvoice = new MyText(grpTotals, SWT.BORDER);
		txtTaxInvoice.setEditable(false);
		txtTaxInvoice.setBounds(87, 54, 128, 26);
		
		Label lblTax = new Label(grpTotals, SWT.NONE);
		lblTax.setBounds(57, 57, 24, 20);
		lblTax.setText("Tax:");
		
		txtFinalTotal = new MyText(grpTotals, SWT.BORDER);
		txtFinalTotal.setEditable(false);
		txtFinalTotal.setBounds(87, 86, 128, 26);
		
		Label lblFinalTotal = new Label(grpTotals, SWT.NONE);
		lblFinalTotal.setBounds(10, 89, 71, 20);
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
		fd_lblSupplier.left = new FormAttachment(textSupplierInvoice, -50, SWT.LEFT);
		fd_lblSupplier.right = new FormAttachment(textSupplierInvoice, -2);
		fd_lblSupplier.top = new FormAttachment(lblNotes, 0, SWT.TOP);
		lblSupplier.setLayoutData(fd_lblSupplier);
		lblSupplier.setText("Supplier:");
		
		Label lblCategory = new Label(grpSelectedPart, SWT.NONE);
		FormData fd_lblCategory = new FormData();
		fd_lblCategory.left = new FormAttachment(lblSupplier, -5, SWT.LEFT);
		fd_lblCategory.right = new FormAttachment(textCategoryInvoice);
		fd_lblCategory.bottom = new FormAttachment(lblSupplier, 16, SWT.BOTTOM);
		fd_lblCategory.top = new FormAttachment(lblSupplier);
		lblCategory.setLayoutData(fd_lblCategory);
		lblCategory.setText("Category:");
		
		textPartNotesInvoice = new MyText(grpSelectedPart, SWT.BORDER);
		FormData fd_textPartNotesInvoice = new FormData();
		fd_textPartNotesInvoice.right = new FormAttachment(lblCategory, -10);
		fd_textPartNotesInvoice.top = new FormAttachment(lblNotes);
		fd_textPartNotesInvoice.bottom = new FormAttachment(100);
		fd_textPartNotesInvoice.left = new FormAttachment(0);
		textPartNotesInvoice.setLayoutData(fd_textPartNotesInvoice);
		textPartNotesInvoice.setEditable(false);
		
		Button btnDeleteLineItem = new Button(grpSelectedPart, SWT.NONE);
		FormData fd_btnDeleteLineItem = new FormData();
		fd_btnDeleteLineItem.top = new FormAttachment(textCategoryInvoice, 10);
		fd_btnDeleteLineItem.left = new FormAttachment(textPartNotesInvoice, 10);
		fd_btnDeleteLineItem.bottom = new FormAttachment(100, 5);
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
		btnCashier.addMouseListener(new MouseAdapter() {
			@Override
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
				boolean cashiered = amountDueDialog.open(txtFinalTotal.getText());
				
				if (cashiered) {
					Invoice cashieredInvoice = new Invoice();
					cashieredInvoice.setCustomerId(((Customer) txtCustomerInvoice.getData()).getCustomerId());
					cashieredInvoice.setCustomerData(txtCustomerInvoice.getText());
					cashieredInvoice.setNotes(txtInvoiceNotes.getText());
					cashieredInvoice.setTax(new BigDecimal(txtTaxInvoice.getText().replace("$", "")));
					cashieredInvoice.setCashiereDateTime(Timestamp.from(Instant.now()));
					cashieredInvoice.setCashiered(true);
					cashieredInvoice.setTableLineItems(invoicePartsTable.getItems());
					
					// send invoice obj to DbServices
					DbServices.saveObject(cashieredInvoice);
					
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
		tbtmCustomers.setText("Customers");

		Composite customerComposite = new Composite(tabFolder, SWT.NONE);
		tbtmCustomers.setControl(customerComposite);

		// Table for Customer search results
		MyTable customerTable = new CustomerTable(customerComposite, SWT.BORDER | SWT.FULL_SELECTION);
		customerTable.setBounds(10, 42, 976, 663);
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
		tblclmnAddressCustomer.setWidth(100);
		tblclmnAddressCustomer.addSelectionListener(new TableColumnSortListener(tblclmnAddressCustomer));
		

		TableColumn tblclmnCityCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnCityCustomer.setText("City");
		tblclmnCityCustomer.setWidth(100);
		tblclmnCityCustomer.addSelectionListener(new TableColumnSortListener(tblclmnCityCustomer));

		TableColumn tblclmnStateCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnStateCustomer.setText("State");
		tblclmnStateCustomer.setWidth(100);
		tblclmnStateCustomer.addSelectionListener(new TableColumnSortListener(tblclmnStateCustomer));

		TableColumn tblclmnZipCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnZipCustomer.setText("Zip");
		tblclmnZipCustomer.setWidth(100);
		tblclmnZipCustomer.addSelectionListener(new TableColumnSortListener(tblclmnZipCustomer));

		TableColumn tblclmnHomePhoneCustomer = new TableColumn(customerTable, SWT.NONE);
		tblclmnHomePhoneCustomer.setText("Home Phone");
		tblclmnHomePhoneCustomer.setWidth(100);
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
		MyText customerSearchTextBox = new MyText(customerComposite, SWT.BORDER);
		customerSearchTextBox.setText("Search...");
		customerSearchTextBox.setBounds(10, 10, 861, 26);
		customerSearchTextBox.addModifyListener(new SearchTextBoxListeners(customerSearchTextBox, customerTable, new Customer()));
		customerSearchTextBox.addFocusListener(new TextBoxFocusListener(customerSearchTextBox));

		Button btnNewCustomer = new Button(customerComposite, SWT.NONE);
		btnNewCustomer.setText("New Customer");
		btnNewCustomer.setBounds(877, 10, 109, 26);
		btnNewCustomer.addMouseListener(new CreateNewObjectListener(customerTable, customerSearchTextBox, shell));
	}
	
	private void unitsTab(TabFolder tabFolder) {
		TabItem tbtmUnits = new TabItem(tabFolder, SWT.NONE);
		tbtmUnits.setText("Units");

		Composite unitsComposite = new Composite(tabFolder, SWT.NONE);
		tbtmUnits.setControl(unitsComposite);

		// Table for Unit search results
		MyTable unitTable = new UnitTable(unitsComposite, SWT.BORDER | SWT.FULL_SELECTION);
		unitTable.setBounds(10, 42, 976, 663);
		unitTable.setHeaderVisible(true);
		unitTable.setLinesVisible(true);
		unitTable.addMouseListener(new OpenExistingObjectMouseListener(unitTable, shell));
		unitTable.addMenuDetectListener(new DeleteEnableListener(unitTable));

		TableColumn tblclmnOwnerUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnOwnerUnit.setText("Owner");
		tblclmnOwnerUnit.setWidth(165);
		tblclmnOwnerUnit.addSelectionListener(new TableColumnSortListener(tblclmnOwnerUnit));

		TableColumn tblclmnMakeUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMakeUnit.setText("Make");
		tblclmnMakeUnit.setWidth(119);
		tblclmnMakeUnit.addSelectionListener(new TableColumnSortListener(tblclmnMakeUnit));

		TableColumn tblclmnModelUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelUnit.setText("Model");
		tblclmnModelUnit.setWidth(135);
		tblclmnModelUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelUnit));

		TableColumn tblclmnModelNameUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelNameUnit.setText("Model Name");
		tblclmnModelNameUnit.setWidth(135);
		tblclmnModelNameUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelNameUnit));

		TableColumn tblclmnYearUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnYearUnit.setText("Year");
		tblclmnYearUnit.setWidth(50);
		tblclmnYearUnit.addSelectionListener(new TableColumnSortListener(tblclmnYearUnit));

		TableColumn tblclmnMileageUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMileageUnit.setText("Mileage");
		tblclmnMileageUnit.setWidth(85);
		tblclmnMileageUnit.addSelectionListener(new TableColumnSortListener(tblclmnMileageUnit));

		TableColumn tblclmnColorUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnColorUnit.setText("Color");
		tblclmnColorUnit.setWidth(92);
		tblclmnColorUnit.addSelectionListener(new TableColumnSortListener(tblclmnColorUnit));

		TableColumn tblclmnVinUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnVinUnit.setText("VIN");
		tblclmnVinUnit.setWidth(188);
		tblclmnVinUnit.addSelectionListener(new TableColumnSortListener(tblclmnVinUnit));

		// Delete right click menu
		setupRightClickMenu(unitTable, "Unit");
		
		// Unit controls
		MyText unitSearchBox = new MyText(unitsComposite, SWT.BORDER);
		unitSearchBox.setText("Search...");
		unitSearchBox.setBounds(10, 10, 861, 26);
		unitSearchBox.addModifyListener(new SearchTextBoxListeners(unitSearchBox, unitTable, new Unit()));
		unitSearchBox.addFocusListener(new TextBoxFocusListener(unitSearchBox));
		
		Button btnNewUnit = new Button(unitsComposite, SWT.NONE);
		btnNewUnit.setText("New Unit");
		btnNewUnit.setBounds(877, 10, 109, 26);
		btnNewUnit.addMouseListener(new CreateNewObjectListener(unitTable, unitSearchBox, shell));
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
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmSettings.setControl(composite);
		
		Group group = new Group(composite, SWT.NONE);
		group.setBounds(10, 10, 323, 98);
		
		Button btnResetConfirmDialog = new Button(group, SWT.NONE);
		btnResetConfirmDialog.setBounds(10, 17, 260, 30);
		btnResetConfirmDialog.setText("Reset Confirmation Dialog Settings");
		btnResetConfirmDialog.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Application.getSettings().setSkipCancelConfirm(false);
				Application.getSettings().setSkipCloseConfirm(false);
			}
		});
		
		Label lblResetConfirmDialog = new Label(group, SWT.WRAP);
		lblResetConfirmDialog.setBounds(10, 53, 310, 42);
		lblResetConfirmDialog.setText("This will re-enable the Repair Order Close and Cancel Changes confirmation dialog boxes.");
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
