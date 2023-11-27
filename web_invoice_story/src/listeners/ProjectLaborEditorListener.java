package listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import gui.ProjectDialog;
import guiParts.JobLaborTable;
import guiParts.JobLaborTableItem;
import guiParts.ProjectJobTable;
import guiParts.ProjectJobTableItem;
import listeners.ProjectLaborTableListener.TabDirection;
import web_invoice_story.Application;

public class ProjectLaborEditorListener implements Listener {

	private JobLaborTable jobLaborTable;
	private JobLaborTableItem selectedTableItem;
	private int selectedColumnIndex;
	private Text editorTxtBox;
	private ProjectJobTable tableJobsRepairOrder;
	private int currentJobTableItemIndex;
	private ProjectDialog projectDialog;
	private ProjectLaborTableListener projectLaborTableListener;


	private static final String ONLY_DECIMALS = "[^0-9.]";		// find a better name


	public ProjectLaborEditorListener(JobLaborTable jobLaborTable, int selectedTableItemIndex, int selectedColumnIndex,
			Text editorTxtBox, ProjectJobTable tableJobsRepairOrder, int currentJobTableItemIndex, ProjectDialog projectDialog) {
		this.jobLaborTable = jobLaborTable;
		this.selectedTableItem = (JobLaborTableItem) jobLaborTable.getItem(selectedTableItemIndex);
		this.selectedColumnIndex = selectedColumnIndex;
		this.editorTxtBox = editorTxtBox;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.currentJobTableItemIndex = currentJobTableItemIndex;
		this.projectDialog = projectDialog;
		
		this.projectLaborTableListener = (ProjectLaborTableListener) jobLaborTable.getListeners(SWT.MouseDown)[0];
	}

	// TODO think about making a table listener SuperClass that implements the shared handleEvent() code, the tabbed() function, TabDirection enum, etc
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.FocusOut) {
			setColumnData();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_ESCAPE) {
			editorTxtBox.dispose();
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_TAB_NEXT) {
			setColumnData();
			projectLaborTableListener.tabbed(selectedColumnIndex, TabDirection.FORWARD);
		} else if (event.type == SWT.Traverse && event.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			setColumnData();
			projectLaborTableListener.tabbed(selectedColumnIndex, TabDirection.BACKWARD);
		} else if (event.type == SWT.Traverse) {
			setColumnData();
		}
//		tableJobsRepairOrder.notifyListeners(SWT.BUTTON4, new Event());		// save Parts and Labor
		projectDialog.saveJob();
		setTotalLabor();
	}
	
	private void setColumnData() {
		if (selectedColumnIndex == JobLaborTable.TECHNICIAN_COLUMN || selectedColumnIndex == JobLaborTable.DESCRIPTION_COLUMN) {
			selectedTableItem.setText(selectedColumnIndex, editorTxtBox.getText());
		} else if (selectedColumnIndex == JobLaborTable.HOURS_COLUMN || selectedColumnIndex == JobLaborTable.RATE_COLUMN) {
																			 // TODO fix this,   v   , probably needs to be ONLY_DECIMALS
			selectedTableItem.setText(selectedColumnIndex, editorTxtBox.getText().replaceAll("[^.0-9]", "").equals("") ? "0" :
				editorTxtBox.getText().replaceAll(ONLY_DECIMALS, ""));
			calculateJobTotal();
		}
		selectedTableItem.setData(selectedTableItem.getText(JobLaborTable.TECHNICIAN_COLUMN), selectedTableItem.getText(JobLaborTable.DESCRIPTION_COLUMN),
										new BigDecimal(selectedTableItem.getText(JobLaborTable.HOURS_COLUMN).replaceAll(ONLY_DECIMALS, "")),
											new BigDecimal(selectedTableItem.getText(JobLaborTable.RATE_COLUMN).replaceAll(ONLY_DECIMALS, "")));
		editorTxtBox.dispose();
	}
	
	private void calculateJobTotal() {
		BigDecimal hours = new BigDecimal(selectedTableItem.getText(JobLaborTable.HOURS_COLUMN));
		BigDecimal rate = new BigDecimal(selectedTableItem.getText(JobLaborTable.RATE_COLUMN));
		selectedTableItem.setText(JobLaborTable.TOTAL_COLUMN, (hours.multiply(rate).setScale(2, RoundingMode.CEILING).toString()));
	}
	
	private void setTotalLabor() {
		ProjectJobTableItem selectedJobTableItem = (ProjectJobTableItem) tableJobsRepairOrder.getItem(currentJobTableItemIndex);
		BigDecimal laborTotal = calculateLaborTotal();
		selectedJobTableItem.setLaborTotal(laborTotal);
		selectedJobTableItem.setText(ProjectJobTable.LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		selectedJobTableItem.setText(ProjectJobTable.JOB_TOTAL_COLUMN, "$" + (laborTotal.add(selectedJobTableItem.getPartTotal()).toString()));
		projectDialog.calcRoTotal();
	}
	
	private BigDecimal calculateLaborTotal() {
//		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = jobLaborTable.getItems();
		for (TableItem item : items) {
			if (item.getText(JobLaborTable.TOTAL_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no data set 
				break;
			}
			Application.getLogger().log(Level.INFO, "labor Total before: {0}", total);
			total = total.add(new BigDecimal(item.getText(JobLaborTable.TOTAL_COLUMN)));
			Application.getLogger().log(Level.INFO, "labor price to BigDecimal: {0}", new BigDecimal(item.getText(JobLaborTable.TOTAL_COLUMN)));
			Application.getLogger().log(Level.INFO, "labor Total after: {0}", total);
		}
		return total;
	}

}
