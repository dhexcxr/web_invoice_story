package guiParts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import objects.DbObjectSearchable;
import objects.Project;

public class RepairOrderTable extends MyTable {
	private static final int RO_NUMBER_INDEX = 0;
	private static final int CUSTOMER_INDEX = 1;
	private static final int YEAR_INDEX = 2;
	private static final int MAKE_INDEX = 3;
	private static final int MODEL_INDEX = 4;
	private static final int VIN_INDEX = 5;
	private static final int JOBS_INDEX = 6;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = RO_NUMBER_INDEX;
	
	
	public RepairOrderTable(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	public void paint(DbObjectSearchable object) {
		// build each TableItem to fill ProjectSite Table
			paint(object, this.getSelectionIndex());
	}

	@Override
	public void paint(DbObjectSearchable object, int selectedTableItemIndex) {
		// TODO compare this with PartInventoryTable.paint(part, slectedTableItemIndex)
		if (object == null) {
			return;
		}
		Project project= (Project) object;
		TableItem tableItem = this.getItem(selectedTableItemIndex);	
		tableItem.setText(new String[] {Long.toString(project.getRepairOrderId()), project.getCustomerName(),
				project.getUnitYear(), project.getUnitMake(), project.getUnitModel(), project.getUnitVin(), project.getJobNames()});
		tableItem.setData(project);
	}

	@Override
	public void paint(List<? extends DbObjectSearchable> objectResults) {
		for (DbObjectSearchable object : objectResults) {
			if (object  instanceof Project project) {
				TableItem tableItem =  new TableItem(this, SWT.NONE);	
				tableItem.setText(new String[] {Long.toString(project.getRepairOrderId()), project.getCustomerName(),
						project.getUnitYear(), project.getUnitMake(), project.getUnitModel(), project.getUnitVin(), project.getJobNames()});
				tableItem.setData(project);

			} else {
				break;
			}
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}

	// TODO this and sort() can be moved into MyTable class, the only thing thats different in any Table Class' version of this method
		// is the String[] values which could be moved into a custom MyTableItem class
	private void initialSortOnPaint(int sortColumn, int sortDirection) {
		// sort table results by 0-indexed column
		TableItem[] items = this.getItems();
		TableColumn column = this.getColumn(sortColumn);
		int index = sortColumn;
		for (int i = 1; i < items.length; i++) {
			String value1 = items[i].getText(index);
			for (int j = 0; j < i; j++) {
				String value2 = items[j].getText(index);
				if ((sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) ||
						(sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0)) {
					String[] values = { items[i].getText(RO_NUMBER_INDEX), items[i].getText(CUSTOMER_INDEX),
							items[i].getText(YEAR_INDEX), items[i].getText(MAKE_INDEX), items[i].getText(MODEL_INDEX),
							items[i].getText(VIN_INDEX), items[i].getText(JOBS_INDEX)};
					Object tempRepairOrder = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					item.setData(tempRepairOrder);
					items = this.getItems();
					break;
				}
			}
		}
		this.setSortColumn(column);
		this.setSortDirection(sortDirection);
	}

	@Override
	public void sort(int sortColumn) {
		if (currentSortedColumn == sortColumn && currentSortDirection == SWT.UP) {
			currentSortDirection = SWT.DOWN;
		} else if (currentSortedColumn == sortColumn && currentSortDirection == SWT.DOWN) {
			currentSortDirection = SWT.UP;
		} else {
			currentSortedColumn = sortColumn;
			currentSortDirection = SWT.UP;
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}

}
