package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class ClientHowTo extends JFrame{
	JPanel Panel_Main = new JPanel();
	JPanel Panel_Bot = new JPanel();
	private JScrollPane Pane_InfoField;

	JTextArea InfoField;
	JButton okButton = new JButton("Ok");
	
	public ClientHowTo(){
		setTitle("How to..");
		setSize(400,500);
		setLocation(450,80);
		
		Panel_Main.setLayout(new BorderLayout());
		Panel_Main.add(new JLabel("How to use the Client",SwingConstants.CENTER),BorderLayout.NORTH);
		
		InfoField = new JTextArea(
				"Blah Blah"
				+ "\n May 2016");
		InfoField.setEditable(false);
		InfoField.setPreferredSize(new Dimension(300,100));
		Pane_InfoField= new JScrollPane(InfoField);
		Panel_Main.add(Pane_InfoField, BorderLayout.CENTER);
		Panel_Bot.setLayout(new FlowLayout(FlowLayout.CENTER));
		okButton.setPreferredSize(new Dimension(50,30));
		Panel_Bot.add(okButton);
		Panel_Main.add(Panel_Bot, BorderLayout.SOUTH);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}
}
