package client;

import server.About;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

import Main.Data;
import Main.LoginDialog;
import server.Server_Main;

public class Client_Main extends JFrame implements ActionListener {
	

	// IO streams
	private ObjectOutputStream outputToServer;
	private ObjectInputStream inputFromServer;

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
	private DefaultListModel userListModel = new DefaultListModel();
	private JList userListBox;
	private JScrollPane Pane_UserListBox = new JScrollPane();
	private JTextArea chatArea = new JTextArea();
	private JScrollPane Pane_ChatArea;
	private JTextField receiverField = new JTextField();	//need to set default receiverField as "All Users"
	private JTextArea messageArea = new JTextArea();
	private JButton sendButton = new JButton("Send"); 
	private JToggleButton connectButton = new JToggleButton("Connect");

	private static int portNum;
	private static String ipAddress;
	private String userName;
	private int userID;
	
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

		//Initialize value
		portNum = 8888;
		ipAddress = "127.0.0.1";
		userName = "hkuster";	//set default userName
		
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
		Panel_Function_Top.setLayout(new BoxLayout(Panel_Function_Top, BoxLayout.LINE_AXIS));
		Panel_Function_Top.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
		Panel_Function_Center.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		Panel_Function_Top.add(new JLabel("  Send To:    "));
		receiverField = new JTextField();
		
		Panel_Function_Top.add(receiverField);
		Panel_Function_Top.add(Box.createRigidArea(new Dimension(387,10)));

		connectButton.addActionListener(this);
		connectButton.setPreferredSize(new Dimension(120,30));
		Panel_Function_Top.add(connectButton);
		Panel_Function_Top.add(Box.createRigidArea(new Dimension(113,10)));
		Panel_Function_Center.add(new JLabel("Message: "));
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		messageArea.setBorder(border);
		messageArea.setPreferredSize(new Dimension(600,80));
		Panel_Function_Center.add(messageArea);
		sendButton.addActionListener(this);
		Panel_Function_Center.add(sendButton);
		
		Panel_Function.add(Panel_Function_Top,BorderLayout.NORTH);
		Panel_Function.add(Panel_Function_Center,BorderLayout.CENTER);
		
		Pane_Top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Panel_OnlineUserList, Panel_Chat);
		Pane_Whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, Pane_Top, Panel_Function);
		getContentPane().add(Pane_Whole);
		
		setVisible(true);



	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == connectButton){
			AbstractButton abstractButton = (AbstractButton) e.getSource();
			boolean selected = abstractButton.getModel().isSelected();
	        if(selected){
	        	connectButton.setText("Disconnect");
	        	//Start Connection on New Thread
	        	Thread clientThread = new Thread(new StartClient(ipAddress,portNum));
	        	clientThread.start();
	        	
	        	
	        }
	        else{
	        	connectButton.setText("Connect");
	        	
	        	//End Server
	        	//End connection with all thread (online users)
	        	try{
	        		
	        	}
	        	finally{
	        		//socket.close();	
	        	}
	        }
		}
		
	}
	
	//Creates MenuBar
	private JMenuBar createMenuBar() {

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

	
	class StartClient implements Runnable {
		private int portNum;
		private String ipAddress;
		
		public StartClient(String address, int num){
			this.ipAddress = address;
			this.portNum = num;
		}
		
		public void run(){
			
        	try {
        		// Create a socket to connect to the server
			
				Socket socket = new Socket(ipAddress, portNum);
				chatArea.append("You have logged in at "+new Date()+"\n");
				  
				// Create an output stream to send data to the server
				outputToServer = new ObjectOutputStream(socket.getOutputStream());
				
				
				//send username to server
				Data d = new Data(1,userName);
				outputToServer.writeObject(d);
				
				// Create an input stream to receive data from the server
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			    //Create a new thread
		        ListeningThread task = new ListeningThread(inputFromServer);
				new Thread(task).start();
      		}
      		catch (IOException ex) {
      			chatArea.append(ex.toString() + '\n');
      	    }
		}
	}
	//Define thread class for new client connection
	class ListeningThread implements Runnable{
		private ObjectInputStream inputFromServer;
		
		
		public ListeningThread(ObjectInputStream is){
			
			this.inputFromServer = is;
		}
		
		public void run() {
			try{
		        // Continuously listen to the client
		        while (true) {
		        	Data object = (Data) inputFromServer.readObject();
		        	
		        	//Process for different message types
		        	if(object.getType()==2){
		        		userID = object.gettoID();
		        		chatArea.append("Your ID is: "+userID+"\n");
		        		
		        		//Update UserList
		        	}
		        	else if (object.getType()==4){
		        		chatArea.append(object.getMessage());
		        		
		        		//Update UserList
		        	}
		        	
		        }
			}
			catch(ClassNotFoundException e){
				//e.printStackTrace();
	    		System.err.println(e);

			}
		    catch(IOException e) {
	    		System.err.println(e);
		    }
		}
	}
}