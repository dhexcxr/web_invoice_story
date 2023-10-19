package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import guiParts.MyText;
import objects.Part;
import guiParts.PartInventoryTable;

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
		shlPartSearch.setSize(946, 410);
		
		Gui.setDialogAtCenter(shlPartSearch);

		// search results table						
		partTableSearchDialog = new PartInventoryTable(shlPartSearch, SWT.BORDER | SWT.FULL_SELECTION);
		partTableSearchDialog.setLinesVisible(true);
		partTableSearchDialog.setHeaderVisible(true);
		partTableSearchDialog.setBounds(10, 42, 908, 282);
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
		btnSelectPart.setText("Select Part");
		btnSelectPart.setBounds(10, 330, 256, 26);
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
		btnCancel.setText("Cancel");
		btnCancel.setBounds(662, 330, 256, 26);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog with no selection
				result = null;
				shlPartSearch.close();
			}
		});

		partSearchTextBox = new MyText(shlPartSearch, SWT.BORDER);
		partSearchTextBox.setText("Search...");
		partSearchTextBox.setBounds(10, 10, 909, 26);
		partSearchTextBox.setFocus();
		partSearchTextBox.addModifyListener(new SearchTextBoxListeners(partSearchTextBox, partTableSearchDialog, new Part()));
		partSearchTextBox.addFocusListener(new TextBoxFocusListener(partSearchTextBox));
	}
}
