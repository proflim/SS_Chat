package client;

import server.About;









import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Main.Data;
import Main.LoginDialog;
import Main.User;
import server.Server_Main;

public class Client_Main extends JFrame implements ActionListener {
	

	// IO streams
	private ObjectOutputStream outputToServer;
	private ObjectInputStream inputFromServer;
	private Socket socket;

	//////////////////////////////////////////////////////
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
	private JTextField receiverField = new JTextField();	//need to set default receiverField as "All Users"
	private JTextArea messageArea = new JTextArea();
	private JButton sendButton = new JButton("Send"); 
	private JToggleButton connectButton = new JToggleButton("Connect");

	private static int portNum;
	private static String ipAddress;
	private String userName;
	private int userID;
	private static int userNum;
	private boolean isConnected;
	private int selectedIndex;
	private String selectedUser;
	private UserConfig uconfig;
	
	public static void main(String[] args) {
		new Client_Main();
	}

	public Client_Main() {

		setTitle("SSChat Client");
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

		//Initialize value
		portNum = 8888;				//set default port
		ipAddress = "127.0.0.1";	//set default ipaddress
		userName = "hkuster";		//set default userName
		isConnected=false;
		
		//OnlineUserList Panel
		Panel_OnlineUserList.setPreferredSize(new Dimension(200, 300));
		Panel_OnlineUserList.setLayout(new BorderLayout());
		
		Panel_OnlineUserList_Top.setLayout(new FlowLayout());
		Panel_OnlineUserList_Top.add(new JLabel("Online Users"));
		numUserField = new JTextField("("+userNum+")");
		numUserField.setPreferredSize(new Dimension(30,25));
		numUserField.setEditable(false);
		Panel_OnlineUserList_Top.add(numUserField);
		Panel_OnlineUserList.add(Panel_OnlineUserList_Top, BorderLayout.NORTH);
		
		//Default List (Contains only 'All' element, and no user)//
		userListModel.addElement("All");

		
		userListBox = new JList(userListModel);
		
		/* Listenener for the userListBox
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
		receiverField.setOpaque(false);
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
		
		//disable certain GUI features, since the client has not connected yet.
		disableGUI();

		
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
	        	
	        	//Send Terminating Signal
	        	if(isConnected){
		        	
		        	try {
		        		Data d = new Data(3);
						outputToServer.writeObject(d);
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						System.err.println("Exception when sending terminating signal to server.");
						e1.printStackTrace();
						
					}
		        	isConnected=false;
					chatArea.append("You have logged off at "+new Date()+"\n");
	        	}
	        }
		}
		else if(e.getSource() == sendButton){
			
			String msg = messageArea.getText();
			//display message
			if(selectedIndex!=0)
				displayMessage(userName,userID,selectedUser,msg);
			//chatArea.append(userName+"["+userID+"]"+"  >>  "+selectedUser+": ");
			//chatArea.append(msg+"\n");
			
			//selectedIndex, selectedUser
			//send message to server
			try {
				Data d = new Data(0,userName,selectedUser,userID, selectedIndex, msg);
				outputToServer.writeObject(d);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			chatArea.append("Successfully sent to server.\n");
			
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

		mi = (JMenuItem) Config.add(new JMenuItem("Connection Config"));	
		mi.setMnemonic('C');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConnectionConfig cconfig = new ConnectionConfig(chatArea,portNum,ipAddress);

			}
		});
		
		mi = (JMenuItem) Config.add(new JMenuItem("User Config"));	
		mi.setMnemonic('U');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				uconfig = new UserConfig(chatArea,userName);
				
				//When uconfig.doneButton is pressed, set new UserName 
				
				uconfig.doneButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent d) {
						userName = uconfig.nameField.getText();
						uconfig.dispose();
						//chatArea.append("Changed userName to : "+userName+"\n");
						
						try {
							Data a = new Data(1,userName,userID);
							outputToServer.writeObject(a);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						chatArea.append("Update userName: Successfully sent to server.\n");
						
					}
				});
			}
		});
		
		
		JMenu Help = (JMenu) menuBar.add(new JMenu("Help"));
		Help.setMnemonic('H');

		
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

	static void setPortNum(int num){
		portNum=num;
	}
	static void setIPAddress(String addr){
		ipAddress=addr;
	}
	
	class StartClient implements Runnable {
		private int portNum;
		private String ipAddress;
		private LinkedList<User> uList;
		
		public StartClient(String address, int num){
			this.ipAddress = address;
			this.portNum = num;
			this.uList = new LinkedList<User>();
		}
		
		public void run(){
			
        	try {
        		// Create a socket to connect to the server
				chatArea.append("Connecting to server on Port "+portNum+"..\n");
				socket = new Socket(ipAddress, portNum);
				chatArea.append("You have logged in at "+new Date()+"\n");
				enableGUI();
				isConnected=true;
				
				// Create an output stream to send data to the server
				outputToServer = new ObjectOutputStream(socket.getOutputStream());
				
				//send username to server
				Data d = new Data(1,userName);
				outputToServer.writeObject(d);
				
				// Create an input stream to receive data from the server
				inputFromServer = new ObjectInputStream(socket.getInputStream());
			    
				//Create a new thread
		        //ListeningThread task = new ListeningThread(inputFromServer);
				//new Thread(task).start();
				
      		}
        	catch(IOException ex){
      			chatArea.append("Connection refused.\n");
      			chatArea.append("Server is not available.\n");

        	}
        	
    		
        	try{
        		while (isConnected) {
		        	Data object = (Data) inputFromServer.readObject();
					
		        	if(object.getUserList()!=null)
		        		chatArea.append("MsgType: "+object.getType()+"\n");
		        	
		        	//Process for different message types
		        	processMessageClient(object);

        		}
        	}
        	catch(ClassNotFoundException e){
				//e.printStackTrace();
	    		System.err.println(e);
	    		e.printStackTrace();
			}
      		catch (IOException ex) {
				//ex.printStackTrace();
      	    }
        	finally{
		        try {
		        	//Check if socket has been initialized
		        	//If not, then server connection hasn't been established.
		        	//Just quit.
		        	if(socket!=null){
					outputToServer.close();
			        inputFromServer.close();
			        socket.close();
		        	}
			        chatArea.append("Disconnected from server at "+new Date()+"\n");
			        if(connectButton.isSelected()){
			        	connectButton.setSelected(false);
			        	connectButton.setText("Connect");
			        }
			        disableGUI();
			        resetUserListDisplay();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
		}
		private void processMessageClient(Data data){
			switch (data.getType()){
				case 0://normal communication
					displayMessage(data.getfromName(),data.getfromID(),data.gettoNameIDString(),data.getMessage());
					break;
			
				case 1://update username in userList
					
					updateUserListClient(data.getfromName(), data.getfromID());
	        		updateUserListDisplay(uList);
					break;
				case 2:	//get ID, receive current UserList
	        		userID = data.getfromID();
	        		chatArea.append("Welcome to SSChat!\n");
	        		chatArea.append("Your username is: "+userName+"\n");
	        		chatArea.append("Your ID is: ["+userID+"]\n");
	        		uList = data.getUserList();
	        		updateUserListDisplay(uList);

					break;
				case 3://terminate connection
					isConnected=false;
					chatArea.append("Received Terminating signal from Server.\n");
					break;
				case 4: //receive new connected user info
					chatArea.append(data.getfromName()+"["+data.getfromID()+"] is online.\n");
					User u = new User(data.getfromName(),data.getfromID());
					uList.add(u);
	        		updateUserListDisplay(uList);

					break;
				case 5: //receive new disconnected user info
					chatArea.append(data.getfromName()+"["+data.getfromID()+"] is offline.\n");
					int index=-1;
					for(int i=0; i< uList.size();i++){
						if((uList.get(i).getUserID())==data.getfromID()){
							index=i;
						}
					}
					uList.remove(index);
	        		updateUserListDisplay(uList);
					break;
				default:
					chatArea.append("Something unexpected has happened. Please Reboot.\n");	
					break;
			}
		}
		
		/***
		 * Updates UserList in the Client (for all clients)
		 * @param newName
		 * @param id
		 * @param idx
		 */
		private void updateUserListClient(String newName, int id){
			
			String oldName="";
			for(int i=0; i<uList.size();i++){
				if(uList.get(i).getUserID() == id){
					//chatArea.append("Found Match: "+i+"\n");
					oldName = uList.get(i).getUserName();
					uList.get(i).setUserName(newName);
				}
			}
			if(id == userID)
				chatArea.append("You changed name to ("+newName+")\n");
			else
				chatArea.append("User ("+oldName+") changed name to ("+newName+")\n");
			
		}
		
		
		
		
		private void updateUserListDisplay(LinkedList<User> list){
			userNum=list.size();
			numUserField.setText("("+userNum+")");
			userListModel.removeAllElements();
			userListModel.addElement("All");
			//Add elements in 
			for(User a : list) {
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
	}
	
	/**
	 * Displays message to chatArea
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