package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ClientHowTo extends JDialog{
	JPanel Panel_Main = new JPanel();
	JPanel Panel_Bot = new JPanel();
	private JScrollPane Pane_InfoField;

	JTextArea InfoField;
	JButton okButton = new JButton("Ok");
	
	/**
	 * Constructs ClientHowTo GUI
	 */
	public ClientHowTo(){
		setTitle("How to..");
		setSize(400,500);
		setLocation(450,80);
		
		Panel_Main.setLayout(new BorderLayout());
		Panel_Main.add(new JLabel("How to use the Client",SwingConstants.CENTER),BorderLayout.NORTH);
		
		InfoField = new JTextArea(
				"\n\n1. Set Server IP Address and Port Number.\n"
				+ "  (Default IP Address: 127.0.0.1, Port Num: 8888)\n"
				+ "2. Enter your user name (Default is hkuster).\n"
				+ "3. Click Connect/Disconnect to connect/disconnect to Server.\n"
				+ "4. Online Users (including yourself) are shown on the left.\n"
				+ "  Chat with one User or All by selecting the userList box.\n"
				+ "  Next, type the message and click Send."
				+ "\n You may change your user name while connected,\n"
				+ "but DO NOT change IPAddress and Port Number while connected.\n\n"
				+ "For more information, please email sslim@connect.ust.hk"
				+ "\n May 2016");
		InfoField.setEditable(false);
		InfoField.setPreferredSize(new Dimension(300,100));
		Pane_InfoField= new JScrollPane(InfoField);
		Panel_Main.add(Pane_InfoField, BorderLayout.CENTER);
		Panel_Bot.setLayout(new FlowLayout(FlowLayout.CENTER));
		okButton.setPreferredSize(new Dimension(50,30));
		okButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		Panel_Bot.add(okButton);
		Panel_Main.add(Panel_Bot, BorderLayout.SOUTH);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}
}
