package listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import gui.RepairOrderDialog;
import guiParts.JobLaborTable;
import guiParts.RepairOrderJobTable;

public class RepairOrderLaborTableListener implements Listener {
	
	private JobLaborTable jobLaborTable;
	private TableEditor editor;
	private RepairOrderJobTable tableJobsRepairOrder;
	private RepairOrderDialog repairOrderDialog;

	public RepairOrderLaborTableListener(JobLaborTable jobLaborTable, RepairOrderJobTable tableJobsRepairOrder, RepairOrderDialog repairOrderDialog) {
		this.jobLaborTable = jobLaborTable;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.repairOrderDialog = repairOrderDialog;
		this.editor = new TableEditor(jobLaborTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	}

	@Override
	public void handleEvent(Event event) {
		int currentTableItemIndex = jobLaborTable.getSelectionIndex();
        Rectangle tableWidgetArea = jobLaborTable.getClientArea();
        Point clickPoint = new Point(event.x, event.y);
        while (currentTableItemIndex < jobLaborTable.getItemCount() && currentTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	boolean visible = false;
        	final TableItem selectedTableItem = jobLaborTable.getItem(currentTableItemIndex);
        	for (int i = 0; i < jobLaborTable.getColumnCount(); i++) {		// find selected column
        		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
        		if (selectedTableItemColumnBounds.contains(clickPoint) &&
        				(i == JobLaborTable.TECHNICIAN_COLUMN || i == JobLaborTable.DESCRIPTION_COLUMN ||
        								i == JobLaborTable.HOURS_COLUMN || i == JobLaborTable.RATE_COLUMN)) {
        			final int selectedColumnIndex = i;
        			Text editorTxtBox = new Text(jobLaborTable, SWT.NONE);
        			int currentJobTableItemIndex = tableJobsRepairOrder.getSelectionIndex();
        			Listener textListener = new RepairOrderLaborEditorListener(jobLaborTable, currentTableItemIndex, selectedColumnIndex,
        																		editorTxtBox, tableJobsRepairOrder, currentJobTableItemIndex,
        																			repairOrderDialog);
        			editor.setEditor(editorTxtBox, selectedTableItem, i);
        			editorTxtBox.addListener(SWT.FocusOut, textListener);
        			editorTxtBox.addListener(SWT.Traverse, textListener);
        			editorTxtBox.addListener(SWT.MouseDown, textListener);
        			editorTxtBox.setText(selectedTableItem.getText(i));
        			editorTxtBox.selectAll();
        			editorTxtBox.setFocus();
        			return;
        		}
        		if (!visible && selectedTableItemColumnBounds.intersects(tableWidgetArea)) {
        			visible = true;
        		}
        	}
        	if (!visible)
        		return;
        	currentTableItemIndex++;
        }
//        tableJobsRepairOrder.notifyListeners(SWT.BUTTON4, new Event());		// save Parts and Labor
	}

}
