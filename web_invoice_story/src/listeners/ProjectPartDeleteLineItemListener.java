package listeners;

import java.math.BigDecimal;

import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.widgets.Text;

import gui.ProjectDialog;
import objects.Job;
import objects.JobPart;
import guiParts.MyTable;
import guiParts.ProjectJobTable;
import guiParts.ProjectJobTableItem;

public class ProjectPartDeleteLineItemListener extends InvoicePartDeleteLineItemListener {
	
	private MyTable repairOrderPartTableInvoice;
	private ProjectJobTable tableJobsRepairOrder;
	private ProjectDialog projectDialog;

	public ProjectPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, ProjectJobTable tableJobsRepairOrder,
				ProjectDialog projectDialog) {
		super(repairOrderPartTableInvoice);
		this.repairOrderPartTableInvoice = repairOrderPartTableInvoice;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.projectDialog = projectDialog;
		// TODO Auto-generated constructor stub
	}

//	public ProjectPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, Text txtPartsTotalInvoice,
//			Text txtTaxInvoice, Text txtFinalTotal) {
//		super(repairOrderPartTableInvoice, txtPartsTotalInvoice, txtTaxInvoice, txtFinalTotal);
//		// TODO Auto-generated constructor stub
//	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		if (repairOrderPartTableInvoice.getSelectionIndex() >= 0 && repairOrderPartTableInvoice.getSelectionIndex() < repairOrderPartTableInvoice.getItemCount() &&
				repairOrderPartTableInvoice.getItem(repairOrderPartTableInvoice.getSelectionIndex()).getData() != null) {
			JobPart selectedJobPart = (JobPart) repairOrderPartTableInvoice.getItem(repairOrderPartTableInvoice.getSelectionIndex()).getData();
			projectDialog.addDetailsToDelete(selectedJobPart.getTableName(), selectedJobPart.getJobPartId());
			// remove the selected JobPart from Job.getJobParts
			((Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData()).getJobParts().remove(selectedJobPart);
			super.mouseDown(event);
			totalParts();
		}
	}
	
	private void totalParts() {
		ProjectJobTableItem selectedJobTableItem = (ProjectJobTableItem) tableJobsRepairOrder.getSelection()[0];
		String textToParse = super.txtPartsTotalInvoice.getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		BigDecimal partTotal = new BigDecimal(textToParse.equals("") ? "0" : textToParse);
		selectedJobTableItem.setPartTotal(partTotal);
		selectedJobTableItem.setText(ProjectJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
		selectedJobTableItem.setText(ProjectJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		projectDialog.calcRoTotal();
	}

}
