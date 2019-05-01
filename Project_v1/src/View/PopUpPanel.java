package view;

import javax.swing.*;

import controller.User;

import java.awt.Dimension;
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
	
    public PopUpPanel() {
    	
    	BoxLayout panelLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(panelLayout);
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
    	serverButton=new JButton("Host");
    	clientButton= new JButton("Connect");
    	JPanel buttons = new JPanel();
    	BoxLayout buttonLayout = new BoxLayout(buttons, BoxLayout.LINE_AXIS);
    	buttons.setLayout(buttonLayout);
    	buttons.add(clientButton);
    	buttons.add(Box.createHorizontalStrut(5));
    	buttons.add(serverButton);
    	buttons.setAlignmentX(LEFT_ALIGNMENT);
    	buttons.setMaximumSize(buttons.getPreferredSize());
    	
    	enterPortcode= new JLabel("Port code");
    	portcodeBar=new JTextField(15);
    	portcodeBar.setMaximumSize(portcodeBar.getPreferredSize());
    	enterPortcode.setAlignmentX(LEFT_ALIGNMENT);
    	portcodeBar.setAlignmentX(LEFT_ALIGNMENT);
    	
    	enterIP=new JLabel("IP adress");
    	userIPBarInput= new JTextField(15);
    	userIPBarInput.setMaximumSize(userIPBarInput.getPreferredSize());
    	enterIP.setAlignmentX(LEFT_ALIGNMENT);
    	userIPBarInput.setAlignmentX(LEFT_ALIGNMENT);
    	
    	enterUserName=new JLabel("Nickname");
    	userNameInput= new JTextField(15);
    	userNameInput.setMaximumSize(userNameInput.getPreferredSize());
    	enterUserName.setAlignmentX(LEFT_ALIGNMENT);
    	userNameInput.setAlignmentX(LEFT_ALIGNMENT);
        
        add(enterUserName);
        add(userNameInput);
        add(enterPortcode);
        add(portcodeBar);
        add(enterIP);
        add(userIPBarInput);
        
        add(Box.createVerticalStrut(25));
        add(buttons);

        add(Box.createVerticalGlue());
    }
    
    public JButton getClientButton() {
    	return clientButton;
    }
    
    public JButton getServerButton() {
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
