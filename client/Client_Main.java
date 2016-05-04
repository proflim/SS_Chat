package client;

import server.About;

import java.io.*;
import java.net.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Main.LoginDialog;
import server.Server_Main;

public class Client_Main extends JFrame implements ActionListener {
	
	/*
	// Text field for receiving radius
	private JTextField jtf = new JTextField();
	// Text area to display contents
	private JTextArea jta = new JTextArea();
	// IO streams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	*/
	//////////////////////////////////////////////////////
	private JPanel Panel_OnlineUserList = new JPanel();
	private JPanel Panel_OnlineUserList_Top = new JPanel();
	
	private JPanel Panel_Chat = new JPanel();
	
	private JPanel Panel_Function = new JPanel();
	private JPanel Panel_Function_Top = new JPanel();
	private JPanel Panel_Function_Center = new JPanel();
	
	private JSplitPane Pane_Top = new JSplitPane();
	private JSplitPane Pane_Whole = new JSplitPane();
	
	private JTextField numUserField = new JTextField("(0)");
	private List<String> userList ;
	private DefaultListModel userListModel = new DefaultListModel();
	private JList userListBox;
	private JScrollPane Pane_UserListBox = new JScrollPane();
	private JTextArea chatArea = new JTextArea();
	private JScrollPane Pane_ChatArea;
	private JTextField receiverField = new JTextField();	//need to set default receiverField as "All Users"
	private JTextArea messageArea = new JTextArea();
	private JButton sendButton = new JButton("Send"); 
	public static void main(String[] args) {
		new Client_Main();
	}

	public Client_Main() {

		setTitle("SSChat Client");
		setSize(800, 500);
		setResizable(false);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//Create MenuBar
		setJMenuBar(createMenuBar());

		
		//OnlineUserList Panel
		Panel_OnlineUserList.setPreferredSize(new Dimension(200, 300));
		Panel_OnlineUserList.setLayout(new BorderLayout());
		
		Panel_OnlineUserList_Top.setLayout(new FlowLayout());
		Panel_OnlineUserList_Top.add(new JLabel("Online Users"));
		numUserField.setEditable(false);
		Panel_OnlineUserList_Top.add(numUserField);
		Panel_OnlineUserList.add(Panel_OnlineUserList_Top, BorderLayout.NORTH);
		
		//Dummy data//
		userListModel.addElement("All");
		userListModel.addElement("user1");
		userListModel.addElement("user2");
		userListModel.addElement("user3");
		
		userListBox = new JList(userListModel);
		Pane_UserListBox = new JScrollPane(userListBox);
		Panel_OnlineUserList.add(Pane_UserListBox,BorderLayout.CENTER);
		
		//Chat Panel
		Panel_Chat.setPreferredSize(new Dimension(300, 300));
		Panel_Chat.setLayout(new BorderLayout());
		Panel_Chat.add(new JLabel("Chat Window"), BorderLayout.NORTH);
		
		chatArea.setEditable(false);
		Pane_ChatArea = new JScrollPane(chatArea);
		Panel_Chat.add(Pane_ChatArea, BorderLayout.CENTER);
		

		//Function Panel
		Panel_Function.setPreferredSize(new Dimension(300, 100));
		Panel_Function.setLayout(new BorderLayout());
		Panel_Function_Top.setLayout(new FlowLayout(FlowLayout.LEADING));
		Panel_Function_Center.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		Panel_Function_Top.add(new JLabel("Send To:   "));
		receiverField.setPreferredSize(new Dimension(90,20));
		Panel_Function_Top.add(receiverField);
		Panel_Function_Center.add(new JLabel("Message: "));
		messageArea.setPreferredSize(new Dimension(600,80));
		Panel_Function_Center.add(messageArea);
		sendButton.addActionListener(this);
		Panel_Function_Center.add(sendButton);
		
		Panel_Function.add(Panel_Function_Top,BorderLayout.NORTH);
		Panel_Function.add(Panel_Function_Center,BorderLayout.CENTER);
		
		Pane_Top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Panel_OnlineUserList, Panel_Chat);
		Pane_Whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, Pane_Top, Panel_Function);
		getContentPane().add(Pane_Whole);

		  		
		/*
		// Panel p to hold the label and text field
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Enter radius"), BorderLayout.WEST);
		p.add(jtf, BorderLayout.CENTER);
		jtf.setHorizontalAlignment(JTextField.RIGHT);
		
		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		jtf.addActionListener(new Listener()); // Register listener
		
		setTitle("Client");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!
		
		try {
		  // Create a socket to connect to the server
		  Socket socket = new Socket("localhost", 8000);
		  // Socket socket = new Socket("130.254.204.36", 8000);
		  // Socket socket = new Socket("drake.Armstrong.edu", 8000);
		
		  // Create an input stream to receive data from the server
		  fromServer = new DataInputStream(
		    socket.getInputStream());
		
		  // Create an output stream to send data to the server
		  toServer =
		    new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
		  jta.append(ex.toString() + '\n');
	    }
	    */
		setVisible(true);
	}
	
	JMenuBar createMenuBar() {


		JMenuBar menuBar = new JMenuBar();
		JMenuItem mi;

		
		
		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		
		mi = (JMenuItem) Access.add(new JMenuItem("Logout"));	
		mi.setMnemonic('L');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "Do you wish to Logout? \n(This will terminate the server as well)",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (ans == JOptionPane.YES_OPTION){
					dispose();
					LoginDialog loginDialog = new LoginDialog();

					return;	
				}
			}
		});
		
		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ans = JOptionPane.showConfirmDialog(null, "Do you wish to Exit completely?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (ans == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});
		
		
		JMenu Config = (JMenu) menuBar.add(new JMenu("Config"));
		Config.setMnemonic('C');

		mi = (JMenuItem) Config.add(new JMenuItem("Connect Config"));	
		mi.setMnemonic('C');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		mi = (JMenuItem) Config.add(new JMenuItem("User Config"));	
		mi.setMnemonic('U');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		
		JMenu Help = (JMenu) menuBar.add(new JMenu("Help"));
		Help.setMnemonic('H');

		//if user is not admin
		mi = (JMenuItem) Help.add(new JMenuItem("How to.."));
		mi.setMnemonic('H');
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {
				ClientHowTo c_howto = new ClientHowTo();
				
			}
		});
		mi = (JMenuItem) Help.add(new JMenuItem("About"));
		mi.setMnemonic('A');
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {

				About about = new About();
			}
		});
		
		return menuBar;
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}