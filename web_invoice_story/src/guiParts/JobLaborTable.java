package guiParts;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

import objects.DbObject;
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
	public void paint(DbObject object) {		// NOTE not used
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(DbObject object, int selectedItemIndex) {		// NOTE not used
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(List<? extends DbObject> objects) {		// NOTE similar to ROJobTable, this is not technically used anywhere
		// TODO Auto-generated method stub							// probably add a 'throw UnsupportedOperation'

	}

	@Override
	public void sort(int i) {		// NOTE probably add a throw UnsupportedOperation here too
		// TODO Auto-generated method stub

	}
	
	public void setTotalLabor(ProjectJobTable tableJobsRepairOrder) {
		ProjectJobTableItem selectedJobTableItem = (ProjectJobTableItem) tableJobsRepairOrder.getSelection()[0];
		BigDecimal laborTotal = calculateLaborTotal();
		selectedJobTableItem.setLaborTotal(laborTotal);
		selectedJobTableItem.setText(ProjectJobTable.LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		selectedJobTableItem.setText(ProjectJobTable.JOB_TOTAL_COLUMN, "$" + (laborTotal.add(selectedJobTableItem.getPartTotal()).toString()));
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
