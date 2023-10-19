package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import gui.UnitSearchDialog;
import guiParts.MyText;
import objects.Unit;

public class UnitSearchListener extends MouseAdapter{

	MyText txtBox;
	String txtBoxStartingText;

	public UnitSearchListener(MyText txtBox) {
		this.txtBox = txtBox;
		this.txtBoxStartingText = txtBox.getText();
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		UnitSearchDialog unitSearchDialog = new UnitSearchDialog(Display.getDefault().getActiveShell(), SWT.NONE);
		Unit selectedUnit;
		if (!txtBox.getText().equals(txtBoxStartingText)) {
			// if we're editing a current unit, pass Search to Unit Search Dialog
			selectedUnit = (Unit) unitSearchDialog.open(((Unit) txtBox.getData()).getOwner());
		} else {
			// open normal, empty Unit Search Dialog
				selectedUnit = (Unit) unitSearchDialog.open();
		}
		if (selectedUnit instanceof Unit) {
			StringBuilder unitData = new StringBuilder(selectedUnit.getYear() + " " + selectedUnit.getMake() + "\n" +
														selectedUnit.getModel() + "\nVin: " + 
														selectedUnit.getVin() + "\nColor: " + selectedUnit.getColor() + "\n" +
														"Mileage: " + selectedUnit.getMileage());
			txtBox.setText(unitData.toString());
			txtBox.setData(selectedUnit);
		}
	}
}
