package guiParts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MyText extends Text {
	// extend Text widget class to add isModified to enable "fancy" test box effects
	
	private boolean modified = false;

	public MyText(Composite parent, int style) {
		super(parent, style);
	}
	
	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
