package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.ProjectSiteSearchDialog;
import guiParts.MyText;
import objects.Customer;
import objects.ProjectSite;

public class UnitSearchListener extends MouseAdapter{

	MyText txtUnitRepairOrder;
	String txtUnitRepairOrderStartingText;
	MyText txtCustomerRepairOrder;
	String txtCustomerRepairOrderStartingText;

//	public UnitSearchListener(MyText txtUnitRepairOrder) {
//		this(txtUnitRepairOrder, new MyText(null, 0));
//	}

	public UnitSearchListener(MyText txtUnitRepairOrder, MyText txtCustomerRepairOrder) {
		this.txtUnitRepairOrder = txtUnitRepairOrder;
		this.txtUnitRepairOrderStartingText = txtUnitRepairOrder.getText();
		this.txtCustomerRepairOrder = txtCustomerRepairOrder;
		this.txtCustomerRepairOrderStartingText = txtCustomerRepairOrder.getText();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		ProjectSiteSearchDialog projectSiteSearchDialog = new ProjectSiteSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		ProjectSite selectedUnit;
		if (!txtUnitRepairOrder.getText().equals(txtUnitRepairOrderStartingText)) {
			// if we're editing a current unit, pass Search to ProjectSite Search Dialog
			selectedUnit = (ProjectSite) projectSiteSearchDialog.open(((ProjectSite) txtUnitRepairOrder.getData()).getProjectSiteAddress());
		} else if (!txtCustomerRepairOrder.getText().equals(txtCustomerRepairOrderStartingText)) {
			selectedUnit = (ProjectSite) projectSiteSearchDialog.open(((Customer) txtCustomerRepairOrder.getData()).getFullName());
		} else {
			// open normal, empty ProjectSite Search Dialog
			selectedUnit = (ProjectSite) projectSiteSearchDialog.open();
		}
		if (selectedUnit instanceof ProjectSite) {
			StringBuilder unitData = new StringBuilder(selectedUnit.getProjectSiteAddress() + "\n" +
														selectedUnit.getProjectSiteState() + ", " + selectedUnit.getProjectSiteCity() + 
														selectedUnit.getProjectSiteZipCode() + "\n");
			txtUnitRepairOrder.setText(unitData.toString());
			txtUnitRepairOrder.setData(selectedUnit);
		}
	}
}
