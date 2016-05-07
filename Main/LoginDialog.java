package Main;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JPanel;


import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import server.Server_Main;
import client.Client_Main;



/**
 * LoginDialog GUI
 * @author ÀÓ¼º¼ö
 *
 */
public class LoginDialog extends JFrame implements ActionListener{

	private JTextField userName;
	private JPasswordField password;
	private JButton loginButton;
	private JButton closeButton;
	private JButton signupButton;
	
	/**
	 * Constructor for LoginDialog
	 */
	public LoginDialog(){
		setTitle("SSChat Login");
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		
		Container contentPane;
		contentPane = getContentPane();
		
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		
		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Welcome to SSChat!"));
		top.add(messPanel);
		
		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(10);
		namePanel.add(userName);
		top.add(namePanel);
		
		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(10);
		pwPanel.add(password);
		top.add(pwPanel);

		
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel("If you don't have an account: "));
		signupButton = new JButton("Sign up now!");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);
		
		contentPane.add("North", top);
		
		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		loginButton = new JButton("Login");
		loginButton.addActionListener(this);
		butPanel.add(loginButton);
		
		//Need to press Alt+Enter//
		loginButton.setMnemonic(KeyEvent.VK_ENTER);
		
		closeButton = new JButton("Close");
		closeButton.addActionListener(this);
		butPanel.add(closeButton);
		
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);	
		
	}
	
	/**
	 * Listener for LoginDialog
	 * listens for loginButton, closeButton
	 */
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == loginButton)
		{

			if (!attemptLogin(userName.getText(), String.valueOf(password.getPassword()))){
				JOptionPane.showMessageDialog(this, "Incorrect Credentials",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		else if(e.getSource() == closeButton)
		{
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
				System.exit(0);			
		}
	}

	/**
	 * Check if credentials are valid
	 * Currently Checks only if the id equals "server" or "client"
	 * @param id
	 * @param pw
	 * @return
	 */
	private boolean attemptLogin(String id, String pw) {

		
		setVisible( false );
		if(id.equals("server")){
			Server_Main server = new Server_Main();
		}
		if(id.equals("client")){
			Client_Main client = new Client_Main(); 
		}
		
		return true;
	}
}
