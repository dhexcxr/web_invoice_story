package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import gui.RepairOrderDialog;
import objects.JobLabor;
import guiParts.JobLaborTable;
import guiParts.JobLaborTableItem;
import guiParts.RepairOrderJobTable;

public class RepairOrderLaborAddListener extends MouseAdapter {
	
	private RepairOrderDialog roDialog;
	private JobLaborTable jobLaborTable;
	private RepairOrderJobTable tableJobsRepairOrder;

	public RepairOrderLaborAddListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.jobLaborTable = roDialog.getJobLaborTable();
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();	
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		TableItem tableItem = new JobLaborTableItem(jobLaborTable, SWT.NONE);
		tableItem.setData(new JobLabor());
		jobLaborTable.setSelection(jobLaborTable.getItemCount() - 1);
		jobLaborTable.notifyListeners(SWT.Selection, new Event());

		jobLaborTable.setTotalLabor(tableJobsRepairOrder);
		roDialog.calcRoTotal();
	}
}
