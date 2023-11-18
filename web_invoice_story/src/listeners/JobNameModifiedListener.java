package listeners;

import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.TableItem;

import objects.Job;
import web_invoice_story.Application;
import guiParts.MyText;
import guiParts.ProjectJobTable;

public class JobNameModifiedListener extends InfoTextBoxModifyListener {
	
	private ProjectJobTable tableJobsRepairOrder;

	public JobNameModifiedListener(MyText text, ProjectJobTable tableJobsRepairOrder) {
		super(text);
		this.tableJobsRepairOrder = tableJobsRepairOrder;
	}
	
	// TODO probably add all Job Details text things in here
	
	@Override
	public void modifyText(ModifyEvent e) {
		// help track if text box has been modified
		super.modifyText(e);
		if (super.txtBox.isModified()) {
			Application.getLogger().log(Level.INFO, "Job Name modified subclass is working");
			TableItem selectedJob = tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex());
			selectedJob.setText(0, super.txtBox.getText());
			((Job) selectedJob.getData()).setJobName(super.txtBox.getText());
		} else if (tableJobsRepairOrder.getItemCount() > 0){
			tableJobsRepairOrder.getItem(tableJobsRepairOrder.getSelectionIndex()).setText(0, "");
		}
	}

}
