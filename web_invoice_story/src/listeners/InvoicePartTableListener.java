package listeners;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import guiParts.MyText;
import guiParts.ProjectJobTable;
import gui.ProjectDialog;
import objects.InvoicePart;
import guiParts.InvoicePartTable;
import guiParts.InvoicePartTableItem;

public class InvoicePartTableListener implements Listener {
		
	private InvoicePartTable invoicePartTable;
	private TableEditor editor;
	private List<MyText> invoiceDetailText;
	private Shell parent;
	private Event originalEvent;
	
	private boolean visible = false;
	
	private ProjectJobTable tableJobsRepairOrder;
	private ProjectDialog projectDialog;
	
	private boolean editingInvoice;
	
	public InvoicePartTableListener(InvoicePartTable invoicePartTable, List<MyText> invoiceDetailText, Shell parent) {
		this.invoicePartTable = invoicePartTable;
		this.invoiceDetailText = invoiceDetailText;
		this.parent = parent;
		
		this.editor = new TableEditor(this.invoicePartTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    
	    editingInvoice = true;
	}
	
	public InvoicePartTableListener(InvoicePartTable invoicePartTable, ProjectJobTable tableJobsRepairOrder, ProjectDialog projectDialog, Shell parent) {
		this(invoicePartTable, new ArrayList<>());
		this.invoiceDetailText.add(new MyText(new Shell(), 0));		// I think this is a total hack way to do this
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.invoiceDetailText.add(new MyText(new Shell(), 0));
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.projectDialog = projectDialog;
		this.parent = parent;
	}

	public InvoicePartTableListener(InvoicePartTable invoicePartTable, List<MyText> invoiceDetailText) {
		this.invoicePartTable = invoicePartTable;
		this.invoiceDetailText = invoiceDetailText;
		
		this.editor = new TableEditor(this.invoicePartTable);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    
	    editingInvoice = false;
	}

	@Override
	public void handleEvent(Event event) {
		originalEvent = event;
        int currentTableItemIndex = invoicePartTable.getSelectionIndex();
        Point clickPoint = new Point(event.x, event.y);
        while (currentTableItemIndex < invoicePartTable.getItemCount() && currentTableItemIndex >= 0) {		// scan through Invoice Part's TableItems
        	if (findColumn(currentTableItemIndex, clickPoint)) {
        		return;
        	}
        	if (!visible)
        		return;
        	currentTableItemIndex++;
        }
	}
	
	private boolean findColumn(int currentTableItemIndex, Point clickPoint) {
		final TableItem selectedTableItem = invoicePartTable.getItem(currentTableItemIndex);
    	for (int i = 0; i < invoicePartTable.getColumnCount(); i++) {		// find selected column
    		Rectangle selectedTableItemColumnBounds = selectedTableItem.getBounds(i);
    		if (selectedTableItemColumnBounds.contains(clickPoint) &&
    				(i == InvoicePartTable.PART_NUMBER_COLUMN || i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)) {
    			if ((i == InvoicePartTable.QUANTITY_COLUMN || i == InvoicePartTable.PART_PRICE_COLUMN)
    					&& (selectedTableItem.getData() == null || ((InvoicePart) selectedTableItem.getData()).getPart() == null)) {
    				return true;		// this IF is what finally did it, the whole not editing QTY or Price without an associated Part
    			}
    			final int selectedColumnIndex = i;
    			Text editorTxtBox = new Text(invoicePartTable, SWT.NONE);
    			Listener textListener = editorListener(currentTableItemIndex, selectedColumnIndex, editorTxtBox);
    			editor.setEditor(editorTxtBox, selectedTableItem, i);
    			editorTxtBox.addListener(SWT.FocusOut, textListener);
    			editorTxtBox.addListener(SWT.Traverse, textListener);
    			editorTxtBox.addListener(SWT.MouseDown, textListener);
    			editorTxtBox.setText(selectedTableItem.getText(i));
    			editorTxtBox.selectAll();
    			editorTxtBox.setFocus();
    			return true;
    		}
    		if (!visible && selectedTableItemColumnBounds.intersects(invoicePartTable.getClientArea())) {
    			visible = true;
    		}
    	}
    	return false;
	}
	
	private Listener editorListener(int currentTableItemIndex, int selectedColumnIndex, Text editorTxtBox) {
		if (editingInvoice) {
			return new InvoicePartEditorListener(invoicePartTable, currentTableItemIndex, selectedColumnIndex,
					editorTxtBox, invoiceDetailText, parent, this);
		} else {
			int currentJobTableItemIndex = tableJobsRepairOrder.getSelectionIndex();
			return new ProjectPartEditorListener(invoicePartTable, currentTableItemIndex, selectedColumnIndex,
					editorTxtBox, invoiceDetailText, parent, this,
					tableJobsRepairOrder, currentJobTableItemIndex, projectDialog);
		}
	}
	
	public void tabbed(int currentColumn) {
		Event tabbedEvent = originalEvent;
		InvoicePartTableItem selectedTableItem = (InvoicePartTableItem) invoicePartTable.getSelection()[0];
		switch (currentColumn) {
		case InvoicePartTable.PART_NUMBER_COLUMN:
			tabbedEvent.x = selectedTableItem.getBounds(InvoicePartTableItem.QUANTITY_COLUMN).x + 1;
			break;
		case InvoicePartTable.QUANTITY_COLUMN:
			tabbedEvent.x = selectedTableItem.getBounds(InvoicePartTableItem.PART_PRICE_COLUMN).x + 1;
			break;
		case InvoicePartTable.PART_PRICE_COLUMN:
			tabbedEvent.x = selectedTableItem.getBounds(InvoicePartTableItem.PART_NUMBER_COLUMN).x + 1;
			tabbedEvent.y = tabbedEvent.y + selectedTableItem.getBounds().height;
			invoicePartTable.setSelection(invoicePartTable.getSelectionIndex() + 1);
			invoicePartTable.notifyListeners(SWT.MouseDown, new Event());
			break;
		default:
			return;
		}
		handleEvent(tabbedEvent);
	}
}
