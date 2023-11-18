package listeners;

import java.math.BigDecimal;
import java.util.List;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import gui.ProjectDialog;
import objects.InvoicePart;
import guiParts.InvoicePartTable;
import objects.JobPart;
import guiParts.MyText;
import guiParts.ProjectJobTable;
import guiParts.ProjectJobTableItem;

public class ProjectPartEditorListener extends InvoicePartEditorListener {
	
	private InvoicePartTable partInvoiceTable;
//	private int selectedTableItemIndex;
	private Text editorTxtBox;
	private List<MyText> invoiceDetailText;
	private ProjectJobTable tableJobsRepairOrder;
	private int currentJobTableItemIndex;
	private ProjectDialog projectDialog;
	
	public ProjectPartEditorListener(InvoicePartTable partInvoiceTable, int selectedTableItemIndex,
			int selectedColumnIndex, Text editorTxtBox, List<MyText> invoiceDetailText, Shell parent,
				InvoicePartTableListener invoicePartTableListener, ProjectJobTable tableJobsRepairOrder, int currentJobTableItemIndex,
					ProjectDialog projectDialog) {
		super(partInvoiceTable, selectedTableItemIndex, selectedColumnIndex, editorTxtBox, invoiceDetailText, parent, invoicePartTableListener);
		// TODO connect Job Part total
		this.partInvoiceTable = partInvoiceTable;
		super.selectedTableItemIndex = selectedTableItemIndex;
		this.editorTxtBox = editorTxtBox;
		this.invoiceDetailText = invoiceDetailText;
		this.tableJobsRepairOrder = tableJobsRepairOrder;
		this.currentJobTableItemIndex = currentJobTableItemIndex;
		this.projectDialog = projectDialog;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (!ignoreFocusOut) {
			if (partInvoiceTable.getItem(selectedTableItemIndex).getData() == null) {
				partInvoiceTable.getItem(selectedTableItemIndex).setData(new JobPart());
			}
			super.handleEvent(event);
			// found a bug where if you double-click the parts line to open the Part Search dialog box, it adds an InvoicePart instead of JobPart
				// and then line ~779 in RoDialog crashed trying to cast to (JobPart)
				// change from InvoicePart to JobPart, didn;t work
			JobPart newInvoicePart = (JobPart) partInvoiceTable.getItem(selectedTableItemIndex).getData(); 
			if (newInvoicePart == null || newInvoicePart.getPart() == null) {
				partInvoiceTable.getItem(selectedTableItemIndex).setData(null);
			}
			
			projectDialog.saveJob();
			totalParts();
			editorTxtBox.dispose();
		}
	}
	
	private void totalParts() {
		ProjectJobTableItem selectedJobTableItem = (ProjectJobTableItem) tableJobsRepairOrder.getItem(currentJobTableItemIndex);
		String textToParse = invoiceDetailText.get(0).getText();
		textToParse = textToParse.replaceAll("[^0-9.]", "");
		if (!textToParse.equals("")) {
			BigDecimal partTotal = new BigDecimal(textToParse);
			selectedJobTableItem.setPartTotal(partTotal);
			selectedJobTableItem.setText(ProjectJobTable.PART_TOTAL_COLUMN, "$" + partTotal.toString());
			selectedJobTableItem.setText(ProjectJobTable.JOB_TOTAL_COLUMN, "$" + (partTotal.add(selectedJobTableItem.getLaborTotal()).toString()));
		}
		projectDialog.calcRoTotal();
	}

	
}
