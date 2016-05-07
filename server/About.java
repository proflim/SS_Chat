package server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * About GUI class
 * @author ÀÓ¼º¼ö
 *
 */
public class About extends JFrame{
	
	JPanel Panel_Main = new JPanel();
	JPanel Panel_Bot = new JPanel();
	JTextArea InfoField;
	JButton okButton = new JButton("Ok");
	
	/**
	 * About GUI Constructor
	 */
	public About(){
		setTitle("About SSChat");
		setSize(300,220);
		setLocation(500,200);
		
		Panel_Main.setLayout(new BorderLayout());
		Panel_Main.add(new JLabel(" What is SSChat? "),BorderLayout.NORTH);
		
		InfoField = new JTextArea(
				"SSChat is an Anonymous Online Chat Application "
				+ "\nthat links users around the world together,"
				+ "\n 'To make the world a better place.'"
				+ "\n\n It is developed by an awesome programmer,"
				+ "\n Sungsu Lim."
				+ "\n May 2016");
		InfoField.setEditable(false);
		InfoField.setPreferredSize(new Dimension(300,100));
		
		Panel_Main.add(InfoField, BorderLayout.CENTER);
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
