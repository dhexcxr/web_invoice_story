package guiParts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import objects.Account;
import objects.Customer;
import objects.DbObjectSearchable;

public class AccountTable extends MyTable{
	// TODO maybe turn these into a AccountTable.Columns enum
	public static final int ACCOUNT_NUMBER_INDEX = 0;
	public static final int ACCOUNT_HOLDER_INDEX = 1;
	public static final int LAST_CHARGE_INDEX = 2;
	public static final int LAST_PAYMENT_INDEX = 3;
	public static final int BALANCE_INDEX = 4;
	public static final int ZIP_INDEX = 5;
	public static final int HOME_PHONE_INDEX = 6;
	public static final int CELL_PHONE_INDEX = 7;
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = ACCOUNT_HOLDER_INDEX;

	public AccountTable(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	public void paint(DbObjectSearchable object) {		// NOTE not used
		// build each TableItem to fill ProjectSite Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(DbObjectSearchable object, int selectedTableItemIndex) {
		// TODO compare this with MyPartInvoiceTable.paint(Object)
//		this.paint(new Object[] {customer});
		List<Account> accounts = new ArrayList<>();
		TableItem[] tableItems = this.getItems();
		
		int i = 0;
		while (i < tableItems.length) {
			if (i == selectedTableItemIndex) {
				accounts.add((Account) object);
			} else {
				accounts.add((Account) tableItems[i].getData());				
			}
			i++;
		}
		this.removeAll();
		paint(accounts);
	}
	
	@Override
	public void paint(List<? extends DbObjectSearchable> objectResults) {
		// build each TableItem to fill Customer Table
		for (DbObjectSearchable object : objectResults) {
			if (object == null || !(object instanceof Account account)) {
				break;		// NOTE the null check might not be necessary now that we're passing in a List<> instead of the built-in array
			}
			TableItem tableItem = new TableItem(this, SWT.NONE);
			tableItem.setText(new String[] {account.getAccountNumber(), account.getAccountHolder(), "0.00" /*last charge*/,
					"0.00" /*last payment*/, "$" + account.getBalance().toString()} );
			tableItem.setData(account);
		}
		initialSortOnPaint(currentSortedColumn, currentSortDirection);
	}
	
	// TODO this and sort() can be moved into MyTable class, the only thing thats different in any Table Class' version of this method
				// is the String[] values which could be moved into a custom MyTableItem class
	private void initialSortOnPaint(int sortColumn, int sortDirection) {		// TODO rewrite this so it works correctly for Numbers, see below
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
					String[] values = { items[i].getText(ACCOUNT_NUMBER_INDEX), items[i].getText(ACCOUNT_HOLDER_INDEX),
							items[i].getText(LAST_CHARGE_INDEX), items[i].getText(LAST_PAYMENT_INDEX), items[i].getText(BALANCE_INDEX)};
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
	
//	private void initialSortOnPaint(int sortColumn, int sortDirection) {		// NOTE half done version with BigDecimal sorting, need to do something about collator
//		// sort table results by 0-indexed column
//		TableItem[] items = this.getItems();
//		TableColumn column = this.getColumn(sortColumn);
//		int index = sortColumn;
//		for (int i = 1; i < items.length; i++) {
//			for (int j = 0; j < i; j++) {
//				if (sortColumn == BALANCE_INDEX) {	// sort by number
//					BigDecimal value1 = new BigDecimal(items[i].getText(index).replaceAll("[^-.0-9]", ""));
//					BigDecimal value2 = new BigDecimal(items[j].getText(index).replaceAll("[^-.0-9]", ""));
//				} else {		// sort by string value
//					String value1 = items[i].getText(index);
//					String value2 = items[j].getText(index);
//				}
//				
//				if ((sortDirection ==  SWT.UP && collator.compare(value1, value2) < 0) ||
//						(sortDirection ==  SWT.DOWN && collator.compare(value1, value2) > 0)) {
//					String[] values = { items[i].getText(ACCOUNT_NUMBER_INDEX), items[i].getText(ACCOUNT_HOLDER_INDEX),
//							items[i].getText(LAST_CHARGE_INDEX), items[i].getText(LAST_PAYMENT_INDEX), items[i].getText(BALANCE_INDEX)};
//					Object tempPart = items[i].getData();
//					items[i].dispose();
//					TableItem item = new TableItem(this, SWT.NONE, j);
//					item.setText(values);
//					item.setData(tempPart);
//					items = this.getItems();
//					break;
//				}
//			}
//		}
//		this.setSortColumn(column);
//		this.setSortDirection(sortDirection);
//	}
	
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
