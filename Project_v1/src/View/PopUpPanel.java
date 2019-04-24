package View;

import javax.swing.*;
import Model.User;
import java.awt.event.*;

public class PopUpPanel extends JPanel{
	private JButton serverButton;
    private JButton clientButton;
    private JTextField portcodeBar;
    private JTextField userIPBarInput;
    private JTextField userNameInput;
    private JLabel enterPortcode;
    private JLabel enterIP;
    private JLabel enterUserName;
	
    public PopUpPanel(ActionListener myChatFrame) {
    	
    	setLayout(null);
    	setSize(700,700);
    	
    	serverButton=new JButton("Server");
    	clientButton= new JButton("Client");
    	enterPortcode= new JLabel("Write the port code");
    	portcodeBar=new JTextField();
    	enterIP=new JLabel("Write the IP-adress");
    	userIPBarInput= new JTextField();
    	enterUserName=new JLabel("What is your username?");
    	userNameInput= new JTextField();
    	
    	
    	enterUserName.setBounds(130,30,200,30);
    	userNameInput.setBounds(130,60,130,30);
    	enterPortcode.setBounds(60,90,120,30);
    	portcodeBar.setBounds(60,120,130,30);
    	
    	enterIP.setBounds(200, 90,120,30);
    	userIPBarInput.setBounds(200,120,130,30);
    	
    	
    	clientButton.setBounds(240,190,90,30);  
    	serverButton.setBounds(100, 190, 90,30);
    
    	serverButton.addActionListener(myChatFrame);
       	clientButton.addActionListener(myChatFrame);
           		
    	  
     	add(serverButton);
        add(clientButton);
        add(portcodeBar);
        add(userIPBarInput);
        add(userNameInput);
        add(enterUserName);
        add(enterPortcode);
        add(enterIP);
        
        	
    	
    }
    
    public Object getClientButton() {
    	return clientButton;
    }
    
    public Object getServerButton() {
    	return serverButton;
    }
    
    public String getUserName() {
    	return userNameInput.getText();
    }
    
    public String getIP() {
    	return userIPBarInput.getText();
    }
    
    public int getPortcode() {
    	return Integer.parseInt(portcodeBar.getText());
    }
    

}
