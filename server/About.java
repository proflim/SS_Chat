package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class About extends JFrame{
	
	JPanel Panel_Main = new JPanel();
	JPanel Panel_Bot = new JPanel();
	JTextArea InfoField;
	JButton okButton = new JButton("Ok");
	
	public About(){
		setTitle("About SSChat");
		setSize(280,220);
		setLocation(500,200);
		
		Panel_Main.setLayout(new BorderLayout());
		Panel_Main.add(new JLabel(" What is SSChat? "),BorderLayout.NORTH);
		
		InfoField = new JTextArea(
				"SSChat is an Online Chat Application "
				+ "\nthat links users around the world together,"
				+ "\n 'To make the world a better place.'"
				+ "\n\n It is developed by the brilliant programmer,"
				+ "\n Sungsu Lim."
				+ "\n May 2016");
		InfoField.setEditable(false);
		InfoField.setPreferredSize(new Dimension(300,100));
		
		Panel_Main.add(InfoField, BorderLayout.CENTER);
		Panel_Bot.setLayout(new FlowLayout(FlowLayout.CENTER));
		okButton.setPreferredSize(new Dimension(50,30));
		Panel_Bot.add(okButton);
		Panel_Main.add(Panel_Bot, BorderLayout.SOUTH);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}
}
