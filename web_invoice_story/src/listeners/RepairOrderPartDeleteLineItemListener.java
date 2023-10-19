package listeners;

import java.math.BigDecimal;

import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.widgets.Text;

import gui.RepairOrderDialog;
import objects.Job;
import objects.JobPart;
import guiParts.MyTable;
import guiParts.RepairOrderJobTable;
import guiParts.RepairOrderJobTableItem;

public class RepairOrderPartDeleteLineItemListener extends InvoicePartDeleteLineItemListener {
	
	private MyTable repairOrderPartTableInvoice;
	private RepairOrderJobTable tableJobsRepairOrder;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, RepairOrderJobTable tableJobsRepairOrder,
				RepairOrderDialog repairOrderDialog) {
		super(repairOrderPartTableInvoice);
		this.repairOrderPartTableInvoice = repairOrderPartTableInvoice;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.repairOrderDialog = repairOrderDialog;
		// TODO Auto-generated constructor stub
	}

//	public RepairOrderPartDeleteLineItemListener(MyTable repairOrderPartTableInvoice, Text txtPartsTotalInvoice,
//			Text txtTaxInvoice, Text txtFinalTotal) {
//		super(repairOrderPartTableInvoice, txtPartsTotalInvoice, txtTaxInvoice, txtFinalTotal);
//		// TODO Auto-generated constructor stub
//	}
	
	@Override
	public void mouseDown(MouseEvent event) {
		if (repairOrderPartTableInvoice.getSelectionIndex() >= 0 && repairOrderPartTableInvoice.getSelectionIndex() < repairOrderPartTableInvoice.getItemCount() &&
				repairOrderPartTableInvoice.getItem(repairOrderPartTableInvoice.getSelectionIndex()).getData() != null) {
			JobPart selectedJobPart = (JobPart) repairOrderPartTableInvoice.getItem(repairOrderPartTableInvoice.getSelectionIndex()).getData();
			repairOrderDialog.addDetailsToDelete(selectedJobPart.getTableName(), selectedJobPart.getJobPartId());
			// remove the selected JobPart from Job.getJobParts
			((Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData()).getJobParts().remove(selectedJobPart);
			super.mouseDown(event);
			totalParts();
		}
	}
	
	private void totalParts() {
		RepairOrderJobTableItem selectedJobTableItem = (RepairOrderJobTableItem) tableJobsRepairOrder.getSelection()[0];
		String textToParse = super.txtPartsTotalInvoice.getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		BigDecimal partTotal = new BigDecimal(textToParse.equals("") ? "0" : textToParse);
		selectedJobTableItem.setPartTotal(partTotal);
		selectedJobTableItem.setText(RepairOrderJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
		selectedJobTableItem.setText(RepairOrderJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		repairOrderDialog.calcRoTotal();
	}

}
