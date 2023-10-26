package guiParts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import objects.DbObjectSearchable;
import objects.Part;

public class PartInventoryTable extends MyTable{
	private static final int PART_NUMBER_INDEX = 0;
	private static final int DESCRIPTION_INDEX = 1;
	private static final int ON_HAND_INDEX = 2;
	private static final int SUPPLIER_INDEX = 3;
	private static final int CATEGORY_INDEX = 4;
	private static final int COST_INDEX = 5;
	private static final int RETAIL_INDEX = 6;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = PART_NUMBER_INDEX;

	public PartInventoryTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(DbObjectSearchable object) {		// NOTE not used
		// build each TableItem to fill Unit Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(DbObjectSearchable part, int selectedTableItemIndex) {		// NOTE not used
		// TODO compare this with MyPartInvoiceTable.paint(Object)
			// e.g. where is this method called and does it matter that I'm ignoring selectedTableItemIndex
//		this.paint(new Object[] {part});
	}
	
	@Override
	public void paint(List<? extends DbObjectSearchable> partResults) {
		// build each TableItem to fill Unit Table
		for (DbObjectSearchable object : partResults) {
			if (object == null || !(object instanceof Part part)) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {part.getPartNumber(), part.getDescription(),
					Integer.toString(part.getOnHand() == -1 ? 0 : part.getOnHand()), part.getSupplier(),
								part.getCategory(), part.getCost().toString(), part.getRetail().toString()});
			tableItem.setData(part);
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
					String[] values = { items[i].getText(PART_NUMBER_INDEX), items[i].getText(DESCRIPTION_INDEX),
							items[i].getText(ON_HAND_INDEX), items[i].getText(SUPPLIER_INDEX), items[i].getText(CATEGORY_INDEX),
								items[i].getText(COST_INDEX), items[i].getText(RETAIL_INDEX)};
					Object tempPart = items[i].getData();
					items[i].dispose();
					TableItem item = new TableItem(this, SWT.NONE, j);
					item.setText(values);
					item.setData(tempPart);
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
