package guiParts;

import java.math.BigDecimal;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import web_invoice_story.Application;

public class JobLaborTable extends MyTable {
	
	public static final int TECHNICIAN_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int HOURS_COLUMN = 2;
	public static final int RATE_COLUMN = 3;
	public static final int TOTAL_COLUMN = 4;

	public JobLaborTable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void paint(Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Object object, int selectedItemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Object[] objects) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sort(int i) {
		// TODO Auto-generated method stub

	}
	
	public void setTotalLabor(RepairOrderJobTable tableJobsRepairOrder) {
		RepairOrderJobTableItem selectedJobTableItem = (RepairOrderJobTableItem) tableJobsRepairOrder.getSelection()[0];
		BigDecimal laborTotal = calculateLaborTotal();
		selectedJobTableItem.setLaborTotal(laborTotal);
		selectedJobTableItem.setText(RepairOrderJobTable.LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		selectedJobTableItem.setText(RepairOrderJobTable.JOB_TOTAL_COLUMN, "$" + (laborTotal.add(selectedJobTableItem.getPartTotal()).toString()));
		tableJobsRepairOrder.notifyListeners(SWT.BUTTON5, new Event());
	}
	
	private BigDecimal calculateLaborTotal() {
//		BigDecimal taxRate = BigDecimal.valueOf(0.065);		// TODO set tax rate in application settings
		BigDecimal total = BigDecimal.valueOf(0);
		TableItem[] items = this.getItems();
		for (TableItem item : items) {
			if (item.getText(JobLaborTable.TOTAL_COLUMN).equals("")) {
				// ignore new TableItem at end of list with no data set 
				break;
			}
			Application.getLogger().log(Level.INFO, "labor Total before: {0}", total);
			total = total.add(new BigDecimal(item.getText(JobLaborTable.TOTAL_COLUMN)));
			Application.getLogger().log(Level.INFO, "labor price to BigDecimal: {0}", new BigDecimal(item.getText(JobLaborTable.TOTAL_COLUMN)));
			Application.getLogger().log(Level.INFO, "labor Total after: {0}", total);
		}
		return total;
	}

}
