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

import listeners.OpenExistingObjectMouseListener;
import listeners.SearchTextBoxListeners;
import listeners.TableColumnSortListener;
import listeners.TextBoxFocusListener;
import guiParts.MyText;
import objects.ProjectSite;
import guiParts.ProjectSiteTable;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class ProjectSiteSearchDialog extends Dialog {

	protected Object result;
	protected Shell shell;
	private ProjectSiteTable projectSiteTable;
	private MyText projectSiteSearchTextBox;

	public ProjectSiteSearchDialog(Shell parent, int style) {
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
		projectSiteSearchTextBox.setText(owner);
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
		shell.setText("Project Site Search");
//		shell.setSize(1016, 410);
//		shell.setText(getText());
		
		int width = 946;
		int height = 410;
		shell.setSize(width, height);
		shell.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (shell.getSize().x < new Point(width, height).x
						|| shell.getSize().y < new Point(width, height).y) {
					shell.setSize(width, height);	// set a minimum shell size
				}
				
			}
		});
		
		Gui.setDialogAtCenter(shell);
		shell.setLayout(new FormLayout());
		
		projectSiteTable = new ProjectSiteTable(shell, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_projectSiteTable = new FormData();
		fd_projectSiteTable.bottom = new FormAttachment(100, -64);
		fd_projectSiteTable.right = new FormAttachment(100, -10);
		fd_projectSiteTable.left = new FormAttachment(0, 10);
		projectSiteTable.setLayoutData(fd_projectSiteTable);
		projectSiteTable.setHeaderVisible(true);
		projectSiteTable.setLinesVisible(true);
		projectSiteTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				if (projectSiteTable.getSelection().length > 0) {
					// return Customer selected from Customer Search Dialog table on double click
					result =  projectSiteTable.getSelection()[0].getData();
					shell.close();
				}
			}
		});

		TableColumn tblclmnOwnerUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnOwnerUnit.setText("Owner");
		tblclmnOwnerUnit.setWidth(165);

		TableColumn tblclmnMakeUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnMakeUnit.setText("Address");
		tblclmnMakeUnit.setWidth(192);
		tblclmnMakeUnit.addSelectionListener(new TableColumnSortListener(tblclmnMakeUnit));

		TableColumn tblclmnModelUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnModelUnit.setText("City");
		tblclmnModelUnit.setWidth(127);
		tblclmnModelUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelUnit));

		TableColumn tblclmnModelNameUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnModelNameUnit.setText("State");
		tblclmnModelNameUnit.setWidth(64);
		tblclmnModelNameUnit.addSelectionListener(new TableColumnSortListener(tblclmnModelNameUnit));

		TableColumn tblclmnYearUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnYearUnit.setText("Zip");
		tblclmnYearUnit.setWidth(128);
		tblclmnYearUnit.addSelectionListener(new TableColumnSortListener(tblclmnYearUnit));

		TableColumn tblclmnMileageUnit = new TableColumn(projectSiteTable, SWT.NONE);
		tblclmnMileageUnit.setText("Notes");
		tblclmnMileageUnit.setWidth(242);
		tblclmnMileageUnit.addSelectionListener(new TableColumnSortListener(tblclmnMileageUnit));

		projectSiteSearchTextBox = new MyText(shell, SWT.BORDER);
		fd_projectSiteTable.top = new FormAttachment(projectSiteSearchTextBox, 6);
		FormData fd_projectSiteSearchTextBox = new FormData();
		fd_projectSiteSearchTextBox.bottom = new FormAttachment(0, 32);
		fd_projectSiteSearchTextBox.right = new FormAttachment(100, -10);
		fd_projectSiteSearchTextBox.top = new FormAttachment(0, 10);
		fd_projectSiteSearchTextBox.left = new FormAttachment(0, 10);
		projectSiteSearchTextBox.setLayoutData(fd_projectSiteSearchTextBox);
		projectSiteSearchTextBox.setText("Search...");
		projectSiteSearchTextBox.setFocus();
		projectSiteSearchTextBox.addModifyListener(new SearchTextBoxListeners(projectSiteSearchTextBox, projectSiteTable, new ProjectSite()));
		projectSiteSearchTextBox.addFocusListener(new TextBoxFocusListener(projectSiteSearchTextBox));
		
		// dialog  controls
		Button btnSelectUnit = new Button(shell, SWT.NONE);
		FormData fd_btnSelectUnit = new FormData();
		fd_btnSelectUnit.top = new FormAttachment(projectSiteTable, 10);
		fd_btnSelectUnit.bottom = new FormAttachment(100, -10);
		fd_btnSelectUnit.right = new FormAttachment(0, 266);
		fd_btnSelectUnit.left = new FormAttachment(0, 10);
		btnSelectUnit.setLayoutData(fd_btnSelectUnit);
		btnSelectUnit.setText("Select Project Site");
		btnSelectUnit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if (projectSiteTable.getSelection().length > 0) {
					result =  projectSiteTable.getSelection()[0].getData();
					shell.close();
				}
			}
		});
		
		Button btnEditSelectedUnit = new Button(shell, SWT.NONE);
		FormData fd_btnEditSelectedUnit = new FormData();
		fd_btnEditSelectedUnit.top = new FormAttachment(projectSiteTable, 10);
		fd_btnEditSelectedUnit.left = new FormAttachment(btnSelectUnit, 48);
		fd_btnEditSelectedUnit.bottom = new FormAttachment(100, -10);
		fd_btnEditSelectedUnit.right = new FormAttachment(0, 631);
		btnEditSelectedUnit.setLayoutData(fd_btnEditSelectedUnit);
		btnEditSelectedUnit.setText("Edit Selected Project Site");
		btnEditSelectedUnit.addMouseListener(new OpenExistingObjectMouseListener(projectSiteTable, shell));
			
		Button btnCancel = new Button(shell, SWT.NONE);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.top = new FormAttachment(projectSiteTable, 10);
		fd_btnCancel.left = new FormAttachment(100, -266);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				shell.close();
			}
		});

	}
}
