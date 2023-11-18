package gui;

import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import guiParts.MyText;
import objects.Part;
import guiParts.PartInventoryTable;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class PartSearchDialog extends Dialog{

	// TODO make a SearchDialog class, extend PartSearch and CustomerSearch from that class

	protected Object result;		// TODO probably change this to Part, wouldn't need cast for results
	protected Shell shlPartSearch;
	private PartInventoryTable partTableSearchDialog;
	private MyText partSearchTextBox;

	public PartSearchDialog(Shell parent, int style) {
		super(parent, style);
	}

	public Object open() {
		createContents();
		shlPartSearch.open();
		shlPartSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlPartSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(String queryString) {
		createContents();
		
		// populate Search Text Box with selected Owner
		partSearchTextBox.setText(queryString);
		shlPartSearch.open();
		shlPartSearch.layout();
		Display display = getParent().getDisplay();
		while (!shlPartSearch.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shlPartSearch = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shlPartSearch.setText("Part Search");
		
		int width = 946;
		int height = 410;
		shlPartSearch.setSize(width, height);
		shlPartSearch.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {		// TODO i have this in like 4 places, just make it it's own class already
				if (shlPartSearch.getSize().x < new Point(width, height).x
						|| shlPartSearch.getSize().y < new Point(width, height).y) {
					shlPartSearch.setSize(width, height);	// set a minimum shell size
				}
				
			}
		});
		
		Gui.setDialogAtCenter(shlPartSearch);
		shlPartSearch.setLayout(new FormLayout());
		
		partSearchTextBox = new MyText(shlPartSearch, SWT.BORDER);
		FormData fd_partSearchTextBox = new FormData();
		fd_partSearchTextBox.bottom = new FormAttachment(0, 32);
		fd_partSearchTextBox.right = new FormAttachment(100, -10);
		fd_partSearchTextBox.top = new FormAttachment(0, 10);
		fd_partSearchTextBox.left = new FormAttachment(0, 10);
		partSearchTextBox.setLayoutData(fd_partSearchTextBox);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setFocus();
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTableSearchDialog, new Part()));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));

		// search results table						
		partTableSearchDialog = new PartInventoryTable(shlPartSearch, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_partTableSearchDialog = new FormData();
		fd_partTableSearchDialog.bottom = new FormAttachment(100, -64);
		fd_partTableSearchDialog.right = new FormAttachment(100, -10);
		fd_partTableSearchDialog.top = new FormAttachment(partSearchTextBox, 6);
		fd_partTableSearchDialog.left = new FormAttachment(0, 10);
		partTableSearchDialog.setLayoutData(fd_partTableSearchDialog);
		partTableSearchDialog.setLinesVisible(true);
		partTableSearchDialog.setHeaderVisible(true);
		partTableSearchDialog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (partTableSearchDialog.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  partTableSearchDialog.getSelection()[0].getData();
					shlPartSearch.close();
				}
			}
		});

		TableColumn tblclmnPartNumberInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnPartNumberInventory.setWidth(126);
		tblclmnPartNumberInventory.setText("Part Number");
		tblclmnPartNumberInventory.addSelectionListener(new TableColumnSortListener(tblclmnPartNumberInventory));

		TableColumn tblclmnDescriptionInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnDescriptionInventory.setWidth(245);
		tblclmnDescriptionInventory.setText("Description");
		tblclmnDescriptionInventory.addSelectionListener(new TableColumnSortListener(tblclmnDescriptionInventory));

		TableColumn tblclmnOnHandInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnOnHandInventory.setWidth(100);
		tblclmnOnHandInventory.setText("On Hand");
		tblclmnOnHandInventory.addSelectionListener(new TableColumnSortListener(tblclmnOnHandInventory));

		TableColumn tblclmnSupplierInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnSupplierInventory.setWidth(109);
		tblclmnSupplierInventory.setText("Supplier");
		tblclmnSupplierInventory.addSelectionListener(new TableColumnSortListener(tblclmnSupplierInventory));

		TableColumn tblclmnCategoryInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnCategoryInventory.setWidth(115);
		tblclmnCategoryInventory.setText("Category");
		tblclmnCategoryInventory.addSelectionListener(new TableColumnSortListener(tblclmnCategoryInventory));

		TableColumn tblclmnCostInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnCostInventory.setWidth(100);
		tblclmnCostInventory.setText("Cost");
		tblclmnCostInventory.addSelectionListener(new TableColumnSortListener(tblclmnCostInventory));

		TableColumn tblclmnRetailPriceInventory = new TableColumn(partTableSearchDialog, SWT.NONE);
		tblclmnRetailPriceInventory.setWidth(110);
		tblclmnRetailPriceInventory.setText("Retail Price");
		tblclmnRetailPriceInventory.addSelectionListener(new TableColumnSortListener(tblclmnRetailPriceInventory));

		// dialog  controls
		Button btnSelectPart = new Button(shlPartSearch, SWT.NONE);
		FormData fd_btnSelectPart = new FormData();
		fd_btnSelectPart.top = new FormAttachment(partTableSearchDialog, 10);
		fd_btnSelectPart.left = new FormAttachment(0, 10);
		fd_btnSelectPart.bottom = new FormAttachment(100, -10);
		fd_btnSelectPart.right = new FormAttachment(0, 266);
		btnSelectPart.setLayoutData(fd_btnSelectPart);
		btnSelectPart.setText("Select Part");
		btnSelectPart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (partTableSearchDialog.getSelection().length > 0) {
					result =  partTableSearchDialog.getSelection()[0].getData();
					shlPartSearch.close();
				}
			}
		});

		Button btnCancel = new Button(shlPartSearch, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(partTableSearchDialog, 10);
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.left = new FormAttachment(100, -266);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog with no selection
				result = null;
				shlPartSearch.close();
			}
		});
	}
}
