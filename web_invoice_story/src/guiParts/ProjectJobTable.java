package guiParts;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import objects.DbObject;

public class ProjectJobTable extends MyTable {
	
	public static final int JOB_NAME_COLUMN = 0;
	public static final int PART_TOTAL_COLUMN = 1;
	public static final int LABOR_TOTAL_COLUMN = 2;
	public static final int JOB_TOTAL_COLUMN = 3;

	public ProjectJobTable(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
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
	public void paint(List<? extends DbObject> objects) {		// NOTE this is technically not used, but is kinda linked via SearchTextBoxListeners
		// TODO Auto-generated method stub							// but there is no TextSearchBox for RO Jobs, probably need to do a 'throw UnsupportedOperation' or whatever

	}

	@Override
	public void sort(int i) {
		// TODO Auto-generated method stub

	}

}
