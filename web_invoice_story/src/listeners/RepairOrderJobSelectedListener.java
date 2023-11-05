package listeners;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;

import gui.RepairOrderDialog;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;
import objects.Job;
import objects.JobLabor;
import guiParts.JobLaborTable;
import guiParts.JobLaborTableItem;
import objects.JobPart;
import guiParts.MyText;
import guiParts.RepairOrderJobTable;

public class RepairOrderJobSelectedListener extends SelectionAdapter {
	
	private RepairOrderDialog roDialog;
	private TabFolder tabFolderJobsRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private List<MyText> jobDetailsText;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;
	private List<Button> jobDetailsButtons;
	
	private static final int JOB_NAME_TEXT = 0;
	private static final int JOB_COMPLAINT_TEXT = 1;
	private static final int JOB_RESOLUTION_TEXT = 2;
	private static final int JOB_RECOMMENDATIONS_TEXT = 3;
	private static final int DELETE_LINE_ITEM_BUTTON = 0;
	private static final int ADD_LABOR_BUTTON = 1;
	private static final int DELETE_LABOR_BUTTON = 2;

	public RepairOrderJobSelectedListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.jobDetailsText = roDialog.getJobDetailsText();
		this.jobPartsTable = roDialog.getJobPartsTable();
		this.jobLaborTable = roDialog.getJobLaborTable();
		this.jobDetailsButtons = roDialog.getJobDetailsButtons();
	}
	
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (tableJobsRepairOrder.getSelectionIndex() < 0 || tableJobsRepairOrder.getSelectionIndex() >= tableJobsRepairOrder.getItemCount()) {
			return;
		}
		
		// remove listeners so we don't run them when text boxes are modified
		jobDetailsText.get(JOB_NAME_TEXT).removeModifyListener(roDialog.getJobNameModifiedListener());
		jobDetailsText.get(JOB_NAME_TEXT).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_COMPLAINT_TEXT).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_RESOLUTION_TEXT).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_RECOMMENDATIONS_TEXT).removeModifyListener(roDialog.getJobDetailsModifiedListener());
		
		// enable job tabls widgets
		jobDetailsText.get(JOB_NAME_TEXT).setEnabled(true);
		jobDetailsText.get(JOB_COMPLAINT_TEXT).setEnabled(true);
		jobDetailsText.get(JOB_RESOLUTION_TEXT).setEnabled(true);
		jobDetailsText.get(JOB_RECOMMENDATIONS_TEXT).setEnabled(true);
		jobPartsTable.setEnabled(true);
		jobLaborTable.setEnabled(true);
		
		if (roDialog.getTextCashieredDate().getText().equals("")) {
			// if this RO is not cashiered, also enable Parts and Labor buttons
			jobDetailsButtons.get(DELETE_LINE_ITEM_BUTTON).setEnabled(true);
			jobDetailsButtons.get(ADD_LABOR_BUTTON).setEnabled(true);
			jobDetailsButtons.get(DELETE_LABOR_BUTTON).setEnabled(true);
		}
		
		Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
		if (selectedJob != null && !selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
			copyJobAndData(selectedJob);
		}

		// re-setup the text box ModifyListeners
		jobDetailsText.get(JOB_NAME_TEXT).addModifyListener(roDialog.getJobNameModifiedListener());
		jobDetailsText.get(JOB_NAME_TEXT).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_COMPLAINT_TEXT).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_RESOLUTION_TEXT).addModifyListener(roDialog.getJobDetailsModifiedListener());
		jobDetailsText.get(JOB_RECOMMENDATIONS_TEXT).addModifyListener(roDialog.getJobDetailsModifiedListener());
	}
	
	private void copyJobAndData(Job selectedJob) {
		tabFolderJobsRepairOrder.setData(selectedJob);
		
		jobDetailsText.get(JOB_NAME_TEXT).setText(selectedJob.getJobName().equals("") ? "Job Name..." : selectedJob.getJobName());
		jobDetailsText.get(JOB_COMPLAINT_TEXT).setText(selectedJob.getComplaints().equals("") ? "Complaints..." : selectedJob.getComplaints());
		jobDetailsText.get(JOB_RESOLUTION_TEXT).setText(selectedJob.getResolution().equals("") ? "Resolution..." : selectedJob.getResolution());
		jobDetailsText.get(JOB_RECOMMENDATIONS_TEXT).setText(selectedJob.getReccomendations().equals("") ? "Reccomendations..." : selectedJob.getReccomendations());
		
		setupJobPartsTable(selectedJob);
		setupJobLaborTable(selectedJob);
	}
	
	private void setupJobPartsTable(Job selectedJob) {
		jobPartsTable.removeAll();
		for (JobPart jobPart : selectedJob.getJobParts()) {
			// TODO see how I stored quantity in Invoice, or figure out how to store quantity
			if (jobPart == null) {
				break;
			}
			addPartToPartTableItem(jobPart);
		}
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
	}
	
	private void setupJobLaborTable(Job selectedJob) {
		jobLaborTable.removeAll();
		for (JobLabor labor : selectedJob.getLabor()) {
			if (labor == null) {
				break;
			}
			addLaborToLaborTableItem(labor);
		}
	}
	
	private void addPartToPartTableItem(JobPart jobPart) {
		InvoicePartTableItem jobPartTableItem = new InvoicePartTableItem(jobPartsTable, roDialog.getStyle());
		jobPartTableItem.setText(new String[] {jobPart.getPartNumber(), jobPart.getDescription(), Integer.toString(jobPart.getQuantity()),
				Integer.toString(jobPart.getPart().getOnHand()), jobPart.getPart().getCost().toString(),
				jobPart.getSoldPrice().toString(), jobPart.getSoldPrice().multiply(BigDecimal.valueOf(jobPart.getQuantity())).toString()});
		jobPartTableItem.setData(jobPart);
	}
	
	private void addLaborToLaborTableItem(JobLabor jobLabor) {
		JobLaborTableItem jobLaborTableItem = new JobLaborTableItem(jobLaborTable, roDialog.getStyle());
		jobLaborTableItem.setText(new String[] {jobLabor.getInstaller(), jobLabor.getDescription(), jobLabor.getHours().toString(), jobLabor.getLaborRate().toString(),
							jobLabor.getHours().multiply(jobLabor.getLaborRate()).setScale(2, RoundingMode.CEILING).toString()});
		jobLaborTableItem.setData(jobLabor);
	}

}
