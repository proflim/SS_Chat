package server;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PortConfig extends JFrame{
	
	JPanel Panel_Main = new JPanel();
	JTextField PortNumField;
	JButton doneButton = new JButton("Done");
	
	public PortConfig(){
		setTitle("Port Configuration");
		setSize(300,80);
		setLocation(500,200);
		
		Panel_Main.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		Panel_Main.add(new JLabel(" Select Port: "));
		
		PortNumField = new JTextField();
		PortNumField.setPreferredSize(new Dimension(90,20));
		Panel_Main.add(PortNumField);
		Panel_Main.add(doneButton);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}
	
	

}
