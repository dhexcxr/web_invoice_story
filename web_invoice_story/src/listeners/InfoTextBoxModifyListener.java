package listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;

import guiParts.MyText;

public class InfoTextBoxModifyListener implements ModifyListener {
	
	protected MyText txtBox;
	private String txtBoxStartingText;
	
	public InfoTextBoxModifyListener(MyText text) {
		this.txtBox = text;
		this.txtBoxStartingText = text.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// help track if text box has been modified
		txtBox.setModified(!txtBox.getText().equals(txtBoxStartingText));
	}
}
