package listeners;

import java.util.logging.Level;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

import guiParts.MyText;
import web_invoice_story.Application;

public class TextBoxFocusListener implements FocusListener {
	private MyText txtBox;
	private String textBoxText;
	
	public TextBoxFocusListener(MyText textBox) {
		this.txtBox = textBox;
		this.textBoxText = textBox.getText();
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		Application.getLogger().log(Level.INFO, "{0} box focused gained", textBoxText);
		// enable "flashy" text box effects
		if (txtBox.getText().equals(textBoxText)) {
			txtBox.setText("");
		}		
	}

	@Override
	public void focusLost(FocusEvent e) {
		Application.getLogger().log(Level.INFO, "{0} box focused lost", textBoxText);
		// enable "flashy" text box effects
		if (txtBox.getText().equals("")) {
			txtBox.setText(textBoxText);
		}
	}
}
