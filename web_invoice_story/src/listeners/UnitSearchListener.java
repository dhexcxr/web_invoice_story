package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.UnitSearchDialog;
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
		UnitSearchDialog unitSearchDialog = new UnitSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		ProjectSite selectedUnit;
		if (!txtUnitRepairOrder.getText().equals(txtUnitRepairOrderStartingText)) {
			// if we're editing a current unit, pass Search to ProjectSite Search Dialog
			selectedUnit = (ProjectSite) unitSearchDialog.open(((ProjectSite) txtUnitRepairOrder.getData()).getAddressProjectSite());
		} else if (!txtCustomerRepairOrder.getText().equals(txtCustomerRepairOrderStartingText)) {
			selectedUnit = (ProjectSite) unitSearchDialog.open(((Customer) txtCustomerRepairOrder.getData()).getFullName());
		} else {
			// open normal, empty ProjectSite Search Dialog
			selectedUnit = (ProjectSite) unitSearchDialog.open();
		}
		if (selectedUnit instanceof ProjectSite) {
			StringBuilder unitData = new StringBuilder(selectedUnit.getAddressProjectSite() + "\n" +
														selectedUnit.getStateProjectSite() + ", " + selectedUnit.getCityProjectSiteString() + 
														selectedUnit.getZipCodeProjectSiteString() + "\n");
			txtUnitRepairOrder.setText(unitData.toString());
			txtUnitRepairOrder.setData(selectedUnit);
		}
	}
}
