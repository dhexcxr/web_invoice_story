package guiParts;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public abstract class MyCombo extends Combo {

	protected MyCombo(Composite parent, int style) {
		super(parent, style);
	}
	
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
}
