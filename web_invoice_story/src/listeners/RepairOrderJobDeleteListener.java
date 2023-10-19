package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;

import gui.RepairOrderDialog;
import objects.Job;
import guiParts.RepairOrderJobTable;

public class RepairOrderJobDeleteListener extends MouseAdapter {
	
	private RepairOrderDialog roDialog;
	private RepairOrderJobTable tableJobsRepairOrder;
	private TabFolder tabFolderJobsRepairOrder;
	public RepairOrderJobDeleteListener(RepairOrderDialog roDialog) {
		this.roDialog = roDialog;
		this.tableJobsRepairOrder = roDialog.getTableJobsRepairOrder();
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsRepairOrder();
	}
	
	@Override
	public void mouseDown(MouseEvent e) {
		// create new Job on Job Table
		int selectedIndex = -1;
		if (tableJobsRepairOrder.getSelectionIndex() >= 0 && tableJobsRepairOrder.getSelectionIndex() < tableJobsRepairOrder.getItemCount()
				&& tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData() != null) {

			Job selectedJob = (Job) tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).getData();
			// keep track of things to delete
			roDialog.addDetailsToDelete(selectedJob.getTableName(), selectedJob.getJobId());
			
			selectedIndex = tableJobsRepairOrder.getSelectionIndex();
			tableJobsRepairOrder.remove(selectedIndex);
			// remove Job we just deleted from tabFolder
			if (selectedJob.equals(tabFolderJobsRepairOrder.getData())) {
				tabFolderJobsRepairOrder.setData(null);
			}
			selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
			tableJobsRepairOrder.setSelection(selectedIndex);
			tableJobsRepairOrder.notifyListeners(SWT.Selection, new Event());

			roDialog.calcRoTotal();
		}
		
		// disable Job Tabs if no jobs on Job Table
		if (tableJobsRepairOrder.getItemCount() == 0) {
			roDialog.disableJobTabs();
		} 
	}

}
