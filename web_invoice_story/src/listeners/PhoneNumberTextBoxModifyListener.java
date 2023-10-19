package listeners;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;

import guiParts.MyText;

public class PhoneNumberTextBoxModifyListener implements ModifyListener{
	private MyText text;
	private String textBoxText;
	
	// this is now used here and in PartInvoiceEditorEventListener, and in Customer
	private static final String NOT_NUMBERS = "[^0-9]";		// find a better name
	private static final String SEVEN_DIGIT_PHONE = "$1-$2";
	private static final String TEN_DIGIT_PHONE = "$1-$2-$3";
	private static final String INTERNATIONAL_PHONE = "$1-$2-$3-$4";
	
	// allow us to ignore modify events inside listener
	boolean ignore = false;
	
	public PhoneNumberTextBoxModifyListener(MyText text) {
		this.text = text;
		this.textBoxText = text.getText();
	}

	@Override
	public void modifyText(ModifyEvent e) {
		// format text in text box, account for different length county codes
		if (ignore) {
			return;
		}
		
		if (!text.getText().equals(textBoxText)) {
			text.setModified(true);
			Point cursorPosition = text.getSelection();
			ignore = true;
			text.setText(text.getText().replaceAll(NOT_NUMBERS, ""));
			if (text.getText().replaceAll(NOT_NUMBERS, "").length() >= 7 && text.getText().replaceAll(NOT_NUMBERS, "").length() < 10) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d+)", SEVEN_DIGIT_PHONE));
			} else if (text.getText().replaceAll(NOT_NUMBERS, "").length() < 11) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d{3})(\\d+)", TEN_DIGIT_PHONE));
			} else if (text.getText().replaceAll(NOT_NUMBERS, "").length() == 11) {
				text.setText(text.getText().replaceFirst("(\\d{1})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (text.getText().replaceAll(NOT_NUMBERS, "").length() == 12) {
				text.setText(text.getText().replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			} else if (text.getText().replaceAll(NOT_NUMBERS, "").length() == 13) {
				text.setText(text.getText().replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d+)", INTERNATIONAL_PHONE));
			}
			ignore = false;
			text.setSelection(cursorPosition);
		} else {
			text.setModified(false);
		}
	}
}