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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ConnectionConfig extends JDialog implements ActionListener{
	
	private JPanel Panel_Top = new JPanel();
	private JPanel Panel_Center = new JPanel();
	private JPanel Panel_Bot = new JPanel();
	
	private JTextField PortNumField;
	private JTextField IPAddressField;
	private JButton doneButton = new JButton("Done");
	private JTextArea chatArea;
	
	
	public ConnectionConfig(JTextArea jta, int portnum, String ipaddress){
		this.chatArea = jta;
		
		setTitle("Connection Configuration");
		setSize(330,180);
		setLocation(500,200);
		
		Panel_Top.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		Panel_Top.add(new JLabel(" Select Port: "));
		PortNumField = new JTextField(Integer.toString(portnum));
		PortNumField.setPreferredSize(new Dimension(90,20));
		Panel_Top.add(PortNumField);
		
		Panel_Center.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		Panel_Center.add(new JLabel(" Select IP Address: "));
		IPAddressField = new JTextField(ipaddress);
		IPAddressField.setPreferredSize(new Dimension(90,20));
		Panel_Center.add(IPAddressField);
		
		Panel_Bot.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		doneButton.addActionListener(this);
		Panel_Bot.add(doneButton);
		
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(Panel_Top,BorderLayout.NORTH);
		getContentPane().add(Panel_Center,BorderLayout.CENTER);
		getContentPane().add(Panel_Bot,BorderLayout.SOUTH);
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		
		//If done is pressed, updates portNum value (if it is a valid value)
		if(e.getSource() == doneButton){
			if(checkValidNum(PortNumField.getText())){
				int num = Integer.parseInt(PortNumField.getText());
				Client_Main.setPortNum(num);
				Client_Main.setIPAddress(IPAddressField.getText());
				chatArea.append("Server Port changed to: "+num+"\n");
				chatArea.append("IP Address changed to: "+IPAddressField.getText()+"\n");

				dispose();
			}
			else{
				//TODO: Output error message and prompt to input again.
				JOptionPane.showMessageDialog(this, "Input correct Port/IPAddress",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
	//Validity Check on modified PortNumField: Checks if the value is an integer
	private boolean checkValidNum( String text ){
		return text.matches("\\d*");
		
	}
	
	
	

}