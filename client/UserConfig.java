package client;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class UserConfig extends JDialog{
	
	private JPanel Panel_Main = new JPanel();
	JTextField nameField;
	JButton doneButton = new JButton("Done");
	private JTextArea chatArea;
	private String newName;
	
	
	public UserConfig(JTextArea jta, String name){
		this.chatArea = jta;
		
		setTitle("Username Configuration");
		setSize(300,120);
		setLocation(500,200);
		
		Panel_Main.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		Panel_Main.add(new JLabel(" Type new username: "));
		
		nameField = new JTextField(name);
		nameField.setPreferredSize(new Dimension(90,20));
		Panel_Main.add(nameField);
		//doneButton.addActionListener(this);
		Panel_Main.add(doneButton);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}
/*
	public void actionPerformed(ActionEvent e) {
		
		//If done is pressed, updates portNum value (if it is a valid value)
		if(e.getSource() == doneButton){
			newName = nameField.getText();
			chatArea.append("Changed userName to: "+newName+"\n");
			dispose();
		}
		
	}
	*/
	
	//Validity Check on modified PortNumField: Checks if the value is an integer
	private boolean checkValidNum( String text ){
		return text.matches("\\d*");
		
	}
	
	String getNewName(){
		return newName;
	}

}
