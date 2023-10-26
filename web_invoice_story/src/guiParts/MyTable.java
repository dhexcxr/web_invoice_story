package guiParts;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import objects.DbObjectSearchable;

public abstract class MyTable extends Table {
	
	public static final int DELETE_MENU_ITEM = 0;
	
	protected static Collator collator = Collator.getInstance(Locale.getDefault());

	protected MyTable(Composite parent, int style) {
		super(parent, style);
	}
	
	public abstract void paint(DbObjectSearchable object);
	
	public abstract void paint(DbObjectSearchable object, int selectedItemIndex);
	
	public abstract void paint(List<? extends DbObjectSearchable> objects);
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public abstract void sort(int i);
}
