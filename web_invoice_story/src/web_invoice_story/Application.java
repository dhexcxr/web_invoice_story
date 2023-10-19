package web_invoice_story;

import java.util.logging.Logger;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

import gui.Gui;

/**
 * This class controls all aspects of the application's execution
 * and is contributed through the plugin.xml.
 */
public class Application /* implements IApplication */ implements EntryPoint {
	
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static Display display;
	private static final Settings settings = new Settings();

//	public Object start(IApplicationContext context) throws Exception {
//		Display display = PlatformUI.createDisplay();
//		WorkbenchAdvisor advisor = new ApplicationWorkbenchAdvisor();
//		return PlatformUI.createAndRunWorkbench(display, advisor);
//}
	
	// this used to be called rap_mail_template

	public int createUI() {
		display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(1, false));
		shell.setText("Service Salamander");
//		shell.setImage(new Image(Main.display(), Main.class.getClassLoader().getResourceAsStream("resources/icon.png")));
		@SuppressWarnings("unused")
		Gui gui = new Gui(shell, SWT.NONE);
		shell.pack();
		shell.open();
		
		// Connect to SQL Database
		DbServices.connectToDb();
		if (DbServices.isConnected()) {
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
		}
		
		// save settings and disconnect from SQL DB before close
		settings.save();
		DbServices.disconnectFromDb();
		display.close();
		return 0;
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}

	public static final Display display() {
		return display;
	}
	
	public static Settings getSettings() {
		return settings;
	}

	public void stop() {
		// Do nothing
	}
}
