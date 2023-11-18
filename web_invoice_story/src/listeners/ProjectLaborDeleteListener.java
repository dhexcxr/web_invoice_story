package listeners;

import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

import gui.ProjectDialog;
import objects.JobLabor;
import guiParts.JobLaborTable;
import guiParts.ProjectJobTable;

public class ProjectLaborDeleteListener extends MouseAdapter {
	
	private ProjectDialog roDialog;
	private JobLaborTable jobLaborTable;
	private ProjectJobTable tableJobsRepairOrder;

	public ProjectLaborDeleteListener(ProjectDialog roDialog) {
		this.roDialog = roDialog;
		this.jobLaborTable = roDialog.getJobLaborTable();
		this.tableJobsRepairOrder = roDialog.getTableJobsProject();
	}

	@Override
	public void mouseDown(MouseEvent e) {
		// delete Labor from Labor Table
		if (jobLaborTable.getSelectionIndex() >=0 && jobLaborTable.getSelectionIndex() < jobLaborTable.getItemCount()
				&& jobLaborTable.getItem(jobLaborTable.getSelectionIndex()).getData() != null) {
			JobLabor selectedJobLabor = (JobLabor) jobLaborTable.getItem(jobLaborTable.getSelectionIndex()).getData();

			// keep track of things to delete
			roDialog.addDetailsToDelete(selectedJobLabor.getTableName(), selectedJobLabor.getJobLaborId());

			int selectedIndex = jobLaborTable.getSelectionIndex();
			jobLaborTable.remove(selectedIndex);

			selectedIndex = selectedIndex == 0 ? 0 : selectedIndex - 1;
			jobLaborTable.setSelection(selectedIndex);

			jobLaborTable.setTotalLabor(tableJobsRepairOrder);
			roDialog.calcRoTotal();
		}
	}
}
