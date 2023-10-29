package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import listeners.OpenExistingObjectMouseListener;
import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import guiParts.MyText;
import objects.ProjectSite;
import guiParts.UnitTable;

public class UnitSearchDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private UnitTable unitTable;
	private MyText unitSearchTextBox;

	public UnitSearchDialog(Shell parent, int style) {
		super(parent, style);
		setText("ProjectSite Search");
	}

	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public Object open(String owner) {
		createContents();
		
		// populate Search Text Box with selected Owner
		unitSearchTextBox.setText(owner);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private void createContents() {
		shell = new Shell(getParent(), SWT.SHELL_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("ProjectSite Search");
		shell.setSize(1016, 410);
		shell.setText(getText());
		
		Gui.setDialogAtCenter(shell);
		
		unitTable = new UnitTable(shell, SWT.BORDER | SWT.FULL_SELECTION);
//		unitTable.addMouseListener(new OpenExistingObjectMouseListener(unitTable, shell));
		unitTable.setBounds(10, 42, 976, 282);
		unitTable.setHeaderVisible(true);
		unitTable.setLinesVisible(true);
		unitTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (unitTable.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  unitTable.getSelection()[0].getData();
					shell.close();
				}
			}
		});

		TableColumn tblclmnOwnerUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnOwnerUnit.setText("Owner");
		tblclmnOwnerUnit.setWidth(165);

		TableColumn tblclmnMakeUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnMakeUnit.setText("Make");
		tblclmnMakeUnit.setWidth(119);
		tblclmnMakeUnit.addSelectionListener(new TableColumnSortListener(tblclmnMakeUnit));

		TableColumn tblclmnModelUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelUnit.setText("Model");
		tblclmnModelUnit.setWidth(138);
		tblclmnModelUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelUnit));

		TableColumn tblclmnModelNameUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnModelNameUnit.setText("Model Name");
		tblclmnModelNameUnit.setWidth(148);
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
		tblclmnColorUnit.setWidth(49);
		tblclmnColorUnit.addSelectionListener(new TableColumnSortListener(tblclmnColorUnit));

		TableColumn tblclmnVinUnit = new TableColumn(unitTable, SWT.NONE);
		tblclmnVinUnit.setText("VIN");
		tblclmnVinUnit.setWidth(215);
		tblclmnVinUnit.addSelectionListener(new TableColumnSortListener(tblclmnVinUnit));

		unitSearchTextBox = new MyText(shell, SWT.BORDER);
		unitSearchTextBox.setText("Search...");
		unitSearchTextBox.setBounds(10, 10, 976, 26);
		unitSearchTextBox.setFocus();
		unitSearchTextBox.addModifyListener(new SearchTextBoxListeners(unitSearchTextBox, unitTable, new ProjectSite()));
		unitSearchTextBox.addFocusListener(new TextBoxFocusListener(unitSearchTextBox));
		
		// dialog  controls
		Button btnSelectUnit = new Button(shell, SWT.NONE);
		btnSelectUnit.setText("Select ProjectSite");
		btnSelectUnit.setBounds(10, 330, 256, 26);
		btnSelectUnit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (unitTable.getSelection().length > 0) {
					result =  unitTable.getSelection()[0].getData();
					shell.close();
				}
			}
		});
		
		Button btnEditSelectedUnit = new Button(shell, SWT.NONE);
		btnEditSelectedUnit.setBounds(342, 330, 317, 26);
		btnEditSelectedUnit.setText("Edit Selected ProjectSite");
		btnEditSelectedUnit.addMouseListener(new OpenExistingObjectMouseListener(unitTable, shell));
			
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setText("Cancel");
		btnCancel.setBounds(730, 330, 256, 26);	
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shell.close();
			}
		});

	}
}
