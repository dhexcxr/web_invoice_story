package listeners;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;

import gui.RepairOrderDialog;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;
import objects.Job;
import guiParts.JobLaborTable;
import guiParts.MyText;
import guiParts.RepairOrderJobTable;
import guiParts.RepairOrderJobTableItem;

public class RepairOrderJobAddListener extends MouseAdapter {
	
	private RepairOrderDialog roDialog;
	private TabFolder tabFolderJobsRepairOrder;
	private RepairOrderJobTable tableJobsRepairOrder;
	private List<MyText> jobDetailsText;
	private InvoicePartTable jobPartsTable;
	private JobLaborTable jobLaborTable;
	
	private static final int JOB_NAME_TEXT = 0;
	private static final int JOB_COMPLAINT_TEXT = 1;
	private static final int JOB_RESOLUTION_TEXT = 2;
	private static final int JOB_RECOMMENDATIONS_TEXT = 3;

	public RepairOrderJobAddListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.jobDetailsText = roDialog.getJobDetailsText();
		this.jobPartsTable = roDialog.getJobPartsTable();
		this.jobLaborTable = roDialog.getJobLaborTable();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		// TODO judging by the comment on 2 lines below I think this notifyListeners might should've been for tableJobsRepairOrder
			// but it seems to be working fine so I don't know that its actually needed
		tabFolderJobsRepairOrder.notifyListeners(SWT.Selection, new Event());		// trigger saving Job details
		// create new Job on Job Table
		RepairOrderJobTableItem job = new RepairOrderJobTableItem(tableJobsRepairOrder, roDialog.getStyle());
		job.setData(new Job());
		tableJobsRepairOrder.setSelection(tableJobsRepairOrder.getItemCount() - 1);
		tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());
		
		
		jobDetailsText.get(JOB_NAME_TEXT).setText("Job Name...");
		jobDetailsText.get(JOB_COMPLAINT_TEXT).setText("Complaints...");
		jobDetailsText.get(JOB_RESOLUTION_TEXT).setText("Resolution...");
		jobDetailsText.get(JOB_RECOMMENDATIONS_TEXT).setText("Reccomendations...");
		jobPartsTable.removeAll();
		@SuppressWarnings("unused")				// this adds a new, empty TableItem at the end of the Invoice Line Items
		TableItem tableItem = new InvoicePartTableItem(jobPartsTable, SWT.NONE);	// so we can add parts
		jobLaborTable.removeAll();
	}

}
