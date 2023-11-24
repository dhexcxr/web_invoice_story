package guiParts;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import objects.DbObject;
import objects.ProjectSite;

public class ProjectSiteTable extends MyTable{
	
	private static final int OWNER_INDEX = 0;
	private static final int ADDRESS_INDEX = 1;
	private static final int CITY_INDEX = 2;
	private static final int STATE_INDEX = 3;
	private static final int ZIP_INDEX = 4;
	private static final int NOTES_INDEX = 5;
	
	private int currentSortDirection = SWT.UP;
	private int currentSortedColumn = OWNER_INDEX;

	public ProjectSiteTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(DbObject object) {		// NOTE not used
		// build each TableItem to fill ProjectSite Table
			paint(object,this.getSelectionIndex());
	}

	@Override
	public void paint(DbObject customer, int selectedTableItemIndex) {		// NOTE not used
		// TODO compare this with MyPartInvoiceTable.paint(Object)
//		this.paint(new Object[] {customer});
	}
	
	@Override
	public void paint(List<? extends DbObject> objectResults) {
		// build each TableItem to fill ProjectSite Table
		for (DbObject object : objectResults) {
			if (object instanceof ProjectSite projectSite) {		// TODO remove all null chekcs with an instanceof
				TableItem tableItem = new TableItem(this, SWT.NONE);
				tableItem.setText(new String[] {projectSite.getOwner(), projectSite.getProjectSiteAddress(), projectSite.getProjectSiteCity(),
						 projectSite.getProjectSiteState(), projectSite.getProjectSiteZipCode(), projectSite.getProjectSiteNotes()});
				tableItem.setData(projectSite);
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
					String[] values = { items[i].getText(OWNER_INDEX), items[i].getText(ADDRESS_INDEX),
							items[i].getText(CITY_INDEX), items[i].getText(STATE_INDEX), items[i].getText(ZIP_INDEX),
							items[i].getText(NOTES_INDEX)};
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
