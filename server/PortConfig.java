package server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.Server_Main;

public class PortConfig extends JDialog implements ActionListener{
	
	JPanel Panel_Main = new JPanel();
	JTextField PortNumField;
	JButton doneButton = new JButton("Done");
	JTextArea jta;
	public PortConfig(JTextArea jta){
		this.jta = jta;
		
		setTitle("Port Configuration");
		setSize(300,80);
		setLocation(500,200);
		
		Panel_Main.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		Panel_Main.add(new JLabel(" Select Port: "));
		
		PortNumField = new JTextField();
		PortNumField.setPreferredSize(new Dimension(90,20));
		Panel_Main.add(PortNumField);
		doneButton.addActionListener(this);
		Panel_Main.add(doneButton);
		getContentPane().add(Panel_Main);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		
		//If done is pressed, updates portNum value (if it is a valid value)
		if(e.getSource() == doneButton){
			if(checkValidNum(PortNumField.getText())){
				int num = Integer.parseInt(PortNumField.getText());
				Server_Main.setPortNum(num);
				jta.append("Server Port changed to: "+Server_Main.getPortNum()+"\n");
				dispose();
			}
			else{
				//TODO: Output error message and prompt to input again.
				JOptionPane.showMessageDialog(this, "Input Error",
						"Input correct Port", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	//Validity Check on modified PortNumField: Checks if the value is an integer
	private boolean checkValidNum( String text ){
		return text.matches("\\d*");
		
	}
	
	
	

}
