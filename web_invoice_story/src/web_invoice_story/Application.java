package web_invoice_story;

import java.util.logging.Logger;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import gui.Gui;
import gui.LoginDialog;
import gui.Splash;

import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

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

	/**
	 * @wbp.parser.entryPoint
	 */
	public int createUI() {
		display = new Display();
		Shell shell = new Shell(display, SWT.NO_TRIM);
		
		shell.setText("Web Invoice Story");
		shell.setLayout(new FormLayout());
//		shell.setLayout(new FillLayout());
//		shell.setImage(new Image(display(), Application.class.getClassLoader().getResourceAsStream("resources/icon.png")));
		shell.setMinimumSize(1024, 768);
		
		
//		@SuppressWarnings("unused")
		Gui gui = new Gui(shell, SWT.NONE);
		FormData fd_gui = new FormData();
		fd_gui.bottom = new FormAttachment(100);
		fd_gui.right = new FormAttachment(100);
		fd_gui.top = new FormAttachment(0);
		fd_gui.left = new FormAttachment(0);
		gui.setLayoutData(fd_gui);
		
		
		
		shell.pack();
		shell.setMaximized(true);
		shell.open();
		
		long splashTime = System.currentTimeMillis();
		Splash splash = new Splash(shell, SWT.SHELL_TRIM);
		splash.open();
		
		// Connect to SQL Database
		DbServices.connectToDb();
		
		while (System.currentTimeMillis() - splashTime < 1500) {};		// leave splash screen open for at least 2 seconds, but no more than 2 seconds
		splash.setShellText("Connected to Database.");
		
		LoginDialog login = new LoginDialog(shell, SWT.SHELL_TRIM);
		boolean loginResponse = login.open();
		
		if (!loginResponse) {
			MessageBox loginCanceled = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
			loginCanceled.setText("Notice");
			loginCanceled.setMessage("Good bye...");
			loginCanceled.open();
			shell.close();
			return 0;
		}
		
		splash.close();
		
		
		// TODO get size of display (browser window), if less than 1024x768 display dialog telling user to increase browser window size
		
		// TODO also, get the streamlined object save style objects from that other project I did
		
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
