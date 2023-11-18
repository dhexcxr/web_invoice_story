package listeners;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.TabFolder;

import gui.ProjectDialog;
import objects.Job;
import web_invoice_story.Application;
import guiParts.MyText;

public class JobDetailsModifiedListener implements ModifyListener {

	private TabFolder tabFolderJobsRepairOrder;
	private List<MyText> jobDetailWidgets;
	
	public JobDetailsModifiedListener(ProjectDialog roDialog) {
		this.tabFolderJobsRepairOrder = roDialog.getTabFolderJobsProject();
		this.jobDetailWidgets = roDialog.getJobDetailsText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		Application.getLogger().log(Level.INFO, "JobDetailModifiedListener called by: {0}", e.widget);
		saveJobDetails();
	}

	
	private void saveJobDetails() {
		Job currentJob = (Job) tabFolderJobsRepairOrder.getData();
		if (currentJob == null) {
			return;
		}
		currentJob.setJobName(jobDetailWidgets.get(0).getText());
		currentJob.setDescription(jobDetailWidgets.get(1).getText());
		currentJob.setResolution(jobDetailWidgets.get(2).getText());
		currentJob.setReccomendations(jobDetailWidgets.get(3).getText());		
	}
}
