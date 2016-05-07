package server;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * ServerHowTo GUI
 * @author ÀÓ¼º¼ö
 *
 */
public class ServerHowTo extends JDialog{
	
	JPanel Panel_Main = new JPanel();
	JPanel Panel_Bot = new JPanel();
	private JScrollPane Pane_InfoField;

	JTextArea InfoField;
	JButton okButton = new JButton("Ok");
	
	/**
	 * Constructor for ServerHowTo GUI
	 */
	public ServerHowTo(){
		setTitle("How to..");
		setSize(400,500);
		setLocation(450,80);
		
		Panel_Main.setLayout(new BorderLayout());
		Panel_Main.add(new JLabel("How to operate the Server",SwingConstants.CENTER),BorderLayout.NORTH);
		
		InfoField = new JTextArea(
				"\n\n1. Set Port Number to open server on.\n"
				+ "  (Default Port Number is 8888)\n"
				+ "2. Click Start/End Service Button to start/end service.\n"
				+ "3. Online Users will be shown on the left.\n"
				+ "4. All messages going to and from the server will be displayed.\n"
				+ "5. You can also send Admin messages to individual user or to All.\n"
				+ "6. Select the User or All by selecting the appropriate userList box.\n"
				+ "  Next, type the message and click Send.\n\n"
				+ "Please be advised that it is unwise to change portNum \nduring connection.\n"
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
