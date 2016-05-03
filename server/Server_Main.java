package server;



import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.table.TableModel;

public class Server_Main extends JFrame implements ActionListener{
  // Text area for displaying contents
	private JTextArea jta = new JTextArea();
	private JSplitPane Pane_Top;
	private JSplitPane Pane_Whole;
	
	public static void main(String[] args) {
		new Server_Main();
	}

	public Server_Main() {
		setTitle("SSChat Server");
		setSize(800, 500);
		setVisible(true); // It is necessary to show the frame here!

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setJMenuBar(createMenuBar());
		JLabel Label_OnlineUsers = new JLabel("Online Users");
		JLabel Label_ChatWindow = new JLabel("Chat Window");
		
		JPanel Panel_CurrentUserList = new JPanel();
		Panel_CurrentUserList.setPreferredSize(new Dimension(200, 300));
		Panel_CurrentUserList.setLayout(new BorderLayout());
		Panel_CurrentUserList.add(Label_OnlineUsers, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(Label_ChatWindow);
		Panel_CurrentUserList.add(scrollPane, BorderLayout.CENTER);
		
		JPanel Panel_Chat = new JPanel();
		Panel_Chat.setPreferredSize(new Dimension(300, 300));

		Panel_Chat.setLayout(new BorderLayout());
		Panel_Chat.add(Label_ChatWindow, BorderLayout.NORTH);
		JPanel Panel_Function = new JPanel();
		Panel_Function.setPreferredSize(new Dimension(300, 100));

		
		
		Pane_Top = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, Panel_CurrentUserList, Panel_Chat);
		Pane_Whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, Pane_Top, Panel_Function);
		getContentPane().add(Pane_Whole);

		// Place text area on the frame
		  
		  /*
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("Server");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true); // It is necessary to show the frame here!
		
		try {
		  // Create a server socket
			System.out.println("Heere");
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
	}
	
	JMenuBar createMenuBar() {


		JMenuBar menuBar = new JMenuBar();
		JMenuItem mi;

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
		
		mi = (JMenuItem) Services.add(new JMenuItem("Logout"));	
		mi.setMnemonic('L');
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		
		mi = (JMenuItem) Services.add(new JMenuItem("Exit"));
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