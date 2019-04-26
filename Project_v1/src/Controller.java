import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.JColorChooser;

import Model.*;
import View.ChatFrame;



public class Controller 
	extends WindowAdapter 
	implements ActionListener {
	private User myUser;
	private ChatFrame myFrame;
	
	Controller() {
		try {
			myFrame = new View.ChatFrame();
			myFrame.addWindowListener(this);
			
			myFrame.getPopUpPanel()
				.getClientButton()
				.addActionListener((ActionListener)this);
			
			myFrame.getPopUpPanel()
				.getServerButton()
				.addActionListener((ActionListener)this);
			
			myFrame.getChatPanel()
				.getCloseButton()
				.addActionListener((ActionListener)this);
			
			myUser = new Model.User("DefaultUser","BLACK",myFrame.getChatPanel());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Controller myController = new Controller();
	}

	@Override
	public void windowClosing(WindowEvent e) {
		myUser.closeConnection();
		System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//	If Client button was pressed, read the user
		//	entered text from the relevant lines and
		//	try to connect.
		if(e.getSource() == myFrame.getPopUpPanel().getClientButton()) {
			String userName = myFrame.getPopUpPanel().getUserName();
			String IP = myFrame.getPopUpPanel().getIP();
			int portCode = myFrame.getPopUpPanel().getPortcode();
			
			myUser.setUsername(userName);
			System.out.println("connect to: "+userName);
			myUser.connectTo(IP, portCode);
			
			myFrame.getChatPanel().setInformationBar(IP, portCode);
			myFrame.remove(myFrame.getPopUpPanel());
			myFrame.add(myFrame.getChatPanel());
		}
		
		//	If Server button was pressed, read the user
		//	entered text and try to set up a server.
		else if(e.getSource() == myFrame.getPopUpPanel().getServerButton()) {
			String userName = myFrame.getPopUpPanel().getUserName();
			int portCode = myFrame.getPopUpPanel().getPortcode();
			
			myUser.setUsername(userName);
			System.out.println("host name "+userName+" at port "+portCode);
			myUser.hostOnce(portCode);
			
			myFrame.getChatPanel().setInformationBar("Host. ", portCode);
			myFrame.remove(myFrame.getPopUpPanel());
			myFrame.add(myFrame.getChatPanel());
			myFrame.pack();
		}
		
		//	Chat window: Close down the current chat session.
		else if(e.getSource()==myFrame.getChatPanel().getCloseButton()) {
			String logOutMessage = Model.Message.createXML(
				"System",
				"RED",
				myUser.getUsername()+" has logged out."
			);
        	myUser.sendMessage(logOutMessage); 
        	myFrame.getChatPanel().displayMessage(
        			Model.Message.readXML(logOutMessage).get());
        	
        	myUser.closeConnection(); 
        	myFrame.remove(myFrame.getChatPanel());
        	myFrame.add(myFrame.getPopUpPanel());
        	myFrame.pack();
		}
		
	}

}
