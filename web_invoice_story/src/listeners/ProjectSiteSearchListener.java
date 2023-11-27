package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.ProjectSiteSearchDialog;
import guiParts.MyText;
import objects.Customer;
import objects.ProjectSite;

public class ProjectSiteSearchListener extends MouseAdapter{

	MyText txtProjectSiteProject;
	String txtProjectSiteProjcetStartingText;
	MyText txtCustomerRepairOrder;
	String txtCustomerRepairOrderStartingText;

//	public ProjectSiteSearchListener(MyText txtProjectSiteProject) {
//		this(txtProjectSiteProject, new MyText(null, 0));
//	}

	public ProjectSiteSearchListener(MyText txtProjectSiteProject, MyText txtCustomerProject) {
		this.txtProjectSiteProject = txtProjectSiteProject;
		this.txtProjectSiteProjcetStartingText = txtProjectSiteProject.getText();
		this.txtCustomerRepairOrder = txtCustomerProject;
		this.txtCustomerRepairOrderStartingText = txtCustomerProject.getText();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		ProjectSiteSearchDialog projectSiteSearchDialog = new ProjectSiteSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		ProjectSite selectedProjectSite;
		if (!txtProjectSiteProject.getText().equals(txtProjectSiteProjcetStartingText)) {
			// if we're editing a current project site, pass Search to ProjectSite Search Dialog
			selectedProjectSite = (ProjectSite) projectSiteSearchDialog.open(((ProjectSite) txtProjectSiteProject.getData()).getProjectSiteAddress());
		} else if (!txtCustomerRepairOrder.getText().equals(txtCustomerRepairOrderStartingText)) {
			selectedProjectSite = (ProjectSite) projectSiteSearchDialog.open(((Customer) txtCustomerRepairOrder.getData()).getFullName());
		} else {
			// open normal, empty ProjectSite Search Dialog
			selectedProjectSite = (ProjectSite) projectSiteSearchDialog.open();
		}
		StringBuilder projectSiteData = new StringBuilder(selectedProjectSite.getProjectSiteAddress() + "\n" +
				selectedProjectSite.getProjectSiteState() + ", " + selectedProjectSite.getProjectSiteCity() + 
				selectedProjectSite.getProjectSiteZipCode() + "\n");
		txtProjectSiteProject.setText(projectSiteData.toString());
		txtProjectSiteProject.setData(selectedProjectSite);
	}
}
