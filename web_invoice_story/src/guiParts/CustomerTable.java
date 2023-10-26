package guiParts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import objects.Customer;
import objects.DbObjectSearchable;

public class CustomerTable extends MyTable{
	
	public static final int FIRST_NAME_INDEX = 0;
	public static final int LAST_NAME_INDEX = 1;
	public static final int ADDRESS_INDEX = 2;
	public static final int CITY_INDEX = 3;
	public static final int STATE_INDEX = 4;
	public static final int ZIP_INDEX = 5;
	public static final int HOME_PHONE_INDEX = 6;
	public static final int CELL_PHONE_INDEX = 7;
	public static final int EMAIL_INDEX = 8;
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = LAST_NAME_INDEX;

	public CustomerTable(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void paint(DbObjectSearchable object) {		// NOTE not used
		// build each TableItem to fill Unit Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(DbObjectSearchable object, int selectedTableItemIndex) {		// NOTE not used
		// TODO compare this with MyPartInvoiceTable.paint(Object)
//		this.paint(new Object[] {customer});
	}
	
	@Override
	public void paint(List<? extends DbObjectSearchable> objectResults) {
		// build each TableItem to fill Customer Table
		for (DbObjectSearchable object : objectResults) {
			if (object == null || !(object instanceof Customer customer)) {
				break;
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {customer.getFirstName(), customer.getLastName(), customer.getAddress(), customer.getCity(),
					customer.getState(), customer.getZipCode(),	customer.setPhoneNumberFormat(customer.getHomePhone()),
						customer.setPhoneNumberFormat(customer.getCellPhone()), customer.getEmail()} );
			tableItem.setData(customer);
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
					String[] values = { items[i].getText(FIRST_NAME_INDEX), items[i].getText(LAST_NAME_INDEX),
							items[i].getText(ADDRESS_INDEX), items[i].getText(CITY_INDEX), items[i].getText(STATE_INDEX),
								items[i].getText(ZIP_INDEX), items[i].getText(HOME_PHONE_INDEX),
								items[i].getText(CELL_PHONE_INDEX), items[i].getText(EMAIL_INDEX) };
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
