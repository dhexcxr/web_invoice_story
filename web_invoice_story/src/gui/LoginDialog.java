package gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import objects.Employee;
import objects.LoginResponse;
import objects.User;
import web_invoice_story.Secure;

import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Control;

public class LoginDialog extends Dialog{
	
	protected Shell shlLogin;
	private Text txtUserName;
	private Text txtPassword;
	Label lblUserName;
	Label lblPassword;
	
	private Button btnSubmit;
	private Button btnCancel;
	
	
//	private boolean response = false;
	private LoginResponse loginResponse = new LoginResponse();

	public LoginDialog(Shell parent, int style) {
		super(parent, style);
	}

	public LoginResponse open() {
		createContents();

		shlLogin.open();
		shlLogin.layout();
		shlLogin.forceActive();
		Display display = getParent().getDisplay();
		while (!shlLogin.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return loginResponse;
	}
	
	private void createContents() {
		shlLogin = new Shell(getParent(), SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
		shlLogin.setSize(412, 224);
		shlLogin.setText("Login");
		
		Gui.setDialogAtCenter(shlLogin);
		FormLayout fl_shlAmountDue = new FormLayout();
		shlLogin.setLayout(fl_shlAmountDue);
		
		txtUserName = new Text(shlLogin, SWT.BORDER);
		FormData fd_txtUserName = new FormData();
		fd_txtUserName.right = new FormAttachment(100, -5);
		fd_txtUserName.height = 24;
		fd_txtUserName.top = new FormAttachment(0, 5);
		txtUserName.setLayoutData(fd_txtUserName);
		
		txtPassword = new Text(shlLogin, SWT.BORDER);
		txtPassword.setEchoChar('*');
		txtPassword.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				// maybe do something
			}
		});
		FormData fd_txtPassword = new FormData();
		fd_txtPassword.top = new FormAttachment(txtUserName, 6);
		fd_txtPassword.left = new FormAttachment(txtUserName, 0, SWT.LEFT);
		fd_txtPassword.right = new FormAttachment(100, -5);
		fd_txtPassword.height = 24;
		txtPassword.setLayoutData(fd_txtPassword);

		lblUserName = new Label(shlLogin, SWT.NONE);
		fd_txtUserName.left = new FormAttachment(0, 180);
		FormData fd_lblUserName = new FormData();
		fd_lblUserName.bottom = new FormAttachment(txtUserName, 0, SWT.BOTTOM);
		fd_lblUserName.right = new FormAttachment(txtUserName, -6);
		lblUserName.setLayoutData(fd_lblUserName);
		lblUserName.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		lblUserName.setText("User Name:");
		
		lblPassword = new Label(shlLogin, SWT.NONE);
		FormData fd_lblPassword = new FormData();
		fd_lblPassword.bottom = new FormAttachment(txtPassword, 0, SWT.BOTTOM);
		fd_lblPassword.right = new FormAttachment(txtPassword, -6);
		lblPassword.setLayoutData(fd_lblPassword);
		lblPassword.setText("Password:");
		lblPassword.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		
		btnSubmit = new Button(shlLogin, SWT.NONE);
		FormData fd_btnLogin = new FormData();
		fd_btnLogin.top = new FormAttachment(txtPassword, 10);
		fd_btnLogin.bottom = new FormAttachment(100, -5);
		fd_btnLogin.right = new FormAttachment(100, -5);
		fd_btnLogin.height = 96;
		btnSubmit.setLayoutData(fd_btnLogin);
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				
				if (txtUserName.getText().length() == 0 || txtPassword.getText().length() == 0) {
					badUserNameOrPass();
					return;
				}

					// check valid usernames and passwords
				String username = txtUserName.getText();
				// get user details from DB
				List<User> users = new User().search(username);
				
				if (users.size() == 0) {		// user not found or incorrect
						// we might need to make sure that exact username get exact result from DB
							// maybe remove the % surrounding the query parameters somehow
					badUserNameOrPass();
					return;
				}
				
				User user = users.get(0);
				
				String password = txtPassword.getText();
				
				Secure passAuth = new Secure();
				Boolean passHashPassed = passAuth.authenticate(password.toCharArray(), user.getPasswordHash());
				
				if (!passHashPassed) {	// bad password
					badUserNameOrPass();
					return;
				} else if (passHashPassed && user.getUserId() == 0) {	// root user, this doesn't have a corresponding employee in DB
					loginResponse.setMatchingHash(passHashPassed);		// TODO this looks like junk
					shlLogin.close();										// make it so I don't check passHashPassed 3 times
				} else if (passHashPassed){									// but also still allow for root, but make sure root types in correct password
					loginResponse.setMatchingHash(passHashPassed);
					loginResponse.setLoginEmployee(new Employee().searchByPk(user.getEmployeeId()));
					shlLogin.close();
				}

			}
		});
		
		btnSubmit.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnSubmit.setText("Submit");

		btnCancel = new Button(shlLogin, SWT.NONE);
		fd_btnLogin.left = new FormAttachment(btnCancel, 11);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.top = new FormAttachment(lblPassword, 10);
		fd_btnCancel.bottom = new FormAttachment(100, -5);
		fd_btnCancel.left = new FormAttachment(0, 5);
		fd_btnCancel.width = 160;
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				// close dialog
				loginResponse.setMatchingHash(false);
				shlLogin.close();
			}
		});
		btnCancel.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.NORMAL));
		btnCancel.setText("Cancel");
		shlLogin.setTabList(new Control[]{txtUserName, txtPassword, btnCancel, btnSubmit});
	}
	
	private void badUserNameOrPass() {
		MessageBox loginCanceled = new MessageBox(shlLogin, SWT.ICON_INFORMATION | SWT.APPLICATION_MODAL);
		loginCanceled.setText("Notice");
		loginCanceled.setMessage("Incorrect User Name or Passoword");
		loginCanceled.open();
	}
}
