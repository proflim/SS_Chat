package server;




import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Server_Main extends JFrame implements ActionListener{
  // Text area for displaying contents
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
		new Server_Main();
	}

	public Server_Main() {
		setTitle("SSChat Server");
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
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("Server");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!
		
		try {
		  // Create a server socket
		  ServerSocket serverSocket = new ServerSocket(8000);
		  jta.append("Server started at " + new Date() + '\n');
		
		  // Listen for a connection request
		  Socket socket = serverSocket.accept();
		
		  // Create data input and output streams
		  DataInputStream inputFromClient = new DataInputStream(
		    socket.getInputStream());
		  DataOutputStream outputToClient = new DataOutputStream(
		    socket.getOutputStream());
		
		  while (true) {
		    // Receive radius from the client
		    double radius = inputFromClient.readDouble();
		
		    // Compute area
		    double area = radius * radius * Math.PI;
		
		    // Send area back to the client
		    outputToClient.writeDouble(area);
		
		    jta.append("Radius received from client: " + radius + '\n');
		    jta.append("Area found: " + area + '\n');
		  }
		}
		catch(IOException ex) {
		  System.err.println(ex);
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

			}
		});
		
		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		
		JMenu Services = (JMenu) menuBar.add(new JMenu("Services"));
		Services.setMnemonic('S');

		mi = (JMenuItem) Services.add(new JMenuItem("Port Config"));	
		mi.setMnemonic('P');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		mi = (JMenuItem) Services.add(new JMenuItem("Start Service"));	
		mi.setMnemonic('S');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		mi = (JMenuItem) Services.add(new JMenuItem("End Service"));	
		mi.setMnemonic('E');
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

				
			}
		});
		mi = (JMenuItem) Help.add(new JMenuItem("About"));
		mi.setMnemonic('A');
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {

				
			}
		});
		
		return menuBar;
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
  
}