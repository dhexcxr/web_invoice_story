package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.UnitSearchDialog;
import guiParts.MyText;
import objects.ProjectSite;

public class UnitSearchListener extends MouseAdapter{

	MyText txtUnitRepairOrder;
	String txtUnitRepairOrderStartingText;

	public UnitSearchListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		UnitSearchDialog unitSearchDialog = new UnitSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		ProjectSite selectedUnit;
		if (!txtUnitRepairOrder.getText().equals(txtUnitRepairOrderStartingText)) {
			// if we're editing a current unit, pass Search to ProjectSite Search Dialog
			selectedUnit = (ProjectSite) unitSearchDialog.open(((ProjectSite) txtUnitRepairOrder.getData()).getOwner());
		} else {
			// open normal, empty ProjectSite Search Dialog
			selectedUnit = (ProjectSite) unitSearchDialog.open();
		}
		if (selectedUnit instanceof ProjectSite) {
			StringBuilder unitData = new StringBuilder(selectedUnit.getStateProjectSite() + " " + selectedUnit.getAddressProjectSite() + "\n" +
														selectedUnit.getCityProjectSiteString() + "\nVin: " + 
														selectedUnit.getNotesProjectSite() + "\nColor: " + selectedUnit.getZipCodeProjectSiteString() + "\n");
			txtUnitRepairOrder.setText(unitData.toString());
			txtUnitRepairOrder.setData(selectedUnit);
		}
	}
}
