package guiParts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import objects.Job;
import objects.JobLabor;
import objects.Part;
import web_invoice_story.Application;

public class RepairOrderJobTableItem extends TableItem {
	
	public static final int JOB_NAME_COLUMN = 0;
	public static final int PART_TOTAL_COLUMN = 1;
	public static final int LABOR_TOTAL_COLUMN = 2;
	public static final int JOB_TOTAL_COLUMN = 3;
	
	private BigDecimal partTotal;
	private BigDecimal laborTotal;

	public RepairOrderJobTableItem(Table parent, int style) {
		super(parent, style);
	}

	public BigDecimal getPartTotal() {
		return partTotal == null ? BigDecimal.valueOf(0) : partTotal;
	}

	public void setPartTotal(BigDecimal partTotal) {
		this.partTotal = partTotal;
	}

	public BigDecimal getLaborTotal() {
		return laborTotal == null ? BigDecimal.valueOf(0) : laborTotal;
	}

	public void setLaborTotal(BigDecimal laborTotal) {
		this.laborTotal = laborTotal;
	}
	
	@Override
	public void setData(Object data) {
		super.setData(data);
		// TODO set TableItem columns from data attributes
		Job thisJob = (Job) data;
		this.setText(JOB_NAME_COLUMN, thisJob.getJobName());
		
		partTotal = BigDecimal.valueOf(0);
		// TODO i'm pretty sure this usage of getParts() instead of getJobParts() will be problematic
		for (Part part : thisJob.getParts()) {
			if (part == null) {
				Application.getLogger().log(Level.INFO, "Null part in ROJobTableItem.setData");
				break;
			}		// here, it gets a part total from each part's retail, instead of the JobPart's current soldPrice
			partTotal = partTotal.add(part.getRetail()).setScale(2, RoundingMode.CEILING);
		}
		this.setText(PART_TOTAL_COLUMN, "$" + partTotal.toString());
		
		laborTotal = BigDecimal.valueOf(0);
		for (JobLabor labor : thisJob.getLabor()) {
			laborTotal = laborTotal.add(labor.getHours().multiply(labor.getLaborRate())).setScale(2, RoundingMode.CEILING);
		}
		this.setText(LABOR_TOTAL_COLUMN, "$" + laborTotal.toString());
		
		this.setText(JOB_TOTAL_COLUMN, "$" + (partTotal.add(laborTotal).toString()));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
