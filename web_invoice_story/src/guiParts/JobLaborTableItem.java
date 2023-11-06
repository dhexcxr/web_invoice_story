package guiParts;

import java.math.BigDecimal;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import objects.JobLabor;

public class JobLaborTableItem extends TableItem {
	
	private static final int HOURS_COLUMN = 2;
	private static final int RATE_COLUMN = 3;

	public JobLaborTableItem(Table parent, int style) {
		super(parent, style);
		this.setText(HOURS_COLUMN, "0");		// TODO choose a good default
		// TODO set default labor rate in settings
		this.setText(RATE_COLUMN, "100");
	}

	public JobLaborTableItem(Table parent, int style, int index) {
		super(parent, style, index);
	}
	
	// TODO create setData(Job job)
	
	public void setData(String installer, String description, BigDecimal hours, BigDecimal rate) {
		if (this.getData() == null) {
			super.setData(new JobLabor(installer, description, hours, rate));
		} else {
			JobLabor tableItemLabor = (JobLabor) this.getData();
			tableItemLabor.setInstaller(installer);
			tableItemLabor.setDescription(description);
			tableItemLabor.setHours(hours);
			tableItemLabor.setLaborRate(rate);
			super.setData(tableItemLabor);	// fix bug where labor changes were not saved to DB
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
