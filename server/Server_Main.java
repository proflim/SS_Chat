package server;




import Main.Data;
import Main.LoginDialog;
import Main.User;

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
import javax.swing.border.Border;

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
	
	private JTextField numUserField;
	private DefaultListModel userListModel = new DefaultListModel();
	private JList userListBox;
	private JScrollPane Pane_UserListBox = new JScrollPane();
	private JTextArea chatArea = new JTextArea();
	private JScrollPane Pane_ChatArea;
	private JTextField receiverField;	
	private JTextArea messageArea = new JTextArea();
	private JButton sendButton = new JButton("Send"); 
	private JToggleButton serviceButton = new JToggleButton("Start Service");
	
	private static int portNum;  
	private ServerSocket serverSocket;
	private static int userNum;
	private static int counterID=1;
	private static final LinkedList<User> userList= new LinkedList<User>();
	

	
	public static void main(String[] args) {
		new Server_Main();
	}

	public Server_Main() {
		setTitle("SSChat Server");
		setSize(800, 500);
		setResizable(false);
		setLocation(300,100);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		//Create MenuBar
		setJMenuBar(createMenuBar());

		//Initialize variables
		portNum=8888;
		userNum=0;
		//userList = new LinkedList<User>();
		
		//OnlineUserList Panel
		Panel_OnlineUserList.setPreferredSize(new Dimension(200, 300));
		Panel_OnlineUserList.setLayout(new BorderLayout());
		
		Panel_OnlineUserList_Top.setLayout(new FlowLayout());
		Panel_OnlineUserList_Top.add(new JLabel("Online Users"));
		numUserField = new JTextField("("+ userNum+")");
		numUserField.setPreferredSize(new Dimension(30,25));
		numUserField.setEditable(false);
		Panel_OnlineUserList_Top.add(numUserField);
		Panel_OnlineUserList.add(Panel_OnlineUserList_Top, BorderLayout.NORTH);
		
		//Dummy data//
		userListModel.addElement("All");
		
		
		userListBox = new JList(userListModel);
		Pane_UserListBox = new JScrollPane(userListBox);
		Panel_OnlineUserList.add(Pane_UserListBox,BorderLayout.CENTER);
		
		//Chat Panel
		Panel_Chat.setPreferredSize(new Dimension(300, 300));
		Panel_Chat.setLayout(new BorderLayout());
		Panel_Chat.add(new JLabel("  Chat Window"), BorderLayout.NORTH);
		
		chatArea.setEditable(false);
		Pane_ChatArea = new JScrollPane(chatArea);
		Panel_Chat.add(Pane_ChatArea, BorderLayout.CENTER);
		

		//Function Panel
		Panel_Function.setPreferredSize(new Dimension(300, 90));
		Panel_Function.setLayout(new BorderLayout());
		Panel_Function_Top.setLayout(new BoxLayout(Panel_Function_Top, BoxLayout.LINE_AXIS));
		Panel_Function_Top.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
		Panel_Function_Center.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		Panel_Function_Top.add(new JLabel("  Send To:    "));
		
		receiverField = new JTextField();	//Need to get the selected value of online users
		receiverField.setOpaque(false);
		Panel_Function_Top.add(receiverField);
		Panel_Function_Top.add(Box.createRigidArea(new Dimension(387,10)));

		serviceButton.addActionListener(this);
		serviceButton.setPreferredSize(new Dimension(120,30));
		Panel_Function_Top.add(serviceButton);
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
		
		//disable certain GUI features, since the server has not started yet.
		disableGUI();
		
		setVisible(true); 

		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == serviceButton){
			AbstractButton abstractButton = (AbstractButton) e.getSource();
			boolean selected = abstractButton.getModel().isSelected();
	        if(selected){
	        	serviceButton.setText("End Service");
	        	
	        	//Start Server on new Thread
	            Thread serverThread = new Thread(new StartServer(portNum));
	            serverThread.start();
	        	
	        }
	        else{
	        	serviceButton.setText("Start Service");
	        	
	        	//End Server
	        	//End connection with all thread (online users)
        		try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        	finally{
	        		//socket.close();	
	        	}
	        }
		}
        
	}
	private void disableGUI(){
		userListBox.setOpaque(false);
		chatArea.setOpaque(false);
		messageArea.setOpaque(false);
		sendButton.setEnabled(false);
		Pane_UserListBox.repaint();
		Pane_ChatArea.repaint();
		Panel_Function_Center.repaint();
	}
	
	private void enableGUI(){
		userListBox.setOpaque(true);
		chatArea.setOpaque(true);
		messageArea.setOpaque(true);
		sendButton.setEnabled(true);
		Pane_UserListBox.repaint();
		Pane_ChatArea.repaint();
		Panel_Function_Center.repaint();

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
		
		
		JMenu Services = (JMenu) menuBar.add(new JMenu("Services"));
		Services.setMnemonic('S');

		mi = (JMenuItem) Services.add(new JMenuItem("Port Config"));	
		mi.setMnemonic('P');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PortConfig portconfig = new PortConfig();
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

		mi = (JMenuItem) Help.add(new JMenuItem("How to.."));
		mi.setMnemonic('H');
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent arg0) {
				ServerHowTo s_howto = new ServerHowTo();
				
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

	public static void setPortNum(int num){
		portNum=num;
	}
	public static int getPortNum(){
		return portNum;
	}
	
	class StartServer implements Runnable {
		private int num;
		
		public StartServer(int portnum){
			this.num= portnum;
		}
		
		public void run(){
			try {
				System.out.println(portNum);
				serverSocket = new ServerSocket(portNum);
				chatArea.append("Server started at "+ new Date() + "\n");
				enableGUI();
				while (true){
					//Listen for a connection request
					Socket socket = serverSocket.accept();
					chatArea.append("Received connection\n");
										
					
			        // Create data input and output streams
			        ObjectInputStream inputFromClient = new ObjectInputStream(
			          socket.getInputStream());
			        ObjectOutputStream outputToClient = new ObjectOutputStream(
			          socket.getOutputStream());
			        
			        //Add to User List
					User u = new User("--",counterID, socket, outputToClient);
					userList.add(u);
					
					//Create a new thread
			        ClientListeningThread task = new ClientListeningThread(socket, inputFromClient, outputToClient, counterID);
					//ClientListeningThread task = new ClientListeningThread(socket, counterID);
					new Thread(task).start();
				}
				
			} catch (IOException e) {
				// TODO Close Connection Gracefully. 
				//e.printStackTrace();
				//Close input/output streams, close socket.
				chatArea.append("Server ended at "+ new Date() + "\n");
				disableGUI();
				resetUserListDisplay();
			}
		}

	}
	
	//Define thread class for new client connection
	class ClientListeningThread implements Runnable{
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int userID;
		private String userName;
		private int counter =1;
		private boolean isFirstTransmission;
		//private LinkedList<User> userList;

		
		public ClientListeningThread(Socket socket, ObjectInputStream is, ObjectOutputStream os, int ID){
			
			this.socket = socket;
			this.userID = ID;
			this.inputFromClient = is;
			this.outputToClient = os;
			this.isFirstTransmission=true;
			//this.userList = list;
			
			//After assigning ID, increment ID
			counterID++;
		}
		
		public void run() {
			try{

				
		        // Continuously listen to the client
		        while (true) {
		        	Data object = (Data) inputFromClient.readObject();
		        	
		        	/*
		        	 * If it is first transmission,
		        	 * Welcome the new user, assign unique ID, and pass UserList
		        	 */
		        	if(isFirstTransmission){
		        		userName = object.getfromName();
			        	//Welcome new user
		        		String s = "User "+object.getfromName()+" is online.\n";
		        		chatArea.append(s);
			        	
		        		//Set username in the userList
			        	for(User u : userList){
			        		if(u.getUserID()==userID){
			        			u.setUserName(userName);
			        			//chatArea.append(userName+", "+userID);
			        		}
			        	}
			        	
			        	//Pass unique ID and userList to the new user, 
			        	//and Announce new user to other users
			        	chatArea.append("sendingUserListSize: "+userList.size()+"\n");
			        	LinkedList<User> uList = userList;
			        	for(User u: userList){
			        		if(u.getUserID()==userID){
			        			Data d1 = new Data(2, userID, uList);
			        			u.getOutputStream().writeObject(d1);
			        			u.getOutputStream().flush();
					        	chatArea.append("newUser["+u.getUserID()+"]: "+uList.size()+"\n");
					        
			        		}
			        		else{
				        		Data d2 = new Data(4, userName,userID, s, uList);
			        			u.getOutputStream().writeObject(d2);
			        			u.getOutputStream().flush();
					        	chatArea.append("otherUser["+u.getUserID()+"]: "+uList.size()+"\n");

			        		}
			        		
			        	}
			        	isFirstTransmission=false;
		        	}
		        	
		        	//Process for different message types
		        	//Exception in Case 3 (Terminate Connection immediately)
		        	if(object.getType()==3){
		        		break;
		        	}
		        	else{
		        		processMessageServer(object);
		        	}
		        	
		        	updateUserListDisplay();
		        }
		        
			}
			catch(ClassNotFoundException e){
				//e.printStackTrace();
	    		System.err.println(e);
			}
		    catch(IOException e) {
		    	e.printStackTrace();
		    	System.err.println("Error here");
	    		System.err.println(e);
		    }
			finally{
				if(counter>0){
	        		String s = "User "+userName+"["+userID+"] has disconnected.\n";
					chatArea.append(s);
					int index=-1;
	        		for(int i = 0; i<userList.size();i++){
	        			if(userList.get(i).getUserID()==userID){
	        				index =i;
	        			}
	        		}
	        		userList.remove(index);
	        		chatArea.append("Deleted userID: "+userID+"\n");
	        		
	        		chatArea.append("userList size: "+userList.size()+"\n");
	        		for(User u: userList){
		        		Data d = new Data(5, userName,userID, s, userList);
		        		try {
							u.getOutputStream().writeObject(d);
							u.getOutputStream().flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        		}
					
					updateUserListDisplay();

					try {
						inputFromClient.close();
						outputToClient.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					counter--;
				}
			}
		}
	}
	
	
	private void processMessageServer(Data data){
		switch (data.getType()){
			case 0:
				
				break;
		
			case 1:
				
				break;
			case 2:
				/////
				break;
			case 3:
				/////
				break;
			case 4:
        		/////
				break;
			case 5:
				/////
				break;
			default:
					
				break;
		}
	}
	
	
	private void updateUserListDisplay(){
		//increment number of online users
		userNum=userList.size();
		numUserField.setText("("+userNum+")");
		
		userListModel.removeAllElements();
		userListModel.addElement("All");
		//Add elements to display
		for(User a : userList) {
			userListModel.addElement(a.getUserName()+"["+a.getUserID()+"]");
		}
	}
	private void resetUserListDisplay(){
		userNum=0;
		numUserField.setText("("+userNum+")");
		userListModel.removeAllElements();
		userListModel.addElement("All");
	}


}

