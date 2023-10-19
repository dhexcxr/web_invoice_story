package listeners;

import java.util.logging.Level;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.wb.swt.SWTResourceManager;

import guiParts.MyText;
import web_invoice_story.Application;

public class RequiredTextBoxModifyListener implements ModifyListener {
	
	private MyText txtBox;
	
	public RequiredTextBoxModifyListener(MyText textBox) {
		this.txtBox = textBox;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// set txtBox background to RED if user has not entered anything
		if (txtBox.getText().length() == 0) {
			txtBox.setModified(false);
			txtBox.setBackground(SWTResourceManager.getColor(255, 102, 102));		// RED
		} else {
			// set to white when user enters data into txtBox
			txtBox.setModified(true);
			txtBox.setBackground(SWTResourceManager.getColor(255, 255, 255));		// WHITE
		}
		Application.getLogger().log(Level.INFO, "txtBox.isModified: {0}", Boolean.toString(txtBox.isModified()));
	}
}
