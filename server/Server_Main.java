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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/***
 * Server_Main Class
 * @author 임성수
 *
 */
public class Server_Main extends JFrame implements ActionListener{
   
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
	private JTextField receiverField = new JTextField();	
	private JTextArea messageArea = new JTextArea();
	private JButton sendButton = new JButton("Send"); 
	private JToggleButton serviceButton = new JToggleButton("Start Service");
	
	private static int portNum;  
	private ServerSocket serverSocket;
	private int selectedIndex;
	private String selectedUser;
	private static int userNum;
	private static int counterID=1;
	private static final LinkedList<User> userList= new LinkedList<User>();
	

	
	public static void main(String[] args) {
		new Server_Main();
	}
	
	/***
	 * Constructs main Server GUI
	 */
	public Server_Main() {
		setTitle("SSChat Server");
		setSize(800, 530);
		//setResizable(false);
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
		
		
		userListModel.addElement("All"); //add Default data 
		userListBox = new JList(userListModel);
		/***
		 * Listener for the userListBox
		 * If certain element is selected, sets the receiverField to that value.
		 * Updates String selectedUser, int selectedIndex: for later sending message.
		 */
		userListBox.addListSelectionListener(new ListSelectionListener(){
			public void  valueChanged(ListSelectionEvent s){
				int index =((JList)s.getSource()).getSelectedIndex();
				selectedUser = (String) ((JList)s.getSource()).getSelectedValue();
				//System.out.println("Index: "+index);
				//System.out.println("userString: "+userString);
				receiverField.setText(selectedUser);
				selectedIndex=index;
			}
		});
		
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
	
	/***
	 * Listener for main Server GUI. 
	 * Listens for serviceButton(Start/End Service), and sendButton(Send system level message to users)
	 */
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
	        	
	        	//End connection with all thread (online users)
	        	//Send Terminating Signal
        		try {
					Data d = new Data(3);
					for(User u: userList){
						u.getOutputStream().writeObject(d);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.err.println("Exception when sending terminating signal to clients.");
					e1.printStackTrace();
				}
	        	finally{
	        		try {
	        			//Clear Data
	        			userList.clear();
	        			//Close ServerSocket
						serverSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
	        	}
	        }
		}
		else if(e.getSource() == sendButton){
			
			String msg = messageArea.getText();
			//display message
			displayMessage("ADMIN",0,selectedUser,msg);
			
			//selectedIndex, selectedUser
			//send message to server
			
			try{
				Data d = new Data(0,"ADMIN",selectedUser,0, selectedIndex, msg);
				//chatArea.append("selectedIndex: "+selectedIndex+", "+selectedUser+"\n");
				if(d.gettoIndex()==0){//send to All
					for(User u: userList){
						u.getOutputStream().writeObject(d);
					}
				}
				else{//sent to specific Client
					userList.get(d.gettoIndex()-1).getOutputStream().writeObject(d);
				}
			}
			catch(IOException e1){
				e1.printStackTrace();
				chatArea.append("System Message: Unsuccessfully sent to client.\n");
			}
			//chatArea.append("System Message: Successfully sent to client.\n");
		}
        
	}
	/***
	 * disables GUI (userListBox, chatArea, messageArea, sendButton)
	 * used when Server is not yet connected.
	 */
	private void disableGUI(){
		userListBox.setOpaque(false);
		chatArea.setOpaque(false);
		messageArea.setOpaque(false);
		sendButton.setEnabled(false);
		Pane_UserListBox.repaint();
		Pane_ChatArea.repaint();
		Panel_Function_Center.repaint();
	}
	
	/***
	 * enables GUI (userListBox, chatArea, messageArea, sendButton)
	 * used when Server is connected.
	 */
	private void enableGUI(){
		userListBox.setOpaque(true);
		chatArea.setOpaque(true);
		messageArea.setOpaque(true);
		sendButton.setEnabled(true);
		Pane_UserListBox.repaint();
		Pane_ChatArea.repaint();
		Panel_Function_Center.repaint();

	}
	
	/***
	 * creates MenuBar in the main Server GUI
	 * Menu implemented: 
	 * Access: Exit, Config: Port Config, Help: How to, About
	 * Listeners for menu bars are implemented anonymously (Functionality trivial)
	 * @return JMenuBar
	 */
	private JMenuBar createMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenuItem mi;

		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		
		
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

		mi = (JMenuItem) Config.add(new JMenuItem("Port Config"));	
		mi.setMnemonic('P');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PortConfig portconfig = new PortConfig(chatArea, portNum);
			}
		});

		JMenu Help = (JMenu) menuBar.add(new JMenu("Help"));
		Help.setMnemonic('H');

		mi = (JMenuItem) Help.add(new JMenuItem("How to.."));
		mi.setMnemonic('H');
		mi.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
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

	/***
	 * sets Port Number
	 * @param num
	 */
	static void setPortNum(int num){
		portNum=num;
	}

	/***
	 * Starts Server.
	 * This class implements a thread that listens for new connections.
	 * For each new connection, a new ClientListeningthread is spawned.
	 * @author 임성수
	 *
	 */
	class StartServer implements Runnable {
		private int num; //port number
		
		 
		/***
		 * constructor of StartServer
		 * 
		 * @param portnum
		 */
		public StartServer(int portnum){
			this.num= portnum;
		}
		
		/***
		 * run()
		 * Connects to the designated port and listens for new connections.
		 */
		public void run(){
			try {
				chatArea.append("Opening server on Port "+portNum+"..\n");
				serverSocket = new ServerSocket(portNum);
				chatArea.append("Server started at "+ new Date() + "\n");
				enableGUI();
				while (true){
					//Listen for a connection request
					Socket socket = serverSocket.accept();
					//chatArea.append("Received connection\n");
										
					
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
					new Thread(task).start();
				}
				
			} catch (IOException e) {
				//e.printStackTrace();
			}
			finally { //quit connection
				chatArea.append("Server ended at "+ new Date() + "\n");
				disableGUI();
				resetUserListDisplay();
				
				//Send termination message to all online users
				try {
					Data d = new Data(3);
					for(User u: userList){
						u.getOutputStream().writeObject(d);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.err.println("IOException when sending terminating signal to clients.");
					e1.printStackTrace();
				}
				finally{//close serverSocket
	        		try {
						serverSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}
			}
		}

	}
	
	/***
	 * Class for listening to each client connection
	 * @author 임성수
	 *
	 */
	class ClientListeningThread implements Runnable{
		private Socket socket;
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputToClient;
		private int userID;
		private String userName;
		private int counter =1;
		private boolean isFirstTransmission;

		/***
		 * Constructor for ClientListeningThread
		 * 
		 * @param socket
		 * @param is
		 * @param os
		 * @param ID
		 */
		public ClientListeningThread(Socket socket, ObjectInputStream is, ObjectOutputStream os, int ID){
			
			this.socket = socket;
			this.userID = ID;
			this.inputFromClient = is;
			this.outputToClient = os;
			this.isFirstTransmission=true;
			
			
			//After assigning ID, increment ID
			counterID++;
		}
		
		/***
		 * change UserName
		 * @param newName
		 */
		public void changeUserName(String newName){
			this.userName = newName;
		}
		
		/***
		 * listens for any data transmission from the client.
		 * Processes incoming Data Object according to type
		 */
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
			        	//Announce new user
		        		String s = "User "+object.getfromName()+" is online.\n";
		        		chatArea.append(s);
			        	
		        		//Set username in the userList
			        	for(User u : userList){
			        		if(u.getUserID()==userID){
			        			u.setUserName(userName);
			        		}
			        	}
			        	
			        	//Pass unique ID and userList to the new user, 
			        	//and Announce new user to other users
			        	LinkedList<User> uList = userList;
			        	for(User u: userList){
			        		if(u.getUserID()==userID){
			        			Data d1 = new Data(2, userID, uList);
			        			u.getOutputStream().writeObject(d1);
			        			u.getOutputStream().flush();
					        	//chatArea.append("newUser["+u.getUserID()+"]: "+uList.size()+"\n");
					        
			        		}
			        		else{
				        		Data d2 = new Data(4, userName,userID, s, uList);
			        			u.getOutputStream().writeObject(d2);
			        			u.getOutputStream().flush();
					        	//chatArea.append("otherUser["+u.getUserID()+"]: "+uList.size()+"\n");

			        		}
			        		
			        	}
			        	isFirstTransmission=false;
		        	}
		        	else{
		        	
			        	//Process for different message types
			        	//Except for Case 3 (Terminate Connection immediately)
			        	if(object.getType()==3){
			        		break;
			        	}
			        	else{
			        		processMessageServer(object);
			        	}
		        	}
		        	updateUserListDisplay();
		        }
		        
			}
			catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		    catch(IOException e) {
		    	//e.printStackTrace();
		    }
			finally{//Close connection
				
				if(!serverSocket.isClosed()){
					//Announce disconnect
	        		String s = "User "+userName+"["+userID+"] has disconnected.\n";
					chatArea.append(s);
					
					//Remove User from UserList
					int index=-1;
	        		for(int i = 0; i<userList.size();i++){
	        			if(userList.get(i).getUserID()==userID){
	        				index =i;
	        			}
	        		}
	        		userList.remove(index);
	        		
	        		//Notify Disconnect to other Users
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
					
					//close streams and socket
					try {
						inputFromClient.close();
						outputToClient.close();
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		
		/***
		 * process Messages appropriately for different types
		 * 
		 * 0: normal communication (Client sends message to another Client)
		 * 1: update username (Client notifies username (can be new or to update) )
		 * 
		 * @param data
		 */
		private void processMessageServer(Data data){
			switch (data.getType()){
				case 0: //normal communication
					
					//display message
					displayMessage(data.getfromName(),data.getfromID(),data.gettoNameIDString(),data.getMessage());
					
					try{
						if(data.gettoIndex()==0){//send to All
							for(User u: userList){
								u.getOutputStream().writeObject(data);
							}
						}
						else{//sent to specific Client
							userList.get(data.gettoIndex()-1).getOutputStream().writeObject(data);
						}
					}
					catch(IOException e){
						e.printStackTrace();
						chatArea.append("Deliver Message: Unsuccessfully sent to client.\n");
					}
					//chatArea.append("Deliver Message: Successfully sent to client.\n");
					break;
			
				case 1://update username
					
					//change username of current listening thread
					changeUserName(data.getfromName());
					
					//updateUserList on server 
					updateUserList(data.getfromName(), data.getfromID());
					

					updateUserListDisplay();
					
					// Send updateUserList Request to all users 
					try{
						for(int i =0; i<userList.size(); i++){
							//Send to rest of the user (except himself)
							Data d = new Data(1,data.getfromName(), data.getfromID());
							//if(i!=indexOfUpdatedUser)
								userList.get(i).getOutputStream().writeObject(data);
						}

					}
					catch(IOException e){
						e.printStackTrace();
						chatArea.append("Update UserList: Unsuccessfully sent to client.\n");
					}
					//chatArea.append("Update UserList: Successfully sent to client.\n");
					
					break;
				
				default:
					chatArea.append("Something unexpected has happened. Please reboot. \n");
					break;
			}
		}
		
	}

	/***
	 * Updates UserList
	 * Changes userName at ID into newName
	 * @param newName
	 * @param id
	 */
	private void updateUserList(String newName, int id){
		
		String oldName="";
		for(int i=0; i<userList.size();i++){
			if(userList.get(i).getUserID() == id){
				oldName = userList.get(i).getUserName();
				userList.get(i).setUserName(newName);
			}
		}
		chatArea.append("User ("+oldName+") changed name to ("+newName+")\n");
	}
	
	/***
	 * updates UserList Display
	 */
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
	
	/**
	 * resets UserListDisplay (when the server ends / client logs out)
	 */
	private void resetUserListDisplay(){
		userNum=0;
		numUserField.setText("("+userNum+")");
		userListModel.removeAllElements();
		userListModel.addElement("All");
	}
	
	/**
	 * Displays message to chatArea
	 * For 0: normal communication
	 * 	"Name[ID]  >>  Name[ID] : msg"
	 * 
	 * @param fromName
	 * @param fromID
	 * @param toNameIDString
	 * @param msg
	 */
	private void displayMessage(String fromName, int fromID, String toNameIDString, String msg){
		chatArea.append(fromName+"["+fromID+"]"+"  >>  "+toNameIDString+": ");
		chatArea.append(msg+"\n");
	}



}

