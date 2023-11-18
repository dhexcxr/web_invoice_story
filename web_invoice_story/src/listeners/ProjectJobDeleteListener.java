package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;

import gui.ProjectDialog;
import objects.Job;
import guiParts.ProjectJobTable;

public class ProjectJobDeleteListener extends MouseAdapter {
	
	private ProjectDialog roDialog;
	private ProjectJobTable tableJobsRepairOrder;
	private TabFolder tabFolderJobsRepairOrder;
	public ProjectJobDeleteListener(ProjectDialog roDialog) {
		this.roDialog = roDialog;
		this.tableJobsRepairOrder = roDialog.getTableJobsProject();
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsProject();
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
