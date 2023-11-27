package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import web_invoice_story.DbServices;

import org.eclipse.swt.graphics.Image;

public class Splash extends Dialog{
	
	protected Shell shlSplash;
	private Display display;
	

	public Splash(Shell parent, int style) {
		super(parent, style);
	}

	public void open() {
		createContents();

		shlSplash.open();
		display = getParent().getDisplay();
		// keeping this out of a loop allows the application to continue without this being dismissed by the user
		if (!display.readAndDispatch()) {
			display.sleep();
		}
	}
	
	private void createContents() {
		shlSplash = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlSplash.setSize(980, 640);
		shlSplash.setText("Connecting to database...");
		
		Gui.setDialogAtCenter(shlSplash);
		
		shlSplash.setBackgroundImage(new Image(display, DbServices.class.getClassLoader().getResourceAsStream("resources/splash.jpg")));
		shlSplash.setText("Connecting to database...");
		
	}
	
	public void setShellText (String newShellText) {
		shlSplash.setText(newShellText);
		return;
	}

	public void close() {
		shlSplash.dispose();
	}
}
